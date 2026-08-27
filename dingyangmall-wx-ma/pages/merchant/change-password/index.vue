<template>
  <view class="page tm-page padding-xl">
    <view class="password-card bg-white radius shadow-lg padding-xl">
      <view class="text-center margin-bottom-xl">
        <view class="text-xxl text-bold text-black">修改密码</view>
        <view v-if="userName" class="text-sm text-gray margin-top-xs">{{ userName }}</view>
      </view>
      <view class="cu-form-group margin-top"><view class="title">原密码</view><input v-model="oldPassword" password placeholder="请输入原密码" /></view>
      <view class="cu-form-group"><view class="title">新密码</view><input v-model="newPassword" password placeholder="至少6位" /></view>
      <view class="cu-form-group"><view class="title">确认密码</view><input v-model="confirmPassword" password placeholder="请再次输入新密码" /></view>
      <button class="cu-btn bg-green block margin-top-xl" :loading="loading" @tap="changePassword">确认修改</button>
      <button class="cu-btn line-gray block margin-top" @tap="goBack">返回</button>
    </view>
  </view>
</template>
<script>
export default {
  name: 'MerchantChangePasswordPage',
  data() { return { userName: '', oldPassword: '', newPassword: '', confirmPassword: '', loading: false } },
  onLoad() { this.loadUserInfo() },
  methods: {
    getApi() { const app = typeof getApp === 'function' ? getApp() : null; return (app && app.api) || {} },
    loadUserInfo() {
      try {
        const app = typeof getApp === 'function' ? getApp() : null
        const info = app && app.globalData && app.globalData.merchantUserInfo
        const saved = uni.getStorageSync('merchantUserInfo')
        this.userName = (info && info.userName) || (saved && saved.userName) || ''
      } catch (e) {}
    },
    changePassword() {
      if (!this.oldPassword) return uni.showToast({ title: '请输入原密码', icon: 'none' })
      if (!this.newPassword) return uni.showToast({ title: '请输入新密码', icon: 'none' })
      if (this.newPassword.length < 6) return uni.showToast({ title: '新密码长度不能少于6位', icon: 'none' })
      if (this.newPassword !== this.confirmPassword) return uni.showToast({ title: '两次输入的新密码不一致', icon: 'none' })
      if (this.oldPassword === this.newPassword) return uni.showToast({ title: '新密码不能与原密码相同', icon: 'none' })
      const api = this.getApi()
      if (!api || typeof api.merchantChangePassword !== 'function') return uni.showToast({ title: '接口未就绪', icon: 'none' })
      this.loading = true
      api.merchantChangePassword({ oldPassword: this.oldPassword, newPassword: this.newPassword }).then((res) => {
        this.loading = false
        if (res && (res.code === 0 || res.code === 200)) {
          uni.showToast({ title: '密码修改成功，请重新登录' })
          uni.removeStorageSync('merchantToken')
          setTimeout(() => uni.reLaunch({ url: '/pages/merchant/login/index' }), 1500)
        } else uni.showToast({ title: (res && res.msg) || '修改失败', icon: 'none' })
      }).catch(() => { this.loading = false; uni.showToast({ title: '修改失败，请重试', icon: 'none' }) })
    },
    goBack() { uni.navigateBack() }
  }
}
</script>
<style scoped>
.password-card { min-height: 650rpx; }
.block { display: block; width: 100%; }
</style>
