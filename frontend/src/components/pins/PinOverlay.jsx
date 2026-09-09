function PinOverlay() {
  return (
    <div className="pin__overlay" aria-hidden="true">
      <span className="pin__save-btn" aria-label="Save outfit">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
          <path d="M19 21l-7-5-7 5V5a2 2 0 012-2h10a2 2 0 012 2z" />
        </svg>
      </span>
      <span className="pin__open-look">Open Look</span>
    </div>
  )
}

export default PinOverlay
