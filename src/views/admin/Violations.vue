<template>
  <div class="violations-page">
    <!-- Page Header -->
    <div class="page-header">
      <div class="header-content">
        <h2 class="page-title">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"/>
            <line x1="12" y1="9" x2="12" y2="13"/>
            <line x1="12" y1="17" x2="12.01" y2="17"/>
          </svg>
          违约记录
        </h2>
        <p class="page-desc">管理学生的违约记录和处理情况</p>
      </div>
    </div>

    <!-- Table Card -->
    <div class="table-card">
      <a-table
        :columns="columns"
        :data-source="violations"
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
          <template v-if="column.key === 'reason'">
            <span class="reason-badge" :class="reasonClass(record.reason)">
              {{ reasonTexts[record.reason] }}
            </span>
          </template>
          <template v-if="column.key === 'time'">
            <span class="time-cell">{{ formatTime(record.createTime) }}</span>
          </template>
        </template>
      </a-table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import request from '@/utils/request'

const violations = ref([])
const loading = ref(false)
const pagination = ref({
  current: 1,
  pageSize: 10,
  total: 0
})

const reasonTexts = {
  1: '超时未签到',
  2: '提前离开',
  3: '恶意占座'
}

const reasonClass = (reason) => ({
  'reason-warning': reason === 1,
  'reason-error': reason === 2 || reason === 3
})

const formatTime = (time) => {
  if (!time) return '-'
  const date = new Date(time)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const columns = [
  { title: '用户', key: 'user', width: 160 },
  { title: '原因', dataIndex: 'reason', key: 'reason', width: 120 },
  { title: '说明', dataIndex: 'description', key: 'description' },
  { title: '时间', key: 'time', width: 160 }
]

const loadViolations = async () => {
  loading.value = true
  try {
    const res = await request.get('/admin/violations', {
      params: {
        pageNum: pagination.value.current,
        pageSize: pagination.value.pageSize
      }
    })
    violations.value = res.data.records || []
    pagination.value.total = res.data.total || 0
  } catch (e) {
    message.error('加载失败')
  } finally {
    loading.value = false
  }
}

const handleTableChange = (pag) => {
  pagination.value.current = pag.current
  loadViolations()
}

onMounted(() => {
  loadViolations()
})
</script>

<style scoped>
.violations-page {
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
  color: var(--error);
}

.page-desc {
  font-size: 0.9rem;
  color: var(--text-muted);
  margin: 0;
  padding-left: 36px;
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
  background: linear-gradient(135deg, rgba(239, 68, 68, 0.2), rgba(239, 68, 68, 0.1));
  border-radius: var(--radius-sm);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.8rem;
  font-weight: 600;
  color: var(--error);
}

/* Reason Badge */
.reason-badge {
  display: inline-flex;
  align-items: center;
  padding: 4px 12px;
  border-radius: var(--radius-full);
  font-size: 0.75rem;
  font-weight: 500;
}

.reason-warning {
  background: var(--warning-bg);
  color: var(--warning);
}

.reason-error {
  background: var(--error-bg);
  color: var(--error);
}

/* Time Cell */
.time-cell {
  font-size: 0.85rem;
  color: var(--text-secondary);
}
</style>
