<template>
  <div class="permission-management-container">
    <div class="page-header">
      <div class="page-title">
        <el-icon><Menu /></el-icon>
        <span>系统权限菜单树</span>
      </div>
    </div>

    <el-card shadow="never" class="glass-card">
      <el-table
        :data="treeData"
        v-loading="loading"
        row-key="id"
        default-expand-all
        stripe
        style="width: 100%"
      >
        <el-table-column prop="name" label="菜单/权限名称" min-width="180" />
        <el-table-column prop="permKey" label="权限标识 (Perm Key)" min-width="180">
          <template #default="{ row }">
            <el-tag type="info" v-if="row.permKey">{{ row.permKey }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="type" label="类型" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.type === 1" type="primary">目录</el-tag>
            <el-tag v-else-if="row.type === 2" type="success">菜单</el-tag>
            <el-tag v-else type="warning">按钮</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="path" label="路由 Path" min-width="160" />
        <el-table-column prop="sort" label="排序" width="80" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { Menu } from '@element-plus/icons-vue'
import { getPermissionTreeApi } from '@/api/system'
import type { SysPermission } from '@/types/api'

const loading = ref(false)
const treeData = ref<SysPermission[]>([])

const fetchTree = async () => {
  loading.value = true
  try {
    const res = await getPermissionTreeApi()
    if (res.code === 200) treeData.value = res.data || []
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchTree()
})
</script>

<style scoped>
.permission-management-container {
  display: flex;
  flex-direction: column;
}
</style>
