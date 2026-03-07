<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="用户名称" prop="userName">
        <el-input
          v-model="queryParams.userName"
          placeholder="支持模糊查找"
          clearable
          style="width: 200px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="手机号码" prop="phonenumber">
        <el-input
          v-model="queryParams.phonenumber"
          placeholder="支持模糊查找"
          clearable
          style="width: 200px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="所属一级" prop="parentDistributorId">
        <el-input
          v-model="queryParams.parentDistributorId"
          placeholder="一级经销商ID"
          clearable
          style="width: 140px"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="用户状态" clearable style="width: 120px">
          <el-option label="正常" value="0" />
          <el-option label="停用" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete">删除</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column label="用户编号" align="center" prop="userId" width="80" />
      <el-table-column label="用户名称" align="center" prop="userName" min-width="100" show-overflow-tooltip />
      <el-table-column label="用户昵称" align="center" prop="nickName" min-width="100" show-overflow-tooltip />
      <el-table-column label="手机号码" align="center" prop="phonenumber" width="120" />
      <el-table-column label="所属一级" align="center" prop="parentDistributorName" min-width="120" show-overflow-tooltip>
        <template #default="scope">
          <span>{{ scope.row.parentDistributorName || '—' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" width="80">
        <template #default="scope">
          <el-tag :type="scope.row.status === '0' ? 'success' : 'danger'">
            {{ scope.row.status === '0' ? '正常' : '停用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="160" />
      <el-table-column label="操作" align="center" width="140" fixed="right">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="用户名称" prop="userName" v-if="!form.userId">
          <el-input v-model="form.userName" placeholder="请输入用户名称" maxlength="30" />
        </el-form-item>
        <el-form-item label="用户密码" prop="password" v-if="!form.userId">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" maxlength="20" show-password />
        </el-form-item>
        <el-form-item label="用户昵称" prop="nickName">
          <el-input v-model="form.nickName" placeholder="请输入用户昵称" maxlength="30" />
        </el-form-item>
        <el-form-item label="手机号码" prop="phonenumber">
          <el-input v-model="form.phonenumber" placeholder="请输入手机号码" maxlength="11" />
        </el-form-item>
        <el-form-item label="所属一级" prop="parentDistributorId">
          <el-select
            v-model="form.parentDistributorId"
            placeholder="请选择所属一级经销商"
            filterable
            clearable
            style="width: 100%"
          >
            <el-option
              v-for="item in distributorL1Options"
              :key="item.userId"
              :label="item.label"
              :value="item.userId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio value="0">正常</el-radio>
            <el-radio value="1">停用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="DistributorL2">
import { listUser, getUser, addUser, updateUser, delUser } from '@/api/system/user'

const { proxy } = getCurrentInstance()
const userList = ref([])
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref('')
const open = ref(false)
const distributorL1Options = ref([])

const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  userName: undefined,
  phonenumber: undefined,
  status: undefined,
  parentDistributorId: undefined,
  dealerLevel: 2
})

const form = ref({
  userId: undefined,
  userName: undefined,
  nickName: undefined,
  password: undefined,
  phonenumber: undefined,
  status: '0',
  dealerLevel: 2,
  parentDistributorId: undefined
})

const rules = {
  userName: [{ required: true, message: '用户名称不能为空', trigger: 'blur' }],
  nickName: [{ required: true, message: '用户昵称不能为空', trigger: 'blur' }],
  phonenumber: [{ pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }],
  parentDistributorId: [{ required: true, message: '请选择所属一级经销商', trigger: 'change' }]
}

function loadDistributorL1() {
  listUser({ pageNum: 1, pageSize: 1000, dealerLevel: 1 }).then(res => {
    const rows = res.rows || res.data?.records || []
    distributorL1Options.value = rows.map(item => ({
      userId: item.userId,
      label: (item.nickName || item.userName) + (item.phonenumber ? ' ' + item.phonenumber : '')
    }))
  }).catch(() => {})
}

function getList() {
  loading.value = true
  listUser(queryParams.value)
    .then(res => {
      userList.value = res.rows || res.data?.records || []
      total.value = res.total ?? res.data?.total ?? 0
    })
    .catch(() => { userList.value = []; total.value = 0 })
    .finally(() => { loading.value = false })
}

function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

function resetQuery() {
  proxy.resetForm('queryRef')
  queryParams.value.userName = undefined
  queryParams.value.phonenumber = undefined
  queryParams.value.status = undefined
  queryParams.value.parentDistributorId = undefined
  queryParams.value.pageNum = 1
  handleQuery()
}

function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.userId)
  single.value = selection.length !== 1
  multiple.value = !selection.length
}

function reset() {
  form.value = {
    userId: undefined,
    userName: undefined,
    nickName: undefined,
    password: undefined,
    phonenumber: undefined,
    status: '0',
    dealerLevel: 2,
    parentDistributorId: undefined
  }
  proxy.resetForm('formRef')
}

function cancel() {
  open.value = false
  reset()
}

function handleAdd() {
  reset()
  loadDistributorL1()
  title.value = '新增二级经销商'
  open.value = true
}

function handleUpdate(row) {
  reset()
  const userId = row?.userId ?? ids.value?.[0]
  if (!userId) return
  getUser(userId).then(res => {
    const data = res.data || res
    form.value = { ...data, password: '', dealerLevel: 2 }
    loadDistributorL1()
    title.value = '修改二级经销商'
    open.value = true
  }).catch(() => {})
}

function submitForm() {
  proxy.$refs.formRef.validate(valid => {
    if (!valid) return
    const submit = form.value.userId ? updateUser(form.value) : addUser({ ...form.value, dealerLevel: 2 })
    submit.then(() => {
      proxy.$modal.msgSuccess(form.value.userId ? '修改成功' : '新增成功')
      open.value = false
      getList()
    }).catch(() => {})
  })
}

function handleDelete(row) {
  const userIds = row?.userId != null ? [row.userId] : ids.value
  if (!userIds?.length) return
  proxy.$modal.confirm('是否确认删除所选用户？').then(() => {
    const promises = userIds.map(id => delUser(id))
    return Promise.all(promises)
  }).then(() => {
    proxy.$modal.msgSuccess('删除成功')
    getList()
  }).catch(() => {})
}

getList()
</script>
