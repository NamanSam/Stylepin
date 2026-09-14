import { Navigate, Outlet, useLocation } from 'react-router-dom'
import useAuth from '../hooks/useAuth.js'
export default function AdminRoute() {
  const { user, loading } = useAuth()
  const location = useLocation()
  if (loading) return <p className="collection-status" role="status">Checking admin access…</p>
  if (!user) return <Navigate to="/login" state={{ from: location.pathname }} replace />
  if (user.role !== 'ADMIN') return <main className="collection-page"><span className="eyebrow">Private workspace</span><h1>Admin access required.</h1><p>Your account does not have permission to manage products.</p></main>
  return <Outlet />
}
