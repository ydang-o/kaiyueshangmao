/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.dingyangmall.com
 * 注意：
 * 本软件为www.dingyangmall.com开发研制，项目使用请保留此说明
 *
 * 所有请求的根地址均来自 config/env.js 的 basePath（端口 7500），勿在别处写死端口或域名。
 */
import __config from '../config/env'

const request = (url, method, data, showLoading) => {
  let _url = __config.basePath + url
  return new Promise((resolve, reject) => {
    if (showLoading){
      wx.showLoading({
        title: '加载中',
      })
    }
    wx.request({
      url: _url,
      method: method,
      data: data,
      header: {
        'app-id': wx.getAccountInfoSync().miniProgram.appId,
        'third-session': getApp().globalData.thirdSession != null ? getApp().globalData.thirdSession : ''
      },
      success(res) {
        if (res.statusCode == 200) {
          if (res.data.code != 200) {
            console.log(res.data)
            wx.showModal({
              title: '提示',
              content: res.data.msg ? res.data.msg : '没有数据' + '',
              success() {
                
              },
              complete(){
                if(res.data.code == 60001){
                  //session过期，则清除过期session，并重新加载当前页
                  getApp().globalData.thirdSession = null
                  wx.reLaunch({
                    url: getApp().getCurrentPageUrlWithArgs()
                  })
                }
              }
            })
            reject(res.data.msg)
          }
          resolve(res.data)
        } else if (res.statusCode == 404) {
          wx.showModal({
            title: '提示',
            content: '接口请求出错，请检查手机网络',
            success(res) {

            }
          })
          reject()
        } else {
          console.log(res)
          wx.showModal({
            title: '提示',
            content: res.errMsg + ':' + res.data.message + ':' + res.data.msg,
            success(res) {

            }
          })
          reject()
        }
      },
      fail(error) {
        console.log(error)
        wx.showModal({
          title: '提示',
          content: '接口请求出错：' + error.errMsg,
          success(res) {

          }
        })
        reject(error)
      },
      complete(res) {
        wx.hideLoading()
      }
    })
  })
}

const merchantRequest = (url, method, data, showLoading) => {
  let _url = __config.basePath + url
  return new Promise((resolve, reject) => {
    if (showLoading){
      wx.showLoading({
        title: '加载中',
      })
    }
    let token = wx.getStorageSync('merchantToken')
    wx.request({
      url: _url,
      method: method,
      data: data,
      header: {
        'Authorization': token ? 'Bearer ' + token : ''
      },
      success(res) {
        if (res.statusCode == 200) {
          if (res.data.code != 200) {
            wx.showModal({
              title: '提示',
              content: res.data.msg ? res.data.msg : '操作失败',
              showCancel: false
            })
            reject(res.data.msg)
          } else {
            resolve(res.data)
          }
        } else if (res.statusCode == 401) {
          wx.showToast({ title: '登录已过期', icon: 'none' })
          wx.removeStorageSync('merchantToken')
          wx.redirectTo({ url: '/pages/merchant/login/index' })
          reject('Unauthorized')
        } else {
          wx.showModal({
            title: '提示',
            content: '系统错误',
            showCancel: false
          })
          reject()
        }
      },
      fail(error) {
        wx.showModal({
          title: '提示',
          content: '网络请求失败',
          showCancel: false
        })
        reject(error)
      },
      complete(res) {
        wx.hideLoading()
      }
    })
  })
}

