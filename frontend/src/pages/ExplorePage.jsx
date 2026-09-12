import { Link } from 'react-router-dom'
import ExploreCanvas from '../components/explore/ExploreCanvas.jsx'

export default function ExplorePage({ outfits, loading, error, usedDev, onRetry, retrying }) {
  return <main className="explore-page"><ExploreCanvas outfits={outfits} loading={loading} error={error} usedDev={usedDev} immersive />
    <section id="explore-looks" className="editorial-section explore-discover"><div><span className="eyebrow">From a feeling to a look</span><h2>Stay curious.<br /><em>Look a little closer.</em></h2></div><div><p>Find a silhouette in motion, or take your time with the full editorial collection.</p><Link className="text-link" to="/#discover">Discover {usedDev || loading ? 'the edit' : `${outfits.length} looks`} <span aria-hidden="true">↗</span></Link>
      {usedDev && <div className="discovery-availability"><p>Campaign previews are inspiration studies. Live looks open their Look Breakdown.</p><button className="text-button" onClick={onRetry} disabled={retrying}>{retrying ? 'Connecting…' : 'Retry live looks ↗'}</button></div>}
    </div>
    </section>
  </main>
}
