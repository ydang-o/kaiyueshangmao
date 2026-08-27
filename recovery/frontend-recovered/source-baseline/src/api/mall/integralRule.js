/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.dingyangmall.com
 */
import request from '@/utils/request'

export function getPage(query) {
  return request({
    url: '/integralrule/page',
    method: 'get',
    params: query
  })
}

export function addObj(obj) {
  return request({
    url: '/integralrule',
    method: 'post',
    data: obj
  })
}

export function getObj(id) {
  return request({
    url: '/integralrule/' + id,
    method: 'get'
  })
}

export function delObj(id) {
  return request({
    url: '/integralrule/' + id,
    method: 'delete'
  })
}

export function putObj(obj) {
  return request({
    url: '/integralrule',
    method: 'put',
    data: obj
  })
}
