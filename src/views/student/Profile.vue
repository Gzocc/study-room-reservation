<template>
  <div class="profile-page">
    <!-- Profile Header -->
    <div class="profile-header">
      <div class="avatar-section">
        <div class="avatar">
          {{ userStore.realName?.charAt(0) || 'S' }}
        </div>
        <div class="avatar-info">
          <h2 class="user-name">{{ userStore.realName }}</h2>
          <span class="user-role">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M22 10v6M2 10l10-5 10 5-10 5z"/>
              <path d="M6 12v5c3 3 9 3 12 0v-5"/>
            </svg>
            学生
          </span>
        </div>
      </div>
    </div>

    <!-- Info Cards -->
    <div class="info-grid">
      <div class="info-card">
        <div class="info-icon">
          <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
            <circle cx="12" cy="7" r="4"/>
          </svg>
        </div>
        <div class="info-content">
          <div class="info-label">用户名</div>
          <div class="info-value">{{ userStore.username }}</div>
        </div>
      </div>
      <div class="info-card">
        <div class="info-icon">
          <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
            <circle cx="12" cy="7" r="4"/>
          </svg>
        </div>
        <div class="info-content">
          <div class="info-label">姓名</div>
          <div class="info-value">{{ userStore.realName }}</div>
        </div>
      </div>
      <div class="info-card">
        <div class="info-icon role">
          <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M22 10v6M2 10l10-5 10 5-10 5z"/>
            <path d="M6 12v5c3 3 9 3 12 0v-5"/>
          </svg>
        </div>
        <div class="info-content">
          <div class="info-label">角色</div>
          <div class="info-value">学生</div>
        </div>
      </div>
      <div class="info-card" :class="{ 'has-violations': (userStore.violationCount || 0) > 0 }">
        <div class="info-icon warning">
          <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"/>
            <line x1="12" y1="9" x2="12" y2="13"/>
            <line x1="12" y1="17" x2="12.01" y2="17"/>
          </svg>
        </div>
        <div class="info-content">
          <div class="info-label">违约次数</div>
          <div class="info-value" :class="{ 'text-error': (userStore.violationCount || 0) > 0 }">
            {{ userStore.violationCount || 0 }}
          </div>
        </div>
      </div>
    </div>

    <!-- Quick Stats -->
    <div class="stats-card">
      <h3 class="card-title">
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <line x1="18" y1="20" x2="18" y2="10"/>
          <line x1="12" y1="20" x2="12" y2="4"/>
          <line x1="6" y1="20" x2="6" y2="14"/>
        </svg>
        使用统计
      </h3>
      <div class="stats-grid">
        <div class="stat-item">
          <div class="stat-icon">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <rect x="3" y="4" width="18" height="18" rx="2" ry="2"/>
              <line x1="16" y1="2" x2="16" y2="6"/>
              <line x1="8" y1="2" x2="8" y2="6"/>
              <line x1="3" y1="10" x2="21" y2="10"/>
            </svg>
          </div>
          <div class="stat-value">{{ stats.totalReservations }}</div>
          <div class="stat-label">总预约次数</div>
        </div>
        <div class="stat-item">
          <div class="stat-icon success">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/>
              <polyline points="22 4 12 14.01 9 11.01"/>
            </svg>
          </div>
          <div class="stat-value">{{ stats.checkedIn }}</div>
          <div class="stat-label">已完成</div>
        </div>
        <div class="stat-item">
          <div class="stat-icon cancelled">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="12" cy="12" r="10"/>
              <line x1="15" y1="9" x2="9" y2="15"/>
              <line x1="9" y1="9" x2="15" y2="15"/>
            </svg>
          </div>
          <div class="stat-value">{{ stats.cancelled }}</div>
          <div class="stat-label">已取消</div>
        </div>
        <div class="stat-item">
          <div class="stat-icon danger">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"/>
              <line x1="12" y1="9" x2="12" y2="13"/>
              <line x1="12" y1="17" x2="12.01" y2="17"/>
            </svg>
          </div>
          <div class="stat-value" :class="{ 'text-error': stats.violations > 0 }">{{ stats.violations }}</div>
          <div class="stat-label">违约</div>
        </div>
      </div>
    </div>

    <!-- Actions -->
    <div class="actions-card">
      <h3 class="card-title">
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <polygon points="13 2 3 14 12 14 11 22 21 10 12 10 13 2"/>
        </svg>
        快捷操作
      </h3>
      <div class="actions-list">
        <div class="action-item" @click="$router.push('/student/rooms')">
          <div class="action-icon">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/>
              <polyline points="9 22 9 12 15 12 15 22"/>
            </svg>
          </div>
          <span class="action-text">去预约座位</span>
          <svg class="action-arrow" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M9 18l6-6-6-6"/>
          </svg>
        </div>
        <div class="action-item" @click="$router.push('/student/records')">
          <div class="action-icon">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
              <polyline points="14 2 14 8 20 8"/>
            </svg>
          </div>
          <span class="action-text">查看预约记录</span>
          <svg class="action-arrow" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M9 18l6-6-6-6"/>
          </svg>
        </div>
        <div class="action-item danger" @click="handleLogout">
          <div class="action-icon">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/>
              <polyline points="16 17 21 12 16 7"/>
              <line x1="21" y1="12" x2="9" y2="12"/>
            </svg>
          </div>
          <span class="action-text">退出登录</span>
          <svg class="action-arrow" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M9 18l6-6-6-6"/>
          </svg>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getMyReservations } from '@/api/reservation'

