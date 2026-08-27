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


// 最新后端补充的经营统计
export function getStatisticsCashSales(params) {
  return request({ url: '/system/dashboard/statistics/cash-sales', method: 'get', params })
}

export function getStatisticsGoodsSales(params) {
  return request({ url: '/system/dashboard/statistics/goods-sales', method: 'get', params })
}

export function getStatisticsGoodsStock(params) {
  return request({ url: '/system/dashboard/statistics/goods-stock', method: 'get', params })
}

export function getStatisticsIntegralExchange(params) {
  return request({ url: '/system/dashboard/statistics/integral-exchange', method: 'get', params })
}

export function getStatisticsIntegralGrant(params) {
  return request({ url: '/system/dashboard/statistics/integral-grant', method: 'get', params })
}

export function getStatisticsMemberNew(params) {
  return request({ url: '/system/dashboard/statistics/member-new', method: 'get', params })
}

export function getStatisticsMemberReferralDetail(params) {
  return request({ url: '/system/dashboard/statistics/member-referral-detail', method: 'get', params })
}

export function getStatisticsMemberReferralSummary(params) {
  return request({ url: '/system/dashboard/statistics/member-referral-summary', method: 'get', params })
}
