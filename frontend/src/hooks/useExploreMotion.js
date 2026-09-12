import { useEffect, useRef } from 'react'
import { EXPLORE_SLOTS } from '../utils/exploreLayout.js'

export default function useExploreMotion(scene, paused, onRecycle) {
  const recycle = useRef(onRecycle)
  useEffect(() => { recycle.current = onRecycle }, [onRecycle])
  const clock = useRef(0)
  useEffect(() => {
    const root = scene.current
    if (!root) return
    const reduced = matchMedia('(prefers-reduced-motion: reduce)')
    const compact = matchMedia('(max-width: 740px), (pointer: coarse)')
    const cards = [...root.querySelectorAll('[data-explore-slot]')]
    let frame = 0, previous = 0, visible = false, disposed = false
    let width = root.clientWidth, height = root.clientHeight
    let targetX = 0, targetY = 0, x = 0, y = 0, scroll = 0
    const laps = cards.map((_, index) => Math.floor((EXPLORE_SLOTS[index].y + 35 - clock.current * EXPLORE_SLOTS[index].speed) / 165))
    const engaged = () => !!root.querySelector('.explore-image:hover') || root.contains(document.activeElement)
    const allowed = () => !disposed && !paused && !reduced.matches && !compact.matches && !document.hidden && visible && !engaged()
    function stop() { cancelAnimationFrame(frame); frame = 0; previous = 0 }
    function draw(now) {
      if (!allowed()) { stop(); return }
      const elapsed = previous ? Math.min((now - previous) / 1000, .05) : 0
      previous = now
      clock.current += elapsed * 1.8
      const ease = 1 - Math.exp(-elapsed * 5)
      x += (targetX - x) * ease
      y += (targetY - y) * ease
      cards.forEach((card, index) => {
        const slot = EXPLORE_SLOTS[index]
        const raw = slot.y + 35 - clock.current * slot.speed
        const lap = Math.floor(raw / 165)
        const position = ((raw % 165) + 165) % 165 - 35
        if (lap !== laps[index]) { laps[index] = lap; recycle.current(index) }
        const parallax = 1 + slot.depth / 450
        card.style.transform = `translate3d(${x * parallax}px,${height * position / 100 + y * parallax - scroll * slot.speed}px,${slot.depth}px) translate(-50%,-50%) rotateY(${slot.angle}deg) rotateZ(${slot.rotation}deg) scale(${slot.scale})`
        const outside = position < -8 || position > 108
        if (card.inert !== outside) card.inert = outside
      })
      frame = requestAnimationFrame(draw)
    }
    function start() { if (!frame && allowed()) frame = requestAnimationFrame(draw) }
    function sync() {
      stop()
      if (reduced.matches || compact.matches) cards.forEach(card => { card.style.removeProperty('transform'); card.inert = false })
      start()
    }
    function pointer(event) {
      const bounds = root.getBoundingClientRect()
      targetX = ((event.clientX - bounds.left) / width - .5) * 30
      targetY = ((event.clientY - bounds.top) / height - .5) * 20
    }
    function resetPointer() { targetX = 0; targetY = 0 }
    function engagement() { sync() }
    function leave() { resetPointer(); sync() }
    function focusOut() { queueMicrotask(sync) }
    function onScroll() { scroll = Math.max(-60, Math.min(60, -root.getBoundingClientRect().top * .08)) }
    const observer = new IntersectionObserver(entries => { visible = entries[0].isIntersecting; sync() }, { threshold: 0 })
    observer.observe(root)
    const resize = new ResizeObserver(() => { width = root.clientWidth; height = root.clientHeight })
    resize.observe(root)
    root.addEventListener('pointermove', pointer, { passive: true })
    root.addEventListener('pointerover', engagement)
    root.addEventListener('pointerout', engagement)
    root.addEventListener('pointerleave', leave)
    root.addEventListener('focusin', engagement)
    root.addEventListener('focusout', focusOut)
    window.addEventListener('scroll', onScroll, { passive: true })
    document.addEventListener('visibilitychange', sync)
    reduced.addEventListener('change', sync)
    compact.addEventListener('change', sync)
    start()
    return () => {
      disposed = true
      stop(); observer.disconnect(); resize.disconnect()
      root.removeEventListener('pointermove', pointer)
      root.removeEventListener('pointerover', engagement)
      root.removeEventListener('pointerout', engagement)
      root.removeEventListener('pointerleave', leave)
      root.removeEventListener('focusin', engagement)
      root.removeEventListener('focusout', focusOut)
      window.removeEventListener('scroll', onScroll)
      document.removeEventListener('visibilitychange', sync)
      reduced.removeEventListener('change', sync)
      compact.removeEventListener('change', sync)
    }
  }, [scene, paused])
}
