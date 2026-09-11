import { apiRequest } from './apiClient.js'
export const fetchSaved = (page = 0) => apiRequest('/users/me/saved-outfits?page=' + page + '&size=100')
export const saveOutfit = (id) => apiRequest('/users/me/saved-outfits/' + id, { method: 'POST' })
export const unsaveOutfit = (id) => apiRequest('/users/me/saved-outfits/' + id, { method: 'DELETE' })
