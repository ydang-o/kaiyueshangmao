<script>
/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.dingyangmall.com
 */
import api from './utils/api'
import __config from './config/env'

export default {
  onLaunch() {
    const app = getApp()
    app.api = api
    app.globalData = {
      thirdSession: null,
      wxUser: null,
      config: __config,
      StatusBar: 0,
      Custom: null,
      CustomBar: 0,
      shoppingCartCount: '0'
    }
    app.initPage = this.initPage
    app.doLogin = this.doLogin
    app.shoppingCartCount = this.shoppingCartCount
    app.getCurrentPageUrlWithArgs = this.getCurrentPageUrlWithArgs

    this.updateManager()
    const u = typeof uni !== 'undefined' ? uni : wx
    u.getSystemInfo({
      success: e => {
        app.globalData.StatusBar = e.statusBarHeight
        const custom = u.getMenuButtonBoundingClientRect && u.getMenuButtonBoundingClientRect()
        if (custom) {
          app.globalData.Custom = custom
          app.globalData.CustomBar = custom.bottom + custom.top - e.statusBarHeight
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
      return new Promise((resolve) => {
        if (!that.globalData.thirdSession) {
          that.doLogin().then(() => resolve('success'))
        } else {
          const u = typeof uni !== 'undefined' ? uni : wx
          u.checkSession({
            success: () => resolve('success'),
            fail: () => that.doLogin().then(() => resolve('success'))
          })
        }
      })
    },
    doLogin() {
      const u = typeof uni !== 'undefined' ? uni : wx
      const that = getApp()
      u.showLoading({ title: '登录中' })
      return new Promise((resolve) => {
        u.login({
          success(res) {
            if (res.code) {
              api.login({ jsCode: res.code }).then(resp => {
                u.hideLoading()
                const wxUser = resp.data
                that.globalData.thirdSession = wxUser.sessionKey
                that.globalData.wxUser = wxUser
                resolve('success')
                that.shoppingCartCount()
              })
            }
          }
        })
      })
    },
    shoppingCartCount() {
      const that = getApp()
      that.api.shoppingCartCount().then(res => {
        that.globalData.shoppingCartCount = res.data + ''
        const u = typeof uni !== 'undefined' ? uni : wx
        u.setTabBarBadge({ index: 2, text: that.globalData.shoppingCartCount + '' })
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
