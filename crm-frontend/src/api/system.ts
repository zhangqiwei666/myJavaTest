import request from '@/utils/request'
import type { Result, SysUser, SysRole, SysPermission } from '@/types/api'

// ================= 用户管理 (SysUser) =================
export const getUserListApi = (): Promise<Result<SysUser[]>> => {
  return request.get('/sys/user/list')
}

export const createUserApi = (data: Partial<SysUser>): Promise<Result<void>> => {
  return request.post('/sys/user', data)
}

export const updateUserApi = (data: Partial<SysUser>): Promise<Result<void>> => {
  return request.put('/sys/user', data)
}

export const deleteUserApi = (id: number): Promise<Result<void>> => {
  return request.delete(`/sys/user/${id}`)
}

export const assignRolesApi = (userId: number, roleIds: number[]): Promise<Result<void>> => {
  return request.post(`/sys/user/${userId}/roles`, roleIds)
}

// ================= 角色管理 (SysRole) =================
export const getRoleListApi = (): Promise<Result<SysRole[]>> => {
  return request.get('/sys/role/list')
}

export const createRoleApi = (data: Partial<SysRole>): Promise<Result<void>> => {
  return request.post('/sys/role', data)
}

export const updateRoleApi = (data: Partial<SysRole>): Promise<Result<void>> => {
  return request.put('/sys/role', data)
}

export const deleteRoleApi = (id: number): Promise<Result<void>> => {
  return request.delete(`/sys/role/${id}`)
}

export const assignPermissionsApi = (roleId: number, permissionIds: number[]): Promise<Result<void>> => {
  return request.post(`/sys/role/${roleId}/permissions`, permissionIds)
}

// ================= 权限菜单 (SysPermission) =================
export const getPermissionTreeApi = (): Promise<Result<SysPermission[]>> => {
  return request.get('/sys/permission/tree')
}
