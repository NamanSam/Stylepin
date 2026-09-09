import { Link } from 'react-router-dom'

function OutfitCard({ id, imageUrl, title, category }) {
  return (
    <Link to={`/outfits/${id}`} className="outfit-card-link" aria-label={`View outfit: ${title}`}>
      <article className="outfit-card">
        <div className="outfit-card__image-wrapper">
          <img className="outfit-card__image" src={imageUrl} alt={title} loading="lazy" />
        </div>
        <div className="outfit-card__body">
          <p className="outfit-card__category">{category}</p>
          <h2 className="outfit-card__title">{title}</h2>
          <span className="outfit-card__button">
            View Outfit
          </span>
        </div>
      </article>
    </Link>
  )
}

export default OutfitCard