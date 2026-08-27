import request from '@/utils/request'

/** 轮播图分页列表 */
export function getPage(query) {
  return request({
    url: '/mall/banner/page',
    method: 'get',
    params: query
  })
}

/** 轮播图详情 */
export function getObj(id) {
  return request({
    url: '/mall/banner/' + id,
    method: 'get'
  })
}

/** 新增轮播图 (JSON) */
export function addObj(obj) {
  return request({
    url: '/mall/banner',
    method: 'post',
    data: obj
  })
}

/** 修改轮播图 (JSON) */
export function putObj(obj) {
  return request({
    url: '/mall/banner',
    method: 'put',
    data: obj
  })
}

/** 删除轮播图 */
export function delObj(id) {
  return request({
    url: '/mall/banner/' + id,
    method: 'delete'
  })
}

/**
 * 新增轮播图 (带文件上传)，仅传 FormData，不设 Content-Type 让浏览器自动带 boundary
 */
export function uploadBanner(formData) {
  return request({
    url: '/mall/banner/upload',
    method: 'post',
    data: formData,
    headers: { 'Content-Type': false }
  })
}

/**
 * 修改轮播图 (带文件上传)
 */
export function updateBannerWithFile(formData) {
  return request({
    url: '/mall/banner/updateWithFile',
    method: 'post',
    data: formData,
    headers: { 'Content-Type': false }
  })
}
