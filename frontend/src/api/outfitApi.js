import { apiRequest } from './apiClient.js'
export const fetchOutfits = () => apiRequest('/outfits', { authenticated: false })
export const fetchOutfitById = (id) => apiRequest('/outfits/' + encodeURIComponent(id), { authenticated: false })
