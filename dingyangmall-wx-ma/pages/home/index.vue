<!--
  Copyright (C) 2018-2019 www.dingyangmall.com
-->
<template>
  <view class="page">
    <view class="cu-bar search bg-white fixed" style="box-shadow:none">
      <view class="search-form">
        <text class="cuIcon-search"></text>
        <navigator class="response" hover-class="none" url="/pages/base/search/index">
          <input type="text" placeholder="请输入商品名" />
        </navigator>
      </view>
    </view>
    <view class="margin-top-bar bg-white">
      <navigator hover-class="none" url="/pages/goods/goods-detail/index?id=1442505794278191105">
        <swiper class="screen-swiper square-dot" indicator-dots circular autoplay :interval="5000" :duration="500" @change="change">
          <swiper-item v-for="(item, i) in swiperData" :key="i" class="margin-top-sm">
            <image class="swiper-image" :src="item" mode="aspectFill" />
          </swiper-item>
        </swiper>
      </navigator>
      <view class="cu-list grid no-border justify-around">
        <navigator v-for="(nav, i) in navList" :key="i" class="cu-item" hover-class="none" :url="nav.url">
          <image class="nav-image" :src="nav.img" mode="aspectFit" />
          <text class="text-black">{{ nav.text }}</text>
        </navigator>
      </view>
      <view class="adsec light bg-white margin-top-sm">
        <swiper class="swiper_container" autoplay circular :interval="6000">
          <swiper-item><view class="bg-white padding-left-sm text-black"><text class="cu-tag line-black text-bold">公告</text><text class="details margin-left">再见2020，你好2021！</text></view></swiper-item>
          <swiper-item><view class="bg-white padding-left-sm text-black"><text class="cu-tag line-black text-bold">注意</text><text class="details margin-left">演示商城，不发货不退款</text></view></swiper-item>
        </swiper>
      </view>
      <navigator hover-class="none" url="/pages/goods/goods-detail/index?id=1442505794278191105">
        <image class="margin-top-sm goods-image" src="https://dingyangmall-base-test.oss-cn-zhangjiakou.aliyuncs.com/1/material/055f3304-13d1-43c8-8547-326cc3efc7fc.jpg" mode="widthFix" />
        <view class="flex justify-between margin-top-sm align-center padding-lr">
          <view class="text-black">RMB 7999 起</view>
          <view class="cu-btn round bg-gray text-white buy-now">购买</view>
        </view>
      </navigator>
      <navigator hover-class="none" url="/pages/goods/goods-detail/index?id=1442512050032275457">
        <image class="margin-top-xl goods-image" src="https://dingyangmall-base-test.oss-cn-zhangjiakou.aliyuncs.com/1/material/2f290591-8351-4be8-a9ab-6277d007b8c7.jpg" mode="widthFix" />
        <view class="flex justify-between margin-top-sm align-center padding-lr padding-bottom">
          <view class="text-black">RMB 3799 起</view>
          <view class="cu-btn round bg-gray text-white buy-now">购买</view>
        </view>
      </navigator>
      <view class="wrapper bg-white margin-top-xl">
        <view class="cu-bar text-black">
          <view class="action margin-left-sm"><text class="text-xxl text-bold">热销单品</text></view>
          <navigator hover-class="none" url="/pages/goods/goods-list/index?type=2" class="action">更多<text class="cuIcon-right"></text></navigator>
        </view>
        <view class="wrapper-list bg-white radius">
          <scroll-view class="scroll-view_x hot-goods" scroll-x>
            <view v-for="(item, i) in goodsListHot" :key="i" class="item">
              <navigator hover-class="none" :url="'/pages/goods/goods-detail/index?id=' + item.id">
                <view class="text-cut text-bold text-xxl text-black margin-top-sm">{{ item.name }}</view>
                <view class="text-cut text-sm text-gray margin-top-sm">{{ item.sellPoint }}</view>
                <view class="img-box margin-top-xl">
                  <image :src="(item.picUrls && item.picUrls[0]) || '/static/img/no_pic.png'" mode="aspectFill" />
                </view>
                <view class="flex justify-between margin-top">
                  <view class="text-df padding-top-sm"><text class="text-price">{{ item.salesPrice }}</text> 起</view>
                  <view class="cu-btn round shadow-blur text-white buy-now">购买</view>
                </view>
              </navigator>
            </view>
          </scroll-view>
        </view>
      </view>
      <view class="cu-bar">
        <view class="action"><text class="text-xxl text-bold text-black">猜你喜欢</text></view>
      </view>
      <goods-card-index :goodsList="goodsList" />
      <view class="cu-load bg-white" :class="loadmore ? 'loading' : 'over'"></view>
    </view>
    <ad v-if="config.adEnable" :unit-id="config.adBannerID"></ad>
  </view>
</template>

