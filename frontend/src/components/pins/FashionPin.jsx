import { Link } from 'react-router-dom'
import { useState } from 'react'
import OutfitActions from './OutfitActions.jsx'
import { outfitImageUrl } from '../../utils/exploreLayout.js'

export default function FashionPin({ outfit, index = 0, onRemove }) {
  const live = outfit.id > 0 && !outfit.isDemo
  const [failed, setFailed] = useState(null)
  const src = outfitImageUrl(outfit.imageUrl, 700)
  const content = <><div className="pin__image-wrap">{failed === src ? <span className="pin-image-empty">Image unavailable</span> : <img className="pin__image" src={src} alt={outfit.title} loading="lazy" decoding="async" onError={() => setFailed(src)} />}{live && <span className="pin__overlay"><span className="pin__open-look">View look ↗</span></span>}</div><div className="pin__caption"><span className="pin__category">{outfit.category}</span><h3 className="pin__title">{outfit.title}</h3></div></>
  return <article className="fashion-pin" style={{ '--pin-ratio': outfit.ratio || ['2/3', '3/4', '4/5'][index % 3], '--stagger': Math.min(index, 5) }}>
    {live ? <Link to={`/outfits/${outfit.id}`} className="pin-link" aria-label={`View outfit: ${outfit.title}`}>{content}</Link> : <div className="pin-preview">{content}</div>}
    <OutfitActions outfit={outfit} onRemove={onRemove} />
  </article>
}
