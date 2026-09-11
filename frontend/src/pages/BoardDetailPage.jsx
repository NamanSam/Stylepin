import { useEffect, useState } from 'react'
import { Link, useNavigate, useParams } from 'react-router-dom'
import { fetchBoard, removeFromBoard, deleteBoard } from '../api/boardApi.js'
import MasonryFeed from '../components/feed/MasonryFeed.jsx'
export default function BoardDetailPage() {
  const { id } = useParams(), navigate = useNavigate()
  const [resource, setResource] = useState(null), [page, setPage] = useState(0), [error, setError] = useState('')
  const [revision, setRevision] = useState(0), [confirm, setConfirm] = useState(false), [busy, setBusy] = useState(false)
  const resourceKey = id + ':' + page + ':' + revision
  const data = resource?.key === resourceKey ? resource.data : null
  useEffect(() => { let ignore = false; fetchBoard(id, page).then(result => { if (!ignore) setResource({ key: resourceKey, data: result }) }).catch(e => { if (!ignore) setError(e.message) }); return () => { ignore = true } }, [id, page, resourceKey])
  async function remove(outfitId) { await removeFromBoard(id, outfitId); if (data.outfits.items.length === 1 && page > 0) setPage(p => p - 1); else setRevision(r => r + 1) }
  async function destroy() {
    setBusy(true)
    try { await deleteBoard(id); navigate('/boards') } catch (e) { setError(e.message) } finally { setBusy(false) }
  }
  return <main className="collection-page"><Link to="/boards" className="text-link">← All boards</Link>
    {error && <p role="alert" className="form-error">{error}</p>}
    {!data && !error && <p role="status">Opening this collection…</p>}
    {data && <><div className="section-heading"><div><span className="eyebrow">Private collection</span><h1>{data.board.name}</h1><p>{data.board.description}</p></div><button className="text-button" onClick={() => setConfirm(true)}>Delete board</button></div>
      {confirm && <div className="delete-confirm" role="alert"><p>Delete this board? Its outfits will remain available in discovery and Saved.</p><button onClick={destroy} disabled={busy}>Delete board</button><button onClick={() => setConfirm(false)}>Keep board</button></div>}
      {data.outfits.items.length ? <MasonryFeed outfits={data.outfits.items} showInserts={false} onRemove={remove} /> : <div className="collection-empty"><h2>Make room for inspiration.</h2><p>Choose Board + on a look to add it here.</p><Link to="/" className="text-link">Find your next look ↗</Link></div>}
      {data.outfits.totalPages > 1 && <div className="pagination"><button disabled={!page} onClick={() => setPage(p => p - 1)}>Previous</button><span>{page + 1} / {data.outfits.totalPages}</span><button disabled={page + 1 >= data.outfits.totalPages} onClick={() => setPage(p => p + 1)}>Next</button></div>}
    </>}
  </main>
}
