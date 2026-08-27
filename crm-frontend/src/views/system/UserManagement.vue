<template>
  <div class="user-management-container">
    <div class="page-header">
      <div class="page-title">
        <el-icon><User /></el-icon>
        <span>系统用户管理</span>
      </div>
      <el-button type="primary" :icon="Plus" @click="handleCreate">新增系统用户</el-button>
    </div>

    <el-card shadow="never" class="glass-card">
      <el-table :data="userList" v-loading="loading" stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="username" label="登录账号" min-width="120" />
        <el-table-column prop="realName" label="真实姓名" min-width="120" />
        <el-table-column prop="phone" label="手机号码" min-width="130" />
        <el-table-column prop="email" label="电子邮箱" min-width="160" />
        <el-table-column prop="status" label="账号状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" min-width="160" />
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="warning" @click="handleAssignRoles(row)">分配角色</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增 / 编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑用户' : '新增用户'"
      width="500px"
      destroy-on-close
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="登录账号" prop="username">
          <el-input v-model="form.username" :disabled="isEdit" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" placeholder="编辑时留空代表不修改密码" show-password />
        </el-form-item>
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="form.realName" placeholder="请输入真实姓名" />
        </el-form-item>
        <el-form-item label="手机号码" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号码" />
        </el-form-item>
        <el-form-item label="电子邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入电子邮箱" />
        </el-form-item>
        <el-form-item label="账号状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :value="1">正常启用</el-radio>
            <el-radio :value="0">停用禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">保存</el-button>
      </template>
    </el-dialog>

    <!-- 分配角色对话框 -->
    <el-dialog v-model="roleDialogVisible" title="为用户分配角色" width="400px">
      <div style="margin-bottom: 12px; color: #64748b;">
        正在为 <b>{{ currentUser?.realName }} ({{ currentUser?.username }})</b> 配置系统角色：
      </div>
      <el-checkbox-group v-model="selectedRoleIds">
        <el-checkbox v-for="role in allRoles" :key="role.id" :value="role.id" style="display: block; margin-bottom: 8px;">
          {{ role.roleName }} ({{ role.roleKey }})
        </el-checkbox>
      </el-checkbox-group>
      <template #footer>
        <el-button @click="roleDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="assignLoading" @click="handleSaveRoles">确认保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { Plus, User } from '@element-plus/icons-vue'
import { getUserListApi, createUserApi, updateUserApi, deleteUserApi, getRoleListApi, assignRolesApi } from '@/api/system'
import type { SysUser, SysRole } from '@/types/api'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'

const loading = ref(false)
const userList = ref<SysUser[]>([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref<FormInstance>()

const roleDialogVisible = ref(false)
const currentUser = ref<SysUser | null>(null)
const allRoles = ref<SysRole[]>([])
const selectedRoleIds = ref<number[]>([])
const assignLoading = ref(false)

const form = reactive({
  id: 0,
  username: '',
  password: '',
  realName: '',
  phone: '',
  email: '',
  status: 1
})

const rules: FormRules = {
  username: [{ required: true, message: '请输入登录账号', trigger: 'blur' }],
  realName: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }]
}

const fetchUsers = async () => {
  loading.value = true
  try {
    const res = await getUserListApi()
    if (res.code === 200) userList.value = res.data || []
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchUsers()
})

const handleCreate = () => {
  isEdit.value = false
  form.id = 0
  form.username = ''
  form.password = ''
  form.realName = ''
  form.phone = ''
  form.email = ''
  form.status = 1
  dialogVisible.value = true
}

const handleEdit = (row: SysUser) => {
  isEdit.value = true
  form.id = row.id
  form.username = row.username
  form.password = ''
  form.realName = row.realName
  form.phone = row.phone
  form.email = row.email
  form.status = row.status
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        if (isEdit.value) {
          await updateUserApi(form)
          ElMessage.success('用户更新成功')
        } else {
          await createUserApi(form)
          ElMessage.success('用户创建成功')
        }
        dialogVisible.value = false
        fetchUsers()
      } finally {
        submitLoading.value = false
      }
    }
  })
}

const handleDelete = (row: SysUser) => {
  ElMessageBox.confirm(`确定要删除用户 "${row.realName}" 吗？`, '警告', {
    confirmButtonText: '确定删除',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await deleteUserApi(row.id)
    ElMessage.success('用户已成功删除')
    fetchUsers()
  })
}

const handleAssignRoles = async (row: SysUser) => {
  currentUser.value = row
  const res = await getRoleListApi()
  if (res.code === 200) {
    allRoles.value = res.data || []
  }
  selectedRoleIds.value = []
  roleDialogVisible.value = true
}

const handleSaveRoles = async () => {
  if (!currentUser.value) return
  assignLoading.value = true
  try {
    await assignRolesApi(currentUser.value.id, selectedRoleIds.value)
    ElMessage.success('角色分配已保存')
    roleDialogVisible.value = false
    fetchUsers()
  } finally {
    assignLoading.value = false
  }
}
</script>

<style scoped>
.user-management-container {
  display: flex;
  flex-direction: column;
}
</style>
