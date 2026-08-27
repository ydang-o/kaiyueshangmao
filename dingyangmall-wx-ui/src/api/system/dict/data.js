/**
 * 字典数据接口，与 RuoYi/Spring 后端 system/dict/data 对齐。
 */
import request from '@/utils/request'

export function getDicts(dictType) {
  return request({ url: '/system/dict/data/type/' + encodeURIComponent(dictType), method: 'get' })
}

export function listDictData(query) {
  return request({ url: '/system/dict/data/list', method: 'get', params: query || {} })
}

export function getDictData(dictCode) {
  return request({ url: '/system/dict/data/' + dictCode, method: 'get' })
}

export function addDictData(data) {
  return request({ url: '/system/dict/data', method: 'post', data })
}

export function updateDictData(data) {
  return request({ url: '/system/dict/data', method: 'put', data })
}

export function delDictData(dictCodes) {
  const ids = Array.isArray(dictCodes) ? dictCodes.join(',') : dictCodes
  return request({ url: '/system/dict/data/' + ids, method: 'delete' })
}
