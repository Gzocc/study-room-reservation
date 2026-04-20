import request from '@/utils/request'

// 获取仪表盘统计数据
export function getDashboard() {
  return request.get('/admin/dashboard')
}
