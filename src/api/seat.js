import request from '@/utils/request'

// 获取座位列表（带预约状态）
export function getSeatsByRoom(roomId, date, startTime, endTime) {
  return request.get(`/seats/room/${roomId}`, {
    params: { date, startTime, endTime }
  })
}

// 获取管理员视角的座位列表
export function getAdminSeats(roomId) {
  return request.get(`/admin/rooms/${roomId}/seats`)
}

// 修改座位状态
export function changeSeatStatus(id, status) {
  return request.put(`/admin/seats/${id}/status`, null, { params: { status } })
}

// 修改座位类型
export function changeSeatType(id, type) {
  return request.put(`/admin/seats/${id}/type`, null, { params: { type } })
}
