<template>
  <div class="app-container">
    <el-card shadow="never">
      <template #header><div class="card-header"><span>字典数据</span><el-button type="primary" @click="handleAdd">新增</el-button></div></template>
      <el-form :inline="true" :model="query" class="query-form">
        <el-form-item label="字典类型"><el-input v-model="query.dictType" clearable placeholder="请输入字典类型" @keyup.enter="loadList" /></el-form-item>
        <el-form-item label="字典标签"><el-input v-model="query.dictLabel" clearable placeholder="请输入字典标签" @keyup.enter="loadList" /></el-form-item>
        <el-form-item><el-button type="primary" @click="loadList">查询</el-button><el-button @click="resetQuery">重置</el-button></el-form-item>
      </el-form>
      <el-table v-loading="loading" :data="rows" border stripe>
        <el-table-column prop="dictCode" label="字典编码" width="110" />
        <el-table-column prop="dictSort" label="排序" width="80" />
        <el-table-column prop="dictLabel" label="字典标签" min-width="140" />
        <el-table-column prop="dictValue" label="字典键值" min-width="140" />
        <el-table-column prop="dictType" label="字典类型" min-width="180" />
        <el-table-column prop="listClass" label="回显样式" width="120" />
        <el-table-column prop="status" label="状态" width="90"><template #default="scope"><el-tag :type="scope.row.status === '0' ? 'success' : 'danger'">{{ scope.row.status === '0' ? '正常' : '停用' }}</el-tag></template></el-table-column>
        <el-table-column label="操作" width="150" fixed="right"><template #default="scope"><el-button link type="primary" @click="handleEdit(scope.row)">编辑</el-button><el-button link type="danger" @click="handleDelete(scope.row)">删除</el-button></template></el-table-column>
      </el-table>
      <pagination v-show="total > 0" v-model:page="query.pageNum" v-model:limit="query.pageSize" :total="total" @pagination="loadList" />
    </el-card>
    <el-dialog v-model="dialogOpen" :title="dialogTitle" width="560px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="排序" prop="dictSort"><el-input-number v-model="form.dictSort" :min="0" /></el-form-item>
        <el-form-item label="字典标签" prop="dictLabel"><el-input v-model="form.dictLabel" /></el-form-item>
        <el-form-item label="字典键值" prop="dictValue"><el-input v-model="form.dictValue" /></el-form-item>
        <el-form-item label="字典类型" prop="dictType"><el-input v-model="form.dictType" /></el-form-item>
        <el-form-item label="回显样式"><el-input v-model="form.listClass" /></el-form-item>
        <el-form-item label="状态"><el-radio-group v-model="form.status"><el-radio value="0">正常</el-radio><el-radio value="1">停用</el-radio></el-radio-group></el-form-item>
        <el-form-item label="备注"><el-input v-model="form.remark" type="textarea" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogOpen = false">取消</el-button><el-button type="primary" :loading="saving" @click="submitForm">保存</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup name="DictData">
import { ref, reactive, onMounted, getCurrentInstance } from 'vue'
import { listDictData, addDictData, updateDictData, delDictData } from '@/api/system/dict/data'

const { proxy } = getCurrentInstance()
const rows = ref([])
const total = ref(0)
const loading = ref(false)
const saving = ref(false)
const dialogOpen = ref(false)
const dialogTitle = ref('新增字典数据')
const formRef = ref()
const query = reactive({ pageNum: 1, pageSize: 10, dictType: '', dictLabel: '' })
const form = reactive({ dictCode: undefined, dictSort: 0, dictLabel: '', dictValue: '', dictType: '', listClass: 'default', status: '0', remark: '' })
const rules = { dictLabel: [{ required: true, message: '请输入字典标签', trigger: 'blur' }], dictValue: [{ required: true, message: '请输入字典键值', trigger: 'blur' }], dictType: [{ required: true, message: '请输入字典类型', trigger: 'blur' }] }
function unwrap(res) { return res && res.data != null ? res.data : res || {} }
function loadList() { loading.value = true; listDictData(query).then(res => { const d = unwrap(res); rows.value = d.rows || d.records || []; total.value = Number(d.total || 0) }).finally(() => { loading.value = false }) }
function resetQuery() { query.pageNum = 1; query.dictType = ''; query.dictLabel = ''; loadList() }
function resetForm() { Object.assign(form, { dictCode: undefined, dictSort: 0, dictLabel: '', dictValue: '', dictType: '', listClass: 'default', status: '0', remark: '' }) }
function handleAdd() { resetForm(); dialogTitle.value = '新增字典数据'; dialogOpen.value = true }
function handleEdit(row) { Object.assign(form, row); dialogTitle.value = '编辑字典数据'; dialogOpen.value = true }
function handleDelete(row) { proxy.$modal.confirm(`是否删除字典数据“${row.dictLabel}”？`).then(() => delDictData(row.dictCode)).then(() => { proxy.$modal.msgSuccess('删除成功'); loadList() }).catch(() => {}) }
function submitForm() { formRef.value.validate(valid => { if (!valid) return; saving.value = true; const action = form.dictCode ? updateDictData(form) : addDictData(form); action.then(() => { proxy.$modal.msgSuccess('保存成功'); dialogOpen.value = false; loadList() }).finally(() => { saving.value = false }) }) }
onMounted(loadList)
</script>

<style scoped>
.app-container { padding: 20px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.query-form { margin-bottom: 12px; }
</style>
