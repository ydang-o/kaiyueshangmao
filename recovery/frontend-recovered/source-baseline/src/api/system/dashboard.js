import request from '@/utils/request'

// 首页仪表盘汇总
export function getDashboardData() {
  return request({
    url: '/system/dashboard/data',
    method: 'get'
  })
}

// 积分统计：总发放、流水条数、会员总积分
export function getStatisticsIntegral() {
  return request({
    url: '/system/dashboard/statistics/integral',
    method: 'get'
  })
}

// 订单统计：总订单数、待发货/已发货/已完成数量
export function getStatisticsOrder() {
  return request({
    url: '/system/dashboard/statistics/order',
    method: 'get'
  })
}

// 核销统计：总核销、今日核销，可选 verifyDealerId 按商家
export function getStatisticsCoupon(params) {
  return request({
    url: '/system/dashboard/statistics/coupon',
    method: 'get',
    params
  })
}

// 用户统计：总会员数
export function getStatisticsUser() {
  return request({
    url: '/system/dashboard/statistics/user',
    method: 'get'
  })
}
