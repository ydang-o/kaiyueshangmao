<!--
  Copyright (C) 2018-2019 www.dingyangmall.com
  积分商品详情页
-->
<template>
  <view class="page tm-page" v-if="goodsDetail">
    <!-- 商品图片 -->
    <view class="goods-swiper">
      <swiper class="screen-swiper" indicator-dots circular autoplay :interval="5000" :duration="500">
        <swiper-item v-for="(pic, i) in (goodsDetail.picUrls || [])" :key="i">
          <image class="swiper-image" :src="$imgUrl(pic)" mode="aspectFill" />
        </swiper-item>
      </swiper>
      <view class="page-index">{{ currentSwiper }}/{{ (goodsDetail.picUrls && goodsDetail.picUrls.length) || 1 }}</view>
    </view>

    <!-- 商品信息 -->
    <view class="goods-info bg-white padding">
      <view class="integral-price">
        <text class="integral-num">{{ goodsDetail.salesPrice }}</text>
        <text class="integral-unit">积分</text>
      </view>
      <view class="goods-name text-xl text-bold margin-top-sm">{{ goodsDetail.name }}</view>
      <view class="goods-desc text-gray margin-top-sm" v-if="goodsDetail.description">{{ goodsDetail.description }}</view>
      <view class="goods-stock text-gray margin-top-sm">
        <text>库存：{{ goodsDetail.stock || 0 }}件</text>
        <text class="margin-left">已兑换：{{ goodsDetail.salesNum || 0 }}件</text>
      </view>
    </view>

    <!-- 商品详情 -->
    <view class="goods-detail bg-white margin-top-sm">
      <view class="cu-bar">
        <view class="action">
          <text class="cuIcon-titles text-red"></text>
          <text>商品详情</text>
        </view>
      </view>
      <view class="padding">
        <rich-text :nodes="goodsDetail.detail || goodsDetail.content || '暂无详情'"></rich-text>
      </view>
    </view>

    <!-- 兑换须知 -->
    <view class="exchange-notice bg-white margin-top-sm">
      <view class="cu-bar">
        <view class="action">
          <text class="cuIcon-titles text-blue"></text>
          <text>兑换须知</text>
        </view>
      </view>
      <view class="padding text-gray">
        <view class="notice-item">1. 请确认您的积分余额充足</view>
        <view class="notice-item margin-top-xs">2. 兑换成功后不支持退换</view>
        <view class="notice-item margin-top-xs">3. 实物商品将在3个工作日内发货</view>
        <view class="notice-item margin-top-xs">4. 虚拟商品将自动发放到账户</view>
      </view>
    </view>

    <!-- 底部操作栏 -->
    <view class="cu-bar bg-white tabbar border foot">
      <view class="integral-info">
        <text class="text-gray">我的积分：</text>
        <text class="integral-num">{{ userInfo.points || 0 }}</text>
      </view>
      <view class="btn-group">
        <button
          class="cu-btn tm-primary-btn exchange-btn"
          :disabled="(userInfo.points || 0) < goodsDetail.salesPrice || (goodsDetail.stock || 0) <= 0"
          @tap="showExchangeModal"
        >
          {{ (goodsDetail.stock || 0) <= 0 ? '已兑完' : '立即兑换' }}
        </button>
      </view>
    </view>

    <!-- 兑换弹窗 -->
    <view class="cu-modal" :class="showModal ? 'show' : ''">
      <view class="cu-dialog">
        <view class="cu-bar bg-white justify-end">
          <view class="content">确认兑换</view>
          <view class="action" @tap="hideExchange">
            <text class="cuIcon-close text-red"></text>
          </view>
        </view>
        <view class="padding-xl">
          <view class="exchange-info">
            <text>商品：</text>
            <text class="text-bold">{{ goodsDetail.name }}</text>
          </view>
          <view class="exchange-info margin-top">
            <text>所需积分：</text>
            <text class="integral-num">{{ goodsDetail.salesPrice }}</text>
          </view>
          <view class="exchange-info margin-top">
            <text>当前积分：</text>
            <text :class="(userInfo.points || 0) >= goodsDetail.salesPrice ? 'text-green' : 'text-red'">{{ userInfo.points || 0 }}</text>
          </view>
          <!-- 收货地址选择 -->
          <view class="address-section margin-top" v-if="goodsDetail.type === 'physical'">
            <view class="section-title">选择收货地址</view>
            <view class="address-card" v-if="selectedAddress" @tap="selectAddress">
              <view class="address-info">
                <view class="user-info">
                  <text class="name">{{ selectedAddress.userName }}</text>
                  <text class="phone">{{ selectedAddress.telNum }}</text>
                </view>
                <view class="address-text">{{ selectedAddress.address }}</view>
              </view>
              <text class="cuIcon-right text-gray"></text>
            </view>
            <view class="no-address" v-else @tap="selectAddress">
              <text class="cuIcon-add text-green"></text>
              <text>请选择收货地址</text>
            </view>
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
  name: 'IntegralShopDetailPage',
  data() {
    return {
      id: '',
      goodsDetail: null,
      userInfo: {},
      currentSwiper: 1,
      showModal: false,
      exchanging: false,
      selectedAddress: null
    }
  },
  onLoad(options) {
    if (options.id) {
      this.id = options.id
      this.loadDetail()
    }
    util.requireLogin('请先登录').then((ok) => {
      if (ok) {
        getApp().initPage().then(() => {
          this.getUserInfo()
          this.loadDefaultAddress()
        })
      }
    })
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
    loadDetail() {
      const api = this.getApi()
      if (!api || typeof api.integralGoodsGet !== 'function') return
      api.integralGoodsGet(this.id).then(res => {
        if (res && (res.code === 0 || res.code === 200)) {
          this.goodsDetail = res.data || {}
        }
      }).catch(() => {})
    },
    getUserInfo() {
      const api = this.getApi()
      const fn = (api && api.wxUserGet) || (api && api.memberInfo)
      if (typeof fn !== 'function') return
      try {
        fn.call(api).then(res => {
          this.userInfo = (res && res.data) || res || {}
        }).catch(() => {})
      } catch (e) {}
    },
    loadDefaultAddress() {
      const api = this.getApi()
      if (!api || typeof api.userAddressPage !== 'function') return
      api.userAddressPage({ current: 1, size: 1 }).then(res => {
        if (res && (res.code === 0 || res.code === 200)) {
          const data = res.data || {}
          const list = data.records || data.list || data.rows || []
          if (list.length > 0) {
            this.selectedAddress = list[0]
          }
        }
      }).catch(() => {})
    },
    selectAddress() {
      uni.navigateTo({
        url: '/pages/user/user-address/list/index?select=true',
        events: {
          selectAddress: (address) => {
            this.selectedAddress = address
          }
        }
      })
    },
    showExchangeModal() {
      if ((this.userInfo.points || 0) < this.goodsDetail.salesPrice) {
        uni.showToast({ title: '积分不足', icon: 'none' })
        return
      }
      if ((this.goodsDetail.stock || 0) <= 0) {
        uni.showToast({ title: '商品已兑完', icon: 'none' })
        return
      }
      this.showModal = true
    },
    hideExchange() {
      this.showModal = false
    },
    confirmExchange() {
      if (this.goodsDetail.type === 'physical' && !this.selectedAddress) {
        uni.showToast({ title: '请选择收货地址', icon: 'none' })
        return
      }
      if (this.exchanging) return
      const api = this.getApi()
      if (!api || typeof api.integralExchange !== 'function') {
        uni.showToast({ title: '接口未就绪', icon: 'none' })
        return
      }
      this.exchanging = true
      // 使用积分订单接口，传入商品信息
      const params = {
        spuId: this.id,
        quantity: 1,
        payType: 2, // 积分支付
        integralAmount: this.goodsDetail.salesPrice
      }
      if (this.selectedAddress) {
        params.addressId = this.selectedAddress.id
      }
      api.integralExchange(params).then(res => {
        this.exchanging = false
        console.log('[IntegralShopDetail] 兑换响应:', res)
        if (res && (res.code === 0 || res.code === 200)) {
          uni.showModal({
            title: '兑换成功',
            content: '您已成功兑换该商品，可在"我的兑换"中查看订单',
            showCancel: false,
            success: () => {
              this.hideExchange()
              uni.navigateTo({ url: '/pages/integral/shop/order/index' })
            }
          })
        } else {
          const msg = res && (res.msg || res.message) || '兑换失败'
          console.error('[IntegralShopDetail] 兑换失败:', msg, res)
          uni.showToast({ title: msg, icon: 'none' })
        }
      }).catch((err) => {
        this.exchanging = false
        console.error('[IntegralShopDetail] 兑换异常:', err)
        uni.showToast({ title: '兑换失败', icon: 'none' })
      })
    }
  }
}
</script>

