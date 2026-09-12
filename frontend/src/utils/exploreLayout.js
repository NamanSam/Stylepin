// Deliberately staggered positions, not lanes. First six form the mobile edit too.
export const EXPLORE_SLOTS = [
  [16, 38, 65, -8, -5, .62, 1.08], [82, 23, -35, 8, 4, .48, 1.0],
  [37, 5, -240, -6, -3, .32, .77], [90, 83, 95, -9, 6, .7, 1.17],
  [9, 96, -65, 7, -4, .52, .88], [66, 101, -190, -5, 3, .38, .86],
  [5, -18, -180, 8, 4, .43, .82], [73, 67, -290, -6, -5, .3, .72],
  [28, 83, -250, 6, 3, .36, .78], [91, -25, -110, -7, -3, .57, .92],
  [46, 118, -330, 4, 5, .28, .68], [22, -31, -70, -8, -6, .58, .94],
  [83, 121, -140, 7, 4, .46, .87], [4, 61, -280, -4, 3, .31, .75],
  [60, -29, -300, 5, -4, .29, .72], [98, 42, -220, -8, 5, .4, .81],
  [32, 127, -130, 6, -3, .5, .9], [71, -14, -90, -5, 4, .54, .95],
].map(([x, y, depth, angle, rotation, speed, scale]) => ({ x, y, depth, angle, rotation, speed, scale }))

export function exploreOutfits(outfits) {
  const seen = new Set()
  // Exclude only recognized stock assets from campaign placement, never discovery.
  const unsuitable = ['1617137968427', '1552374196', '1507679799987', '1617137984095', '1617127365659', '1490481651871', '1523381210434', '1506629082955', '1495385794356', '1469334031218', '1445205170230', '1543087903', '1534528741775', '1524504388940', '1500648767791', '1521510895919', '1544441893', '1509316975850', '1434389677669', '1590874103328', '1490578474895', '1483985988355', '1560250097', '1509233725247', '1485738422979', '1509941943102', '1490114538077']
  const editorial = ['1515886657613', '1509631179647', '1529139574466']
  const score = outfit => {
    if (outfit.isGenerated) return 20
    if (editorial.some(photo => outfit.imageUrl.includes(photo))) return 10
    const direction = [outfit.category, ...(outfit.tags || [])].join(' ')
    return /streetwear|korean|minimal|oversized|old.money|editorial/i.test(direction) ? 5 : 0
  }
  return outfits.filter(outfit => {
    if (!outfit?.imageUrl || seen.has(outfit.imageUrl)) return false
    if (unsuitable.some(photo => outfit.imageUrl.includes(photo))) return false
    seen.add(outfit.imageUrl)
    return true
  }).sort((a, b) => score(b) - score(a))
}
export function outfitImageUrl(src, width = 600) {
  if (!src) return src
  try {
    const url = new URL(src)
    if (url.hostname === 'images.unsplash.com') {
      url.searchParams.set('w', String(width))
      url.searchParams.set('q', '80')
      url.searchParams.delete('h')
    }
    return url.href
  } catch { return src }
}

