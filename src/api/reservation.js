import request from '@/utils/request'

// 创建预约
export function createReservation(data) {
  return request.post('/reservations', data)
}

// 取消预约
export function cancelReservation(id) {
  return request.put(`/reservations/${id}/cancel`)
}

// 签到
export function checkin(id) {
  return request.post(`/reservations/${id}/checkin`, null, { params: { ip: '127.0.0.1' } })
}

// 签退
export function checkout(id) {
  return request.post(`/reservations/${id}/checkout`, null, { params: { ip: '127.0.0.1' } })
}

// 我的预约记录
export function getMyReservations(params) {
  return request.get('/reservations/my', { params })
}

// 管理员获取所有预约
export function getAllReservations(params) {
  return request.get('/admin/reservations', { params })
}
