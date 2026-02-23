<template>
  <view class="page">
    <text class="title">我的（uni-app 迁移中）</text>
    <view class="tip">原 `user-center` 页面将逐步迁移到该 vue 页面。</view>
  </view>
</template>

<script>
export default {
  name: 'UserCenterPage'
}
</script>

<style scoped>
.page { padding: 32rpx; }
.title { font-size: 36rpx; font-weight: 600; color: #333; }
.tip { margin-top: 20rpx; color: #666; line-height: 1.8; }
</style>
