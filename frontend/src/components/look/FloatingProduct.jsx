import { formatINR } from '../../utils/formatPrice.js'
import { productType, storeUrl } from '../../utils/relatedOutfits.js'
import LookImage from './LookImage.jsx'

export default function FloatingProduct({ product, index = 0, detailed = false, alternatives = [] }) {
  const type = productType(product)
  const url = storeUrl(product.productUrl)
  const price = product.price != null && Number.isFinite(Number(product.price)) ? formatINR(product.price) : null
  return <article className={`look-piece ${detailed ? 'look-piece--detailed' : 'look-piece--floating'}`} style={{ '--look-index': index }}>
    <div className="look-piece__image"><LookImage src={product.imageUrl} alt={product.name} /></div>
    <div className="look-piece__content">
      <p className="look-kicker">{type.label}<span>{String(index + 1).padStart(2, '0')}</span></p>
      <h3>{product.name}</h3>
      <p className="look-piece__brand">{product.brand}{detailed && product.category && <span> / {product.category}</span>}</p>
      <div className="look-piece__footer">{price && <span>{price}</span>}
        {url ? <a href={url} target="_blank" rel="noopener noreferrer" aria-label={`View ${product.name} (opens in a new tab)`}>View Product ↗</a> : <span className="look-piece__unavailable">Store link unavailable</span>}
      </div>
      {alternatives.length > 0 && <details className="look-piece__alternatives"><summary>Alternative pieces ({alternatives.length})</summary>
        {alternatives.map(item => <a key={item.productUrl} href={storeUrl(item.productUrl)} target="_blank" rel="noopener noreferrer">{item.name} ↗<span>{item.brand}</span></a>)}
      </details>}
    </div>
  </article>
}
