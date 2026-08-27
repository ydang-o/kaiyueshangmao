export const tableOption = {
  border: true,
  index: true,
  indexLabel: '序号',
  stripe: true,
  menuAlign: 'center',
  align: 'center',
  addBtn: false,
  editBtn: false,
  delBtn: true,
  column: [
    {
      label: 'ID',
      prop: 'id',
      hide: true,
      editDisabled: true,
      addDisplay: false
    },
    {
      label: '券码',
      prop: 'couponCode',
      search: true,
      editDisabled: true
    },
    {
      label: '用户ID',
      prop: 'userId',
      search: true,
      editDisabled: true
    },
    {
      label: '商品名称',
      prop: 'goodsName',
      search: true,
      editDisabled: true
    },
    {
      label: '商品图片',
      prop: 'goodsPic',
      type: 'upload',
      imgWidth: 50,
      listType: 'picture-img',
      editDisabled: true,
      addDisplay: false
    },
    {
      label: '状态',
      prop: 'couponStatus',
      type: 'select',
      search: true,
      dicData: [
        {
          label: '未使用',
          value: 1
        },
        {
          label: '已使用',
          value: 2
        },
        {
          label: '已过期',
          value: 3
        }
      ],
      editDisabled: true
    },
    {
      label: '有效期至',
      prop: 'validityEnd',
      type: 'datetime',
      format: 'yyyy-MM-dd HH:mm:ss',
      valueFormat: 'yyyy-MM-dd HH:mm:ss',
      editDisabled: true
    },
    {
      label: '核销时间',
      prop: 'verifyTime',
      type: 'datetime',
      format: 'yyyy-MM-dd HH:mm:ss',
      valueFormat: 'yyyy-MM-dd HH:mm:ss',
      editDisabled: true,
      addDisplay: false
    },
    {
      label: '核销商家',
      prop: 'verifyDealerName',
      editDisabled: true,
      addDisplay: false
    },
    {
      label: '创建时间',
      prop: 'createTime',
      type: 'datetime',
      format: 'yyyy-MM-dd HH:mm:ss',
      valueFormat: 'yyyy-MM-dd HH:mm:ss',
      editDisabled: true,
      addDisplay: false
    },
  ]
}