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
        <view class="cu-avatar round xl head flex flex-shrink-0 avatar-tappable" :style="avatarStyle" @tap="openProfileModal">{{ avatarText }}</view>
        <view class="flex-sub user-header-info">
          <view class="text-xxl text-bold text-black" v-if="displayName">{{ displayName }}</view>
          <view v-else-if="isLoggedIn" class="text-df text-gray">已登录</view>
          <view v-else class="text-df text-gray">未登录</view>
          <view v-if="!isLoggedIn" class="user-header-actions"><button type="button" class="cu-btn round lg tm-primary-btn" hover-class="button-hover" @tap="wxLogin" :loading="loginLoading">微信一键登录</button></view>
          <view v-else class="user-header-actions flex align-center flex-wrap">
            <view class="cu-tag sm round line-orange margin-right-sm" @tap="getUserProfile">更新昵称</view>
          </view>
          <!-- 积分展示：登录后显眼展示 -->
          <view v-if="isLoggedIn" class="user-points-row">
            <view class="user-points-value" @tap="goIntegral">
              <text class="points-num">{{ displayPoints }}</text>
              <text class="points-label">积分</text>
            </view>
          </view>
          <view class="text-xs text-gray margin-top-xs" v-if="displayUserId">ID: {{ displayUserId }}</view>
        </view>
        <view v-if="isLoggedIn" class="user-header-member-wrap" @tap="showMemberCode">
          <text class="cuIcon-qrcode text-xl"></text>
          <text class="member-code-label">会员码</text>
        </view>
      </view>
    </view>
    <view v-if="showMemberCodeModal" class="member-code-mask" @tap="showMemberCodeModal = false">
      <view class="member-code-modal" @tap.stop>
        <view class="member-code-modal-header">
          <text class="member-code-modal-title">我的会员码</text>
          <view class="member-code-modal-close" @tap="showMemberCodeModal = false"><text class="cuIcon-close"></text></view>
        </view>
        <view v-if="memberCodeLoading" class="member-code-loading">加载中...</view>
        <view v-else-if="memberCodeError" class="member-code-error">{{ memberCodeError }}</view>
        <template v-else-if="memberCodeData && memberCodeData.memberCode">
          <view class="member-code-qr-wrap">
            <image class="member-code-qr" :src="memberCodeQrUrl" mode="aspectFit" />
          </view>
          <view class="member-code-value-wrap">
            <text class="member-code-value">{{ memberCodeData.memberCode }}</text>
          </view>
          <view class="member-code-hint">出示给商家扫码或输入会员码</view>
          <view class="member-code-actions">
            <view class="member-code-btn member-code-btn-copy" @tap="copyMemberCode">
              <text class="cuIcon-copy"></text>
              <text>复制会员码</text>
            </view>
            <view class="member-code-btn member-code-btn-close" @tap="showMemberCodeModal = false">关闭</view>
          </view>
        </template>
        <view v-else class="member-code-empty">暂无会员码</view>
      </view>
    </view>
    <view class="cu-listradius order-list tm-section tm-card">
      <view class="cu-bar">
        <view class="action"><text class="cuIcon-titles text-black titles-color"></text>我的订单</view>
        <navigator class="action" url="/pages/order/order-list/index" hover-class="none">全部订单<text class="cuIcon-right"></text></navigator>
      </view>
      <view class="cu-list grid col-4 no-border order-list-2">
        <view class="cu-item">
          <navigator url="/pages/order/order-list/index?status=0" hover-class="none">
            <view class="order-icon cuIcon-pay text-orange">
              <view class="cu-tag badge" v-if="orderCount.payment > 0">{{ orderCount.payment > 99 ? '99+' : orderCount.payment }}</view>
            </view>
            <text class="text-black">待付款</text>
          </navigator>
        </view>
        <view class="cu-item">
          <navigator url="/pages/order/order-list/index?status=1" hover-class="none">
            <view class="order-icon cuIcon-deliver text-blue">
              <view class="cu-tag badge" v-if="orderCount.deliver > 0">{{ orderCount.deliver > 99 ? '99+' : orderCount.deliver }}</view>
            </view>
            <text class="text-black">待发货</text>
          </navigator>
        </view>
        <view class="cu-item">
          <navigator url="/pages/order/order-list/index?status=2" hover-class="none">
            <view class="order-icon cuIcon-goods text-green">
              <view class="cu-tag badge" v-if="orderCount.receive > 0">{{ orderCount.receive > 99 ? '99+' : orderCount.receive }}</view>
            </view>
            <text class="text-black">待收货</text>
          </navigator>
        </view>
        <view class="cu-item"><navigator url="/pages/order/order-list/index?status=4" hover-class="none"><view class="order-icon cuIcon-roundcheck text-gray"></view><text class="text-black">已完成</text></navigator></view>
      </view>
    </view>
    <view class="cu-list menu card-menu radius address tm-section tm-card">
      <navigator class="cu-item arrow" url="/pages/user/user-address/list/index" hover-class="none">
        <view class="content"><view class="address-icon cuIcon-location text-blue"></view><text class="text-black">收货地址</text></view>
      </navigator>
      <navigator class="cu-item arrow" url="/pages/coupon/my-coupons/index" hover-class="none">
        <view class="content"><view class="address-icon cuIcon-form text-orange"></view><text class="text-black">我的优惠券</text></view>
      </navigator>
      <view class="cu-item arrow" @tap="showService">
        <view class="content"><view class="address-icon cuIcon-service text-green"></view><text class="text-black">联系客服</text></view>
      </view>
      <view v-if="isLoggedIn" class="cu-item arrow" @tap="logout">
        <view class="content"><view class="address-icon cuIcon-roundclose text-red"></view><text class="text-black">退出登录</text></view>
      </view>
    </view>
    <view v-if="showServiceModal" class="member-code-mask" @tap="showServiceModal = false">
      <view class="member-code-modal service-modal" @tap.stop>
        <view class="member-code-modal-header">
          <text class="member-code-modal-title">联系客服</text>
          <view class="member-code-modal-close" @tap="showServiceModal = false"><text class="cuIcon-close"></text></view>
        </view>
        <view class="service-modal-body">
          <view v-if="serviceConfig && serviceConfig.phone" class="service-row" @tap="callPhone(serviceConfig.phone)">
            <text class="cuIcon-phone text-green"></text>
            <text class="service-label">客服电话</text>
            <text class="service-value">{{ serviceConfig.phone }}</text>
            <text class="cuIcon-right text-gray"></text>
          </view>
          <view v-if="serviceConfig && serviceConfig.wechat" class="service-row" @tap="copyWechat(serviceConfig.wechat)">
            <text class="cuIcon-weixin text-green"></text>
            <text class="service-label">客服微信</text>
            <text class="service-value">{{ serviceConfig.wechat }}</text>
            <text class="cuIcon-right text-gray"></text>
          </view>
          <view v-if="serviceConfig && !serviceConfig.phone && !serviceConfig.wechat" class="member-code-empty">暂无客服信息</view>
        </view>
      </view>
    </view>
    <view class="text-gray text-sm text-center page-footer">www.dingyangmall.com 提供技术支持</view>
    <view v-if="!isLoggedIn" class="login-banner-bottom-spacer"></view>
    <login-banner
      ref="loginBanner"
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
import util from '@/utils/util'
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
      loginLoading: false,
      showMemberCodeModal: false,
      memberCodeLoading: false,
      memberCodeError: '',
      orderCount: { payment: 0, deliver: 0, receive: 0, evaluate: 0 },
      memberCodeData: null,
      serviceConfig: null,
      showServiceModal: false
    }
  },
  computed: {
    memberCodeQrUrl() {
      if (!this.memberCodeData || !this.memberCodeData.memberCode) return ''
      return 'https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=' + encodeURIComponent(this.memberCodeData.memberCode)
    },
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
    },
    displayPoints() {
      const p = (this.userInfo && this.userInfo.points) ?? (this.memberCodeData && this.memberCodeData.points)
      return p != null ? p : '--'
    }
  },
  onShow() {
    const app = getApp()
    const pages = getCurrentPages()
    const page = pages[pages.length - 1]
    if (page && typeof page.getTabBar === 'function') {
      const tabBar = page.getTabBar()
      if (tabBar && tabBar.setData) tabBar.setData({ selected: 3 })
    }
    this.isLoggedIn = !!app.globalData.thirdSession
    this.wxUser = app.globalData.wxUser
    this.profileSkipped = !!app.globalData.profileSkipped
    const wxUser = app.globalData.wxUser || {}
    this.hasPhone = !!(wxUser.phoneNumber || wxUser.phone)
    this.displayNickname = (wxUser.nickName || wxUser.nickname || '').trim()
    this.displayAvatar = (wxUser.headimgUrl || wxUser.avatarUrl || wxUser.avatar || '').trim()
    this.displayPhone = (wxUser.phoneNumber || wxUser.phone || '').trim()
    util.updateCartBadge(app.globalData.shoppingCartCount || 0)
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
      if (typeof this.fetchPoints === 'function') this.fetchPoints()
      if (app.shoppingCartCount) app.shoppingCartCount()
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
    goIntegral() {
      uni.navigateTo({ url: '/pages/integral/packet/index' })
    },
    fetchPoints() {
      const api = (getApp() && getApp().api) || apiModule
      if (!api || typeof api.getMemberCode !== 'function') return
      api.getMemberCode().then(res => {
        if (res && res.code === 200 && res.data) {
          const pts = res.data.points
          if (pts != null) {
            this.userInfo = Object.assign(this.userInfo || {}, { points: pts })
            if (!this.memberCodeData) this.memberCodeData = {}
            this.memberCodeData.points = pts
          }
        }
      }).catch(() => {})
    },
    fetchUserDataOnceReady() {
      const app = typeof getApp === 'function' ? getApp() : null
      const api = (app && app.api) || (app && app.globalData && app.globalData.__api) || apiModule
      if (!api) return
      if (this.isLoggedIn) {
        if (typeof api.wxUserGet === 'function') this.wxUserGet()
        this.orderCountAll()
        this.fetchPoints()
      }
    },
    showMemberCode() {
      this.showMemberCodeModal = true
      this.memberCodeError = ''
      this.memberCodeData = null
      this.memberCodeLoading = true
      const api = (getApp() && getApp().api) || (getApp() && getApp().globalData && getApp().globalData.__api) || apiModule
      if (api && typeof api.getMemberCode === 'function') {
        api.getMemberCode().then(res => {
          this.memberCodeLoading = false
          if (res && res.code === 200 && res.data) {
            this.memberCodeData = res.data
            if (res.data.points != null) this.userInfo = Object.assign(this.userInfo || {}, { points: res.data.points })
          } else {
            this.memberCodeError = (res && res.msg) || '获取会员码失败'
          }
        }).catch(err => {
          this.memberCodeLoading = false
          this.memberCodeError = (err && (err.msg || err.message)) || '获取会员码失败'
        })
      } else {
        this.memberCodeLoading = false
        this.memberCodeError = '接口未就绪'
      }
    },
    copyMemberCode() {
      if (!this.memberCodeData || !this.memberCodeData.memberCode) return
      uni.setClipboardData({
        data: this.memberCodeData.memberCode,
        success: () => { uni.showToast({ title: '已复制会员码', icon: 'success' }) },
        fail: () => { uni.showToast({ title: '复制失败', icon: 'none' }) }
      })
    },
    callPhone(phone) {
      if (!phone) return
      uni.makePhoneCall({ phoneNumber: String(phone).replace(/\D/g, '') })
    },
    copyWechat(wx) {
      if (!wx) return
      uni.setClipboardData({
        data: String(wx),
        success: () => { uni.showToast({ title: '已复制微信号', icon: 'success' }) },
        fail: () => { uni.showToast({ title: '复制失败', icon: 'none' }) }
      })
    },
    /** 联系客服：拉取客服配置并展示电话/微信，无配置时提示 */
    showService() {
      const api = (getApp() && getApp().api) || (getApp() && getApp().globalData && getApp().globalData.__api) || apiModule
      if (api && typeof api.getServiceConfig === 'function') {
        api.getServiceConfig().then(res => {
          const data = (res && res.data) != null ? res.data : (res || {})
          const phone = (data.phone || data.servicePhone || data.tel || '').trim()
          const wechat = (data.wechat || data.serviceWechat || data.wx || '').trim()
          if (phone || wechat) {
            this.serviceConfig = { phone, wechat }
            this.showServiceModal = true
          } else {
            uni.showToast({ title: '暂无客服信息', icon: 'none' })
          }
        }).catch(() => {
          uni.showToast({ title: '暂无客服配置', icon: 'none' })
        })
      } else {
        uni.showToast({ title: '暂无客服配置', icon: 'none' })
      }
    },
    /** 点击头像打开更新头像/昵称弹窗（已登录时） */
    openProfileModal() {
      if (!this.isLoggedIn) return
      const banner = this.$refs.loginBanner
      if (banner && typeof banner.openProfileModal === 'function') banner.openProfileModal()
    },
    logout() {
      const u = typeof uni !== 'undefined' ? uni : wx
      u.showModal({
        title: '提示',
        content: '确定要退出登录吗？',
        cancelText: '取消',
        confirmText: '退出',
        confirmColor: '#ff0036',
        success: (res) => {
          if (!res.confirm) return
          const app = getApp()
          if (app && app.globalData) {
            app.globalData.wxToken = null
            app.globalData.thirdSession = null
            app.globalData.wxUser = null
            app.globalData.profileSkipped = false
            app.globalData.shoppingCartCount = '0'
          }
          try {
            if (u.removeStorageSync) {
              u.removeStorageSync('wx_token')
              u.removeStorageSync('wx_third_session')
            }
          } catch (e) {}
          this.isLoggedIn = false
          this.wxUser = null
          this.userInfo = null
          this.displayNickname = ''
          this.displayAvatar = ''
          this.displayPhone = ''
          util.updateCartBadge(0)
          u.showToast({ title: '已退出登录', icon: 'none' })
        }
      })
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
      api.orderCountAll().then(res => {
        const data = (res && res.data) || res || {}
        this.orderCount = {
          payment: data['0'] || 0,
          deliver: data['1'] || 0,
          receive: data['2'] || 0,
          evaluate: data['3'] || 0
        }
      }).catch(() => {})
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
.user-points-row { margin-top: 20rpx; padding-top: 16rpx; border-top: 1rpx solid #f0f0f0; }
.user-points-value { display: inline-flex; align-items: baseline; gap: 8rpx; }
.user-points-value .points-num { font-size: 40rpx; font-weight: bold; color: #ff0036; }
.user-points-value .points-label { font-size: 26rpx; color: #666; }
.user-header-body { padding: 8rpx 0; }
.user-header-info { margin-left: 20rpx; padding: 4rpx 0; }
.user-header-info .user-header-actions { margin-top: 12rpx; }
.user-header-info .user-header-actions .cu-tag { margin-right: 12rpx; margin-bottom: 4rpx; }
.user-header-info .text-xs { margin-top: 8rpx; }
.user-header .head { width: 128rpx; height: 128rpx; background-color: #e5e7eb; background-size: cover; }
.avatar-tappable { cursor: pointer; }
.order-icon, .address-icon { font-size: 44rpx; width: 80rpx; height: 80rpx; display: flex; align-items: center; justify-content: center; border-radius: 16rpx; flex-shrink: 0; position: relative; }
.address-icon { margin-right: 20rpx; }
.order-list { margin-top: 12rpx; padding: 8rpx 0 12rpx; }
.order-list .cu-bar { padding: 12rpx 24rpx; min-height: auto; }
.address { margin-top: 12rpx; }
.address .cu-item .content { display: flex; flex-direction: row; align-items: center; }
.address .cu-item { padding: 20rpx 24rpx; }
.page-footer { margin-top: 32rpx; padding: 0 24rpx; }
.login-banner-bottom-spacer { height: 140rpx; }
.user-header-member-wrap { flex-shrink: 0; margin-left: 24rpx; padding: 24rpx; display: flex; flex-direction: column; align-items: center; justify-content: center; border-radius: 16rpx; background: linear-gradient(135deg, #fef2f2 0%, #fce7f3 100%); min-width: 120rpx; }
.user-header-member-wrap .member-code-label { font-size: 24rpx; color: #374151; margin-top: 8rpx; }
.member-code-mask { position: fixed; left: 0; right: 0; top: 0; bottom: 0; background: rgba(0,0,0,0.45); z-index: 999; display: flex; align-items: center; justify-content: center; padding: 48rpx; }
.member-code-modal { background: #fff; max-width: 580rpx; width: 100%; border-radius: 32rpx; overflow: hidden; box-shadow: 0 24rpx 48rpx rgba(0,0,0,0.12); text-align: center; }
.member-code-modal-header { position: relative; padding: 36rpx 32rpx 24rpx; border-bottom: 1rpx solid #f0f0f0; }
.member-code-modal-title { font-size: 32rpx; font-weight: 600; color: #1a1a1a; }
.member-code-modal-close { position: absolute; right: 24rpx; top: 28rpx; width: 56rpx; height: 56rpx; display: flex; align-items: center; justify-content: center; color: #999; font-size: 36rpx; }
.member-code-qr-wrap { padding: 40rpx 32rpx 24rpx; display: flex; justify-content: center; }
.member-code-qr { width: 280rpx; height: 280rpx; background: #fff; border: 1rpx solid #eee; border-radius: 16rpx; padding: 16rpx; box-sizing: border-box; }
.member-code-value-wrap { margin: 0 32rpx; padding: 20rpx 24rpx; background: #f7f8fa; border-radius: 16rpx; }
.member-code-value { font-size: 36rpx; font-weight: 600; color: #1a1a1a; letter-spacing: 6rpx; word-break: break-all; }
.member-code-hint { font-size: 24rpx; color: #8c8c8c; margin-top: 20rpx; padding: 0 32rpx; }
.member-code-actions { display: flex; flex-direction: column; gap: 20rpx; padding: 32rpx 32rpx 40rpx; }
.member-code-btn { height: 88rpx; line-height: 88rpx; border-radius: 44rpx; font-size: 30rpx; text-align: center; }
.member-code-btn-copy { background: linear-gradient(135deg, #ff0036 0%, #ff3352 100%); color: #fff; display: flex; align-items: center; justify-content: center; gap: 12rpx; }
.member-code-btn-copy .cuIcon-copy { font-size: 34rpx; }
.member-code-btn-close { background: #f5f5f5; color: #666; }
.member-code-loading, .member-code-error { font-size: 28rpx; color: #8c8c8c; padding: 48rpx 32rpx; }
.member-code-error { color: #ed6a0c; }
.member-code-empty { font-size: 28rpx; color: #8c8c8c; padding: 48rpx 32rpx; }
.service-modal .service-modal-body { padding: 24rpx 32rpx 40rpx; }
.service-row { display: flex; align-items: center; padding: 24rpx 0; border-bottom: 1rpx solid #f0f0f0; }
.service-row:last-child { border-bottom: none; }
.service-row .cuIcon-phone, .service-row .cuIcon-weixin { font-size: 40rpx; margin-right: 16rpx; }
.service-label { flex: 0 0 140rpx; font-size: 28rpx; color: #666; }
.service-value { flex: 1; font-size: 30rpx; color: #1a1a1a; }
.service-row .cuIcon-right { font-size: 28rpx; margin-left: 8rpx; }
</style>
