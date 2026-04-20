<template>
  <div class="dashboard">
    <!-- Welcome Section -->
    <div class="welcome-section">
      <div class="welcome-content">
        <div class="welcome-greeting">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M22 10v6M2 10l10-5 10 5-10 5z"/>
            <path d="M6 12v5c3 3 9 3 12 0v-5"/>
          </svg>
          你好，{{ userStore.realName }}
        </div>
        <p class="welcome-subtitle">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <rect x="3" y="4" width="18" height="18" rx="2" ry="2"/>
            <line x1="16" y1="2" x2="16" y2="6"/>
            <line x1="8" y1="2" x2="8" y2="6"/>
            <line x1="3" y1="10" x2="21" y2="10"/>
          </svg>
          今天是 {{ todayStr }}，祝你学习愉快！
        </p>
      </div>
      <a-button type="primary" size="large" class="welcome-btn" @click="$router.push('/student/rooms')">
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <rect x="3" y="4" width="18" height="18" rx="2" ry="2"/>
          <line x1="16" y1="2" x2="16" y2="6"/>
          <line x1="8" y1="2" x2="8" y2="6"/>
          <line x1="3" y1="10" x2="21" y2="10"/>
        </svg>
        预约座位
      </a-button>
    </div>

    <!-- Stats Grid -->
    <div class="stats-grid">
      <div class="stat-card" v-for="(stat, index) in stats" :key="stat.label" :style="{ animationDelay: `${index * 0.1}s` }">
        <div class="stat-icon" :style="{ background: stat.bgColor }">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" v-html="stat.icon"></svg>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ stat.value }}</div>
          <div class="stat-label">{{ stat.label }}</div>
        </div>
        <div class="stat-trend" :class="stat.trendClass" v-if="stat.trend">
          {{ stat.trend }}
        </div>
      </div>
    </div>

    <!-- Main Content Grid -->
    <div class="content-grid">
      <!-- Quick Actions -->
      <div class="card quick-actions-card">
        <div class="card-header">
          <h3 class="card-title">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <polygon points="13 2 3 14 12 14 11 22 21 10 12 10 13 2"/>
            </svg>
            快捷操作
          </h3>
        </div>
        <div class="actions-list">
          <div 
            class="action-item" 
            v-for="action in quickActions" 
            :key="action.path"
            @click="$router.push(action.path)"
          >
            <div class="action-icon" :style="{ background: action.bgColor }">
              <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" v-html="action.icon"></svg>
            </div>
            <div class="action-content">
              <div class="action-title">{{ action.title }}</div>
              <div class="action-desc">{{ action.desc }}</div>
            </div>
            <svg class="action-arrow" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M9 18l6-6-6-6"/>
            </svg>
          </div>
        </div>
      </div>

      <!-- Recent Reservations -->
      <div class="card recent-card">
        <div class="card-header">
          <h3 class="card-title">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
              <polyline points="14 2 14 8 20 8"/>
              <line x1="16" y1="13" x2="8" y2="13"/>
              <line x1="16" y1="17" x2="8" y2="17"/>
            </svg>
            最近预约
          </h3>
          <router-link to="/student/records" class="card-link">查看全部</router-link>
        </div>
        <div class="table-container">
          <a-table
            v-if="recentReservations.length > 0"
            :columns="columns"
            :data-source="recentReservations"
            :pagination="false"
            size="small"
            :locale="{ emptyText: '暂无预约记录' }"
          >
            <template #bodyCell="{ column, record }">
              <template v-if="column.key === 'status'">
                <span class="status-badge" :class="statusClass(record.status)">
                  {{ statusTexts[record.status] }}
                </span>
              </template>
              <template v-if="column.key === 'time'">
                {{ record.startTime }} - {{ record.endTime }}
              </template>
            </template>
          </a-table>
          <div v-else class="empty-state">
            <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
              <rect x="3" y="4" width="18" height="18" rx="2" ry="2"/>
              <line x1="16" y1="2" x2="16" y2="6"/>
              <line x1="8" y1="2" x2="8" y2="6"/>
              <line x1="3" y1="10" x2="21" y2="10"/>
            </svg>
            <p>暂无预约记录</p>
            <a-button type="primary" size="small" @click="$router.push('/student/rooms')">去预约</a-button>
          </div>
        </div>
      </div>
    </div>

    <!-- Today's Reservations -->
    <div class="card today-card" v-if="todayReservations.length > 0">
      <div class="card-header">
        <h3 class="card-title">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <circle cx="12" cy="12" r="10"/>
            <polyline points="12 6 12 12 16 14"/>
          </svg>
          今日预约
          <span class="card-badge">{{ todayReservations.length }}</span>
        </h3>
      </div>
      <div class="today-list">
        <div 
          class="today-item" 
          v-for="reservation in todayReservations" 
          :key="reservation.id"
        >
          <div class="today-time">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="12" cy="12" r="10"/>
              <polyline points="12 6 12 12 16 14"/>
            </svg>
            <span class="time-value">{{ reservation.startTime }} - {{ reservation.endTime }}</span>
          </div>
          <div class="today-info">
            <div class="today-room">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/>
                <polyline points="9 22 9 12 15 12 15 22"/>
              </svg>
              {{ reservation.roomName }}
            </div>
            <div class="today-seat">座位 {{ reservation.seatNo }}</div>
          </div>
          <div class="today-status">
            <span class="status-badge" :class="statusClass(reservation.status)">
              {{ statusTexts[reservation.status] }}
            </span>
            <a-button 
              v-if="reservation.status === 0"
              type="primary" 
              size="small"
              @click="$router.push('/student/checkin')"
            >
              去签到
            </a-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { getMyReservations } from '@/api/reservation'

