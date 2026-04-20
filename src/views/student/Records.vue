<template>
  <div class="records-page">
    <!-- Page Header -->
    <div class="page-header">
      <div class="header-content">
        <h2 class="page-title">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
            <polyline points="14 2 14 8 20 8"/>
            <line x1="16" y1="13" x2="8" y2="13"/>
            <line x1="16" y1="17" x2="8" y2="17"/>
          </svg>
          预约记录
        </h2>
        <p class="page-desc">查看和管理您的预约</p>
      </div>
    </div>

    <!-- Filters -->
    <div class="filters-card">
      <div class="filters-row">
        <div class="filter-group">
          <label class="filter-label">状态筛选</label>
          <a-select 
            v-model:value="filters.status" 
            style="width: 160px" 
            allowClear
            placeholder="全部状态"
            class="filter-select"
          >
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
        <a-button type="primary" @click="loadRecords">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <circle cx="11" cy="11" r="8"/>
            <path d="m21 21-4.35-4.35"/>
          </svg>
          查询
        </a-button>
      </div>
    </div>

    <!-- Records Table -->
    <div class="table-card">
      <a-table
        :columns="columns"
        :data-source="records"
        :pagination="pagination"
        :loading="loading"
        @change="handleTableChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'room'">
            <div class="room-cell">
              <div class="room-icon-wrapper">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/>
                  <polyline points="9 22 9 12 15 12 15 22"/>
                </svg>
              </div>
              <span>{{ record.roomName }}</span>
            </div>
          </template>
          <template v-if="column.key === 'seat'">
            <span class="seat-badge">{{ record.seatNo }}</span>
          </template>
          <template v-if="column.key === 'time'">
            <span class="time-range">
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <circle cx="12" cy="12" r="10"/>
                <polyline points="12 6 12 12 16 14"/>
              </svg>
              {{ record.startTime }} - {{ record.endTime }}
            </span>
          </template>
          <template v-if="column.key === 'status'">
            <span class="status-badge" :class="statusClass(record.status)">
              {{ statusTexts[record.status] }}
            </span>
          </template>
          <template v-if="column.key === 'action'">
            <a-space>
              <a-button
                v-if="record.status === 0"
                type="primary"
                size="small"
                @click="handleCheckin(record)"
              >
                签到
              </a-button>
              <a-button
                v-if="record.status === 1"
                type="primary"
                size="small"
                @click="handleCheckout(record)"
              >
                签退
              </a-button>
              <a-popconfirm
                v-if="record.status === 0"
                title="确定取消预约吗？"
                @confirm="handleCancel(record)"
              >
                <a-button danger size="small">取消</a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { getMyReservations, cancelReservation, checkin, checkout } from '@/api/reservation'

const loading = ref(false)
const records = ref([])

const filters = reactive({
  status: undefined
})

const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0
})

const statusTexts = {
  0: '待签到',
  1: '已签到',
  2: '已签退',
  3: '已取消',
  4: '违约'
}

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

const columns = [
  { title: '教室', key: 'room', dataIndex: 'roomName' },
  { title: '座位', key: 'seat', dataIndex: 'seatNo', width: 80 },
  { title: '日期', dataIndex: 'date', key: 'date', width: 100 },
  { title: '时间', key: 'time', width: 160 },
  { title: '状态', key: 'status', width: 100 },
  { title: '操作', key: 'action', width: 180 }
]

const loadRecords = async () => {
  loading.value = true
  try {
    const res = await getMyReservations({
      status: filters.status,
      pageNum: pagination.current,
      pageSize: pagination.pageSize
    })
    records.value = res.data.records || []
    pagination.total = res.data.total || 0
  } catch (e) {
    message.error('加载失败')
  } finally {
    loading.value = false
  }
}

const handleTableChange = (pag) => {
  pagination.current = pag.current
  loadRecords()
}

const handleCheckin = async (record) => {
  try {
    await checkin(record.id)
    message.success('签到成功')
    loadRecords()
  } catch (e) {}
}

const handleCheckout = async (record) => {
  try {
    await checkout(record.id)
    message.success('签退成功')
    loadRecords()
  } catch (e) {}
}

const handleCancel = async (record) => {
  try {
    await cancelReservation(record.id)
    message.success('取消成功')
    loadRecords()
  } catch (e) {}
}

onMounted(() => {
  loadRecords()
})
</script>

<style scoped>
.records-page {
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
  margin-bottom: 28px;
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
  color: var(--accent-primary);
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

.filter-select :deep(.ant-select-selector) {
  background: var(--bg-secondary) !important;
  border-color: var(--border-subtle) !important;
  color: var(--text-primary) !important;
}

/* Table Card */
.table-card {
  background: var(--bg-card);
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-lg);
  overflow: hidden;
}

/* Cell Styles */
.room-cell {
  display: flex;
  align-items: center;
  gap: 10px;
}

.room-icon-wrapper {
  width: 32px;
  height: 32px;
  background: linear-gradient(135deg, rgba(6, 182, 212, 0.15), rgba(6, 182, 212, 0.05));
  border-radius: var(--radius-sm);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--accent-primary);
}

.seat-badge {
  display: inline-flex;
  align-items: center;
  padding: 4px 10px;
  background: var(--bg-elevated);
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-sm);
  font-size: 0.8rem;
  font-weight: 600;
  color: var(--text-primary);
}

.time-range {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 0.85rem;
  color: var(--text-secondary);
}

.time-range svg {
  color: var(--accent-primary);
}

/* Status Badges */
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

/* Status Option Styles */
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
