<!--
  系统参数配置：客服、短信策略等 sys_config
-->
<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
      <el-form-item label="参数名称" prop="configName">
        <el-input v-model="queryParams.configName" placeholder="请输入参数名称" clearable style="width: 200px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="参数键名" prop="configKey">
        <el-input v-model="queryParams.configKey" placeholder="请输入参数键名" clearable style="width: 200px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['system:config:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate" v-hasPermi="['system:config:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['system:config:remove']">删除</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="configList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="参数主键" align="center" prop="configId" width="100" />
      <el-table-column label="参数名称" align="center" prop="configName" :show-overflow-tooltip="true" />
      <el-table-column label="参数键名" align="center" prop="configKey" :show-overflow-tooltip="true" />
      <el-table-column label="参数键值" align="center" prop="configValue" :show-overflow-tooltip="true" />
      <el-table-column label="系统内置" align="center" prop="configType" width="100">
        <template #default="scope">
          <el-tag v-if="scope.row.configType === 'Y'" type="danger">是</el-tag>
          <el-tag v-else type="info">否</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="160" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:config:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['system:config:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="configRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="参数名称" prop="configName">
          <el-input v-model="form.configName" placeholder="请输入参数名称" />
        </el-form-item>
        <el-form-item label="参数键名" prop="configKey">
          <el-input v-model="form.configKey" placeholder="请输入参数键名" :disabled="!!form.configId" />
        </el-form-item>
        <el-form-item label="参数键值" prop="configValue">
          <el-input v-model="form.configValue" type="textarea" placeholder="请输入参数键值" :rows="3" />
        </el-form-item>
        <el-form-item label="系统内置" prop="configType">
          <el-radio-group v-model="form.configType">
            <el-radio label="Y">是</el-radio>
            <el-radio label="N">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Config">
import { listConfig, getConfig, addConfig, updateConfig, delConfig } from '@/api/system/config'

const { proxy } = getCurrentInstance()
const configList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref('')

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    configName: undefined,
    configKey: undefined
  },
  rules: {
    configName: [{ required: true, message: '参数名称不能为空', trigger: 'blur' }],
    configKey: [{ required: true, message: '参数键名不能为空', trigger: 'blur' }],
    configValue: [{ required: true, message: '参数键值不能为空', trigger: 'blur' }]
  }
})
const { queryParams, form, rules } = toRefs(data)

function getList() {
  loading.value = true
  listConfig(queryParams.value).then((res) => {
    configList.value = res.rows != null ? res.rows : (res.data && res.data.records) ? res.data.records : []
    total.value = res.total != null ? res.total : (res.data && res.data.total) ? res.data.total : 0
    loading.value = false
  }).catch(() => { loading.value = false })
}

function cancel() {
  open.value = false
  reset()
}

function reset() {
  form.value = {
    configId: undefined,
    configName: undefined,
    configKey: undefined,
    configValue: undefined,
    configType: 'N',
    remark: undefined
  }
  proxy.resetForm('configRef')
}

function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

function resetQuery() {
  proxy.resetForm('queryRef')
  handleQuery()
}

function handleSelectionChange(selection) {
  ids.value = selection.map((item) => item.configId)
  single.value = selection.length !== 1
  multiple.value = !selection.length
}

function handleAdd() {
  reset()
  open.value = true
  title.value = '添加参数'
}

function handleUpdate(row) {
  reset()
  const configId = row.configId != null ? row.configId : ids.value[0]
  getConfig(configId).then((res) => {
    form.value = res.data != null ? res.data : res
    open.value = true
    title.value = '修改参数'
  })
}

function submitForm() {
  proxy.$refs['configRef'].validate((valid) => {
    if (valid) {
      if (form.value.configId != null) {
        updateConfig(form.value).then(() => {
          proxy.$modal.msgSuccess('修改成功')
          open.value = false
          getList()
        })
      } else {
        addConfig(form.value).then(() => {
          proxy.$modal.msgSuccess('新增成功')
          open.value = false
          getList()
        })
      }
    }
  })
}

function handleDelete(row) {
  const configIds = row.configId != null ? [row.configId] : ids.value
  proxy.$modal.confirm('是否确认删除参数编号为"' + configIds.join(',') + '"的数据？').then(() => {
    return delConfig(configIds.join(','))
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess('删除成功')
  }).catch(() => {})
}

getList()
</script>
