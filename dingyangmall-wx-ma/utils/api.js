/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.dingyangmall.com
 * 注意：
 * 本软件为www.dingyangmall.com开发研制，项目使用请保留此说明
 *
 * 所有请求的根地址均来自 config/env.js 的 basePath（端口 7500），勿在别处写死端口或域名。
 */
import __configIn from '../config/env'
const __config = (__configIn && typeof __configIn === 'object') ? __configIn : { basePath: 'http://localhost:7500', apiDebug: false }

const isBizSuccess = (data) => {
  const code = data && data.code
  return code === 200 || code === '200' || code === 0 || code === '0' || code === undefined
}

const debugLog = (stage, method, url, payload, extra) => {
  if (!__config.apiDebug) return
  try {
    console.log('[API][' + stage + ']', method, url, payload || '', extra || '')
  } catch (e) {}
}

const recordMissingApi = (method, url, reason) => {
  try {
    const key = 'missingApiList'
    const list = wx.getStorageSync(key) || []
    const hit = {
      method: (method || '').toUpperCase(),
      url,
      reason: reason || 'unknown',
      time: new Date().toISOString()
    }
    const exists = list.some(i => i.method === hit.method && i.url === hit.url)
    if (!exists) {
      list.push(hit)
      wx.setStorageSync(key, list)
    }
    debugLog('MISS', method, url, null, reason)
  } catch (e) {}
}

/** 把后端返回的 data 转成完整报错文案（code、msg 及所有其它字段），便于排查，不再只显示 60001 等 */
const formatFullError = (data, method, url) => {
  if (!data || typeof data !== 'object') {
    return '响应非对象: ' + String(data)
  }
  const lines = ['接口: ' + (method || '') + ' ' + (url || '')]
  try {
    const keys = Object.keys(data)
    for (let i = 0; i < keys.length; i++) {
      const k = keys[i]
      const v = data[k]
      if (v === null || v === undefined) {
        lines.push(k + ': ' + String(v))
      } else if (typeof v === 'object') {
        lines.push(k + ': ' + JSON.stringify(v))
      } else {
        lines.push(k + ': ' + String(v))
      }
    }
  } catch (e) {
    lines.push('序列化失败: ' + (e && e.message))
  }
  return lines.join('\n')
}

const getRequestHeader = () => {
  let appId = ''
  let token = ''
  try {
    if (typeof wx !== 'undefined' && wx.getAccountInfoSync) {
      appId = (wx.getAccountInfoSync().miniProgram || {}).appId || ''
    }
  } catch (e) {
    console.warn('[API] getAccountInfoSync 失败', e && e.message)
  }
  try {
    const app = typeof getApp === 'function' ? getApp() : null
    token = (app && app.globalData && app.globalData.wxToken != null) ? String(app.globalData.wxToken) : ''
    if (!token && app && app.globalData && app.globalData.thirdSession != null) {
      token = String(app.globalData.thirdSession)
    }
    if (!token && typeof wx !== 'undefined' && wx.getStorageSync) {
      try {
        const saved = wx.getStorageSync('wx_token')
        if (saved && typeof saved === 'string' && saved.length > 0) {
          token = saved
          if (app && app.globalData) app.globalData.wxToken = saved
        }
      } catch (e) {}
    }
  } catch (e) {
    console.warn('[API] getApp/wxToken 失败', e && e.message)
  }
  const HEADER_TOKEN = 'X-Wx-Token'
  const header = { 'app-id': appId || '', [HEADER_TOKEN]: token || '' }
  if (__config.apiDebug && token) {
    try { console.log('[API] 请求头', HEADER_TOKEN, '已带', (token || '').slice(0, 12) + '...') } catch (e) {}
  }
  return header
}

