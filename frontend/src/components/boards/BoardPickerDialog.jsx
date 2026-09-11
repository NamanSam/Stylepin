import { useEffect, useRef, useState } from 'react'
import { fetchBoards, addToBoard, createBoard } from '../../api/boardApi.js'
export default function BoardPickerDialog({ outfit, onClose }) {
  const dialog = useRef(null)
  const [boards, setBoards] = useState([])
  const [error, setError] = useState('')
  const [busy, setBusy] = useState(false)
  const [loading, setLoading] = useState(true)
  const [added, setAdded] = useState(null)
  useEffect(() => {
    dialog.current.showModal()
    let ignore = false
    async function load() {
      try {
        let page = 0, result, all = []
        do { result = await fetchBoards(page++); all = all.concat(result.items) } while (!ignore && page < result.totalPages)
        if (!ignore) setBoards(all)
      } catch (e) { if (!ignore) setError(e.message) } finally { if (!ignore) setLoading(false) }
    }
    load()
    return () => { ignore = true }
  }, [])
  async function add(id) {
    setBusy(true); setError('')
    try { await addToBoard(id, outfit.id); setAdded(id) } catch (e) { setError(e.message) } finally { setBusy(false) }
  }
  async function create(event) {
    event.preventDefault(); setBusy(true); setError('')
    const name = new FormData(event.currentTarget).get('name')
    try { const board = await createBoard({ name }); setBoards(previous => [board, ...previous]); await addToBoard(board.id, outfit.id); setAdded(board.id) }
    catch (e) { setError(e.message) } finally { setBusy(false) }
  }
  return <dialog ref={dialog} className="board-dialog" onCancel={onClose} aria-labelledby="board-picker-title">
    <button className="dialog-close" onClick={onClose} aria-label="Close board picker">×</button>
    <span className="eyebrow">Your private collections</span><h2 id="board-picker-title">A place for this look.</h2>
    <p>{outfit.title}</p>
    {loading ? <p role="status">Loading boards…</p> : <div className="board-picker-list">{boards.map(board =>
      <button key={board.id} onClick={() => add(board.id)} disabled={busy || added === board.id}>{board.name}<span>{added === board.id ? 'Added ✓' : '+'}</span></button>)}</div>}
    {added && <p role="status">Outfit added to your board.</p>}
    <form onSubmit={create}><label>New board<input name="name" placeholder="e.g. Winter Looks" maxLength={100} required /></label><button className="solid-button" disabled={busy}>Create & add</button></form>
    {error && <p className="form-error" role="alert">{error}</p>}
  </dialog>
}
