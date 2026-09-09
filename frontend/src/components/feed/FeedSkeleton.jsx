import FeedSkeletonCard from '../ui/FeedSkeletonCard.jsx'

const SKELETON_HEIGHTS = [110, 130, 95, 120, 105, 140, 100, 125, 115, 135, 90, 128]

function FeedSkeleton() {
  return (
    <div className="masonry-feed" role="status" aria-label="Loading fashion feed">
      {SKELETON_HEIGHTS.map((h, i) => (
        <FeedSkeletonCard key={i} height={h} />
      ))}
      <span className="sr-only">Loading curated styles...</span>
    </div>
  )
}

export default FeedSkeleton
