<!--
  Copyright (C) 2018-2019 www.dingyangmall.com
-->
<template>
  <view class="page">
    <scroll-view scroll-x class="bg-white nav">
      <view class="flex text-center">
        <view v-for="(item, index) in tabList" :key="item.id" class="cu-item flex-sub" :class="{ 'text-red cur': index === TabCur }" @tap="tabSelect(index)">{{ item.name }}</view>
      </view>
    </scroll-view>
    <view class="cu-card article no-card margin-top">
      <view class="cu-item shadow" v-for="(item, i) in coupons" :key="i">
        <view class="content">
          <view class="desc">
            <view class="text-content padding-top-sm">
              <view class="flex justify-between align-center">
                <view class="text-bold text-xl">{{ item.goodsName || '通用券' }}</view>
                <view class="cu-tag line-red" v-if="item.couponStatus == 1">未使用</view>
                <view class="cu-tag line-gray" v-if="item.couponStatus == 2">已使用</view>
                <view class="cu-tag line-gray" v-if="item.couponStatus == 3">已过期</view>
              </view>
              <view class="margin-top-xs">券码：<text class="text-blue text-xxl text-bold" @tap="copyCode(item.couponCode)">{{ item.couponCode }}</text></view>
              <view class="margin-top-xs text-gray text-sm">有效期至：{{ item.validityEnd }}</view>
            </view>
          </view>
        </view>
        <view class="action padding-right padding-bottom" v-if="item.couponStatus == 1"><view class="text-xs text-gray">出示券码给商家核销</view></view>
      </view>
    </view>
    <view class="padding text-center text-gray margin-top" v-if="coupons.length == 0">暂无优惠券</view>
  </view>
</template>

<script>
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
    getApp().initPage().then(() => this.getCoupons())
  },
  methods: {
    tabSelect(index) {
      this.TabCur = index
      this.status = this.tabList[index].id
      this.coupons = []
      this.getCoupons()
    },
    getCoupons() {
      getApp().api.couponMy(this.status).then(res => {
        this.coupons = (res.code === 0 || res.code === 200) ? (res.data || []) : []
      })
    },
    copyCode(code) {
      uni.setClipboardData({ data: code || '', success: () => uni.showToast({ title: '已复制' }) })
    }
  }
}
</script>

<style scoped>
.page { min-height: 100vh; background: #f3f4f7; }
</style>
