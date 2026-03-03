/**
 * 从腾讯 WeUI 官方仓库下载 tabBar 图标（微信/腾讯系设计资源）
 * 源：https://github.com/Tencent/weui-wxss (WeUI 为微信官方设计团队提供)
 * 运行：node scripts/download-tabbar-icons.js
 */
const https = require('https')
const fs = require('fs')
const path = require('path')

const BASE = 'https://raw.githubusercontent.com/Tencent/weui-wxss/master/dist/example/images'
const OUT_DIR = path.join(__dirname, '..', 'static', 'tabbar')

// 本项目需要的文件名 -> WeUI 中的图标（同一图标未选中/选中可复用，后续可替换为高亮版）
const MAP = [
  { file: 'home.png', url: `${BASE}/icon_nav_search.png` },
  { file: 'home-active.png', url: `${BASE}/icon_nav_search.png` },
  { file: 'category.png', url: `${BASE}/icon_nav_layout.png` },
  { file: 'category-active.png', url: `${BASE}/icon_nav_layout.png` },
  { file: 'user.png', url: `${BASE}/icon_nav_nav.png` },
  { file: 'user-active.png', url: `${BASE}/icon_nav_nav.png` }
]

function get(url) {
  return new Promise((resolve, reject) => {
    https.get(url, { headers: { 'User-Agent': 'Node.js' } }, (res) => {
      if (res.statusCode === 301 || res.statusCode === 302) {
        return get(res.headers.location).then(resolve).catch(reject)
      }
      if (res.statusCode !== 200) {
        reject(new Error(url + ' ' + res.statusCode))
        return
      }
      const chunks = []
      res.on('data', (c) => chunks.push(c))
      res.on('end', () => resolve(Buffer.concat(chunks)))
      res.on('error', reject)
    }).on('error', reject)
  })
}

if (!fs.existsSync(OUT_DIR)) {
  fs.mkdirSync(OUT_DIR, { recursive: true })
}

;(async () => {
  for (const { file, url } of MAP) {
    try {
      const buf = await get(url)
      const outPath = path.join(OUT_DIR, file)
      fs.writeFileSync(outPath, buf)
      console.log('OK:', file)
    } catch (e) {
      console.error('FAIL:', file, e.message)
    }
  }
  console.log('Done. Icons from Tencent WeUI (https://github.com/Tencent/weui-wxss).')
})()
