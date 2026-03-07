<template>
  <div class="execution">
    <basic-container>
      <avue-crud
        ref="crud"
        v-model="form"
        :page="page"
        :data="tableData"
        :permission="permissionList"
        :table-loading="tableLoading"
        :option="tableOption"
        @on-load="getList"
        @refresh-change="refreshChange"
        @row-save="handleSave"
        @row-update="handleUpdate"
        @row-del="handleDel"
        @search-change="searchChange"
      >
        <!-- 列表展示 -->
        <template #imageUrl="scope">
          <el-image
            v-if="getImageUrl(scope.row)"
            :src="formatImageUrl(getImageUrl(scope.row))"
            :preview-src-list="[formatImageUrl(getImageUrl(scope.row))]"
            fit="cover"
            style="width: 100px; height: 50px; border-radius: 4px; object-fit: cover;"
            preview-teleported
          >
            <template #error>
              <div class="image-slot">加载失败</div>
            </template>
          </el-image>
          <span v-else class="text-gray-400">无图片</span>
        </template>
        <!-- 表单上传：使用原生 el-upload 并设置 auto-upload="false" 确保只在点击保存时上传 -->
        <template #imageUrl-form="scope">
          <el-upload
            ref="uploadRef"
            action="#"
            list-type="picture-card"
            :auto-upload="false"
            :limit="1"
            :on-change="handleFileChange"
            :on-remove="handleFileRemove"
            :file-list="fileList"
            :disabled="scope.type === 'view'"
          >
            <el-icon><Plus /></el-icon>
          </el-upload>
          <div class="el-upload__tip" v-if="scope.type !== 'view'">
            请上传 大小不超过 5MB 格式为 png/jpg/jpeg 的文件
          </div>
        </template>
      </avue-crud>
    </basic-container>
  </div>
</template>

<script setup name="Banner">
import { ref, reactive, computed, getCurrentInstance, watch } from 'vue'
import { getPage, delObj, uploadBanner, updateBannerWithFile } from '@/api/mall/banner'
import { Plus } from '@element-plus/icons-vue'

const { proxy } = getCurrentInstance()
const tableData = ref([])
const tableLoading = ref(false)
const page = reactive({ total: 0, currentPage: 1, pageSize: 10 })
const form = ref({}) 
const fileList = ref([]) // 用于 el-upload 展示
const currentFile = ref(null) // 当前选中的原始文件对象

// 智能获取图片字段：兼容 imageUrl, url, picUrl 等多种后端字段名
const getImageUrl = (row) => {
  if (!row) return ''
  // 提取可能的字段，按优先级排序
  const val = row.imageUrl ?? row.url ?? row.picUrl ?? row.picUrls ?? row.coverUrl ?? row.fileId
  if (!val) return ''
  
  // 处理数组形式（如商品管理返回的可能是数组）
  if (Array.isArray(val)) {
    return val.length > 0 ? val[0] : ''
  }
  
  // 处理字符串形式（如 "url1,url2"）
  if (typeof val === 'string' && val.includes(',')) {
    return val.split(',')[0]
  }
  
  return val
}

// 图片路径格式化：确保所有图片都能正确显示
const baseApi = import.meta.env.VITE_APP_BASE_API || ''
const formatImageUrl = (url) => {
  if (!url) return ''
  // 1. 如果是 http/https 开头，或已经是 blob 预览图，直接返回
  if (/^https?:\/\//i.test(url) || url.startsWith('blob:')) return url
  
  // 2. 如果是纯数字 ID，拼成 /profile/file/{id} 路径
  if (/^\d+$/.test(url)) {
    const path = '/profile/file/' + url
    return baseApi ? baseApi + path : path
  }
  
  // 3. 处理相对路径：统一补全前缀
  let path = url.startsWith('/') ? url : '/' + url
  
  // 避免重复拼接 baseApi（如 /dev-api/dev-api/...）
  if (baseApi && baseApi !== '/' && path.startsWith(baseApi)) {
    return path
  }
  
  // 如果路径不带 baseApi 且非根目录，则拼接
  return baseApi + path
}

// 文件变动处理
const handleFileChange = (file) => {
  currentFile.value = file.raw
  fileList.value = [file]
}
const handleFileRemove = () => {
  currentFile.value = null
  fileList.value = []
}

// 监听编辑时的回显
watch(() => form.value.imageUrl, (val) => {
  if (val && typeof val === 'string') {
    fileList.value = [{ name: 'banner', url: formatImageUrl(val) }]
    currentFile.value = null // 回显时不是新选的文件
  } else if (!val) {
    fileList.value = []
    currentFile.value = null
  }
}, { deep: true })

const tableOption = {
  border: true,
  index: true,
  indexLabel: '序号',
  stripe: true,
  menuAlign: 'center',
  align: 'center',
  labelWidth: 100,
  column: [
    {
      label: '轮播图片',
      prop: 'imageUrl',
      formslot: true, // 使用插槽
      slot: true,
      span: 24
    },
    {
      label: '显示排序',
      prop: 'sort',
      type: 'number',
      width: 100,
      value: 0
    },
    {
      label: '状态',
      prop: 'status',
      type: 'select',
      dicData: [
        { label: '显示', value: '1' },
        { label: '隐藏', value: '0' }
      ],
      width: 100,
      search: true,
      value: '1'
    },
    {
      label: '创建时间',
      prop: 'createTime',
      display: false,
      width: 160
    }
  ]
}

const permissionList = computed(() => {
  return {
    addBtn: true,
    delBtn: true,
    editBtn: true
  }
})

function getList(page, params) {
  tableLoading.value = true
  const query = {
    pageNum: page.currentPage,
    pageSize: page.pageSize,
    ...params
  }
  getPage(query).then(res => {
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

function handleSave(row, done, loading) {
  if (!currentFile.value) {
    proxy.$modal.msgError('请选择一张轮播图片')
    loading()
    return
  }
  
  const formData = new FormData()
  formData.append('file', currentFile.value)
  formData.append('sort', row.sort || 0)
  formData.append('status', row.status || '1')
  
  uploadBanner(formData).then(() => {
    proxy.$modal.msgSuccess('新增成功')
    done()
    getList(page)
  }).catch((err) => { 
    proxy.$modal.msgError('保存失败：' + (err.msg || err.message || '接口异常'))
    loading() 
  })
}

function handleUpdate(row, index, done, loading) {
  const formData = new FormData()
  if (row.id) formData.append('id', row.id)
  
  if (currentFile.value) {
    formData.append('file', currentFile.value)
  }
  
  formData.append('sort', row.sort || 0)
  formData.append('status', row.status || '1')
  
  updateBannerWithFile(formData).then(() => {
    proxy.$modal.msgSuccess('修改成功')
    done()
    getList(page)
  }).catch((err) => { 
    proxy.$modal.msgError('修改失败：' + (err.msg || err.message || '接口异常'))
    loading() 
  })
}

function handleDel(row) {
  proxy.$modal.confirm('确定删除该轮播图吗？').then(() => {
    return delObj(row.id)
  }).then(() => {
    proxy.$modal.msgSuccess('删除成功')
    getList(page)
  })
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
.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  color: #909399;
  font-size: 12px;
}
</style>
