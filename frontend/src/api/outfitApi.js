const API_BASE_URL = "http://localhost:8080/api"

export async function fetchOutfits() {
  const response = await fetch(`${API_BASE_URL}/outfits`)
  if (!response.ok) {
    throw new Error(`Failed to fetch outfits: ${response.status} ${response.statusText}`)
  }
  return response.json()
}

