import { Link } from 'react-router-dom'
import ProductCard from '../ProductCard.jsx'

function CampaignImage({ outfit, className = '', eager = false }) {
  if (!outfit) return null
  const image = <img src={outfit.imageUrl?.replace(/w=\d+/, 'w=1400')} alt={outfit.title} loading={eager ? 'eager' : 'lazy'} fetchPriority={eager ? 'high' : 'auto'} />
  return outfit.id > 0 ? <Link className={'campaign-image ' + className} to={'/outfits/' + outfit.id}>{image}<span className="image-caption">{outfit.title} ↗</span></Link> : <div className={'campaign-image ' + className}>{image}</div>
}
export default function EditorialHome({ outfits, onCategory }) {
  if (!outfits.length) return null
  const pick = (category, fallback) => outfits.find(o => o.category?.toLowerCase().includes(category)) || outfits[fallback % outfits.length]
  const hero = pick('old money', 1), secondary = pick('minimal', 4), street = pick('streetwear', 0)
  const formalLooks = outfits.filter(o => o.category?.toLowerCase().includes('formal'))
  const formal = formalLooks.at(-1) || outfits[3 % outfits.length]
  const seen = new Set()
  const products = outfits.flatMap(o => o.products || []).filter(p => {
    if (seen.has(p.name)) return false
    seen.add(p.name); return true
  }).slice(0,4)
  function explore(category) { onCategory(category); requestAnimationFrame(() => document.getElementById('discover')?.scrollIntoView({ behavior: 'instant' })) }
  return <>
    <section className="campaign-hero">
      <div className="campaign-kicker"><span>THE STYLEPIN EDIT</span><span>Vol. 01 / A new perspective</span></div>
      <div className="campaign-title"><h1>The art of<br /><em>getting dressed.</em></h1><div><p>Considered looks.<br />Unexpected combinations.<br />Entirely your own.</p><a className="text-link" href="#discover">Explore the edit <span>↗</span></a></div></div>
      <div className="campaign-pair"><CampaignImage outfit={hero} eager className="campaign-primary" /><CampaignImage outfit={secondary} className="campaign-secondary" eager /><div className="campaign-note"><span>01 — THE EVERYDAY, RECONSIDERED</span><p>Good style starts<br />with a point of view.</p></div></div>
    </section>
    <section className="editorial-story editorial-section" id="collections">
      <div className="story-copy"><span className="eyebrow">01 / The quiet statement</span><h2>Less noise.<br /><em>More presence.</em></h2><p>Easy layers. Intentional proportions. A study in the pieces you reach for, again and again.</p><button className="text-link" onClick={() => explore('Minimal')}>Discover minimal style ↗</button></div>
      <CampaignImage outfit={secondary} className="story-image" />
      <div className="story-aside"><CampaignImage outfit={street} /><span className="eyebrow">A different kind of everyday</span></div>
    </section>
    {products.length > 0 && <section className="editorial-section selected-pieces" id="pieces"><div className="section-heading"><div><span className="eyebrow">Details make the difference</span><h2>Selected pieces.</h2></div><a className="text-link" href="#discover">Find the whole look ↗</a></div><div className="editorial-products">{products.map(p => <ProductCard key={p.id} product={p} />)}</div></section>}
    <section className="philosophy editorial-section"><span className="eyebrow">The StylePin point of view</span><h2>Style is personal.<br /><em>Inspiration is everywhere.</em></h2><div className="philosophy-bottom"><span className="philosophy-mark" aria-hidden="true">SP.</span><p>We bring together looks, pieces, and possibilities. You decide what feels like you. No rules to follow. Just room to explore.</p></div></section>
    <section className="afterhours editorial-section"><CampaignImage outfit={formal} /><div className="afterhours-copy"><span className="eyebrow">02 / Beyond the everyday</span><h2>A change<br />of <em>pace.</em></h2><p>Sharper lines. A little contrast.<br />Looks for wherever the day takes you.</p><button className="text-link" onClick={() => explore('Formal')}>Explore the occasion ↗</button></div></section>
  </>
}
