const INSERTS = [
  { id: 'trending-week', text: 'Trending this week', icon: '🔥' },
  { id: 'explore-korean', text: 'Explore Korean Casual', icon: '✨' },
  { id: 'new-streetwear', text: 'New Streetwear Drops', icon: '🆕' },
]

function FeedInsert({ insertIndex }) {
  const insert = INSERTS[insertIndex % INSERTS.length]

  return (
    <div className="feed-insert" aria-label={insert.text}>
      <span className="feed-insert__icon" aria-hidden="true">{insert.icon}</span>
      <span className="feed-insert__text">{insert.text}</span>
      <span className="feed-insert__arrow" aria-hidden="true">→</span>
    </div>
  )
}

export default FeedInsert
