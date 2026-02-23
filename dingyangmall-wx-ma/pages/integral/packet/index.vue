<!--
  Copyright (C) 2018-2019 www.dingyangmall.com
-->
<template>
  <view class="padding">
    <view class="bg-white padding radius shadow-lg">
      <view class="text-center margin-bottom-lg">
        <view class="text-xxl text-bold text-red">积分红包</view>
        <view class="text-gray margin-top-sm">赠送积分给好友</view>
        <view class="margin-top-sm text-lg">当前可用积分：<text class="text-orange text-bold">{{ userInfo.points || 0 }}</text></view>
      </view>
      <view>
        <view class="cu-form-group margin-top"><view class="title">接收方手机</view><input placeholder="请输入接收方手机号" type="number" maxlength="11" v-model="phone" /></view>
        <view class="cu-form-group"><view class="title">赠送数量</view><input placeholder="请输入积分数量" type="number" v-model="amount" /></view>
        <view class="cu-form-group">
          <view class="title">验证码</view>
          <input placeholder="短信验证码" type="number" maxlength="6" v-model="code" />
          <button class="cu-btn bg-green shadow" @tap="sendSms" :disabled="smsDisabled">{{ smsText }}</button>
        </view>
        <view class="padding flex flex-direction margin-top-xl">
          <button class="cu-btn bg-red margin-tb-sm lg" @tap="sendPacket" :loading="loading">确认发送</button>
        </view>
      </view>
      <view class="text-gray text-sm padding-lr margin-top">
        <view>说明：</view>
        <view>1. 接收方必须是平台注册会员。</view>
        <view>2. 发送前请确认您的积分余额充足。</view>
        <view>3. 验证码将发送至您当前绑定的手机号。</view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  name: 'IntegralPacketPage',
  data() {
    return {
      phone: '',
      amount: '',
      code: '',
      loading: false,
      smsText: '获取验证码',
      smsDisabled: false,
      timer: null,
      userInfo: {}
    }
  },
  onLoad() {
    getApp().initPage().then(() => this.getUserInfo())
  },
  beforeDestroy() {
    if (this.timer) clearInterval(this.timer)
  },
  methods: {
    getUserInfo() {
      getApp().api.memberInfo().then(res => { this.userInfo = res.data || {} })
    },
    sendSms() {
      if (this.smsDisabled) return
      if (!this.userInfo.phone) { uni.showToast({ title: '无法获取您的手机号', icon: 'none' }); return }
      getApp().api.sendSmsCode(this.userInfo.phone).then(() => {
        uni.showToast({ title: '验证码已发送', icon: 'none' })
        this.startTimer()
      })
    },
    startTimer() {
      let time = 60
      this.smsDisabled = true
      this.smsText = time + 's'
      this.timer = setInterval(() => {
        time--
        if (time <= 0) { clearInterval(this.timer); this.smsDisabled = false; this.smsText = '获取验证码' }
        else this.smsText = time + 's'
      }, 1000)
    },
    sendPacket() {
      if (!this.phone || this.phone.length !== 11) { uni.showToast({ title: '请输入正确的接收方手机号', icon: 'none' }); return }
      if (!this.amount || Number(this.amount) <= 0) { uni.showToast({ title: '请输入正确的积分数量', icon: 'none' }); return }
      if (!this.code) { uni.showToast({ title: '请输入验证码', icon: 'none' }); return }
      if (Number(this.amount) > (this.userInfo.points || 0)) { uni.showToast({ title: '积分余额不足', icon: 'none' }); return }
      this.loading = true
      getApp().api.sendPacket({ phone: this.phone, amount: Number(this.amount), code: this.code }).then(res => {
        this.loading = false
        if (res.code === 0) {
          uni.showModal({ title: '发送成功', content: res.msg || '红包已发送', showCancel: false, success: () => uni.navigateBack() })
        } else uni.showToast({ title: res.msg, icon: 'none' })
      }).catch(() => { this.loading = false })
    }
  }
}
</script>

<style scoped>
.padding { padding: 30rpx; min-height: 100vh; background: #f3f4f7; }
</style>
