const inrFormatter = new Intl.NumberFormat('en-IN', {
  style: 'currency',
  currency: 'INR',
  maximumFractionDigits: 0,
})

export function formatINR(price) {
  if (price == null) return null
  return inrFormatter.format(Number(price))
}
