/**
 * 用户/经销商相关接口（已移除 system/user 后端，此为兼容存根）
 * 如需经销商管理，请使用 api/mall/member 或对接对应后端
 */
import request from '@/utils/request'

export function listUser(query) {
  return Promise.resolve({ rows: [], total: 0 })
}

export function getUser(userId) {
  return Promise.resolve({ data: {} })
}

export function addUser(data) {
  return Promise.resolve({ msg: '接口已移除' })
}

export function updateUser(data) {
  return Promise.resolve({ msg: '接口已移除' })
}

export function delUser(userId) {
  return Promise.resolve({ msg: '接口已移除' })
}

export function changeUserStatus(userId, status) {
  return Promise.resolve({ msg: '接口已移除' })
}

export function resetUserPwd(userId, password) {
  return Promise.resolve({ msg: '接口已移除' })
}
