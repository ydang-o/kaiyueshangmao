import request from '@/utils/request'

export function getPage(query) {
  return request({
    url: '/mall/coupon/page',
    method: 'get',
    params: query
  })
}

export function addObj(obj) {
  return request({
    url: '/mall/coupon',
    method: 'post',
    data: obj
  })
}

export function getObj(id) {
  return request({
    url: '/mall/coupon/' + id,
    method: 'get'
  })
}

export function delObj(id) {
  return request({
    url: '/mall/coupon/' + id,
    method: 'delete'
  })
}

export function putObj(obj) {
  return request({
    url: '/mall/coupon',
    method: 'put',
    data: obj
  })
}

// 核销统计汇总：总核销量、今日核销，可选 verifyDealerId 按商家
export function getStatistics(params) {
  return request({
    url: '/mall/coupon/statistics',
    method: 'get',
    params: params || {}
  })
}

/** 发放商品券 */
export function distributeCoupon(data) {
  return request({
    url: '/mall/coupon/distribute',
    method: 'post',
    data: data
  })
}