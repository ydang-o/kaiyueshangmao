<!--
  未登录：底部横幅 微信一键登录；已登录未完善：屏幕居中弹窗 完善个人资料（头像、昵称、授权手机号）
-->
<template>
  <!-- 未登录：底部条 -->
  <view v-if="!isLoggedIn" class="login-banner-wrap">
    <view class="login-banner tm-card">
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
  </view>
  <!-- 已登录未完善：居中弹窗，适配当前 UI 主题 -->
  <view v-else-if="showProfileModal" class="profile-modal-mask" @tap.stop="">
    <view class="profile-modal-card tm-card" @tap.stop="">
      <view class="profile-modal-title tm-section-title">完善个人资料</view>
      <view class="profile-modal-field">
        <text class="profile-field-label tm-text-secondary">头像</text>
        <button class="avatar-btn" open-type="chooseAvatar" @chooseavatar="onChooseAvatar">
          <image v-if="localAvatar" class="avatar-img" :src="localAvatar" mode="aspectFill" />
          <view v-else class="avatar-placeholder">
            <text class="avatar-hint tm-text-muted">点击选择头像</text>
          </view>
        </button>
      </view>
      <view class="profile-modal-field">
        <text class="profile-field-label tm-text-secondary">昵称</text>
        <input
          type="nickname"
          class="nickname-input"
          :value="localNickname"
          placeholder="请输入昵称"
          @blur="onNicknameBlur"
          @input="onNicknameInput"
        />
      </view>
      <view class="profile-modal-field">
        <text class="profile-field-label tm-text-secondary">电话</text>
        <view v-if="displayPhone" class="nickname-box tm-text-primary">{{ displayPhone }}</view>
        <button
          v-else
          class="cu-btn round block tm-primary-btn profile-modal-btn"
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
        class="cu-btn round block tm-primary-btn profile-modal-btn profile-modal-confirm"
        type="button"
        hover-class="button-hover"
        @tap="onConfirm"
      >
        确认登录
      </button>
    </view>
  </view>
</template>

