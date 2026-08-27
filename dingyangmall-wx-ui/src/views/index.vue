<template>
  <div class="app-container home">
    <!-- 标题区域 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="24">
        <el-card>
          <div class="header-section">
            <h2>如囍优选 - 管理端</h2>
            <p>欢迎使用如囍优选。本系统围绕“厂商 → 一级经销商 → 二级经销商 → 终端客户”的积分流转链路，实现积分分发、商品兑换、商品券核销等核心业务。</p>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 核心指标区域 (模拟数据) -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>用户总数</span>
            </div>
          </template>
          <div class="card-body">
            <h2 class="text-primary">{{ statistics.userCount }}</h2>
            <div class="sub-text">各级经销商与终端客户</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>平台积分发放总量</span>
            </div>
          </template>
          <div class="card-body">
            <h2 class="text-warning">{{ statistics.pointsIssued }}</h2>
            <div class="sub-text">厂商分发总量</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>待发货订单</span>
            </div>
          </template>
          <div class="card-body">
            <h2 class="text-danger">{{ statistics.pendingOrders }}</h2>
            <div class="sub-text">实体商品兑换待处理</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>今日核销</span>
            </div>
          </template>
          <div class="card-body">
            <h2 class="text-success">{{ statistics.todayWriteOffs }}</h2>
            <div class="sub-text">商品券线下核销数</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 最新接口统计：与后端 dashboard statistics/* 接口对应 -->
    <el-row :gutter="20" class="mb-20 stats-extra">
      <el-col v-for="item in extraCards" :key="item.key" :span="6" :xs="12">
        <el-card shadow="hover" class="extra-card">
          <div class="extra-label">{{ item.label }}</div>
          <div class="extra-value">{{ extraStats[item.key] }}</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 业务流程与快捷入口 -->
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card header="积分流转体系" class="box-card">
           <el-steps direction="vertical" :active="4" finish-status="process">
            <el-step title="厂商 (平台方)" description="系统唯一积分创建入口，负责分发给一级经销商"></el-step>
            <el-step title="一级经销商" description="接收厂商积分，可赠送给二级经销商或终端客户"></el-step>
            <el-step title="二级经销商" description="接收上级积分，可赠送给终端客户"></el-step>
            <el-step title="终端客户" description="积分仅用于兑换商品，不可转赠"></el-step>
          </el-steps>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card header="核心业务快捷入口" class="box-card">
          <div class="quick-actions">
            <el-button type="primary" plain @click="handleNav('/mall/distributor')">一级经销商管理</el-button>
            <el-button type="warning" plain @click="handleNav('/mall/goodsspu')">商品上架管理</el-button>
            <el-button type="danger" plain @click="handleNav('/mall/orderinfo')">订单发货处理</el-button>
          </div>
          <div class="notes-section">
             <h4>系统说明：</h4>
             <ul>
               <li>只有厂商（本账号）可以创建一级经销商。</li>
               <li>商品券核销请登录商家端APP/小程序操作。</li>
               <li>每日0点系统自动处理签到积分重置。</li>
             </ul>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup name="ViewIndex">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getDashboardData, getStatisticsCashSales, getStatisticsGoodsSales, getStatisticsGoodsStock, getStatisticsIntegralExchange, getStatisticsIntegralGrant, getStatisticsMemberNew, getStatisticsMemberReferralDetail, getStatisticsMemberReferralSummary } from '@/api/system/dashboard'

const router = useRouter()
const statistics = ref({
  userCount: 0,
  pointsIssued: 0,
  pendingOrders: 0,
  todayWriteOffs: 0
})
const extraStats = ref({ cashSales: 0, goodsSales: 0, goodsStock: 0, integralExchange: 0, integralGrant: 0, memberNew: 0, referralDetail: 0, referralSummary: 0 })
const extraCards = [
  { key: 'cashSales', label: '累计销售额' },
  { key: 'goodsSales', label: '商品销售数量' },
  { key: 'goodsStock', label: '商品库存数量' },
  { key: 'integralExchange', label: '积分兑换量' },
  { key: 'integralGrant', label: '积分发放量' },
  { key: 'memberNew', label: '新增会员数' },
  { key: 'referralDetail', label: '推荐明细数' },
  { key: 'referralSummary', label: '推荐人数量' }
]

function handleNav(path) {
  router.push(path)
}

function initData() {
  getDashboardData().then(response => {
    const d = response.data != null ? response.data : response
    if (d && typeof d === 'object') {
      statistics.value = {
        userCount: d.userCount ?? 0,
        pointsIssued: d.pointsIssued ?? 0,
        pendingOrders: d.pendingOrders ?? 0,
        todayWriteOffs: d.todayWriteOffs ?? 0
      }
    }
  }).catch(() => {})
}

function initExtraStats() {
  const calls = [
    ['cashSales', getStatisticsCashSales()], ['goodsSales', getStatisticsGoodsSales()], ['goodsStock', getStatisticsGoodsStock()],
    ['integralExchange', getStatisticsIntegralExchange()], ['integralGrant', getStatisticsIntegralGrant()], ['memberNew', getStatisticsMemberNew()],
    ['referralDetail', getStatisticsMemberReferralDetail()], ['referralSummary', getStatisticsMemberReferralSummary()]
  ]
  calls.forEach(([key, promise]) => promise.then(response => {
    const d = response && response.data != null ? response.data : response || {}
    extraStats.value[key] = d.totalSales ?? d.totalQuantity ?? d.totalStock ?? d.totalPoints ?? d.count ?? d.referralDetails?.length ?? d.referralSummary?.length ?? (typeof d === 'object' ? Object.keys(d).length : 0)
  }).catch(() => {}))
}

onMounted(() => {
  initData()
  initExtraStats()
})
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
  background-color: #f0f2f5;
  min-height: 100vh;
}
.mb-20 {
  margin-bottom: 20px;
}
.header-section {
  h2 {
    margin-top: 0;
    color: #303133;
  }
  p {
    color: #606266;
    margin-bottom: 0;
  }
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
}
.card-body {
  text-align: center;
  h2 {
    margin: 10px 0;
    font-size: 28px;
  }
  .sub-text {
    color: #909399;
    font-size: 13px;
  }
}
.text-primary { color: #409EFF; }
.text-warning { color: #E6A23C; }
.text-danger { color: #F56C6C; }
.text-success { color: #67C23A; }

.quick-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  margin-bottom: 30px;
  .el-button {
    margin-left: 0;
    width: 45%;
    justify-content: flex-start;
  }
}

.notes-section {
  background-color: #f4f4f5;
  padding: 15px;
  border-radius: 4px;
  h4 {
    margin-top: 0;
    margin-bottom: 10px;
    color: #303133;
  }
  ul {
    padding-left: 20px;
    margin: 0;
    li {
      color: #606266;
      line-height: 1.8;
    }
  }
}
.box-card {
  height: 100%;
}
.extra-card { min-height: 105px; }
.extra-label { color: #909399; font-size: 13px; }
.extra-value { color: #303133; font-size: 24px; font-weight: 600; margin-top: 12px; }
</style>