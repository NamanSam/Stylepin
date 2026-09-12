import OutfitActions from '../pins/OutfitActions.jsx'
import { productType } from '../../utils/relatedOutfits.js'
import FloatingProduct from './FloatingProduct.jsx'
import LookImage from './LookImage.jsx'

export default function LookHero({ outfit }) {
  const sides = { left: [], right: [] }
  const order = { Outerwear: 0, Bottoms: 0, Layer: 1, Shoes: 2, Accessories: 3 }
  ;(outfit.products || []).forEach((product, index) => {
    const type = productType(product)
    const side = type.side || (sides.left.length <= sides.right.length ? 'left' : 'right')
    sides[side].push({ product, index, priority: order[type.label] ?? 4 })
  })
  Object.values(sides).forEach(items => items.sort((a, b) => a.priority - b.priority || a.index - b.index))
  return <section className="look-hero" aria-labelledby="look-title">
    <header className="look-heading"><p className="look-kicker">A closer look / {String((outfit.products || []).length).padStart(2, '0')} pieces</p><h2>LOOK <em>BREAKDOWN</em></h2><p>One look. Every detail.</p></header>
    <div className="look-stage">
      <figure className="look-model"><LookImage src={outfit.imageUrl} alt={outfit.title} eager /><figcaption><span>The complete look</span><span>StylePin / {String(outfit.id).padStart(2, '0')}</span></figcaption></figure>
      <div className="look-info">
        {outfit.category && <p className="look-kicker">{outfit.category}</p>}
        <h1 id="look-title">{outfit.title}</h1>
        {outfit.description && <p className="look-description">{outfit.description}</p>}
        {!!outfit.tags?.length && <ul className="look-tags" aria-label="Style tags">{outfit.tags.map(tag => <li key={tag}>{tag}</li>)}</ul>}
        <OutfitActions outfit={outfit} />
      </div>
      {['left', 'right'].map(side => <div className={`look-orbit look-orbit--${side}`} key={side}>{sides[side].map(({ product, index }) => <FloatingProduct key={product.id ?? index} product={product} index={index} />)}</div>)}
    </div>
    <a className="look-shop-jump" href="#look-shop">Explore the pieces <span>↓</span></a>
  </section>
}
