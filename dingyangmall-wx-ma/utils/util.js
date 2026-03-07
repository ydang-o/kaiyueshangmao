/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.dingyangmall.com
 * 注意：
 * 本软件为www.dingyangmall.com开发研制，项目使用请保留此说明
 */
const validate = require('./validate.js')

const formatTime = date => {
  const year = date.getFullYear()
  const month = date.getMonth() + 1
  const day = date.getDate()
  const hour = date.getHours()
  const minute = date.getMinutes()
  const second = date.getSeconds()

  return [year, month, day].map(formatNumber).join('/') + ' ' + [hour, minute, second].map(formatNumber).join(':')
}

const formatNumber = n => {
  n = n.toString()
  return n[1] ? n : '0' + n
}

//空值过滤器
const filterForm = (form) => {
  let obj = {};
  Object.keys(form).forEach(ele => {
    if (!validate.validatenull(form[ele])) {
      obj[ele] = form[ele]
    }
  });
  return obj;
}

/**
 * 更新购物车角标（同步 globalData 并刷新自定义 tabBar 上的数量）
 * @param {number|string} count 购物车件数
 */
const updateCartBadge = (count) => {
  const n = parseInt(count, 10) || 0
  try {
    const app = typeof getApp === 'function' ? getApp() : null
    if (app && app.globalData) app.globalData.shoppingCartCount = String(n)
    const pages = getCurrentPages()
    const page = pages[pages.length - 1]
    if (page && typeof page.getTabBar === 'function') {
      const tabBar = page.getTabBar()
      if (tabBar && tabBar.setData) tabBar.setData({ cartCount: n })
    }
  } catch (e) {}
}

/**
 * 需要登录时提醒并可选跳转「我的」页登录
 * @param {string} [content='此功能需要登录，请先登录'] 提示文案
 * @returns {Promise<boolean>} 已登录 resolve(true)，未登录且用户点了「去登录」会 switchTab 到我的页并 resolve(false)，点取消 resolve(false)
 */
const requireLogin = (content) => {
  const msg = content || '此功能需要登录，请先登录'
  try {
    const app = typeof getApp === 'function' ? getApp() : null
    const token = (app && app.globalData && (app.globalData.thirdSession || app.globalData.wxToken)) || ''
    if (token) return Promise.resolve(true)
  } catch (e) {}
  return new Promise((resolve) => {
    const u = typeof uni !== 'undefined' ? uni : wx
    u.showModal({
      title: '提示',
      content: msg,
      cancelText: '取消',
      confirmText: '去登录',
      success: (res) => {
        if (res.confirm) {
          try {
            u.switchTab({ url: '/pages/user/user-center/index' })
          } catch (e) {}
        }
        resolve(false)
      },
      fail: () => resolve(false)
    })
  })
}

module.exports = {
  formatTime: formatTime,
  formatNumber: formatNumber,
  filterForm: filterForm,
  requireLogin: requireLogin,
  updateCartBadge: updateCartBadge
}

