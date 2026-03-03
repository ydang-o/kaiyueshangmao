<!--
  Copyright (C) 2018-2019 www.dingyangmall.com
-->
<template>
  <view class="page tm-page">
    <scroll-view scroll-x class="bg-white nav fixed">
      <view class="flex text-center">
        <view v-for="(item, index) in orderStatus" :key="index" class="cu-item flex-sub" :class="{ 'tm-tab-active cur': index === tabCur }" @tap="tabSelect(index, item.key)">{{ item.value }}</view>
      </view>
    </scroll-view>
    <view class="margin-top-bar">
      <view class="cu-card article">
        <view class="cu-item" v-for="(item, index) in orderList" :key="index">
          <navigator hover-class="none" :url="'/pages/order/order-detail/index?id=' + item.id">
            <view class="cu-bar bg-white">
              <view class="action"><text class="cuIcon-titles text-black"></text> {{ item.createTime }}</view>
              <view class="action text-red">{{ item.statusDesc }}</view>
            </view>
            <view class="cu-item padding-bottom" v-for="(item2, index2) in (item.listOrderItem || [])" :key="index2">
              <view class="content">
                <image :src="$imgUrl(item2.picUrl) || '/static/img/no_pic.png'" mode="aspectFill" class="row-img margin-top-xs" />
                <view class="desc row-info margin-top-sm">
                  <view class="text-black margin-top-sm overflow-2">{{ item2.spuName }}</view>
                  <view class="text-gray text-sm margin-top-sm overflow-2" v-if="item2.specInfo">{{ item2.specInfo }}</view>
                  <view class="flex justify-between">
                    <view class="text-price text-bold text-xl tm-brand margin-top-sm">{{ item2.paymentPrice }}</view>
                    <view class="text-black text-sm margin-top-sm padding-lr-sm">x{{ item2.quantity }}</view>
                  </view>
                </view>
              </view>
              <view class="cu-item text-right padding-sm" v-if="item2.status != '0' && item2.statusDesc">
                <navigator class="cu-btn line sm text-orange" :url="'/pages/order/order-refunds/form/index?orderItemId=' + item2.id">{{ item2.statusDesc }}</navigator>
              </view>
            </view>
          </navigator>
          <order-operate class="response" :orderInfo="item" @orderCancel="onOrderCancel(index)" @orderReceive="onOrderReceive(index)" @orderDel="onOrderDel(index)" @unifiedOrder="onUnifiedOrder(index)" />
        </view>
      </view>
      <view class="cu-load bg-gray" :class="loadmore ? 'loading' : 'over'"></view>
    </view>
  </view>
</template>

<script>
import util from '@/utils/util'
import OrderOperate from '@/components/order-operate/index.vue'
export default {
  name: 'OrderListPage',
  components: { OrderOperate },
  data() {
    return {
      tabCur: 0,
      orderStatus: [
        { value: '全部订单', key: '' },
        { value: '待付款', key: '0' },
        { value: '待发货', key: '1' },
        { value: '待收货', key: '2' },
        { value: '已完成', key: '4' }
      ],
      page: { searchCount: false, current: 1, size: 10, ascs: '', descs: 'create_time' },
      parameter: {},
      loadmore: true,
      orderList: []
    }
  },
  onLoad(options) {
    if (options.status) {
      this.parameter.status = options.status
      const i = this.orderStatus.findIndex(s => s.key === options.status)
      if (i >= 0) this.tabCur = i
    }
    getApp().initPage().then(() => this.orderPage())
  },
  onReachBottom() {
    if (this.loadmore) { this.page.current++; this.orderPage() }
  },
  onPullDownRefresh() {
    uni.showNavigationBarLoading()
    this.refresh()
    uni.hideNavigationBarLoading()
    uni.stopPullDownRefresh()
  },
  methods: {
    tabSelect(index, key) {
      if (index === this.tabCur) return
      this.tabCur = index
      this.parameter.status = key
      this.refresh()
    },
    refresh() {
      this.loadmore = true
      this.orderList = []
      this.page.current = 1
      this.orderPage()
    },
    orderPage() {
      getApp().api.orderPage(Object.assign({}, this.page, util.filterForm(this.parameter))).then(res => {
        const list = (res.data && res.data.records) || []
        this.orderList = [...this.orderList, ...list]
        if (list.length < this.page.size) this.loadmore = false
      })
    },
    onOrderCancel(index) {
      getApp().api.orderGet(this.orderList[index].id).then(res => {
        this.orderList[index] = res.data
        this.orderList = [...this.orderList]
      })
    },
    onOrderReceive(index) {
      this.onOrderCancel(index)
    },
    onOrderDel(index) {
      this.orderList.splice(index, 1)
      this.orderList = [...this.orderList]
    },
    onUnifiedOrder(index) {
      this.onOrderCancel(index)
    }
  }
}
</script>

<style scoped>
.margin-top-bar { margin-top: 80rpx; }
.row-img { width: 200rpx; height: 200rpx; border-radius: 14rpx; }
.row-info { flex: 1; margin-left: 20rpx; }
</style>
