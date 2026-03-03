<!--
  启动页 / 微信一键登录：进入小程序后首屏，登录后收集昵称、头像、手机号，再进入首页
-->
<template>
  <view class="page tm-page launch-page">
    <view class="launch-content">
      <view class="logo-area">
        <text class="brand-name tm-brand">如囍优选</text>
        <text class="brand-desc tm-text-secondary">欢迎使用，请先登录</text>
      </view>

      <!-- 未登录：微信一键登录 -->
      <view v-if="!isLoggedIn" class="btn-area">
        <button
          class="cu-btn block round lg tm-primary-btn"
          type="button"
          hover-class="button-hover"
          :loading="loginLoading"
          @tap="onWxLogin"
        >
          微信一键登录
        </button>
      </view>

      <!-- 已登录：完善信息 + 进入首页 -->
      <view v-else class="info-area tm-card tm-section">
        <view class="cu-bar text-black margin-bottom"><text class="text-lg text-bold">完善资料（可选）</text></view>
        <view class="flex align-center margin-bottom-sm" v-if="wxUser && (wxUser.nickName || wxUser.headimgUrl)">
          <image v-if="wxUser.headimgUrl" class="avatar round margin-right" :src="wxUser.headimgUrl" mode="aspectFill" />
          <text class="text-df">{{ wxUser.nickName || '已获取昵称' }}</text>
        </view>
        <view class="btn-row">
          <button
            v-if="!hasProfile"
            class="cu-btn block round tm-outline-btn margin-bottom"
            type="button"
            hover-class="button-hover"
            :loading="profileLoading"
            @tap="onGetUserProfile"
          >
            获取昵称与头像
          </button>
          <button
            class="cu-btn block round tm-outline-btn margin-bottom"
            type="button"
            hover-class="button-hover"
            :loading="phoneLoading"
            open-type="getPhoneNumber"
            @getphonenumber="onGetPhoneNumber"
          >
            授权手机号
          </button>
        </view>
        <button
          class="cu-btn block round lg tm-primary-btn margin-top"
          type="button"
          hover-class="button-hover"
          @tap="goHome"
        >
          进入首页
        </button>
      </view>
    </view>
  </view>
</template>

<script>
import api from '@/utils/api'

