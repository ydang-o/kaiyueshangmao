<!--
  Copyright (C) 2018-2019 www.dingyangmall.com
-->
<template>
  <view class="page tm-page">
    <!-- 顶部用户信息区：左右分栏，左侧头像、右侧信息 -->
    <view class="user-header bg-white tm-card radius margin">
      <view class="flex justify-end user-header-top">
        <view class="text-xl" @tap="settings"><text class="cuIcon-settingsfill text-black"></text></view>
      </view>
      <view class="flex align-center user-header-body">
        <view class="cu-avatar round xl head flex flex-shrink-0" :style="avatarStyle">{{ avatarText }}</view>
        <view class="flex-sub user-header-info">
          <view class="text-xxl text-bold text-black" v-if="displayName">{{ displayName }}</view>
          <view v-else-if="isLoggedIn" class="text-df text-gray">已登录</view>
          <view v-else class="text-df text-gray">未登录</view>
          <view v-if="!isLoggedIn" class="user-header-actions"><button type="button" class="cu-btn round lg tm-primary-btn" hover-class="button-hover" @tap="wxLogin" :loading="loginLoading">微信一键登录</button></view>
          <view v-else class="user-header-actions flex align-center flex-wrap">
            <view class="cu-tag sm round line-orange margin-right-sm" @tap="getUserProfile">更新昵称</view>
            <view v-if="userInfo && userInfo.points != null" class="cu-tag sm round line-blue">积分 {{ userInfo.points }}</view>
            <view v-if="userInfo && userInfo.memberCode" class="cu-tag sm round line-gray margin-left-sm">会员码 {{ userInfo.memberCode }}</view>
          </view>
          <view class="text-xs text-gray margin-top-xs" v-if="displayUserId">ID: {{ displayUserId }}</view>
        </view>
      </view>
    </view>
    <view class="cu-listradius order-list tm-section tm-card">
      <view class="cu-bar">
        <view class="action"><text class="cuIcon-titles text-black titles-color"></text>我的订单</view>
        <navigator class="action" url="/pages/order/order-list/index" hover-class="none">全部订单<text class="cuIcon-right"></text></navigator>
      </view>
      <view class="cu-list grid col-4 no-border order-list-2">
        <view class="cu-item"><navigator url="/pages/order/order-list/index?status=0" hover-class="none"><view class="order-icon cuIcon-pay text-orange"></view><text class="text-black">待付款</text></navigator></view>
        <view class="cu-item"><navigator url="/pages/order/order-list/index?status=1" hover-class="none"><view class="order-icon cuIcon-deliver text-blue"></view><text class="text-black">待发货</text></navigator></view>
        <view class="cu-item"><navigator url="/pages/order/order-list/index?status=2" hover-class="none"><view class="order-icon cuIcon-goods text-green"></view><text class="text-black">待收货</text></navigator></view>
        <view class="cu-item"><navigator url="/pages/order/order-list/index?status=4" hover-class="none"><view class="order-icon cuIcon-roundcheck text-gray"></view><text class="text-black">已完成</text></navigator></view>
      </view>
    </view>
    <view class="cu-list menu card-menu radius address tm-section tm-card">
      <navigator class="cu-item arrow" url="/pages/coupon/my-coupons/index" hover-class="none">
        <view class="content"><view class="address-icon cuIcon-form text-orange"></view><text class="text-black">我的优惠券</text></view>
      </navigator>
      <view class="cu-item arrow" @tap="signIn">
        <view class="content"><view class="address-icon cuIcon-calendar text-blue"></view><text class="text-black">每日签到</text></view>
      </view>
      <navigator class="cu-item arrow" url="/pages/lottery/index/index" hover-class="none">
        <view class="content"><view class="address-icon cuIcon-medal text-red"></view><text class="text-black">积分抽奖</text></view>
      </navigator>
      <navigator class="cu-item arrow" url="/pages/integral/packet/index" hover-class="none">
        <view class="content"><view class="address-icon cuIcon-redpacket text-red"></view><text class="text-black">积分红包</text></view>
      </navigator>
      <navigator class="cu-item arrow" url="/pages/merchant/login/index" hover-class="none">
        <view class="content"><view class="address-icon cuIcon-shop text-green"></view><text class="text-black">商家入口</text></view>
      </navigator>
      <navigator class="cu-item arrow" url="/pages/user/user-address/list/index" hover-class="none">
        <view class="content"><view class="address-icon cuIcon-location text-blue"></view><text class="text-black">收货地址</text></view>
      </navigator>
      <navigator class="cu-item arrow" url="/pages/debug/api-test/index" hover-class="none">
        <view class="content"><view class="address-icon cuIcon-form text-gray"></view><text class="text-black">接口调试</text><text class="text-gray text-sm margin-left">逐条测接口</text></view>
      </navigator>
    </view>
    <view class="text-gray text-sm text-center page-footer">www.dingyangmall.com 提供技术支持</view>
    <view v-if="!isLoggedIn" class="login-banner-bottom-spacer"></view>
    <login-banner
      :is-logged-in="isLoggedIn"
      :has-phone="hasPhone"
      :profile-skipped="profileSkipped"
      :suppress-profile-modal="true"
      :display-nickname="displayNickname"
      :display-avatar="displayAvatar"
      :display-phone="displayPhone"
      @login-success="onLoginSuccess"
      @skip="onProfileSkipOrConfirm"
      @confirm="onProfileSkipOrConfirm"
    />
  </view>
