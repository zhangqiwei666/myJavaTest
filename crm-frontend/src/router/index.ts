import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'
import MainLayout from '@/layout/MainLayout.vue'

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/LoginView.vue'),
    meta: { title: '用户登录' }
  },
  {
    path: '/',
    component: MainLayout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/DashboardView.vue'),
        meta: { title: '系统首页概览', icon: 'Odometer' }
      },
      {
        path: 'system/user',
        name: 'UserManagement',
        component: () => import('@/views/system/UserManagement.vue'),
        meta: { title: '用户管理', icon: 'User' }
      },
      {
        path: 'system/role',
        name: 'RoleManagement',
        component: () => import('@/views/system/RoleManagement.vue'),
        meta: { title: '角色管理', icon: 'Lock' }
      },
      {
        path: 'system/permission',
        name: 'PermissionManagement',
        component: () => import('@/views/system/PermissionManagement.vue'),
        meta: { title: '权限菜单', icon: 'Menu' }
      },
      {
        path: 'crm/customer',
        name: 'CustomerManagement',
        component: () => import('@/views/crm/CustomerManagement.vue'),
        meta: { title: '客户管理', icon: 'UserFilled' }
      },
      {
        path: 'crm/clue',
        name: 'ClueManagement',
        component: () => import('@/views/crm/ClueManagement.vue'),
        meta: { title: '销售线索', icon: 'Opportunity' }
      },
      {
        path: 'crm/opportunity',
        name: 'OpportunityManagement',
        component: () => import('@/views/crm/OpportunityManagement.vue'),
        meta: { title: '销售商机', icon: 'Money' }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/dashboard'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由全局前置守卫：进行登录鉴权
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('crm_token')
  if (to.path !== '/login' && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/')
  } else {
    if (to.meta.title) {
      document.title = `${to.meta.title} - CRM 客户关系管理系统`
    }
    next()
  }
})

export default router
