<!--
  Copyright (C) 2018-2019 www.dingyangmall.com
-->
<template>
  <view class="page tm-page margin-bottom-bar" v-if="orderInfo">
    <view class="bg-white padding">
      <view class="status-desc text-red margin-left text-bold text-center" :class="'cuIcon-' + statusIcon">{{ orderInfo.statusDesc }}</view>
      <view class="text-black margin-left text-center" v-if="orderInfo.isPay == '0' && !orderInfo.status">
        请在<count-down :outTime="1000 * orderInfo.outTime" @countDownDone="countDownDone" />内付款，超时订单将自动取消
      </view>
      <view class="text-black margin-left text-center" v-if="orderInfo.status == '2'">
        还剩<count-down :outTime="1000 * orderInfo.outTime" @countDownDone="countDownDone" />自动确认
      </view>
    </view>
    <view class="cu-list cu-card menu-avatar">
      <navigator v-if="orderInfo.deliveryWay == '1' && ['2','3','4'].indexOf(orderInfo.status) >= 0 && orderInfo.orderLogistics" class="cu-item" :url="'/pages/order/order-logistics/index?id=' + orderInfo.orderLogistics.id">
        <view class="cu-avatar round cuIcon-deliver bg-red"></view>
        <view class="content loc-content">
          <view class="flex"><view class="text-black">{{ orderInfo.orderLogistics.statusDesc }}</view><view class="text-sm margin-left-sm">{{ orderInfo.orderLogistics.logisticsDesc }}</view></view>
          <view class="text-black text-sm overflow-2 loc-info" v-if="orderInfo.orderLogistics.message">{{ orderInfo.orderLogistics.message }}</view>
        </view>
        <view class="action"><text class="cuIcon-right"></text></view>
      </navigator>
      <view class="cu-item" v-if="orderInfo.orderLogistics">
        <view class="cu-avatar round cuIcon-location bg-black"></view>
        <view class="content loc-content">
          <view class="flex"><view class="text-black">{{ orderInfo.orderLogistics.userName }}</view><view class="text-gray text-sm margin-left-sm">{{ orderInfo.orderLogistics.telNum }}</view></view>
          <view class="text-gray text-sm overflow-2 loc-info">{{ orderInfo.orderLogistics.address }}</view>
        </view>
      </view>
    </view>
    <view class="cu-card article mar-top-30">
      <view class="cu-item">
        <view class="cu-list menu">
          <view v-for="(item, i) in (orderInfo.listOrderItem || [])" :key="i">
            <navigator hover-class="none" :url="'/pages/goods/goods-detail/index?id=' + item.spuId" class="cu-item">
              <view class="flex align-center">
                <image :src="$imgUrl(item.picUrl) || '/static/img/no_pic.png'" mode="aspectFill" class="row-img margin-top-xs" />
                <view class="desc row-info">
                  <view class="text-black margin-left margin-top-sm overflow-2">{{ item.spuName }}</view>
                  <view class="text-gray text-sm margin-top-sm overflow-2" v-if="item.specInfo">{{ item.specInfo }}</view>
                  <view class="flex justify-between"><view class="text-bold text-gray margin-top-sm margin-left-xs padding-lr-sm">数量</view><view class="text-black text-sm margin-top-sm margin-right-xs">x{{ item.quantity }}</view></view>
                  <view class="flex justify-between"><view class="text-bold text-gray margin-top-sm margin-left-xs padding-lr-sm">是否退款</view><view class="text-black text-sm margin-top-sm margin-right-xs">{{ item.isRefund == '0' ? '否' : '是' }}</view></view>
                  <view class="flex justify-between"><view class="text-bold text-gray margin-top-sm margin-left-xs padding-lr-sm">状态</view><view class="text-black text-sm margin-top-sm margin-right-xs">{{ item.status == '0' ? '正常' : item.status == '1' ? '申请退款中' : item.status == '2' ? '拒绝退款' : item.status == '3' ? '同意退款' : '' }}</view></view>
                </view>
              </view>
            </navigator>
            <view class="cu-item margin" v-if="orderInfo.isPay == '1' && item.isRefund == '0' && (item.status == '0' || item.status == '2')">
              <button class="cu-btn sm" @tap="refunds(item.id)">申请退款</button>
            </view>
          </view>
          <view class="cu-item margin-top-xs"><text class="text-gray font-weight">订单金额</text><view class="action"><text class="text-price">{{ orderInfo.salesPrice }}</text></view></view>
          <view class="cu-item margin-top-xs"><text class="text-gray font-weight">运费金额</text><view class="action">+ <text class="text-price">{{ orderInfo.freightPrice }}</text></view></view>
          <view class="cu-item margin-top-xs"><text class="text-gray font-weight">支付金额</text><view class="action"><text class="text-price text-xl tm-brand text-bold">{{ orderInfo.paymentPrice }}</text></view></view>
        </view>
      </view>
    </view>
    <view class="cu-card mar-top-30">
      <view class="cu-item">
        <view class="cu-bar bg-white"><view class="action"><text class="cuIcon-titles text-black"></text>订单信息</view></view>
        <view class="margin flex"><text class="flex-sub text-gray font-weight">订单编号</text><view class="action">{{ orderInfo.orderNo }} <button class="cu-btn sm" @tap="copyData(orderInfo.orderNo)">复制</button></view></view>
        <view class="margin flex"><text class="flex-sub text-gray font-weight">创建时间</text><view class="action">{{ orderInfo.createTime }}</view></view>
        <view class="margin flex" v-if="orderInfo.paymentTime"><text class="flex-sub text-gray font-weight">付款时间</text><view class="action">{{ orderInfo.paymentTime }}</view></view>
      </view>
    </view>
    <view class="cu-card mar-top-30" v-if="orderInfo.orderLogistics && orderInfo.orderLogistics.logisticsDesc">
      <view class="cu-item">
        <view class="cu-bar bg-white"><view class="action"><text class="cuIcon-titles text-orange"></text> 物流信息</view></view>
        <view class="margin flex"><text class="flex-sub">快递公司</text><view class="flex-twice">{{ orderInfo.orderLogistics.logisticsDesc }}</view></view>
        <view class="margin flex"><text class="flex-sub">快递单号</text><view class="flex-twice">{{ orderInfo.orderLogistics.logisticsNo }} <button class="cu-btn sm" @tap="copyData(orderInfo.orderLogistics.logisticsNo)">复制</button></view></view>
      </view>
    </view>
    <view class="cu-card mar-top-30" v-if="orderInfo.userMessage">
      <view class="cu-item cu-form-group align-start"><view class="title">给卖家留言</view><textarea readonly :value="orderInfo.userMessage"></textarea></view>
    </view>
    <view style="padding-bottom: 60px;"></view>
    <view class="cu-bar tabbar bg-white border foot">
      <order-operate class="response" :orderInfo="orderInfo" :callPay="callPay" contact @orderCancel="orderCancel" @orderReceive="orderCancel" @orderDel="orderDel" @unifiedOrder="unifiedOrder" />
    </view>
  </view>
