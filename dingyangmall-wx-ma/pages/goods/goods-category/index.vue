<!--
  Copyright (C) 2018-2019 www.dingyangmall.com
-->
<template>
  <view class="page">
    <view class="cu-bar search bg-white">
      <view class="search-form round">
        <text class="cuIcon-search"></text>
        <navigator class="response" hover-class="none" url="/pages/base/search/index">
          <input type="text" placeholder="请输入商品名" />
        </navigator>
      </view>
    </view>
    <view class="VerticalBox margin-top-xs margin-left-xs">
      <scroll-view class="VerticalNav nav" scroll-y scroll-with-animation :scroll-top="VerticalNavTop" style="height:calc(100vh - 100rpx)">
        <view v-for="(item, index) in goodsCategory" :key="index" class="cu-item" :class="{ 'text-red cur': index === TabCur }" @tap="tabSelect(index)">
          {{ item.name }}
        </view>
      </scroll-view>
      <scroll-view class="VerticalMain" scroll-y scroll-with-animation style="height:calc(100vh - 100rpx)" :scroll-into-view="'main-' + MainCur" @scroll="VerticalMain">
        <view v-for="(item, index) in goodsCategory" :key="index" :id="'main-' + index" class="padding-tb-xs padding-lr-sm">
          <view class="cu-bar solid-bottom bg-white">
            <view class="action"><text class="cuIcon-titles text-black"></text>{{ item.name }}</view>
          </view>
          <view class="cu-bar bg-white solid-bottom">
            <view class="cate-list">
              <image v-if="item.picUrl" class="img-banner radius" :src="item.picUrl" mode="aspectFit" />
              <view v-if="item.children && item.children.length" class="cate" v-for="(item2, i2) in item.children" :key="i2">
                <navigator hover-class="none" :url="'/pages/goods/goods-list/index?categorySecond=' + item2.id + '&title=' + encodeURIComponent(item2.name)">
                  <image class="cate-img" :src="item2.picUrl || '/static/img/no_pic.png'" mode="aspectFit" />
                  <view class="text-sm">{{ item2.name }}</view>
                </navigator>
              </view>
              <view v-if="!item.children || !item.children.length" class="padding response text-center">暂无数据</view>
            </view>
          </view>
        </view>
      </scroll-view>
    </view>
  </view>
</template>

<script>
export default {
  name: 'GoodsCategoryPage',
  data() {
    const app = getApp()
    return {
      config: app.globalData.config || {},
      TabCur: 0,
      MainCur: 0,
      VerticalNavTop: 0,
      goodsCategory: [],
      load: true
    }
  },
  onLoad() {
    getApp().initPage().then(() => this.goodsCategoryGet())
  },
  onShow() {
    const app = getApp()
    uni.setTabBarBadge({ index: 2, text: (app.globalData.shoppingCartCount || '') + '' })
  },
  methods: {
    goodsCategoryGet() {
      getApp().api.goodsCategoryGet().then(res => {
        this.goodsCategory = (res.data && res.data) || []
        this.goodsCategory.forEach((item, i) => {
          item.top = i * 50
          item.bottom = (i + 1) * 50
        })
        this.load = false
      })
    },
    tabSelect(index) {
      this.TabCur = index
      this.MainCur = index
      this.VerticalNavTop = (index - 1) * 50
    },
    VerticalMain(e) {
      const scrollTop = (e.detail && e.detail.scrollTop) || 0
      const list = this.goodsCategory
      for (let i = 0; i < list.length; i++) {
        if (scrollTop + 20 > list[i].top && scrollTop < (list[i].bottom || 99999)) {
          this.VerticalNavTop = (i - 1) * 50
          this.TabCur = i
          break
        }
      }
    }
  }
}
</script>

<style scoped>
.VerticalNav.nav { width: 220rpx; white-space: initial; }
.VerticalNav.nav .cu-item { font-size: 14px; font-weight: 300; color: #333; width: 100%; text-align: center; background-color: #f1f1f1; margin: 0; border: none; height: 50px; position: relative; }
.VerticalNav.nav .cu-item.cur { font-size: 15px; font-weight: 500; border-radius: 10rpx 0 0 10rpx; background-color: #fff; }
.VerticalBox { display: flex; }
.VerticalMain { background-color: #fff; }
.img-banner { width: 94%; height: 148rpx; margin: auto; }
.cate-list { display: flex; flex-wrap: wrap; width: 100%; }
.cate { width: 150rpx; font-size: 14px; margin: 15rpx; text-align: center; }
.cate-img { width: 140rpx; height: 140rpx; }
</style>