<script>
import apiModule from '@/utils/api'
export default {
  name: 'LoginBanner',
  props: {
    isLoggedIn: { type: Boolean, default: false },
    hasPhone: { type: Boolean, default: false },
    profileSkipped: { type: Boolean, default: false },
    /** 为 true 时不展示完善资料弹窗（如「我的」页由用户自行管理资料） */
    suppressProfileModal: { type: Boolean, default: false },
    displayNickname: { type: String, default: '' },
    displayAvatar: { type: String, default: '' },
    displayPhone: { type: String, default: '' }
  },
  data() {
    return {
      loginLoading: false,
      phoneLoading: false,
      profileLoading: false,
      localAvatar: '',
      localNickname: ''
    }
  },
  watch: {
    displayAvatar: { handler(v) { this.localAvatar = (v || '').trim() }, immediate: true },
    displayNickname: { handler(v) { this.localNickname = (v || '').trim() }, immediate: true }
  },
  computed: {
    showProfileModal() {
      return this.isLoggedIn && !this.hasPhone && !this.profileSkipped && !this.suppressProfileModal
    }
  },
  methods: {
    onWxLogin() {
      const app = getApp()
      const u = typeof uni !== 'undefined' ? uni : wx
      if (!app || typeof app.doLogin !== 'function') {
        u.showToast({ title: '登录功能暂不可用', icon: 'none' })
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
    },
    onChooseAvatar(e) {
      const u = typeof uni !== 'undefined' ? uni : wx
      const avatarUrl = (e.detail && e.detail.avatarUrl) || ''
      if (!avatarUrl) return
      this.localAvatar = avatarUrl
      this.profileLoading = true
      const app = getApp()
      const api = app.api || app.globalData.__api
      const saveAvatar = (url) => {
        if (app.globalData.wxUser) {
          app.globalData.wxUser.headimgUrl = url
          app.globalData.wxUser.avatarUrl = url
        }
        if (api && typeof api.wxUserSave === 'function') {
          api.wxUserSave({ avatarUrl: url, nickName: this.localNickname || undefined }).then(() => {
            u.showToast({ title: '头像已更新', icon: 'success' })
            this.$emit('login-success')
          }).catch(() => this.$emit('login-success'))
        } else this.$emit('login-success')
        this.profileLoading = false
      }
      if (api && typeof api.uploadFile === 'function') {
        api.uploadFile(avatarUrl).then((url) => {
          saveAvatar(url || avatarUrl)
        }).catch(() => {
          saveAvatar(avatarUrl)
          this.profileLoading = false
        })
      } else {
        saveAvatar(avatarUrl)
      }
    },
    onNicknameInput(e) {
      this.localNickname = (e.detail && e.detail.value) || ''
    },
    onNicknameBlur() {
      const nickName = (this.localNickname || '').trim()
      if (!nickName) return
      const app = getApp()
      const u = typeof uni !== 'undefined' ? uni : wx
      if (app.globalData.wxUser) {
        app.globalData.wxUser.nickName = nickName
        app.globalData.wxUser.nickname = nickName
      }
      const api = app.api || app.globalData.__api
      if (api && typeof api.wxUserSave === 'function') {
        api.wxUserSave({ nickName, avatarUrl: this.localAvatar || undefined }).then(() => {
          u.showToast({ title: '昵称已更新', icon: 'success' })
          this.$emit('login-success')
        }).catch(() => this.$emit('login-success'))
      } else this.$emit('login-success')
    },
    onConfirm() {
      this.$emit('confirm')
    },
    onGetPhoneNumber(e) {
      const app = getApp()
      const u = typeof uni !== 'undefined' ? uni : wx
      if (e.detail.errMsg !== 'getPhoneNumber:ok') {
        if (e.detail.errMsg && e.detail.errMsg.indexOf('cancel') !== -1) return
        u.showToast({ title: '需要授权手机号以完善资料', icon: 'none' })
        return
      }
      const code = e.detail.code
      if (!code) {
        u.showToast({ title: '获取手机号失败', icon: 'none' })
        return
      }
      this.phoneLoading = true
      const api = (app && app.api) || (app && app.globalData && app.globalData.__api) || apiModule
      if (!api || typeof api.bindPhoneNumber !== 'function') {
        this.phoneLoading = false
        u.showToast({ title: '暂不支持绑定手机号', icon: 'none' })
        return
      }
      api.bindPhoneNumber({ code })
        .then((resp) => {
          const data = (resp && resp.data) || resp
          const phone = data.phoneNumber || data.phone
          if (phone && app.globalData.wxUser) {
            app.globalData.wxUser.phoneNumber = phone
            app.globalData.wxUser.phone = phone
          }
          this.phoneLoading = false
          u.showToast({ title: '手机号已绑定', icon: 'success' })
          this.$emit('login-success')
        })
        .catch(() => {
          this.phoneLoading = false
          u.showToast({ title: '绑定失败', icon: 'none' })
        })
    }
  }
}
</script>

<style lang="scss" scoped>
/* 紧挨 tabbar 上方固定，避免被页面内容遮挡；tabbar 高度约 100rpx */
.login-banner-wrap {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 100rpx;
  z-index: 9998;
  padding: 0 20rpx 16rpx;
  pointer-events: auto;
}
.login-banner {
  padding: 24rpx 32rpx;
  box-shadow: 0 -4rpx 20rpx rgba(0, 0, 0, 0.08);
}
.login-banner-inner {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.login-banner-text {
  font-size: 26rpx;
}

/* 完善个人资料：居中弹窗，沿用 theme 的 tm-card / tm-primary 风格 */
.profile-modal-mask {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 10000;
  background: rgba(17, 24, 39, 0.35);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 60rpx 40rpx;
  box-sizing: border-box;
}
.profile-modal-card {
  width: 100%;
  max-width: 680rpx;
  padding: 40rpx 36rpx;
  border-radius: 20rpx;
  box-shadow: 0 8rpx 32rpx rgba(17, 24, 39, 0.08);
}
.profile-modal-title {
  font-size: 34rpx;
  text-align: center;
  margin-bottom: 36rpx;
  color: #111827;
}
.profile-modal-field {
  margin-bottom: 24rpx;
}
.profile-field-label {
  font-size: 26rpx;
  display: block;
  margin-bottom: 12rpx;
}
.avatar-btn {
  width: 160rpx;
  height: 160rpx;
  margin: 0 auto;
  padding: 0;
  border: 2rpx dashed #e5e7eb;
  border-radius: 50%;
  background: #f7f8fa;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  line-height: normal;
}
.avatar-btn::after {
  border: none;
}
.avatar-img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
}
.avatar-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
}
.avatar-hint { font-size: 26rpx; }
.nickname-input {
  height: 76rpx;
  padding: 0 24rpx;
  background: #f7f8fa;
  border-radius: 12rpx;
  font-size: 28rpx;
  width: 100%;
  box-sizing: border-box;
}
.nickname-box {
  height: 76rpx;
  line-height: 76rpx;
  padding: 0 24rpx;
  background: #f7f8fa;
  border-radius: 12rpx;
  font-size: 28rpx;
}
.profile-modal-btn {
  width: 100%;
  height: 84rpx;
  line-height: 84rpx;
  font-size: 30rpx;
  border-radius: 42rpx;
}
.profile-modal-confirm {
  margin-top: 16rpx;
}
</style>
