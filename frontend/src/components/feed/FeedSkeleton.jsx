import FeedSkeletonCard from '../ui/FeedSkeletonCard.jsx'

const SKELETON_HEIGHTS = [150, 133, 125, 150, 133, 150, 125, 133]

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
