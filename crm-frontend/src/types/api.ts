// 统一 API 响应结构类比前端的标准 Response
export interface Result<T = any> {
  code: number
  message: string
  data: T
}

// 登录请求参数
export interface LoginParams {
  username: string
  password: string
}

// 登录响应 Token 数据
export interface LoginResult {
  token: string
}

// 用户对象实体类型 (对应 backend SysUser)
export interface SysUser {
  id: number
  username: string
  realName: string
  phone: string
  email: string
  status: number // 1: 正常, 0: 禁用
  roles?: string[]
  permissions?: string[]
  createTime?: string
  updateTime?: string
}

// 角色对象实体类型 (对应 backend SysRole)
export interface SysRole {
  id: number
  roleName: string
  roleKey: string
  description?: string
  createTime?: string
}

// 权限菜单实体类型 (对应 backend SysPermission)
export interface SysPermission {
  id: number
  parentId: number
  name: string
  permKey: string
  type: number // 1-目录 2-菜单 3-按钮
  path?: string
  sort?: number
  children?: SysPermission[]
}

// CRM 客户档案类型 (对应 backend CrmCustomer)
export interface CrmCustomer {
  id: number
  name: string
  phone?: string
  email?: string
  company?: string
  address?: string
  industry?: string
  level?: 'VIP客户' | '重要客户' | '普通客户'
  status?: '潜在' | '跟进中' | '已成交' | '已流失'
  ownerId?: number
  creatorId?: number
  remark?: string
  createTime?: string
  updateTime?: string
}

// CRM 销售线索类型 (对应 backend CrmClue)
export interface CrmClue {
  id: number
  name: string
  phone?: string
  company?: string
  source?: string
  status?: '未处理' | '转化中' | '已转化' | '已作废'
  ownerId?: number
  createTime?: string
}

// CRM 销售商机类型 (对应 backend CrmOpportunity)
export interface CrmOpportunity {
  id: number
  customerId: number
  customerName?: string
  name: string
  amount: number
  stage: '初步沟通' | '需求确认' | '方案报价' | '签订合同' | '赢单'
  ownerId?: number
  createTime?: string
}
