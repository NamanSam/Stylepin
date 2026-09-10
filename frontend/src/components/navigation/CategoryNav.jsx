const CATEGORIES = [
  'For You',
  'Trending',
  'Streetwear',
  'Old Money',
  'Korean',
  'Y2K',
  'Minimal',
  'College',
  'Vintage',
  'Casual',
  'Formal',
  'Summer',
  'Winter',
  'Monochrome',
  'Denim',
  'Oversized',
]

function CategoryNav({ activeCategory, onCategoryChange }) {
  return (
    <nav className="category-nav" aria-label="Fashion categories">
      <div className="category-nav__scroll">
        {CATEGORIES.map((category) => (
          <button
            key={category}
            type="button"
            className={`category-nav__tab ${activeCategory === category ? 'category-nav__tab--active' : ''}`}
            onClick={() => onCategoryChange(category)}
          >
            {category}
          </button>
        ))}
      </div>
    </nav>
  )
}

export default CategoryNav