const userStore = useUserStore()

const todayStr = computed(() => {
  const d = new Date()
  const weekDays = ['日', '一', '二', '三', '四', '五', '六']
  return `${d.getMonth() + 1}月${d.getDate()}日 星期${weekDays[d.getDay()]}`
})

const recentReservations = ref([])
const todayReservations = ref([])

const statusTexts = { 0: '待签到', 1: '已签到', 2: '已签退', 3: '已取消', 4: '违约' }

const statusClass = (status) => {
  const classes = {
    0: 'status-pending',
    1: 'status-active',
    2: 'status-done',
    3: 'status-cancelled',
    4: 'status-violation'
  }
  return classes[status] || ''
}

const stats = computed(() => [
  { 
    label: '今日预约', 
    value: todayReservations.value.length,
    bgColor: 'linear-gradient(135deg, rgba(6, 182, 212, 0.2), rgba(6, 182, 212, 0.1))',
    icon: '<rect x="3" y="4" width="18" height="18" rx="2" ry="2"/><line x1="16" y1="2" x2="16" y2="6"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/>',
    trendClass: ''
  },
  { 
    label: '已签到', 
    value: todayReservations.value.filter(r => r.status === 1).length,
    bgColor: 'linear-gradient(135deg, rgba(16, 185, 129, 0.2), rgba(16, 185, 129, 0.1))',
    icon: '<path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/><polyline points="22 4 12 14.01 9 11.01"/>',
    trendClass: 'trend-up'
  },
  { 
    label: '本月预约', 
    value: recentReservations.value.length,
    bgColor: 'linear-gradient(135deg, rgba(139, 92, 246, 0.2), rgba(139, 92, 246, 0.1))',
    icon: '<path d="M12 2L2 7l10 5 10-5-10-5z"/><path d="M2 17l10 5 10-5"/><path d="M2 12l10 5 10-5"/>',
    trendClass: ''
  },
  { 
    label: '违约次数', 
    value: userStore.violationCount || 0,
    bgColor: recentReservations.value.filter(r => r.status === 4).length > 0 
      ? 'linear-gradient(135deg, rgba(239, 68, 68, 0.2), rgba(239, 68, 68, 0.1))'
      : 'linear-gradient(135deg, rgba(100, 116, 139, 0.2), rgba(100, 116, 139, 0.1))',
    icon: '<path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"/><line x1="12" y1="9" x2="12" y2="13"/><line x1="12" y1="17" x2="12.01" y2="17"/>',
    trendClass: (userStore.violationCount || 0) > 0 ? 'trend-down' : 'trend-neutral'
  }
])

