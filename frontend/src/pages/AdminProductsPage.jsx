import { useEffect, useRef, useState } from 'react'
import { fetchProducts, fetchProductCategories, createProduct, updateProduct, deleteProduct } from '../api/adminProductApi.js'
import './admin-products.css'

const emptyForm = () => ({ name: '', brand: '', price: '', imageUrl: '', productUrl: '', retailer: '', category: '', tags: '', available: true })
const priceFormatter = new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR', minimumFractionDigits: 0, maximumFractionDigits: 2 })
function validUrl(value, shopping = false) {
  try {
    const url = new URL(value.trim())
    const host = url.hostname.toLowerCase().replace(/\.$/, '')
    return ['http:', 'https:'].includes(url.protocol) && !url.username && !url.password &&
      (!shopping || !(/(^|\.)(example\.(com|org|net)|localhost)$|\.(invalid|test|example)$/.test(host)))
  } catch { return false }
}
function validate(form) {
  const errors = {}
  for (const key of ['name', 'brand', 'price', 'imageUrl', 'productUrl', 'category']) {
    if (!String(form[key]).trim()) errors[key] = 'This field is required.'
  }
  if (form.price && (!/^\d+(\.\d{1,2})?$/.test(form.price) || Number(form.price) <= 0 || Number(form.price) > 99999999.99)) errors.price = 'Enter an INR price from ₹0.01 to ₹9,99,99,999.99, with up to two decimal places.'
  if (form.imageUrl && !validUrl(form.imageUrl)) errors.imageUrl = 'Enter a valid HTTP(S) image URL.'
  if (form.productUrl && !validUrl(form.productUrl, true)) errors.productUrl = 'Enter a valid retailer URL, not a placeholder.'
  const tags = form.tags.split(',').map(tag => tag.trim()).filter(Boolean)
  if (tags.length > 30 || tags.some(tag => tag.length > 50)) errors.tags = 'Use up to 30 tags, each at most 50 characters.'
  return errors
}
function ProductImage({ url, name, className }) {
  const [failed, setFailed] = useState(false)
  if (!validUrl(url || '') || failed) return <div className={`${className} admin-image-empty`}><span>{failed ? 'Image unavailable' : 'Image preview'}</span></div>
  return <img className={className} src={url} alt={name || 'Product preview'} onError={() => setFailed(true)} referrerPolicy="no-referrer" />
}
function DeleteDialog({ product, busy, error, onCancel, onConfirm }) {
  const dialog = useRef(null)
  useEffect(() => { dialog.current.showModal() }, [])
  return <dialog ref={dialog} className="admin-delete-dialog" aria-labelledby="delete-product-title" onCancel={event => { event.preventDefault(); if (!busy) onCancel() }}>
    <span className="eyebrow">Product management</span><h2 id="delete-product-title">Delete this piece?</h2>
    <p><strong>{product.name}</strong> will be permanently removed from the product catalog.</p>
    {error && <p role="alert" className="form-error">{error}</p>}
    <div className="admin-form-actions"><button type="button" className="admin-secondary" onClick={onCancel} disabled={busy} autoFocus>Keep product</button><button type="button" className="solid-button" onClick={onConfirm} disabled={busy}>{busy ? 'Deleting…' : 'Delete product'}</button></div>
  </dialog>
}
export default function AdminProductsPage() {
  const [form, setForm] = useState(emptyForm)
  const [editing, setEditing] = useState(null)
  const [errors, setErrors] = useState({})
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [busy, setBusy] = useState(false)
  const [result, setResult] = useState(null)
  const [page, setPage] = useState(0)
  const [revision, setRevision] = useState(0)
  const [loading, setLoading] = useState(true)
  const [listError, setListError] = useState('')
  const [categories, setCategories] = useState([])
  const [deleting, setDeleting] = useState(null)
  const [deleteError, setDeleteError] = useState('')
  const formRef = useRef(null)
  useEffect(() => {
    let ignore = false
    fetchProducts(page).then(data => { if (!ignore) setResult(data) })
      .catch(e => { if (!ignore) setListError(e.message) }).finally(() => { if (!ignore) setLoading(false) })
    return () => { ignore = true }
  }, [page, revision])
  useEffect(() => {
    let ignore = false
    fetchProductCategories().then(data => { if (!ignore) setCategories(data) }).catch(() => { /* Free-text category entry remains available. */ })
    return () => { ignore = true }
  }, [revision])
  function reload(nextPage = page) { setLoading(true); setListError(''); setPage(nextPage); setRevision(value => value + 1) }
  function reset() { setForm(emptyForm()); setEditing(null); setErrors({}); setError('') }
  function change(event) {
    const { name, value, checked, type } = event.target
    setForm(previous => ({ ...previous, [name]: type === 'checkbox' ? checked : value }))
    setErrors(previous => ({ ...previous, [name]: undefined })); setSuccess('')
  }
  async function submit(event) {
    event.preventDefault(); setError(''); setSuccess('')
    const nextErrors = validate(form)
    setErrors(nextErrors)
    if (Object.keys(nextErrors).length) { formRef.current.elements.namedItem(Object.keys(nextErrors)[0])?.focus(); return }
    setBusy(true)
    const body = { ...form, price: Number(form.price), tags: form.tags.split(',').map(tag => tag.trim()).filter(Boolean) }
    try {
      if (editing) await updateProduct(editing, body)
      else await createProduct(body)
      setSuccess(editing ? 'Product updated.' : 'Product added to your catalog.'); reset(); reload(0)
    } catch (e) { setError(e.message); setErrors(e.fieldErrors || {}) } finally { setBusy(false) }
  }
  function edit(product) {
    setForm({ name: product.name || '', brand: product.brand || '', price: String(product.price ?? ''), imageUrl: product.imageUrl || '', productUrl: product.productUrl || '', retailer: product.retailer || '', category: product.category || '', tags: (product.tags || []).join(', '), available: product.available })
    setEditing(product.id); setErrors({}); setError(''); setSuccess('')
    formRef.current.scrollIntoView({ block: 'start' }); formRef.current.elements.namedItem('name').focus({ preventScroll: true })
  }
  async function remove() {
    setBusy(true); setDeleteError(''); setSuccess('')
    try {
      await deleteProduct(deleting.id)
      if (editing === deleting.id) reset()
      setDeleting(null); setSuccess('Product deleted.'); reload(0)
    } catch (e) { setDeleteError(e.message) } finally { setBusy(false) }
  }
  function field(name, label, { required = false, maxLength, ...props } = {}) {
    return <label className="admin-field" htmlFor={`product-${name}`}>{label}{required && ' *'}
      <input id={`product-${name}`} name={name} value={form[name]} onChange={change} required={required} maxLength={maxLength} aria-invalid={!!errors[name]} aria-describedby={errors[name] ? `error-${name}` : undefined} {...props} />
      {errors[name] && <span className="form-error" id={`error-${name}`}>{errors[name]}</span>}
    </label>
  }
  return <main className="admin-products">
    <header className="admin-heading"><span className="eyebrow">StylePin / Private workspace</span><h1>The product <em>edit.</em></h1><p>A considered catalog. Add the pieces behind the looks.</p></header>
    <section className="admin-workspace" aria-labelledby="product-form-title">
      <form ref={formRef} className="admin-product-form" onSubmit={submit} noValidate>
        <div className="admin-section-heading"><span className="eyebrow">01 / Product details</span><h2 id="product-form-title">{editing ? 'Refine a piece.' : 'Add a new piece.'}</h2></div>
        <fieldset disabled={busy}><div className="admin-fields">
          {field('name', 'Product Name', { required: true, maxLength: 200 })}
          {field('brand', 'Brand', { required: true, maxLength: 100 })}
          {field('price', 'Price ₹', { required: true, inputMode: 'decimal' })}
          {field('retailer', 'Retailer', { maxLength: 100 })}
          {field('category', 'Category', { required: true, maxLength: 100, list: 'product-categories' })}
          <datalist id="product-categories">{categories.map(category => <option key={category} value={category} />)}</datalist>
          {field('tags', 'Tags', { placeholder: 'Separate tags with commas', maxLength: 1550 })}
          {field('imageUrl', 'Image URL', { required: true, type: 'url', maxLength: 2048 })}
          {field('productUrl', 'Product URL', { required: true, type: 'url', maxLength: 2048 })}
        </div><p className="admin-help">Image URL displays the piece. Product URL opens the retailer’s shopping page. All prices are in INR.</p>
        <label className="admin-availability"><input type="checkbox" name="available" checked={form.available} onChange={change} /><span>Available<span className="admin-help">Currently available from the retailer</span></span></label>
        <div className="admin-form-actions"><button className="solid-button" type="submit">{busy ? 'Saving…' : editing ? 'Save changes' : 'Add product'} <span aria-hidden="true">↗</span></button><button className="admin-secondary" type="button" onClick={reset}>{editing ? 'Cancel edit' : 'Reset form'}</button></div>
        </fieldset>
        {error && <p className="form-error" role="alert">{error}</p>}
        {success && <p className="admin-success" role="status">{success}</p>}
      </form>
      <aside className="admin-preview" aria-label="Live product preview"><span className="eyebrow">A first look</span>
        <ProductImage key={form.imageUrl} url={form.imageUrl} name={form.name} className="admin-preview-image" />
        <span className="eyebrow">{form.brand || 'Your brand'}</span><h2>{form.name || 'The next considered piece.'}</h2><p className="admin-preview-price">{Number(form.price) > 0 && Number.isFinite(Number(form.price)) ? priceFormatter.format(Number(form.price)) : '₹ —'}</p>
        <p className="admin-help">{form.category || 'Category'}{form.retailer ? ` / ${form.retailer}` : ''}</p>
      </aside>
    </section>
    <section className="admin-recent" aria-labelledby="recent-products-title"><div className="admin-section-heading"><span className="eyebrow">02 / Your catalog</span><h2 id="recent-products-title">Recent products</h2><p className="admin-help">Newest additions first. Products attached to outfits are protected from deletion.</p></div>
      {loading ? <p role="status">Loading products…</p> : listError ? <div role="alert"><p>{listError}</p><button className="admin-secondary" onClick={() => reload()}>Retry</button></div> : <>
        {!result?.items.length ? <p className="admin-empty">Your catalog starts here. Add your first product above.</p> : <ul className="admin-product-list">{result.items.map(product => <li key={product.id}>
          <ProductImage key={product.imageUrl} url={product.imageUrl} name={product.name} className="admin-thumbnail" />
          <div className="admin-product-info"><span className="eyebrow">{product.brand || 'Unbranded'}</span><h3>{product.name}</h3><p>{product.category || 'Uncategorized'} <span aria-hidden="true">/</span> {product.retailer || 'Retailer not set'}</p></div>
          <div className="admin-product-meta"><strong>{product.price == null ? 'Price not set' : priceFormatter.format(product.price)}</strong><span className="admin-stock">{product.available ? 'Available' : 'Unavailable'}</span>{product.outfitId && <small>Attached to outfit #{product.outfitId}</small>}</div>
          <div className="admin-row-actions"><button onClick={() => edit(product)} disabled={busy}>Edit<span className="admin-sr-only"> {product.name}</span></button><button onClick={() => { setDeleting(product); setDeleteError('') }} disabled={busy || !!product.outfitId} title={product.outfitId ? 'Attached products cannot be deleted' : undefined}>Delete<span className="admin-sr-only"> {product.name}</span></button></div>
        </li>)}</ul>}
        {result?.totalPages > 1 && <nav className="admin-pagination" aria-label="Product pages"><button className="admin-secondary" disabled={page === 0 || busy} onClick={() => reload(page - 1)}>Previous</button><span>Page {page + 1} of {result.totalPages}</span><button className="admin-secondary" disabled={page + 1 >= result.totalPages || busy} onClick={() => reload(page + 1)}>Next</button></nav>}
      </>}
    </section>
    {deleting && <DeleteDialog product={deleting} busy={busy} error={deleteError} onCancel={() => setDeleting(null)} onConfirm={remove} />}
  </main>
}

