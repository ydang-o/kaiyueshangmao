<!--
  Copyright (C) 2018-2019 www.dingyangmall.com
-->
<template>
  <view class="page tm-page product-bg">
    <view v-if="goodsSpu" class="bg-white padding-top">
      <swiper class="screen-swiper square-dot screen" indicator-dots circular autoplay :interval="5000" :duration="500" @change="change">
        <swiper-item v-for="(pic, i) in (goodsSpu.picUrls || [])" :key="i">
          <image class="screen-image" :src="$imgUrl(pic)" mode="aspectFill" />
        </swiper-item>
      </swiper>
      <view class="page-index cu-tag round">{{ currents }}/{{ (goodsSpu.picUrls && goodsSpu.picUrls.length) || 1 }}</view>
    </view>
    <view v-if="goodsSpu" class="cu-bar bg-white padding-top-xl">
      <view class="text-xxl padding-tb-xs padding-lr-sm">
        <text class="text-price text-blue text-bold">{{ goodsSpu.salesPrice }}</text>
      </view>
      <button class="cu-btn icon margin-right" @tap="shareShow"><text class="cuIcon-share text-green"></text></button>
    </view>
    <view v-if="goodsSpu" class="cu-bar bg-white">
      <view class="text-lg text-bold padding-lr-sm"><text class="text-black">{{ goodsSpu.name }}</text></view>
    </view>
    <view v-if="goodsSpu" class="cu-bar bg-white">
      <view class="text-sm padding-lr-sm"><text class="text-gray">{{ goodsSpu.sellPoint }}</text></view>
    </view>
    <view v-if="goodsSpu" class="cu-bar bg-white solid-bottom">
      <view class="text-sm padding-lr-sm text-gray" v-if="goodsSpu.marketPrice">市场价：<text class="text-price text-gray">{{ goodsSpu.marketPrice }}</text></view>
      <view class="text-sm margin-right text-gray">库存：{{ goodsSpu.stock }}</view>
    </view>
    <view v-if="goodsSpu" class="cu-bar bg-white solid-bottom">
      <view class="flex response">
        <view class="flex-sub text-sm"><view class="text-gray margin-left-sm">深圳发货</view></view>
        <view class="flex-treble text-sm">
          <text class="cuIcon-location text-black" v-if="goodsSpu.deliveryPlace">{{ (goodsSpu.deliveryPlace && goodsSpu.deliveryPlace.place) || '' }} | </text>
          <text class="text-black" v-if="goodsSpu.freightTemplat">运费：全国包邮</text>
        </view>
        <view class="flex-sub text-sm text-gray text-right margin-right-sm">销量{{ goodsSpu.saleNum }}</view>
      </view>
    </view>
    <view v-if="goodsSpu" class="cu-bar bg-white">
      <view class="flex response">
        <view class="flex-sub text-sm"><view class="text-gray margin-left-sm">服务</view></view>
        <view class="flex-sub text-sm text-gray text-right margin-right-sm">假一赔十 | 七天无理由退款</view>
      </view>
    </view>
    <ad v-if="config.adEnable" :unit-id="config.adBannerID"></ad>
    <view v-if="goodsSpu" class="cu-bar bg-white margin-top-sm">
      <view class="content">商品信息</view>
    </view>
    <view v-if="goodsSpu && goodsSpu.description" class="bg-white padding">
      <rich-text :nodes="goodsSpu.description"></rich-text>
    </view>
    <view class="cu-load bg-gray to-down">已经到底啦...</view>
    <view class="cu-bar bg-white tabbar border shop foot">
      <button open-type="contact" class="action bg-white" :send-message-title="(goodsSpu && goodsSpu.name) + '--咨询'" :send-message-img="$imgUrl(goodsSpu && goodsSpu.picUrls && goodsSpu.picUrls[0]) || '/static/img/no_pic.png'" show-message-card :send-message-path="'/pages/goods/goods-detail/index?id=' + (goodsSpu && goodsSpu.id)">
        <view class="cuIcon-servicefill"></view> 客服
      </button>
      <navigator class="action" url="/pages/shopping-cart/index">
        <view class="cuIcon-cart"><view class="cu-tag badge" v-if="shoppingCartCount">{{ shoppingCartCount }}</view></view>
        购物车
      </navigator>
      <view class="btn-group">
        <button class="cu-btn shadow-blur margin-left-sm shopping-cart tm-secondary-btn" @tap="toDo(1)"><text class="text-white">加入购物车</text></button>
        <button class="cu-btn shadow-blur buy-now tm-primary-btn" @tap="toDo(2)"><text class="text-white">立即购买</text></button>
      </view>
    </view>
    <view class="cu-modal" :class="goodsSpu && goodsSpu.shelf === '0' ? 'show' : ''">
      <view class="cu-dialog">
        <view class="cu-bar bg-white justify-end"><view class="content">提示</view></view>
        <view class="padding-xl">抱歉，该商品已下架</view>
      </view>
    </view>
    <view class="cu-modal bottom-modal" :class="shareModalVisible">
      <view class="cu-dialog">
        <view class="cu-bar bg-white">
          <view class="action text-green"></view>
          <view class="action text-red" @tap="shareHide">取消</view>
        </view>
        <view class="padding flex flex-direction">
          <button class="cu-btn bg-green lg" open-type="share">发送给朋友</button>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import apiModule from '@/utils/api'
