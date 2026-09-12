// Campaign studies only: negative IDs must never navigate to live outfit details.
const photo = id => `https://images.unsplash.com/${id}?auto=format&fit=crop&w=700&q=80`
export default [
  { id: -101, title: 'Concrete & Leather', category: 'Streetwear', tags: ['oversized', 'neutral', 'editorial'], imageUrl: '/images/editorial/concrete-leather.jpg', isDemo: true, isGenerated: true, ratio: '2/3' },
  { id: -102, title: 'Soft Structure', category: 'Korean', tags: ['minimal', 'knitwear', 'oversized'], imageUrl: '/images/editorial/soft-structure.jpg', isDemo: true, isGenerated: true, ratio: '2/3' },
  { id: -103, title: 'The Moving Silhouette', category: 'Old Money', tags: ['minimal', 'trench', 'neutral'], imageUrl: '/images/editorial/trench-in-motion.jpg', isDemo: true, isGenerated: true, ratio: '2/3' },
  { id: -104, title: 'A Study in Yellow', category: 'Minimal', tags: ['monochrome', 'statement', 'editorial'], imageUrl: photo('photo-1515886657613-9f3515b0c78f'), isDemo: true, ratio: '2/3' },
  { id: -105, title: 'After Dark', category: 'Streetwear', tags: ['cinematic', 'night', 'statement'], imageUrl: photo('photo-1509631179647-0177331693ae'), isDemo: true, ratio: '2/3' },
  { id: -106, title: 'City in Layers', category: 'Streetwear', tags: ['street', 'layering', 'modern'], imageUrl: photo('photo-1529139574466-a303027c1d8b'), isDemo: true, ratio: '2/3' },
]