</template>

<script>
import LoginBanner from '@/components/login-banner/index.vue'
import apiModule from '@/utils/api'
export default {
  name: 'UserCenterPage',
  components: { LoginBanner },
  data() {
    const app = getApp()
    const wxUser = app.globalData.wxUser || {}
    return {
      config: app.globalData.config || {},
      wxUser: app.globalData.wxUser || null,
      userInfo: null,
      isLoggedIn: !!app.globalData.thirdSession,
      hasPhone: !!(wxUser.phoneNumber || wxUser.phone),
      profileSkipped: !!app.globalData.profileSkipped,
      displayNickname: (wxUser.nickName || wxUser.nickname || '').trim(),
      displayAvatar: (wxUser.headimgUrl || wxUser.avatarUrl || wxUser.avatar || '').trim(),
      displayPhone: (wxUser.phoneNumber || wxUser.phone || '').trim(),
      loginLoading: false
    }
  },
  computed: {
    displayName() {
      const u = this.wxUser || this.userInfo
      return (u && (u.nickName || u.nickname)) || ''
    },
    displayUserId() {
      const u = this.wxUser || this.userInfo
      return (u && (u.userId || u.openid || u.id)) || ''
    },
    avatarStyle() {
      const u = this.wxUser || this.userInfo
      const url = u && (u.headimgUrl || u.avatar || u.avatarUrl)
      return url ? 'background-image:url(' + url + ')' : ''
    },
    avatarText() {
      return this.avatarStyle ? '' : '我'
    }
  },
  onShow() {
    const app = getApp()
    const pages = getCurrentPages()
    const page = pages[pages.length - 1]
    if (page && typeof page.getTabBar === 'function') {
      const tabBar = page.getTabBar()
      if (tabBar && tabBar.setData) tabBar.setData({ selected: 2 })
    }
    this.isLoggedIn = !!app.globalData.thirdSession
    this.wxUser = app.globalData.wxUser
    this.profileSkipped = !!app.globalData.profileSkipped
    const wxUser = app.globalData.wxUser || {}
    this.hasPhone = !!(wxUser.phoneNumber || wxUser.phone)
    this.displayNickname = (wxUser.nickName || wxUser.nickname || '').trim()
    this.displayAvatar = (wxUser.headimgUrl || wxUser.avatarUrl || wxUser.avatar || '').trim()
    this.displayPhone = (wxUser.phoneNumber || wxUser.phone || '').trim()
    uni.setTabBarBadge({ index: 2, text: (app.globalData.shoppingCartCount || '') + '' })
    this.fetchUserDataOnceReady()
    if (this.config.adEnable && uni.createInterstitialAd) {
      try {
        const ad = uni.createInterstitialAd({ adUnitId: this.config.adInsertScreenID })
        ad.show().catch(() => {})
      } catch (e) {}
    }
  },
  methods: {
    onProfileSkipOrConfirm() {
      const app = getApp()
      if (app && app.globalData) app.globalData.profileSkipped = true
      this.profileSkipped = true
    },
    onLoginSuccess() {
      const app = getApp()
      this.isLoggedIn = !!app.globalData.thirdSession
      this.wxUser = app.globalData.wxUser
      const wxUser = app.globalData.wxUser || {}
      this.hasPhone = !!(wxUser.phoneNumber || wxUser.phone)
      this.displayNickname = (wxUser.nickName || wxUser.nickname || '').trim()
      this.displayAvatar = (wxUser.headimgUrl || wxUser.avatarUrl || wxUser.avatar || '').trim()
      this.displayPhone = (wxUser.phoneNumber || wxUser.phone || '').trim()
      if (typeof this.wxUserGet === 'function') this.wxUserGet()
      if (typeof this.orderCountAll === 'function') this.orderCountAll()
    },
    /** 从接口返回中解析出展示用用户对象（兼容 data 包裹或平铺） */
    normalizeUserFromApi(res) {
      const data = (res && (res.data || res)) || {}
      return {
        nickName: data.nickName || data.nickname || '',
        headimgUrl: data.headimgUrl || data.avatar || data.avatarUrl || '',
        userId: data.userId || data.id || data.openid || '',
        openid: data.openid || data.userId || '',
        ...data
      }
    },
    /** 将用户数据同步到页面与 globalData */
    syncUserToState(user) {
      if (!user || (!user.userId && !user.openid)) return
      this.wxUser = this.wxUser ? Object.assign({}, this.wxUser, user) : { ...user }
      const app = getApp()
      if (app && app.globalData) {
        app.globalData.wxUser = Object.assign(app.globalData.wxUser || {}, this.wxUser)
      }
    },
    fetchUserDataOnceReady() {
      const app = typeof getApp === 'function' ? getApp() : null
      const api = (app && app.api) || (app && app.globalData && app.globalData.__api) || apiModule
      if (!api || typeof api.wxUserGet !== 'function') return
      if (this.isLoggedIn) {
        this.wxUserGet()
        this.orderCountAll()
      }
    },
    settings() { uni.openSetting() },
    wxLogin() {
      uni.showModal({
        title: '微信授权登录',
        content: '为了更好的服务体验，将使用你的微信账号登录如囍优选。是否授权？',
        cancelText: '取消',
        confirmText: '授权登录',
        confirmColor: '#ff0036',
        success: (res) => {
          if (!res.confirm) return
          this.loginLoading = true
          getApp().doLogin().then((result) => {
            this.loginLoading = false
            if (result === 'fail') {
              uni.showToast({ title: '登录失败，请重试', icon: 'none' })
              return
            }
            this.isLoggedIn = !!getApp().globalData.thirdSession
            this.wxUser = getApp().globalData.wxUser
            this.wxUserGet()
            this.orderCountAll()
            uni.showToast({ title: '登录成功', icon: 'success' })
            // 登录后请求会带 X-Wx-Token；用户可点击「更新昵称」再授权头像
          })
        }
      })
    },
    fetchUserProfileAuth() {
      if (typeof uni.getUserProfile !== 'function') return
      uni.getUserProfile({
        desc: '用于完善会员资料、展示昵称与头像',
        success: (detail) => {
          const that = this
          const app = typeof getApp === 'function' ? getApp() : null
          const api = (app && app.api) || (app && app.globalData && app.globalData.__api) || apiModule
          const thirdSession = app && app.globalData && app.globalData.thirdSession
          const payload = { ...detail, _thirdSession: thirdSession || '' }
          setTimeout(() => {
            if (!api || typeof api.wxUserSave !== 'function') return
            api.wxUserSave(payload).then(res => {
              that.syncUserToState(that.normalizeUserFromApi(res))
              that.wxUserGet()
            }).catch(() => {})
          }, 0)
        },
        fail: () => {}
      })
    },
    getUserProfile() {
      uni.getUserProfile({
        desc: '用于完善会员资料',
        success: (detail) => {
          const that = this
          const app = typeof getApp === 'function' ? getApp() : null
          const api = (app && app.api) || (app && app.globalData && app.globalData.__api) || apiModule
          const thirdSession = app && app.globalData && app.globalData.thirdSession
          const payload = { ...detail, _thirdSession: thirdSession || '' }
          setTimeout(() => {
            if (!api || typeof api.wxUserSave !== 'function') return
            api.wxUserSave(payload).then(res => {
              that.syncUserToState(that.normalizeUserFromApi(res))
              that.wxUserGet()
            }).catch(() => {
              uni.showToast({ title: '更新失败，请重试', icon: 'none' })
            })
          }, 0)
        },
        fail: () => {}
      })
    },
    wxUserGet() {
      const app = typeof getApp === 'function' ? getApp() : null
      const api = (app && app.api) || (app && app.globalData && app.globalData.__api) || apiModule
      if (!api || typeof api.wxUserGet !== 'function') return
      api.wxUserGet().then(res => {
        const data = (res && (res.data || res)) || {}
        this.userInfo = data
        if (data && (data.userId != null || data.openid != null || data.nickname != null || data.nickName != null || data.avatarUrl != null || data.avatar != null || data.headimgUrl != null)) {
          this.syncUserToState(this.normalizeUserFromApi(res))
        }
      }).catch(err => console.error('[我的] wxUserGet 失败', err))
    },
    orderCountAll() {
      const app = typeof getApp === 'function' ? getApp() : null
      const api = (app && app.api) || (app && app.globalData && app.globalData.__api) || apiModule
      if (!api || typeof api.orderCountAll !== 'function') return
      api.orderCountAll().then(() => {}).catch(() => {})
    },
    signIn() {
      const app = typeof getApp === 'function' ? getApp() : null
      const api = (app && app.api) || (app && app.globalData && app.globalData.__api) || apiModule
      if (!api || typeof api.memberSignIn !== 'function') return
      api.memberSignIn().then(res => {
        uni.showToast({ title: (res && res.msg) || '签到成功', icon: 'success' })
        this.wxUserGet()
      }).catch(() => {
        uni.showToast({ title: '签到失败或今日已签到', icon: 'none' })
      })
    }
  }
}
</script>

