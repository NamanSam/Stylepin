import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { fetchBoards, createBoard } from '../api/boardApi.js'
export default function BoardsPage() {
  const [resource, setResource] = useState(null), [page, setPage] = useState(0)
  const [error, setError] = useState(''), [busy, setBusy] = useState(false), [revision, setRevision] = useState(0)
  const resourceKey = page + ':' + revision
  const data = resource?.key === resourceKey ? resource.data : null
  useEffect(() => { let ignore = false; fetchBoards(page).then(result => { if (!ignore) setResource({ key: resourceKey, data: result }) }).catch(e => { if (!ignore) setError(e.message) }); return () => { ignore = true } }, [page, resourceKey])
  async function create(event) {
    event.preventDefault(); setBusy(true); setError('')
    const form = event.currentTarget
    try { await createBoard(Object.fromEntries(new FormData(form))); form.reset(); setPage(0); setRevision(r => r + 1) }
    catch (e) { setError(e.message) } finally { setBusy(false) }
  }
  return <main className="collection-page"><div className="section-heading"><div><span className="eyebrow">Private / Your point of view</span><h1>Ideas, collected.</h1></div><p>A mood. An occasion. A new direction.</p></div>
    <form className="board-create" onSubmit={create}><label>Board name<input name="name" placeholder="e.g. Streetwear Ideas" maxLength={100} required /></label><label>Description <span>(optional)</span><input name="description" placeholder="What inspires this collection?" maxLength={1000} /></label><button className="solid-button" disabled={busy}>{busy ? 'Creating…' : 'Create board +'}</button></form>
    {error && <p role="alert" className="form-error">{error}</p>}
    {!data && !error && <p role="status">Loading your boards…</p>}
    <div className="boards-grid">{data?.items.map((board, index) => <Link className="board-card" to={'/boards/' + board.id} key={board.id}><div className="board-card-cover"><span>{String(page * 24 + index + 1).padStart(2, '0')}</span><h2>{board.name}</h2><span aria-hidden="true">↗</span></div><p>{board.description || 'A personal collection of looks.'}</p><span className="eyebrow">Private board</span></Link>)}</div>
    {data?.totalElements === 0 && <p className="collection-empty">Your first collection starts with a name.</p>}
    {data && data.totalPages > 1 && <div className="pagination"><button disabled={!page} onClick={() => setPage(p => p - 1)}>Previous</button><span>{page + 1} / {data.totalPages}</span><button disabled={page + 1 >= data.totalPages} onClick={() => setPage(p => p + 1)}>Next</button></div>}
  </main>
}
