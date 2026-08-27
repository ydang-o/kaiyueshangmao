import request from '@/utils/request'

export function getPage(query) {
  return request({
    url: '/mall/member/page',
    method: 'get',
    params: query
  })
}

export function getObj(id) {
  return request({
    url: '/mall/member/' + id,
    method: 'get'
  })
}

export function addObj(obj) {
  return request({
    url: '/mall/member',
    method: 'post',
    data: obj
  })
}

export function putObj(obj) {
  return request({
    url: '/mall/member',
    method: 'put',
    data: obj
  })
}

export function delObj(id) {
  return request({
    url: '/mall/member/' + id,
    method: 'delete'
  })
}
