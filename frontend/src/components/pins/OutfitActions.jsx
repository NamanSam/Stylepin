import { useState } from 'react'
import { useLocation, useNavigate } from 'react-router-dom'
import useAuth from '../../hooks/useAuth.js'
import useSavedOutfits from '../../hooks/useSavedOutfits.js'
import BoardPickerDialog from '../boards/BoardPickerDialog.jsx'
export default function OutfitActions({ outfit, onRemove }) {
  const { user } = useAuth()
  const { outfits, toggle, loading, error: loadError } = useSavedOutfits()
  const [busy, setBusy] = useState(false)
  const [error, setError] = useState('')
  const [picker, setPicker] = useState(false)
  const navigate = useNavigate(), location = useLocation()
  const saved = outfits.some(item => item.id === outfit.id)
  function signedIn() {
    if (user) return true
    navigate('/login', { state: { from: location.pathname + location.search } }); return false
  }
  async function save() {
    if (!signedIn()) return
    setBusy(true); setError('')
    try { await toggle(outfit) } catch (e) { setError(e.message) } finally { setBusy(false) }
  }
  async function remove() {
    setBusy(true); setError('')
    try { await onRemove(outfit.id) } catch (e) { setError(e.message) } finally { setBusy(false) }
  }
  if (outfit.id < 1 || outfit.isDemo) return <span className="demo-label">Preview look</span>
  return <div className="outfit-actions">
    <button onClick={save} disabled={busy || (user && (loading || !!loadError))} aria-pressed={saved} title={loadError || undefined}>{saved ? 'Saved ✓' : 'Save'}</button>
    <button onClick={() => { if (signedIn()) setPicker(true) }} aria-label={'Add ' + outfit.title + ' to a board'}>Board +</button>
    {onRemove && <button onClick={remove} disabled={busy}>Remove</button>}
    {error && <span role="alert" className="action-error">{error}</span>}
    {picker && <BoardPickerDialog outfit={outfit} onClose={() => setPicker(false)} />}
  </div>
}
