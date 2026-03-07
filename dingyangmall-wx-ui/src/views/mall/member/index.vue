<template>
  <div class="app-container">
    <avue-crud
      ref="crud"
      :page="page"
      :data="tableData"
      :permission="permissionList"
      :table-loading="tableLoading"
      :option="tableOption"
      @on-load="getList"
      @refresh-change="refreshChange"
      @row-update="rowUpdate"
      @row-save="rowSave"
      @row-del="rowDel"
      @search-change="searchChange">
    </avue-crud>
  </div>
</template>

<script>
import { getPage, getObj, addObj, putObj, delObj } from '@/api/mall/member'
import { tableOption } from '@/const/crud/mall/member'

export default {
  name: 'MallMember',
  data() {
    return {
      tableData: [],
      page: {
        total: 0, // 总页数
        currentPage: 1, // 当前页数
        pageSize: 20 // 每页显示多少条
      },
      tableLoading: false,
      tableOption: tableOption
    }
  },
  computed: {
    permissionList() {
      return {
        addBtn: this.$auth.hasPermi(['mall:member:add']),
        viewBtn: this.$auth.hasPermi(['mall:member:get']),
        delBtn: this.$auth.hasPermi(['mall:member:del']),
        editBtn: this.$auth.hasPermi(['mall:member:edit'])
      }
    }
  },
  methods: {
    getList(page, params) {
      this.tableLoading = true
      const query = {
        current: page.currentPage,
        size: page.pageSize,
        ...(params || {}),
        ...(this.searchForm || {})
      }
      getPage(query).then(response => {
        const res = response?.data ?? response
        this.tableData = res.records ?? res.data?.records ?? []
        this.page.total = res.total ?? res.data?.total ?? 0
        this.tableLoading = false
      }).catch(() => {
        this.tableData = []
        this.tableLoading = false
      })
    },
    rowDel: function(row, index) {
      var _this = this
      this.$confirm('是否确认删除ID为' + row.id + '的记录', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return delObj(row.id)
      }).then(data => {
        _this.$message({
          showClose: true,
          message: '删除成功',
          type: 'success'
        })
        this.refreshChange()
      })
    },
    rowUpdate: function(row, index, done, loading) {
      putObj(row).then(data => {
        this.$message({
          showClose: true,
          message: '修改成功',
          type: 'success'
        })
        done()
        this.refreshChange()
      }).catch(() => {
        loading()
      })
    },
    rowSave: function(row, done, loading) {
      addObj(row).then(data => {
        this.$message({
          showClose: true,
          message: '添加成功',
          type: 'success'
        })
        done()
        this.refreshChange()
      }).catch(() => {
        loading()
      })
    },
    searchChange(params, done) {
      this.page.currentPage = 1
      this.getList(this.page, params)
      done()
    },
    refreshChange() {
      this.getList(this.page)
    }
  }
}
</script>
