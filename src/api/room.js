import request from '@/utils/request'

// 获取开放教室列表
export function getRooms() {
  return request.get('/rooms')
}

// 获取教室详情
export function getRoomById(id) {
  return request.get(`/rooms/${id}`)
}

// 分页查询教室
export function pageRooms(params) {
  return request.get('/rooms/page', { params })
}

// 添加教室
export function addRoom(data) {
  return request.post('/admin/rooms', data)
}

// 更新教室
export function updateRoom(id, data) {
  return request.put(`/admin/rooms/${id}`, data)
}

// 修改教室状态
export function changeRoomStatus(id, status) {
  return request.put(`/admin/rooms/${id}/status`, null, { params: { status } })
}

// 删除教室
export function deleteRoom(id) {
  return request.delete(`/admin/rooms/${id}`)
}
