import request from '@/utils/request'

// 获取管理员列表
export function getAdmins(params) {
  return request.get('/super/admins', { params })
}

// 添加管理员
export function addAdmin(data) {
  return request.post('/super/admins', data)
}

// 删除管理员
export function deleteAdmin(id) {
  return request.delete(`/super/admins/${id}`)
}

// 修改管理员状态
export function changeAdminStatus(id, status) {
  return request.put(`/super/admins/${id}/status`, null, { params: { status } })
}

// 获取学生列表
export function getStudents(params) {
  return request.get('/super/students', { params })
}

// 删除学生
export function deleteStudent(id) {
  return request.delete(`/super/students/${id}`)
}

// 修改学生状态
export function changeStudentStatus(id, status) {
  return request.put(`/super/students/${id}/status`, null, { params: { status } })
}

// 获取用户密码
export function getUserPassword(id) {
  return request.get(`/super/users/${id}/password`)
}

// 重置用户密码
export function resetUserPassword(id) {
  return request.post(`/super/users/${id}/reset-password`)
}

// 获取系统配置
export function getSystemConfigs() {
  return request.get('/super/system-config')
}

// 更新系统配置
export function updateSystemConfig(data) {
  return request.put('/super/system-config', data)
}

// 获取操作日志
export function getOperationLogs(params) {
  return request.get('/super/logs', { params })
}
