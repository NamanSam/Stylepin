import { apiRequest } from './apiClient.js'
export const fetchProducts = (page = 0) => apiRequest(`/admin/products?page=${page}&size=24`)
export const fetchProductCategories = () => apiRequest('/admin/categories')
export const createProduct = (body) => apiRequest('/admin/products', { method: 'POST', body })
export const updateProduct = (id, body) => apiRequest(`/admin/products/${id}`, { method: 'PUT', body })
export const deleteProduct = (id) => apiRequest(`/admin/products/${id}`, { method: 'DELETE' })
