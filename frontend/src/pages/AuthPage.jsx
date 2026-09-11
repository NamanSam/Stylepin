import { useState } from 'react'
import { Link, Navigate, useLocation, useNavigate } from 'react-router-dom'
import useAuth from '../hooks/useAuth.js'
export default function AuthPage({ registration = false }) {
  const { user, login, register } = useAuth()
  const [error, setError] = useState('')
  const [busy, setBusy] = useState(false)
  const location = useLocation(), navigate = useNavigate()
  const destination = location.state?.from
  const next = typeof destination === 'string' && destination.startsWith('/') && !destination.startsWith('//') && !destination.startsWith('/login') && !destination.startsWith('/register') ? destination : '/'
  if (user) return <Navigate to={next} replace />
  async function submit(event) {
    event.preventDefault(); setBusy(true); setError('')
    const data = Object.fromEntries(new FormData(event.currentTarget))
    if (new TextEncoder().encode(data.password).length > 72) {
      setError('Please keep your password within 72 UTF-8 bytes.'); setBusy(false); return
    }
    try {
      if (registration) { await register(data); navigate('/login', { state: { from: next, registered: true } }) }
      else { await login(data); navigate(next, { replace: true }) }
    } catch (e) { setError(Object.values(e.fieldErrors || {}).join('. ') || e.message) }
    finally { setBusy(false) }
  }
  return <main className="auth-page">
    <div className="auth-story"><span className="eyebrow">A wardrobe of possibilities</span><h1>Your style.<br /><em>Your point of view.</em></h1><p>Keep the looks that speak to you. Give your ideas a place to live.</p></div>
    <section className="auth-panel"><span className="eyebrow">StylePin / Personal collection</span>
      <h2>{registration ? 'Make it yours.' : 'Welcome back.'}</h2>
      {location.state?.registered && <p role="status">Your account is ready. Sign in to begin.</p>}
      <form onSubmit={submit}>
        {registration && <label>Username<input aria-label="Username" aria-describedby="username-hint" name="username" autoComplete="username" pattern="[a-zA-Z0-9_]{3,30}" minLength={3} maxLength={30} required /><small id="username-hint">3–30 letters, numbers or underscores.</small></label>}
        <label>Email<input name="email" type="email" autoComplete="email" maxLength={254} required /></label>
        <label>Password<input aria-label="Password" aria-describedby={registration ? 'password-hint' : undefined} name="password" type="password" autoComplete={registration ? 'new-password' : 'current-password'} minLength={registration ? 12 : undefined} maxLength={72} required />{registration && <small id="password-hint">At least 12 characters; up to 72 UTF-8 bytes.</small>}</label>
        {error && <p className="form-error" role="alert">{error}</p>}
        <button className="solid-button" disabled={busy}>{busy ? 'One moment…' : registration ? 'Create account' : 'Sign in'} <span aria-hidden="true">↗</span></button>
      </form>
      <p className="auth-switch">{registration ? 'Already have an account? ' : 'New to StylePin? '}<Link to={registration ? '/login' : '/register'} state={{ from: next }}>{registration ? 'Sign in' : 'Create an account'}</Link></p>
    </section>
  </main>
}
