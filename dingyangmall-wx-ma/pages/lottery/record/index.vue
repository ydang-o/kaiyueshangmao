<template>
  <view class="page tm-page padding">
    <view v-if="recordList.length" class="record-list"><view class="record-item bg-white radius padding margin-bottom-sm" v-for="(item, i) in recordList" :key="item.id || i"><view class="flex justify-between"><text class="text-gray text-sm">{{ formatTime(item.createTime) }}</text><text :class="item.isWin === '1' ? 'text-red' : 'text-gray'">{{ item.isWin === '1' ? '已中奖' : '未中奖' }}</text></view><view class="text-lg margin-top-sm">{{ item.prizeName || '未中奖' }}</view><view class="flex justify-between text-sm text-gray margin-top-sm"><text>消耗 {{ item.costPoints || 0 }} 积分</text><text :class="item.grantStatus === '1' ? 'text-green' : 'text-orange'">{{ item.grantStatus === '1' ? '已发放' : '待发放' }}</text></view></view></view>
    <view v-else class="text-center text-gray padding-xl">暂无抽奖记录</view>
    <view v-if="loadmore" class="text-center text-gray padding" @tap="loadRecords">{{ loading ? '加载中...' : '点击加载更多' }}</view>
  </view>
</template>
<script>
import util from '@/utils/util'
export default {
  name: 'LotteryRecordPage',
  data() { return { recordList: [], page: { current: 1, size: 10 }, loadmore: true, loading: false } },
  onLoad() { getApp().initPage().then(() => this.loadRecords()) },
  onReachBottom() { this.loadmore && !this.loading && (this.page.current++, this.loadRecords()) },
  methods: {
    getApi() { const app = typeof getApp === 'function' ? getApp() : null; return (app && app.api) || {} },
    loadRecords() { const api = this.getApi(); if (!api || typeof api.lotteryRecord !== 'function') return; this.loading = true; api.lotteryRecord(this.page).then(res => { this.loading = false; const d = (res && res.data) || res || {}; const rows = Array.isArray(d) ? d : d.records || d.list || []; this.recordList = this.page.current === 1 ? rows : this.recordList.concat(rows); if (rows.length < this.page.size) this.loadmore = false }).catch(() => { this.loading = false; uni.showToast({ title: '加载失败', icon: 'none' }) }) },
    formatTime(value) { if (!value) return ''; const d = new Date(value); return `${d.getFullYear()}-${String(d.getMonth()+1).padStart(2,'0')}-${String(d.getDate()).padStart(2,'0')} ${String(d.getHours()).padStart(2,'0')}:${String(d.getMinutes()).padStart(2,'0')}` }
  }
}
</script>
<style scoped>
.page { min-height: 100vh; background: #f3f4f7; }
</style>
