<template>
  <div class="execution">
    <basic-container>
      <!-- 统计卡片 -->
      <el-row :gutter="16" class="mb-16">
        <el-col :span="8">
          <el-card shadow="hover">
            <template #header><span>总核销量</span></template>
            <div class="stat-value">{{ statistics.totalVerify || 0 }}</div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover">
            <template #header><span>今日核销</span></template>
            <div class="stat-value">{{ statistics.todayVerify || 0 }}</div>
          </el-card>
        </el-col>
      </el-row>

      <avue-crud ref="crud"
                 v-model="form"
                 :page="page"
                 :data="tableData"
                 :permission="permissionList"
                 :table-loading="tableLoading"
                 :option="tableOption"
                 @on-load="getList"
                 @search-change="searchChange"
                 @refresh-change="refreshChange"
                 @row-del="rowDel">
        <!-- 顶部左侧按钮插槽 -->
        <template #menu-left>
          <el-button
            v-if="permissionList.addBtn"
            type="primary"
            icon="Plus"
            @click="openDistributeDialog"
          >发放商品券</el-button>
        </template>

        <!-- 商品图片展示 -->
        <template #goodsPic="scope">
          <el-image
            v-if="scope.row.goodsPic"
            :src="formatImageUrl(scope.row.goodsPic)"
            :preview-src-list="[formatImageUrl(scope.row.goodsPic)]"
            fit="cover"
            style="width: 50px; height: 50px; border-radius: 4px"
            preview-teleported
          />
        </template>
      </avue-crud>

      <!-- 发放商品券弹窗 -->
      <el-dialog
        v-model="distributeVisible"
        title="发放商品券"
        width="500px"
        append-to-body
      >
        <el-form :model="distributeForm" :rules="distributeRules" ref="distributeFormRef" label-width="100px">
          <el-form-item label="用户手机号" prop="phone">
            <el-input v-model="distributeForm.phone" placeholder="请输入已注册的手机号" maxlength="11"></el-input>
          </el-form-item>
          <el-form-item label="选择商品券" prop="goodsId">
            <el-select
              v-model="distributeForm.goodsId"
              filterable
              remote
              reserve-keyword
              placeholder="搜索商品券(类型=2)"
              :remote-method="queryCouponGoods"
              :loading="loadingGoods"
              style="width: 100%"
            >
              <el-option
                v-for="item in goodsOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              >
                <span style="float: left">{{ item.name }}</span>
                <span style="float: right; color: #8492a6; font-size: 13px">ID:{{ item.id }}</span>
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="发放数量" prop="count">
            <el-input-number v-model="distributeForm.count" :min="1" :max="100" style="width: 100%"></el-input-number>
          </el-form-item>
          <el-form-item label="有效天数" prop="validityDays">
            <el-input-number v-model="distributeForm.validityDays" :min="1" style="width: 100%"></el-input-number>
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="distributeVisible = false">取 消</el-button>
          <el-button type="primary" @click="handleDistribute" :loading="distributeLoading">确 定</el-button>
        </template>
      </el-dialog>
    </basic-container>
  </div>
</template>

<script setup name="Coupon">
import { ref, reactive, computed, getCurrentInstance, onMounted } from 'vue'
import { getPage, delObj, getStatistics, distributeCoupon } from '@/api/mall/coupon'
import { getPage as getGoodsPage } from '@/api/mall/goodsspu'
import { tableOption } from '@/const/crud/mall/coupon'
import useUserStore from '@/store/modules/user'

const { proxy } = getCurrentInstance()
const tableData = ref([])
const tableLoading = ref(false)
const page = reactive({ total: 0, currentPage: 1, pageSize: 20 })
const form = ref({})
const statistics = reactive({ totalVerify: 0, todayVerify: 0 })

// 发放弹窗相关
const distributeVisible = ref(false)
const distributeLoading = ref(false)
const distributeFormRef = ref(null)
const distributeForm = reactive({
  phone: '',
  goodsId: '',
  count: 1,
  validityDays: 365
})
const distributeRules = {
  phone: [
    { required: true, message: '请输入用户手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号格式', trigger: 'blur' }
  ],
  goodsId: [{ required: true, message: '请选择商品券', trigger: 'change' }],
  count: [{ required: true, message: '请输入发放数量', trigger: 'blur' }],
  validityDays: [{ required: true, message: '请输入有效天数', trigger: 'blur' }]
}

// 商品搜索相关
const goodsOptions = ref([])
const loadingGoods = ref(false)

const permissionList = computed(() => {
  const permissions = useUserStore().permissions || []
  const has = (key) => permissions.includes(key) || permissions.includes('*:*:*')
  return {
    addBtn: has('mall:coupon:add'),
    delBtn: has('mall:coupon:del'),
    editBtn: has('mall:coupon:edit'),
    viewBtn: true
  }
})

// 图片路径格式化
const baseApi = import.meta.env.VITE_APP_BASE_API || ''
const formatImageUrl = (url) => {
  if (!url) return ''
  if (/^https?:\/\//i.test(url)) return url
  let path = url.startsWith('/') ? url : '/' + url
  if (baseApi && !path.startsWith(baseApi)) return baseApi + path
  return path
}

function loadStatistics() {
  getStatistics().then((res) => {
    const d = res.data || res
    statistics.totalVerify = d.totalVerify ?? d.total ?? 0
    statistics.todayVerify = d.todayVerify ?? d.today ?? 0
  })
}

function getList(p, params) {
  tableLoading.value = true
  getPage(Object.assign({
    current: p.currentPage,
    size: p.pageSize,
    pageNum: p.currentPage,
    pageSize: p.pageSize
  }, params)).then(res => {
    const data = res.data || res
    tableData.value = data.records || data.rows || []
    page.total = data.total || 0
  }).finally(() => {
    tableLoading.value = false
  })
}

function rowDel(row) {
  proxy.$modal.confirm('是否确认删除ID为' + row.id + '的记录？').then(() => {
    return delObj(row.id)
  }).then(() => {
    proxy.$modal.msgSuccess('删除成功')
    refreshChange()
  })
}

function searchChange(params, done) {
  page.currentPage = 1
  getList(page, params)
  done()
}

function refreshChange() {
  getList(page)
  loadStatistics()
}

// 发放弹窗逻辑
function openDistributeDialog() {
  distributeVisible.value = true
  Object.assign(distributeForm, {
    phone: '',
    goodsId: '',
    count: 1,
    validityDays: 365
  })
  goodsOptions.value = []
}

function queryCouponGoods(query) {
  if (!query) return
  loadingGoods.value = true
  // 搜索 goodsType=2 的商品（商品券）
  getGoodsPage({
    current: 1,
    size: 50,
    name: query,
    goodsType: '2', // 核心：过滤商品券类型
    shelf: '1'
  }).then(res => {
    goodsOptions.value = res.data.records || []
  }).finally(() => {
    loadingGoods.value = false
  })
}

function handleDistribute() {
  distributeFormRef.value.validate(valid => {
    if (!valid) return
    distributeLoading.value = true
    distributeCoupon(distributeForm).then(res => {
      proxy.$modal.msgSuccess(res.msg || '发放成功')
      distributeVisible.value = false
      refreshChange()
    }).finally(() => {
      distributeLoading.value = false
    })
  })
}

onMounted(() => {
  loadStatistics()
})
</script>

<style scoped>
.mb-16 { margin-bottom: 16px; }
.stat-value { font-size: 24px; font-weight: bold; color: #409EFF; }
</style>
