import { useMemo, useState } from 'react'
import MasonryFeed from '../components/feed/MasonryFeed.jsx'
import FeedSkeleton from '../components/feed/FeedSkeleton.jsx'
import EditorialHome from '../components/editorial/EditorialHome.jsx'
import CategoryNav from '../components/navigation/CategoryNav.jsx'
export default function FeedPage({ outfits, loading, error, usedDev, activeCategory, onCategoryChange, searchQuery }) {
  const [visible, setVisible] = useState(8)
  const filtered = useMemo(() => outfits.filter(o => {
    const category = activeCategory === 'For You' || (o.category || '').toLowerCase().includes(activeCategory.toLowerCase())
    const text = [o.title, o.category, ...(o.tags || [])].join(' ').toLowerCase()
    return category && text.includes(searchQuery.trim().toLowerCase())
  }), [outfits, activeCategory, searchQuery])
  const searching = !!searchQuery.trim() || activeCategory !== 'For You'
  if (loading) return <main className="collection-page"><p role="status">Preparing the edit…</p><FeedSkeleton /></main>
  if (error) return <main className="collection-page" role="alert"><h1>The edit is taking a moment.</h1><p>{error}</p></main>
  return <main>
    {usedDev && <p className="preview-notice" role="status">Preview · Live outfits unavailable.</p>}
    {!searching && <EditorialHome outfits={outfits} onCategory={onCategoryChange} />}
    <section id="discover" className="editorial-section discover-section"><div className="section-heading"><div><span className="eyebrow">Your next point of view</span><h2>{searching ? 'Find your inspiration.' : 'Looks worth a closer look.'}</h2></div><span>{filtered.length} looks</span></div>
      <CategoryNav activeCategory={activeCategory} onCategoryChange={value => { setVisible(8); onCategoryChange(value) }} />
      {filtered.length ? <><MasonryFeed outfits={filtered.slice(0, searching ? filtered.length : visible)} searchQuery={searchQuery} showInserts={false} />
        {!searching && visible < filtered.length && <div className="discover-more"><button className="outline-button" onClick={() => setVisible(n => n + 8)}>More to discover ↓</button></div>}</> :
        <div className="collection-empty"><h3>No looks found.</h3><p>Try another search or category.</p><button onClick={() => onCategoryChange('For You')} className="text-link">View all categories</button></div>}
    </section>
  </main>
}
