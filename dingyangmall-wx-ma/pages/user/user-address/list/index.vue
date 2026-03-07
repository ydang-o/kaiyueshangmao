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
import util from '@/utils/util'
import apiModule from '@/utils/api'
export default {
  name: 'UserAddressListPage',
  data() {
    return { page: { searchCount: false, current: 1, size: 10, ascs: '', descs: '' }, loadmore: true, userAddress: [], select: false }
  },
  onLoad(options) {
    if (options.select) this.select = true
  },
  onShow() {
    util.requireLogin('请先登录后管理收货地址').then((ok) => {
      if (!ok) {
        this.loadmore = false
        return
      }
      getApp().initPage().then(() => this.userAddressPage())
    })
  },
  methods: {
    getApi() {
      const app = getApp()
      return (app && app.api) || (app && app.globalData && app.globalData.__api) || apiModule
    },
    /** 从地址分页响应中解析列表 */
    _parseAddressList(res) {
      const data = (res && res.data) || res || {}
      const list = data.records || data.rows || data.list || data.content || data.data || []
      return Array.isArray(list) ? list : []
    },
    userAddressPage() {
      const api = this.getApi()
      if (!api || typeof api.userAddressPage !== 'function') {
        this.loadmore = false
        return
      }
      api.userAddressPage(this.page).then(res => {
        this.userAddress = this._parseAddressList(res)
        this.loadmore = false
      }).catch(() => {
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
