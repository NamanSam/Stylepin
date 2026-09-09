import PinOverlay from './PinOverlay.jsx'

function PinMinimal({ outfit }) {
  return (
    <article className="pin pin--minimal">
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
      <div className="pin__caption pin__caption--minimal">
        <span className="pin__title">{outfit.title}</span>
      </div>
    </article>
  )
}

export default PinMinimal
