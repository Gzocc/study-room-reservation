<template>
  <div class="reservations-page">
    <!-- Page Header -->
    <div class="page-header">
      <div class="header-content">
        <h2 class="page-title">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <rect x="3" y="4" width="18" height="18" rx="2" ry="2"/>
            <line x1="16" y1="2" x2="16" y2="6"/>
            <line x1="8" y1="2" x2="8" y2="6"/>
            <line x1="3" y1="10" x2="21" y2="10"/>
          </svg>
          预约管理
        </h2>
        <p class="page-desc">管理所有学生的预约记录</p>
      </div>
    </div>

    <!-- Filters Card -->
    <div class="filters-card">
      <div class="filters-row">
        <div class="filter-group">
          <label class="filter-label">教室</label>
          <a-select v-model:value="roomId" style="width: 180px" allowClear placeholder="全部教室">
            <a-select-option v-for="room in rooms" :key="room.id" :value="room.id">
              {{ room.name }}
            </a-select-option>
          </a-select>
        </div>
        <div class="filter-group">
          <label class="filter-label">状态</label>
          <a-select v-model:value="status" style="width: 140px" allowClear placeholder="全部状态">
            <a-select-option :value="0">
              <span class="status-option pending">待签到</span>
            </a-select-option>
            <a-select-option :value="1">
              <span class="status-option active">已签到</span>
            </a-select-option>
            <a-select-option :value="2">
              <span class="status-option done">已签退</span>
            </a-select-option>
            <a-select-option :value="3">
              <span class="status-option cancelled">已取消</span>
            </a-select-option>
            <a-select-option :value="4">
              <span class="status-option violation">违约</span>
            </a-select-option>
          </a-select>
        </div>
        <div class="filter-group">
          <label class="filter-label">日期</label>
          <a-date-picker v-model:value="date" format="YYYY-MM-DD" style="width: 150px" />
        </div>
        <a-button type="primary" @click="loadReservations">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <circle cx="11" cy="11" r="8"/>
            <path d="m21 21-4.35-4.35"/>
          </svg>
          查询
        </a-button>
      </div>
    </div>

    <!-- Table Card -->
    <div class="table-card">
      <a-table
        :columns="columns"
        :data-source="reservations"
        :loading="loading"
        :pagination="pagination"
        @change="handleTableChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'user'">
            <div class="user-cell">
              <div class="user-avatar">{{ record.userName?.charAt(0) || 'U' }}</div>
              <span>{{ record.userName }}</span>
            </div>
          </template>
          <template v-if="column.key === 'status'">
            <span class="status-badge" :class="statusClass(record.status)">
              {{ statusTexts[record.status] }}
            </span>
          </template>
          <template v-if="column.key === 'time'">
            <span class="time-cell">{{ record.startTime }} - {{ record.endTime }}</span>
          </template>
        </template>
      </a-table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { getAllReservations } from '@/api/reservation'
import { getRooms } from '@/api/room'

const roomId = ref(undefined)
const status = ref(undefined)
const date = ref(null)
const reservations = ref([])
const rooms = ref([])
const loading = ref(false)
const pagination = ref({
  current: 1,
  pageSize: 10,
  total: 0
})

const statusTexts = {
  0: '待签到',
  1: '已签到',
  2: '已签退',
  3: '已取消',
  4: '违约',
  5: '超时释放'
}

const statusClass = (s) => ({
  'status-pending': s === 0,
  'status-active': s === 1,
  'status-done': s === 2,
  'status-cancelled': s === 3,
  'status-violation': s === 4 || s === 5
})

const columns = [
  { title: '用户', key: 'user', width: 160 },
  { title: '教室', dataIndex: 'roomName', key: 'roomName', width: 140 },
  { title: '座位', dataIndex: 'seatNo', key: 'seatNo', width: 80 },
  { title: '日期', dataIndex: 'date', key: 'date', width: 120 },
  { title: '时间', key: 'time', width: 140 },
  { title: '状态', key: 'status', width: 100 }
]

const loadRooms = async () => {
  try {
    const res = await getRooms()
    rooms.value = res.data || []
  } catch (e) {
    // ignore
  }
}

const loadReservations = async () => {
  loading.value = true
  try {
    const res = await getAllReservations({
      roomId: roomId.value,
      status: status.value,
      date: date.value?.format('YYYY-MM-DD'),
      pageNum: pagination.value.current,
      pageSize: pagination.value.pageSize
    })
    reservations.value = res.data.records || []
    pagination.value.total = res.data.total || 0
  } catch (e) {
    message.error('加载失败')
  } finally {
    loading.value = false
  }
}

const handleTableChange = (pag) => {
  pagination.value.current = pag.current
  loadReservations()
}

onMounted(() => {
  loadRooms()
  loadReservations()
})
</script>

<style scoped>
.reservations-page {
  max-width: 1200px;
  margin: 0 auto;
  animation: fadeIn 0.5s ease-out;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

/* Page Header */
.page-header {
  margin-bottom: 24px;
  padding: 24px;
  background: var(--bg-card);
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-lg);
}

.header-content {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.page-title {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 1.35rem;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0;
}

.page-title svg {
  color: #f59e0b;
}

.page-desc {
  font-size: 0.9rem;
  color: var(--text-muted);
  margin: 0;
  padding-left: 36px;
}

/* Filters Card */
.filters-card {
  background: var(--bg-card);
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-lg);
  padding: 20px 24px;
  margin-bottom: 20px;
}

.filters-row {
  display: flex;
  align-items: flex-end;
  gap: 16px;
  flex-wrap: wrap;
}

.filter-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.filter-label {
  font-size: 0.85rem;
  font-weight: 500;
  color: var(--text-secondary);
}

/* Table Card */
.table-card {
  background: var(--bg-card);
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-lg);
  overflow: hidden;
}

/* User Cell */
.user-cell {
  display: flex;
  align-items: center;
  gap: 10px;
}

.user-avatar {
  width: 32px;
  height: 32px;
  background: linear-gradient(135deg, rgba(6, 182, 212, 0.2), rgba(6, 182, 212, 0.1));
  border-radius: var(--radius-sm);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.8rem;
  font-weight: 600;
  color: var(--accent-primary);
}

/* Time Cell */
.time-cell {
  font-size: 0.85rem;
  color: var(--text-secondary);
}

/* Status Badge */
.status-badge {
  display: inline-flex;
  align-items: center;
  padding: 4px 12px;
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

/* Status Option */
.status-option {
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 0.8rem;
}

.status-option.pending {
  background: var(--warning-bg);
  color: var(--warning);
}

.status-option.active {
  background: var(--success-bg);
  color: var(--success);
}

.status-option.done {
  background: var(--info-bg);
  color: var(--info);
}

.status-option.cancelled {
  background: var(--bg-elevated);
  color: var(--text-muted);
}

.status-option.violation {
  background: var(--error-bg);
  color: var(--error);
}
</style>