<style scoped>
.goods-swiper {
  position: relative;
}
.swiper-image {
  width: 100%;
  height: 600rpx;
}
.page-index {
  position: absolute;
  bottom: 20rpx;
  right: 20rpx;
  background: rgba(0,0,0,0.5);
  color: #fff;
  padding: 6rpx 16rpx;
  border-radius: 20rpx;
  font-size: 24rpx;
}

.goods-info {
  border-bottom: 1rpx solid #f0f0f0;
}
.integral-price {
  display: flex;
  align-items: baseline;
}
.integral-num {
  font-size: 48rpx;
  font-weight: bold;
  color: #ff6b6b;
}
.integral-unit {
  font-size: 28rpx;
  color: #ff6b6b;
  margin-left: 8rpx;
}
.goods-stock {
  font-size: 26rpx;
}

.goods-detail, .exchange-notice {
  padding-bottom: 20rpx;
}
.notice-item {
  font-size: 26rpx;
  line-height: 1.6;
}

.cu-bar.foot {
  padding: 0 30rpx;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.integral-info .integral-num {
  font-size: 32rpx;
}
.exchange-btn {
  padding: 0 60rpx;
  height: 72rpx;
  line-height: 72rpx;
  font-size: 30rpx;
}
.exchange-btn[disabled] {
  background: #ccc;
  color: #fff;
}

.exchange-info {
  font-size: 28rpx;
  color: #666;
}
.exchange-info .integral-num {
  font-size: 32rpx;
}

.address-section {
  text-align: left;
  border-top: 1rpx solid #f0f0f0;
  padding-top: 20rpx;
}
.section-title {
  font-size: 28rpx;
  color: #333;
  margin-bottom: 16rpx;
}
.address-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #f8f8f8;
  padding: 20rpx;
  border-radius: 8rpx;
}
.user-info {
  margin-bottom: 10rpx;
}
.user-info .name {
  font-size: 30rpx;
  font-weight: bold;
  color: #333;
  margin-right: 20rpx;
}
.user-info .phone {
  font-size: 26rpx;
  color: #666;
}
.address-text {
  font-size: 26rpx;
  color: #666;
  line-height: 1.4;
}
.no-address {
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f8f8f8;
  padding: 30rpx;
  border-radius: 8rpx;
  color: #666;
  font-size: 28rpx;
}
.no-address text:first-child {
  margin-right: 10rpx;
  font-size: 32rpx;
}
</style>
