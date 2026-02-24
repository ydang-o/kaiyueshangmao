<!--
  Copyright (C) 2018-2019 www.dingyangmall.com
  申请退款
-->
<template>
  <view class="page padding">
    <view class="bg-white padding radius">
      <view class="cu-form-group">
        <view class="title">订单项</view>
        <view class="text-gray">订单项 ID：{{ orderItemId || '-' }}</view>
      </view>
      <view class="cu-form-group" v-if="orderItemId">
        <view class="title">退款原因（选填）</view>
        <textarea v-model="reason" placeholder="请输入退款原因" maxlength="200" />
      </view>
      <view class="padding flex flex-direction margin-top-xl">
        <button class="cu-btn bg-red lg" @tap="submit" :loading="loading">提交申请</button>
      </view>
      <view class="text-gray text-sm margin-top">提交后将由商家审核，请耐心等待。</view>
    </view>
  </view>
</template>

<script>
export default {
  name: 'OrderRefundsFormPage',
  data() {
    return { orderItemId: '', reason: '', loading: false }
  },
  onLoad(options) {
    this.orderItemId = options.orderItemId || ''
    if (!this.orderItemId) {
      uni.showToast({ title: '参数错误', icon: 'none' })
      setTimeout(() => uni.navigateBack(), 1500)
    }
  },
  methods: {
    submit() {
      if (!this.orderItemId) return
      uni.showModal({
        content: '确认申请退款吗？',
        cancelText: '取消',
        confirmColor: '#ff0000',
        success: (res) => {
          if (res.confirm) {
            this.loading = true
            getApp().api.orderRefunds({ id: this.orderItemId }).then(() => {
              this.loading = false
              uni.showToast({ title: '已提交' })
              setTimeout(() => uni.navigateBack(), 1500)
            }).catch(() => { this.loading = false })
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.page { min-height: 100vh; background: #f3f4f7; }
</style>
