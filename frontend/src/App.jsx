import { BrowserRouter, Routes, Route } from 'react-router-dom'
import { useState, useEffect } from 'react'
import Sidebar from './components/layout/Sidebar.jsx'
import DiscoveryHeader from './components/layout/DiscoveryHeader.jsx'
import CategoryNav from './components/navigation/CategoryNav.jsx'
import FeedPage from './pages/FeedPage.jsx'
import OutfitDetailPage from './pages/OutfitDetailPage.jsx'
import { fetchOutfits } from './api/outfitApi.js'
import useTheme from './hooks/useTheme.js'

function App() {
  const { theme, toggleTheme } = useTheme()
  const [activeCategory, setActiveCategory] = useState('For You')
  const [searchQuery, setSearchQuery] = useState('')
  const [outfits, setOutfits] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [usedDev, setUsedDev] = useState(false)

  useEffect(() => {
    let ignore = false

    async function loadOutfits() {
      try {
        setLoading(true)
        setError(null)
        const data = await fetchOutfits()
        if (!ignore) {
          if (data && data.length > 0) {
            setOutfits(data)
          } else {
            const mod = await import('./dev/demoFashionFeed.js')
            setOutfits(mod.default)
            setUsedDev(true)
          }
        }
      } catch {
        if (!ignore) {
          try {
            const mod = await import('./dev/demoFashionFeed.js')
            setOutfits(mod.default)
            setUsedDev(true)
          } catch {
            setError('Failed to load outfits')
          }
        }
      } finally {
        if (!ignore) {
          setLoading(false)
        }
      }
    }

    loadOutfits()

    return () => { ignore = true }
  }, [])

  return (
    <BrowserRouter>
      <Routes>
        <Route
          path="/"
          element={
            <div className="app-shell">
              <Sidebar />
              <div className="app-main">
                <DiscoveryHeader
                  searchQuery={searchQuery}
                  onSearchChange={setSearchQuery}
                  theme={theme}
                  onToggleTheme={toggleTheme}
                />
                <CategoryNav
                  activeCategory={activeCategory}
                  onCategoryChange={setActiveCategory}
                />
                <FeedPage
                  outfits={outfits}
                  loading={loading}
                  error={error}
                  usedDev={usedDev}
                  activeCategory={activeCategory}
                  searchQuery={searchQuery}
                />
              </div>
            </div>
          }
        />
        <Route path="/outfits/:id" element={<OutfitDetailPage />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App
