<!--
  Copyright (C) 2018-2019 www.dingyangmall.com
-->
<template>
  <view class="page">
    <form @submit="userAddressSave">
      <view class="cu-form-group"><view class="title">姓名</view><input placeholder="请输入姓名" name="userName" v-model="userAddress.userName" /></view>
      <view class="cu-form-group"><view class="title">联系电话</view><input placeholder="请输入电话" name="telNum" v-model="userAddress.telNum" type="number" /></view>
      <view class="cu-form-group">
        <view class="title">地址选择</view>
        <picker mode="region" @change="regionChange" :value="region">
          <view class="picker">{{ region[0] }}，{{ region[1] }}，{{ region[2] }}</view>
        </picker>
      </view>
      <view class="cu-form-group"><view class="title">详细地址</view><input placeholder="请输入详细地址" name="detailInfo" v-model="userAddress.detailInfo" /></view>
      <view class="cu-form-group"><view class="title">设为默认地址</view><switch class="red sm" :checked="userAddress.isDefault == '1'" @change="isDefaultChange" /></view>
      <button class="cu-btn block bg-green margin-sm" formType="submit">立即保存</button>
      <button class="cu-btn block bg-red margin-sm" v-if="userAddress.id" @tap="userAddressDelete">删除</button>
      <button class="cu-btn block bg-orange margin-sm cuIcon-weixin" @tap="getWxAddress">导入微信地址</button>
    </form>
  </view>
</template>

<script>
export default {
  name: 'UserAddressFormPage',
  data() {
    return { region: ['选择省', '选择市', '选择区'], userAddress: {} }
  },
  onLoad() {
    uni.getStorage({
      key: 'param-userAddressForm',
      success: (res) => {
        const u = res.data || {}
        this.userAddress = u
        this.region = [u.provinceName || '选择省', u.cityName || '选择市', u.countyName || '选择区']
      }
    })
  },
  methods: {
    regionChange(e) { this.region = e.detail.value || [] },
    isDefaultChange(e) { this.userAddress.isDefault = e.detail.value ? '1' : '0' },
    userAddressSave(e) {
      const value = e.detail.value || {}
      const region = this.region
      if (!value.userName) { uni.showToast({ title: '请填写收货人姓名', icon: 'none' }); return }
      if (!value.telNum) { uni.showToast({ title: '请填写联系电话', icon: 'none' }); return }
      if (!/^1[3-9]\d{9}$/.test(value.telNum)) { uni.showToast({ title: '请输入正确的手机号码', icon: 'none' }); return }
      if (region[0] === '选择省') { uni.showToast({ title: '请选择所在地区', icon: 'none' }); return }
      if (!value.detailInfo) { uni.showToast({ title: '请填写详细地址', icon: 'none' }); return }
      getApp().api.userAddressSave({
        id: this.userAddress.id,
        userName: value.userName,
        telNum: value.telNum,
        provinceName: region[0],
        cityName: region[1],
        countyName: region[2],
        detailInfo: value.detailInfo,
        isDefault: this.userAddress.isDefault || '0'
      }).then(() => uni.navigateBack())
    },
    userAddressDelete() {
      uni.showModal({ content: '确认将这个地址删除吗？', cancelText: '我再想想', confirmColor: '#ff0000', success: (res) => {
        if (res.confirm) getApp().api.userAddressDel(this.userAddress.id).then(() => uni.navigateBack())
      }})
    },
    getWxAddress() {
      uni.chooseAddress({
        success: (res) => {
          this.region = [res.provinceName, res.cityName, res.countyName]
          this.userAddress.userName = res.userName
          this.userAddress.telNum = res.telNumber
          this.userAddress.detailInfo = res.detailInfo
        },
        fail: (res) => { if (res.errMsg && res.errMsg.indexOf('cancel') >= 0) uni.showToast({ title: '取消选择', icon: 'none' }) }
      })
    }
  }
}
</script>

<style scoped>
.page { padding: 20rpx; }
.picker { padding: 20rpx 0; }
</style>
