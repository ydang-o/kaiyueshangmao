<!--
  积分流水：全平台分页，支持 userId、operType、beginTime、endTime 筛选
-->
<template>
  <div class="execution">
    <basic-container>
      <avue-crud
        ref="crud"
        :page="page"
        :data="tableData"
        :table-loading="tableLoading"
        :option="tableOption"
        @on-load="getList"
        @search-change="searchChange"
        @refresh-change="refreshChange"
      />
    </basic-container>
  </div>
</template>

<script>
import { getPage } from '@/api/mall/integralflow'

export default {
  name: 'IntegralFlow',
  data() {
    return {
      tableData: [],
      page: {
        total: 0,
        currentPage: 1,
        pageSize: 20
      },
      tableLoading: false,
      tableOption: {
        border: true,
        index: true,
        indexLabel: '序号',
        stripe: true,
        menuAlign: 'center',
        align: 'center',
        addBtn: false,
        editBtn: false,
        delBtn: false,
        column: [
          { label: 'ID', prop: 'id', hide: true },
          { label: '用户ID', prop: 'userId', search: true },
          { label: '操作类型', prop: 'operType', search: true },
          { label: '积分变动', prop: 'points', type: 'number' },
          { label: '变动后余额', prop: 'balanceAfter', type: 'number' },
          { label: '关联单号/说明', prop: 'bizNo' },
          { label: '创建时间', prop: 'createTime', type: 'datetime', format: 'yyyy-MM-dd HH:mm', valueFormat: 'yyyy-MM-dd HH:mm:ss' }
        ]
      }
    }
  },
  methods: {
    getList(page, params) {
      this.tableLoading = true
      const query = {
        current: page.currentPage,
        size: page.pageSize,
        ...(params || {})
      }
      getPage(query)
        .then((res) => {
          const d = res.data && res.data.data
          this.tableData = (d && d.records) ? d.records : (res.data && res.data.records) ? res.data.records : []
          this.page.total = (d && d.total) ? d.total : (res.data && res.data.total) ? res.data.total : 0
          this.tableLoading = false
        })
        .catch(() => {
          this.tableLoading = false
        })
    },
    searchChange(form, done) {
      this.page.currentPage = 1
      this.getList(this.page, form)
      done()
    },
    refreshChange() {
      this.getList(this.page)
    }
  }
}
</script>
