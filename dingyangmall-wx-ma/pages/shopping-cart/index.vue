<!--
  Copyright (C) 2018-2019 www.dingyangmall.com
-->
<template>
  <view class="page">
    <view class="cu-bar bg-white fixed" style="min-height: 80rpx;">
      <view class="grid col-3 response text-center text-gray">
        <view><text class="cuIcon-selection margin-right-xs"></text>100%正品保证</view>
        <view><text class="cuIcon-discover margin-right-xs"></text>精挑细选</view>
        <view><text class="cuIcon-squarecheck margin-right-xs"></text>售后无忧</view>
      </view>
    </view>
    <view class="cu-bar bg-white solid-bottom solid-top fixed bar-top">
      <view class="action">共{{ shoppingCartData.length }}件宝贝</view>
      <view class="action"><button class="cu-btn line-gray sm" @tap="operation">{{ operation ? '管理' : '完成' }}</button></view>
    </view>
    <view class="content-wrap">
      <checkbox-group @change="checkboxChange">
        <view class="cu-card article no-card solid-bottom">
          <view class="cu-item padding-top-xs" v-for="(item, index) in shoppingCartData" :key="index">
            <view class="flex align-center">
              <checkbox class="blue round margin-left" :value="item.id" :disabled="(item.quantity > (item.goodsSpu && item.goodsSpu.stock) || !item.goodsSpu) && operation" :checked="item.checked" />
              <navigator hover-class="none" class="flex-sub" :url="'/pages/goods/goods-detail/index?id=' + item.spuId">
                <view class="content">
                  <image :src="(item.goodsSpu && item.goodsSpu.picUrls && item.goodsSpu.picUrls[0]) || '/static/img/no_pic.png'" mode="aspectFill" class="row-img margin-top-xs" />
                  <view class="desc row-info">
                    <view class="text-black margin-top-sm overflow-2">{{ item.goodsSpu && item.goodsSpu.name }}</view>
                    <view class="text-gray margin-top-sm text-right text-sm">库存{{ item.goodsSpu && item.goodsSpu.stock }}</view>
                    <view v-if="item.goodsSpu" class="flex">
                      <view class="flex-sub"><view class="text-price text-bold text-blue text-xl margin-top-sm">{{ item.goodsSpu.salesPrice }}</view></view>
                      <view class="flex-sub margin-top-sm" @tap.stop>
                        <base-stepper :stNum="item.quantity" :min="1" :max="item.goodsSpu.stock" @numChange="(n) => cartNumChang(n, index)" />
                      </view>
                    </view>
                    <view v-if="!item.goodsSpu" class="margin-top-sm text-red">请重新选择规格</view>
                  </view>
                </view>
              </navigator>
            </view>
          </view>
        </view>
      </checkbox-group>
      <view v-if="shoppingCartDataInvalid.length > 0" class="cu-bar bg-white solid-bottom margin-top">
        <view class="action">失效宝贝{{ shoppingCartDataInvalid.length }}件</view>
        <view class="action"><button class="cu-btn line-blue round" @tap="clearInvalid">清空失效宝贝</button></view>
      </view>
      <view class="cu-card article no-card" v-for="(item, index) in shoppingCartDataInvalid" :key="'inv'+index">
        <view class="cu-item">
          <navigator hover-class="none" :url="'/pages/goods/goods-detail/index?id=' + item.spuId">
            <view class="content">
              <image :src="(item.goodsSpu && item.goodsSpu.picUrls && item.goodsSpu.picUrls[0]) || '/static/img/no_pic.png'" mode="aspectFill" class="row-img" />
              <view class="desc row-info">
                <view class="text-black margin-top-sm overflow-2">{{ item.goodsSpu && item.goodsSpu.name }}</view>
                <view class="text-sm margin-top-lg text-blue">已下架</view>
              </view>
            </view>
          </navigator>
        </view>
      </view>
      <view class="cu-load bg-gray" :class="loadmore ? 'loading' : ''"></view>
      <view v-if="shoppingCartData.length <= 0 && !loadmore" class="text-center margin-bottom">
        <view class="text-xsl margin-top without"><image class="margin-top-sm" src="/static/img/shopping-cart.jpg" mode="widthFix" /></view>
        购物车空空如也~
        <navigator hover-class="none" url="/pages/goods/goods-list/index"><button class="cu-btn bg-black margin-top">去逛逛</button></navigator>
      </view>
      <ad v-if="config.adEnable" :unit-id="config.adBannerID"></ad>
      <view class="cu-bar justify-center bg-white margin-top-sm">
        <view class="action text-blue text-bold">为您推荐</view>
      </view>
      <goods-card-index :goodsList="goodsListRecom" />
    </view>
    <view class="cu-bar bg-white tabbar border shop foot">
      <view class="flex align-center">
        <checkbox-group @change="checkboxAllChange">
          <checkbox class="blue round margin-left" value="all" :checked="isAllSelect" />
        </checkbox-group>
        <view class="text-lg margin-left-xs">全选</view>
      </view>
      <view class="action bar-rt" v-if="operation">
        <text class="text-xs text-bold">合计：</text>
        <text class="text-xl text-bold text-price text-blue margin-right-sm">{{ settlePrice }}</text>
        <button class="cu-btn shadow-blur margin-left-sm settle-bt" :disabled="selectValue.length <= 0" @tap="orderConfirm">结算{{ selectValue.length > 0 ? '(' + selectValue.length + ')' : '' }}</button>
      </view>
      <view class="action bar-rt" v-else>
        <button class="cu-btn shadow-blur collection" :disabled="selectValue.length <= 0" @tap="userCollectAdd">移入收藏夹</button>
        <button class="cu-btn shadow-blur margin-left-sm delete" :disabled="selectValue.length <= 0" @tap="shoppingCartDel">删除</button>
      </view>
    </view>
  </view>