export default {
  name: 'LaunchPage',
  data() {
    const app = getApp()
    return {
      loginLoading: false,
      profileLoading: false,
      phoneLoading: false,
      isLoggedIn: !!app.globalData.thirdSession,
      wxUser: app.globalData.wxUser || null
    }
  },
  computed: {
    hasProfile() {
      const u = this.wxUser
      return !!(u && (u.nickName || u.headimgUrl || u.avatarUrl))
    }
  },
  onShow() {
    const app = getApp()
    this.isLoggedIn = !!app.globalData.thirdSession
    this.wxUser = app.globalData.wxUser || null
  },
  methods: {
    onWxLogin() {
      const app = getApp()
      const u = typeof uni !== 'undefined' ? uni : wx
      this.loginLoading = true
      u.login({
        success: (res) => {
          if (!res.code) {
            this.loginLoading = false
            u.showToast({ title: '获取登录码失败', icon: 'none' })
            return
          }
          const apiModule = app.api || app.globalData.__api || api
          apiModule.login({ code: res.code, jsCode: res.code })
            .then((resp) => {
              const raw = resp && typeof resp === 'object' ? resp : {}
              const data = (raw.data && (raw.data.thirdSession != null || raw.data.userId != null)) ? raw.data : raw
              const thirdSession = data.thirdSession || data.sessionKey || data.token
              if (!thirdSession) {
                u.showToast({ title: '登录失败', icon: 'none' })
                this.loginLoading = false
                return
              }
              app.globalData.thirdSession = thirdSession
              app.globalData.wxUser = {
                sessionKey: thirdSession,
                userId: data.userId || data.openid,
                openid: data.userId || data.openid,
                nickName: data.nickname != null ? data.nickname : (data.nickName || ''),
                headimgUrl: data.avatarUrl != null ? data.avatarUrl : (data.headimgUrl || data.avatar || ''),
                ...data
              }
              this.isLoggedIn = true
              this.wxUser = app.globalData.wxUser
              this.loginLoading = false
              try { if (app && typeof app.shoppingCartCount === 'function') app.shoppingCartCount() } catch (e) {}
            })
            .catch((err) => {
              console.warn('[Launch] 登录请求失败', err)
              u.showToast({ title: '登录失败', icon: 'none' })
              this.loginLoading = false
            })
        },
        fail: () => {
          this.loginLoading = false
          u.showToast({ title: '登录失败', icon: 'none' })
        }
      })
    },
    onGetUserProfile() {
      const app = getApp()
      const u = typeof uni !== 'undefined' ? uni : wx
      this.profileLoading = true
      u.getUserProfile({
        desc: '用于完善会员资料',
        success: (res) => {
          const userInfo = res.userInfo || {}
          const nickName = userInfo.nickName || ''
          const avatarUrl = userInfo.avatarUrl || ''
          app.globalData.wxUser = app.globalData.wxUser || {}
          app.globalData.wxUser.nickName = nickName
          app.globalData.wxUser.headimgUrl = avatarUrl
          app.globalData.wxUser.avatarUrl = avatarUrl
          app.globalData.wxUser.nickname = nickName
          this.wxUser = app.globalData.wxUser
          const apiModule = app.api || app.globalData.__api || api
          apiModule.wxUserSave({ nickName, avatarUrl }).then(() => {
            u.showToast({ title: '已保存', icon: 'success' })
          }).catch(() => {})
          this.profileLoading = false
        },
        fail: (err) => {
          this.profileLoading = false
          if (err.errMsg && err.errMsg.indexOf('cancel') !== -1) return
          u.showToast({ title: '获取失败', icon: 'none' })
        }
      })
    },
    onGetPhoneNumber(e) {
      const app = getApp()
      const u = typeof uni !== 'undefined' ? uni : wx
      if (e.detail.errMsg !== 'getPhoneNumber:ok') {
        if (e.detail.errMsg && e.detail.errMsg.indexOf('cancel') !== -1) return
        u.showToast({ title: '需要授权手机号', icon: 'none' })
        return
      }
      const code = e.detail.code
      if (!code) {
        u.showToast({ title: '获取手机号失败', icon: 'none' })
        return
      }
      this.phoneLoading = true
      const apiModule = app.api || app.globalData.__api || api
      apiModule.bindPhoneNumber({ code })
        .then((resp) => {
          const data = (resp && resp.data) || resp
          const phone = data.phoneNumber || data.phone
          if (phone && app.globalData.wxUser) {
            app.globalData.wxUser.phoneNumber = phone
            app.globalData.wxUser.phone = phone
            this.wxUser = app.globalData.wxUser
          }
          u.showToast({ title: '已绑定手机号', icon: 'success' })
          this.phoneLoading = false
        })
        .catch(() => {
          u.showToast({ title: '绑定失败', icon: 'none' })
          this.phoneLoading = false
        })
    },
    goHome() {
      const u = typeof uni !== 'undefined' ? uni : wx
      u.switchTab({ url: '/pages/home/index' })
    }
  }
}
</script>

<style lang="scss" scoped>
.launch-page {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  padding: 40rpx;
}
.launch-content {
  width: 100%;
  max-width: 600rpx;
}
.logo-area {
  text-align: center;
  margin-bottom: 80rpx;
}
.brand-name {
  display: block;
  font-size: 48rpx;
  font-weight: 700;
  margin-bottom: 16rpx;
}
.brand-desc {
  font-size: 28rpx;
}
.btn-area .cu-btn {
  margin-top: 24rpx;
}
.info-area {
  padding: 32rpx;
}
.avatar {
  width: 72rpx;
  height: 72rpx;
}
.btn-row .cu-btn {
  margin-bottom: 24rpx;
}
</style>
