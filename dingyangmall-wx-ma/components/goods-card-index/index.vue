<!--
  Copyright (C) 2018-2019 www.dingyangmall.com
-->
<template>
  <view class="goods-container">
    <view class="goods-box" v-for="(item, index) of goodsList" :key="index">
      <navigator hover-class="none" :url="'/pages/goods/goods-detail/index?id=' + item.id">
        <view class="text-black margin-top text-bold padding-lr overflow-1">{{ item.name }}</view>
        <view class="img-box">
          <image :src="(item.picUrls && item.picUrls[0]) ? item.picUrls[0] : '/static/img/no_pic.png'" class="margin-top-xs" mode="aspectFill" />
        </view>
        <view class="text-center margin-top">
          <view class="cu-tag line-black round text-df text-price text-bold text-black">{{ item.salesPrice }}</view>
          <view class="text-gray text-sm padding-lr-sm margin-top-sm">已售{{ item.saleNum }}</view>
        </view>
      </navigator>
    </view>
  </view>
</template>

<script>
export default {
  name: 'GoodsCardIndex',
  props: {
    goodsList: { type: Array, default: () => [] }
  }
}
</script>

<style scoped>
.goods-container { display: flex; justify-content: space-between; flex-wrap: wrap; box-sizing: content-box; padding: 20rpx; }
.goods-box { width: 346rpx; height: 560rpx; background-color: #fff; overflow: hidden; margin-bottom: 40rpx; border-radius: 20rpx; box-shadow: 0 0 60rpx rgba(211,211,211,1); }
.goods-box .img-box { width: 100%; height: 349rpx; overflow: hidden; }
.goods-box .img-box image { width: 100%; height: 336rpx; }
</style>
