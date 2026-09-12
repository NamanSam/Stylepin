import { Link, useLocation } from 'react-router-dom'
import ThemeToggle from '../ui/ThemeToggle.jsx'
import AccountMenu from './AccountMenu.jsx'
export default function DiscoveryHeader({ searchQuery, onSearchChange, theme, onToggleTheme }) {
  const { pathname } = useLocation()
  return <header className="editorial-header">
    <div className="editorial-nav"><Link to="/" className="editorial-logo" aria-label="StylePin home">StylePin<span> / </span></Link>
      <nav className="editorial-links" aria-label="Main navigation"><Link to="/" aria-current={pathname === '/' ? 'page' : undefined}>Home</Link><Link to="/explore" aria-current={pathname === '/explore' ? 'page' : undefined}>Explore</Link><Link to="/#discover">Discover</Link><Link to="/#collections">The edit</Link></nav>
      <div className="editorial-account"><ThemeToggle theme={theme} onToggle={onToggleTheme} /><AccountMenu /></div>
    </div>
    {pathname === '/' && <div className="editorial-search"><span>FASHION, FROM YOUR POINT OF VIEW.</span><label><span className="sr-only">Search outfits, categories and tags</span><input type="search" placeholder="Find your inspiration" value={searchQuery} onChange={e => onSearchChange(e.target.value)} /><span aria-hidden="true">⌕</span></label></div>}
  </header>
}
