import PinOverlay from './PinOverlay.jsx'

function PinShoppable({ outfit }) {
  return (
    <article className="pin pin--shoppable">
      <div className="pin__image-wrap">
        <img
          className="pin__image"
          src={outfit.imageUrl}
          alt={outfit.title}
          loading="lazy"
          decoding="async"
        />
        <span className="pin__shop-indicator" aria-label="Shoppable outfit">
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
            <circle cx="9" cy="21" r="1" />
            <circle cx="20" cy="21" r="1" />
            <path d="M1 1h4l2.68 13.39a2 2 0 002 1.61h9.72a2 2 0 002-1.61L23 6H6" />
          </svg>
        </span>
        <PinOverlay />
      </div>
      <div className="pin__caption">
        <span className="pin__title">{outfit.title}</span>
        {outfit.category && (
          <span className="pin__category">{outfit.category}</span>
        )}
      </div>
    </article>
  )
}

export default PinShoppable
