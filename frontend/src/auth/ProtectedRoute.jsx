import { Navigate, Outlet, useLocation } from 'react-router-dom'
import useAuth from '../hooks/useAuth.js'
export default function ProtectedRoute() {
  const { user, loading } = useAuth()
  const location = useLocation()
  if (loading) return <p className="collection-status" role="status">Opening your collection…</p>
  return user ? <Outlet /> : <Navigate to="/login" state={{ from: location.pathname }} replace />
}
