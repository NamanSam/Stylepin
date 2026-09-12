import { useState } from 'react'

export default function LookImage({ src, alt, eager = false }) {
  const [failedSrc, setFailedSrc] = useState(null)
  return src && failedSrc !== src
    ? <img src={src} alt={alt} loading={eager ? 'eager' : 'lazy'} decoding="async" onError={() => setFailedSrc(src)} />
    : <span className="look-image-empty" role="img" aria-label={alt}>Image unavailable</span>
}
