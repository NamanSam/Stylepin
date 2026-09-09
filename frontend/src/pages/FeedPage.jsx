import { useEffect, useState } from 'react'
import MasonryFeed from '../components/feed/MasonryFeed.jsx'
import FeedSkeleton from '../components/feed/FeedSkeleton.jsx'
import { fetchOutfits } from '../api/outfitApi.js'

function FeedPage() {
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
            const mod = await import('../dev/demoFashionFeed.js')
            setOutfits(mod.default)
            setUsedDev(true)
          }
        }
      } catch {
        if (!ignore) {
          try {
            const mod = await import('../dev/demoFashionFeed.js')
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

    return () => {
      ignore = true
    }
  }, [])

  if (loading) {
    return <FeedSkeleton />
  }

  if (error) {
    return (
      <div className="feed-status feed-status--error" role="alert">
        <p>{error}</p>
        <p className="feed-status__subtext">Please ensure the backend is running at http://localhost:8080.</p>
      </div>
    )
  }

  if (outfits.length === 0) {
    return (
      <div className="feed-status feed-status--empty">
        <p>No outfits found.</p>
      </div>
    )
  }

  return (
    <>
      {usedDev && (
        <div className="feed-dev-banner" role="status">
          <span className="feed-dev-banner__dot" aria-hidden="true" />
          Development mode — using demo feed data
        </div>
      )}
      <div className="feed-intro">
        <h2 className="feed-intro__heading">Your Daily Style Feed</h2>
      </div>
      <MasonryFeed outfits={outfits} />
    </>
  )
}

export default FeedPage