const router = useRouter()
const userStore = useUserStore()

const stats = reactive({
  totalReservations: 0,
  checkedIn: 0,
  cancelled: 0,
  violations: 0
})

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}

onMounted(async () => {
  try {
    const res = await getMyReservations({ pageSize: 100 })
    const records = res.data.records || []
    stats.totalReservations = records.length
    stats.checkedIn = records.filter(r => r.status === 1 || r.status === 2).length
    stats.cancelled = records.filter(r => r.status === 3).length
    stats.violations = records.filter(r => r.status === 4).length
  } catch (e) {
    console.error('Failed to load stats:', e)
  }
})
</script>

<style scoped>
.profile-page {
  max-width: 800px;
  margin: 0 auto;
  animation: fadeIn 0.5s ease-out;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

/* Profile Header */
.profile-header {
  background: linear-gradient(135deg, #0891b2 0%, #06b6d4 50%, #22d3ee 100%);
  border-radius: var(--radius-xl);
  padding: 32px;
  margin-bottom: 24px;
  box-shadow: 0 8px 32px rgba(6, 182, 212, 0.25);
}

.avatar-section {
  display: flex;
  align-items: center;
  gap: 20px;
}

.avatar {
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, #fff 0%, rgba(255,255,255,0.8) 100%);
  border-radius: var(--radius-lg);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 2rem;
  font-weight: 700;
  color: var(--accent-primary);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);
}

.user-name {
  font-size: 1.5rem;
  font-weight: 700;
  color: white;
  margin: 0 0 8px;
}

.user-role {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  font-size: 0.9rem;
  color: rgba(255, 255, 255, 0.9);
  padding: 6px 14px;
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(10px);
  border-radius: var(--radius-full);
}

/* Info Grid */
.info-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
  margin-bottom: 24px;
}

.info-card {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
  background: var(--bg-card);
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-lg);
  transition: all var(--transition-fast);
}

.info-card:hover {
  border-color: var(--border-primary);
}

.info-card.has-violations {
  border-color: var(--error);
  background: var(--error-bg);
}

.info-icon {
  width: 48px;
  height: 48px;
  background: linear-gradient(135deg, rgba(6, 182, 212, 0.15), rgba(6, 182, 212, 0.05));
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--accent-primary);
  flex-shrink: 0;
}

.info-icon.role {
  background: linear-gradient(135deg, rgba(139, 92, 246, 0.15), rgba(139, 92, 246, 0.05));
  color: #8b5cf6;
}

.info-icon.warning {
  background: linear-gradient(135deg, rgba(239, 68, 68, 0.15), rgba(239, 68, 68, 0.05));
  color: var(--error);
}

.info-content {
  flex: 1;
}

.info-label {
  font-size: 0.8rem;
  color: var(--text-muted);
  margin-bottom: 4px;
}

.info-value {
  font-size: 1rem;
  font-weight: 600;
  color: var(--text-primary);
}

.info-value.text-error {
  color: var(--error);
}

/* Stats Card */
.stats-card {
  background: var(--bg-card);
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-lg);
  padding: 24px;
  margin-bottom: 24px;
}

.card-title {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 1rem;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 20px;
}

.card-title svg {
  color: var(--accent-primary);
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

.stat-item {
  text-align: center;
  padding: 20px 16px;
  background: var(--bg-secondary);
  border-radius: var(--radius-lg);
  transition: all var(--transition-fast);
}

.stat-item:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-lg);
}

.stat-icon {
  width: 44px;
  height: 44px;
  margin: 0 auto 12px;
  background: linear-gradient(135deg, rgba(6, 182, 212, 0.15), rgba(6, 182, 212, 0.05));
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--accent-primary);
}

.stat-icon.success {
  background: linear-gradient(135deg, rgba(16, 185, 129, 0.15), rgba(16, 185, 129, 0.05));
  color: var(--success);
}

.stat-icon.cancelled {
  background: linear-gradient(135deg, rgba(100, 116, 139, 0.15), rgba(100, 116, 139, 0.05));
  color: var(--text-muted);
}

.stat-icon.danger {
  background: linear-gradient(135deg, rgba(239, 68, 68, 0.15), rgba(239, 68, 68, 0.05));
  color: var(--error);
}

.stat-value {
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--text-primary);
  margin-bottom: 4px;
}

.stat-value.text-error {
  color: var(--error);
}

.stat-label {
  font-size: 0.8rem;
  color: var(--text-muted);
}

/* Actions Card */
.actions-card {
  background: var(--bg-card);
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-lg);
  padding: 24px;
}

.actions-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.action-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px 18px;
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.action-item:hover {
  background: var(--bg-hover);
}

.action-item:hover .action-arrow {
  transform: translateX(4px);
  color: var(--accent-primary);
}

.action-item.danger:hover {
  background: var(--error-bg);
}

.action-item.danger:hover .action-icon {
  background: var(--error-bg);
  color: var(--error);
}

.action-item.danger:hover .action-text {
  color: var(--error);
}

.action-icon {
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, rgba(6, 182, 212, 0.15), rgba(6, 182, 212, 0.05));
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--accent-primary);
  flex-shrink: 0;
}

.action-text {
  flex: 1;
  font-size: 0.95rem;
  font-weight: 500;
  color: var(--text-primary);
}

.action-arrow {
  color: var(--text-muted);
  transition: all var(--transition-fast);
}

/* Responsive */
@media (max-width: 640px) {
  .info-grid {
    grid-template-columns: 1fr;
  }
  
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>
