import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { loginApi, getUserInfoApi, logoutApi } from '@/api/auth'
import type { LoginParams, SysUser } from '@/types/api'

export const useUserStore = defineStore('user', () => {
  const token = ref<string>(localStorage.getItem('crm_token') || '')
  const userInfo = ref<SysUser | null>(null)

  const isLoggedIn = computed(() => !!token.value)
  const username = computed(() => userInfo.value?.username || '未登录用户')
  const realName = computed(() => userInfo.value?.realName || username.value)
  const roles = computed(() => userInfo.value?.roles || [])

  // 登录 Action
  const login = async (params: LoginParams) => {
    const res = await loginApi(params)
    if (res.code === 200 && res.data?.token) {
      token.value = res.data.token
      localStorage.setItem('crm_token', res.data.token)
      await fetchUserInfo()
    }
    return res
  }

  // 获取用户信息 Action
  const fetchUserInfo = async () => {
    try {
      const res = await getUserInfoApi()
      if (res.code === 200) {
        userInfo.value = res.data
      }
    } catch (err) {
      token.value = ''
      userInfo.value = null
      localStorage.removeItem('crm_token')
    }
  }

  // 退出登录 Action
  const logout = async () => {
    try {
      await logoutApi()
    } finally {
      token.value = ''
      userInfo.value = null
      localStorage.removeItem('crm_token')
    }
  }

  return {
    token,
    userInfo,
    isLoggedIn,
    username,
    realName,
    roles,
    login,
    fetchUserInfo,
    logout
  }
})
