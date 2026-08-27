<!--
  Copyright (C) 2018-2019 www.dingyangmall.com
-->
<template>
  <view class="page tm-page">
    <view class="cu-list menu card-menu margin-top">
      <view class="cu-item"><view class="content"><text class="text-grey">物流公司</text></view><view class="action"><text class="text-black">{{ orderLogistics.logisticsDesc }}</text></view></view>
      <view class="cu-item"><view class="content"><text class="text-grey">快递单号</text></view><view class="action"><text class="text-black margin-right-sm">{{ orderLogistics.logisticsNo }}</text><button class="cu-btn sm tm-outline-btn" @tap="copyNo">复制</button></view></view>
      <view class="cu-item"><view class="content"><text class="text-grey">物流状态</text></view><view class="action"><text class="text-blue">{{ orderLogistics.statusDesc }}</text></view></view>
    </view>
    <view class="cu-list menu card-menu margin-top">
      <view class="cu-item"><view class="content"><text class="text-grey">收货人</text></view><view class="action"><text class="text-black">{{ orderLogistics.userName }}</text></view></view>
      <view class="cu-item"><view class="content"><text class="text-grey">联系电话</text></view><view class="action"><text class="text-black">{{ orderLogistics.telNum }}</text></view></view>
      <view class="cu-item"><view class="content padding-tb-sm"><view class="text-grey">收货地址</view><view class="text-black text-content text-sm margin-top-sm">{{ orderLogistics.address }}</view></view></view>
    </view>
  </view>
</template>

<script>
import apiModule from '@/utils/api'
export default {
  name: 'OrderLogisticsPage',
  data() { return { orderLogistics: {} } },
  onLoad(options) {
    getApp().initPage().then(() => {
      if (options && options.id) this.getLogistics(options.id)
    })
  },
  methods: {
    getApi() {
      const app = getApp()
      return (app && app.api) || (app && app.globalData && app.globalData.__api) || apiModule
    },
    getLogistics(id) {
      const api = this.getApi()
      if (!api || typeof api.orderLogistics !== 'function') return
      api.orderLogistics(id).then(res => {
        this.orderLogistics = (res && res.data != null) ? res.data : (res || {})
      }).catch(() => {})
    },
    copyNo() {
      uni.setClipboardData({ data: this.orderLogistics.logisticsNo || '', success: () => uni.showToast({ title: '复制成功' }) })
    }
  }
}
</script>

<style scoped>
.page { padding: 20rpx; }
</style>
