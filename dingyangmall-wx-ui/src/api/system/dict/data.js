/**
 * 字典数据接口（已移除 system/dict 后端，此为兼容存根，返回空数据）
 */
export function getDicts(dictType) {
  return Promise.resolve({ data: [] })
}
