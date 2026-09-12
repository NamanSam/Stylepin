import { useEffect, useState } from 'react'
import { useParams, useLocation, Link } from 'react-router-dom'
import { fetchOutfitById, fetchOutfits } from '../api/outfitApi.js'
import LookBreakdown from '../components/look/LookBreakdown.jsx'
import '../components/look/look.css'

export default function OutfitDetailPage() {
  const { id } = useParams()
  const location = useLocation()
  const back = location.state?.fromExplore === '/explore' ? '/explore' : '/#discover'
  const [result, setResult] = useState(null)
  const [catalog, setCatalog] = useState({ outfits: [], loading: true, error: false })
  useEffect(() => {
    let ignore = false
    fetchOutfits().then(outfits => {
      if (!ignore) setCatalog({ outfits: Array.isArray(outfits) ? outfits : [], loading: false, error: false })
    }).catch(() => { if (!ignore) setCatalog({ outfits: [], loading: false, error: true }) })
    return () => { ignore = true }
  }, [])
  useEffect(() => {
    let ignore = false
    fetchOutfitById(id).then(outfit => {
      if (!ignore) setResult({ id, outfit })
    }).catch(error => { if (!ignore) setResult({ id, error }) })
    return () => { ignore = true }
  }, [id])
  const loading = result?.id !== id
  const error = !loading && result?.error
  return <main className="look-page" aria-busy={loading}>
    <nav className="look-nav" aria-label="Breadcrumb"><Link to={back}>← {back === '/explore' ? 'Back to Explore' : 'Back to discovery'}</Link><span>The StylePin edit / Look breakdown</span></nav>
    {loading && <p className="look-loading" role="status">Loading your look…</p>}
    {error ? <div className="look-status" role="alert">
      <p className="look-kicker">{error.status === 404 ? 'Look not found / 404' : 'Something went wrong'}</p>
      <h1>{error.status === 404 ? 'This look is no longer here.' : 'Unable to load this look.'}</h1>
      <p>Please return to discovery and try another look.</p><Link to="/">Back to discovery ↗</Link>
    </div> : result?.outfit ? <div className={loading ? 'look-pending' : undefined} inert={loading || undefined}>
      <LookBreakdown key={result.id} outfit={result.outfit} catalog={catalog} />
    </div> : <div className="look-skeleton" aria-hidden="true"><div /><div /><div /></div>}
  </main>
}
