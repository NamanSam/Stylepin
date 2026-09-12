import { useMemo } from 'react'
import MasonryFeed from '../components/feed/MasonryFeed.jsx'
import FeedSkeleton from '../components/feed/FeedSkeleton.jsx'
import EditorialHome from '../components/editorial/EditorialHome.jsx'
import CategoryNav from '../components/navigation/CategoryNav.jsx'
import ExploreCanvas from '../components/explore/ExploreCanvas.jsx'
export default function FeedPage({ outfits, loading, error, usedDev, activeCategory, onCategoryChange, searchQuery, onRetry, retrying }) {
  const filtered = useMemo(() => outfits.filter(o => {
    const category = activeCategory === 'For You' || (o.category || '').toLowerCase().includes(activeCategory.toLowerCase())
    const text = [o.title, o.category, ...(o.tags || [])].join(' ').toLowerCase()
    return category && text.includes(searchQuery.trim().toLowerCase())
  }), [outfits, activeCategory, searchQuery])
  const searching = !!searchQuery.trim() || activeCategory !== 'For You'
  if (loading) return <main className="collection-page"><p role="status">Preparing the edit…</p><FeedSkeleton /></main>
  if (error) return <main className="collection-page" role="alert"><h1>The edit is taking a moment.</h1><p>{error}</p></main>
  return <main>
    {usedDev && <p className="preview-notice" role="status">Campaign preview · Includes AI-created imagery · Live outfits unavailable.</p>}
    {!searching && <><ExploreCanvas outfits={outfits} usedDev={usedDev} /><EditorialHome outfits={outfits} onCategory={onCategoryChange} /></>}
    <section id="discover" className="editorial-section discover-section"><div className="section-heading"><div><span className="eyebrow">Discover / Your next point of view</span><h2>{searching ? 'Find your inspiration.' : 'Looks worth a closer look.'}</h2></div><span role="status" aria-live="polite">{filtered.length} {usedDev ? 'preview studies' : 'looks'}</span></div>
      {usedDev && <div className="discovery-availability"><p>Featured campaign preview. The full outfit collection is currently unavailable.</p><button className="text-button" onClick={onRetry} disabled={retrying}>{retrying ? 'Connecting…' : 'Retry live looks ↗'}</button></div>}
      <CategoryNav activeCategory={activeCategory} onCategoryChange={onCategoryChange} />
      {filtered.length ? <MasonryFeed outfits={filtered} searchQuery={searchQuery} showInserts={false} /> :
        <div className="collection-empty"><h3>No looks found.</h3><p>Try another search or category.</p><button onClick={() => onCategoryChange('For You')} className="text-link">View all categories</button></div>}
    </section>
    {!searching && <EditorialHome outfits={outfits} onCategory={onCategoryChange} part="collections" />}
  </main>
}
