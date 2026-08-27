<template>
  <div class="role-management-container">
    <div class="page-header">
      <div class="page-title">
        <el-icon><Lock /></el-icon>
        <span>系统角色管理</span>
      </div>
      <el-button type="primary" :icon="Plus" @click="handleCreate">新增角色</el-button>
    </div>

    <el-card shadow="never" class="glass-card">
      <el-table :data="roleList" v-loading="loading" stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="roleName" label="角色名称" min-width="140" />
        <el-table-column prop="roleKey" label="角色标识 (Role Key)" min-width="160">
          <template #default="{ row }">
            <el-tag type="info">{{ row.roleKey }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="角色描述" min-width="200" />
        <el-table-column prop="createTime" label="创建时间" min-width="160" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="warning" @click="handleAssignPerms(row)">分配权限</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增 / 编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑角色' : '新增角色'" width="480px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="form.roleName" placeholder="例如: 销售主管" />
        </el-form-item>
        <el-form-item label="角色标识" prop="roleKey">
          <el-input v-model="form.roleKey" placeholder="例如: ROLE_MANAGER" />
        </el-form-item>
        <el-form-item label="角色描述" prop="description">
          <el-input v-model="form.description" type="textarea" rows="3" placeholder="简要描述角色的职责权限" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">保存</el-button>
      </template>
    </el-dialog>

    <!-- 分配权限树对话框 -->
    <el-dialog v-model="permDialogVisible" title="分配角色权限菜单" width="450px">
      <div style="margin-bottom: 12px; color: #64748b;">
        正在设置角色 <b>{{ currentRole?.roleName }}</b> 的操作权限：
      </div>
      <el-tree
        ref="treeRef"
        :data="permissionTree"
        show-checkbox
        node-key="id"
        default-expand-all
        :props="{ label: 'name', children: 'children' }"
      />
      <template #footer>
        <el-button @click="permDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="assignLoading" @click="handleSavePerms">保存修改</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { Plus, Lock } from '@element-plus/icons-vue'
import { getRoleListApi, createRoleApi, updateRoleApi, deleteRoleApi, getPermissionTreeApi, assignPermissionsApi } from '@/api/system'
import type { SysRole, SysPermission } from '@/types/api'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules, type ElTree } from 'element-plus'

const loading = ref(false)
const roleList = ref<SysRole[]>([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref<FormInstance>()

const permDialogVisible = ref(false)
const currentRole = ref<SysRole | null>(null)
const permissionTree = ref<SysPermission[]>([])
const treeRef = ref<InstanceType<typeof ElTree>>()
const assignLoading = ref(false)

const form = reactive({
  id: 0,
  roleName: '',
  roleKey: '',
  description: ''
})

const rules: FormRules = {
  roleName: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
  roleKey: [{ required: true, message: '请输入角色标识', trigger: 'blur' }]
}

const fetchRoles = async () => {
  loading.value = true
  try {
    const res = await getRoleListApi()
    if (res.code === 200) roleList.value = res.data || []
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchRoles()
})

const handleCreate = () => {
  isEdit.value = false
  form.id = 0
  form.roleName = ''
  form.roleKey = ''
  form.description = ''
  dialogVisible.value = true
}

const handleEdit = (row: SysRole) => {
  isEdit.value = true
  form.id = row.id
  form.roleName = row.roleName
  form.roleKey = row.roleKey
  form.description = row.description || ''
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        if (isEdit.value) {
          await updateRoleApi(form)
          ElMessage.success('角色信息修改成功')
        } else {
          await createRoleApi(form)
          ElMessage.success('角色新建成功')
        }
        dialogVisible.value = false
        fetchRoles()
      } finally {
        submitLoading.value = false
      }
    }
  })
}

const handleDelete = (row: SysRole) => {
  ElMessageBox.confirm(`确定要删除角色 "${row.roleName}" 吗？`, '警告', {
    confirmButtonText: '确定删除',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await deleteRoleApi(row.id)
    ElMessage.success('角色已成功删除')
    fetchRoles()
  })
}

const handleAssignPerms = async (row: SysRole) => {
  currentRole.value = row
  const res = await getPermissionTreeApi()
  if (res.code === 200) {
    permissionTree.value = res.data || []
  }
  permDialogVisible.value = true
}

const handleSavePerms = async () => {
  if (!currentRole.value || !treeRef.value) return
  const checkedKeys = treeRef.value.getCheckedKeys() as number[]
  const halfCheckedKeys = treeRef.value.getHalfCheckedKeys() as number[]
  const allIds = [...checkedKeys, ...halfCheckedKeys]

  assignLoading.value = true
  try {
    await assignPermissionsApi(currentRole.value.id, allIds)
    ElMessage.success('角色权限配置保存成功')
    permDialogVisible.value = false
  } finally {
    assignLoading.value = false
  }
}
</script>

<style scoped>
.role-management-container {
  display: flex;
  flex-direction: column;
}
</style>
