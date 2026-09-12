import { BrowserRouter, Routes, Route, useLocation } from 'react-router-dom'
import { useState, useEffect } from 'react'
import DiscoveryHeader from './components/layout/DiscoveryHeader.jsx'
import FeedPage from './pages/FeedPage.jsx'
import ExplorePage from './pages/ExplorePage.jsx'
import OutfitDetailPage from './pages/OutfitDetailPage.jsx'
import AuthPage from './pages/AuthPage.jsx'
import SavedPage from './pages/SavedPage.jsx'
import BoardsPage from './pages/BoardsPage.jsx'
import BoardDetailPage from './pages/BoardDetailPage.jsx'
import AuthProvider from './auth/AuthProvider.jsx'
import ProtectedRoute from './auth/ProtectedRoute.jsx'
import SavedOutfitsProvider from './context/SavedOutfitsProvider.jsx'
import { fetchOutfits } from './api/outfitApi.js'
import useTheme from './hooks/useTheme.js'
import './editorial.css'

function Site() {
  const { theme, toggleTheme } = useTheme()
  const [activeCategory, setActiveCategory] = useState('For You')
  const [searchQuery, setSearchQuery] = useState('')
  const [outfits, setOutfits] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [usedDev, setUsedDev] = useState(false)
  const [revision, setRevision] = useState(0)
  const [retrying, setRetrying] = useState(false)
  function retryOutfits() { setRetrying(true); setRevision(value => value + 1) }
  const location = useLocation()
  useEffect(() => {
    if (!location.hash) window.scrollTo(0,0)
    else {
      const frame = requestAnimationFrame(() => document.getElementById(location.hash.slice(1))?.scrollIntoView())
      return () => cancelAnimationFrame(frame)
    }
  }, [location.pathname, location.hash, loading])
  useEffect(() => {
    let ignore = false
    async function load() {
      try {
        const data = await fetchOutfits()
        if (!data?.length) throw new Error('No live outfits')
        if (!ignore) { setOutfits(data); setUsedDev(false); setError(null) }
      } catch {
        try {
          const mod = await import('./data/editorialPreview.js')
          if (!ignore) { setOutfits(mod.default); setUsedDev(true); setError(null) }
        } catch { if (!ignore) setError('Unable to load outfits. Please try again later.') }
      } finally { if (!ignore) { setLoading(false); setRetrying(false) } }
    }
    load()
    return () => { ignore = true }
  }, [revision])
  return <div className="editorial-app"><a href="#page-content" className="skip-link">Skip to content</a>
    <DiscoveryHeader searchQuery={searchQuery} onSearchChange={setSearchQuery} theme={theme} onToggleTheme={toggleTheme} />
    <div id="page-content" tabIndex={-1}><Routes>
      <Route path="/" element={<FeedPage outfits={outfits} loading={loading} error={error} usedDev={usedDev} activeCategory={activeCategory} onCategoryChange={setActiveCategory} searchQuery={searchQuery} onRetry={retryOutfits} retrying={retrying} />} />
      <Route path="/explore" element={<ExplorePage outfits={outfits} loading={loading} error={error} usedDev={usedDev} onRetry={retryOutfits} retrying={retrying} />} />
      <Route path="/outfits/:id" element={<OutfitDetailPage />} />
      <Route path="/login" element={<AuthPage key="login" />} />
      <Route path="/register" element={<AuthPage key="register" registration />} />
      <Route element={<ProtectedRoute />}>
        <Route path="/saved" element={<SavedPage />} />
        <Route path="/boards" element={<BoardsPage />} />
        <Route path="/boards/:id" element={<BoardDetailPage />} />
      </Route>
      <Route path="*" element={<main className="collection-page"><h1>Page not found.</h1><a className="text-link" href="/">Back to the edit ↗</a></main>} />
    </Routes></div>
    <footer className="editorial-footer"><a href="/" className="footer-wordmark">STYLEPIN</a><div><span>A point of view. A world of possibilities.</span><span>Fashion discovery / Made personal.</span><a href="#page-content">Back to top ↑</a></div></footer>
  </div>
}
export default function App() {
  return <BrowserRouter><AuthProvider><SavedOutfitsProvider><Site /></SavedOutfitsProvider></AuthProvider></BrowserRouter>
}
