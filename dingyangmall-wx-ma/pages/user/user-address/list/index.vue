<!--
  Copyright (C) 2018-2019 www.dingyangmall.com
-->
<template>
  <view class="page tm-page">
    <view class="cu-list menu-avatar">
      <view class="cu-item solid-top" v-for="(item, index) in userAddress" :key="index">
        <view class="cu-avatar round bg-red"><text class="avatar-text">{{ item.userName }}</text></view>
        <view class="content loc-content" @tap="selectUserAddress(index)">
          <view class="flex"><view class="text-black">{{ item.userName }}</view><view class="text-gray text-sm margin-left-sm">{{ item.telNum }}</view></view>
          <view class="text-black text-sm overflow-2 loc-info">
            <view class="cu-tag bg-orange sm margin-left-sm" v-if="item.isDefault == '1'">默认</view>
            {{ item.provinceName }}{{ item.cityName }}{{ item.countyName }}{{ item.detailInfo }}
          </view>
        </view>
        <view class="action" @tap="toEdit(index)"><text class="cuIcon-edit"></text></view>
      </view>
    </view>
    <view class="cu-load bg-gray" :class="loadmore ? 'loading' : ''"></view>
    <view class="cu-load bg-gray margin-top-xl" v-if="userAddress.length <= 0 && !loadmore"><text class="text-gray">暂无收货地址，请添加</text></view>
    <button class="cu-btn block shadow-blur margin-sm tm-primary-btn add-btn" v-if="userAddress.length < 10" @tap="toAdd"><text class="text-white">添加新地址</text></button>
  </view>
</template>

<script>
export default {
  name: 'UserAddressListPage',
  data() {
    return { page: { searchCount: false, current: 1, size: 10, ascs: '', descs: '' }, loadmore: true, userAddress: [], select: false }
  },
  onLoad(options) {
    if (options.select) this.select = true
  },
  onShow() {
    getApp().initPage().then(() => this.userAddressPage())
  },
  methods: {
    userAddressPage() {
      getApp().api.userAddressPage(this.page).then(res => {
        this.userAddress = (res.data && res.data.records) || []
        this.loadmore = false
      })
    },
    toAdd() {
      uni.setStorage({ key: 'param-userAddressForm', data: {} })
      uni.navigateTo({ url: '/pages/user/user-address/form/index' })
    },
    toEdit(index) {
      uni.setStorage({ key: 'param-userAddressForm', data: this.userAddress[index] })
      uni.navigateTo({ url: '/pages/user/user-address/form/index' })
    },
    selectUserAddress(index) {
      if (!this.select) return
      const pages = getCurrentPages()
      const prevPage = pages[pages.length - 2]
      if (prevPage && prevPage.$vm) {
        prevPage.$vm.userAddress = this.userAddress[index]
      }
      uni.navigateBack()
    }
  }
}
</script>

<style scoped>
.avatar-text { font-size: 28rpx; color: #fff; }
.loc-info { min-height: 40rpx; }
.add-btn { height: 88rpx; margin-top: 200rpx; }
</style>
