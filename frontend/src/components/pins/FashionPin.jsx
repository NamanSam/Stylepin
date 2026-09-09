import { Link } from 'react-router-dom'
import PinStandard from './PinStandard.jsx'
import PinEditorial from './PinEditorial.jsx'
import PinMinimal from './PinMinimal.jsx'
import PinTrend from './PinTrend.jsx'
import PinShoppable from './PinShoppable.jsx'

const PIN_COMPONENTS = {
  standard: PinStandard,
  editorial: PinEditorial,
  minimal: PinMinimal,
  trend: PinTrend,
  shoppable: PinShoppable,
}

function FashionPin({ outfit, index }) {
  const variant = outfit.variant || 'standard'
  const PinComponent = PIN_COMPONENTS[variant] || PinStandard

  return (
    <Link
      to={`/outfits/${outfit.id}`}
      className="pin-link"
      aria-label={`View outfit: ${outfit.title}`}
      style={{ '--stagger': index }}
    >
      <PinComponent outfit={outfit} />
    </Link>
  )
}

export default FashionPin
