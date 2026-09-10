import { useMemo } from 'react'
import MasonryFeed from '../components/feed/MasonryFeed.jsx'
import FeedSkeleton from '../components/feed/FeedSkeleton.jsx'

function FeedPage({ outfits, loading, error, usedDev, activeCategory, searchQuery }) {
  const filteredOutfits = useMemo(() => {
    let result = outfits

    if (activeCategory !== 'For You') {
      const catLower = activeCategory.toLowerCase()
      result = result.filter((o) => {
        const cat = (o.category || '').toLowerCase()
        return cat.includes(catLower)
      })
    }

    const q = searchQuery.trim().toLowerCase()
    if (q) {
      result = result.filter((o) => {
        const title = (o.title || '').toLowerCase()
        const cat = (o.category || '').toLowerCase()
        const tags = (o.tags || []).join(' ').toLowerCase()
        return title.includes(q) || cat.includes(q) || tags.includes(q)
      })
    }

    return result
  }, [outfits, activeCategory, searchQuery])

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
      <div className="feed-filter-wrapper" key={`${activeCategory}-${searchQuery}`}>
        {filteredOutfits.length > 0 ? (
          <MasonryFeed outfits={filteredOutfits} searchQuery={searchQuery} />
        ) : (
          <div className="feed-empty-state" role="status">
            <div className="feed-empty-state__icon" aria-hidden="true">
              <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.2" strokeLinecap="round" strokeLinejoin="round">
                <circle cx="11" cy="11" r="8" />
                <path d="M21 21l-4.35-4.35" />
                <path d="M8 11h6" />
              </svg>
            </div>
            <p className="feed-empty-state__title">No outfits found</p>
            <p className="feed-empty-state__subtext">
              {searchQuery
                ? `No results for "${searchQuery}" in ${activeCategory === 'For You' ? 'all categories' : activeCategory}`
                : `No outfits in the ${activeCategory} category yet`}
            </p>
          </div>
        )}
      </div>
    </>
  )
}

export default FeedPage
