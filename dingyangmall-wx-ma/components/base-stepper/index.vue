<!--
  Copyright (C) 2018-2019 www.dingyangmall.com
-->
<template>
  <view class="flex text-center st-wrap" style="width:230rpx">
    <view class="flex-sub">
      <button class="cu-btn cuIcon-move sm st-num-bt" :disabled="stNum <= min" @tap="stNumMinus">-</button>
    </view>
    <view class="flex-sub">
      <input type="number" class="st-num text-center bg-gray radius" :value="stNum" disabled @input="onInput" />
    </view>
    <view class="flex-sub">
      <button class="cu-btn cuIcon-add sm st-num-bt" :disabled="max > 0 && stNum >= max" @tap="stNumAdd">+</button>
    </view>
  </view>
</template>

<script>
export default {
  name: 'BaseStepper',
  props: {
    stNum: { type: Number, default: 0 },
    min: { type: Number, default: 0 },
    max: { type: Number, default: 0 },
    customClass: { type: String, default: '' }
  },
  data() {
    return { num: this.stNum }
  },
  watch: {
    stNum: { handler(v) { this.num = v }, immediate: true }
  },
  methods: {
    stNumMinus() {
      if (this.num <= this.min) return
      this.num--
      this.$emit('numChange', this.num)
    },
    stNumAdd() {
      if (this.max > 0 && this.num >= this.max) return
      this.num++
      this.$emit('numChange', this.num)
    },
    onInput(e) {
      const val = parseInt(e.detail.value, 10)
      if (!isNaN(val)) this.$emit('numChange', val)
    }
  }
}
</script>

<style scoped>
.st-num-bt { min-width: 60rpx; }
.st-num { width: 80rpx; }
</style>
