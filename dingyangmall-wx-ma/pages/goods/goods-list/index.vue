<!--
  Copyright (C) 2018-2019 www.dingyangmall.com
-->
<template>
  <view class="page tm-page">
    <view class="cu-bar search bg-white fixed tm-top-nav">
      <view class="search-form round tm-search-bar">
        <text class="cuIcon-search"></text>
        <navigator class="response" hover-class="none" url="/pages/base/search/index">
          <input type="text" placeholder="请输入商品名" :value="parameter.name" disabled />
        </navigator>
      </view>
      <view class="action">
        <view class="text-xxl" @tap="viewTypeEdit">
          <text :class="viewType ? 'cuIcon-list' : 'cuIcon-cascades'" class="text-black"></text>
        </view>
      </view>
    </view>
    <view class="cu-bar justify-center bg-white fixed bar-fix tm-top-nav">
      <view class="grid response text-center align-start">
        <view class="flex-sub padding-sm margin-xs radius tm-tab-active">{{ title }}</view>
        <view class="flex-sub padding-sm margin-xs radius" @tap="sortHandle('price')">
          价格
          <view class="margin-left-xs">
            <view class="cuIcon-triangleupfill" :class="{ 'text-blue': price === 'asc' }"></view>
            <view class="cuIcon-triangledownfill" :class="{ 'text-blue': price === 'desc' }"></view>
          </view>
        </view>
        <view class="flex-sub padding-sm margin-xs radius" @tap="sortHandle('sales')">
          销量
          <view class="margin-left-xs">
            <view class="cuIcon-triangleupfill" :class="{ 'text-blue': sales === 'asc' }"></view>
            <view class="cuIcon-triangledownfill" :class="{ 'text-blue': sales === 'desc' }"></view>
          </view>
        </view>
        <view class="flex-sub padding-sm margin-xs radius" :class="{ 'text-blue text-bold': createTime === 'desc' }" @tap="sortHandle('createTime')">新上架</view>
      </view>
    </view>
    <view class="list-wrap">
      <view v-if="viewType"><goods-card-index :goodsList="goodsList" /></view>
      <view v-else><goods-row :goodsList="goodsList" /></view>
      <view class="cu-load bg-gray" :class="loadmore ? 'loading' : 'over'"></view>
    </view>
  </view>
</template>

<script>
import util from '@/utils/util'
import apiModule from '@/utils/api'
import GoodsCardIndex from '@/components/goods-card-index/index.vue'
import GoodsRow from '@/components/goods-row/index.vue'
export default {
  name: 'GoodsListPage',
  components: { GoodsCardIndex, GoodsRow },
  data() {
    return {
      page: { searchCount: false, current: 1, size: 10, ascs: '', descs: '' },
      parameter: {},
      loadmore: true,
      goodsList: [],
      viewType: true,
      price: '',
      sales: '',
      createTime: '',
      title: '默认'
    }
  },
  onLoad(options) {
    this.title = options.title ? decodeURIComponent(options.title) : '默认'
    if (options.categorySecond) this.parameter.categorySecond = options.categorySecond
    if (options.name) this.parameter.name = options.name
    if (options.type === '1') { this.title = '新品首发'; this.page.descs = 'create_time' }
    if (options.type === '2') { this.title = '热销单品'; this.page.descs = 'sale_num' }
    if (options.couponUserId) this.parameter.couponUserId = options.couponUserId
    getApp().initPage().then(() => this.goodsPage())
  },
  onReachBottom() {
    if (this.loadmore) { this.page.current++; this.goodsPage() }
  },
  methods: {
    goodsPage() {
      const app = getApp()
      const api = (app && app.api) || (app && app.globalData && app.globalData.__api) || apiModule
      if (!api || typeof api.goodsPage !== 'function') return
      api.goodsPage(Object.assign({}, this.page, util.filterForm(this.parameter))).then(res => {
        const list = (res.data && res.data.records) || []
        this.goodsList = [...this.goodsList, ...list]
        if (list.length < this.page.size) this.loadmore = false
      })
    },
    viewTypeEdit() { this.viewType = !this.viewType },
    sortHandle(type) {
      if (type === 'price') {
        if (this.price === '') { this.price = 'asc'; this.page.descs = ''; this.page.ascs = 'sales_price' }
        else if (this.price === 'asc') { this.price = 'desc'; this.page.descs = 'sales_price'; this.page.ascs = '' }
        else { this.price = ''; this.page.ascs = ''; this.page.descs = '' }
        this.sales = ''; this.createTime = ''
      } else if (type === 'sales') {
        if (this.sales === '') { this.sales = 'desc'; this.page.descs = 'sale_num'; this.page.ascs = '' }
        else if (this.sales === 'desc') { this.sales = 'asc'; this.page.descs = ''; this.page.ascs = 'sale_num' }
        else { this.sales = ''; this.page.ascs = ''; this.page.descs = '' }
        this.price = ''; this.createTime = ''
      } else {
        if (this.createTime === '') { this.createTime = 'desc'; this.page.descs = 'create_time'; this.page.ascs = '' }
        else { this.createTime = ''; this.page.ascs = ''; this.page.descs = '' }
        this.price = ''; this.sales = ''
      }
      this.relod()
    },
    relod() {
      this.loadmore = true
      this.goodsList = []
      this.page.current = 1
      this.goodsPage()
    }
  }
}
</script>

<style scoped>
.page { padding-bottom: 40rpx; }
.bar-fix { margin-top: 80rpx; box-shadow: none; }
.list-wrap { margin-top: 208rpx; }
.search-form { border-radius: 999rpx; }
.cuIcon-triangleupfill.text-blue,
.cuIcon-triangledownfill.text-blue { color: #ff0036 !important; }
</style>
