import { Link } from 'react-router-dom'
import useSavedOutfits from '../hooks/useSavedOutfits.js'
import MasonryFeed from '../components/feed/MasonryFeed.jsx'
export default function SavedPage() {
  const { outfits, loading, error, reload } = useSavedOutfits()
  return <main className="collection-page"><div className="section-heading"><div><span className="eyebrow">Private / Just for you</span><h1>The looks you keep.</h1></div><span>{outfits.length} saved looks</span></div>
    {loading ? <p role="status">Opening your collection…</p> : error ? <div role="alert"><p>{error}</p><button onClick={reload}>Try again</button></div> : outfits.length ?
      <MasonryFeed outfits={outfits} showInserts={false} /> : <div className="collection-empty"><h2>A little inspiration goes a long way.</h2><p>Save a look to start your personal collection.</p><Link className="text-link" to="/">Explore the edit ↗</Link></div>}
  </main>
}
