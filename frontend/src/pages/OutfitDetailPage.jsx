import { useEffect, useState } from 'react'
import { useParams, Link } from 'react-router-dom'
import { fetchOutfitById } from '../api/outfitApi.js'
import ProductCard from '../components/ProductCard.jsx'

function OutfitDetailPage() {
  const { id } = useParams()
  const [outfit, setOutfit] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    let ignore = false

    async function loadOutfit() {
      try {
        setLoading(true)
        setError(null)
        const data = await fetchOutfitById(id)
        if (!ignore) {
          setOutfit(data)
        }
      } catch (err) {
        if (!ignore) {
          setError(err)
        }
      } finally {
        if (!ignore) {
          setLoading(false)
        }
      }
    }

    loadOutfit()

    return () => {
      ignore = true
    }
  }, [id])

  return (
    <div className="page outfit-detail-page">
      <nav className="detail-nav" aria-label="Breadcrumb">
        <div className="detail-nav__inner">
          <Link to="/" className="detail-nav__back">
            <span aria-hidden="true">←</span> Back to Feed
          </Link>
          <Link to="/" className="detail-nav__logo">
            StylePin
          </Link>
        </div>
      </nav>

      <main className="detail-container">
        {loading && (
          <div className="detail-status" role="status">
            <div className="detail-status__spinner" aria-hidden="true" />
            <p>Loading outfit details...</p>
          </div>
        )}

        {!loading && error && error.status === 404 && (
          <div className="detail-status detail-status--not-found" role="alert">
            <p className="detail-status__badge">404</p>
            <h2 className="detail-status__title">Outfit Not Found</h2>
            <p className="detail-status__desc">
              The look you are searching for does not exist or may have been removed.
            </p>
            <Link to="/" className="detail-status__button">
              Return to Fashion Feed
            </Link>
          </div>
        )}

        {!loading && error && error.status !== 404 && (
          <div className="detail-status detail-status--error" role="alert">
            <h2 className="detail-status__title">Unable to Load Outfit</h2>
            <p className="detail-status__desc">{error.message || 'An unexpected error occurred.'}</p>
            <p className="detail-status__subtext">
              Please check that the backend is running at http://localhost:8080.
            </p>
            <Link to="/" className="detail-status__button">
              Return to Fashion Feed
            </Link>
          </div>
        )}

        {!loading && !error && outfit && (
          <>
            <section className="outfit-hero">
              <div className="outfit-hero__image-column">
                <img
                  className="outfit-hero__image"
                  src={outfit.imageUrl}
                  alt={outfit.title}
                />
              </div>

              <div className="outfit-hero__info-column">
                {outfit.category && (
                  <p className="outfit-hero__category">{outfit.category}</p>
                )}
                <h1 className="outfit-hero__title">{outfit.title}</h1>
                {outfit.description && (
                  <p className="outfit-hero__description">{outfit.description}</p>
                )}

                {outfit.tags && outfit.tags.length > 0 && (
                  <div className="outfit-hero__tags" aria-label="Tags">
                    {outfit.tags.map((tag) => (
                      <span key={tag} className="outfit-hero__tag">
                        #{tag}
                      </span>
                    ))}
                  </div>
                )}
              </div>
            </section>

            <section className="shop-the-look" aria-labelledby="shop-the-look-heading">
              <div className="shop-the-look__header">
                <h2 id="shop-the-look-heading" className="shop-the-look__title">
                  Shop the Look
                </h2>
                <p className="shop-the-look__subtitle">
                  Curated pieces featured in this outfit
                </p>
              </div>

              {outfit.products && outfit.products.length > 0 ? (
                <div className="shop-the-look__grid">
                  {outfit.products.map((product) => (
                    <ProductCard key={product.id} product={product} />
                  ))}
                </div>
              ) : (
                <div className="shop-the-look__empty">
                  <p>No products currently available for this look.</p>
                </div>
              )}
            </section>
          </>
        )}
      </main>
    </div>
  )
}

export default OutfitDetailPage