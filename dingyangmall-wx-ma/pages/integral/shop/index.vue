<!--
  Copyright (C) 2018-2019 www.dingyangmall.com
  积分兑换商城：展示可用积分兑换的商品列表
-->
<template>
  <view class="page tm-page">
    <!-- 顶部积分信息栏 -->
    <view class="integral-header bg-white">
      <view class="integral-info">
        <view class="integral-label">我的积分</view>
        <view class="integral-value">{{ userInfo.points || 0 }}</view>
        <view class="integral-tip" v-if="(userInfo.points || 0) === 0">
          <text class="cuIcon-info text-gray"></text>
          <text class="text-gray text-sm">每日签到可获得积分</text>
        </view>
      </view>
      <view class="integral-rules" @tap="showRules">
        <text class="cuIcon-questionfill"></text>
        <text>兑换规则</text>
      </view>
    </view>

    <!-- 商品分类标签 -->
    <view class="category-tabs bg-white">
      <scroll-view scroll-x class="nav" scroll-with-animation>
        <view class="flex text-center">
          <view
            v-for="(item, index) in categories"
            :key="index"
            class="cu-item flex-sub"
            :class="{ 'tm-tab-active cur': currentCategory === index }"
            @tap="selectCategory(index)"
          >
            {{ item.name }}
          </view>
        </view>
      </scroll-view>
    </view>

    <!-- 商品列表 -->
    <view class="goods-list bg-white margin-top-sm">
      <view class="goods-grid">
        <view
          class="goods-item"
          v-for="(item, index) in goodsList"
          :key="index"
          @tap="goDetail(item)"
        >
          <image class="goods-img" :src="$imgUrl(item.picUrls && item.picUrls[0])" mode="aspectFill" @error="onImageError($event, index)" />
          <view class="goods-img-placeholder" v-if="!item.picUrls || !item.picUrls[0] || item.imgError">
            <text class="cuIcon-goods text-gray"></text>
          </view>
          <view class="goods-info">
            <view class="goods-name overflow-2">{{ item.name }}</view>
            <view class="goods-desc overflow-1" v-if="item.description">{{ item.description }}</view>
            <view class="goods-bottom">
              <view class="integral-price">
                <text class="integral-num">{{ item.salesPrice }}</text>
                <text class="integral-unit">积分</text>
              </view>
              <button
                class="cu-btn sm tm-primary-btn exchange-btn"
                :disabled="(userInfo.points || 0) < item.salesPrice || item.stock <= 0"
                @tap.stop="exchange(item)"
              >
                {{ item.stock <= 0 ? '已兑完' : '兑换' }}
              </button>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 空状态 -->
    <view class="empty-state" v-if="goodsList.length === 0 && !loading">
      <view class="empty-icon">
        <text class="cuIcon-goods text-gray"></text>
      </view>
      <text class="empty-text">暂无兑换商品</text>
      <text class="empty-subtext">敬请期待更多积分好物</text>
    </view>

    <!-- 加载更多 -->
    <view class="cu-load bg-white" :class="loadmore ? 'loading' : 'over'" v-if="goodsList.length > 0"></view>

    <!-- 兑换规则弹窗 -->
    <view class="cu-modal" :class="showRulesModal ? 'show' : ''">
      <view class="cu-dialog">
        <view class="cu-bar bg-white justify-end">
          <view class="content">兑换规则</view>
          <view class="action" @tap="hideRules">
            <text class="cuIcon-close text-red"></text>
          </view>
        </view>
        <view class="padding-xl text-left">
          <view class="rule-item">1. 积分可通过每日签到、购物消费、参与活动等方式获得</view>
          <view class="rule-item margin-top-sm">2. 积分兑换的商品不支持退换货</view>
          <view class="rule-item margin-top-sm">3. 兑换成功后，商品将在3个工作日内发货</view>
          <view class="rule-item margin-top-sm">4. 积分一经兑换，不予退还</view>
          <view class="rule-item margin-top-sm">5. 如有疑问，请联系客服</view>
        </view>
        <view class="cu-bar bg-white justify-end">
          <view class="action">
            <button class="cu-btn bg-green" @tap="hideRules">我知道了</button>
          </view>
        </view>
      </view>
    </view>

    <!-- 兑换确认弹窗 -->
    <view class="cu-modal" :class="showExchangeModal ? 'show' : ''">
      <view class="cu-dialog">
        <view class="cu-bar bg-white justify-end">
          <view class="content">确认兑换</view>
          <view class="action" @tap="hideExchange">
            <text class="cuIcon-close text-red"></text>
          </view>
        </view>
        <view class="padding-xl">
          <view class="exchange-goods">
            <image class="exchange-img" :src="$imgUrl(selectedGoods.picUrls && selectedGoods.picUrls[0])" mode="aspectFill" />
            <view class="exchange-img-placeholder" v-if="!selectedGoods.picUrls || !selectedGoods.picUrls[0]">
              <text class="cuIcon-goods text-gray"></text>
            </view>
            <view class="exchange-info">
              <view class="exchange-name">{{ selectedGoods.name }}</view>
              <view class="exchange-price">
                <text>需支付：</text>
                <text class="integral-num">{{ selectedGoods.salesPrice }}</text>
                <text class="integral-unit">积分</text>
              </view>
            </view>
          </view>
          <view class="exchange-balance margin-top">
            <text>当前积分：</text>
            <text :class="(userInfo.points || 0) >= selectedGoods.salesPrice ? 'text-green' : 'text-red'">{{ userInfo.points || 0 }}</text>
          </view>
        </view>
        <view class="cu-bar bg-white justify-end">
          <view class="action">
            <button class="cu-btn line-green text-green" @tap="hideExchange">取消</button>
            <button class="cu-btn bg-green margin-left" @tap="confirmExchange" :loading="exchanging">确认兑换</button>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import util from '@/utils/util'