</template>

<script>
import numberUtil from '@/utils/numberUtil'
import BaseStepper from '@/components/base-stepper/index.vue'
import GoodsCardIndex from '@/components/goods-card-index/index.vue'
export default {
  name: 'ShoppingCartPage',
  components: { BaseStepper, GoodsCardIndex },
  data() {
    const app = getApp()
    return {
      config: app.globalData.config || {},
      page: { current: 1, size: 50, ascs: '', descs: 'create_time' },
      loadmore: true,
      operation: true,
      shoppingCartData: [],
      shoppingCartDataInvalid: [],
      isAllSelect: false,
      selectValue: [],
      settlePrice: 0,
      goodsListRecom: []
    }
  },
  onShow() {
    const app = getApp()
    uni.setTabBarBadge({ index: 2, text: (app.globalData.shoppingCartCount || '') + '' })
    getApp().initPage().then(() => this.shoppingCartPage())
  },
  onLoad() {
    getApp().initPage().then(() => this.goodsRecom())
  },
  methods: {
    operation() { this.operation = !this.operation; this.checkboxHandle(this.selectValue) },
    shoppingCartPage() {
      getApp().api.shoppingCartPage(this.page).then(res => {
        const records = (res.data && res.data.records) || []
        getApp().globalData.shoppingCartCount = (res.data && res.data.total) + ''
        uni.setTabBarBadge({ index: 2, text: (res.data && res.data.total) + '' })
        const valid = [], invalid = []
        records.forEach(r => {
          if (!r.goodsSpu || r.goodsSpu.shelf === '0') invalid.push(r)
          else valid.push(r)
        })
        this.shoppingCartData = valid
        this.shoppingCartDataInvalid = invalid
        this.loadmore = false
        this.checkboxHandle(this.selectValue)
      })
    },
    goodsRecom() {
      getApp().api.goodsPage({ searchCount: false, current: 1, size: 4, descs: 'create_time' }).then(res => {
        this.goodsListRecom = (res.data && res.data.records) || []
      })
    },
    cartNumChang(n, index) {
      this.shoppingCartData[index].quantity = n
      getApp().api.shoppingCartEdit({ id: this.shoppingCartData[index].id, quantity: n })
      this.countSelect()
    },
    checkboxChange(e) { this.checkboxHandle(e.detail.value || []) },
    checkboxAllChange(e) {
      const value = e.detail.value || []
      this.setAllSelectValue(value.length > 0)
    },
    checkboxHandle(selectValue) {
      const list = this.shoppingCartData
      let isAll = false
      if (list.length && selectValue.length === list.length) isAll = true
      list.forEach(c => {
        c.checked = selectValue.indexOf(c.id) > -1 && (this.operation ? (c.goodsSpu && c.quantity <= c.goodsSpu.stock) : true)
      })
      this.selectValue = selectValue.filter(id => list.some(c => c.id === id))
      this.isAllSelect = isAll
      this.countSelect()
    },
    setAllSelectValue(status) {
      const list = this.shoppingCartData
      const selectValue = status ? list.filter(c => this.operation ? (c.goodsSpu && c.quantity <= c.goodsSpu.stock) : true).map(c => c.id) : []
      this.checkboxHandle(selectValue)
    },
    countSelect() {
      let total = 0
      this.shoppingCartData.forEach(c => {
        if (this.selectValue.indexOf(c.id) > -1 && c.goodsSpu && c.quantity <= c.goodsSpu.stock)
          total += Number(c.quantity) * Number(c.goodsSpu.salesPrice)
      })
      this.settlePrice = total.toFixed(2)
    },
    userCollectAdd() {},
    shoppingCartDel() {
      if (this.selectValue.length <= 0) return
      uni.showModal({ content: '确认将这' + this.selectValue.length + '个宝贝删除', cancelText: '我再想想', confirmColor: '#ff0000', success: (res) => {
        if (res.confirm) {
          getApp().api.shoppingCartDel(this.selectValue).then(() => {
            this.selectValue = []; this.isAllSelect = false; this.settlePrice = 0
            this.shoppingCartPage()
          })
        }
      }})
    },
    clearInvalid() {
      const ids = this.shoppingCartDataInvalid.map(c => c.id)
      uni.showModal({ content: '确认清空失效的宝贝吗', cancelText: '我再想想', confirmColor: '#ff0000', success: (res) => {
        if (res.confirm) getApp().api.shoppingCartDel(ids).then(() => { this.shoppingCartDataInvalid = [] })
      }})
    },
    orderConfirm() {
      const params = this.shoppingCartData.filter(c => c.checked && c.goodsSpu && c.goodsSpu.shelf === '1' && c.quantity <= c.goodsSpu.stock).map(c => ({
        spuId: c.spuId, quantity: c.quantity, salesPrice: c.goodsSpu.salesPrice, spuName: c.goodsSpu.name, picUrl: (c.goodsSpu.picUrls && c.goodsSpu.picUrls[0]) || ''
      }))
      uni.setStorage({ key: 'param-orderConfirm', data: params })
      uni.navigateTo({ url: '/pages/order/order-confirm/index' })
    }
  }
}
</script>

<style scoped>
.bar-top { margin-top: 80rpx; box-shadow: none; }
.content-wrap { margin-top: 160rpx; margin-bottom: 100rpx; padding-top: 40rpx; padding-bottom: 50px; }
.bar-rt { width: 480rpx !important; text-align: right !important; margin-right: 10rpx !important; }
.settle-bt { background-color: #2967ff !important; font-weight: 300; width: 220rpx; }
.row-img { width: 30% !important; border-radius: 10rpx; }
.row-info { display: block !important; width: 60%; }
.without image { padding-top: 50rpx; width: 360rpx; height: 305rpx; }
.collection { background-color: #2d2d2f !important; font-weight: 300; width: 220rpx; }
.delete { background-color: #2967ff !important; font-weight: 300; width: 220rpx; }
</style>
