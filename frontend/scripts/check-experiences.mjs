import assert from 'node:assert/strict'
import { createServer } from 'vite'
import { createElement as h } from 'react'
import { renderToStaticMarkup } from 'react-dom/server'
import { MemoryRouter } from 'react-router-dom'

// Render-only fixtures: never sent to the API or inserted into the app catalog.
const server = await createServer({ server: { middlewareMode: true } })
try {
  const { default: Auth } = await server.ssrLoadModule('/src/auth/AuthContext.js')
  const { default: Saved } = await server.ssrLoadModule('/src/context/SavedOutfitsContext.js')
  const { default: Feed } = await server.ssrLoadModule('/src/pages/FeedPage.jsx')
  const { default: Explore } = await server.ssrLoadModule('/src/pages/ExplorePage.jsx')
  const outfits = Array.from({ length: 40 }, (_, index) => ({
    id: index + 1, title: `Look ${index + 1}`, category: index % 2 ? 'Minimal' : 'Streetwear',
    tags: [index % 2 ? 'quiet' : 'layered'], imageUrl: `https://example.test/look-${index + 1}.jpg`,
  }))
  const render = (Component, props) => renderToStaticMarkup(h(MemoryRouter, {},
    h(Auth.Provider, { value: { user: null } },
      h(Saved.Provider, { value: { outfits: [], loading: false } }, h(Component, {
        outfits, activeCategory: 'For You', searchQuery: '', onCategoryChange() {}, ...props,
      })))))
  const feed = render(Feed)
  assert.equal((feed.match(/<article /g) || []).length, 40, 'Discover must render the complete catalog')
  for (const outfit of outfits) assert(feed.includes(`href="/outfits/${outfit.id}"`))
  assert.equal((feed.match(/>Save<\/button>/g) || []).length, 40)
  assert.equal((feed.match(/>Board \+<\/button>/g) || []).length, 40)
  assert.equal((render(Feed, { activeCategory: 'Minimal' }).match(/<article /g) || []).length, 20)
  assert.equal((render(Feed, { searchQuery: 'layered' }).match(/<article /g) || []).length, 20)
  const explore = render(Explore)
  assert.equal((explore.match(/data-explore-slot=/g) || []).length, 18)
  assert(!explore.includes('masonry-feed'), 'Explore must not become a second Discover grid')
  assert(explore.includes('href="/#discover"'))
  const preview = render(Explore, { outfits: [{ ...outfits[0], id: -1, isDemo: true }], usedDev: true })
  assert(!preview.includes('href="/outfits/-1"'))
  console.log('PASS: complete 40-look Discover, search/categories, Save/Board controls, real detail links, bounded spatial Explore, Discover transition, preview link safety.')
} finally {
  await server.close()
}
// Imported auth modules keep a BroadcastChannel open; this render-only check is complete.
process.exit(0)
