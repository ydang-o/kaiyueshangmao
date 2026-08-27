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
      <button class="draw-btn" @tap="handleDraw" :disabled="loading">
        <text v-if="!loading">立即抽奖</text>
        <view v-else class="draw-loading">
          <view class="draw-spinner"></view>
          <text>抽奖中...</text>
        </view>
      </button>
    </view>
    <!-- 抽奖结果弹窗：过渡动画 + 展示中了多少 -->
    <view v-if="showResultModal" class="lottery-result-mask" @tap="closeResultModal">
      <view class="lottery-result-modal" :class="{ 'lottery-result-enter': resultEnter }" @tap.stop>
        <view class="lottery-result-icon" :class="lastResult && lastResult.isWin === '1' ? 'win' : 'lose'">
          <text v-if="lastResult && lastResult.isWin === '1'" class="cuIcon-roundcheckfill"></text>
          <text v-else class="cuIcon-roundclosefill"></text>
        </view>
        <view class="lottery-result-title">{{ lastResult && lastResult.isWin === '1' ? '恭喜中奖' : '很遗憾' }}</view>
        <view class="lottery-result-content" v-if="lastResult && lastResult.isWin === '1'">
          <text class="prize-name">获得 {{ lastResult.prizeName || '奖品' }}</text>
          <text v-if="(lastResult.prizePoints || lastResult.points) != null" class="prize-extra">+{{ lastResult.prizePoints || lastResult.points }} 积分</text>
          <text v-if="(lastResult.prizeCount || lastResult.quantity || lastResult.num) > 1" class="prize-extra"> x{{ lastResult.prizeCount || lastResult.quantity || lastResult.num }}</text>
        </view>
        <view class="lottery-result-content" v-else>未中奖，下次再来</view>
        <view class="lottery-result-btn" @tap="closeResultModal">确定</view>
      </view>
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
import util from '@/utils/util'
import apiModule from '@/utils/api'
export default {
  name: 'LotteryPage',
  data() {
    return {
      userInfo: {},
      config: {},
      recordList: [],
      loading: false,
      recordPage: { pageNum: 1, pageSize: 20 },
      showResultModal: false,
      resultEnter: false,
      lastResult: null
    }
  },
  onShow() {
    util.requireLogin('请先登录后参与积分抽奖').then((ok) => {
      if (!ok) return
      getApp().initPage().then(() => {
        this.getUserInfo()
        this.getConfig()
        this.getRecords()
      })
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
    getUserInfo() {
      const api = this.getApi()
      const fn = (api && api.wxUserGet) || (api && api.memberInfo)
      if (typeof fn !== 'function') return
      try {
        fn.call(api).then(res => { this.userInfo = (res && res.data) || res || {} }).catch(() => {})
      } catch (e) {}
    },
    getConfig() {
      const api = this.getApi()
      if (!api || typeof api.lotteryConfig !== 'function') return
      api.lotteryConfig().then(res => {
        if (res && (res.code === 0 || res.code === 200)) this.config = res.data || {}
      }).catch(() => {})
    },
    getRecords() {
      const api = this.getApi()
      if (!api || typeof api.lotteryRecord !== 'function') return
      api.lotteryRecord(this.recordPage).then(res => {
        if (res && res.code !== 0 && res.code !== 200) return
        const data = (res && res.data) || res || {}
        const list = Array.isArray(data) ? data : (data.records || data.list || data.rows || [])
        this.recordList = list
      }).catch(() => { this.recordList = [] })
    },
    handleDraw() {
      if (!this.config.id) { uni.showToast({ title: '活动未开启', icon: 'none' }); return }
      if ((this.userInfo.points || 0) < (this.config.costPoints || 0)) { uni.showToast({ title: '积分不足', icon: 'none' }); return }
      const api = this.getApi()
      if (!api || typeof api.lotteryDraw !== 'function') {
        uni.showToast({ title: '接口未就绪', icon: 'none' })
        return
      }
      this.loading = true
      api.lotteryDraw().then(res => {
        this.loading = false
        if (res && (res.code === 0 || res.code === 200)) {
          const record = res.data || {}
          this.lastResult = record
          this.showResultModal = true
          setTimeout(() => { this.resultEnter = true }, 50)
          this.getUserInfo()
          this.getRecords()
        } else {
          uni.showToast({ title: (res && res.msg) || '抽奖失败', icon: 'none' })
        }
      }).catch(() => { this.loading = false })
    },
    closeResultModal() {
      this.resultEnter = false
      this.showResultModal = false
      this.lastResult = null
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
.draw-loading { display: flex; align-items: center; justify-content: center; gap: 16rpx; }
.draw-spinner { width: 36rpx; height: 36rpx; border: 4rpx solid rgba(255,255,255,.3); border-top-color: #fff; border-radius: 50%; animation: spin 0.8s linear infinite; }
@keyframes spin { to { transform: rotate(360deg); } }
.lottery-result-mask { position: fixed; inset: 0; background: rgba(0,0,0,.5); display: flex; align-items: center; justify-content: center; z-index: 999; }
.lottery-result-modal { width: 560rpx; background: #fff; border-radius: 24rpx; padding: 48rpx; text-align: center; transform: scale(0.8); opacity: 0; transition: transform 0.3s ease, opacity 0.3s ease; }
.lottery-result-modal.lottery-result-enter { transform: scale(1); opacity: 1; }
.lottery-result-icon { font-size: 120rpx; margin-bottom: 24rpx; }
.lottery-result-icon.win { color: #ff0036; }
.lottery-result-icon.lose { color: #999; }
.lottery-result-title { font-size: 40rpx; font-weight: bold; margin-bottom: 20rpx; }
.lottery-result-content { font-size: 30rpx; color: #666; line-height: 1.6; }
.lottery-result-content .prize-name { display: block; font-size: 36rpx; font-weight: bold; color: #ff0036; margin-bottom: 8rpx; }
.lottery-result-content .prize-extra { font-size: 28rpx; color: #999; }
.lottery-result-btn { margin-top: 40rpx; padding: 24rpx; background: linear-gradient(135deg, #ff2b56, #ff0036); color: #fff; border-radius: 44rpx; font-size: 32rpx; }
</style>