const request = (url, method, data, showLoading, retryTimes = 0) => {
  const basePath = (__config && __config.basePath) ? __config.basePath : 'http://localhost:7500'
  let _url = basePath + url
  try {
    console.log('[API] 发起请求', method, _url)
  } catch (e) {}
  const req = typeof wx !== 'undefined' && wx.request ? wx : (typeof uni !== 'undefined' ? uni : null)
  if (!req || !req.request) {
    console.error('[API] wx.request / uni.request 不可用')
    return Promise.reject(new Error('request 不可用'))
  }
  return new Promise((resolve, reject) => {
    debugLog('REQ', method, _url, data)
    if (showLoading && req.showLoading) {
      req.showLoading({ title: '加载中' })
    }
    const header = getRequestHeader()
    try {
      req.request({
      url: _url,
      method: method.toUpperCase ? method.toUpperCase() : method,
      data: data,
      header: header,
      success(res) {
        debugLog('RESP', method, _url, data, { statusCode: res.statusCode, code: res.data && res.data.code, msg: res.data && res.data.msg })
        console.log('[API][响应]', method, _url, '→', res.data)
        if (res.statusCode == 200) {
          if (!isBizSuccess(res.data)) {
            const msg = (res.data && (res.data.msg || res.data.message)) || ''
            if (msg.indexOf('No static resource') >= 0 || msg.indexOf('not found') >= 0 || msg.indexOf('Not Found') >= 0) {
              recordMissingApi(method, _url, msg)
            }
            console.log(res.data)
            const code = res.data && res.data.code
            const fullErrorText = formatFullError(res.data, method, url)
            // 60001/60002 对用户统一展示 token 文案，不展示含 session 的下游原文
            const displayMsg = (code === 60001 || code === 60002) && msg && (msg.indexOf('session') >= 0 || msg.indexOf('会话') >= 0)
              ? (code === 60002 ? '请携带登录令牌（请重新登录后重试）' : '登录已过期，请重新登录')
              : fullErrorText
            console.error('[API] 接口报错', method, _url, '完整响应=', res.data)
            // 令牌缺失/需登录时先静默登录并自动重试一次
            if (code === 60002 && retryTimes < 1 && (url.indexOf('/api/ma/') === 0 || url.indexOf('/weixin/api/ma/') === 0) && url.indexOf('/wxuser/login') < 0) {
              try {
                const app = typeof getApp === 'function' ? getApp() : null
                // 登录失败后 5 秒内不再触发静默登录，避免疯狂重试
                if (app && app._loginFailedAt && (Date.now() - app._loginFailedAt) < 5000) {
                  console.warn('[API] 登录刚失败，跳过静默登录重试')
                  reject(res.data.msg)
                  return
                }
                const doSilent = app && typeof app._doSilentLogin === 'function' ? app._doSilentLogin.bind(app) : null
                const doLogin = app && typeof app.doLogin === 'function' ? app.doLogin.bind(app) : null
                const loginPromise = doSilent ? doSilent() : (doLogin ? doLogin() : null)
                if (loginPromise && typeof loginPromise.then === 'function') {
                  loginPromise.then(() => {
                    request(url, method, data, false, retryTimes + 1).then(resolve).catch(reject)
                  }).catch(() => {
                    if (wx.showModal) wx.showModal({ title: '接口报错', content: displayMsg, showCancel: false })
                    else if (wx.showToast) wx.showToast({ title: (code === 60002 ? '请重新登录' : '登录已过期').slice(0, 20), icon: 'none', duration: 3500 })
                    reject(res.data.msg)
                  })
                  return
                }
              } catch (e) {}
            }
            if (code === 60001) {
              try {
                const app = typeof getApp === 'function' ? getApp() : null
                if (app && app.globalData) {
                  app.globalData.wxToken = null
                  app.globalData.thirdSession = null
                  app.globalData.profileSkipped = false
                }
                if (typeof wx !== 'undefined' && wx.removeStorageSync) {
                  wx.removeStorageSync('wx_token')
                  wx.removeStorageSync('wx_third_session')
                }
              } catch (e) {}
            }
            // 所有业务错误都展示完整报错信息；60001 时 3 秒内只弹一次，避免多请求并发失败时弹窗刷屏
            const is60001 = code === 60001
            const shouldShowModal = !is60001 || !(typeof getApp === 'function' && getApp()._last60001ModalAt && (Date.now() - getApp()._last60001ModalAt) < 3000)
            if (shouldShowModal) {
              if (is60001 && typeof getApp === 'function') {
                try { getApp()._last60001ModalAt = Date.now() } catch (e) {}
              }
              if (wx.showModal) {
                wx.showModal({ title: '接口报错', content: displayMsg, showCancel: false })
              } else if (wx.showToast) {
                const shortMsg = (code === 60001 || code === 60002) && (msg.indexOf('session') >= 0 || msg.indexOf('会话') >= 0)
                  ? (code === 60002 ? '请重新登录' : '登录已过期') : (msg || String(code)).slice(0, 20)
                wx.showToast({ title: shortMsg, icon: 'none', duration: 3500 })
              }
            }
            reject(res.data.msg)
            return
          }
          resolve(res.data)
        } else if (res.statusCode == 404) {
          recordMissingApi(method, _url, 'http 404')
          wx.showModal({
            title: '提示',
            content: '接口不存在：' + _url,
            success(res) {

            }
          })
          reject()
        } else {
          console.log(res)
          wx.showModal({
            title: '提示',
            content: res.errMsg + ':' + res.data.message + ':' + res.data.msg,
            success(res) {

            }
          })
          reject()
        }
      },
      fail(error) {
        console.log('[API][请求失败]', method, _url, '→', error)
        wx.showModal({
          title: '提示',
          content: '接口请求出错：' + error.errMsg + '\n' + _url,
          success(res) {

          }
        })
        reject(error)
      },
      complete(res) {
        if (req.hideLoading) req.hideLoading()
      }
    })
    } catch (e) {
      console.error('[API] request 调用异常', e)
      if (req.hideLoading) req.hideLoading()
      reject(e)
    }
  })
}

