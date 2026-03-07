<!--
  Copyright (C) 2018-2019 www.dingyangmall.com
-->
<template>
  <view class="page tm-page">
    <view class="bg-white nav tm-tab-bar">
      <view class="flex text-center">
        <view v-for="(item, index) in tabList" :key="item.id" class="cu-item flex-sub tm-tab-item" :class="{ 'tm-tab-active cur': index === TabCur }" hover-class="tm-tab-hover" @tap="tabSelect(index)">{{ item.name }}</view>
      </view>
    </view>
    <view class="cu-card article no-card margin-top">
      <view class="cu-item shadow coupon-card" v-for="(item, i) in coupons" :key="i">
        <view class="content flex align-start">
          <view class="desc flex-sub">
            <view class="text-content padding-top-sm">
              <view class="flex justify-between align-center">
                <view class="text-bold text-xl">{{ item.goodsName || '通用券' }}</view>
                <view class="cu-tag line-red" v-if="item.couponStatus == 1">未使用</view>
                <view class="cu-tag line-gray" v-if="item.couponStatus == 2">已使用</view>
                <view class="cu-tag line-gray" v-if="item.couponStatus == 3">已过期</view>
              </view>
              <view class="margin-top-xs text-gray text-sm">有效期至：{{ item.validityEnd }}</view>
              <view class="margin-top-xs text-gray text-sm">券码：<text class="tm-brand text-bold" @tap="copyCode(item.couponCode)">{{ item.couponCode }}</text>（点击复制）</view>
            </view>
          </view>
          <view class="coupon-qr-wrap" v-if="item.couponCode">
            <image class="coupon-qr" :src="qrUrl(item.couponCode)" mode="aspectFit" />
          </view>
        </view>
        <view class="action padding-right padding-bottom" v-if="item.couponStatus == 1"><view class="text-xs text-gray">出示二维码给商家扫码核销</view></view>
      </view>
    </view>
    <view class="padding text-center text-gray margin-top" v-if="coupons.length == 0">暂无优惠券</view>
  </view>
</template>

<script>
import util from '@/utils/util'
import apiModule from '@/utils/api'
export default {
  name: 'MyCouponsPage',
  data() {
    return {
      TabCur: 0,
      tabList: [{ id: 1, name: '未使用' }, { id: 2, name: '已使用' }, { id: 3, name: '已过期' }],
      coupons: [],
      status: 1
    }
  },
  onLoad() {
    util.requireLogin('请先登录后查看优惠券').then((ok) => {
      if (!ok) return
      const init = typeof getApp === 'function' && getApp() && getApp().initPage
      if (init && typeof init === 'function') {
        getApp().initPage().then(() => this.getCoupons()).catch(() => this.getCoupons())
      } else {
        this.getCoupons()
      }
    })
  },
  methods: {
    tabSelect(index) {
      this.TabCur = index
      this.status = this.tabList[index].id
      this.coupons = []
      this.getCoupons()
    },
    getApi() {
      const app = typeof getApp === 'function' ? getApp() : null
      return (app && app.api) || apiModule
    },
    getCoupons() {
      const api = this.getApi()
      const fn = (api && api.couponMyMa && typeof api.couponMyMa === 'function') ? api.couponMyMa : (api && api.couponMy)
      if (!fn) {
        this.coupons = []
        return
      }
      fn.call(api, this.status).then(res => {
        const ok = res && (res.code === 0 || res.code === 200 || res.code === '200' || res.code === '0')
        this.coupons = ok ? this._parseCouponList(res) : []
      }).catch(() => { this.coupons = [] })
    },
    _parseCouponList(res) {
      const data = res && (res.data !== undefined ? res.data : res)
      let list = []
      if (Array.isArray(data)) list = data
      else if (data && Array.isArray(data.records)) list = data.records
      else if (data && Array.isArray(data.list)) list = data.list
      else if (data && Array.isArray(data.rows)) list = data.rows
      else if (data && Array.isArray(data.content)) list = data.content
      return list.map(item => ({
        ...item,
        couponStatus: item.couponStatus != null ? item.couponStatus : item.status,
        couponCode: item.couponCode || item.code,
        validityEnd: item.validityEnd || item.endTime || item.validityEndTime,
        goodsName: item.goodsName != null ? item.goodsName : item.name
      }))
    },
    copyCode(code) {
      uni.setClipboardData({ data: code || '', success: () => uni.showToast({ title: '已复制' }) })
    },
    qrUrl(couponCode) {
      if (!couponCode) return ''
      return 'https://api.qrserver.com/v1/create-qr-code/?size=160x160&data=' + encodeURIComponent(couponCode)
    }
  }
}
</script>

<style scoped>
.page { min-height: 100vh; background: #f3f4f7; }
.tm-tab-bar { padding: 24rpx 0; }
.tm-tab-item { min-height: 80rpx; line-height: 80rpx; }
.tm-tab-hover { opacity: 0.8; background-color: rgba(0,0,0,0.04); }
.coupon-card .content { display: flex; align-items: flex-start; }
.coupon-qr-wrap { flex-shrink: 0; padding: 16rpx 0 16rpx 24rpx; display: flex; align-items: center; justify-content: center; }
.coupon-qr { width: 160rpx; height: 160rpx; background: #fff; border: 1rpx solid #eee; border-radius: 12rpx; padding: 8rpx; box-sizing: border-box; }
</style>
