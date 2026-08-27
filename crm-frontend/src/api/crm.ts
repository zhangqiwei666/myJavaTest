import request from '@/utils/request'
import type { Result, CrmCustomer, CrmClue, CrmOpportunity } from '@/types/api'

// ================= 客户管理 (CrmCustomer) =================
export const getCustomerListApi = (params?: { keyword?: string; status?: string }): Promise<Result<CrmCustomer[]>> => {
  return request.get('/crm/customer/page', { params })
}

export const getCustomerByIdApi = (id: number): Promise<Result<CrmCustomer>> => {
  return request.get(`/crm/customer/${id}`)
}

export const createCustomerApi = (data: Partial<CrmCustomer>): Promise<Result<void>> => {
  return request.post('/crm/customer', data)
}

export const updateCustomerApi = (data: Partial<CrmCustomer>): Promise<Result<void>> => {
  return request.put('/crm/customer', data)
}

export const deleteCustomerApi = (id: number): Promise<Result<void>> => {
  return request.delete(`/crm/customer/${id}`)
}

// ================= 销售线索 (CrmClue) =================
export const getClueListApi = (): Promise<Result<CrmClue[]>> => {
  return request.get('/crm/clue/list')
}

export const createClueApi = (data: Partial<CrmClue>): Promise<Result<void>> => {
  return request.post('/crm/clue', data)
}

export const updateClueApi = (data: Partial<CrmClue>): Promise<Result<void>> => {
  return request.put('/crm/clue', data)
}

export const deleteClueApi = (id: number): Promise<Result<void>> => {
  return request.delete(`/crm/clue/${id}`)
}

export const convertClueToCustomerApi = (id: number): Promise<Result<void>> => {
  return request.post(`/crm/clue/${id}/convert`)
}

// ================= 销售商机 (CrmOpportunity) =================
export const getOpportunityListApi = (): Promise<Result<CrmOpportunity[]>> => {
  return request.get('/crm/opportunity/list')
}

export const createOpportunityApi = (data: Partial<CrmOpportunity>): Promise<Result<void>> => {
  return request.post('/crm/opportunity', data)
}

export const updateOpportunityApi = (data: Partial<CrmOpportunity>): Promise<Result<void>> => {
  return request.put('/crm/opportunity', data)
}

export const deleteOpportunityApi = (id: number): Promise<Result<void>> => {
  return request.delete(`/crm/opportunity/${id}`)
}
