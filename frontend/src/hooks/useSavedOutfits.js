import { useContext } from 'react'
import SavedOutfitsContext from '../context/SavedOutfitsContext.js'
export default function useSavedOutfits() { return useContext(SavedOutfitsContext) }
