import FloatingProduct from './FloatingProduct.jsx'
import { alternativePieces } from '../../utils/relatedOutfits.js'

export default function ShopTheLook({ outfit, outfits }) {
  return <section id="look-shop" className="look-section look-reveal" aria-labelledby="look-shop-title">
    <header className="look-section-heading"><div><p className="look-kicker">01 / The pieces</p><h2 id="look-shop-title">SHOP <em>THE LOOK</em></h2></div><p>A considered wardrobe, piece by piece.</p></header>
    {outfit.products?.length ? <div className="look-shop-grid">{outfit.products.map((product, index) => <FloatingProduct key={product.id ?? index} product={product} index={index} detailed alternatives={alternativePieces(product, outfit, outfits)} />)}</div> : <p className="look-empty">The pieces for this look haven’t been added yet.</p>}
  </section>
}
