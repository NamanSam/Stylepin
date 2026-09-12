import { Link } from 'react-router-dom'
import { useLocation } from 'react-router-dom'
import { useState } from 'react'
import { outfitImageUrl } from '../../utils/exploreLayout.js'

export default function ExploreImage({ outfit, slot, index }) {
  const location = useLocation()
  const [failed, setFailed] = useState(null)
  const src = outfitImageUrl(outfit.imageUrl)
  const live = outfit.id > 0 && !outfit.isDemo
  const content = <>
    {failed === src ? <span className="explore-image-failed">{outfit.title}</span> : <picture key={src}>
      <source media="(max-width: 740px), (pointer: coarse)" srcSet={index < 6 ? outfitImageUrl(outfit.imageUrl, 420) : 'data:image/gif;base64,R0lGODlhAQABAAD/ACwAAAAAAQABAAACADs='} />
      <img src={src} alt={outfit.title} loading="eager" decoding="async" onError={() => setFailed(src)} />
    </picture>}
    <span className="explore-image-caption"><small>{outfit.category}</small><strong>{outfit.title}</strong><span>{live ? 'View look ↗' : outfit.isGenerated ? 'AI campaign study' : 'Editorial preview'}</span></span>
  </>
  return <div className="explore-slot" data-explore-slot={index} style={{ left: `${slot.x}%`, '--slot-y': `${slot.y}cqh`, '--slot-depth': `${slot.depth}px`, '--slot-angle': `${slot.angle}deg`, '--slot-rotation': `${slot.rotation}deg`, '--slot-scale': slot.scale, '--slot-brightness': slot.depth < -180 ? .73 : .94, '--slot-delay': `${index * 65}ms` }}>
    {live ? <Link className="explore-image" to={`/outfits/${outfit.id}`} state={{ fromExplore: location.pathname }} aria-label={`View look: ${outfit.title}`}>{content}</Link> : <div className="explore-image explore-image--preview">{content}</div>}
  </div>
}
