import { BrowserRouter, Routes, Route } from 'react-router-dom'
import FeedPage from './pages/FeedPage.jsx'
import OutfitDetailPage from './pages/OutfitDetailPage.jsx'

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<FeedPage />} />
        <Route path="/outfits/:id" element={<OutfitDetailPage />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App