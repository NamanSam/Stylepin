import { useEffect, useSyncExternalStore } from 'react'
import AuthContext from './AuthContext.js'
import { subscribe, getSnapshot, bootstrap, login, logout, register } from './authSession.js'
export default function AuthProvider({ children }) {
  const session = useSyncExternalStore(subscribe, getSnapshot)
  useEffect(() => { bootstrap() }, [])
  return <AuthContext.Provider value={{ ...session, login, logout, register }}>{children}</AuthContext.Provider>
}
