import { useEffect, useState } from 'react'
import OutfitCard from './components/OutfitCard.jsx'
import { fetchOutfits } from './api/outfitApi.js'

function App() {
  const [outfits, setOutfits] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    let ignore = false

    async function loadOutfits() {
      try {
        setLoading(true)
        setError(null)
        const data = await fetchOutfits()
        if (!ignore) {
          setOutfits(data)
        }
      } catch (err) {
        if (!ignore) {
          setError(err.message || 'Failed to load outfits')
        }
      } finally {
        if (!ignore) {
          setLoading(false)
        }
      }
    }

    loadOutfits()

    return () => {
      ignore = true
    }
  }, [])

  return (
    <div className="page">
      <header className="home">
        <h1>StylePin</h1>
        <p>Discover Your Style</p>
      </header>

      <main className="feed">
        {loading && (
          <div className="feed-status" role="status">
            <p>Loading curated styles...</p>
          </div>
        )}

        {!loading && error && (
          <div className="feed-status feed-status--error" role="alert">
            <p>{error}</p>
            <p className="feed-status__subtext">Please ensure the backend is running at http://localhost:8080.</p>
          </div>
        )}

        {!loading && !error && outfits.length === 0 && (
          <div className="feed-status feed-status--empty">
            <p>No outfits found.</p>
          </div>
        )}

        {!loading &&
          !error &&
          outfits.map((outfit) => (
            <OutfitCard
              key={outfit.id}
              imageUrl={outfit.imageUrl}
              title={outfit.title}
              category={outfit.category}
            />
          ))}
      </main>
    </div>
  )
}

export default App