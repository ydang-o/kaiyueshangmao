<!--
  Copyright (C) 2018-2019 www.dingyangmall.com
-->
<template>
  <view class="page tm-page">
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
              <image v-if="item.picUrl" class="img-banner radius" :src="$imgUrl(item.picUrl)" mode="aspectFit" />
              <view v-if="item.children && item.children.length" class="cate" v-for="(item2, i2) in item.children" :key="i2">
                <navigator hover-class="none" :url="'/pages/goods/goods-list/index?categorySecond=' + item2.id + '&title=' + encodeURIComponent(item2.name)">
                  <image class="cate-img" :src="$imgUrl(item2.picUrl) || '/static/img/no_pic.png'" mode="aspectFit" />
                  <view class="text-sm">{{ item2.name }}</view>
                </navigator>
              </view>
              <view v-if="!item.children || !item.children.length" class="padding response text-center">暂无数据</view>
            </view>
          </view>
        </view>
      </scroll-view>
    </view>
    <view v-if="!isLoggedIn" class="login-banner-bottom-spacer"></view>
    <login-banner
      :is-logged-in="isLoggedIn"
      :has-phone="hasPhone"
      :profile-skipped="profileSkipped"
      :display-nickname="displayNickname"
      :display-avatar="displayAvatar"
      :display-phone="displayPhone"
      @login-success="onLoginSuccess"
      @skip="onProfileSkipOrConfirm"
      @confirm="onProfileSkipOrConfirm"
    />
  </view>
</template>

<script>
import LoginBanner from '@/components/login-banner/index.vue'
import apiModule from '@/utils/api'
export default {
  name: 'GoodsCategoryPage',
  components: { LoginBanner },
  data() {
    const app = getApp()
    const wxUser = app.globalData.wxUser || {}
    return {
      config: app.globalData.config || {},
      isLoggedIn: !!app.globalData.thirdSession,
      hasPhone: !!(wxUser.phoneNumber || wxUser.phone),
      profileSkipped: !!app.globalData.profileSkipped,
      displayNickname: (wxUser.nickName || wxUser.nickname || '').trim(),
      displayAvatar: (wxUser.headimgUrl || wxUser.avatarUrl || wxUser.avatar || '').trim(),
      displayPhone: (wxUser.phoneNumber || wxUser.phone || '').trim(),
      TabCur: 0,
      MainCur: 0,
      VerticalNavTop: 0,
      goodsCategory: [],
      load: true
    }
  },
  onLoad() {
    const app = getApp()
    if (!app.initPage) {
      this.goodsCategoryGet()
      return
    }
    app.initPage().then(() => this.goodsCategoryGet()).catch(() => this.goodsCategoryGet())
  },
  onShow() {
    const app = getApp()
    this.isLoggedIn = !!app.globalData.thirdSession
    this.profileSkipped = !!app.globalData.profileSkipped
    const wxUser = app.globalData.wxUser || {}
    this.hasPhone = !!(wxUser.phoneNumber || wxUser.phone)
    this.displayNickname = (wxUser.nickName || wxUser.nickname || '').trim()
    this.displayAvatar = (wxUser.headimgUrl || wxUser.avatarUrl || wxUser.avatar || '').trim()
    this.displayPhone = (wxUser.phoneNumber || wxUser.phone || '').trim()
    const pages = getCurrentPages()
    const page = pages[pages.length - 1]
    if (page && typeof page.getTabBar === 'function') {
      const tabBar = page.getTabBar()
      if (tabBar && tabBar.setData) tabBar.setData({ selected: 1 })
    }
    uni.setTabBarBadge({ index: 2, text: (app.globalData.shoppingCartCount || '') + '' })
  },
  methods: {
    onProfileSkipOrConfirm() {
      const app = getApp()
      if (app && app.globalData) app.globalData.profileSkipped = true
      this.profileSkipped = true
    },
    onLoginSuccess() {
      const app = getApp()
      this.isLoggedIn = !!app.globalData.thirdSession
      const wxUser = app.globalData.wxUser || {}
      this.hasPhone = !!(wxUser.phoneNumber || wxUser.phone)
      this.displayNickname = (wxUser.nickName || wxUser.nickname || '').trim()
      this.displayAvatar = (wxUser.headimgUrl || wxUser.avatarUrl || wxUser.avatar || '').trim()
      this.displayPhone = (wxUser.phoneNumber || wxUser.phone || '').trim()
    },
    goodsCategoryGet() {
      const app = getApp()
      const api = (app && app.api) || (app && app.globalData && app.globalData.__api) || apiModule
      const retryCount = (this._categoryRetryCount || 0)
      if (!api || typeof api.goodsCategoryGet !== 'function') {
        if (retryCount >= 10) {
          console.error('[Category] api 始终未就绪，已停止重试')
          return
        }
        this._categoryRetryCount = retryCount + 1
        if (retryCount === 0) console.warn('[Category] api 未就绪，将重试最多 10 次')
        setTimeout(() => this.goodsCategoryGet(), 200)
        return
      }
      this._categoryRetryCount = 0
      api.goodsCategoryGet().then(res => {
        this.goodsCategory = (res.data && res.data) || []
        this.goodsCategory.forEach((item, i) => {
          item.top = i * 50
          item.bottom = (i + 1) * 50
        })
        this.load = false
      }).catch(err => {
        console.error('[Category] goodsCategoryGet 失败', err)
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
.VerticalNav.nav { width: 230rpx; white-space: initial; }
.VerticalNav.nav .cu-item { font-size: 14px; font-weight: 400; color: #374151; width: 100%; text-align: center; background-color: #f3f4f8; margin: 0; border: none; height: 52px; position: relative; }
.VerticalNav.nav .cu-item.cur { font-size: 15px; font-weight: 700; color: #ff0036; border-radius: 14rpx 0 0 14rpx; background-color: #fff; }
.VerticalBox { display: flex; }
.VerticalMain { background-color: #fff; border-radius: 18rpx 0 0 18rpx; box-shadow: 0 8rpx 26rpx rgba(15,23,42,.06); }
.img-banner { width: 94%; height: 148rpx; margin: auto; border-radius: 12rpx; }
.cate-list { display: flex; flex-wrap: wrap; width: 100%; }
.cate { width: 150rpx; font-size: 14px; margin: 15rpx; text-align: center; }
.cate-img { width: 140rpx; height: 140rpx; border-radius: 14rpx; background: #f3f4f6; }
.login-banner-bottom-spacer { height: 140rpx; }
</style>
