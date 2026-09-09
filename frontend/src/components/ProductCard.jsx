function ProductCard({ product }) {
  const formattedPrice = product.price != null
    ? `$${Number(product.price).toFixed(2)}`
    : null

  return (
    <article className="product-card">
      <div className="product-card__image-wrapper">
        {product.imageUrl ? (
          <img
            className="product-card__image"
            src={product.imageUrl}
            alt={product.name}
            loading="lazy"
          />
        ) : (
          <div className="product-card__placeholder">No image</div>
        )}
      </div>

      <div className="product-card__content">
        <div className="product-card__meta">
          {product.brand && (
            <span className="product-card__brand">{product.brand}</span>
          )}
          {product.category && (
            <span className="product-card__category">{product.category}</span>
          )}
        </div>

        <h3 className="product-card__name">{product.name}</h3>

        <div className="product-card__footer">
          {formattedPrice && (
            <span className="product-card__price">{formattedPrice}</span>
          )}

          {product.productUrl ? (
            <a
              href={product.productUrl}
              target="_blank"
              rel="noopener noreferrer"
              className="product-card__button"
              aria-label={`View ${product.name} on external store (opens in a new tab)`}
            >
              View Product
              <span className="product-card__external-icon" aria-hidden="true">↗</span>
            </a>
          ) : (
            <button type="button" className="product-card__button" disabled>
              View Product
            </button>
          )}
        </div>
      </div>
    </article>
  )
}

export default ProductCard