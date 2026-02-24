<!--
  Copyright (C) 2018-2019 www.dingyangmall.com
-->
<template>
  <view class="page" style="background-color:#f3f4f7;">
    <view class="cu-list menu-avatar bg-white padding-bottom">
      <view class="flex justify-end margin-right">
        <view class="text-xl margin-top" @tap="settings"><text class="cuIcon-settingsfill text-black"></text></view>
      </view>
      <view class="cu-avatar round xl head flex" :style="wxUser && wxUser.headimgUrl ? 'background-image:url(' + wxUser.headimgUrl + ')' : ''">{{ !(wxUser && wxUser.headimgUrl) ? '头' : '' }}</view>
      <view class="content text-center margin-top-sm">
        <view class="margin-top-xs text-xxl text-bold text-black" v-if="wxUser && wxUser.nickName">{{ wxUser.nickName }}</view>
        <button class="cu-btn round sm margin-top-xs" @tap="getUserProfile">{{ !(wxUser && wxUser.nickName) ? '获取昵称' : '更新昵称' }}</button>
        <view class="margin-top-xs text-sm" v-if="userInfo && userInfo.memberCode">
          <view>会员码：{{ userInfo.memberCode }}</view>
          <view>积分：{{ userInfo.points || 0 }}</view>
        </view>
      </view>
    </view>
    <view class="cu-listradius order-list">
      <view class="cu-bar">
        <view class="action"><text class="cuIcon-titles text-black titles-color"></text>我的订单</view>
        <navigator class="action" url="/pages/order/order-list/index" hover-class="none">全部订单<text class="cuIcon-right"></text></navigator>
      </view>
      <view class="cu-list grid col-4 no-border order-list-2">
        <view class="cu-item"><navigator url="/pages/order/order-list/index?status=0" hover-class="none"><image class="order-image" src="/static/img/5-1.png"></image><text class="text-black">待付款</text></navigator></view>
        <view class="cu-item"><navigator url="/pages/order/order-list/index?status=1" hover-class="none"><image class="order-image" src="/static/img/5-2.png"></image><text class="text-black">待发货</text></navigator></view>
        <view class="cu-item"><navigator url="/pages/order/order-list/index?status=2" hover-class="none"><image class="order-image" src="/static/img/5-3.png"></image><text class="text-black">待收货</text></navigator></view>
        <view class="cu-item"><navigator url="/pages/order/order-list/index?status=4" hover-class="none"><image class="order-image" src="/static/img/5-4.png"></image><text class="text-black">已完成</text></navigator></view>
      </view>
    </view>
    <view class="cu-list menu card-menu radius address">
      <navigator class="cu-item arrow" url="/pages/coupon/my-coupons/index" hover-class="none">
        <view class="content"><image class="address-image" src="/static/img/5-4.png"></image><text class="text-black">我的优惠券</text></view>
      </navigator>
      <view class="cu-item arrow" @tap="signIn">
        <view class="content"><image class="address-image" src="/static/img/5-1.png"></image><text class="text-black">每日签到</text></view>
      </view>
      <navigator class="cu-item arrow" url="/pages/lottery/index/index" hover-class="none">
        <view class="content"><image class="address-image" src="/static/img/5-2.png"></image><text class="text-black">积分抽奖</text></view>
      </navigator>
      <navigator class="cu-item arrow" url="/pages/integral/packet/index" hover-class="none">
        <view class="content"><image class="address-image" src="/static/img/5-3.png"></image><text class="text-black">积分红包</text></view>
      </navigator>
      <navigator class="cu-item arrow" url="/pages/merchant/login/index" hover-class="none">
        <view class="content"><image class="address-image" src="/static/img/5-4.png"></image><text class="text-black">商家入口</text></view>
      </navigator>
      <navigator class="cu-item arrow" url="/pages/user/user-address/list/index" hover-class="none">
        <view class="content"><image class="address-image" src="/static/img/7-1.png"></image><text class="text-black">收货地址</text></view>
      </navigator>
    </view>
    <view class="text-gray text-sm text-center margin-top-xl">www.dingyangmall.com 提供技术支持</view>
  </view>
</template>

<script>
export default {
  name: 'UserCenterPage',
  data() {
    const app = getApp()
    return { config: app.globalData.config || {}, wxUser: null, userInfo: null }
  },
  onShow() {
    const app = getApp()
    uni.setTabBarBadge({ index: 2, text: (app.globalData.shoppingCartCount || '') + '' })
    this.wxUser = app.globalData.wxUser
    this.wxUserGet()
    this.orderCountAll()
    if (this.config.adEnable && uni.createInterstitialAd) {
      try {
        const ad = uni.createInterstitialAd({ adUnitId: this.config.adInsertScreenID })
        ad.show().catch(() => {})
      } catch (e) {}
    }
  },
  methods: {
    settings() { uni.openSetting() },
    getUserProfile() {
      uni.getUserProfile({ desc: '用于完善会员资料', success: (detail) => {
        getApp().api.wxUserSave(detail).then(res => {
          this.wxUser = res.data
          getApp().globalData.wxUser = res.data
          this.wxUserGet()
        })
      }})
    },
    wxUserGet() {
      getApp().api.wxUserGet().then(res => { this.userInfo = res.data })
    },
    orderCountAll() {
      getApp().api.orderCountAll().then(res => {})
    },
    signIn() {
      getApp().api.memberSignIn().then(res => {
        uni.showToast({ title: res.msg || '签到成功', icon: 'success' })
        this.wxUserGet()
      })
    }
  }
}
</script>

<style scoped>
.order-image { width: 80rpx; height: 80rpx; }
.address-image { width: 80rpx; height: 80rpx; margin-right: 20rpx; }
</style>
