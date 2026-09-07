import OutfitCard from './components/OutfitCard.jsx'
import outfits from './data/outfits.js'

function App() {
  return (
    <div className="page">
      <header className="home">
        <h1>StylePin</h1>
        <p>Discover Your Style</p>
      </header>

      <main className="feed">
        {outfits.map((outfit) => (
          <OutfitCard
            key={outfit.id}
            imageUrl={outfit.imageUrl}
            title={outfit.title}
            category={outfit.category}
          />
        ))}
      </main>
    </div>
  )
}

export default App
