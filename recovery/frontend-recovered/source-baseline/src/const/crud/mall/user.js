export const tableOption = {
  dialogDrag: true,
  border: true,
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
      label: '头像',
      prop: 'avatar',
      type: 'upload',
      dataType: 'string',
      fileType: 'img',
      limit: 1,
      imgWidth: 50,
      listType: 'picture-img',
      editDisplay: false,
      addDisplay: false
    },
    {
      label: '昵称',
      prop: 'nickname',
      search: true
    },
    {
      label: '真实姓名',
      prop: 'realName',
      search: true
    },
    {
      label: '手机号',
      prop: 'phone',
      search: true
    },
    {
      label: '性别',
      prop: 'sex',
      type: 'select',
      dicData: [{
        label: '未知',
        value: '0'
      }, {
        label: '男',
        value: '1'
      }, {
        label: '女',
        value: '2'
      }]
    },
    {
      label: '城市',
      prop: 'city'
    },
    {
      label: '创建时间',
      prop: 'createTime',
      type: 'datetime',
      format: 'YYYY-MM-DD HH:mm:ss',
      valueFormat: 'YYYY-MM-DD HH:mm:ss',
      editDisplay: false,
      addDisplay: false
    }
  ]
}
