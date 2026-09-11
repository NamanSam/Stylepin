import { API_BASE } from '../api/config.js'

let accessToken = null
let snapshot = { user: null, loading: true }
let refreshFlight = null
let bootstrapFlight = null
let generation = 0
const listeners = new Set()
const channel = typeof BroadcastChannel !== 'undefined' ? new BroadcastChannel('stylepin-session') : null
export const subscribe = (listener) => { listeners.add(listener); return () => listeners.delete(listener) }
export const getSnapshot = () => snapshot
export const getAccessToken = () => accessToken
export const getGeneration = () => generation
function publish(result) {
  accessToken = result?.accessToken || null
  snapshot = { user: result?.user || null, loading: false }
  listeners.forEach(listener => listener())
}
channel?.addEventListener('message', () => { generation++; publish(null) })
export function clearSession() { generation++; publish(null) }

async function responseBody(response) {
  const body = response.status === 204 ? null : await response.json().catch(() => null)
  if (!response.ok) {
    const error = new Error(body?.message || 'Unable to connect. Please try again.')
    error.status = response.status
    error.fieldErrors = body?.fieldErrors || {}
    throw error
  }
  return body
}
async function authRequest(path, body) {
  const csrf = await fetch(API_BASE + '/auth/csrf', { credentials: 'include', cache: 'no-store' }).then(responseBody)
  return fetch(API_BASE + '/auth/' + path, {
    method: 'POST', credentials: 'include', cache: 'no-store',
    headers: { 'Content-Type': 'application/json', 'X-XSRF-TOKEN': csrf.token },
    ...(body ? { body: JSON.stringify(body) } : {}),
  }).then(responseBody)
}
// Web Locks serialize rotation across same-origin tabs. Never broadcast tokens.
function sessionLock(task) {
  return globalThis.navigator?.locks ? navigator.locks.request('stylepin-refresh', task) : task()
}
export function refreshSession() {
  if (!refreshFlight) {
    const started = generation
    refreshFlight = sessionLock(async () => {
      try {
        const result = await authRequest('refresh')
        if (generation === started) publish(result)
        return generation === started ? result : null
      } catch (error) {
        if (generation === started && error.status === 401) clearSession()
        throw error
      }
    }).finally(() => { refreshFlight = null })
  }
  return refreshFlight
}
export function bootstrap() {
  if (!bootstrapFlight) bootstrapFlight = refreshSession().catch(() => publish(null))
  return bootstrapFlight
}
export async function login(credentials) {
  return sessionLock(async () => {
    const result = await authRequest('login', credentials)
    generation++
    publish(result)
    channel?.postMessage('account-changed')
    return result
  })
}
export const register = (details) => sessionLock(() => authRequest('register', details))
export async function logout() {
  return sessionLock(async () => {
    await authRequest('logout')
    clearSession()
    channel?.postMessage('logged-out')
  })
}
