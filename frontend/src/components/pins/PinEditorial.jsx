import PinOverlay from './PinOverlay.jsx'

function PinEditorial({ outfit }) {
  return (
    <article className="pin pin--editorial">
      <div className="pin__image-wrap">
        <img
          className="pin__image"
          src={outfit.imageUrl}
          alt={outfit.title}
          loading="lazy"
          decoding="async"
        />
        <div className="pin__editorial-overlay">
          <span className="pin__editorial-title">{outfit.title}</span>
          {outfit.category && (
            <span className="pin__editorial-label">{outfit.category}</span>
          )}
        </div>
        <PinOverlay />
      </div>
    </article>
  )
}

export default PinEditorial
