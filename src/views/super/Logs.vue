<template>
  <div class="logs-page">
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
          操作日志
        </h2>
        <p class="page-desc">查看系统操作记录和审计日志</p>
      </div>
      <a-button @click="loadLogs">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <polyline points="23 4 23 10 17 10"/>
          <path d="M20.49 15a9 9 0 1 1-2.12-9.36L23 10"/>
        </svg>
        刷新
      </a-button>
    </div>

    <!-- Table Card -->
    <div class="table-card">
      <a-table
        :columns="columns"
        :data-source="logs"
        :loading="loading"
        :pagination="pagination"
        @change="handleTableChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'module'">
            <span class="module-badge" :class="moduleClass(record.module)">
              {{ record.module }}
            </span>
          </template>
          <template v-if="column.key === 'operation'">
            <span class="operation-text">{{ record.operation }}</span>
          </template>
          <template v-if="column.key === 'detail'">
            <a-tooltip :title="record.detail">
              <span class="detail-text">{{ record.detail || '-' }}</span>
            </a-tooltip>
          </template>
          <template v-if="column.key === 'ip'">
            <span class="ip-text">{{ record.ip || '-' }}</span>
          </template>
          <template v-if="column.key === 'time'">
            <span>{{ formatDate(record.createTime) }}</span>
          </template>
        </template>
      </a-table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { getOperationLogs } from '@/api/super'

const logs = ref([])
const loading = ref(false)
const pagination = ref({ current: 1, pageSize: 10, total: 0 })

const columns = [
  { title: 'ID', dataIndex: 'id', key: 'id', width: 70 },
  { title: '模块', dataIndex: 'module', key: 'module', width: 110 },
  { title: '操作', dataIndex: 'operation', key: 'operation', width: 120 },
  { title: '详情', dataIndex: 'detail', key: 'detail', ellipsis: true },
  { title: 'IP地址', dataIndex: 'ip', key: 'ip', width: 130 },
  { title: '时间', dataIndex: 'createTime', key: 'time', width: 170 }
]

const moduleClass = (module) => ({
  'module-user': module === '用户管理',
  'module-room': module === '教室管理',
  'module-reservation': module === '预约管理',
  'module-system': module === '系统配置',
  'module-auth': module === '登录' || module === '注册'
})

const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

const loadLogs = async () => {
  loading.value = true
  try {
    const res = await getOperationLogs({
      pageNum: pagination.value.current,
      pageSize: pagination.value.pageSize
    })
    logs.value = res.data.records || []
    pagination.value.total = res.data.total || 0
  } catch (e) {
    message.error('加载失败')
  } finally {
    loading.value = false
  }
}

const handleTableChange = (pag) => {
  pagination.value.current = pag.current
  loadLogs()
}

onMounted(() => {
  loadLogs()
})
</script>

<style scoped>
.logs-page {
  max-width: 1400px;
  margin: 0 auto;
  animation: fadeIn 0.5s ease-out;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

/* Page Header */
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
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
  color: #8b5cf6;
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

/* Module Badge */
.module-badge {
  display: inline-flex;
  align-items: center;
  padding: 4px 12px;
  border-radius: var(--radius-full);
  font-size: 0.75rem;
  font-weight: 500;
}

.module-user {
  background: rgba(59, 130, 246, 0.15);
  color: #3b82f6;
}

.module-room {
  background: var(--success-bg);
  color: var(--success);
}

.module-reservation {
  background: var(--warning-bg);
  color: var(--warning);
}

.module-system {
  background: rgba(139, 92, 246, 0.15);
  color: #8b5cf6;
}

.module-auth {
  background: var(--info-bg);
  color: var(--info);
}

/* Operation Text */
.operation-text {
  font-weight: 500;
  color: var(--text-primary);
}

/* Detail Text */
.detail-text {
  color: var(--text-muted);
  max-width: 300px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  display: block;
}

/* IP Text */
.ip-text {
  font-family: 'Fira Code', 'Consolas', monospace;
  color: var(--text-muted);
  font-size: 0.85rem;
}
</style>
