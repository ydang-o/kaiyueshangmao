<!--
  登录横幅：未登录时显示在 TabBar 页上方，提供微信一键登录
-->
<template>
  <view v-if="!isLoggedIn" class="login-banner tm-card">
    <view class="login-banner-inner">
      <text class="login-banner-text tm-text-secondary">登录后享受完整服务</text>
      <button
        class="cu-btn round tm-primary-btn"
        type="button"
        hover-class="button-hover"
        :loading="loginLoading"
        @tap="onWxLogin"
      >
        微信一键登录
      </button>
    </view>
  </view>
</template>

<script>
export default {
  name: 'LoginBanner',
  props: {
    isLoggedIn: { type: Boolean, default: false }
  },
  data() {
    return { loginLoading: false }
  },
  methods: {
    onWxLogin() {
      const app = getApp()
      if (!app || typeof app.doLogin !== 'function') {
        wx.showToast({ title: '登录功能暂不可用', icon: 'none' })
        return
      }
      this.loginLoading = true
      app.doLogin().then((result) => {
        this.loginLoading = false
        this.$emit('login-success')
        if (result === 'success' && typeof app.shoppingCartCount === 'function') {
          try { app.shoppingCartCount() } catch (e) {}
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.login-banner {
  margin: 20rpx;
  padding: 24rpx 32rpx;
}
.login-banner-inner {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.login-banner-text {
  font-size: 28rpx;
}
</style>
