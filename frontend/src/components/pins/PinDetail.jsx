import PinOverlay from './PinOverlay.jsx'

function PinDetail({ outfit }) {
  return (
    <article className="pin pin--detail">
      <div className="pin__image-wrap">
        <img
          className="pin__image"
          src={outfit.imageUrl}
          alt={outfit.title}
          loading="lazy"
          decoding="async"
        />
        <PinOverlay />
      </div>
      <div className="pin__caption pin__caption--detail">
        <span className="pin__title">{outfit.title}</span>
        {outfit.category && (
          <span className="pin__category">{outfit.category}</span>
        )}
        {outfit.tags && outfit.tags.length > 0 && (
          <span className="pin__tags-line">
            {outfit.tags.slice(0, 3).map(t => `#${t}`).join(' ')}
          </span>
        )}
      </div>
    </article>
  )
}

export default PinDetail