import apiModule from '@/utils/api'

export default {
  name: 'IntegralShopPage',
  data() {
    return {
      userInfo: {},
      categories: [
        { name: '全部', id: '' },
        { name: '实物商品', id: 'physical' },
        { name: '虚拟商品', id: 'virtual' },
        { name: '优惠券', id: 'coupon' }
      ],
      currentCategory: 0,
      goodsList: [],
      page: { current: 1, size: 10 },
      loadmore: true,
      loading: false,
      showRulesModal: false,
      showExchangeModal: false,
      selectedGoods: {},
      exchanging: false
    }
  },
  onLoad() {
    util.requireLogin('请先登录后查看积分商城').then((ok) => {
      if (!ok) return
      getApp().initPage().then(() => {
        this.getUserInfo()
        this.loadGoods()
      })
    })
  },
  onReachBottom() {
    if (this.loadmore) {
      this.page.current++
      this.loadGoods()
    }
  },
  onPullDownRefresh() {
    this.refresh()
    uni.stopPullDownRefresh()
  },
  methods: {
    getApi() {
      try {
        const app = typeof getApp === 'function' ? getApp() : null
        const fromApp = app && app.api && typeof app.api === 'object'
        return (fromApp ? app.api : null) || (apiModule && typeof apiModule === 'object' ? apiModule : null) || {}
      } catch (e) {
        return apiModule || {}
      }
    },
    getUserInfo() {
      const api = this.getApi()
      const fn = (api && api.wxUserGet) || (api && api.memberInfo)
      if (typeof fn !== 'function') {
        console.warn('[IntegralShop] 用户信息接口未就绪')
        return
      }
      try {
        fn.call(api).then(res => {
          const data = (res && res.data) || res || {}
          // 积分可能存储在不同字段：points、integral、score
          data.points = data.points || data.integral || data.score || 0
          this.userInfo = data
          console.log('[IntegralShop] 用户信息:', this.userInfo)
          // 如果用户信息中没有积分，尝试单独获取
          if (!data.points && api && typeof api.getUserPoints === 'function') {
            this.loadUserPoints()
          }
        }).catch((err) => {
          console.error('[IntegralShop] 获取用户信息失败:', err)
        })
      } catch (e) {
        console.error('[IntegralShop] 获取用户信息异常:', e)
      }
    },
    loadUserPoints() {
      const api = this.getApi()
      if (!api || typeof api.getUserPoints !== 'function') return
      api.getUserPoints().then(res => {
        if (res && (res.code === 0 || res.code === 200)) {
          const data = res.data || res || {}
          const points = data.points || data.integral || data.score || 0
          this.userInfo = { ...this.userInfo, points }
          console.log('[IntegralShop] 积分余额:', points)
        }
      }).catch(() => {
        // 接口调用失败，静默处理
        console.log('[IntegralShop] 积分接口调用失败')
      })
    },
    selectCategory(index) {
      this.currentCategory = index
      this.refresh()
    },
    refresh() {
      this.page.current = 1
      this.goodsList = []
      this.loadmore = true
      this.loadGoods()
    },
    loadGoods() {
      if (this.loading || !this.loadmore) return
      this.loading = true
      const api = this.getApi()
      if (!api || typeof api.integralGoodsPage !== 'function') {
        this.loading = false
        this.loadmore = false
        return
      }
      const params = {
        ...this.page,
        categoryId: this.categories[this.currentCategory].id
      }
      api.integralGoodsPage(params).then(res => {
        this.loading = false
        if (res && (res.code === 0 || res.code === 200)) {
          const data = res.data || {}
          const list = data.records || data.list || data.rows || []
          if (list.length < this.page.size) {
            this.loadmore = false
          }
          this.goodsList = this.page.current === 1 ? list : [...this.goodsList, ...list]
        } else {
          // 接口返回错误，显示空状态
          this.loadmore = false
        }
      }).catch(() => {
        this.loading = false
        this.loadmore = false
        // 接口调用失败，显示空状态
      })
    },
    goDetail(item) {
      uni.navigateTo({
        url: '/pages/integral/shop/detail/index?id=' + item.id
      })
    },
    onImageError(e, index) {
      // 图片加载失败时，设置标记显示占位符
      if (this.goodsList[index]) {
        this.$set(this.goodsList[index], 'imgError', true)
      }
    },
    exchange(item) {
      if ((this.userInfo.points || 0) < item.salesPrice) {
        uni.showToast({ title: '积分不足', icon: 'none' })
        return
      }
      if (item.stock <= 0) {
        uni.showToast({ title: '商品已兑完', icon: 'none' })
        return
      }
      this.selectedGoods = item
      this.showExchangeModal = true
    },
    showRules() {
      this.showRulesModal = true
    },
    hideRules() {
      this.showRulesModal = false
    },
    hideExchange() {
      this.showExchangeModal = false
      this.selectedGoods = {}
    },
    confirmExchange() {
      if (this.exchanging) return
      const api = this.getApi()
      if (!api || typeof api.integralExchange !== 'function') {
        uni.showToast({ title: '接口未就绪', icon: 'none' })
        return
      }
      this.exchanging = true
      // 使用积分订单接口，传入商品信息
      api.integralExchange({
        spuId: this.selectedGoods.id,
        quantity: 1,
        payType: 2, // 积分支付
        integralAmount: this.selectedGoods.salesPrice
      }).then(res => {
        this.exchanging = false
        console.log('[IntegralShop] 兑换响应:', res)
        if (res && (res.code === 0 || res.code === 200)) {
          uni.showModal({
            title: '兑换成功',
            content: '您已成功兑换该商品，可在"我的兑换"中查看',
            showCancel: false,
            success: () => {
              this.hideExchange()
              this.getUserInfo()
              this.refresh()
            }
          })
        } else {
          const msg = res && (res.msg || res.message) || '兑换失败'
          console.error('[IntegralShop] 兑换失败:', msg, res)
          uni.showToast({ title: msg, icon: 'none' })
        }
      }).catch((err) => {
        this.exchanging = false
        console.error('[IntegralShop] 兑换异常:', err)
        uni.showToast({ title: '兑换失败', icon: 'none' })
      })
    }
  }
}
</script>

