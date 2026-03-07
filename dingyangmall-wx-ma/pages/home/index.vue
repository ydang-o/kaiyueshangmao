<!--
  Copyright (C) 2018-2019 www.dingyangmall.com
  首页：轮播、宫格、公告、推荐均来自后端接口
-->
<template>
  <view class="page tm-page">
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
            <image class="swiper-image" :src="$imgUrl(item.img)" mode="aspectFill" />
          </swiper-item>
        </swiper>
      </view>
      <!-- 四大功能：优惠券、签到、抽奖、红包（紧接轮播，优先展示） -->
      <view class="home-four-actions bg-white">
        <view class="four-actions-grid">
          <navigator class="four-action-item" url="/pages/coupon/my-coupons/index" hover-class="none">
            <view class="four-action-icon-wrap orange"><text class="cuIcon-form"></text></view>
            <text class="four-action-text">我的优惠券</text>
          </navigator>
          <view class="four-action-item" @tap="signIn">
            <view class="four-action-icon-wrap blue"><text class="cuIcon-calendar"></text></view>
            <text class="four-action-text">每日签到</text>
          </view>
          <navigator class="four-action-item" url="/pages/lottery/index/index" hover-class="none">
            <view class="four-action-icon-wrap red"><text class="cuIcon-medal"></text></view>
            <text class="four-action-text">积分抽奖</text>
          </navigator>
          <navigator class="four-action-item" url="/pages/integral/packet/index" hover-class="none">
            <view class="four-action-icon-wrap red"><text class="cuIcon-redpacket"></text></view>
            <text class="four-action-text">积分红包</text>
          </navigator>
        </view>
      </view>
      <!-- 公告：来自接口，无则不展示 -->
      <view class="adsec light bg-white margin-top-sm" v-if="noticeList.length">
        <swiper class="swiper_container" autoplay circular :interval="6000">
          <swiper-item v-for="(item, i) in noticeList" :key="i">
            <view class="bg-white padding-left-sm text-black"><text class="cu-tag line-black text-bold">公告</text><text class="details margin-left">{{ item }}</text></view>
          </swiper-item>
        </swiper>
      </view>
      <!-- <view class="cu-bar section-title-bar">
        <view class="action"><text>猜你喜欢</text></view>
      </view> -->
      <goods-card-index :goods-list="goodsList" :staggered="true" />
      <view class="cu-load bg-white" :class="loadmore ? 'loading' : 'over'"></view>
    </view>
    <!-- 未登录或未授权手机号时底部留白，避免内容被固定登录条遮挡 -->
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
    <view v-if="config.adEnable" class="ad-wrap"><ad :unit-id="config.adBannerID"></ad></view>
  </view>
</template>

