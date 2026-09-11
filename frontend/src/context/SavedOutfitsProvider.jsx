import { useCallback, useEffect, useRef, useState } from 'react'
import useAuth from '../hooks/useAuth.js'
import SavedOutfitsContext from './SavedOutfitsContext.js'
import { fetchSaved, saveOutfit, unsaveOutfit } from '../api/savedOutfitApi.js'

function SessionSaves({ userId, children, reload }) {
  const [outfits, setOutfits] = useState([])
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(!!userId)
  const pending = useRef(new Set())
  useEffect(() => {
    let ignore = false
    if (!userId) return
    async function load() {
      try {
        let items = [], page = 0, data
        do {
          data = await fetchSaved(page++)
          items = items.concat(data.items)
        } while (!ignore && page < data.totalPages)
        if (!ignore) setOutfits(items)
      } catch (e) { if (!ignore) setError(e.message) }
      finally { if (!ignore) setLoading(false) }
    }
    load()
    return () => { ignore = true }
  }, [userId])
  const toggle = useCallback(async (outfit) => {
    const id = outfit.id
    if (!userId || pending.current.has(id)) return
    pending.current.add(id)
    const saved = outfits.some(item => item.id === id)
    try {
      await (saved ? unsaveOutfit(id) : saveOutfit(id))
      setOutfits(previous => saved ? previous.filter(item => item.id !== id) : [outfit, ...previous])
    } finally { pending.current.delete(id) }
  }, [outfits, userId])
  return <SavedOutfitsContext.Provider value={{ outfits, loading, error, toggle, reload }}>{children}</SavedOutfitsContext.Provider>
}
export default function SavedOutfitsProvider({ children }) {
  const { user } = useAuth()
  const [revision, setRevision] = useState(0)
  return <SessionSaves key={(user?.id || 'guest') + ':' + revision} userId={user?.id} reload={() => setRevision(r => r + 1)}>{children}</SessionSaves>
}
