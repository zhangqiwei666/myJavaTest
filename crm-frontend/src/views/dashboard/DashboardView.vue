<template>
  <div class="dashboard-container">
    <div class="page-header">
      <div>
        <h2 class="page-title">数据概览 Dashboard</h2>
        <p class="subtitle">欢迎回来，{{ userStore.realName }}！以下是系统的实时业务数据概况。</p>
      </div>
      <el-button type="primary" :icon="Refresh" @click="fetchData">刷新数据</el-button>
    </div>

    <!-- 顶层指标卡片 4 Grid Cards -->
    <el-row :gutter="20" class="stats-row">
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card card-blue">
          <div class="stat-icon"><el-icon><UserFilled /></el-icon></div>
          <div class="stat-info">
            <span class="stat-label">客户总数</span>
            <span class="stat-value">{{ customerList.length }}</span>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card card-purple">
          <div class="stat-icon"><el-icon><Opportunity /></el-icon></div>
          <div class="stat-info">
            <span class="stat-label">销售线索数</span>
            <span class="stat-value">{{ clueList.length }}</span>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card card-emerald">
          <div class="stat-icon"><el-icon><Money /></el-icon></div>
          <div class="stat-info">
            <span class="stat-label">商机预计总额</span>
            <span class="stat-value">¥{{ totalOpportunityAmount.toLocaleString() }}</span>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card card-orange">
          <div class="stat-icon"><el-icon><TrendCharts /></el-icon></div>
          <div class="stat-info">
            <span class="stat-label">线索转化率</span>
            <span class="stat-value">{{ conversionRate }}%</span>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 近期数据表格列表 -->
    <el-row :gutter="20" class="content-row">
      <el-col :span="14">
        <el-card shadow="never" class="table-card">
          <template #header>
            <div class="card-header">
              <span>近期商机与跟进进展</span>
              <el-tag type="primary" size="small">最新 5 条</el-tag>
            </div>
          </template>
          <el-table :data="opportunityList.slice(0, 5)" stripe style="width: 100%">
            <el-table-column prop="name" label="商机名称" min-width="140" />
            <el-table-column prop="amount" label="预计金额">
              <template #default="{ row }">
                <span class="amount-text">¥{{ row.amount?.toLocaleString() }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="stage" label="商机阶段">
              <template #default="{ row }">
                <el-tag :type="getStageTag(row.stage)">{{ row.stage }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>

      <el-col :span="10">
        <el-card shadow="never" class="table-card">
          <template #header>
            <div class="card-header">
              <span>重要客户占比看板</span>
            </div>
          </template>
          <div class="level-stats">
            <div v-for="item in levelDistribution" :key="item.label" class="level-item">
              <div class="level-info">
                <span>{{ item.label }}</span>
                <span class="count">{{ item.count }} 家</span>
              </div>
              <el-progress :percentage="item.percentage" :color="item.color" :stroke-width="10" />
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useUserStore } from '@/store/user'
import { Refresh } from '@element-plus/icons-vue'
import { getCustomerListApi, getClueListApi, getOpportunityListApi } from '@/api/crm'
import type { CrmCustomer, CrmClue, CrmOpportunity } from '@/types/api'

const userStore = useUserStore()
const customerList = ref<CrmCustomer[]>([])
const clueList = ref<CrmClue[]>([])
const opportunityList = ref<CrmOpportunity[]>([])

const fetchData = async () => {
  try {
    const [resCust, resClue, resOpp] = await Promise.all([
      getCustomerListApi(),
      getClueListApi(),
      getOpportunityListApi()
    ])
    if (resCust.code === 200) customerList.value = resCust.data || []
    if (resClue.code === 200) clueList.value = resClue.data || []
    if (resOpp.code === 200) opportunityList.value = resOpp.data || []
  } catch (err) {
    // handled
  }
}

onMounted(() => {
  fetchData()
})

const totalOpportunityAmount = computed(() => {
  return opportunityList.value.reduce((acc, curr) => acc + (curr.amount || 0), 0)
})

const conversionRate = computed(() => {
  if (clueList.value.length === 0) return '0.0'
  const converted = clueList.value.filter((c) => c.status === '已转化').length
  return ((converted / clueList.value.length) * 100).toFixed(1)
})

const levelDistribution = computed(() => {
  const total = customerList.value.length || 1
  const vip = customerList.value.filter((c) => c.level === 'VIP客户').length
  const imp = customerList.value.filter((c) => c.level === '重要客户').length
  const normal = customerList.value.filter((c) => c.level === '普通客户' || !c.level).length

  return [
    { label: 'VIP客户', count: vip, percentage: Math.round((vip / total) * 100), color: '#f59e0b' },
    { label: '重要客户', count: imp, percentage: Math.round((imp / total) * 100), color: '#3b82f6' },
    { label: '普通客户', count: normal, percentage: Math.round((normal / total) * 100), color: '#10b981' }
  ]
})

const getStageTag = (stage: string) => {
  switch (stage) {
    case '赢单': return 'success'
    case '签订合同': return 'primary'
    case '方案报价': return 'warning'
    default: return 'info'
  }
}
</script>

<style scoped>
.dashboard-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.subtitle {
  color: #64748b;
  font-size: 13px;
  margin-top: 4px;
}

.stats-row {
  margin-bottom: 8px;
}

.stat-card {
  padding: 20px;
  border-radius: 14px;
  color: white;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.08);
  transition: transform 0.2s;
}

.stat-card:hover {
  transform: translateY(-4px);
}

.card-blue {
  background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
}

.card-purple {
  background: linear-gradient(135deg, #8b5cf6 0%, #6d28d9 100%);
}

.card-emerald {
  background: linear-gradient(135deg, #10b981 0%, #047857 100%);
}

.card-orange {
  background: linear-gradient(135deg, #f59e0b 0%, #b45309 100%);
}

.stat-icon {
  width: 48px;
  height: 48px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
}

.stat-info {
  display: flex;
  flex-direction: column;
}

.stat-label {
  font-size: 13px;
  opacity: 0.9;
}

.stat-value {
  font-size: 22px;
  font-weight: 800;
  margin-top: 2px;
}

.table-card {
  border-radius: 12px;
  border: 1px solid #e2e8f0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 700;
}

.amount-text {
  color: #10b981;
  font-weight: 700;
}

.level-stats {
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding: 10px 0;
}

.level-info {
  display: flex;
  justify-content: space-between;
  font-size: 14px;
  font-weight: 600;
  margin-bottom: 6px;
}

.level-info .count {
  color: #64748b;
}
</style>
