import PinOverlay from './PinOverlay.jsx'

function PinTrend({ outfit }) {
  return (
    <article className="pin pin--trend">
      <div className="pin__image-wrap">
        <img
          className="pin__image"
          src={outfit.imageUrl}
          alt={outfit.title}
          loading="lazy"
          decoding="async"
        />
        <span className="pin__trend-badge">Trending</span>
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

export default PinTrend
