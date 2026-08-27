<template>
  <view class="flow-page">
    <view class="points-card"><view class="card-bg"></view><view class="card-content"><view class="points-label">当前积分</view><view class="points-value">{{ userPoints }}</view></view></view>
    <view class="flow-list"><view class="list-header"><text class="list-title">积分流水</text></view><view v-if="loading" class="flow-empty">加载中...</view><view v-else-if="records.length === 0" class="flow-empty">暂无积分流水</view><view v-else><view class="flow-item" v-for="item in records" :key="item.id"><view class="flow-item-left"><view class="flow-type-icon" :class="getTypeClass(item.operType)">{{ getTypeIcon(item.operType) }}</view><view class="flow-info"><view class="flow-type">{{ getTypeName(item.operType) }}</view><view class="flow-time">{{ formatTime(item.operTime) }}</view><view v-if="item.remark" class="flow-remark">{{ item.remark }}</view></view></view><view class="flow-points" :class="item.integralNum >= 0 ? 'points-up' : 'points-down'">{{ item.integralNum >= 0 ? '+' : '' }}{{ item.integralNum }}</view></view></view></view><view v-if="hasMore && !loading" class="load-more" @tap="loadMore">点击加载更多</view><view v-else-if="records.length" class="load-more text-gray">没有更多了</view>
  </view>
</template>
<script>
import util from '@/utils/util'
export default {
  name: 'IntegralFlowPage',
  data() { return { userPoints: 0, records: [], page: 1, pageSize: 20, total: 0, loading: false, hasMore: true } },
  onLoad() { util.requireLogin('请先登录后查看积分流水').then(ok => { if (ok) { this.fetchPoints(); this.fetchFlow() } else uni.navigateBack({ delta: 1 }) }) },
  methods: {
    fetchPoints() { const api = getApp().api; if (!api || typeof api.getUserPoints !== 'function') return; api.getUserPoints().then(res => { const d = res && (res.data || res); this.userPoints = (d && (d.points || d.integral || d.score)) || 0 }).catch(() => {}) },
    fetchFlow() { if (this.loading || !this.hasMore) return; this.loading = true; const api = getApp().api; if (!api || typeof api.integralFlowPage !== 'function') { this.loading = false; return } api.integralFlowPage({ page: this.page, pageSize: this.pageSize }).then(res => { this.loading = false; const d = (res && res.data) || {}; const rows = d.records || d.list || []; this.records = this.records.concat(rows); this.total = d.total || 0; this.hasMore = this.records.length < this.total && rows.length > 0 }).catch(() => { this.loading = false; uni.showToast({ title: '网络异常，请稍后重试', icon: 'none' }) }) },
    loadMore() { if (this.hasMore && !this.loading) { this.page++; this.fetchFlow() } },
    formatTime(t) { return t ? String(t).replace('T', ' ').substring(0, 19) : '' },
    getTypeName(t) { return ({ 1:'兑换商品',2:'发放获得',3:'退款返还',4:'赠送扣减',5:'收到赠送',6:'经销商扣减',7:'积分红包',8:'签到获得',9:'积分购买' })[t] || '积分变动' },
    getTypeClass(t) { return [2,3,5,7,8].includes(t) ? 'type-plus' : [1,4,6,9].includes(t) ? 'type-minus' : 'type-default' },
    getTypeIcon(t) { return [2,3,5,7,8].includes(t) ? '+' : [1,4,6,9].includes(t) ? '-' : '·' }
  }
}
</script>
<style scoped>
.flow-page { min-height: 100vh; background: #f5f6f8; padding-bottom: 40rpx; }
.points-card { margin: 24rpx; height: 220rpx; border-radius: 24rpx; overflow: hidden; position: relative; background: linear-gradient(135deg,#ff5f6d,#ff9966); color:#fff; }
.card-content { position: relative; padding: 48rpx; text-align:center; }.points-label { font-size: 28rpx; opacity:.9; }.points-value { font-size: 68rpx; font-weight:bold; margin-top:16rpx; }
.flow-list { margin: 24rpx; background:#fff; border-radius:16rpx; }.list-header { padding: 28rpx; border-bottom:1rpx solid #eee; }.list-title { font-size:32rpx; font-weight:bold; }.flow-item { display:flex; justify-content:space-between; padding:26rpx 28rpx; border-bottom:1rpx solid #f3f3f3; }.flow-item-left{display:flex;}.flow-type-icon{width:58rpx;height:58rpx;line-height:58rpx;text-align:center;border-radius:50%;margin-right:20rpx;font-weight:bold;}.type-plus{background:#e8fff0;color:#16a34a;}.type-minus{background:#fff0f0;color:#ef4444;}.type-default{background:#f3f4f6;color:#6b7280;}.flow-type{font-size:29rpx;color:#222;}.flow-time,.flow-remark{font-size:23rpx;color:#999;margin-top:8rpx;}.flow-points{font-size:34rpx;font-weight:bold;}.points-up{color:#16a34a;}.points-down{color:#ef4444;}.flow-empty,.load-more{text-align:center;color:#999;padding:40rpx;}
</style>
