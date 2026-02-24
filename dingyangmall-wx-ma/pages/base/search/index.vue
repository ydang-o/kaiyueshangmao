<!--
  Copyright (C) 2018-2019 www.dingyangmall.com
-->
<template>
  <view class="page">
    <view class="cu-bar search bg-white">
      <view class="search-form round">
        <text class="cuIcon-search"></text>
        <input type="text" placeholder="请输入商品名" confirm-type="search" v-model="keyword" @confirm="searchHandle" focus />
      </view>
    </view>
    <view v-if="searchHistory.length > 0" class="bg-white">
      <view class="cu-bar bg-white">
        <view class="action"><text class="cuIcon-time"></text>历史搜索</view>
        <view class="action" @tap="clearSearchHistory"><text class="cuIcon-delete lg text-gray"></text></view>
      </view>
      <view class="padding-sm flex flex-wrap bg-white">
        <view class="padding-xs" v-for="(item, i) in searchHistory" :key="i">
          <view class="cu-tag round" @tap="searchHandle(item.name)">{{ item.name }}</view>
        </view>
      </view>
    </view>
    <view v-if="goodsList.length" class="bg-white">
      <view class="cu-bar bg-white"><view class="action"><text class="cuIcon-hot text-orange"></text>全网热榜</view></view>
      <view class="cu-list menu card-menu sm-border margin-top-sm">
        <view class="cu-item" v-for="(item, i) in goodsList" :key="i">
          <navigator hover-class="none" :url="'/pages/goods/goods-detail/index?id=' + item.id" class="content overflow-1">{{ i + 1 }} {{ item.name }}</navigator>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  name: 'SearchPage',
  data() {
    return { searchHistory: [], goodsList: [], keyword: '' }
  },
  onShow() {
    try {
      this.searchHistory = uni.getStorageSync('searchHistory') || []
    } catch (e) { this.searchHistory = [] }
  },
  onLoad() {
    getApp().initPage().then(() => this.goodsPage())
  },
  methods: {
    searchHandle(e) {
      let value = ''
      if (typeof e === 'string') value = e
      else if (e.detail && e.detail.value) value = e.detail.value
      if (!value) return
      let searchHistory = [...(this.searchHistory || [])]
      searchHistory = searchHistory.filter(item => item.name !== value).slice(0, 9)
      searchHistory.unshift({ name: value })
      uni.setStorageSync('searchHistory', searchHistory)
      this.searchHistory = searchHistory
      uni.navigateTo({ url: '/pages/goods/goods-list/index?name=' + encodeURIComponent(value) })
    },
    clearSearchHistory() {
      uni.showModal({ content: '确认删除全部历史记录？', cancelText: '我再想想', confirmColor: '#ff0000', success: (res) => {
        if (res.confirm) { this.searchHistory = []; uni.setStorageSync('searchHistory', []) }
      }})
    },
    goodsPage() {
      getApp().api.goodsPage({ searchCount: false, current: 1, size: 10, ascs: '', descs: 'sale_num' }).then(res => {
        this.goodsList = (res.data && res.data.records) || []
      })
    }
  }
}
</script>

<style scoped>
.page { min-height: 100vh; background: #f3f4f7; }
</style>
