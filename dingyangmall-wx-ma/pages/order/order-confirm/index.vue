<!--
  Copyright (C) 2018-2019 www.dingyangmall.com
-->
<template>
  <view class="page margin-bottom-bar">
    <view class="cu-list menu-avatar">
      <navigator v-if="orderSubParm.deliveryWay == '1'" class="cu-item" url="/pages/user/user-address/list/index?select=true">
        <view class="cu-avatar round cuIcon-location bg-black"></view>
        <view class="content loc-content" v-if="userAddress">
          <view class="flex padding-top-sm">
            <view class="cu-tag bg-red radius margin-right-sm" v-if="userAddress.isDefault == '1'">默认</view>
            <view class="text-black">{{ userAddress.userName }}</view>
            <view class="text-gray text-sm margin-left-sm">{{ userAddress.telNum }}</view>
          </view>
          <view class="text-gray text-sm overflow-2 loc-info padding-bottom-sm address">{{ userAddress.provinceName }}{{ userAddress.cityName }}{{ userAddress.countyName }}{{ userAddress.detailInfo }}</view>
        </view>
        <view class="content loc-content" v-else>请选择收货地址</view>
        <view class="action"><text class="cuIcon-right"></text></view>
      </navigator>
    </view>
    <view class="cu-card article">
      <view class="cu-item">
        <view class="cu-list menu">
          <view v-for="(item, i) in orderConfirmData" :key="i" class="flex align-center">
            <image :src="item.picUrl || '/static/img/no_pic.png'" mode="aspectFill" class="row-img margin-top-xs" />
            <view class="row-info margin-left">
              <view class="text-black margin-top-xl overflow-2">{{ item.spuName }}</view>
              <view class="text-gray text-sm margin-top-xs cu-tag round" v-if="item.specInfo">{{ item.specInfo }}</view>
              <view class="flex margin-top-sm">
                <view class="flex-sub"><text class="text-price text-xl text-blue text-bold margin-top-sm">{{ item.salesPrice }}</text></view>
                <view class="flex-twice text-gray text-sm text-right margin-right">x{{ item.quantity }}</view>
              </view>
            </view>
          </view>
          <view class="cu-item margin-top-sm"><text class="text-gray text-sm">订单金额</text><view class="action"><view class="text-price">{{ salesPrice }}</view></view></view>
          <view class="cu-item margin-top-sm"><text class="text-gray text-sm">运费金额</text><view class="action"><view class="text-price">{{ freightPrice }}</view></view></view>
        </view>
      </view>
    </view>
    <view class="cu-card mar-top-30">
      <view class="cu-item cu-form-group align-start" style="height:200rpx;">
        <input style="font-size:24rpx; margin-top:20rpx;" v-model="orderSubParm.userMessage" placeholder="给卖家留言" />
      </view>
    </view>
    <view class="cu-bar tabbar bg-white border foot">
      <view class="flex response">
        <view class="flex-sub"></view>
        <view class="flex-treble bar-rt">
          <text class="text-sm text-gray">共{{ orderConfirmData.length }}件，</text>
          <text class="text-sm text-gray">合计：</text>
          <text class="text-xl text-price text-blue text-bold">{{ paymentTotal }}</text>
          <button class="cu-btn shadow-blur margin-left-sm submit-btn" @tap="orderSub" :loading="loading" :disabled="loading"><text class="text-white">提交订单</text></button>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import numberUtil from '@/utils/numberUtil'
export default {
  name: 'OrderConfirmPage',
  data() {
    return {
      orderConfirmData: [],
      salesPrice: 0,
      paymentPrice: 0,
      freightPrice: 0,
      userAddress: null,
      orderSubParm: { paymentType: '1', deliveryWay: '1', userMessage: '' },
      loading: false
    }
  },
  computed: {
    paymentTotal() {
      return numberUtil.numberAddition(this.paymentPrice || 0, this.freightPrice || 0)
    }
  },
  onLoad() {
    this.userAddressPage()
    this.orderConfirmDo()
  },
  methods: {
    orderConfirmDo() {
      uni.getStorage({
        key: 'param-orderConfirm',
        success: (res) => {
          const orderConfirmData = res.data || []
          let salesPrice = 0, freightPrice = 0
          orderConfirmData.forEach(item => {
            salesPrice = (Number(salesPrice) + item.salesPrice * item.quantity).toFixed(2)
            item.paymentPrice = (item.salesPrice * item.quantity).toFixed(2)
            item.freightPrice = 0
            freightPrice = (Number(freightPrice) + Number(item.freightPrice || 0)).toFixed(2)
          })
          this.orderConfirmData = orderConfirmData
          this.salesPrice = salesPrice
          this.freightPrice = freightPrice
          this.paymentPrice = salesPrice
        }
      })
    },
    userAddressPage() {
      getApp().api.userAddressPage({ searchCount: false, current: 1, size: 1, isDefault: '1' }).then(res => {
        const records = (res.data && res.data.records) || []
        if (records.length > 0) this.userAddress = records[0]
      })
    },
    orderSub() {
      if (this.orderSubParm.deliveryWay == '1' && !this.userAddress) {
        uni.showToast({ title: '请选择收货地址', icon: 'none' })
        return
      }
      this.loading = true
      const skus = this.orderConfirmData
      const userAddressId = this.orderSubParm.deliveryWay == '1' ? this.userAddress.id : null
      getApp().api.orderSub(Object.assign({}, this.orderSubParm, { skus, userAddressId })).then(res => {
        uni.redirectTo({ url: '/pages/order/order-detail/index?callPay=true&id=' + res.data.id })
      }).catch(() => { this.loading = false })
    }
  }
}
</script>

<style scoped>
.margin-bottom-bar { margin-bottom: 120rpx; }
.bar-rt { text-align: right; }
.submit-btn { background-color: #2967ff; font-weight: 300; width: 220rpx; }
.row-img { width: 200rpx; height: 200rpx; border-radius: 10rpx; }
.row-info { flex: 1; }
.loc-info { min-height: 40rpx; }
</style>
