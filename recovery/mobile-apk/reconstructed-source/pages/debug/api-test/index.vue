<!--
  接口调试页：点击对应按钮只发一个请求，便于一个接口一个接口检查。
-->
<template>
  <view class="page padding">
    <view class="text-bold text-lg margin-bottom">接口调试（一次只发一个请求）</view>
    <view class="text-gray text-sm margin-bottom">点击下方按钮会单独调用对应接口，便于在控制台/后端逐条检查。</view>
    <view v-if="lastResult" class="result-box margin-bottom">
      <view class="text-sm text-bold">上次结果：</view>
      <view class="text-sm">{{ lastResult }}</view>
    </view>
    <view class="btn-group">
      <view class="cu-list menu card-menu radius">
        <view class="cu-item arrow" @tap="callApi('goodsspu/page', () => api.goodsPage({ searchCount: false, current: 1, size: 5 }))">
          <view class="content"><text class="text-black">1. 商品分页</text> <text class="text-gray text-sm">GET goodsspu/page</text></view>
        </view>
        <view class="cu-item arrow" @tap="callApi('goodscategory/tree', () => api.goodsCategoryGet())">
          <view class="content"><text class="text-black">2. 分类树</text> <text class="text-gray text-sm">GET goodscategory/tree</text></view>
        </view>
        <view class="cu-item arrow" @tap="callApi('notice/list', () => api.getNoticeList())">
          <view class="content"><text class="text-black">3. 公告列表</text> <text class="text-gray text-sm">GET notice/list</text></view>
        </view>
        <view class="cu-item arrow" @tap="callApi('wxuser/info', () => api.wxUserGet())">
          <view class="content"><text class="text-black">4. 用户信息</text> <text class="text-gray text-sm">GET wxuser/info（需登录）</text></view>
        </view>
        <view class="cu-item arrow" @tap="callApi('shoppingcart/count', () => api.shoppingCartCount())">
          <view class="content"><text class="text-black">5. 购物车数量</text> <text class="text-gray text-sm">GET shoppingcart/count</text></view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import api from '@/utils/api'
export default {
  name: 'ApiTestPage',
  data() {
    return { lastResult: '' }
  },
  methods: {
    callApi(name, fn) {
      if (typeof fn !== 'function') return
      this.lastResult = '请求中: ' + name + ' ...'
      fn()
        .then(res => {
          this.lastResult = name + ' 成功 code=' + (res && res.code) + ' '
          if (res && res.data != null) this.lastResult += '有 data'
          uni.showToast({ title: '成功', icon: 'success' })
        })
        .catch(err => {
          this.lastResult = name + ' 失败: ' + (err && (err.message || err.toString()).slice(0, 80))
          uni.showToast({ title: '失败', icon: 'none', duration: 2000 })
        })
    }
  }
}
</script>

<style scoped>
.page { min-height: 100vh; background: #f5f5f5; }
.result-box { background: #fff; padding: 20rpx; border-radius: 12rpx; word-break: break-all; }
.btn-group { margin-top: 20rpx; }
</style>
