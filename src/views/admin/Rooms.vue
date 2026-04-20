<template>
  <div class="rooms-page">
    <!-- Page Header -->
    <div class="page-header">
      <div class="header-content">
        <h2 class="page-title">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/>
            <polyline points="9 22 9 12 15 12 15 22"/>
          </svg>
          教室管理
        </h2>
        <p class="page-desc">管理系统中的自习室信息</p>
      </div>
      <a-button type="primary" size="large" @click="showAddModal">
        <template #icon>
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <line x1="12" y1="5" x2="12" y2="19"/>
            <line x1="5" y1="12" x2="19" y2="12"/>
          </svg>
        </template>
        添加教室
      </a-button>
    </div>

    <!-- Table Card -->
    <div class="table-card">
      <a-table
        :columns="columns"
        :data-source="rooms"
        :loading="loading"
        :pagination="pagination"
        @change="handleTableChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'status'">
            <span class="status-badge" :class="record.status === 1 ? 'active' : 'inactive'">
              <span class="status-dot"></span>
              {{ record.status === 1 ? '开放' : '关闭' }}
            </span>
          </template>
          <template v-if="column.key === 'action'">
            <a-space>
              <a-button type="link" size="small" @click="goToSeats(record)">
                座位管理
              </a-button>
              <a-switch
                :checked="record.status === 1"
                @change="(checked) => toggleStatus(record, checked)"
                size="small"
              />
              <a-popconfirm title="确定删除吗？" @confirm="handleDelete(record)">
                <a-button type="link" danger size="small">删除</a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </div>

    <!-- Add Modal -->
    <a-modal v-model:open="modalVisible" title="添加教室" :width="480" centered @ok="handleAdd">
      <a-form :model="form" layout="vertical" class="modal-form">
        <a-form-item label="教室名称" required>
          <a-input v-model:value="form.name" placeholder="如：图书馆301" />
        </a-form-item>
        <a-form-item label="楼栋" required>
          <a-input v-model:value="form.building" placeholder="如：图书馆" />
        </a-form-item>
        <a-form-item label="楼层" required>
          <a-input-number v-model:value="form.floor" :min="1" style="width: 100%" />
        </a-form-item>
        <a-form-item label="行数" required>
          <a-input-number v-model:value="form.rows" :min="1" :max="20" style="width: 100%" />
        </a-form-item>
        <a-form-item label="列数" required>
          <a-input-number v-model:value="form.cols" :min="1" :max="20" style="width: 100%" />
        </a-form-item>
        <a-form-item label="描述">
          <a-textarea v-model:value="form.description" :rows="3" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { pageRooms, addRoom, changeRoomStatus, deleteRoom } from '@/api/room'

const router = useRouter()
const loading = ref(false)
const rooms = ref([])
const modalVisible = ref(false)
const form = reactive({
  name: '',
  building: '',
  floor: 1,
  rows: 5,
  cols: 8,
  description: ''
})

const pagination = ref({
  current: 1,
  pageSize: 10,
  total: 0
})

const columns = [
  { title: '教室名称', dataIndex: 'name', key: 'name' },
  { title: '楼栋', dataIndex: 'building', key: 'building' },
  { title: '楼层', dataIndex: 'floor', key: 'floor', width: 80 },
  { title: '座位数', dataIndex: 'totalSeats', key: 'totalSeats', width: 100 },
  { title: '状态', dataIndex: 'status', key: 'status', width: 100 },
  { title: '操作', key: 'action', width: 280 }
]

const loadRooms = async () => {
  loading.value = true
  try {
    const res = await pageRooms({
      pageNum: pagination.value.current,
      pageSize: pagination.value.pageSize
    })
    rooms.value = res.data.records || []
    pagination.value.total = res.data.total || 0
  } catch (e) {
    message.error('加载失败')
  } finally {
    loading.value = false
  }
}

const handleTableChange = (pag) => {
  pagination.value.current = pag.current
  loadRooms()
}

const showAddModal = () => {
  Object.assign(form, { name: '', building: '', floor: 1, rows: 5, cols: 8, description: '' })
  modalVisible.value = true
}

const handleAdd = async () => {
  if (!form.name || !form.building) {
    message.warning('请填写必填项')
    return
  }
  try {
    await addRoom(form)
    message.success('添加成功')
    modalVisible.value = false
    loadRooms()
  } catch (e) {
    // 错误已在拦截器处理
  }
}

const toggleStatus = async (record, checked) => {
  try {
    await changeRoomStatus(record.id, checked ? 1 : 0)
    message.success('修改成功')
    loadRooms()
  } catch (e) {
    // 错误已在拦截器处理
  }
}

const handleDelete = async (record) => {
  try {
    await deleteRoom(record.id)
    message.success('删除成功')
    loadRooms()
  } catch (e) {
    // 错误已在拦截器处理
  }
}

const goToSeats = (record) => {
  router.push(`/admin/seats/${record.id}`)
}

onMounted(() => {
  loadRooms()
})
</script>

<style scoped>
.rooms-page {
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
  color: #f59e0b;
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

/* Status Badge */
.status-badge {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 4px 12px;
  border-radius: var(--radius-full);
  font-size: 0.8rem;
  font-weight: 500;
}

.status-badge.active {
  background: var(--success-bg);
  color: var(--success);
}

.status-badge.inactive {
  background: var(--bg-elevated);
  color: var(--text-muted);
}

.status-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: currentColor;
}

.status-badge.active .status-dot {
  box-shadow: 0 0 6px var(--success);
}

/* Modal Form */
.modal-form :deep(.ant-form-item-label > label) {
  color: var(--text-secondary);
}
</style>