<script>
import GoodsCardIndex from '@/components/goods-card-index/index.vue'
import LoginBanner from '@/components/login-banner/index.vue'
import apiModule from '@/utils/api'
import util from '@/utils/util'
export default {
  name: 'HomePage',
  components: { GoodsCardIndex, LoginBanner },
  data() {
    const app = getApp()
    const wxUser = app.globalData.wxUser || {}
    return {
      config: app.globalData.config || {},
      page: { searchCount: false, current: 1, size: 10 },
      loadmore: true,
      goodsList: [],
      swiperData: [],
      navList: [],
      noticeList: [],
      promoList: [],
      isLoggedIn: !!app.globalData.thirdSession,
      hasPhone: !!(wxUser.phoneNumber || wxUser.phone),
      profileSkipped: !!app.globalData.profileSkipped,
      displayNickname: (wxUser.nickName || wxUser.nickname || '').trim(),
      displayAvatar: (wxUser.headimgUrl || wxUser.avatarUrl || wxUser.avatar || '').trim(),
      displayPhone: (wxUser.phoneNumber || wxUser.phone || '').trim(),
      loginLoading: false
    }
  },
  onLoad() {
    console.log('Hello World')
    const app = getApp()
    let loadDataScheduled = false
    const tryLoad = () => {
      if (loadDataScheduled) return
      loadDataScheduled = true
      if (typeof this.loadData === 'function') this.loadData()
    }
    // 商品分页走 /api/ma，未登录也可请求，首页始终拉取商品列表
    const runLoad = () => {
      tryLoad()
    }
    if (!app.initPage) {
      console.warn('[Home] initPage 未挂载，延迟 500ms 再拉取')
      setTimeout(runLoad, 500)
      setTimeout(() => { if (!loadDataScheduled) runLoad() }, 2500)
    } else {
      app.initPage()
        .then(runLoad)
        .catch(() => {
          setTimeout(() => {
            app.initPage().then(runLoad).catch(runLoad)
          }, 300)
        })
    }
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
      if (tabBar && tabBar.setData) tabBar.setData({ selected: 0 })
    }
    util.updateCartBadge(app.globalData.shoppingCartCount || 0)
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
    onProfileSkipOrConfirm() {
      const app = getApp()
      if (app && app.globalData) app.globalData.profileSkipped = true
      this.profileSkipped = true
    },
    onLoginSuccess() {
      const app = getApp()
      this.isLoggedIn = !!(app.globalData.wxToken || app.globalData.thirdSession)
    const wxUser = app.globalData.wxUser || {}
    this.hasPhone = !!(wxUser.phoneNumber || wxUser.phone)
    this.displayNickname = (wxUser.nickName || wxUser.nickname || '').trim()
    this.displayAvatar = (wxUser.headimgUrl || wxUser.avatarUrl || wxUser.avatar || '').trim()
    this.displayPhone = (wxUser.phoneNumber || wxUser.phone || '').trim()
    if (this.isLoggedIn && typeof this.loadData === 'function') this.loadData()
    },
    wxLogin() {
      uni.showModal({
        title: '微信授权登录',
        content: '为了更好的服务体验，将使用你的微信账号登录如囍优选。是否授权？',
        cancelText: '取消',
        confirmText: '授权登录',
        confirmColor: '#ff0036',
        success: (res) => {
          if (!res.confirm) return
          this.loginLoading = true
          getApp().doLogin().then((result) => {
            this.loginLoading = false
            this.isLoggedIn = !!getApp().globalData.thirdSession
            if (result === 'fail') {
              uni.showToast({ title: '登录失败，请重试', icon: 'none' })
              return
            }
            uni.showToast({ title: '登录成功', icon: 'success' })
            this.fetchUserProfileAuth()
          })
        }
      })
    },
    fetchUserProfileAuth() {
      if (typeof uni.getUserProfile !== 'function') return
      uni.getUserProfile({
        desc: '用于完善会员资料、展示昵称与头像',
        success: (detail) => {
          getApp().api.wxUserSave(detail).then(res => {
            const data = res.data || res
            const user = {
              headimgUrl: data.headimgUrl || data.avatar,
              nickName: data.nickName || data.nickname,
              userId: data.userId || data.id || data.openid,
              ...data
            }
            getApp().globalData.wxUser = Object.assign(getApp().globalData.wxUser || {}, user)
          }).catch(() => {})
        },
        fail: () => {}
      })
    },
    goGoodsDetail(id) {
      if (id) uni.navigateTo({ url: '/pages/goods/goods-detail/index?id=' + id })
    },
    loadData() {
      const app = typeof getApp === 'function' ? getApp() : {}
      // 发请求前再次从 storage 同步 token，避免 GET 请求未带 X-Wx-Token
      if (app && app.globalData && !app.globalData.wxToken && !app.globalData.thirdSession) {
        try {
          const u = typeof uni !== 'undefined' ? uni : wx
          if (u && u.getStorageSync) {
            const saved = u.getStorageSync('wx_token') || u.getStorageSync('wx_third_session')
            if (saved && typeof saved === 'string' && saved.length > 0) {
              app.globalData.wxToken = saved
              app.globalData.thirdSession = saved
              console.log('[Home] loadData 前已从 storage 恢复 token')
            }
          }
        } catch (e) {}
      }
      const api = (app && app.api) || (app.globalData && app.globalData.__api) || apiModule
      const retryCount = (this._loadDataRetryCount || 0)
      if (!api || (typeof api.getHomePage !== 'function' && typeof api.goodsPage !== 'function')) {
        if (retryCount >= 10) {
          console.error('[Home] api 始终未就绪，请确认用 HBuilderX「运行到微信开发者工具」并打开 unpackage/dist/dev/mp-weixin')
          return
        }
        this._loadDataRetryCount = retryCount + 1
        if (retryCount === 0) console.warn('[Home] api 未就绪，将重试最多 10 次')
        setTimeout(() => this.loadData(), 200)
        return
      }
      this._loadDataRetryCount = 0
      console.log('[Home] loadData 开始请求')
      // 优先使用首页聚合接口（与各大购物平台一致，一次请求拿全首页数据）
      if (typeof api.getHomePage === 'function') {
        api.getHomePage().then((res) => {
          const data = (res && res.data) || res || {}
          const bannerList = data.bannerList || []
          const categoryTree = data.categoryTree || []
          const noticeList = data.noticeList || []
          const goodsList = data.goodsList || []
          const promoList = data.promoList || []
          this.buildSwiper(bannerList)
          this.buildNavList(categoryTree)
          this.noticeList = Array.isArray(noticeList) ? noticeList : []
          this.goodsList = [...this.goodsList, ...(Array.isArray(goodsList) ? goodsList : [])]
          this.promoList = Array.isArray(promoList) ? promoList : []
          this.loadmore = this.goodsList.length >= (this.page.size || 10)
          console.log('[Home] 首页聚合成功', '轮播:', bannerList.length, '分类:', categoryTree.length, '商品:', this.goodsList.length, '推荐:', this.promoList.length)
        }).catch((err) => {
          console.warn('[Home] 首页聚合接口失败，回退到分接口', err)
          this._loadDataFallback(api)
        })
        return
      }
      this._loadDataFallback(api)
    },
    _loadDataFallback(api) {
      Promise.all([
        api.goodsPage(this.page),
        api.goodsPage({ searchCount: false, current: 1, size: 6 })
      ]).then(([resPage, resBanner]) => {
        const pageRecords = this._getRecords(resPage)
        if (!pageRecords.length && resPage) {
          try { console.warn('[Home] 商品分页返回空，请检查后端数据或响应格式。原始响应:', JSON.stringify(resPage).slice(0, 500)) } catch (e) {}
        }
        this.goodsList = [...this.goodsList, ...pageRecords]
        if (!pageRecords.length || pageRecords.length < this.page.size) this.loadmore = false
        this.buildSwiper(this._getRecords(resBanner))
        this.loadNotice()
        console.log('[Home] loadData 成功', '列表:', this.goodsList.length, '首页条数:', pageRecords.length)
      }).catch((err) => {
        console.error('[Home] loadData 请求失败', err)
        this.loadmore = false
      })
    },
    loadNotice() {
      const app = typeof getApp === 'function' ? getApp() : null
      const api = (app && app.api) || (app && app.globalData && app.globalData.__api) || apiModule
      if (!api || typeof api.getNoticeList !== 'function') return
      api.getNoticeList().then(res => {
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
      const app = getApp()
      const api = (app && app.api) || (app && app.globalData && app.globalData.__api) || apiModule
      if (!api || typeof api.goodsPage !== 'function') return
      api.goodsPage(this.page).then(res => {
        const list = this._getRecords(res)
        this.goodsList = [...this.goodsList, ...list]
        if (list.length < this.page.size) this.loadmore = false
      }).catch(() => {})
    },
    _getRecords(r) {
      if (!r) return []
      if (Array.isArray(r)) return r
      if (r.records && Array.isArray(r.records)) return r.records
      if (r.data && Array.isArray(r.data)) return r.data
      if (r.data && r.data.records && Array.isArray(r.data.records)) return r.data.records
      if (r.data && r.data.list && Array.isArray(r.data.list)) return r.data.list
      if (r.data && r.data.rows && Array.isArray(r.data.rows)) return r.data.rows
      if (r.data && r.data.content && Array.isArray(r.data.content)) return r.data.content
      if (r.data && typeof r.data === 'object' && r.data.data && Array.isArray(r.data.data)) return r.data.data
      if (r.list && Array.isArray(r.list)) return r.list
      if (r.rows && Array.isArray(r.rows)) return r.rows
      if (r.content && Array.isArray(r.content)) return r.content
      if (r.result && Array.isArray(r.result)) return r.result
      return []
    },
    signIn() {
      util.requireLogin('请先登录后再签到').then((ok) => {
        if (!ok) return
        const app = getApp()
        const api = (app && app.api) || (app && app.globalData && app.globalData.__api) || apiModule
        if (!api || typeof api.memberSignIn !== 'function') {
          uni.showToast({ title: '功能暂不可用', icon: 'none' })
          return
        }
        api.memberSignIn().then(res => {
          uni.showToast({ title: (res && res.msg) || '签到成功', icon: 'success' })
        }).catch(() => {
          uni.showToast({ title: '签到失败或今日已签到', icon: 'none' })
        })
      })
    },
    refresh() {
      this.loadmore = true
      this.page.current = 1
      this.goodsList = []
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
.margin-top-bar { margin-top: 88rpx; }
.swiper-wrap { margin: 0; padding-bottom: 0; }
.swiper-wrap .margin-top-sm { margin-top: 0; }
.adsec { width: calc(100% - 40rpx); display: flex; margin: 0 auto; padding: 10rpx 16rpx; height: 86rpx; align-items: center; border-radius: 16rpx; box-shadow: 0 8rpx 26rpx rgba(15,23,42,.06); }
.swiper_container { height: 80rpx; width: 95%; }
.screen-swiper { height: 250rpx; box-sizing: border-box; }
.swiper-image { width: 94% !important; height: 320rpx !important; margin: 10rpx auto 0 !important; border-radius: 20rpx; box-shadow: 0 8rpx 24rpx rgba(15,23,42,.08); display: block; }
.text-black { color: #000 !important; }
.search-form { border-radius: 20rpx; }
.home-four-actions { margin-top: -10rpx; padding: 12rpx 20rpx 24rpx; border-radius: 20rpx; margin-left: 20rpx; margin-right: 20rpx; box-shadow: 0 8rpx 28rpx rgba(15,23,42,.06); position: relative; z-index: 1; }
.section-title-bar { padding: 12rpx 0 8rpx; min-height: auto; }
.four-actions-grid { display: flex; flex-wrap: wrap; justify-content: space-between; }
.four-action-item { width: 25%; display: flex; flex-direction: column; align-items: center; padding: 6rpx 0; }
.four-action-icon-wrap { width: 96rpx; height: 96rpx; border-radius: 24rpx; display: flex; align-items: center; justify-content: center; font-size: 44rpx; color: #fff; margin-bottom: 10rpx; }
.four-action-icon-wrap.orange { background: linear-gradient(135deg, #ff9a56 0%, #ff6b35 100%); }
.four-action-icon-wrap.blue { background: linear-gradient(135deg, #5b9cf5 0%, #3b82f6 100%); }
.four-action-icon-wrap.red { background: linear-gradient(135deg, #f87171 0%, #ef4444 100%); }
.four-action-text { font-size: 24rpx; color: #374151; }
.ad-wrap { position: relative; z-index: 0; min-height: 80rpx; }
.login-banner-bottom-spacer { height: 140rpx; }
</style>
