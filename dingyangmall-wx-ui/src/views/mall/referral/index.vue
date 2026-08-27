<template>
  <div class="app-container">
    <el-card shadow="never">
      <template #header>
        <div class="card-header"><span>推荐关系管理</span><el-button type="primary" @click="loadAll">刷新</el-button></div>
      </template>
      <el-tabs v-model="activeTab">
        <el-tab-pane label="会员推荐" name="members">
          <el-form :inline="true" :model="memberQuery" class="query-form">
            <el-form-item label="关键词"><el-input v-model="memberQuery.keyword" clearable placeholder="昵称/手机号/会员码" @keyup.enter="loadMembers" /></el-form-item>
            <el-form-item><el-button type="primary" @click="loadMembers">查询</el-button><el-button @click="resetMembers">重置</el-button></el-form-item>
          </el-form>
          <el-table v-loading="memberLoading" :data="members" border stripe>
            <el-table-column prop="id" label="ID" width="180" />
            <el-table-column prop="nickname" label="昵称" min-width="140" />
            <el-table-column prop="phone" label="手机号" width="140" />
            <el-table-column prop="memberCode" label="会员码" width="150" />
            <el-table-column prop="points" label="积分" width="100" />
            <el-table-column prop="createTime" label="注册时间" min-width="170" />
            <el-table-column label="操作" width="120" fixed="right"><template #default="scope"><el-button link type="primary" @click="openGift(scope.row)">发放推荐积分</el-button></template></el-table-column>
          </el-table>
          <pagination v-show="memberTotal > 0" v-model:page="memberQuery.current" v-model:limit="memberQuery.size" :total="memberTotal" @pagination="loadMembers" />
        </el-tab-pane>
        <el-tab-pane label="推荐流水" name="records">
          <el-table v-loading="recordLoading" :data="records" border stripe>
            <el-table-column prop="id" label="流水ID" width="180" />
            <el-table-column prop="userId" label="用户ID" width="180" />
            <el-table-column prop="sourceUserId" label="推荐人ID" width="180" />
            <el-table-column prop="integralNum" label="奖励积分" width="120" />
            <el-table-column prop="remark" label="说明" min-width="220" />
            <el-table-column prop="operTime" label="发生时间" min-width="170" />
          </el-table>
          <pagination v-show="recordTotal > 0" v-model:page="recordQuery.current" v-model:limit="recordQuery.size" :total="recordTotal" @pagination="loadRecords" />
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <el-dialog v-model="giftOpen" title="发放推荐积分" width="460px">
      <el-form ref="giftFormRef" :model="giftForm" label-width="100px">
        <el-form-item label="会员"><span>{{ giftForm.nickname || '-' }}（{{ giftForm.phone || '-' }}）</span></el-form-item>
        <el-form-item label="积分数量"><el-input-number v-model="giftForm.points" :min="1" :max="100000" controls-position="right" /></el-form-item>
        <el-form-item label="说明"><el-input v-model="giftForm.remark" placeholder="推荐奖励" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="giftOpen = false">取消</el-button><el-button type="primary" :loading="giftLoading" @click="submitGift">确认发放</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup name="ReferralManagement">
import { ref, reactive, onMounted } from 'vue'
import { listReferralMembers, giftReferral, listReferralRecords } from '@/api/mall/referral'

const activeTab = ref('members')
const members = ref([])
const records = ref([])
const memberTotal = ref(0)
const recordTotal = ref(0)
const memberLoading = ref(false)
const recordLoading = ref(false)
const giftOpen = ref(false)
const giftLoading = ref(false)
const memberQuery = reactive({ current: 1, size: 10, keyword: '' })
const recordQuery = reactive({ current: 1, size: 10 })
const giftForm = reactive({ targetUserId: null, nickname: '', phone: '', points: 10, remark: '推荐奖励' })

function unwrap(response) { return response && response.data != null ? response.data : response || {} }
function loadMembers() {
  memberLoading.value = true
  listReferralMembers(memberQuery).then(res => { const d = unwrap(res); members.value = d.records || d.rows || []; memberTotal.value = Number(d.total || 0) }).finally(() => { memberLoading.value = false })
}
function loadRecords() {
  recordLoading.value = true
  listReferralRecords(recordQuery).then(res => { const d = unwrap(res); records.value = d.records || d.rows || []; recordTotal.value = Number(d.total || 0) }).finally(() => { recordLoading.value = false })
}
function loadAll() { loadMembers(); loadRecords() }
function resetMembers() { memberQuery.current = 1; memberQuery.keyword = ''; loadMembers() }
function openGift(row) { Object.assign(giftForm, { targetUserId: row.id, nickname: row.nickname, phone: row.phone, points: 10, remark: '推荐奖励' }); giftOpen.value = true }
function submitGift() {
  giftLoading.value = true
  giftReferral({ targetUserId: giftForm.targetUserId, points: giftForm.points, remark: giftForm.remark }).then(() => { ElMessage.success('发放成功'); giftOpen.value = false; loadMembers(); loadRecords() }).finally(() => { giftLoading.value = false })
}
onMounted(loadAll)
</script>

<style scoped>
.app-container { padding: 20px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.query-form { margin-bottom: 12px; }
</style>
