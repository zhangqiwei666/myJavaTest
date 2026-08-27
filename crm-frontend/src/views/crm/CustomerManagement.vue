<template>
  <div class="customer-management-container">
    <div class="page-header">
      <div class="page-title">
        <el-icon><UserFilled /></el-icon>
        <span>CRM 客户档案管理</span>
      </div>
      <el-button type="primary" :icon="Plus" @click="handleCreate">新增客户档案</el-button>
    </div>

    <!-- 筛选搜索栏 -->
    <el-card shadow="never" class="filter-card glass-card">
      <el-form :inline="true" :model="query" class="filter-form">
        <el-form-item label="关键字搜索">
          <el-input v-model="query.keyword" placeholder="搜索客户姓名/公司/电话" clearable @clear="fetchCustomers" @keyup.enter="fetchCustomers" />
        </el-form-item>
        <el-form-item label="跟进状态">
          <el-select v-model="query.status" placeholder="全部状态" clearable style="width: 140px;" @change="fetchCustomers">
            <el-option label="潜在" value="潜在" />
            <el-option label="跟进中" value="跟进中" />
            <el-option label="已成交" value="已成交" />
            <el-option label="已流失" value="已流失" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="fetchCustomers">查询</el-button>
          <el-button :icon="Refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 客户列表 -->
    <el-card shadow="never" class="glass-card table-box">
      <el-table :data="customerList" v-loading="loading" stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="name" label="客户姓名/公司" min-width="140" />
        <el-table-column prop="company" label="所属公司" min-width="140" />
        <el-table-column prop="phone" label="联系电话" min-width="120" />
        <el-table-column prop="industry" label="所属行业" min-width="120" />
        <el-table-column prop="level" label="客户级别" width="110">
          <template #default="{ row }">
            <el-tag :type="getLevelTag(row.level)">{{ row.level || '普通客户' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="跟进状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTag(row.status)">{{ row.status || '跟进中' }}</el-tag>
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

    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑客户档案' : '新增客户档案'" width="550px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="客户姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入客户姓名或企业名称" />
        </el-form-item>
        <el-form-item label="所属公司" prop="company">
          <el-input v-model="form.company" placeholder="请输入公司全称" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机或固定电话" />
        </el-form-item>
        <el-form-item label="电子邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入电子邮箱" />
        </el-form-item>
        <el-form-item label="所属行业" prop="industry">
          <el-input v-model="form.industry" placeholder="例如: 互联网/科技, 金融, 制造" />
        </el-form-item>
        <el-form-item label="客户级别" prop="level">
          <el-select v-model="form.level" placeholder="请选择级别" style="width: 100%">
            <el-option label="VIP客户" value="VIP客户" />
            <el-option label="重要客户" value="重要客户" />
            <el-option label="普通客户" value="普通客户" />
          </el-select>
        </el-form-item>
        <el-form-item label="跟进状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择跟进状态" style="width: 100%">
            <el-option label="潜在" value="潜在" />
            <el-option label="跟进中" value="跟进中" />
            <el-option label="已成交" value="已成交" />
            <el-option label="已流失" value="已流失" />
          </el-select>
        </el-form-item>
        <el-form-item label="联系地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入详细联系地址" />
        </el-form-item>
        <el-form-item label="备注说明" prop="remark">
          <el-input v-model="form.remark" type="textarea" rows="3" placeholder="记录跟进注意事项与背景信息" />
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
import { Plus, UserFilled, Search, Refresh } from '@element-plus/icons-vue'
import { getCustomerListApi, createCustomerApi, updateCustomerApi, deleteCustomerApi } from '@/api/crm'
import type { CrmCustomer } from '@/types/api'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'

const loading = ref(false)
const customerList = ref<CrmCustomer[]>([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref<FormInstance>()

const query = reactive({
  keyword: '',
  status: ''
})

const form = reactive({
  id: 0,
  name: '',
  company: '',
  phone: '',
  email: '',
  industry: '',
  level: '普通客户',
  status: '跟进中',
  address: '',
  remark: ''
})

const rules: FormRules = {
  name: [{ required: true, message: '请输入客户姓名', trigger: 'blur' }]
}

const fetchCustomers = async () => {
  loading.value = true
  try {
    const res = await getCustomerListApi(query)
    if (res.code === 200) customerList.value = res.data.list || []
  } finally {
    loading.value = false
  }
}

const resetQuery = () => {
  query.keyword = ''
  query.status = ''
  fetchCustomers()
}

onMounted(() => {
  fetchCustomers()
})

const handleCreate = () => {
  isEdit.value = false
  form.id = 0
  form.name = ''
  form.company = ''
  form.phone = ''
  form.email = ''
  form.industry = ''
  form.level = '普通客户'
  form.status = '跟进中'
  form.address = ''
  form.remark = ''
  dialogVisible.value = true
}

const handleEdit = (row: CrmCustomer) => {
  isEdit.value = true
  form.id = row.id
  form.name = row.name
  form.company = row.company || ''
  form.phone = row.phone || ''
  form.email = row.email || ''
  form.industry = row.industry || ''
  form.level = row.level || '普通客户'
  form.status = row.status || '跟进中'
  form.address = row.address || ''
  form.remark = row.remark || ''
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        if (isEdit.value) {
          await updateCustomerApi(form as any)
          ElMessage.success('客户档案已被修改')
        } else {
          await createCustomerApi(form as any)
          ElMessage.success('客户档案新建成功')
        }
        dialogVisible.value = false
        fetchCustomers()
      } finally {
        submitLoading.value = false
      }
    }
  })
}

const handleDelete = (row: CrmCustomer) => {
  ElMessageBox.confirm(`确定要删除客户 "${row.name}" 的档案吗？`, '警告', {
    confirmButtonText: '确定删除',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await deleteCustomerApi(row.id)
    ElMessage.success('档案已成功删除')
    fetchCustomers()
  })
}

const getLevelTag = (level?: string) => {
  switch (level) {
    case 'VIP客户': return 'warning'
    case '重要客户': return 'primary'
    default: return 'info'
  }
}

const getStatusTag = (status?: string) => {
  switch (status) {
    case '已成交': return 'success'
    case '跟进中': return 'primary'
    case '潜在': return 'warning'
    case '已流失': return 'danger'
    default: return 'info'
  }
}
</script>

<style scoped>
.customer-management-container {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.filter-card {
  padding-bottom: 0;
}

.table-box {
  margin-top: 0;
}
</style>
