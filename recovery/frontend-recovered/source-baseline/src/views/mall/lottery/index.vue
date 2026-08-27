<template>
  <div class="execution">
    <basic-container>
      <el-tabs v-model="activeName">
        <!-- 标签页 1：活动配置列表 -->
        <el-tab-pane label="活动列表" name="list">
          <avue-crud
            ref="crud"
            :page="page"
            :data="tableData"
            :permission="permissionList"
            :table-loading="tableLoading"
            :option="tableOption"
            @on-load="getList"
            @refresh-change="refreshChange"
            @row-save="handleSaveConfig"
            @row-update="handleUpdateConfig"
            @row-del="handleDelConfig"
            @search-change="searchChange"
          >
            <!-- 奖品列表编辑插槽 -->
            <template #prizeList-form="scope">
              <div style="padding: 10px; border: 1px solid #ebeef5; border-radius: 4px;">
                <el-button type="primary" icon="Plus" size="small" @click="handleAddPrize" style="margin-bottom: 10px;">添加奖品</el-button>
                <el-table :data="form.prizeList || []" border size="small">
                  <el-table-column label="排序" width="70">
                    <template #default="item">
                      <el-input-number v-model="item.row.sortOrder" :min="0" controls-position="right" style="width: 100%"></el-input-number>
                    </template>
                  </el-table-column>
                  <el-table-column label="类型" width="100">
                    <template #default="item">
                      <el-select v-model="item.row.prizeType" @change="() => handleTypeChange(item.row)">
                        <el-option label="商品" value="0"></el-option>
                        <el-option label="积分" value="1"></el-option>
                      </el-select>
                    </template>
                  </el-table-column>
                  <el-table-column label="奖品内容" min-width="180">
                    <template #default="item">
                      <div v-if="item.row.prizeType === '1'">
                        <el-input-number v-model="item.row.pointAmount" :min="1" size="small"></el-input-number> 积分
                      </div>
                      <div v-else>
                        <el-select
                          v-model="item.row.goodsId"
                          filterable
                          remote
                          placeholder="搜索商品"
                          :remote-method="queryGoods"
                          :loading="loadingGoods"
                          @change="(val) => handleGoodsChange(val, item.row)"
                        >
                          <el-option v-for="g in goodsOptions" :key="g.id" :label="g.name" :value="g.id"></el-option>
                        </el-select>
                      </div>
                    </template>
                  </el-table-column>
                  <el-table-column label="展示名称">
                    <template #default="item">
                      <el-input v-model="item.row.prizeName" placeholder="奖品简称"></el-input>
                    </template>
                  </el-table-column>
                  <el-table-column label="概率(%)" width="100">
                    <template #default="item">
                      <el-input-number v-model="item.row.probability" :precision="2" :min="0" :max="100" controls-position="right" style="width: 100%"></el-input-number>
                    </template>
                  </el-table-column>
                  <el-table-column label="操作" width="60" align="center">
                    <template #default="item">
                      <el-button link type="danger" icon="Delete" @click="handleRemovePrize(item.$index)"></el-button>
                    </template>
                  </el-table-column>
                </el-table>
                <div class="el-upload__tip">注：(所有奖品概率 + 未中奖概率) 总和必须等于 100%</div>
              </div>
            </template>
          </avue-crud>
        </el-tab-pane>

        <!-- 标签页 2：抽奖记录 -->
        <el-tab-pane label="抽奖记录" name="record">
          <avue-crud
            :page="recordPage"
            :data="recordData"
            :table-loading="recordLoading"
            :option="recordOption"
            @on-load="getRecordList"
            @search-change="searchRecordChange"
            @refresh-change="getRecordList"
          ></avue-crud>
        </el-tab-pane>
      </el-tabs>
    </basic-container>
  </div>
</template>

<script setup name="Lottery">
import { ref, reactive, computed, getCurrentInstance } from 'vue'
import { getConfigPage, getConfigById, saveConfig, delConfig, getRecordPage } from '@/api/mall/lottery'
import { getPage as getGoodsPage } from '@/api/mall/goodsspu'
import { checkPermi } from '@/utils/permission'

