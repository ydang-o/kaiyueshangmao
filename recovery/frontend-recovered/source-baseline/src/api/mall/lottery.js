import request from '@/utils/request'

// 配置相关 (TbLotteryConfig)
/** 获取当前编辑用配置（优先开启，否则最新一条），始终带 prizeList */
export function getConfig() {
  return request({
    url: '/mall/lottery/config',
    method: 'get'
  })
}

/** 分页查询配置列表 */
export function getConfigPage(query) {
  return request({
    url: '/mall/lottery/config/page',
    method: 'get',
    params: query
  })
}

/** 按 id 查一条配置（含 prizeList） */
export function getConfigById(id) {
  return request({
    url: '/mall/lottery/config/' + id,
    method: 'get'
  })
}

/** 保存/修改配置（Body 可带 prizeList，更新时覆盖原奖品） */
export function saveConfig(data) {
  return request({
    url: '/mall/lottery/config',
    method: 'post',
    data: data
  })
}

/** 删除配置（级联删除其下奖品） */
export function delConfig(id) {
  return request({
    url: '/mall/lottery/config/' + id,
    method: 'delete'
  })
}

// 奖品相关 (TbLotteryPrize)
/** 奖品列表，可选 configId 按配置过滤 */
export function getPrizeList(query) {
  return request({
    url: '/mall/lottery/prize/list',
    method: 'get',
    params: query || {}
  })
}

/** 新增奖品 */
export function addPrize(obj) {
  return request({
    url: '/mall/lottery/prize',
    method: 'post',
    data: obj
  })
}

/** 修改奖品（需带 id） */
export function updatePrize(obj) {
  return request({
    url: '/mall/lottery/prize',
    method: 'put',
    data: obj
  })
}

/** 删除奖品 */
export function delPrize(id) {
  return request({
    url: '/mall/lottery/prize/' + id,
    method: 'delete'
  })
}

// 抽奖记录相关
/** 抽奖记录分页查询 */
export function getRecordPage(query) {
  return request({
    url: '/mall/lottery/record/page',
    method: 'get',
    params: query
  })
}
