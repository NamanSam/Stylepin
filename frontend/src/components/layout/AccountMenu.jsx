import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import useAuth from '../../hooks/useAuth.js'
export default function AccountMenu() {
  const { user, loading, logout } = useAuth()
  const [error, setError] = useState('')
  const [busy, setBusy] = useState(false)
  const navigate = useNavigate()
  async function leave() {
    setBusy(true); setError('')
    try { await logout(); navigate('/') } catch (e) { setError(e.message) } finally { setBusy(false) }
  }
  if (loading) return <span className="account-loading" aria-label="Checking session">…</span>
  return <div className="account-menu">
    {user ? <>
      <span className="account-avatar" title={user.username} aria-label={'Signed in as ' + user.username}>{user.username.slice(0,1).toUpperCase()}</span>
      <Link to="/saved">Saved</Link><Link to="/boards">Boards</Link>
      <button onClick={leave} disabled={busy}>{busy ? 'Leaving…' : 'Logout'}</button>
    </> : <><Link to="/login">Login</Link><Link to="/register" className="register-link">Register</Link></>}
    {error && <p className="account-error" role="alert">{error}</p>}
  </div>
}
