/**
 * 将后端返回的相对路径图片转为小程序可请求的完整 URL，避免 /profile/upload/xxx 请求到错误域名导致 500/404。
 * 规则：以 / 开头且非 // 的路径，拼上 basePath（如 https://kaiyueshangmao.xyz）。
 */
let getBasePath = () => {
  try {
    const app = typeof getApp === 'function' ? getApp() : null
    if (app && app.globalData && app.globalData.config && app.globalData.config.basePath) {
      return app.globalData.config.basePath
    }
  } catch (e) {}
  // 默认使用服务器地址，避免 localhost 在微信小程序中无法访问
  return (typeof __config !== 'undefined' && __config && __config.basePath) ? __config.basePath : 'https://kaiyueshangmao.xyz'
}

export function fullImageUrl(url) {
  if (url == null || typeof url !== 'string' || url === '') return ''
  const s = url.trim()
  if (s.startsWith('http://') || s.startsWith('https://') || s.startsWith('//')) return s
  if (s.startsWith('/')) {
    const base = getBasePath().replace(/\/$/, '')
    return base + s
  }
  return s
}

export default { fullImageUrl }