const { proxy } = getCurrentInstance()
const activeName = ref('list')
const form = ref({ prizeList: [] })
const tableData = ref([])
const tableLoading = ref(false)
const page = reactive({ total: 0, currentPage: 1, pageSize: 10 })

// 权限控制
const permissionList = computed(() => ({
  addBtn: checkPermi(['mall:lottery:config']),
  editBtn: checkPermi(['mall:lottery:config']),
  delBtn: checkPermi(['mall:lottery:config']),
  viewBtn: true
}))

// 配置列表 Option
const tableOption = {
  border: true,
  index: true,
  stripe: true,
  menuAlign: 'center',
  align: 'center',
  dialogWidth: '80%',
  column: [
    { label: '活动ID', prop: 'id', display: false },
    {
      label: '状态',
      prop: 'status',
      type: 'select',
      dicData: [{ label: '开启', value: '1' }, { label: '关闭', value: '0' }],
      search: true,
      rules: [{ required: true, message: '请选择状态' }]
    },
    { label: '未中奖概率(%)', prop: 'noPrizeProbability', type: 'number', precision: 2, value: 50, rules: [{ required: true }] },
    { label: '单次消耗积分', prop: 'costPoints', type: 'number', value: 10, rules: [{ required: true }] },
    { label: '每日抽奖上限', prop: 'dailyLimit', type: 'number', value: 3, rules: [{ required: true }] },
    {
      label: '奖品配置',
      prop: 'prizeList',
      formslot: true, // 使用奖品编辑插槽
      span: 24,
      hide: true // 列表隐藏
    },
    { label: '创建时间', prop: 'createTime', display: false }
  ]
}

// 记录列表相关
const recordData = ref([])
const recordLoading = ref(false)
const recordPage = reactive({ total: 0, currentPage: 1, pageSize: 10 })
const recordOption = {
  border: true,
  index: true,
  addBtn: false,
  editBtn: false,
  delBtn: false,
  column: [
    { label: '用户ID', prop: 'userId', search: true },
    { label: '中奖', prop: 'isWin', type: 'select', dicData: [{ label: '否', value: '0' }, { label: '是', value: '1' }], search: true },
    { label: '奖品名称', prop: 'prizeName' },
    { label: '消耗积分', prop: 'costPoints' },
    { label: '发放状态', prop: 'grantStatus', type: 'select', dicData: [{ label: '待发', value: '0' }, { label: '已发', value: '1' }] },
    { label: '抽奖时间', prop: 'createTime', width: 160 }
  ]
}

// 方法：获取配置列表
function getList(p, params) {
  tableLoading.value = true
  const query = {
    pageNum: p.currentPage,
    pageSize: p.pageSize,
    ...params
  }
  getConfigPage(query).then(res => {
    // 兼容多种后端返回：res.data / res.data.data / res，以及 records/rows 等
    const raw = res.data !== undefined ? res.data : res
    const list = raw?.data?.records ?? raw?.data?.rows ?? raw?.records ?? raw?.rows ?? (Array.isArray(raw?.data) ? raw.data : (Array.isArray(raw) ? raw : []))
    const total = raw?.data?.total ?? raw?.total ?? list.length
    tableData.value = Array.isArray(list) ? list : []
    page.total = Number(total) || 0
  }).catch(() => {
    tableData.value = []
    page.total = 0
  }).finally(() => { 
    tableLoading.value = false 
  })
}

// 方法：保存/修改配置（包含强制数据同步与概率校验）
function handleSaveConfig(row, done, loading) {
  // 强制同步：插槽里的 prizeList 可能未挂载到 row 上，需要从 ref 中获取
  const submitData = { 
    ...row, 
    prizeList: form.value.prizeList || [] 
  }
  
  if (!validateProbability(submitData)) {
    loading()
    return
  }
  
  saveConfig(submitData).then(() => {
    proxy.$modal.msgSuccess('新增成功')
    done()
    getList(page)
  }).catch(() => { loading() })
}

