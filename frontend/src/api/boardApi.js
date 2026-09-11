import { apiRequest } from './apiClient.js'
export const fetchBoards = (page = 0) => apiRequest('/boards?page=' + page)
export const createBoard = (body) => apiRequest('/boards', { method: 'POST', body })
export const fetchBoard = (id, page = 0) => apiRequest('/boards/' + id + '?page=' + page)
export const addToBoard = (id, outfitId) => apiRequest('/boards/' + id + '/outfits/' + outfitId, { method: 'POST' })
export const removeFromBoard = (id, outfitId) => apiRequest('/boards/' + id + '/outfits/' + outfitId, { method: 'DELETE' })
export const deleteBoard = (id) => apiRequest('/boards/' + id, { method: 'DELETE' })