const merchantRequest = (url, method, data, showLoading) => {
  let _url = __config.basePath + url
  return new Promise((resolve, reject) => {
    debugLog('REQ_M', method, _url, data)
    if (showLoading){
      wx.showLoading({
        title: '加载中',
      })
    }
    let token = wx.getStorageSync('merchantToken')
    wx.request({
      url: _url,
      method: method,
      data: data,
      header: {
        'Authorization': token ? 'Bearer ' + token : ''
      },
      success(res) {
        debugLog('RESP_M', method, _url, data, { statusCode: res.statusCode, code: res.data && res.data.code, msg: res.data && res.data.msg })
        console.log('[API][商户响应]', method, _url, '→', res.data)
        if (res.statusCode == 200) {
          if (!isBizSuccess(res.data)) {
            const msg = (res.data && (res.data.msg || res.data.message)) || ''
            if (msg.indexOf('No static resource') >= 0 || msg.indexOf('not found') >= 0 || msg.indexOf('Not Found') >= 0) {
              recordMissingApi(method, _url, msg)
            }
            wx.showModal({
              title: '提示',
              content: res.data.msg ? res.data.msg : '操作失败',
              showCancel: false
            })
            reject(res.data.msg)
          } else {
            resolve(res.data)
          }
        } else if (res.statusCode == 401) {
          wx.showToast({ title: '登录已过期', icon: 'none' })
          wx.removeStorageSync('merchantToken')
          wx.redirectTo({ url: '/pages/merchant/login/index' })
          reject('Unauthorized')
        } else {
          if (res.statusCode == 404) recordMissingApi(method, _url, 'http 404')
          wx.showModal({
            title: '提示',
            content: '系统错误',
            showCancel: false
          })
          reject()
        }
      },
      fail(error) {
        console.log('[API][商户请求失败]', method, _url, '→', error)
        wx.showModal({
          title: '提示',
          content: '网络请求失败',
          showCancel: false
        })
        reject(error)
      },
      complete(res) {
        wx.hideLoading()
      }
    })
  })
}

