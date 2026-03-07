/**
 * 从 Element Plus 图标集（Iconify API，ep 前缀）下载 tabBar 图标并转为 PNG
 * 图标风格与 Element UI / Element Plus 一致
 * 运行：node scripts/download-tabbar-icons-element.js
 *
 * 依赖（可选，用于 SVG→PNG）：npm install sharp --save-dev
 * 若未安装 sharp，脚本会仅下载 SVG，需自行用在线工具转为 81×81 PNG
 */
const https = require('https')
const fs = require('fs')
const path = require('path')

const OUT_DIR = path.join(__dirname, '..', 'static', 'tabbar')
const SIZE = 81
const ICON_BASE = 'https://api.iconify.design/ep'

// 输出文件名 -> Element Plus 图标名（ep 集）
const MAP = [
  { file: 'home.png', icon: 'house', activeFile: 'home-active.png', activeIcon: 'home-filled' },
  { file: 'category.png', icon: 'grid', activeFile: 'category-active.png', activeIcon: 'grid' },
  { file: 'cart.png', icon: 'shopping-cart', activeFile: 'cart-active.png', activeIcon: 'shopping-cart-full' },
  { file: 'user.png', icon: 'user', activeFile: 'user-active.png', activeIcon: 'user-filled' }
]

function get(url) {
  return new Promise((resolve, reject) => {
    https.get(url, { headers: { 'User-Agent': 'Node.js' } }, (res) => {
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

function flattenMap() {
  const list = []
  MAP.forEach(({ file, icon, activeFile, activeIcon }) => {
    list.push({ file, icon })
    if (activeFile && activeIcon) list.push({ file: activeFile, icon: activeIcon })
  })
  return list
}

async function downloadSvg(iconName) {
  const url = `${ICON_BASE}/${iconName}.svg`
  const buf = await get(url)
  return buf.toString('utf8')
}

async function svgToPng(svgContent, outPath) {
  try {
    const sharp = require('sharp')
    const buf = Buffer.from(svgContent, 'utf8')
    await sharp(buf)
      .resize(SIZE, SIZE)
      .png()
      .toFile(outPath)
    return true
  } catch (e) {
    console.warn('sharp 未安装或转换失败，将只保留 SVG:', e.message)
    return false
  }
}

if (!fs.existsSync(OUT_DIR)) {
  fs.mkdirSync(OUT_DIR, { recursive: true })
}

;(async () => {
  const list = flattenMap()
  let sharpAvailable = true
  for (const { file, icon } of list) {
    try {
      const svg = await downloadSvg(icon)
      const base = file.replace(/\.png$/, '')
      const svgPath = path.join(OUT_DIR, base + '.svg')
      fs.writeFileSync(svgPath, svg, 'utf8')
      console.log('OK (SVG):', base + '.svg')
      const pngPath = path.join(OUT_DIR, file)
      if (sharpAvailable) {
        const ok = await svgToPng(svg, pngPath)
        if (ok) console.log('OK (PNG):', file)
        else sharpAvailable = false
      }
    } catch (e) {
      console.error('FAIL:', icon, e.message)
    }
  }
  if (!sharpAvailable) {
    console.log('\n未检测到 sharp，仅生成了 SVG。')
    console.log('生成 PNG 方式一：npm install sharp --save-dev 后重新运行本脚本。')
    console.log('生成 PNG 方式二：将 static/tabbar/*.svg 用 https://cloudconvert.com/svg-to-png 等转为 81×81 PNG，按 README 重命名。')
  }
  console.log('\nDone. Icons from Element Plus (Iconify ep set).')
})()