<style scoped>
.page.tm-page { padding-bottom: 24rpx; }
.user-header { box-shadow: 0 2rpx 12rpx rgba(0,0,0,0.06); padding: 20rpx 24rpx; margin: 20rpx 24rpx 0; }
.user-header-top { margin-bottom: 12rpx; }
.user-header-body { padding: 8rpx 0; }
.user-header-info { margin-left: 20rpx; padding: 4rpx 0; }
.user-header-info .user-header-actions { margin-top: 12rpx; }
.user-header-info .user-header-actions .cu-tag { margin-right: 12rpx; margin-bottom: 4rpx; }
.user-header-info .text-xs { margin-top: 8rpx; }
.user-header .head { width: 128rpx; height: 128rpx; background-color: #e5e7eb; background-size: cover; }
.order-icon, .address-icon { font-size: 44rpx; width: 80rpx; height: 80rpx; display: flex; align-items: center; justify-content: center; border-radius: 16rpx; flex-shrink: 0; }
.address-icon { margin-right: 20rpx; }
.order-list { margin-top: 12rpx; padding: 8rpx 0 12rpx; }
.order-list .cu-bar { padding: 12rpx 24rpx; min-height: auto; }
.address { margin-top: 12rpx; }
.address .cu-item .content { display: flex; flex-direction: row; align-items: center; }
.address .cu-item { padding: 20rpx 24rpx; }
.page-footer { margin-top: 32rpx; padding: 0 24rpx; }
.login-banner-bottom-spacer { height: 140rpx; }
</style>
