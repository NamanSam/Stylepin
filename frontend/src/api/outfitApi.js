const API_BASE_URL = "http://localhost:8080/api"

export async function fetchOutfits() {
  const response = await fetch(`${API_BASE_URL}/outfits`)
  if (!response.ok) {
    throw new Error(`Failed to fetch outfits: ${response.status} ${response.statusText}`)
  }
  return response.json()
}

export async function fetchOutfitById(id) {
  const response = await fetch(`${API_BASE_URL}/outfits/${id}`)
  if (response.status === 404) {
    const error = new Error("Outfit not found")
    error.status = 404
    throw error
  }
  if (!response.ok) {
    const error = new Error(`Failed to fetch outfit: ${response.status} ${response.statusText}`)
    error.status = response.status
    throw error
  }
  return response.json()
}