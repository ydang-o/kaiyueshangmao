import request from '@/utils/request'

export function listReferralMembers(params) {
  return request({ url: '/api/mall/referral-code/members', method: 'get', params: params || {} })
}

export function giftReferral(data) {
  return request({ url: '/api/mall/referral-code/gift', method: 'post', data })
}

export function listReferralRecords(params) {
  return request({ url: '/api/mall/referral-code/records', method: 'get', params: params || {} })
}