module.exports = {
  request,
  merchantRequest,
  login: (data) => {//小程序登录接口
    return request('/weixin/api/ma/wxuser/login', 'post', data, false)
  },
  wxUserGet: (data) => {//微信用户查询
    return request('/app/member/info', 'get', null, false)
  },
  memberInfo: () => {//会员信息查询
    return request('/app/member/info', 'get', null, false)
  },
  couponMy: (status) => {//我的优惠券
    return request('/app/coupon/my', 'get', {status: status}, false)
  },
  lotteryConfig: () => {//抽奖配置
    return request('/app/lottery/config', 'get', null, false)
  },
  lotteryDraw: () => {//抽奖
    return request('/app/lottery/draw', 'post', null, true)
  },
  lotteryRecord: (data) => {//抽奖记录
    return request('/app/lottery/record', 'get', data, false)
  },
  memberSignIn: () => {//每日签到
    return request('/app/member/sign-in', 'post', null, true)
  },
  wxUserSave: (data) => {//微信用户新增
    return request('/weixin/api/ma/wxuser', 'post', data, true)
  },
  goodsCategoryGet: (data) => {//商品分类查询
    return request('/weixin/api/ma/goodscategory/tree', 'get', data, true)
  },
  // 首页公告列表（若后端有该接口则返回数组，无则前端不展示公告）
  getNoticeList: () => {
    return request('/weixin/api/ma/notice/list', 'get', null, false)
  },
  goodsPage: (data) => {//商品列表
    return request('/weixin/api/ma/goodsspu/page', 'get', data, false)
  },
  goodsGet: (id) => {//商品查询
    return request('/weixin/api/ma/goodsspu/' + id, 'get', null, false)
  },
  shoppingCartPage: (data) => {//购物车列表
    return request('/weixin/api/ma/shoppingcart/page', 'get', data, false)
  },
  shoppingCartAdd: (data) => {//购物车新增
    return request('/weixin/api/ma/shoppingcart', 'post', data, true)
  },
  shoppingCartEdit: (data) => {//购物车修改
    return request('/weixin/api/ma/shoppingcart', 'put', data, true)
  },
  shoppingCartDel: (data) => {//购物车删除
    return request('/weixin/api/ma/shoppingcart/del', 'post', data, false)
  },
  shoppingCartCount: (data) => {//购物车数量
    return request('/weixin/api/ma/shoppingcart/count', 'get', data, false)
  },
  orderSub: (data) => {//订单提交
    return request('/weixin/api/ma/orderinfo', 'post', data, true)
  },
  // 商家端API
  getCaptcha: () => {
    return request('/captchaImage', 'get', null, false)
  },
  merchantLogin: (data) => {
    return request('/login', 'post', data, true)
  },
  merchantScanUser: (code) => {
    return merchantRequest('/api/mall/merchant/scan/user/' + code, 'get', null, false)
  },
  merchantGivePoints: (data) => {
    return merchantRequest('/api/mall/merchant/scan/points', 'post', data, true)
  },
  merchantVerifyCoupon: (data) => {
    return merchantRequest('/api/mall/merchant/scan/coupon/verify', 'post', data, true)
  },
  sendPacket: (data) => {//发送积分红包
    return request('/app/member/send-packet', 'post', data, true)
  },
  sendSmsCode: (phone) => {//发送短信验证码
    return request('/app/member/send-sms-code', 'get', { phone: phone }, true)
  },
  orderPage: (data) => {//订单列表
    return request('/weixin/api/ma/orderinfo/page', 'get', data, false)
  },
  orderGet: (id) => {//订单详情查询
    return request('/weixin/api/ma/orderinfo/' + id, 'get', null, false)
  },
  orderCancel: (id) => {//订单确认取消
    return request('/weixin/api/ma/orderinfo/cancel/' + id, 'put', null, true)
  },
  orderRefunds: (data) => {//订单申请退款
    return request('/weixin/api/ma/orderinfo/refunds', 'post', data, true)
  },
  orderReceive: (id) => {//订单确认收货
    return request('/weixin/api/ma/orderinfo/receive/' + id, 'put', null, true)
  },
  orderLogistics: (id) => {//订单物流信息
    return request('/weixin/api/ma/orderinfo/logistics/' + id, 'get', null, false)
  },
  orderDel: (id) => {//订单删除
    return request('/weixin/api/ma/orderinfo/' + id, 'delete', null, false)
  },
  orderCountAll: (data) => {//订单计数
    return request('/weixin/api/ma/orderinfo/countAll', 'get', data, false)
  },
  unifiedOrder: (data) => {//下单接口
    return request('/weixin/api/ma/orderinfo/unifiedOrder', 'post', data, true)
  },
  userAddressPage: (data) => {//用户收货地址列表
    return request('/weixin/api/ma/useraddress/page', 'get', data, false)
  },
  userAddressSave: (data) => {//用户收货地址新增
    return request('/weixin/api/ma/useraddress', 'post', data, true)
  },
  userAddressDel: (id) => {//用户收货地址删除
    return request('/weixin/api/ma/useraddress/' + id, 'delete', null, false)
  }
}