</template>

<script>
import CountDown from '@/components/count-down/index.vue'
import OrderOperate from '@/components/order-operate/index.vue'
export default {
  name: 'OrderDetailPage',
  components: { CountDown, OrderOperate },
  data() {
    return { orderInfo: null, id: '', callPay: false }
  },
  computed: {
    statusIcon() {
      const s = this.orderInfo && this.orderInfo.status
      const map = { '0': 'pay', '1': 'send', '2': 'deliver', '3': 'evaluate', '4': 'upstage', '5': 'roundclose' }
      return map[s] || ''
    }
  },
  onShow() {
    getApp().initPage().then(() => this.id && this.orderGet(this.id))
  },
  onLoad(options) {
    this.id = options.id || ''
    if (options.callPay) this.callPay = true
  },
  methods: {
    orderGet(id) {
      getApp().api.orderGet(id).then(res => {
        if (!res.data) { uni.redirectTo({ url: '/pages/order/order-list/index' }); return }
        this.orderInfo = res.data
        setTimeout(() => { this.callPay = false }, 4000)
      })
    },
    copyData(data) { uni.setClipboardData({ data }) },
    refunds(orderItemId) {
      uni.showModal({ content: '确认申请退款吗？', cancelText: '我再想想', confirmColor: '#ff0000', success: (res) => {
        if (res.confirm) getApp().api.orderRefunds({ id: orderItemId }).then(() => this.orderGet(this.id))
      }})
    },
    orderCancel() { this.orderGet(this.id) },
    orderDel() { uni.navigateBack() },
    unifiedOrder() { this.onShow() },
    countDownDone() { this.orderGet(this.id) }
  }
}
</script>

<style scoped>
.margin-bottom-bar { margin-bottom: 120rpx; }
.mar-top-30 { margin-top: 30rpx; }
.row-img { width: 200rpx; height: 200rpx; border-radius: 14rpx; }
.row-info { flex: 1; margin-left: 20rpx; }
.loc-info { min-height: 40rpx; }
</style>
