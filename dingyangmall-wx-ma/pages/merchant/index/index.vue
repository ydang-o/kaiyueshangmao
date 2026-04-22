<!--
  Copyright (C) 2018-2019 www.dingyangmall.com
-->
<template>
  <view class="page tm-page padding">
    <view class="bg-white padding radius shadow-lg margin-bottom">
      <view class="text-xl text-bold margin-bottom">常用功能</view>
      <view class="grid col-2 text-center">
        <view class="padding" @tap="scanUser"><view class="cuIcon-scan text-blue text-sl"></view><view class="margin-top-sm">扫会员码</view><view class="text-gray text-xs margin-top-xs">赠送积分</view></view>
        <view class="padding" @tap="scanCoupon"><view class="cuIcon-qrcode text-orange text-sl"></view><view class="margin-top-sm">扫核销码</view><view class="text-gray text-xs margin-top-xs">核销商品券</view></view>
        <view class="padding" @tap="showInputUser"><view class="cuIcon-edit text-blue text-sl"></view><view class="margin-top-sm">手动输码</view><view class="text-gray text-xs margin-top-xs">输入会员码</view></view>
        <view class="padding" @tap="showInputCoupon"><view class="cuIcon-write text-orange text-sl"></view><view class="margin-top-sm">手动核销</view><view class="text-gray text-xs margin-top-xs">输入券码</view></view>
      </view>
    </view>
    <view class="cu-modal" :class="showInputModal ? 'show' : ''">
      <view class="cu-dialog">
        <view class="cu-bar bg-white justify-end">
          <view class="content">{{ inputType == 'user' ? '输入会员码/手机号' : '输入券码' }}</view>
          <view class="action" @tap="hideInputModal"><text class="cuIcon-close text-red"></text></view>
        </view>
        <view class="padding-xl">
          <view class="cu-form-group border"><input :placeholder="inputType == 'user' ? '请输入会员码或手机号' : '请输入商品券码'" v-model="manualCode" /></view>
        </view>
        <view class="cu-bar bg-white justify-end">
          <view class="action">
            <button class="cu-btn line-green text-green" @tap="hideInputModal">取消</button>
            <button class="cu-btn bg-green margin-left" @tap="confirmManualInput">确认</button>
          </view>
        </view>
      </view>
    </view>
    <view class="cu-modal" :class="showUserModal ? 'show' : ''">
      <view class="cu-dialog">
        <view class="cu-bar bg-white justify-end">
          <view class="content">会员信息</view>
          <view class="action" @tap="hideUserModal"><text class="cuIcon-close text-red"></text></view>
        </view>
        <view class="padding-xl text-left">
          <view>昵称：{{ userInfo.nickname }}</view>
          <view class="margin-top-sm">手机：{{ userInfo.phone }}</view>
          <view class="margin-top-sm">当前积分：{{ userInfo.points }}</view>
          <view class="cu-form-group margin-top border"><view class="title">赠送积分</view><input placeholder="请输入积分数量" type="number" v-model="pointsToGive" /></view>
        </view>
        <view class="cu-bar bg-white justify-end">
          <view class="action">
            <button class="cu-btn line-green text-green" @tap="hideUserModal">取消</button>
            <button class="cu-btn bg-green margin-left" @tap="givePoints">确认赠送</button>
          </view>
        </view>
      </view>
    </view>
    <view class="padding flex flex-direction margin-top-xl">
      <button class="cu-btn tm-secondary-btn lg" @tap="logout">退出登录</button>
    </view>
  </view>
</template>

<script>
import apiModule from '@/utils/api'
export default {
  name: 'MerchantIndexPage',
  data() {
    return {
      showUserModal: false,
      userInfo: {},
      pointsToGive: '',
      memberCode: '',
      showInputModal: false,
      inputType: '',
      manualCode: ''
    }
  },
  onLoad() {
    const token = uni.getStorageSync('merchantToken')
    if (!token) uni.redirectTo({ url: '/pages/merchant/login/index' })
  },
  methods: {
    getApi() {
      try {
        const app = typeof getApp === 'function' ? getApp() : null
        const fromApp = app && app.api && typeof app.api === 'object'
        return (fromApp ? app.api : null) || (apiModule && typeof apiModule === 'object' ? apiModule : null) || {}
      } catch (e) {
        return apiModule || {}
      }
    },
    scanUser() {
      uni.scanCode({ success: (res) => this.fetchUserInfo(res.result) })
    },
    showInputUser() { this.showInputModal = true; this.inputType = 'user'; this.manualCode = '' },
    showInputCoupon() { this.showInputModal = true; this.inputType = 'coupon'; this.manualCode = '' },
    hideInputModal() { this.showInputModal = false },
    confirmManualInput() {
      if (!this.manualCode) { uni.showToast({ title: '请输入内容', icon: 'none' }); return }
      this.hideInputModal()
      if (this.inputType === 'user') this.fetchUserInfo(this.manualCode)
      else this.verifyCoupon(this.manualCode)
    },
    fetchUserInfo(code) {
      const api = this.getApi()
      if (!api || typeof api.merchantScanUser !== 'function') {
        uni.showToast({ title: '接口未就绪', icon: 'none' })
        return
      }
      uni.showLoading({ title: '查询中' })
      api.merchantScanUser(code).then(res => {
        uni.hideLoading()
        if (res.code === 200) {
          this.userInfo = res.data || {}
          this.memberCode = code
          this.showUserModal = true
        } else uni.showToast({ title: res.msg || '无效的会员码', icon: 'none' })
      }).catch(() => uni.hideLoading())
    },
    scanCoupon() {
      uni.scanCode({ success: (res) => this.verifyCoupon(res.result) })
    },
    verifyCoupon(code) {
      const api = this.getApi()
      if (!api || typeof api.merchantVerifyCoupon !== 'function') {
        uni.showToast({ title: '接口未就绪', icon: 'none' })
        return
      }
      uni.showModal({ title: '确认核销', content: '确定要核销该商品券吗？', success: (res) => {
        if (res.confirm) {
          uni.showLoading({ title: '核销中' })
          api.merchantVerifyCoupon({ couponCode: code }).then(resp => {
            uni.hideLoading()
            if (resp.code === 200) uni.showToast({ title: '核销成功' })
            else uni.showToast({ title: resp.msg || '核销失败', icon: 'none' })
          }).catch(() => uni.hideLoading())
        }
      }})
    },
    hideUserModal() { this.showUserModal = false; this.pointsToGive = '' },
    givePoints() {
      if (!this.pointsToGive || this.pointsToGive <= 0) { uni.showToast({ title: '请输入有效积分', icon: 'none' }); return }
      const api = this.getApi()
      if (!api || typeof api.merchantGivePoints !== 'function') {
        uni.showToast({ title: '接口未就绪', icon: 'none' })
        return
      }
      uni.showLoading({ title: '处理中' })
      api.merchantGivePoints({ memberCode: this.memberCode, points: Number(this.pointsToGive) }).then(res => {
        uni.hideLoading()
        if (res.code === 200) { uni.showToast({ title: '赠送成功' }); this.hideUserModal() }
        else uni.showToast({ title: res.msg || '赠送失败', icon: 'none' })
      }).catch(() => uni.hideLoading())
    },
    logout() {
      uni.showModal({ title: '提示', content: '确定要退出登录吗？', success: (res) => {
        if (res.confirm) { uni.removeStorageSync('merchantToken'); uni.reLaunch({ url: '/pages/merchant/login/index' }) }
      }})
    }
  }
}
</script>

<style scoped>
.page { min-height: 100vh; background: #f3f4f7; }
</style>
