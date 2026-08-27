/**
 * 积分流水分页，支持 userId、operType、beginTime、endTime 筛选
 */
import request from '@/utils/request'

export function getPage(query) {
  return request({
    url: '/integralflow/page',
    method: 'get',
    params: query || {}
  })
}
