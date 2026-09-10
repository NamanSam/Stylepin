function PinOverlay() {
  return (
    <div className="pin__overlay" aria-hidden="true">
      <div className="pin__overlay-top">
        <span className="pin__save-btn" aria-label="Save outfit">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
            <path d="M19 21l-7-5-7 5V5a2 2 0 012-2h10a2 2 0 012 2z" />
          </svg>
        </span>
        <span className="pin__overflow-btn" aria-label="More options">
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
            <circle cx="12" cy="5" r="1" />
            <circle cx="12" cy="12" r="1" />
            <circle cx="12" cy="19" r="1" />
          </svg>
        </span>
      </div>
      <span className="pin__open-look">Open Look</span>
    </div>
  )
}

export default PinOverlay