import util from '@/utils/util'
export default {
  name: 'GoodsDetailPage',
  data() {
    const app = getApp()
    return {
      config: app.globalData.config || {},
      id: '',
      goodsSpu: null,
      currents: 1,
      cartNum: 1,
      shoppingCartCount: 0,
      shareModalVisible: ''
    }
  },
  onLoad(options) {
    let id = options.scene ? decodeURIComponent(options.scene) : (options.id || '')
    this.id = id
    getApp().initPage().then(() => {
      this.goodsGet(id)
      this.shoppingCartCountFn()
    })
  },
  onShareAppMessage() {
    const g = this.goodsSpu
    return { title: g ? g.name : '', path: 'pages/goods/goods-detail/index?id=' + (g ? g.id : this.id), imageUrl: g && g.picUrls && g.picUrls[0] ? this.$imgUrl(g.picUrls[0]) : '' }
  },
  methods: {
    goodsGet(id) {
      if (!id) return
      const app = getApp()
      const api = (app && app.api) || (app && app.globalData && app.globalData.__api) || apiModule
      if (!api || typeof api.goodsGet !== 'function') return
      api.goodsGet(id).then(res => {
        this.goodsSpu = res.data || null
        this.currents = 1
      })
    },
    change(e) { this.currents = (e.detail && e.detail.current) + 1 },
    shoppingCartCountFn() {
      const app = getApp()
      const api = (app && app.api) || (app && app.globalData && app.globalData.__api)
      if (api && typeof api.shoppingCartCount === 'function') {
        api.shoppingCartCount().then(res => {
          const count = (res && res.data != null) ? res.data : (res && res.data !== undefined ? res.data : 0)
          this.shoppingCartCount = count
          util.updateCartBadge(count)
        }).catch(() => {})
      }
    },
    toDo(type) {
      const goodsSpu = this.goodsSpu
      if (!goodsSpu) return
      const msg = type === 1 ? '请先登录后再加入购物车' : '请先登录后再购买'
      util.requireLogin(msg).then((ok) => {
        if (!ok) return
        this._toDoAction(type)
      })
    },
    _toDoAction(type) {
      const goodsSpu = this.goodsSpu
      if (!goodsSpu) return
      const app = getApp()
      const api = (app && app.api) || (app && app.globalData && app.globalData.__api) || apiModule
      if (type === 1) {
        if (!api || typeof api.shoppingCartAdd !== 'function') {
          uni.showToast({ title: '功能暂不可用，请稍后重试', icon: 'none' })
          return
        }
        const pic = (goodsSpu.picUrls && goodsSpu.picUrls[0]) || ''
        api.shoppingCartAdd({
          spuId: goodsSpu.id,
          quantity: this.cartNum,
          addPrice: goodsSpu.salesPrice,
          salesPrice: goodsSpu.salesPrice,
          spuName: goodsSpu.name,
          picUrl: pic
        }).then(() => {
          uni.showToast({ title: '添加成功' })
          this.shoppingCartCountFn()
        }).catch(() => {
          uni.showToast({ title: '加入购物车失败', icon: 'none' })
        })
      } else {
        if (goodsSpu.stock <= 0) {
          uni.showToast({ title: '抱歉，库存不足暂时无法购买', icon: 'none' })
          return
        }
        uni.setStorage({
          key: 'param-orderConfirm',
          data: [{ spuId: goodsSpu.id, quantity: this.cartNum, salesPrice: goodsSpu.salesPrice, spuName: goodsSpu.name, picUrl: (goodsSpu.picUrls && goodsSpu.picUrls[0]) || '' }]
        })
        uni.navigateTo({ url: '/pages/order/order-confirm/index' })
      }
    },
    shareShow() { this.shareModalVisible = 'show' },
    shareHide() { this.shareModalVisible = '' }
  }
}
</script>

<style scoped>
.product-bg { width: 100%; position: relative; }
.product-bg swiper { width: 100%; height: calc(100vw); position: relative; }
.page-index { position: absolute; right: 30rpx; bottom: 30rpx; background: rgba(17, 24, 39, .55); color: #fff; }
.to-down { margin-bottom: 130rpx; }
.screen { width: 94% !important; border-radius: 24rpx; min-height: 900rpx; margin: auto; background-color: #f2f3f5; box-shadow: 0 10rpx 34rpx rgba(15,23,42,.08); }
.screen-image { padding-top: 80rpx; height: 780rpx !important; }
.shopping-cart { width: 220rpx; }
.buy-now { width: 220rpx; }
.cu-bar.tabbar.shop .action { width: unset; }
</style>
