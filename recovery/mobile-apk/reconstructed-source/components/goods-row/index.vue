<!--
  Copyright (C) 2018-2019 www.dingyangmall.com
-->
<template>
  <view class="cu-card article no-card">
    <view class="cu-item" v-for="(item, index) of goodsList" :key="index">
      <navigator hover-class="none" :url="'/pages/goods/goods-detail/index?id=' + item.id">
        <view class="content">
          <image :src="$imgUrl(item.picUrls && item.picUrls[0]) || '/static/img/no_pic.png'" mode="aspectFill" class="row-img margin-top-xs tm-product-image" />
          <view class="desc solid-bottom row-info">
            <view class="tm-product-title margin-top-sm overflow-2">{{ item.name }}</view>
            <view class="tm-product-desc margin-top-sm overflow-2">{{ item.sellPoint }}</view>
            <view class="flex justify-between margin-top-sm">
              <view class="row-price padding-lr-sm">{{ item.salesPrice }}</view>
              <view class="row-sale padding-lr-sm">已售 {{ item.saleNum }}</view>
            </view>
            <view class="flex justify-start margin-tb-sm">
              <view class="cu-tag line-red radius sm" v-if="item.pointsGiveSwitch == '1'">积分加赠</view>
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
.row-img { width: 200rpx; height: 200rpx; border-radius: 14rpx; }
.row-info { flex: 1; margin-left: 20rpx; }
.row-price { color: #ff0036; font-size: 30rpx; font-weight: 700; }
.row-price::before { content: "¥"; font-size: 22rpx; margin-right: 4rpx; }
.row-sale { color: #6b7280; font-size: 22rpx; }
</style>
