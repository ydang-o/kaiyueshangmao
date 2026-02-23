/**
 * Copyright (C) 2018-2019
 * 数字工具
 */
export function numberFormat(value, num) {
  return parseFloat(Number(value).toFixed(num))
}
export function numberSubtract(value1, value2) {
  const v = parseFloat(value1) - parseFloat(value2)
  return parseFloat(v.toFixed(2))
}
export function numberAddition(value1, value2) {
  const v = parseFloat(value1) + parseFloat(value2)
  return parseFloat(v.toFixed(2))
}
export default { numberFormat, numberSubtract, numberAddition }
