<!--
  Copyright (C) 2018-2019 www.dingyangmall.com
-->
<template>
  <view class="flex justify-end">
    <button v-if="contact" class="cu-btn radius margin-right shadow-blur service" open-type="contact" @contact="handleContact">
      <view class="cuIcon-servicefill text-white"></view>
      <text class="text-white">客服</text>
    </button>
    <button v-if="orderInfo.status == '5' && orderInfo.isPay == '0'" class="cu-btn radius margin-right shadow-blur delete-order" :loading="loading" :disabled="loading" @tap="orderDel"><text class="text-white">删除订单</text></button>
    <button v-if="orderInfo.isPay == '0' && !orderInfo.status" class="cu-btn radius margin-right shadow-blur cancel-order" :loading="loading" :disabled="loading" @tap="orderCancel"><text class="text-white">取消订单</text></button>
    <button v-if="orderInfo.deliveryWay == '1' && ['2','3','4'].indexOf(orderInfo.status) >= 0" class="cu-btn radius margin-right shadow-blur check-logistics" :loading="loading" :disabled="loading" @tap="orderLogistics"><text class="text-white">查看物流</text></button>
    <button v-if="orderInfo.isPay == '0' && !orderInfo.status" class="cu-btn radius margin-right shadow-blur payment" :loading="loading" :disabled="loading" @tap="unifiedOrder"><text class="text-white">付款</text></button>
    <button v-if="orderInfo.status == '2'" class="cu-btn radius margin-right shadow-blur confirm-goods" :loading="loading" :disabled="loading" @tap="orderReceive"><text class="text-white">确认收货</text></button>
    <button v-if="orderInfo.status == '3' && orderInfo.appraisesStatus == '0'" class="cu-btn radius margin-right shadow-blur evaluation" :loading="loading" :disabled="loading" @tap="orderAppraise"><text class="text-white">评价</text></button>
  </view>
</template>

<script>
import apiModule from '@/utils/api'
export default {
  name: 'OrderOperate',
  props: {
    orderInfo: { type: Object, default: () => ({}) },
    callPay: { type: Boolean, default: false },
    contact: { type: Boolean, default: false }
  },
  data() { return { loading: false } },
  mounted() {
    if (this.callPay) setTimeout(() => this.unifiedOrder(), 1000)
  },
  methods: {
    getApi() {
      const app = getApp()
      return (app && app.api) || (app && app.globalData && app.globalData.__api) || apiModule
    },
    handleContact() {},
    orderReceive() {
      const api = this.getApi()
      if (!api || typeof api.orderReceive !== 'function') return
      uni.showModal({
        content: '是否确认收货吗？',
        cancelText: '我再想想',
        confirmColor: '#ff0000',
        success: res => {
          if (res.confirm) api.orderReceive(this.orderInfo.id).then(r => this.$emit('orderReceive', r)).catch(() => {})
        }
      })
    },
    orderLogistics() {
      const id = (this.orderInfo.orderLogistics && this.orderInfo.orderLogistics.id) || this.orderInfo.id
      uni.navigateTo({ url: '/pages/order/order-logistics/index?id=' + id })
    },
    orderCancel() {
      const api = this.getApi()
      if (!api || typeof api.orderCancel !== 'function') return
      uni.showModal({
        content: '确认取消该订单吗？',
        cancelText: '我再想想',
        confirmColor: '#ff0000',
        success: res => {
          if (res.confirm) api.orderCancel(this.orderInfo.id).then(r => this.$emit('orderCancel', r)).catch(() => {})
        }
      })
    },
    orderDel() {
      const api = this.getApi()
      if (!api || typeof api.orderDel !== 'function') return
      uni.showModal({
        content: '确认删除该订单吗？',
        cancelText: '我再想想',
        confirmColor: '#ff0000',
        success: res => {
          if (res.confirm) api.orderDel(this.orderInfo.id).then(r => this.$emit('orderDel', r)).catch(() => {})
        }
      })
    },
    unifiedOrder() {
      const api = this.getApi()
      if (!api || typeof api.unifiedOrder !== 'function') {
        this.loading = false
        return
      }
      this.loading = true
      api.unifiedOrder({ id: this.orderInfo.id }).then(res => {
        this.loading = false
        if (this.orderInfo.paymentPrice <= 0) {
          this.$emit('unifiedOrder', res)
        } else {
          const payData = (res && res.data) || res || {}
          const pkg = payData.packageValue || payData.package
          if (payData.timeStamp && payData.nonceStr && pkg && payData.signType && payData.paySign) {
            uni.requestPayment({
              timeStamp: payData.timeStamp,
              nonceStr: payData.nonceStr,
              package: pkg,
              signType: payData.signType,
              paySign: payData.paySign,
              success: () => this.$emit('unifiedOrder', res),
              fail: () => {},
              complete: () => {}
            })
          } else {
            this.$emit('unifiedOrder', res)
          }
        }
      }).catch(() => { this.loading = false })
    },
    orderAppraise() {
      uni.navigateTo({ url: '/pages/appraises/form/index?orderId=' + this.orderInfo.id })
    }
  }
}
</script>

<style scoped>
.service { background: linear-gradient(135deg, #ff2b56 0%, #ff0036 100%); }
.cancel-order, .delete-order { background-color: #111827; }
.check-logistics { background-color: #ff7a45; }
.payment { background: linear-gradient(135deg, #ff2b56 0%, #ff0036 100%); }
.confirm-goods { background-color: #16a34a; }
.evaluation { background-color: #ff7a45; }
</style>