<script>
import GoodsCardIndex from '@/components/goods-card-index/index.vue'
export default {
  name: 'HomePage',
  components: { GoodsCardIndex },
  data() {
    const app = getApp()
    return {
      config: app.globalData.config || {},
      page: { searchCount: false, current: 1, size: 10 },
      loadmore: true,
      goodsList: [],
      goodsListHot: [],
      swiperData: [
        'https://dingyangmall-base-test.oss-cn-zhangjiakou.aliyuncs.com/1/material/c888e1d3-f542-4b4e-aa43-be9d50cc0696.jpg',
        'https://dingyangmall-base-test.oss-cn-zhangjiakou.aliyuncs.com/1/material/a5e3b9f4-f1fe-4bb2-b487-13f4395ef187.jpg',
        'https://dingyangmall-base-test.oss-cn-zhangjiakou.aliyuncs.com/1/material/c8fd87ff-ca5d-4f95-8f81-e99cae48b0f7.jpg',
        'https://dingyangmall-base-test.oss-cn-zhangjiakou.aliyuncs.com/1/material/bf176bd5-3487-4b61-8d30-1cc2ad95b8ac.jpg'
      ],
      navList: [
        { url: '/pages/goods/goods-detail/index?id=1442505794278191105', img: '/static/img/6-1.png', text: 'iPhone' },
        { url: '/pages/goods/goods-detail/index?id=1442512382615416833', img: '/static/img/6-2.png', text: 'iPad' },
        { url: '/pages/goods/goods-detail/index?id=1442512958581436418', img: '/static/img/6-3.png', text: 'Mac' },
        { url: '/pages/goods/goods-detail/index?id=1442513594978988034', img: '/static/img/6-4.png', text: 'Watch' },
        { url: '/pages/goods/goods-detail/index?id=1442514202062548994', img: '/static/img/6-5.png', text: 'AirPods' }
      ]
    }
  },
  onLoad() {
    getApp().initPage().then(() => this.loadData())
  },
  onShow() {
    const app = getApp()
    uni.setTabBarBadge({ index: 2, text: (app.globalData.shoppingCartCount || '') + '' })
  },
  onPullDownRefresh() {
    uni.showNavigationBarLoading()
    this.refresh()
    uni.hideNavigationBarLoading()
    uni.stopPullDownRefresh()
  },
  onReachBottom() {
    if (this.loadmore) {
      this.page.current++
      this.goodsPage()
    }
  },
  onShareAppMessage() {
    return { title: 'dingyangmall商城源码-小程序演示', path: 'pages/home/index' }
  },
  methods: {
    change() {},
    loadData() {
      const app = getApp()
      Promise.all([
        app.api.goodsPage({ searchCount: false, current: 1, size: 5, descs: 'create_time' }),
        app.api.goodsPage({ searchCount: false, current: 1, size: 5, descs: 'sale_num' }),
        app.api.goodsPage(this.page)
      ]).then(([resNew, resHot, resPage]) => {
        this.goodsListHot = (resHot.data && resHot.data.records) || []
        this.goodsList = [...this.goodsList, ...((resPage.data && resPage.data.records) || [])]
        if (!resPage.data.records || resPage.data.records.length < this.page.size) this.loadmore = false
      }).catch(() => {})
    },
    goodsPage() {
      getApp().api.goodsPage(this.page).then(res => {
        const list = (res.data && res.data.records) || []
        this.goodsList = [...this.goodsList, ...list]
        if (list.length < this.page.size) this.loadmore = false
      }).catch(() => {})
    },
    refresh() {
      this.loadmore = true
      this.page.current = 1
      this.goodsList = []
      this.goodsListHot = []
      this.loadData()
    }
  }
}
</script>

<style scoped>
.page { padding-bottom: 20rpx; }
.wrapper-list { white-space: nowrap; padding: 0 0 50rpx 0; }
.wrapper-list .item { display: inline-block; width: 560rpx; height: 800rpx; margin: 60rpx 0 60rpx 50rpx; padding: 10rpx 30rpx; border-radius: 25rpx; box-shadow: 0 0 60rpx rgba(211,211,211,1); }
.wrapper-list .item .img-box { width: 100%; height: 480rpx; }
.wrapper-list .item .img-box image { width: 100%; height: 100%; }
.adsec { width: 100%; display: flex; padding: 7rpx 10rpx; height: 80rpx; align-items: center; }
.swiper_container { height: 80rpx; width: 95%; }
.screen-swiper { height: 480rpx; }
.swiper-image { width: 96% !important; height: 350rpx !important; margin: 40rpx auto !important; border-radius: 20rpx; box-shadow: 0 10rpx 60rpx rgba(211,211,211,1); }
.nav-image { width: 100rpx; height: 100rpx; }
.text-black { color: #000 !important; }
.goods-image { width: 100%; height: 1150rpx; }
.hot-goods { width: auto; overflow: hidden; }
.buy-now { color: #2967ff; width: 160rpx; }
.search-form { border-radius: 20rpx; }
.margin-top-bar { margin-top: 80rpx; }
</style>
