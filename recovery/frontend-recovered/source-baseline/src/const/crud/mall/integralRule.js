/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.dingyangmall.com
 * 注意：
 * 本软件为www.dingyangmall.com开发研制，项目使用请保留此说明
 */
export const tableOption = {
  dialogDrag: true,
  border: true,
  index: true,
  indexLabel: '序号',
  stripe: true,
  menuAlign: 'center',
  align: 'center',
  menuType: 'text',
  searchShow: false,
  excelBtn: true,
  printBtn: true,
  viewBtn: true,
  column: [
    {
      label: '注册赠送积分',
      prop: 'registerIntegral',
      type: 'number',
      rules: [{
        required: true,
        message: '请输入注册赠送积分',
        trigger: 'blur'
      }]
    },
    {
      label: '首充赠送积分',
      prop: 'firstRechargeIntegral',
      type: 'number',
      rules: [{
        required: true,
        message: '请输入首充赠送积分',
        trigger: 'blur'
      }]
    },
    {
      label: '签到赠送积分',
      prop: 'signIntegral',
      type: 'number',
      rules: [{
        required: true,
        message: '请输入签到赠送积分',
        trigger: 'blur'
      }]
    },
    {
      label: '推荐赠送积分',
      prop: 'recommendIntegral',
      type: 'number',
      rules: [{
        required: true,
        message: '请输入推荐赠送积分',
        trigger: 'blur'
      }]
    },
    {
      label: '红包开关',
      prop: 'redPacketSwitch',
      type: 'select',
      dicData: [
        {
          label: '关闭',
          value: 0
        },
        {
          label: '开启',
          value: 1
        }
      ],
      rules: [{
        required: true,
        message: '请选择红包开关',
        trigger: 'blur'
      }]
    },
    {
      label: '创建时间',
      prop: 'createTime',
      type: 'datetime',
      addDisplay: false,
      editDisplay: false,
      viewDisplay: true
    },
    {
      label: '更新时间',
      prop: 'updateTime',
      type: 'datetime',
      addDisplay: false,
      editDisplay: false,
      viewDisplay: true
    }
  ]
}
