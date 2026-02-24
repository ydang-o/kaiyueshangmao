<!--
  Copyright (C) 2018-2019 www.dingyangmall.com
  首页：轮播、宫格、公告、推荐均来自后端接口
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
      <!-- 轮播：来自接口 -->
      <view v-if="swiperData.length" class="swiper-wrap">
        <swiper class="screen-swiper square-dot" indicator-dots circular autoplay :interval="5000" :duration="500" @change="change">
          <swiper-item v-for="(item, i) in swiperData" :key="i" class="margin-top-sm" @tap="goGoodsDetail(item.id)">
            <image class="swiper-image" :src="item.img" mode="aspectFill" />
          </swiper-item>
        </swiper>
      </view>
      <!-- 宫格导航：来自分类接口 -->
      <view class="cu-list grid no-border justify-around" v-if="navList.length">
        <navigator v-for="(nav, i) in navList" :key="i" class="cu-item" hover-class="none" :url="nav.url">
          <image class="nav-image" :src="nav.img" mode="aspectFit" />
          <text class="text-black">{{ nav.text }}</text>
        </navigator>
      </view>
      <!-- 公告：来自接口，无则不展示 -->
      <view class="adsec light bg-white margin-top-sm" v-if="noticeList.length">
        <swiper class="swiper_container" autoplay circular :interval="6000">
          <swiper-item v-for="(item, i) in noticeList" :key="i">
            <view class="bg-white padding-left-sm text-black"><text class="cu-tag line-black text-bold">公告</text><text class="details margin-left">{{ item }}</text></view>
          </swiper-item>
        </swiper>
      </view>
      <!-- 推荐大图块：来自接口 -->
      <view v-for="(item, i) in promoList" :key="i" class="promo-block">
        <navigator hover-class="none" :url="'/pages/goods/goods-detail/index?id=' + item.id">
          <image class="margin-top-sm goods-image" :src="(item.picUrls && item.picUrls[0]) || ''" mode="widthFix" />
          <view class="flex justify-between margin-top-sm align-center padding-lr padding-bottom">
            <view class="text-black"><text class="text-price" v-if="item.salesPrice">{{ item.salesPrice }}</text> 起 {{ item.name }}</view>
            <view class="cu-btn round bg-gray text-white buy-now">购买</view>
          </view>
        </navigator>
      </view>
      <!-- 热销单品：来自接口 -->
      <view class="wrapper bg-white margin-top-xl">
        <view class="cu-bar text-black">
          <view class="action margin-left-sm"><text class="text-xxl text-bold">热销单品</text></view>
          <navigator hover-class="none" url="/pages/goods/goods-list/index?type=2" class="action">更多<text class="cuIcon-right"></text></navigator>
        </view>
        <view class="wrapper-list bg-white radius">
          <scroll-view class="scroll-view_x hot-goods" scroll-x v-if="goodsListHot.length">
            <view v-for="(item, i) in goodsListHot" :key="i" class="item">
              <navigator hover-class="none" :url="'/pages/goods/goods-detail/index?id=' + item.id">
                <view class="text-cut text-bold text-xxl text-black margin-top-sm">{{ item.name }}</view>
                <view class="text-cut text-sm text-gray margin-top-sm">{{ item.sellPoint }}</view>
                <view class="img-box margin-top-xl">
                  <image :src="(item.picUrls && item.picUrls[0]) || ''" mode="aspectFill" />
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
      swiperData: [],
      navList: [],
      noticeList: [],
      promoList: []
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
    return { title: '如囍优选', path: 'pages/home/index' }
  },
  methods: {
    change() {},
    goGoodsDetail(id) {
      if (id) uni.navigateTo({ url: '/pages/goods/goods-detail/index?id=' + id })
    },
    loadData() {
      const app = getApp()
      Promise.all([
        app.api.goodsPage({ searchCount: false, current: 1, size: 5, descs: 'create_time' }),
        app.api.goodsPage({ searchCount: false, current: 1, size: 5, descs: 'sale_num' }),
        app.api.goodsPage(this.page),
        app.api.goodsCategoryGet(),
        app.api.goodsPage({ searchCount: false, current: 1, size: 6 }),
        app.api.goodsPage({ searchCount: false, current: 1, size: 2 })
      ]).then(([resNew, resHot, resPage, resCategory, resBanner, resPromo]) => {
        this.goodsListHot = (resHot.data && resHot.data.records) || []
        this.goodsList = [...this.goodsList, ...((resPage.data && resPage.data.records) || [])]
        if (!resPage.data.records || resPage.data.records.length < this.page.size) this.loadmore = false
        this.buildNavList(resCategory.data || resCategory)
        this.buildSwiper((resBanner.data && resBanner.data.records) || [])
        this.promoList = (resPromo.data && resPromo.data.records) || []
        this.loadNotice()
      }).catch(() => {})
    },
    loadNotice() {
      const app = getApp()
      if (typeof app.api.getNoticeList !== 'function') return
      app.api.getNoticeList().then(res => {
        const raw = (res && res.data) || []
        if (Array.isArray(raw)) this.noticeList = raw
        else if (raw && raw.content && Array.isArray(raw.content)) this.noticeList = raw.content
        else if (raw && raw.records && Array.isArray(raw.records)) this.noticeList = raw.records.map(r => r.content || r.title || r.name)
      }).catch(() => {})
    },
    buildNavList(categoryTree) {
      if (!categoryTree || !categoryTree.length) return
      const list = []
      for (let i = 0; i < categoryTree.length && list.length < 8; i++) {
        const cat = categoryTree[i]
        if (cat.children && cat.children.length) {
          cat.children.slice(0, 8 - list.length).forEach(c => {
            list.push({
              url: '/pages/goods/goods-list/index?categorySecond=' + (c.id || c.categoryId) + '&title=' + encodeURIComponent(c.name || ''),
              img: c.picUrl || '',
              text: c.name || ''
            })
          })
        } else {
          list.push({
            url: '/pages/goods/goods-list/index?categorySecond=' + (cat.id || cat.categoryId) + '&title=' + encodeURIComponent(cat.name || ''),
            img: cat.picUrl || '',
            text: cat.name || ''
          })
        }
      }
      this.navList = list
    },
    buildSwiper(records) {
      this.swiperData = (records || []).map(item => ({
        id: item.id,
        img: (item.picUrls && item.picUrls[0]) ? item.picUrls[0] : (item.picUrl || '')
      })).filter(item => item.img)
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
      this.swiperData = []
      this.navList = []
      this.promoList = []
      this.loadData()
    }
  }
}
</script>

<style scoped>
.page { padding-bottom: 20rpx; }
.swiper-wrap { margin-bottom: 20rpx; }
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
.goods-image { width: 100%; height: 400rpx; min-height: 200rpx; }
.hot-goods { width: auto; overflow: hidden; }
.buy-now { color: #2967ff; width: 160rpx; }
.search-form { border-radius: 20rpx; }
.margin-top-bar { margin-top: 80rpx; }
.promo-block { margin-bottom: 20rpx; }
</style>