<style scoped>
.integral-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx;
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%);
  color: #fff;
}
.integral-info {
  display: flex;
  flex-direction: column;
}
.integral-label {
  font-size: 24rpx;
  opacity: 0.9;
}
.integral-value {
  font-size: 56rpx;
  font-weight: bold;
  margin-top: 10rpx;
}
.integral-tip {
  display: flex;
  align-items: center;
  margin-top: 10rpx;
  opacity: 0.8;
}
.integral-tip text:first-child {
  margin-right: 6rpx;
  font-size: 24rpx;
}
.integral-rules {
  display: flex;
  align-items: center;
  font-size: 24rpx;
  opacity: 0.9;
}
.integral-rules text:first-child {
  margin-right: 6rpx;
}

.category-tabs {
  border-bottom: 1rpx solid #f0f0f0;
}
.category-tabs .cu-item {
  padding: 20rpx 30rpx;
  font-size: 28rpx;
  color: #666;
}
.category-tabs .cu-item.tm-tab-active {
  color: #ff6b6b;
  font-weight: bold;
}

.goods-list {
  padding: 20rpx;
}
.goods-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20rpx;
}
.goods-item {
  background: #fff;
  border-radius: 12rpx;
  overflow: hidden;
  box-shadow: 0 2rpx 12rpx rgba(0,0,0,0.08);
}
.goods-img {
  width: 100%;
  height: 300rpx;
}
.goods-img-placeholder {
  width: 100%;
  height: 300rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f5f5;
  margin-top: -300rpx;
}
.goods-img-placeholder text {
  font-size: 80rpx;
  opacity: 0.3;
}
.goods-info {
  padding: 20rpx;
}
.goods-name {
  font-size: 28rpx;
  color: #333;
  line-height: 1.4;
  min-height: 80rpx;
}
.goods-desc {
  font-size: 24rpx;
  color: #999;
  margin-top: 10rpx;
}
.goods-bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 20rpx;
}
.integral-price {
  display: flex;
  align-items: baseline;
}
.integral-num {
  font-size: 36rpx;
  font-weight: bold;
  color: #ff6b6b;
}
.integral-unit {
  font-size: 24rpx;
  color: #ff6b6b;
  margin-left: 4rpx;
}
.exchange-btn {
  padding: 0 30rpx;
  height: 56rpx;
  line-height: 56rpx;
  font-size: 26rpx;
}
.exchange-btn[disabled] {
  background: #ccc;
  color: #fff;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 100rpx 0;
}
.empty-icon {
  width: 200rpx;
  height: 200rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f5f5;
  border-radius: 50%;
}
.empty-icon text {
  font-size: 100rpx;
  opacity: 0.3;
}
.empty-text {
  font-size: 30rpx;
  color: #666;
  margin-top: 40rpx;
  font-weight: bold;
}
.empty-subtext {
  font-size: 26rpx;
  color: #999;
  margin-top: 16rpx;
}

.rule-item {
  font-size: 28rpx;
  color: #666;
  line-height: 1.6;
}

.exchange-goods {
  display: flex;
  align-items: center;
}
.exchange-img {
  width: 120rpx;
  height: 120rpx;
  border-radius: 8rpx;
  margin-right: 20rpx;
}
.exchange-img-placeholder {
  width: 120rpx;
  height: 120rpx;
  border-radius: 8rpx;
  margin-right: 20rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f5f5;
}
.exchange-img-placeholder text {
  font-size: 60rpx;
  opacity: 0.3;
}
.exchange-info {
  flex: 1;
  text-align: left;
}
.exchange-name {
  font-size: 28rpx;
  color: #333;
  margin-bottom: 10rpx;
}
.exchange-price {
  font-size: 26rpx;
  color: #666;
}
.exchange-balance {
  font-size: 28rpx;
  color: #333;
}
</style>
