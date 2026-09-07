function OutfitCard({ imageUrl, title, category }) {
  return (
    <article className="outfit-card">
      <img className="outfit-card__image" src={imageUrl} alt={title} />
      <div className="outfit-card__body">
        <p className="outfit-card__category">{category}</p>
        <h2 className="outfit-card__title">{title}</h2>
        <button type="button" className="outfit-card__button">
          View Outfit
        </button>
      </div>
    </article>
  )
}

export default OutfitCard
