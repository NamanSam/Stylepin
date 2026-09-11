import { useContext } from 'react'
import AuthContext from '../auth/AuthContext.js'
export default function useAuth() { return useContext(AuthContext) }
