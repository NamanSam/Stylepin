import { useEffect, useRef } from 'react'
import LookHero from './LookHero.jsx'
import ShopTheLook from './ShopTheLook.jsx'
import AlternativeLooks from './AlternativeLooks.jsx'

export default function LookBreakdown({ outfit, catalog }) {
  const container = useRef(null)
  useEffect(() => {
    if (!window.IntersectionObserver || window.matchMedia('(prefers-reduced-motion: reduce)').matches) return
    const observer = new IntersectionObserver(entries => entries.forEach(entry => {
      if (entry.isIntersecting) { entry.target.classList.remove('look-awaiting'); observer.unobserve(entry.target) }
    }), { threshold: 0.05 })
    container.current.querySelectorAll('.look-reveal').forEach(section => {
      section.classList.add('look-awaiting')
      observer.observe(section)
    })
    return () => observer.disconnect()
  }, [])
  return <div ref={container} className="look-breakdown"><LookHero outfit={outfit} /><ShopTheLook outfit={outfit} outfits={catalog.outfits} /><AlternativeLooks outfit={outfit} catalog={catalog} /></div>
}
