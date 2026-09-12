import { useMemo, useRef, useState } from 'react'
import { Link } from 'react-router-dom'
import ExploreImage from './ExploreImage.jsx'
import useExploreMotion from '../../hooks/useExploreMotion.js'
import { EXPLORE_SLOTS, exploreOutfits } from '../../utils/exploreLayout.js'
import './explore.css'

function ImageField({ outfits, paused }) {
  const scene = useRef(null)
  const [offsets, setOffsets] = useState({})
  const pool = useMemo(() => exploreOutfits(outfits), [outfits])
  useExploreMotion(scene, paused, index => setOffsets(previous => ({ ...previous, [index]: (previous[index] || 0) + 18 })))
  return <div ref={scene} className="explore-field" aria-label="Fashion image field">
    {pool.length > 0 && EXPLORE_SLOTS.slice(0, Math.min(18, pool.length)).map((slot, index) => <ExploreImage key={index} slot={slot} index={index} outfit={pool[(index + (offsets[index] || 0)) % pool.length]} />)}
  </div>
}

export default function ExploreCanvas({ outfits, immersive = false, loading = false, error = null, usedDev = false }) {
  const [paused, setPaused] = useState(false)
  return <section className={`explore-campaign ${immersive ? 'explore-campaign--immersive' : ''}`} aria-labelledby="explore-heading">
    <ImageField key={outfits.map(outfit => outfit.id).join(',')} outfits={outfits} paused={paused} />
    <div className="explore-vignette" aria-hidden="true" />
    <div className="explore-edition"><span>STYLEPIN / IN MOTION</span><span>A study in personal style — 01</span></div>
    <div className="explore-copy"><p className="eyebrow">The art of getting dressed</p><h1 id="explore-heading">Explore styles<br /><em>differently.</em></h1><p>Unexpected silhouettes. A point of view that is yours.</p>
      {!immersive && <Link className="explore-enter" to="/explore">Enter Explore <span>↗</span></Link>}
      {loading && <p role="status">Preparing your inspiration…</p>}
      {error && <p role="alert">The looks are unavailable right now. Please try again later.</p>}
      {!loading && !error && !outfits.length && <p>No looks available yet.</p>}
    </div>
    <div className="explore-bottom"><span>{usedDev ? 'Campaign preview / Includes AI imagery' : 'Fashion, from your point of view'}</span><a href={immersive ? '#explore-looks' : '#discover'} className="explore-scroll"><span aria-hidden="true">↓</span>Scroll to discover</a><button className="explore-pause" aria-pressed={paused} onClick={() => setPaused(value => !value)}>{paused ? 'Play motion' : 'Pause motion'} <span aria-hidden="true">{paused ? '▷' : 'Ⅱ'}</span></button></div>
  </section>
}