const quickActions = [
  { 
    title: '浏览自习室', 
    desc: '查看可用教室和座位',
    path: '/student/rooms',
    bgColor: 'linear-gradient(135deg, rgba(6, 182, 212, 0.2), rgba(6, 182, 212, 0.1))',
    icon: '<path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/><polyline points="9 22 9 12 15 12 15 22"/>'
  },
  { 
    title: '签到签退', 
    desc: '点击座位进行签到',
    path: '/student/checkin',
    bgColor: 'linear-gradient(135deg, rgba(16, 185, 129, 0.2), rgba(16, 185, 129, 0.1))',
    icon: '<path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/><polyline points="22 4 12 14.01 9 11.01"/>'
  },
  { 
    title: '预约记录', 
    desc: '查看历史预约',
    path: '/student/records',
    bgColor: 'linear-gradient(135deg, rgba(139, 92, 246, 0.2), rgba(139, 92, 246, 0.1))',
    icon: '<path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><polyline points="14 2 14 8 20 8"/><line x1="16" y1="13" x2="8" y2="13"/><line x1="16" y1="17" x2="8" y2="17"/>'
  }
]

const columns = [
  { title: '教室', dataIndex: 'roomName', key: 'roomName' },
  { title: '座位', dataIndex: 'seatNo', key: 'seatNo', width: 80 },
  { title: '日期', dataIndex: 'date', key: 'date', width: 100 },
  { title: '时间', key: 'time', width: 140 },
  { title: '状态', key: 'status', width: 80 }
]

onMounted(async () => {
  try {
    const res = await getMyReservations({ pageSize: 10 })
    const records = res.data.records || []
    recentReservations.value = records
    
    const today = new Date().toISOString().split('T')[0]
    todayReservations.value = records.filter(r => 
      r.date === today && (r.status === 0 || r.status === 1)
    )
  } catch (e) {
    console.error('Failed to load reservations:', e)
  }
})
</script>

<style scoped>
.dashboard {
  max-width: 1400px;
  margin: 0 auto;
  animation: fadeIn 0.5s ease-out;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

/* Welcome Section */
.welcome-section {
  background: linear-gradient(135deg, #0891b2 0%, #06b6d4 50%, #22d3ee 100%);
  border-radius: var(--radius-xl);
  padding: 28px 32px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  box-shadow: 0 8px 32px rgba(6, 182, 212, 0.25);
}

.welcome-content {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.welcome-greeting {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 1.5rem;
  font-weight: 700;
  color: white;
}

.welcome-subtitle {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.95rem;
  color: rgba(255, 255, 255, 0.85);
  margin: 0;
}

.welcome-btn {
  background: white !important;
  color: var(--accent-primary) !important;
  border: none !important;
  font-weight: 600;
  padding: 12px 24px !important;
  height: auto !important;
  border-radius: var(--radius-md) !important;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: all var(--transition-normal);
}

.welcome-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

/* Stats Grid */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 24px;
}

.stat-card {
  background: var(--bg-card);
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-lg);
  padding: 20px;
  display: flex;
  align-items: flex-start;
  gap: 16px;
  transition: all var(--transition-normal);
  animation: slideUp 0.5s ease-out both;
}

@keyframes slideUp {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

.stat-card:hover {
  transform: translateY(-4px);
  border-color: var(--border-primary);
  box-shadow: var(--shadow-lg);
}

.stat-icon {
  width: 52px;
  height: 52px;
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--accent-primary);
  flex-shrink: 0;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 1.75rem;
  font-weight: 700;
  color: var(--text-primary);
  line-height: 1.2;
}

.stat-label {
  font-size: 0.85rem;
  color: var(--text-muted);
  margin-top: 4px;
}

.stat-trend {
  font-size: 0.75rem;
  font-weight: 600;
  padding: 4px 8px;
  border-radius: var(--radius-full);
}

.trend-up {
  background: var(--success-bg);
  color: var(--success);
}

.trend-down {
  background: var(--error-bg);
  color: var(--error);
}

.trend-neutral {
  background: var(--bg-elevated);
  color: var(--text-muted);
}

/* Content Grid */
.content-grid {
  display: grid;
  grid-template-columns: 1fr 2fr;
  gap: 24px;
  margin-bottom: 24px;
}

/* Card Styles */
.card {
  background: var(--bg-card);
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-lg);
  overflow: hidden;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 20px;
  border-bottom: 1px solid var(--border-subtle);
  background: var(--bg-elevated);
}

