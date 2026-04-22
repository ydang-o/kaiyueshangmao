<!--
  Copyright (C) 2018-2019 www.dingyangmall.com
-->
<template>
  <view class="page tm-page padding-xl">
    <view class="text-center margin-bottom-xl">
      <view class="text-xxl text-bold text-black">商家登录</view>
    </view>
    <view>
      <view class="cu-form-group margin-top"><view class="title">账号</view><input placeholder="请输入系统账号" v-model="username" /></view>
      <view class="cu-form-group"><view class="title">密码</view><input placeholder="请输入密码" type="password" v-model="password" /></view>
      <view class="cu-form-group" v-if="captchaEnabled">
        <view class="title">验证码</view>
        <input placeholder="请输入验证码" v-model="code" />
        <image :src="captchaImg" class="captcha-img" mode="aspectFit" @tap="getCaptcha" />
      </view>
      <view class="padding flex flex-direction margin-top-xl">
        <button class="cu-btn tm-primary-btn margin-tb-sm lg" @tap="login" :loading="loading">登录</button>
      </view>
    </view>
  </view>
</template>

<script>
import apiModule from '@/utils/api'
export default {
  name: 'MerchantLoginPage',
  data() {
    return {
      username: '',
      password: '',
      code: '',
      uuid: '',
      captchaImg: '',
      captchaEnabled: true,
      loading: false
    }
  },
  onLoad() {
    this.getCaptcha()
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
    getCaptcha() {
      const api = this.getApi()
      if (!api || typeof api.getCaptcha !== 'function') return
      api.getCaptcha().then(res => {
        if (res.captchaEnabled === undefined || res.captchaEnabled) {
          this.captchaEnabled = true
          this.captchaImg = 'data:image/gif;base64,' + (res.img || '')
          this.uuid = res.uuid || ''
        } else {
          this.captchaEnabled = false
        }
      }).catch(() => {})
    },
    login() {
      if (!this.username || !this.password) {
        uni.showToast({ title: '请输入账号和密码', icon: 'none' })
        return
      }
      if (this.captchaEnabled && !this.code) {
        uni.showToast({ title: '请输入验证码', icon: 'none' })
        return
      }
      const api = this.getApi()
      if (!api || typeof api.merchantLogin !== 'function') {
        uni.showToast({ title: '接口未就绪', icon: 'none' })
        return
      }
      this.loading = true
      api.merchantLogin({
        username: this.username,
        password: this.password,
        code: this.code,
        uuid: this.uuid
      }).then(res => {
        this.loading = false
        if (res.code === 200) {
          uni.setStorageSync('merchantToken', res.token)
          uni.showToast({ title: '登录成功' })
          setTimeout(() => uni.redirectTo({ url: '/pages/merchant/index/index' }), 1000)
        } else {
          uni.showToast({ title: res.msg || '登录失败', icon: 'none' })
          if (this.captchaEnabled) this.getCaptcha()
        }
      }).catch(() => {
        this.loading = false
        if (this.captchaEnabled) this.getCaptcha()
      })
    }
  }
}
</script>

<style scoped>
.captcha-img { width: 200rpx; height: 70rpx; margin-left: 20rpx; }
</style>
