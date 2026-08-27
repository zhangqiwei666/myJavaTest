<template>
  <div class="opportunity-management-container">
    <div class="page-header">
      <div class="page-title">
        <el-icon><Money /></el-icon>
        <span>销售商机管理</span>
      </div>
      <el-button type="primary" :icon="Plus" @click="handleCreate">新建销售商机</el-button>
    </div>

    <el-card shadow="never" class="glass-card">
      <el-table :data="opportunityList" v-loading="loading" stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="name" label="商机名称" min-width="160" />
        <el-table-column prop="customerName" label="关联客户" min-width="140">
          <template #default="{ row }">
            <span class="customer-tag">
              <el-icon><User /></el-icon> {{ row.customerName || `客户 ID: ${row.customerId}` }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="amount" label="预计金额 (元)" min-width="140">
          <template #default="{ row }">
            <span class="amount-val">¥{{ (row.amount || 0).toLocaleString() }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="stage" label="商机阶段" min-width="120">
          <template #default="{ row }">
            <el-tag :type="getStageTag(row.stage)">{{ row.stage || '初步沟通' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" min-width="160" />
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增 / 编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑销售商机' : '新建销售商机'" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="商机名称" prop="name">
          <el-input v-model="form.name" placeholder="如: 云服务采购扩容项目" />
        </el-form-item>
        <el-form-item label="关联客户" prop="customerId">
          <el-select v-model="form.customerId" placeholder="请选择关联的客户" style="width: 100%">
            <el-option
              v-for="c in customers"
              :key="c.id"
              :label="`${c.name} (${c.company || '个人客户'})`"
              :value="c.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="预计金额" prop="amount">
          <el-input-number v-model="form.amount" :min="0" :step="1000" style="width: 100%" placeholder="请输入预计金额" />
        </el-form-item>
        <el-form-item label="商机阶段" prop="stage">
          <el-select v-model="form.stage" placeholder="请选择阶段" style="width: 100%">
            <el-option label="初步沟通" value="初步沟通" />
            <el-option label="需求确认" value="需求确认" />
            <el-option label="方案报价" value="方案报价" />
            <el-option label="签订合同" value="签订合同" />
            <el-option label="赢单" value="赢单" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { Plus, Money, User } from '@element-plus/icons-vue'
import { getOpportunityListApi, createOpportunityApi, updateOpportunityApi, deleteOpportunityApi, getCustomerListApi } from '@/api/crm'
import type { CrmOpportunity, CrmCustomer } from '@/types/api'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'

const loading = ref(false)
const opportunityList = ref<CrmOpportunity[]>([])
const customers = ref<CrmCustomer[]>([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref<FormInstance>()

const form = reactive({
  id: 0,
  name: '',
  customerId: undefined as number | undefined,
  amount: 10000,
  stage: '初步沟通'
})

const rules: FormRules = {
  name: [{ required: true, message: '请输入商机名称', trigger: 'blur' }],
  customerId: [{ required: true, message: '请选择关联客户', trigger: 'change' }]
}

const fetchData = async () => {
  loading.value = true
  try {
    const [resOpp, resCust] = await Promise.all([getOpportunityListApi(), getCustomerListApi()])
    if (resOpp.code === 200) opportunityList.value = resOpp.data || []
    if (resCust.code === 200) customers.value = resCust.data || []
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchData()
})

const handleCreate = () => {
  isEdit.value = false
  form.id = 0
  form.name = ''
  form.customerId = customers.value[0]?.id
  form.amount = 10000
  form.stage = '初步沟通'
  dialogVisible.value = true
}

const handleEdit = (row: CrmOpportunity) => {
  isEdit.value = true
  form.id = row.id
  form.name = row.name
  form.customerId = row.customerId
  form.amount = row.amount || 0
  form.stage = row.stage || '初步沟通'
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        if (isEdit.value) {
          await updateOpportunityApi(form as any)
          ElMessage.success('商机修改成功')
        } else {
          await createOpportunityApi(form as any)
          ElMessage.success('商机创建成功')
        }
        dialogVisible.value = false
        fetchData()
      } finally {
        submitLoading.value = false
      }
    }
  })
}

const handleDelete = (row: CrmOpportunity) => {
  ElMessageBox.confirm(`确定要删除商机 "${row.name}" 吗？`, '警告', {
    confirmButtonText: '确定删除',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await deleteOpportunityApi(row.id)
    ElMessage.success('商机已成功删除')
    fetchData()
  })
}

const getStageTag = (stage?: string) => {
  switch (stage) {
    case '赢单': return 'success'
    case '签订合同': return 'primary'
    case '方案报价': return 'warning'
    default: return 'info'
  }
}
</script>

<style scoped>
.opportunity-management-container {
  display: flex;
  flex-direction: column;
}

.amount-val {
  color: #059669;
  font-weight: 700;
}

.customer-tag {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  color: #334155;
}
</style>
