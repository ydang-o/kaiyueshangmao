<!--
  Copyright (C) 2018-2019 www.dingyangmall.com
-->
<template>
  <view class="cu-card article no-card">
    <view class="cu-item" v-for="(item, index) of goodsList" :key="index">
      <navigator hover-class="none" :url="'/pages/goods/goods-detail/index?id=' + item.id">
        <view class="content">
          <image :src="(item.picUrls && item.picUrls[0]) ? item.picUrls[0] : '/static/img/no_pic.png'" mode="aspectFill" class="row-img margin-top-xs" />
          <view class="desc solid-bottom row-info">
            <view class="text-black margin-top-sm overflow-2">{{ item.name }}</view>
            <view class="text-gray text-sm margin-top-sm overflow-2">{{ item.sellPoint }}</view>
            <view class="flex justify-between margin-top-sm">
              <view class="text-price text-bold text-red padding-lr-sm">{{ item.salesPrice }}</view>
              <view class="text-gray text-sm padding-lr-sm">已售{{ item.saleNum }}</view>
            </view>
            <view class="flex justify-start margin-tb-sm">
              <view class="cu-tag bg-orange radius sm" v-if="item.pointsGiveSwitch == '1'">积分</view>
            </view>
          </view>
        </view>
      </navigator>
    </view>
  </view>
</template>

<script>
export default {
  name: 'GoodsRow',
  props: { goodsList: { type: Array, default: () => [] } }
}
</script>

<style scoped>
.row-img { width: 200rpx; height: 200rpx; border-radius: 10rpx; }
.row-info { flex: 1; margin-left: 20rpx; }
</style>
