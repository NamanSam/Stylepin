import { Link } from 'react-router-dom'
import { rankRelatedOutfits } from '../../utils/relatedOutfits.js'
import LookImage from './LookImage.jsx'

export default function AlternativeLooks({ outfit, catalog }) {
  const related = rankRelatedOutfits(outfit, catalog.outfits)
  return <section className="look-section look-reveal" aria-labelledby="look-alternatives-title">
    <header className="look-section-heading"><div><p className="look-kicker">02 / A new perspective</p><h2 id="look-alternatives-title">STYLE IT <em>DIFFERENTLY</em></h2></div><p>More ways to wear your point of view.</p></header>
    {catalog.loading ? <p role="status">Finding related looks…</p> : related.length ? <div className="look-alternative-grid">{related.map(({ outfit: alternative, label }, index) => <Link className="look-alternative" key={alternative.id} to={`/outfits/${alternative.id}`} style={{ '--look-index': index }}>
      <div className="look-alternative__image"><LookImage src={alternative.imageUrl} alt={alternative.title} /><span>Explore look ↗</span></div>
      <p className="look-kicker">{alternative.category}</p><h3>{alternative.title}</h3><p className="look-alternative__label">{label}</p>
    </Link>)}</div> : <p className="look-empty">{catalog.error ? 'Related looks are unavailable right now.' : 'More looks in this aesthetic are on their way.'}</p>}
  </section>
}
