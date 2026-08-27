export const tableOption = {
  dialogDrag: true,
  border: true,
  indexLabel: '序号',
  stripe: true,
  menuAlign: 'center',
  align: 'center',
  menuType: 'text',
  searchShow: true,
  excelBtn: true,
  printBtn: true,
  viewBtn: true,
  column: [
    {
      label: '昵称',
      prop: 'nickname',
      search: true,
      editDisplay: false,
      addDisplay: false
    },
    {
      label: '手机号',
      prop: 'phone',
      search: true,
      width: 120,
      editDisplay: false,
      addDisplay: false
    },
    {
      label: '会员码',
      prop: 'memberCode',
      search: true
    },
    {
      label: '身份类型',
      prop: 'identityType',
      type: 'select',
      dicData: [{
        label: '普通会员',
        value: '1'
      }, {
        label: 'VIP会员',
        value: '2'
      }]
    },
    {
      label: '积分',
      prop: 'points',
      type: 'number'
    },
    {
      label: '余额',
      prop: 'balance',
      type: 'number'
    },
    {
      label: '等级',
      prop: 'level',
      type: 'number'
    },
    {
      label: '成长值',
      prop: 'growth',
      type: 'number',
      hide: true
    }
  ]
}
