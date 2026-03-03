<script>
/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.dingyangmall.com
 */
import api from './utils/api'
import __config from './config/env'

export default {
  onLaunch() {
    console.log('Hello World')
    console.log('[如囍优选] App.onLaunch 已执行')
    // 注意：onLaunch 时 getApp() 可能尚未可用，必须用 this 指向当前 App 实例
    this.api = api
    this.globalData = {
      __api: api,
      wxToken: null,
      thirdSession: null,
      wxUser: null,
      profileSkipped: false,
      config: __config,
      StatusBar: 0,
      Custom: null,
      CustomBar: 0,
      shoppingCartCount: '0'
    }
    // 冷启动时从 storage 恢复令牌，后续请求带 X-Wx-Token
    try {
      const u = typeof uni !== 'undefined' ? uni : wx
      if (u.getStorageSync) {
        const saved = u.getStorageSync('wx_token')
        if (saved && typeof saved === 'string' && saved.length > 0) {
          this.globalData.wxToken = saved
          this.globalData.thirdSession = saved
          console.log('[App] 已从 storage 恢复 token')
        }
      }
    } catch (e) {
      console.warn('[App] 恢复 token 失败', e && e.message)
    }
    this.initPage = this.initPage.bind(this)
    this.doLogin = this.doLogin.bind(this)
    this.shoppingCartCount = this.shoppingCartCount.bind(this)
    this.getCurrentPageUrlWithArgs = this.getCurrentPageUrlWithArgs.bind(this)

    // 诊断：仅在有 token 时发起测试请求，不在启动时自动静默登录（避免未点「微信一键登录」就调登录接口、启动页不跳转）
    const that = this
    setTimeout(() => {
      if ((that.globalData.wxToken || that.globalData.thirdSession) && that.api && typeof that.api.goodsCategoryGet === 'function') {
        console.log('[App] 诊断：发起测试请求', __config.basePath)
        that.api.goodsCategoryGet()
          .then(() => console.log('[App] 诊断请求成功'))
          .catch(e => console.warn('[App] 诊断请求失败', e))
      }
    }, 300)

    this.updateManager()
    const u = typeof uni !== 'undefined' ? uni : wx
    u.getSystemInfo({
      success: e => {
        that.globalData.StatusBar = e.statusBarHeight
        const custom = u.getMenuButtonBoundingClientRect && u.getMenuButtonBoundingClientRect()
        if (custom) {
          that.globalData.Custom = custom
          that.globalData.CustomBar = custom.bottom + custom.top - e.statusBarHeight
        }
      }
    })
  },
  onShow() {},
  onHide() {},
  methods: {
    updateManager() {
      const u = typeof uni !== 'undefined' ? uni : wx
      if (!u.getUpdateManager) return
      const updateManager = u.getUpdateManager()
      updateManager.onUpdateReady(() => {
        u.showModal({
          title: '更新提示',
          content: '新版本已经准备好，是否重启应用？',
          success(res) {
            if (res.confirm) updateManager.applyUpdate()
          }
        })
      })
    },
    initPage() {
      const that = getApp()
      const u = typeof uni !== 'undefined' ? uni : wx
      return new Promise((resolve) => {
        if (that.globalData.wxToken || that.globalData.thirdSession) {
          u.checkSession({
            success: () => resolve('success'),
            fail: () => {
              that.globalData.wxToken = null
              that.globalData.thirdSession = null
              that.globalData.wxUser = null
              try {
                if (u.removeStorageSync) u.removeStorageSync('wx_token')
                if (u.removeStorageSync) u.removeStorageSync('wx_third_session')
              } catch (e) {}
              resolve('success')
            }
          })
          return
        }
        // 无 token 时不再自动静默登录，避免未点击「微信一键登录」就调登录接口；需用户主动点击登录
        resolve('success')
      })
    },
    _doSilentLogin() {
      // 单例：多接口同时 60002 时共享同一登录请求，避免疯狂重试
      const that = getApp()
      if (that._silentLoginPromise) return that._silentLoginPromise
      const u = typeof uni !== 'undefined' ? uni : wx
      // 微信环境下必须用官方 wx.login 取 code，后端据此调 code2Session
      const loginApi = (typeof wx !== 'undefined' && wx.login) ? wx : u
      that._silentLoginPromise = new Promise((resolve, reject) => {
        loginApi.login({
          success(res) {
            if (!res.code) {
              reject(new Error('no code'))
              return
            }
            api.login({ code: res.code, jsCode: res.code })
              .then(resp => {
                const raw = resp && (typeof resp === 'object') ? resp : {}
                let data = raw.data && typeof raw.data === 'object' ? raw.data : raw
                if (data && data.data && typeof data.data === 'object' && (data.data.token != null || data.data.openid != null)) data = data.data
                const token = (data && (data.token != null ? data.token : (data.thirdSession || data.sessionKey))) || (raw.token != null ? raw.token : null)
                const openid = data && (data.openid != null ? data.openid : (data.userId != null ? data.userId : ''))
                const userId = data && (data.userId != null ? data.userId : openid)
                if (!token) {
                  reject(new Error('no token'))
                  return
                }
                that.globalData.wxToken = token
                that.globalData.thirdSession = token
                try {
                  const u = typeof uni !== 'undefined' ? uni : wx
                  if (u.setStorageSync) {
                    u.setStorageSync('wx_token', token)
                    u.setStorageSync('wx_third_session', token)
                  }
                } catch (e) {}
                that.globalData.wxUser = {
                  sessionKey: token,
                  userId: userId || openid,
                  openid: openid || userId,
                  nickName: (data && (data.nickname != null ? data.nickname : data.nickName)) || '',
                  headimgUrl: (data && (data.avatarUrl != null ? data.avatarUrl : (data.headimgUrl || data.avatar))) || '',
                  ...(data || {})
                }
                that._silentLoginPromise = null
                that._loginFailedAt = 0
                resolve()
              })
              .catch(err => {
                console.warn('[静默登录] 失败', err)
                that._silentLoginPromise = null
                that._loginFailedAt = Date.now()
                reject(err)
              })
          },
          fail() {
            that._silentLoginPromise = null
            that._loginFailedAt = Date.now()
            reject(new Error('wx.login fail'))
          }
        })
      })
      return that._silentLoginPromise
    },
    doLogin() {
      const that = getApp()
      // 单例：防止用户快速多次点击或多处同时调用导致重复请求
      if (that._doLoginPromise) return that._doLoginPromise
      const u = typeof uni !== 'undefined' ? uni : wx
      u.showLoading({ title: '登录中' })
      // 微信小程序必须走官方 wx.login 获取 code，后端据此调微信 code2Session
      const loginApi = (typeof wx !== 'undefined' && wx.login) ? wx : u
      that._doLoginPromise = new Promise((resolve) => {
        loginApi.login({
          success(res) {
            if (res.code) {
              api.login({ code: res.code, jsCode: res.code }).then(resp => {
                u.hideLoading()
                const raw = resp && (typeof resp === 'object') ? resp : {}
                let data = raw.data && typeof raw.data === 'object' ? raw.data : raw
                if (data && data.data && typeof data.data === 'object' && (data.data.token != null || data.data.openid != null)) data = data.data
                if (!(data && (data.token != null || data.userId != null || data.openid != null))) data = raw
                const openid = data && (data.openid != null ? data.openid : (data.userId != null ? data.userId : ''))
                const userId = data && (data.userId != null ? data.userId : openid)
                const token = (data && (data.token != null ? data.token : (data.thirdSession || data.sessionKey))) || (raw.token != null ? raw.token : null) || (openid || userId)
                if (!token) {
                  console.warn('[Login] 响应中无 token', raw)
                  that._doLoginPromise = null
                  resolve('fail')
                  return
                }
                console.log('[Login] 成功，已保存 token', { openid: openid ? openid.slice(0, 12) + '...' : '(空)', tokenPrefix: token ? token.slice(0, 12) + '...' : '(空)' })
                if (__config.apiDebug) console.log('[Login] 解析结果', { token: token.slice(0, 12) + '...', userId, openid, hasNickname: !!(data.nickname || data.nickName), hasAvatar: !!(data.avatarUrl || data.avatar) })
                that._loginFailedAt = 0
                that.globalData.wxToken = token
                that.globalData.thirdSession = token
                try {
                  if (u.setStorageSync) {
                    u.setStorageSync('wx_token', token)
                    u.setStorageSync('wx_third_session', token)
                  }
                } catch (e) {}
                that.globalData.wxUser = {
                  sessionKey: token,
                  userId,
                  openid: openid || userId,
                  nickName: data.nickname != null ? data.nickname : (data.nickName || ''),
                  headimgUrl: data.avatarUrl != null ? data.avatarUrl : (data.headimgUrl || data.avatar || ''),
                  avatarUrl: data.avatarUrl,
                  nickname: data.nickname,
                  unionid: data.unionid,
                  ...data
                }
                that._doLoginPromise = null
                resolve('success')
              }).catch((err) => {
                console.warn('[Login] 请求失败', err)
                u.hideLoading()
                that._doLoginPromise = null
                that._loginFailedAt = Date.now()
                resolve('fail')
              })
            } else {
              u.hideLoading()
              that._doLoginPromise = null
              that._loginFailedAt = Date.now()
              resolve('fail')
            }
          },
          fail() {
            u.hideLoading()
            that._doLoginPromise = null
            that._loginFailedAt = Date.now()
            resolve('fail')
          }
        })
      })
      return that._doLoginPromise
    },
    shoppingCartCount() {
      const that = getApp() || this
      if (!that || !that.globalData) return
      const hasToken = !!(that.globalData.wxToken || that.globalData.thirdSession)
      if (!hasToken) {
        that.globalData.shoppingCartCount = '0'
        const u = typeof uni !== 'undefined' ? uni : wx
        if (u.setTabBarBadge) u.setTabBarBadge({ index: 2, text: '0' }).catch(() => {})
        return
      }
      const apiRef = (that && that.api) || (that.globalData && that.globalData.__api) || (typeof api !== 'undefined' ? api : null)
      if (!apiRef || typeof apiRef.shoppingCartCount !== 'function') return
      apiRef.shoppingCartCount().then(res => {
        if (that && that.globalData) that.globalData.shoppingCartCount = (res.data != null ? res.data : res) + ''
        const u = typeof uni !== 'undefined' ? uni : wx
        if (that && that.globalData && u.setTabBarBadge) u.setTabBarBadge({ index: 2, text: that.globalData.shoppingCartCount + '' })
      }).catch(() => {
        if (that && that.globalData) that.globalData.shoppingCartCount = '0'
      })
    },
    getCurrentPageUrlWithArgs() {
      const pages = getCurrentPages()
      const currentPage = pages[pages.length - 1]
      const route = currentPage.route || (currentPage.$page && currentPage.$page.fullPath && currentPage.$page.fullPath.replace(/^\//, '').replace(/\?.*/, ''))
      const options = currentPage.options || (currentPage.$page && currentPage.$page.options) || {}
      let urlWithArgs = `/${route || ''}?`
      for (const key in options) urlWithArgs += `${key}=${options[key]}&`
      return urlWithArgs.substring(0, urlWithArgs.length - 1)
    }
  }
}
</script>

<style>
@import "./app.wxss";
</style>
