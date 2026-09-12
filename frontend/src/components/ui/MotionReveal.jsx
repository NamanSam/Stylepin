import { useEffect, useRef } from 'react'

export default function MotionReveal({ children, className = '' }) {
  const ref = useRef(null)
  useEffect(() => {
    const element = ref.current
    const reduced = matchMedia('(prefers-reduced-motion: reduce)')
    if (reduced.matches || !window.IntersectionObserver) return
    const observer = new IntersectionObserver(entries => {
      if (entries[0].isIntersecting) { element.classList.remove('motion-awaiting'); observer.disconnect() }
    }, { threshold: .06 })
    function preferenceChanged() {
      if (reduced.matches) { element.classList.remove('motion-awaiting'); observer.disconnect() }
    }
    reduced.addEventListener('change', preferenceChanged)
    element.classList.add('motion-awaiting')
    observer.observe(element)
    return () => { observer.disconnect(); reduced.removeEventListener('change', preferenceChanged); element.classList.remove('motion-awaiting') }
  }, [])
  return <div ref={ref} className={`motion-reveal ${className}`}>{children}</div>
}
