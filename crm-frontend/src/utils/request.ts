import axios, { type AxiosInstance, type InternalAxiosRequestConfig, type AxiosResponse } from 'axios'
import { ElMessage } from 'element-plus'
import type { Result } from '@/types/api'

const request: AxiosInstance = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器：自动注入 JWT Bearer Token
request.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const token = localStorage.getItem('crm_token')
    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器：统一处理 200/401/500 Code 与错误提示
request.interceptors.response.use(
  (response: AxiosResponse<Result>) => {
    const res = response.data
    // Spring Boot Result 约定: 200 为成功
    if (res.code !== 200) {
      ElMessage.error(res.message || '请求失败，请稍后重试')
      if (res.code === 401) {
        localStorage.removeItem('crm_token')
        window.location.href = '/login'
      }
      return Promise.reject(new Error(res.message || 'Error'))
    }
    return res as any
  },
  (error) => {
    if (error.response?.status === 401) {
      ElMessage.error('登录状态已过期，请重新登录')
      localStorage.removeItem('crm_token')
      window.location.href = '/login'
    } else {
      ElMessage.error(error.response?.data?.message || error.message || '网络连接异常')
    }
    return Promise.reject(error)
  }
)

export default request