function handleUpdateConfig(row, index, done, loading) {
  // 强制同步
  const submitData = { 
    ...row, 
    prizeList: form.value.prizeList || [] 
  }

  if (!validateProbability(submitData)) {
    loading()
    return
  }
  
  saveConfig(submitData).then(() => {
    proxy.$modal.msgSuccess('修改成功')
    done()
    getList(page)
  }).catch(() => { loading() })
}

function handleDelConfig(row) {
  proxy.$modal.confirm(`是否删除ID为 ${row.id} 的活动配置？`).then(() => {
    return delConfig(row.id)
  }).then(() => {
    proxy.$modal.msgSuccess('删除成功')
    getList(page)
  })
}

// 概率校验函数
function validateProbability(row) {
  const prizeList = row.prizeList || []
  if (prizeList.length === 0) {
    proxy.$modal.msgWarning('请至少添加一个奖品')
    return false
  }
  const prizeProb = prizeList.reduce((sum, p) => sum + Number(p.probability || 0), 0)
  const total = Number(row.noPrizeProbability || 0) + prizeProb
  if (Math.abs(total - 100) > 0.01) {
    proxy.$modal.msgWarning(`当前概率总和为 ${total.toFixed(2)}% (未中奖${row.noPrizeProbability}% + 奖品${prizeProb.toFixed(2)}%)，必须调整至 100%`)
    return false
  }
  return true
}

// 奖品编辑逻辑
function handleAddPrize() {
  if (!form.value.prizeList) form.value.prizeList = []
  form.value.prizeList.push({
    sortOrder: form.value.prizeList.length,
    prizeType: '0',
    goodsId: '',
    pointAmount: 0,
    prizeName: '',
    probability: 0
  })
}

function handleRemovePrize(index) {
  form.value.prizeList.splice(index, 1)
}

function handleTypeChange(row) {
  row.goodsId = ''
  row.pointAmount = 0
  row.prizeName = ''
}

// 商品搜索相关
const goodsOptions = ref([])
const loadingGoods = ref(false)
function queryGoods(query) {
  if (!query) return
  loadingGoods.value = true
  getGoodsPage({ current: 1, size: 20, name: query, shelf: '1' }).then(res => {
    goodsOptions.value = res.data.records || []
  }).finally(() => { loadingGoods.value = false })
}

function handleGoodsChange(val, row) {
  const g = goodsOptions.value.find(item => item.id === val)
  if (g) {
    row.prizeName = g.name
    row.prizePic = g.picUrls ? g.picUrls[0] : ''
  }
}

// 抽奖记录逻辑
function getRecordList(p, params) {
  recordLoading.value = true
  const query = {
    pageNum: p.currentPage,
    pageSize: p.pageSize,
    ...params
  }
  getRecordPage(query).then(res => {
    const raw = res.data !== undefined ? res.data : res
    const list = raw?.data?.records ?? raw?.data?.rows ?? raw?.records ?? raw?.rows ?? (Array.isArray(raw?.data) ? raw.data : (Array.isArray(raw) ? raw : []))
    const total = raw?.data?.total ?? raw?.total ?? list.length
    recordData.value = Array.isArray(list) ? list : []
    recordPage.total = Number(total) || 0
  }).catch(() => {
    recordData.value = []
    recordPage.total = 0
  }).finally(() => { 
    recordLoading.value = false 
  })
}

function searchRecordChange(params, done) {
  recordPage.currentPage = 1
  getRecordList(recordPage, params)
  done()
}

function searchChange(params, done) {
  page.currentPage = 1
  getList(page, params)
  done()
}

function refreshChange() {
  getList(page)
}
</script>

<style scoped>
.mb-10 { margin-bottom: 10px; }
:deep(.el-input-number.is-controls-right .el-input__inner) { text-align: left; }
</style>
