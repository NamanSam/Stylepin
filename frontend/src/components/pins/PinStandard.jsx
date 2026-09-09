import PinOverlay from './PinOverlay.jsx'

function PinStandard({ outfit }) {
  return (
    <article className="pin pin--standard">
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
      <div className="pin__caption">
        <span className="pin__title">{outfit.title}</span>
        {outfit.category && (
          <span className="pin__category">{outfit.category}</span>
        )}
      </div>
    </article>
  )
}

export default PinStandard
