import request from '@/utils/request'
import type { Result, LoginParams, LoginResult, SysUser } from '@/types/api'

// 1. 用户登录
export const loginApi = (data: LoginParams): Promise<Result<LoginResult>> => {
  return request.post('/auth/login', data)
}

// 2. 获取当前登录用户信息
export const getUserInfoApi = (): Promise<Result<SysUser>> => {
  return request.get('/auth/info')
}

// 3. 退出登录
export const logoutApi = (): Promise<Result<void>> => {
  return request.post('/auth/logout')
}
