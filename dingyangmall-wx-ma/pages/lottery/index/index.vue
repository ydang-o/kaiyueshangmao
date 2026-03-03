<!--
  Copyright (C) 2018-2019 www.dingyangmall.com
-->
<template>
  <view class="container tm-page">
    <view class="header">
      <view class="title">幸运大抽奖</view>
      <view class="sub-title" v-if="config.costPoints">消耗 {{ config.costPoints }} 积分/次</view>
      <view class="points">我的积分: {{ userInfo.points || 0 }}</view>
      <view class="limit" v-if="config.dailyLimit">每日限抽: {{ config.dailyLimit }} 次</view>
    </view>
    <view class="wheel-area">
      <button class="draw-btn" @tap="handleDraw" :disabled="loading">{{ loading ? '抽奖中...' : '立即抽奖' }}</button>
    </view>
    <view class="prize-list" v-if="config.prizeList && config.prizeList.length > 0">
      <view class="section-title">奖品列表</view>
      <view class="prize-grid">
        <view class="prize-item" v-for="(item, i) in config.prizeList" :key="i">
          <image :src="item.prizePic || '/static/img/no_pic.png'" mode="aspectFit" class="prize-img" />
          <view class="prize-name">{{ item.prizeName }}</view>
        </view>
      </view>
    </view>
    <view class="record-list">
      <view class="section-title">我的中奖记录</view>
      <view class="record-item" v-for="(item, i) in recordList" :key="i">
        <text class="time">{{ item.createTime }}</text>
        <text class="name">{{ item.prizeName }}</text>
        <text class="status">{{ item.grantStatus == '1' ? '已发放' : '待发放' }}</text>
      </view>
      <view class="empty" v-if="recordList.length === 0">暂无记录</view>
    </view>
  </view>
</template>

<script>
export default {
  name: 'LotteryPage',
  data() {
    return { userInfo: {}, config: {}, recordList: [], loading: false }
  },
  onShow() {
    getApp().initPage().then(() => {
      this.getUserInfo()
      this.getConfig()
      this.getRecords()
    })
  },
  methods: {
    getUserInfo() {
      getApp().api.memberInfo().then(res => { this.userInfo = res.data || {} })
    },
    getConfig() {
      getApp().api.lotteryConfig().then(res => { if (res.code === 0) this.config = res.data || {} })
    },
    getRecords() {
      getApp().api.lotteryRecord().then(res => { if (res.code === 0) this.recordList = res.data || [] })
    },
    handleDraw() {
      if (!this.config.id) { uni.showToast({ title: '活动未开启', icon: 'none' }); return }
      if ((this.userInfo.points || 0) < (this.config.costPoints || 0)) { uni.showToast({ title: '积分不足', icon: 'none' }); return }
      this.loading = true
      getApp().api.lotteryDraw().then(res => {
        this.loading = false
        if (res.code === 0) {
          const record = res.data
          if (record.isWin === '1') uni.showModal({ title: '恭喜中奖', content: '获得 ' + (record.prizeName || ''), showCancel: false })
          else uni.showToast({ title: '很遗憾未中奖', icon: 'none' })
          this.getUserInfo()
          this.getRecords()
        } else uni.showToast({ title: res.msg, icon: 'none' })
      }).catch(() => { this.loading = false })
    }
  }
}
</script>

<style scoped>
.container { padding: 40rpx; min-height: 100vh; }
.header { text-align: center; padding: 40rpx 0; }
.title { font-size: 44rpx; font-weight: bold; }
.sub-title { margin-top: 20rpx; color: #666; }
.points { margin-top: 20rpx; font-size: 32rpx; }
.limit { margin-top: 10rpx; font-size: 28rpx; color: #999; }
.wheel-area { text-align: center; padding: 40rpx 0; }
.draw-btn { background: linear-gradient(135deg, #ff2b56, #ff0036); color: #fff; width: 400rpx; height: 88rpx; line-height: 88rpx; border-radius: 44rpx; font-size: 32rpx; }
.section-title { font-size: 32rpx; font-weight: bold; margin-bottom: 20rpx; }
.prize-grid { display: flex; flex-wrap: wrap; }
.prize-item { width: 33.33%; padding: 20rpx; text-align: center; }
.prize-img { width: 120rpx; height: 120rpx; }
.prize-name { font-size: 24rpx; margin-top: 10rpx; }
.record-list { margin-top: 40rpx; }
.record-item { background: #fff; padding: 24rpx; margin-bottom: 20rpx; border-radius: 16rpx; display: flex; justify-content: space-between; box-shadow: 0 8rpx 24rpx rgba(15,23,42,.06); }
.empty { text-align: center; color: #999; padding: 40rpx; }
</style>
