<template>
  <div class="layout-container">
    <!-- 侧边栏 Navigation Sidebar -->
    <aside class="sidebar" :class="{ collapsed: isCollapse }">
      <div class="logo-box">
        <div class="logo-icon">CRM</div>
        <span v-if="!isCollapse" class="logo-title">CRM 后台管理</span>
      </div>

      <el-menu
        :default-active="activeRoute"
        class="sidebar-menu"
        :collapse="isCollapse"
        background-color="#0f172a"
        text-color="#94a3b8"
        active-text-color="#ffffff"
        router
      >
        <el-menu-item index="/dashboard">
          <el-icon><Odometer /></el-icon>
          <template #title>系统首页</template>
        </el-menu-item>

        <el-sub-menu index="crm">
          <template #title>
            <el-icon><UserFilled /></el-icon>
            <span>CRM 客户管理</span>
          </template>
          <el-menu-item index="/crm/customer">客户档案</el-menu-item>
          <el-menu-item index="/crm/clue">销售线索</el-menu-item>
          <el-menu-item index="/crm/opportunity">销售商机</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="system">
          <template #title>
            <el-icon><Setting /></el-icon>
            <span>系统设置</span>
          </template>
          <el-menu-item index="/system/user">用户管理</el-menu-item>
          <el-menu-item index="/system/role">角色管理</el-menu-item>
          <el-menu-item index="/system/permission">权限菜单</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </aside>

    <!-- 主界面 Main Container -->
    <div class="main-wrapper">
      <!-- 顶部 Header Bar -->
      <header class="header-bar">
        <div class="header-left">
          <el-icon class="toggle-btn" @click="isCollapse = !isCollapse">
            <Expand v-if="isCollapse" />
            <Fold v-else />
          </el-icon>
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ currentTitle }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>

        <div class="header-right">
          <el-dropdown trigger="click" @command="handleUserCommand">
            <div class="user-info-trigger">
              <el-avatar :size="32" class="user-avatar">
                {{ userStore.realName.substring(0, 1) }}
              </el-avatar>
              <span class="user-name">{{ userStore.realName }}</span>
              <el-icon><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item disabled>
                  <el-icon><User /></el-icon> 账号：{{ userStore.username }}
                </el-dropdown-item>
                <el-dropdown-item divided command="logout">
                  <el-icon><SwitchButton /></el-icon> 退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </header>

      <!-- 页面内容主体 View Container -->
      <main class="content-body">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { ElMessageBox, ElMessage } from 'element-plus'

const isCollapse = ref(false)
const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const activeRoute = computed(() => route.path)
const currentTitle = computed(() => (route.meta.title as string) || '后台管理')

onMounted(() => {
  if (userStore.isLoggedIn && !userStore.userInfo) {
    userStore.fetchUserInfo()
  }
})

const handleUserCommand = async (command: string) => {
  if (command === 'logout') {
    try {
      await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      await userStore.logout()
      ElMessage.success('已成功退出')
      router.push('/login')
    } catch {
      // cancel
    }
  }
}
</script>

<style scoped>
.layout-container {
  display: flex;
  width: 100vw;
  height: 100vh;
  background-color: #f1f5f9;
  overflow: hidden;
}

.sidebar {
  width: 240px;
  background-color: #0f172a;
  color: #fff;
  transition: width 0.3s ease;
  display: flex;
  flex-direction: column;
  box-shadow: 4px 0 12px rgba(0, 0, 0, 0.05);
  z-index: 10;
}

.sidebar.collapsed {
  width: 64px;
}

.logo-box {
  height: 60px;
  display: flex;
  align-items: center;
  padding: 0 16px;
  gap: 12px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
}

.logo-icon {
  width: 36px;
  height: 36px;
  background: linear-gradient(135deg, #6366f1 0%, #4f46e5 100%);
  color: #fff;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 800;
  font-size: 14px;
  box-shadow: 0 4px 10px rgba(99, 102, 241, 0.3);
}

.logo-title {
  font-size: 16px;
  font-weight: 700;
  color: #f8fafc;
  white-space: nowrap;
}

.sidebar-menu {
  border-right: none;
  flex: 1;
}

.sidebar-menu :deep(.el-menu-item.is-active) {
  background: linear-gradient(90deg, #4f46e5 0%, #6366f1 100%) !important;
  border-radius: 8px;
  margin: 4px 8px;
  box-shadow: 0 4px 12px rgba(79, 70, 229, 0.3);
}

.main-wrapper {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.header-bar {
  height: 60px;
  background: #ffffff;
  border-bottom: 1px solid #e2e8f0;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.02);
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.toggle-btn {
  font-size: 20px;
  cursor: pointer;
  color: #64748b;
  transition: color 0.2s;
}

.toggle-btn:hover {
  color: #4f46e5;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info-trigger {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 6px;
  transition: background 0.2s;
}

.user-info-trigger:hover {
  background: #f1f5f9;
}

.user-avatar {
  background: linear-gradient(135deg, #4f46e5 0%, #06b6d4 100%);
  color: white;
  font-weight: bold;
}

.user-name {
  font-size: 14px;
  font-weight: 600;
  color: #334155;
}

.content-body {
  flex: 1;
  padding: 24px;
  overflow-y: auto;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
