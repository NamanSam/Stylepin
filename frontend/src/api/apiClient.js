import { API_BASE } from './config.js'
import { getAccessToken, getGeneration, refreshSession, clearSession } from '../auth/authSession.js'

export async function apiRequest(path, { method = 'GET', body, authenticated = true } = {}, retry = true) {
  const generation = getGeneration()
  if (authenticated && !getAccessToken()) await refreshSession()
  const response = await fetch(API_BASE + path, {
    method, credentials: 'omit',
    headers: { ...(body ? { 'Content-Type': 'application/json' } : {}),
      ...(authenticated && getAccessToken() ? { Authorization: 'Bearer ' + getAccessToken() } : {}) },
    ...(body ? { body: JSON.stringify(body) } : {}),
  })
  if (authenticated && generation !== getGeneration()) throw new Error('Your account changed. Please try again.')
  if (response.status === 401 && authenticated && retry) {
    await refreshSession()
    return apiRequest(path, { method, body, authenticated }, false)
  }
  if (response.status === 401 && authenticated) clearSession()
  const data = response.status === 204 ? null : await response.json().catch(() => null)
  if (!response.ok) {
    const error = new Error(data?.message || 'Unable to complete the request. Please try again.')
    error.status = response.status
    error.fieldErrors = data?.fieldErrors || {}
    throw error
  }
  return data
}
