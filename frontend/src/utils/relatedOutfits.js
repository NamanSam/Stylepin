const normalize = value => String(value || '').toLowerCase().trim()
const styleTerms = new Set('minimal minimalist casual relaxed streetwear smart tailored formal sporty vintage classic modern monochrome neutral denim layered elegant bohemian summer winter oversized polished'.split(' '))
const terms = outfit => new Set(normalize([outfit.title, outfit.description, ...(outfit.tags || [])].join(' ')).split(/[^a-z]+/).filter(word => styleTerms.has(word)))

export function rankRelatedOutfits(current, outfits, limit = 6) {
  const tags = new Set((current.tags || []).map(normalize))
  const aesthetic = terms(current)
  const seen = new Set([String(current.id)])
  return outfits.filter(outfit => {
    if (!outfit?.id || outfit.isDemo || seen.has(String(outfit.id))) return false
    seen.add(String(outfit.id))
    return true
  }).map(outfit => {
    const category = Boolean(current.category && normalize(current.category) === normalize(outfit.category))
    const sharedTags = [...new Set((outfit.tags || []).map(normalize))].filter(tag => tags.has(tag)).length
    const sharedStyle = [...terms(outfit)].filter(term => aesthetic.has(term)).length
    return { outfit, category, sharedTags, sharedStyle, label: category ? 'Same aesthetic' : sharedTags ? 'Shared style notes' : 'A similar point of view' }
  }).filter(item => item.category || item.sharedTags || item.sharedStyle)
    .sort((a, b) => Number(b.category) - Number(a.category) || b.sharedTags - a.sharedTags || b.sharedStyle - a.sharedStyle || String(a.outfit.id).localeCompare(String(b.outfit.id)))
    .slice(0, limit)
}

const types = [
  ['Outerwear', /\b(jacket|coat|blazer|bomber|overshirt|parka|outerwear)\b/, 'left'],
  ['Shoes', /\b(shoes?|sneakers?|boots?|loafers?|sandals?|heels?|footwear|trainers?)\b/, 'right'],
  ['Bottoms', /\b(pants?|trousers?|jeans|cargos?|shorts?|skirts?|chinos|bottoms)\b/, 'right'],
  ['Layer', /\b(tops?|shirts?|t-shirts?|tees?|knitwear|sweaters?|hoodies?|cardigans?|blouses?|vests?)\b/, 'left'],
  ['Accessories', /\b(bags?|watch|watches|glasses|sunglasses|belts?|scarves|scarf|hats?|jewel\w*|necklace|earrings?|accessories|tote)\b/, 'left'],
  ['One piece', /\b(dress|jumpsuit|romper)\b/, 'left'],
]
export function productType(product) {
  // Seed categories describe the outfit aesthetic, so inspect the product name first.
  const match = types.find(([, pattern]) => pattern.test(normalize(product.name))) || types.find(([, pattern]) => pattern.test(normalize(product.category)))
  return match ? { label: match[0], side: match[2] } : { label: 'Piece', side: null }
}
export function storeUrl(value) {
  try {
    const url = new URL(value)
    if (!['https:', 'http:'].includes(url.protocol) || url.username || url.password || /(^|\.)(example\.(com|org|net)|localhost|invalid|test)$/.test(url.hostname)) return null
    return url.href
  } catch { return null }
}
export function alternativePieces(product, current, outfits) {
  const type = productType(product).label
  if (type === 'Piece') return []
  const seen = new Set((current.products || []).map(item => storeUrl(item.productUrl)).filter(Boolean))
  return outfits.filter(outfit => String(outfit.id) !== String(current.id) && !outfit.isDemo)
    .flatMap(outfit => outfit.products || []).filter(item => {
      const url = storeUrl(item.productUrl)
      if (!url || seen.has(url) || productType(item).label !== type || normalize(item.name) === normalize(product.name)) return false
      seen.add(url)
      return true
    }).slice(0, 3)
}
