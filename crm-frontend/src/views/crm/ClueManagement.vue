<template>
  <div class="clue-management-container">
    <div class="page-header">
      <div class="page-title">
        <el-icon><Opportunity /></el-icon>
        <span>销售线索管理</span>
      </div>
      <el-button type="primary" :icon="Plus" @click="handleCreate">录入新线索</el-button>
    </div>

    <el-card shadow="never" class="glass-card">
      <el-table :data="clueList" v-loading="loading" stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="name" label="线索名称/联系人" min-width="140" />
        <el-table-column prop="company" label="意向公司" min-width="140" />
        <el-table-column prop="phone" label="联系电话" min-width="130" />
        <el-table-column prop="source" label="线索来源" min-width="120" />
        <el-table-column prop="status" label="线索状态" width="110">
          <template #default="{ row }">
            <el-tag :type="getStatusTag(row.status)">{{ row.status || '未处理' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" min-width="160" />
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button
              v-if="row.status !== '已转化'"
              link
              type="success"
              @click="handleConvert(row)"
            >
              转为客户
            </el-button>
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增 / 编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑销售线索' : '录入新线索'" width="480px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="联系人/姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名或客户称呼" />
        </el-form-item>
        <el-form-item label="意向公司" prop="company">
          <el-input v-model="form.company" placeholder="请输入公司名称" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="线索来源" prop="source">
          <el-select v-model="form.source" placeholder="请选择来源" style="width: 100%">
            <el-option label="官网在线咨询" value="官网在线咨询" />
            <el-option label="行业展会" value="行业展会" />
            <el-option label="电话推销" value="电话推销" />
            <el-option label="老客户推荐" value="老客户推荐" />
          </el-select>
        </el-form-item>
        <el-form-item label="处理状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择处理状态" style="width: 100%">
            <el-option label="未处理" value="未处理" />
            <el-option label="转化中" value="转化中" />
            <el-option label="已转化" value="已转化" />
            <el-option label="已作废" value="已作废" />
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
import { Plus, Opportunity } from '@element-plus/icons-vue'
import { getClueListApi, createClueApi, updateClueApi, deleteClueApi, convertClueToCustomerApi } from '@/api/crm'
import type { CrmClue } from '@/types/api'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'

const loading = ref(false)
const clueList = ref<CrmClue[]>([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref<FormInstance>()

const form = reactive({
  id: 0,
  name: '',
  company: '',
  phone: '',
  source: '官网在线咨询',
  status: '未处理'
})

const rules: FormRules = {
  name: [{ required: true, message: '请输入联系人姓名', trigger: 'blur' }]
}

const fetchClues = async () => {
  loading.value = true
  try {
    const res = await getClueListApi()
    if (res.code === 200) clueList.value = res.data || []
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchClues()
})

const handleCreate = () => {
  isEdit.value = false
  form.id = 0
  form.name = ''
  form.company = ''
  form.phone = ''
  form.source = '官网在线咨询'
  form.status = '未处理'
  dialogVisible.value = true
}

const handleEdit = (row: CrmClue) => {
  isEdit.value = true
  form.id = row.id
  form.name = row.name
  form.company = row.company || ''
  form.phone = row.phone || ''
  form.source = row.source || '官网在线咨询'
  form.status = row.status || '未处理'
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        if (isEdit.value) {
          await updateClueApi(form as any)
          ElMessage.success('线索修改成功')
        } else {
          await createClueApi(form as any)
          ElMessage.success('线索录入成功')
        }
        dialogVisible.value = false
        fetchClues()
      } finally {
        submitLoading.value = false
      }
    }
  })
}

const handleDelete = (row: CrmClue) => {
  ElMessageBox.confirm(`确定要删除线索 "${row.name}" 吗？`, '警告', {
    confirmButtonText: '确定删除',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await deleteClueApi(row.id)
    ElMessage.success('线索已成功删除')
    fetchClues()
  })
}

const handleConvert = (row: CrmClue) => {
  ElMessageBox.confirm(
    `是否将线索 "${row.name}" (${row.company || '无公司'}) 一键转化为【正式客户档案】？`,
    '确认转化',
    {
      confirmButtonText: '立即转化',
      cancelButtonText: '取消',
      type: 'success'
    }
  ).then(async () => {
    await convertClueToCustomerApi(row.id)
    ElMessage.success('线索已成功转化为正式客户！')
    fetchClues()
  })
}

const getStatusTag = (status?: string) => {
  switch (status) {
    case '已转化': return 'success'
    case '转化中': return 'warning'
    case '已作废': return 'danger'
    default: return 'info'
  }
}
</script>

<style scoped>
.clue-management-container {
  display: flex;
  flex-direction: column;
}
</style>
