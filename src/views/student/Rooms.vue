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
          自习室列表
        </h2>
        <p class="page-desc">浏览并选择自习室进行预约</p>
      </div>
      <div class="header-actions">
        <a-input-search
          v-model:value="searchText"
          placeholder="搜索自习室..."
          style="width: 280px"
          allow-clear
          class="search-input"
          @search="onSearch"
        />
      </div>
    </div>

    <!-- Rooms Grid -->
    <div class="rooms-grid" v-if="filteredRooms.length > 0">
      <div 
        class="room-card" 
        v-for="(room, index) in filteredRooms" 
        :key="room.id"
        :style="{ animationDelay: `${index * 0.05}s` }"
        @click="goToReserve(room.id)"
      >
        <div class="room-header">
          <div class="room-icon">
            <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/>
              <polyline points="9 22 9 12 15 12 15 22"/>
            </svg>
          </div>
          <span class="room-status" :class="{ active: room.status === 1 }">
            <span class="status-dot"></span>
            {{ room.status === 1 ? '开放中' : '已关闭' }}
          </span>
        </div>
        
        <div class="room-body">
          <h3 class="room-name">{{ room.name }}</h3>
          <div class="room-location">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"/>
              <circle cx="12" cy="10" r="3"/>
            </svg>
            {{ room.building }} {{ room.floor }}层
          </div>
        </div>
        
        <div class="room-footer">
          <div class="room-stats">
            <div class="stat-item">
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <rect x="2" y="7" width="20" height="14" rx="2" ry="2"/>
                <path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16"/>
              </svg>
              <span>{{ room.totalSeats || 0 }}</span>
            </div>
            <div class="stat-item available" v-if="room.availableSeats !== undefined">
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <polyline points="20 6 9 17 4 12"/>
              </svg>
              <span>{{ room.availableSeats }}</span>
            </div>
          </div>
          <div class="room-action">
            <span>立即预约</span>
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M5 12h14M12 5l7 7-7 7"/>
            </svg>
          </div>
        </div>
        
        <!-- Hover Glow Effect -->
        <div class="card-glow"></div>
      </div>
    </div>

    <!-- Empty State -->
    <div class="empty-state" v-else>
      <div class="empty-icon">
        <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
          <circle cx="11" cy="11" r="8"/>
          <path d="m21 21-4.35-4.35"/>
          <path d="M8 11h6"/>
        </svg>
      </div>
      <h3 class="empty-title">{{ searchText ? '没有找到匹配的自习室' : '暂无自习室' }}</h3>
      <p class="empty-desc">{{ searchText ? '请尝试其他关键词搜索' : '暂无可预约的自习室' }}</p>
      <a-button v-if="searchText" type="primary" @click="searchText = ''">清除搜索</a-button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getRooms } from '@/api/room'

const router = useRouter()
const rooms = ref([])
const searchText = ref('')

const filteredRooms = computed(() => {
  if (!searchText.value) return rooms.value
  const keyword = searchText.value.toLowerCase()
  return rooms.value.filter(room => 
    room.name.toLowerCase().includes(keyword) ||
    room.building.toLowerCase().includes(keyword)
  )
})

const onSearch = (value) => {
  searchText.value = value
}

onMounted(async () => {
  try {
    const res = await getRooms()
    rooms.value = res.data || []
  } catch (e) {
    console.error('Failed to load rooms:', e)
  }
})

const goToReserve = (roomId) => {
  router.push(`/student/reserve/${roomId}`)
}
</script>

<style scoped>
.rooms-page {
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

.search-input :deep(.ant-input) {
  background: var(--bg-secondary) !important;
  border-color: var(--border-subtle) !important;
  color: var(--text-primary) !important;
  border-radius: var(--radius-md) !important;
}

.search-input :deep(.ant-input::placeholder) {
  color: var(--text-disabled) !important;
}

.search-input :deep(.ant-input-search-button) {
  background: var(--accent-primary) !important;
  border-color: var(--accent-primary) !important;
}

/* Rooms Grid */
.rooms-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 24px;
}

/* Room Card */
.room-card {
  position: relative;
  background: var(--bg-card);
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-lg);
  padding: 24px;
  cursor: pointer;
  transition: all var(--transition-normal);
  overflow: hidden;
  animation: slideUp 0.5s ease-out both;
}

@keyframes slideUp {
  from { 
    opacity: 0; 
    transform: translateY(20px); 
  }
  to { 
    opacity: 1; 
    transform: translateY(0); 
  }
}

.room-card:hover {
  transform: translateY(-6px);
  border-color: var(--accent-primary);
  box-shadow: var(--shadow-lg), 0 0 30px rgba(6, 182, 212, 0.15);
}

.room-card:hover .card-glow {
  opacity: 1;
}

.room-card:hover .room-action svg {
  transform: translateX(6px);
}

.card-glow {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
  background: linear-gradient(90deg, var(--accent-primary), var(--accent-secondary));
  opacity: 0;
  transition: opacity var(--transition-normal);
}

/* Room Header */
.room-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 20px;
}

.room-icon {
  width: 56px;
  height: 56px;
  background: linear-gradient(135deg, rgba(6, 182, 212, 0.15), rgba(6, 182, 212, 0.05));
  border-radius: var(--radius-lg);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--accent-primary);
}

.room-status {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 12px;
  background: var(--bg-elevated);
  border-radius: var(--radius-full);
  font-size: 0.8rem;
  font-weight: 500;
  color: var(--text-muted);
}

.status-dot {
  width: 6px;
  height: 6px;
  background: var(--text-muted);
  border-radius: 50%;
}

.room-status.active {
  background: var(--success-bg);
  color: var(--success);
}

.room-status.active .status-dot {
  background: var(--success);
  box-shadow: 0 0 8px var(--success);
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}

/* Room Body */
.room-body {
  margin-bottom: 20px;
}

.room-name {
  font-size: 1.15rem;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 8px;
}

.room-location {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 0.875rem;
  color: var(--text-muted);
}

.room-location svg {
  color: var(--text-disabled);
}

/* Room Footer */
.room-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 16px;
  border-top: 1px solid var(--border-subtle);
}

.room-stats {
  display: flex;
  gap: 16px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 0.85rem;
  color: var(--text-muted);
}

.stat-item svg {
  color: var(--text-disabled);
}

.stat-item.available {
  color: var(--success);
}

.stat-item.available svg {
  color: var(--success);
}

.room-action {
  display: flex;
  align-items: center;
  gap: 6px;
  color: var(--accent-primary);
  font-size: 0.875rem;
  font-weight: 600;
  transition: all var(--transition-fast);
}

.room-action svg {
  transition: transform var(--transition-fast);
}

/* Empty State */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80px 20px;
  background: var(--bg-card);
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-lg);
  text-align: center;
}

.empty-icon {
  color: var(--text-disabled);
  margin-bottom: 24px;
  opacity: 0.6;
}

.empty-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 8px;
}

.empty-desc {
  font-size: 0.9rem;
  color: var(--text-muted);
  margin: 0 0 24px;
}

/* Responsive */
@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    gap: 16px;
  }
  
  .header-actions {
    width: 100%;
  }
  
  .header-actions :deep(.ant-input-search) {
    width: 100% !important;
  }
  
  .rooms-grid {
    grid-template-columns: 1fr;
  }
}
</style>
