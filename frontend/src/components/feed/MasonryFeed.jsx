import FashionPin from '../pins/FashionPin.jsx'
import FeedInsert from './FeedInsert.jsx'

const INSERT_INTERVAL = 8
const INSERT_SLOTS = new Set([INSERT_INTERVAL, INSERT_INTERVAL * 3, INSERT_INTERVAL * 5])

function MasonryFeed({ outfits, searchQuery }) {
  let insertCounter = 0

  return (
    <div className="masonry-feed">
      {outfits.map((outfit, index) => {
        const items = []

        if (INSERT_SLOTS.has(index) && insertCounter < 3) {
          items.push(
            <div key={`insert-${insertCounter}`} className="masonry-feed__insert">
              <FeedInsert insertIndex={insertCounter} />
            </div>
          )
          insertCounter++
        }

        const staggerBase = searchQuery ? 0 : index
        items.push(
          <FashionPin key={outfit.id} outfit={outfit} index={staggerBase} />
        )

        return items
      })}
    </div>
  )
}

export default MasonryFeed