module.exports = {
  request,
  merchantRequest,
  // 小程序登录：POST /weixin/api/ma/wxuser/login，返回 token；后续请求带 Header X-Wx-Token
  login: (data) => {
    return request('/weixin/api/ma/wxuser/login', 'post', data, false)
  },
  wxUserGet: () => {
    return request('/api/ma/wxuser/info', 'get', null, false)
  },
  memberInfo: () => {
    return request('/api/ma/wxuser/info', 'get', null, false)
  },
  couponMy: (status) => {//我的优惠券
    return request('/app/coupon/my', 'get', {status: status}, false)
  },
  lotteryConfig: () => {//抽奖配置
    return request('/app/lottery/config', 'get', null, false)
  },
  lotteryDraw: () => {//抽奖
    return request('/app/lottery/draw', 'post', null, true)
  },
  lotteryRecord: (data) => {//抽奖记录
    return request('/app/lottery/record', 'get', data, false)
  },
  memberSignIn: () => {//每日签到
    return request('/app/member/sign-in', 'post', null, true)
  },
  wxUserSave: (data) => {
    return request('/api/ma/wxuser', 'post', data, true)
  },
  // 手机号授权：走 /api/ma 链避免 60002，getPhoneNumber 回调的 code 传此接口
  bindPhoneNumber: (data) => {
    return request('/api/ma/wxuser/phone', 'post', data, true)
  },
  /** 上传头像等图片：传入 chooseAvatar 返回的临时路径，返回 Promise<url>；若无上传接口则走 wx.uploadFile 到 /weixin/api/ma/upload */
  uploadFile: (filePath) => {
    const basePath = (__config && __config.basePath) ? __config.basePath : ''
    if (!basePath || !filePath) return Promise.reject(new Error('缺少 basePath 或 filePath'))
    const url = basePath + '/weixin/api/ma/upload'
    const header = getRequestHeader()
    return new Promise((resolve, reject) => {
      wx.uploadFile({
        url,
        filePath,
        name: 'file',
        header,
        success: (res) => {
          if (res.statusCode !== 200) {
            reject(new Error('upload status ' + res.statusCode))
            return
          }
          try {
            const data = JSON.parse(res.data || '{}')
            const out = (data.data && data.data.url) || (data.url) || (data.data && data.data.link) || ''
            resolve(out || filePath)
          } catch (e) {
            resolve(filePath)
          }
        },
        fail: (err) => reject(err || new Error('upload fail'))
      })
    })
  },
  // 首页聚合：轮播、分类宫格、公告、猜你喜欢、推荐块，一次请求（与各大购物平台一致）
  getHomePage: () => {
    return request('/api/ma/home', 'get', null, false)
  },
  // 商品分类树：统一走 /api/ma
  goodsCategoryGet: (data) => {
    return request('/api/ma/goodscategory/tree', 'get', data, false)
  },
  // 公告列表：走公开接口，无需 token
  getNoticeList: () => {
    return request('/api/public/ma/notice/list', 'get', null, false)
  },
  // 商品分页：请求 /api/ma/goodsspu/page，后端查 goods_spu 表
  goodsPage: (data) => {
    const params = data ? { ...data } : {}
    if (params.current != null && params.pageNum == null) params.pageNum = params.current
    if (params.size != null && params.pageSize == null) params.pageSize = params.size
    return request('/api/ma/goodsspu/page', 'get', params, false)
  },
  // 查询所有商品：请求 /api/ma/goodsspu/list，后端直接查 goods_spu 表返回列表
  goodsListAll: (limit) => {
    return request('/api/ma/goodsspu/list', 'get', limit != null ? { limit } : {}, false)
  },
  // 商品详情：请求 /api/ma/goodsspu/{id}，后端查 goods_spu 表
  goodsGet: (id) => {
    return request('/api/ma/goodsspu/' + id, 'get', null, false)
  },
  shoppingCartPage: (data) => {
    return request('/api/ma/shoppingcart/page', 'get', data, false)
  },
  shoppingCartAdd: (data) => {
    return request('/api/ma/shoppingcart', 'post', data, true)
  },
  shoppingCartEdit: (data) => {
    return request('/api/ma/shoppingcart', 'put', data, true)
  },
  shoppingCartDel: (data) => {
    return request('/api/ma/shoppingcart/del', 'post', data, false)
  },
  // 购物车数量：走 /api/ma 链，仅按 token 注入 memberId，避免 weixin 依赖 60002
  shoppingCartCount: (data) => {
    return request('/api/ma/shoppingcart/count', 'get', data, false)
  },
  orderSub: (data) => {
    return request('/api/ma/orderinfo', 'post', data, true)
  },
  // 商家端API
  getCaptcha: () => {
    return request('/captchaImage', 'get', null, false)
  },
  merchantLogin: (data) => {
    return request('/login', 'post', data, true)
  },
  merchantScanUser: (code) => {
    return merchantRequest('/api/mall/merchant/scan/user/' + code, 'get', null, false)
  },
  merchantGivePoints: (data) => {
    return merchantRequest('/api/mall/merchant/scan/points', 'post', data, true)
  },
  merchantVerifyCoupon: (data) => {
    return merchantRequest('/api/mall/merchant/scan/coupon/verify', 'post', data, true)
  },
  sendPacket: (data) => {//发送积分红包
    return request('/app/member/send-packet', 'post', data, true)
  },
  sendSmsCode: (phone) => {//发送短信验证码
    return request('/app/member/send-sms-code', 'get', { phone: phone }, true)
  },
  orderPage: (data) => {
    return request('/api/ma/orderinfo/page', 'get', data, false)
  },
  orderGet: (id) => {
    return request('/api/ma/orderinfo/' + id, 'get', null, false)
  },
  orderCancel: (id) => {
    return request('/api/ma/orderinfo/cancel/' + id, 'put', null, true)
  },
  orderRefunds: (data) => {
    return request('/api/ma/orderinfo/refunds', 'post', data, true)
  },
  orderReceive: (id) => {
    return request('/api/ma/orderinfo/receive/' + id, 'put', null, true)
  },
  orderLogistics: (id) => {
    return request('/api/ma/orderinfo/logistics/' + id, 'get', null, false)
  },
  orderDel: (id) => {
    return request('/api/ma/orderinfo/' + id, 'delete', null, false)
  },
  orderCountAll: (data) => {
    return request('/api/ma/orderinfo/countAll', 'get', data, false)
  },
  unifiedOrder: (data) => {
    return request('/api/ma/orderinfo/unifiedOrder', 'post', data, true)
  },
  userAddressPage: (data) => {
    return request('/api/ma/useraddress/page', 'get', data, false)
  },
  userAddressSave: (data) => {
    return request('/api/ma/useraddress', 'post', data, true)
  },
  userAddressDel: (id) => {
    return request('/api/ma/useraddress/' + id, 'delete', null, false)
  }
}
