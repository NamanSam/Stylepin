import { BrowserRouter, Routes, Route } from 'react-router-dom'
import Sidebar from './components/layout/Sidebar.jsx'
import DiscoveryHeader from './components/layout/DiscoveryHeader.jsx'
import CategoryNav from './components/navigation/CategoryNav.jsx'
import FeedPage from './pages/FeedPage.jsx'
import OutfitDetailPage from './pages/OutfitDetailPage.jsx'

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route
          path="/"
          element={
            <div className="app-shell">
              <Sidebar />
              <div className="app-main">
                <DiscoveryHeader />
                <CategoryNav />
                <FeedPage />
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
