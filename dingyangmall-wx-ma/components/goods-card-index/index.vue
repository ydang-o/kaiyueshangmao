<!--
  Copyright (C) 2018-2019 www.dingyangmall.com
  支持错位双列展示（主流电商风格）
-->
<template>
  <view class="goods-container" :class="{ 'goods-container--staggered': staggered }">
    <template v-if="staggered">
      <view class="goods-col goods-col--left">
        <view class="goods-box tm-grid-card" v-for="(item, index) in leftList" :key="item.id || 'left-'+index">
          <navigator hover-class="none" :url="'/pages/goods/goods-detail/index?id=' + item.id">
            <view class="goods-name padding-lr overflow-1">{{ item.name }}</view>
            <view class="img-box">
              <image :src="$imgUrl(item.picUrls && item.picUrls[0]) || '/static/img/no_pic.png'" class="margin-top-xs tm-product-image" mode="aspectFill" />
            </view>
            <view class="text-center margin-top-sm padding-bottom-sm">
              <view class="goods-price">{{ item.salesPrice }}</view>
              <view class="goods-sale padding-lr-sm margin-top-sm">已售 {{ item.saleNum }}</view>
            </view>
          </navigator>
        </view>
      </view>
      <view class="goods-col goods-col--right">
        <view class="goods-box tm-grid-card" v-for="(item, index) in rightList" :key="item.id || 'right-'+index">
          <navigator hover-class="none" :url="'/pages/goods/goods-detail/index?id=' + item.id">
            <view class="goods-name padding-lr overflow-1">{{ item.name }}</view>
            <view class="img-box">
              <image :src="$imgUrl(item.picUrls && item.picUrls[0]) || '/static/img/no_pic.png'" class="margin-top-xs tm-product-image" mode="aspectFill" />
            </view>
            <view class="text-center margin-top-sm padding-bottom-sm">
              <view class="goods-price">{{ item.salesPrice }}</view>
              <view class="goods-sale padding-lr-sm margin-top-sm">已售 {{ item.saleNum }}</view>
            </view>
          </navigator>
        </view>
      </view>
    </template>
    <template v-else>
      <view class="goods-box tm-grid-card" v-for="(item, index) in goodsList" :key="item.id || index">
        <navigator hover-class="none" :url="'/pages/goods/goods-detail/index?id=' + item.id">
          <view class="goods-name padding-lr overflow-1">{{ item.name }}</view>
          <view class="img-box">
            <image :src="$imgUrl(item.picUrls && item.picUrls[0]) || '/static/img/no_pic.png'" class="margin-top-xs tm-product-image" mode="aspectFill" />
          </view>
          <view class="text-center margin-top-sm padding-bottom-sm">
            <view class="goods-price">{{ item.salesPrice }}</view>
            <view class="goods-sale padding-lr-sm margin-top-sm">已售 {{ item.saleNum }}</view>
          </view>
        </navigator>
      </view>
    </template>
  </view>
</template>

<script>
export default {
  name: 'GoodsCardIndex',
  props: {
    goodsList: { type: Array, default: () => [] },
    /** 是否错位双列展示（左列 0,2,4... 右列 1,3,5...，右列上移形成错位） */
    staggered: { type: Boolean, default: false }
  },
  computed: {
    leftList() {
      return (this.goodsList || []).filter((_, i) => i % 2 === 0)
    },
    rightList() {
      return (this.goodsList || []).filter((_, i) => i % 2 === 1)
    }
  }
}
</script>

<style scoped>
.goods-container {
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
  box-sizing: content-box;
  padding: 20rpx;
}

.goods-container--staggered {
  display: flex;
  justify-content: space-between;
  padding: 20rpx 16rpx;
  gap: 16rpx;
}

.goods-container--staggered .goods-col {
  width: calc(50% - 8rpx);
  display: flex;
  flex-direction: column;
  gap: 24rpx;
}

.goods-container--staggered .goods-col--right {
  margin-top: 80rpx;
}

.goods-container--staggered .goods-box {
  width: 100%;
  margin-bottom: 0;
}

.goods-box {
  width: 346rpx;
  overflow: hidden;
  margin-bottom: 28rpx;
  border-radius: 20rpx;
}

.goods-name {
  margin-top: 20rpx;
  font-size: 28rpx;
  font-weight: 600;
  color: #111827;
}

.goods-box .img-box {
  width: 100%;
  height: 349rpx;
  overflow: hidden;
}

.goods-box .img-box image {
  width: 100%;
  height: 336rpx;
}

.goods-price {
  display: inline-block;
  padding: 8rpx 20rpx;
  border-radius: 999rpx;
  background: #fff5f7;
  color: #ff0036;
  font-size: 30rpx;
  font-weight: 700;
}

.goods-price::before {
  content: "¥";
  font-size: 24rpx;
  margin-right: 4rpx;
}

.goods-sale {
  color: #6b7280;
  font-size: 22rpx;
}
</style>
