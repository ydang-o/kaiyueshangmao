/**
 * 系统参数配置：客服、短信策略等
 */
import request from '@/utils/request'

export function listConfig(query) {
  return request({
    url: '/system/config/list',
    method: 'get',
    params: query || {}
  })
}

export function getConfigByKey(configKey) {
  return request({
    url: '/system/config/configKey/' + configKey,
    method: 'get'
  })
}

export function getConfig(configId) {
  return request({
    url: '/system/config/' + configId,
    method: 'get'
  })
}

export function addConfig(data) {
  return request({
    url: '/system/config',
    method: 'post',
    data
  })
}

export function updateConfig(data) {
  return request({
    url: '/system/config',
    method: 'put',
    data
  })
}

export function delConfig(configIds) {
  return request({
    url: '/system/config/' + configIds,
    method: 'delete'
  })
}
