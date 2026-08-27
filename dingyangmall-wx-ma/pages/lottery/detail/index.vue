<template>
  <view class="container tm-page" v-if="activity">
    <view class="activity-banner"><image class="banner-image" :src="getBannerImage()" mode="aspectFill" /></view>
    <view class="activity-info bg-white padding">
      <view class="info-title text-xl text-bold">{{ getActivityTitle() }}</view>
      <view class="info-desc text-gray margin-top-sm">{{ getActivityDesc() }}</view>
      <view class="info-meta flex margin-top"><text class="margin-right">消耗 {{ activity.costPoints || 0 }} 积分</text><text v-if="activity.dailyLimit">每日限 {{ activity.dailyLimit }} 次</text></view>
    </view>
    <view class="my-points bg-white margin-top-sm padding">我的积分：<text class="text-red text-bold">{{ userInfo.points || 0 }}</text></view>
    <view class="prizes-section padding bg-white margin-top-sm"><view class="section-title text-center text-lg text-bold">奖品列表</view><view class="prizes-grid flex flex-wrap"><view class="prize-card" v-for="(item, i) in (activity.prizeList || [])" :key="i"><image class="prize-image" :src="item.prizePic || '/static/img/no_pic.png'" mode="aspectFit" /><view class="prize-name text-center">{{ item.prizeName || '奖品' }}</view><view class="text-gray text-center text-sm">{{ item.probability || '' }}</view></view></view></view>
    <view class="draw-section padding bg-white margin-top-sm text-center"><button class="cu-btn bg-red lg" :disabled="loading" @tap="handleDraw">{{ loading ? '抽奖中...' : '立即抽奖（' + (activity.costPoints || 0) + '积分）' }}</button></view>
    <view v-if="showResultModal" class="result-modal" @tap="hideResultModal"><view class="result-modal-mask"></view><view class="result-modal-content bg-white radius padding-xl text-center" @tap.stop><view class="text-xxl text-red text-bold">{{ resultTitle }}</view><view v-if="resultPrize" class="margin-top"><image class="result-image" :src="resultPrize.prizePic || resultPrize.image || '/static/img/no_pic.png'" mode="aspectFit" /><view class="text-lg">{{ resultPrize.prizeName }}</view></view><view v-if="resultPoints > 0" class="text-orange margin-top">获得 {{ resultPoints }} 积分</view><button class="cu-btn bg-red margin-top-xl" @tap="hideResultModal">确定</button></view></view>
  </view>
</template>
<script>
import { fullImageUrl } from '@/utils/imageUrl'
import util from '@/utils/util'
export default {
  name: 'LotteryDetailPage',
  data() { return { activityId: null, activity: {}, userInfo: {}, showResultModal: false, resultTitle: '', resultPrize: null, resultPoints: 0, loading: false } },
  onLoad(options) { if (options && options.id) this.activityId = options.id; this.checkLoginAndLoad() },
  methods: {
    getApi() { const app = typeof getApp === 'function' ? getApp() : null; return (app && app.api) || {} },
    checkLoginAndLoad() { util.requireLogin('请先登录后参与积分抽奖').then(ok => { if (!ok) return uni.navigateBack({ delta: 1 }); getApp().initPage().then(() => { this.getUserInfo(); this.loadActivityDetail() }) }) },
    getUserInfo() { const api = this.getApi(); const fn = api.wxUserGet || api.memberInfo; if (typeof fn === 'function') fn.call(api).then(res => { this.userInfo = (res && res.data) || res || {} }).catch(() => {}) },
    loadActivityDetail() { const api = this.getApi(); if (!api || typeof api.lotteryList !== 'function') return; this.loading = true; api.lotteryList().then(res => { this.loading = false; const data = res && res.data; const list = Array.isArray(data) ? data : (data && (data.records || data.list || data.rows)) || (Array.isArray(res) ? res : []); const item = this.activityId ? list.find(x => String(x.id) === String(this.activityId)) : list[0]; if (item) this.activity = this.formatActivity(item) }).catch(() => { this.loading = false }) },
    formatActivity(item) { return { ...item, prizeList: (item.prizeList || []).map(p => ({ ...p, prizePic: this.formatImageUrl(p.prizePic || p.image) })) } },
    formatImageUrl(url) { return url ? (String(url).startsWith('http') ? url : fullImageUrl(url)) : '/static/img/no_pic.png' },
    getBannerImage() { const p = this.activity.prizeList && this.activity.prizeList[0]; return this.formatImageUrl((p && p.prizePic) || this.activity.coverImage || this.activity.banner) },
    getActivityTitle() { const p = this.activity.prizeList && this.activity.prizeList[0]; return p ? '抽' + (p.prizeName || '奖品') : '积分抽奖' },
    getActivityDesc() { const list = (this.activity.prizeList || []).slice(0, 3).map(p => p.prizeName).filter(Boolean); return list.length ? '奖品：' + list.join('、') + ((this.activity.prizeList || []).length > 3 ? '等' : '') : '参与抽奖赢取丰富奖品' },
    handleDraw() { if (this.loading) return; if ((this.userInfo.points || 0) < (this.activity.costPoints || 0)) return uni.showToast({ title: '积分不足', icon: 'none' }); const api = this.getApi(); if (!api || typeof api.lotteryDraw !== 'function') return uni.showToast({ title: '接口未就绪', icon: 'none' }); this.loading = true; api.lotteryDraw().then(res => { this.loading = false; const data = (res && res.data) || res || {}; this.resultPrize = data.prize || data; this.resultPoints = Number(data.prizePoints || data.points || 0); this.resultTitle = data.isWin === '1' || data.isWin === 1 ? '恭喜中奖' : '很遗憾'; this.showResultModal = true; this.getUserInfo() }).catch(() => { this.loading = false }) },
    hideResultModal() { this.showResultModal = false }
  }
}
</script>
<style scoped>
.container { min-height: 100vh; background: #f3f4f7; padding-bottom: 40rpx; }
.banner-image { width: 100%; height: 300rpx; }
.prizes-grid { gap: 20rpx; margin-top: 24rpx; }
.prize-card { width: calc(33.333% - 14rpx); background: #fff7f7; border-radius: 12rpx; padding: 12rpx; box-sizing: border-box; }
.prize-image,.result-image { width: 100%; height: 150rpx; }
.prize-name { overflow: hidden; white-space: nowrap; text-overflow: ellipsis; }
.result-modal,.result-modal-mask { position: fixed; inset: 0; z-index: 99; }
.result-modal-mask { background: rgba(0,0,0,.55); }
.result-modal-content { position: absolute; z-index: 100; left: 8%; right: 8%; top: 25%; }
</style>
