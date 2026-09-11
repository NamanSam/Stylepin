import { Link } from 'react-router-dom'
import PinStandard from './PinStandard.jsx'
import PinEditorial from './PinEditorial.jsx'
import PinMinimal from './PinMinimal.jsx'
import PinTrend from './PinTrend.jsx'
import PinShoppable from './PinShoppable.jsx'
import PinDetail from './PinDetail.jsx'
import OutfitActions from './OutfitActions.jsx'

const PIN_COMPONENTS = {
  standard: PinStandard,
  editorial: PinEditorial,
  minimal: PinMinimal,
  trend: PinTrend,
  shoppable: PinShoppable,
  detail: PinDetail,
}

const VARIANT_SEQUENCE = ['standard', 'editorial', 'minimal', 'editorial', 'standard', 'minimal']

function assignVariant(outfit, index) {
  if (outfit.variant && PIN_COMPONENTS[outfit.variant]) return outfit.variant
  const tags = outfit.tags || []
  if (tags.some(t => t.toLowerCase().includes('trending'))) return 'trend'
  return VARIANT_SEQUENCE[index % VARIANT_SEQUENCE.length]
}

function FashionPin({ outfit, index, onRemove }) {
  const variant = assignVariant(outfit, index)
  const PinComponent = PIN_COMPONENTS[variant] || PinStandard

  return (
    <div
      className="fashion-pin"
      style={outfit.ratio ? { '--pin-ratio': outfit.ratio } : undefined}
    ><Link
      to={`/outfits/${outfit.id}`}
      className="pin-link"
      aria-label={`View outfit: ${outfit.title}`}
      style={{ '--stagger': index }}
    >
      <PinComponent outfit={outfit} />
    </Link><OutfitActions outfit={outfit} onRemove={onRemove} /></div>
  )
}

export default FashionPin
