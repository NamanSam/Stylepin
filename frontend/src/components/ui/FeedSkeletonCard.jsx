function FeedSkeletonCard({ height }) {
  return (
    <div className="skeleton-card" aria-hidden="true">
      <div className="skeleton-card__image" style={{ paddingBottom: `${height}%` }} />
      <div className="skeleton-card__body">
        <div className="skeleton-card__line skeleton-card__line--title" />
        <div className="skeleton-card__line skeleton-card__line--sub" />
      </div>
    </div>
  )
}

export default FeedSkeletonCard
