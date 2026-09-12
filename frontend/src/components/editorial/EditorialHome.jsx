import { Link } from 'react-router-dom'
import MotionReveal from '../ui/MotionReveal.jsx'
import { exploreOutfits, outfitImageUrl } from '../../utils/exploreLayout.js'

function CampaignImage({ outfit, className = '' }) {
  if (!outfit) return null
  const image = <img src={outfitImageUrl(outfit.imageUrl, 1000)} alt={outfit.title} loading="lazy" decoding="async" />
  return outfit.id > 0 && !outfit.isDemo ? <Link className={'campaign-image ' + className} to={'/outfits/' + outfit.id}>{image}<span className="image-caption">{outfit.title} ↗</span></Link> : <div className={'campaign-image ' + className}>{image}<span className="image-caption">{outfit.title} / Preview</span></div>
}
export default function EditorialHome({ outfits, onCategory, part = 'story' }) {
  if (!outfits.length) return null
  const campaign = exploreOutfits(outfits)
  const selection = campaign.length ? campaign : outfits
  const pick = (category, fallback) => selection.find(o => o.category?.toLowerCase().includes(category)) || selection[fallback % selection.length]
  const secondary = pick('old money', 2), street = pick('streetwear', 0), formal = pick('korean', 1)
  const editTiles = [...new Map([pick('old money', 1), pick('korean', 7), pick('casual', 6), pick('denim', 12)].map(o => [o.id, o])).values()]
  function explore(category) { onCategory(category); requestAnimationFrame(() => document.getElementById('discover')?.scrollIntoView({ behavior: 'instant' })) }
  if (part === 'story') return <MotionReveal><section className="editorial-story editorial-section" id="collections">
    <div className="story-copy"><span className="eyebrow">01 / The quiet statement</span><h2>Less noise.<br /><em>More presence.</em></h2><p>Easy layers. Intentional proportions. A study in the pieces you reach for, again and again.</p><button className="text-link" onClick={() => explore('Minimal')}>Discover minimal style ↗</button></div>
    <CampaignImage outfit={secondary} className="story-image" />
    <div className="story-aside"><CampaignImage outfit={street} /><span className="eyebrow">A different kind of everyday</span></div>
  </section></MotionReveal>
  return <>
    <MotionReveal><section className="editorial-section style-directions"><div className="section-heading"><div><span className="eyebrow">03 / In the conversation</span><h2>Find your <em>direction.</em></h2></div><p>Different moods. Your own interpretation.</p></div><div className="style-directions-list">{['Streetwear', 'Old Money', 'Korean', 'Minimal'].map((category, index) => <button key={category} onClick={() => explore(category)}><small>0{index + 1}</small><span>{category}</span><span aria-hidden="true">↗</span></button>)}</div></section></MotionReveal>
    <MotionReveal><section className="afterhours editorial-section"><CampaignImage outfit={formal} /><div className="afterhours-copy"><span className="eyebrow">04 / Beyond the everyday</span><h2>A change<br />of <em>pace.</em></h2><p>Softer structure. Stronger silhouettes.<br />A fresh perspective on everyday dressing.</p><button className="text-link" onClick={() => explore('Korean')}>Explore a new perspective ↗</button></div></section></MotionReveal>
    <MotionReveal><section className="editorial-section the-edit" id="edit"><div className="section-heading"><div><span className="eyebrow">05 / Selected looks</span><h2>The considered <em>edit.</em></h2></div><Link className="text-link" to="/explore">Keep exploring ↗</Link></div><div className="editorial-products">{editTiles.map(o => <CampaignImage key={o.id} outfit={o} />)}</div></section></MotionReveal>
  </>
}