.card-title {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 1rem;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0;
}

.card-title svg {
  color: var(--accent-primary);
}

.card-badge {
  background: var(--accent-primary);
  color: var(--bg-primary);
  font-size: 0.75rem;
  font-weight: 700;
  padding: 2px 8px;
  border-radius: var(--radius-full);
}

.card-link {
  font-size: 0.875rem;
  color: var(--accent-primary);
  text-decoration: none;
  font-weight: 500;
  transition: all var(--transition-fast);
}

.card-link:hover {
  color: var(--accent-secondary);
}

/* Quick Actions */
.actions-list {
  padding: 12px;
}

.action-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px;
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.action-item:hover {
  background: var(--bg-hover);
}

.action-icon {
  width: 48px;
  height: 48px;
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--accent-primary);
  flex-shrink: 0;
}

.action-content {
  flex: 1;
}

.action-title {
  font-size: 0.95rem;
  font-weight: 600;
  color: var(--text-primary);
}

.action-desc {
  font-size: 0.8rem;
  color: var(--text-muted);
  margin-top: 2px;
}

.action-arrow {
  color: var(--text-muted);
  transition: all var(--transition-fast);
}

.action-item:hover .action-arrow {
  color: var(--accent-primary);
  transform: translateX(4px);
}

/* Table */
.table-container {
  padding: 16px;
}

/* Empty State */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
  color: var(--text-muted);
  text-align: center;
}

.empty-state svg {
  margin-bottom: 16px;
  opacity: 0.5;
}

.empty-state p {
  margin-bottom: 16px;
}

/* Status Badges */
.status-badge {
  display: inline-flex;
  align-items: center;
  padding: 4px 10px;
  border-radius: var(--radius-full);
  font-size: 0.75rem;
  font-weight: 500;
}

.status-pending {
  background: var(--warning-bg);
  color: var(--warning);
}

.status-active {
  background: var(--success-bg);
  color: var(--success);
}

.status-done {
  background: var(--info-bg);
  color: var(--info);
}

.status-cancelled {
  background: var(--bg-elevated);
  color: var(--text-muted);
}

.status-violation {
  background: var(--error-bg);
  color: var(--error);
}

/* Today Card */
.today-list {
  padding: 8px 16px;
}

.today-item {
  display: flex;
  align-items: center;
  gap: 24px;
  padding: 16px;
  border-radius: var(--radius-md);
  transition: all var(--transition-fast);
}

.today-item:hover {
  background: var(--bg-hover);
}

.today-time {
  display: flex;
  align-items: center;
  gap: 8px;
  min-width: 160px;
}

.today-time svg {
  color: var(--accent-primary);
}

.time-value {
  font-size: 0.9rem;
  font-weight: 600;
  color: var(--text-primary);
}

.today-info {
  flex: 1;
}

.today-room {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.9rem;
  font-weight: 500;
  color: var(--text-primary);
}

.today-room svg {
  color: var(--text-muted);
}

.today-seat {
  font-size: 0.8rem;
  color: var(--text-muted);
  margin-top: 4px;
  margin-left: 24px;
}

.today-status {
  display: flex;
  align-items: center;
  gap: 12px;
}

/* Responsive */
@media (max-width: 1200px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .content-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .welcome-section {
    flex-direction: column;
    text-align: center;
    gap: 20px;
  }
  
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .today-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
}
</style>
