<template>
  <div class="reserve-page">
    <!-- Page Header -->
    <div class="page-header">
      <a-button type="text" @click="$router.back()" class="back-btn">
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M19 12H5M12 19l-7-7 7-7"/>
        </svg>
        返回
      </a-button>
      <div class="header-content">
        <h2 class="page-title">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <rect x="3" y="4" width="18" height="18" rx="2" ry="2"/>
            <line x1="16" y1="2" x2="16" y2="6"/>
            <line x1="8" y1="2" x2="8" y2="6"/>
            <line x1="3" y1="10" x2="21" y2="10"/>
          </svg>
          预约座位
        </h2>
        <p class="page-desc">选择时间并预约空闲座位</p>
      </div>
    </div>

    <!-- Time Selection -->
    <div class="card time-card">
      <h3 class="card-title">
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <circle cx="12" cy="12" r="10"/>
          <polyline points="12 6 12 12 16 14"/>
        </svg>
        选择预约时间
      </h3>
      <div class="time-form">
        <div class="form-item">
          <label class="form-label">日期</label>
          <a-date-picker 
            v-model:value="date" 
            format="YYYY-MM-DD" 
            :disabled-date="disabledDate"
            class="form-control"
          />
        </div>
        <div class="form-item">
          <label class="form-label">开始时间</label>
          <a-time-picker 
            v-model:value="startTime" 
            format="HH:mm" 
            minute-step="30"
            class="form-control"
          />
        </div>
        <div class="form-item">
          <label class="form-label">结束时间</label>
          <a-time-picker 
            v-model:value="endTime" 
            format="HH:mm" 
            minute-step="30"
            class="form-control"
          />
        </div>
        <div class="form-item form-action">
          <a-button 
            type="primary" 
            @click="loadSeats" 
            :disabled="!date || !startTime || !endTime"
            size="large"
          >
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="11" cy="11" r="8"/>
              <path d="m21 21-4.35-4.35"/>
            </svg>
            查询空位
          </a-button>
        </div>
      </div>
    </div>

    <!-- Seat Selection -->
    <div class="card seat-card" v-if="seats.length > 0 || querySubmitted">
      <div class="card-header">
        <h3 class="card-title">
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <rect x="2" y="7" width="20" height="14" rx="2" ry="2"/>
            <path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16"/>
          </svg>
          选择座位
        </h3>
        <div class="legend">
          <div class="legend-item">
            <span class="legend-dot available"></span>
            <span>可预约</span>
          </div>
          <div class="legend-item">
            <span class="legend-dot occupied"></span>
            <span>已预约</span>
          </div>
          <div class="legend-item">
            <span class="legend-dot selected"></span>
            <span>已选中</span>
          </div>
        </div>
      </div>

      <!-- Seat Type Legend -->
      <div class="seat-type-legend">
        <div class="type-item">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <rect x="4" y="2" width="16" height="20" rx="2"/>
            <line x1="8" y1="6" x2="16" y2="6"/>
            <line x1="8" y1="10" x2="16" y2="10"/>
          </svg>
          <span class="type-tag normal">普通</span>
        </div>
        <div class="type-item">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <rect x="3" y="3" width="18" height="18" rx="2"/>
            <line x1="3" y1="9" x2="21" y2="9"/>
            <line x1="9" y1="3" x2="9" y2="21"/>
          </svg>
          <span class="type-tag window">靠窗</span>
        </div>
        <div class="type-item">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M13 2L3 14h9l-1 8 10-12h-9l1-8z"/>
          </svg>
          <span class="type-tag power">有电源</span>
        </div>
      </div>

      <div class="seat-map" v-if="seats.length > 0">
        <div class="seat-grid">
          <div class="seat-row" v-for="row in seatRows" :key="row">
            <div
              v-for="seat in getRowSeats(row)"
              :key="seat.id"
              :class="['seat', getSeatClass(seat)]"
              @click="selectSeat(seat)"
              :title="getSeatTypeText(seat.type)"
            >
              <div class="seat-icon">
                <!-- 普通座位 -->
                <svg v-if="seat.type === 0" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <rect x="4" y="2" width="16" height="20" rx="2"/>
                  <line x1="8" y1="6" x2="16" y2="6"/>
                  <line x1="8" y1="10" x2="16" y2="10"/>
                </svg>
                <!-- 靠窗座位 -->
                <svg v-else-if="seat.type === 1" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <rect x="3" y="3" width="18" height="18" rx="2"/>
                  <line x1="3" y1="9" x2="21" y2="9"/>
                  <line x1="9" y1="3" x2="9" y2="21"/>
                </svg>
                <!-- 有电源座位 -->
                <svg v-else-if="seat.type === 2" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M13 2L3 14h9l-1 8 10-12h-9l1-8z"/>
                </svg>
                <!-- 默认普通座位 -->
                <svg v-else width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <rect x="4" y="2" width="16" height="20" rx="2"/>
                  <line x1="8" y1="6" x2="16" y2="6"/>
                  <line x1="8" y1="10" x2="16" y2="10"/>
                </svg>
              </div>
              <div class="seat-number">{{ seat.seatNo }}</div>
            </div>
          </div>
        </div>
      </div>

      <div class="empty-state" v-else>
        <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
          <rect x="2" y="7" width="20" height="14" rx="2" ry="2"/>
          <path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16"/>
        </svg>
        <p>该时段暂无空位</p>
      </div>
    </div>

    <!-- Reservation Summary -->
    <div class="card summary-card" v-if="selectedSeat">
      <h3 class="card-title">
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
          <polyline points="14 2 14 8 20 8"/>
        </svg>
        预约信息
      </h3>
      <div class="summary-grid">
        <div class="summary-item">
          <span class="summary-label">座位</span>
          <span class="summary-value highlight">{{ selectedSeat.seatNo }}</span>
        </div>
        <div class="summary-item">
          <span class="summary-label">类型</span>
          <span class="summary-value">
            <span class="type-tag" :class="getSeatTypeClass(selectedSeat.type)">
              {{ getSeatTypeText(selectedSeat.type) }}
            </span>
          </span>
        </div>
        <div class="summary-item">
          <span class="summary-label">日期</span>
          <span class="summary-value">{{ date?.format('YYYY-MM-DD') }}</span>
        </div>
        <div class="summary-item">
          <span class="summary-label">时间</span>
          <span class="summary-value highlight">{{ startTime?.format('HH:mm') }} - {{ endTime?.format('HH:mm') }}</span>
        </div>
      </div>
      <a-button 
        type="primary" 
        size="large" 
        @click="handleReserve" 
        :loading="loading"
        block
        class="confirm-btn"
      >
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/>
          <polyline points="22 4 12 14.01 9 11.01"/>
        </svg>
        确认预约
      </a-button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { getSeatsByRoom } from '@/api/seat'
import { createReservation } from '@/api/reservation'
import dayjs from 'dayjs'

const route = useRoute()
const router = useRouter()
const roomId = route.params.roomId

const date = ref(dayjs())
const startTime = ref(null)
const endTime = ref(null)
const seats = ref([])
const selectedSeat = ref(null)
const loading = ref(false)
const querySubmitted = ref(false)

const seatTypeMap = {
  0: { text: '普通', class: 'normal' },
  1: { text: '靠窗', class: 'window' },
  2: { text: '有电源', class: 'power' }
}

const getSeatTypeText = (type) => seatTypeMap[type]?.text || '普通'
const getSeatTypeClass = (type) => seatTypeMap[type]?.class || 'normal'

const seatRows = computed(() => {
  const rows = [...new Set(seats.value.map(s => s.rowNum))]
  return rows.sort((a, b) => a - b)
})

const getRowSeats = (row) => {
  return seats.value
    .filter(s => s.rowNum === row)
    .sort((a, b) => a.colNum - b.colNum)
}

const disabledDate = (current) => {
  return current && current < dayjs().startOf('day')
}

const loadSeats = async () => {
  if (!date.value || !startTime.value || !endTime.value) {
    message.warning('请选择完整的时间')
    return
  }

  querySubmitted.value = true
  try {
    const res = await getSeatsByRoom(
      roomId,
      date.value.format('YYYY-MM-DD'),
      startTime.value.format('HH:mm'),
      endTime.value.format('HH:mm')
    )
    seats.value = res.data || []
    selectedSeat.value = null
    if (seats.value.length === 0) {
      message.info('该时段暂无空位')
    }
  } catch (e) {
    message.error('加载座位失败')
  }
}

const getSeatClass = (seat) => {
  if (selectedSeat.value?.id === seat.id) return 'selected'
  if (seat.reserveStatus === 1) return 'occupied'
  return 'available'
}

const selectSeat = (seat) => {
  if (seat.reserveStatus === 1) {
    message.warning('该座位已被预约')
    return
  }
  selectedSeat.value = seat
}

const handleReserve = async () => {
  loading.value = true
  try {
    await createReservation({
      seatId: selectedSeat.value.id,
      date: date.value.format('YYYY-MM-DD'),
      startTime: startTime.value.format('HH:mm'),
      endTime: endTime.value.format('HH:mm')
    })
    message.success('预约成功')
    router.push('/student/records')
  } catch (e) {
    // 错误已在拦截器处理
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.reserve-page {
  max-width: 900px;
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

.back-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  color: var(--text-secondary);
  margin-bottom: 12px;
  padding: 0;
  font-size: 0.875rem;
}

.back-btn:hover {
  color: var(--accent-primary);
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

/* Card Styles */
.card {
  background: var(--bg-card);
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-lg);
  padding: 24px;
  margin-bottom: 20px;
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

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  flex-wrap: wrap;
  gap: 16px;
}

.card-header .card-title {
  margin: 0;
}

/* Time Form */
.time-form {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  align-items: flex-end;
}

.form-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-label {
  font-size: 0.875rem;
  font-weight: 500;
  color: var(--text-secondary);
}

.form-control {
  width: 100%;
}

.form-control :deep(.ant-picker),
.form-control :deep(.ant-picker-input > input) {
  background: var(--bg-secondary) !important;
  border-color: var(--border-subtle) !important;
  color: var(--text-primary) !important;
}

.form-action {
  display: flex;
  align-items: flex-end;
}

/* Legend */
.legend {
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.85rem;
  color: var(--text-secondary);
}

.legend-dot {
  width: 14px;
  height: 14px;
  border-radius: 4px;
}

.legend-dot.available {
  background: var(--success-bg);
  border: 1px solid var(--success);
}

.legend-dot.occupied {
  background: var(--error-bg);
  border: 1px solid var(--error);
}

.legend-dot.selected {
  background: var(--accent-glow);
  border: 2px solid var(--accent-primary);
}

/* Seat Type Legend */
.seat-type-legend {
  display: flex;
  gap: 16px;
  margin-bottom: 20px;
  padding: 14px 18px;
  background: var(--bg-secondary);
  border-radius: var(--radius-md);
}

.type-item {
  display: flex;
  align-items: center;
  gap: 6px;
}

.type-tag {
  display: inline-flex;
  padding: 4px 10px;
  border-radius: var(--radius-full);
  font-size: 0.75rem;
  font-weight: 500;
}

.type-tag.normal {
  background: var(--success-bg);
  color: var(--success);
}

.type-tag.window {
  background: var(--info-bg);
  color: var(--info);
}

.type-tag.power {
  background: var(--warning-bg);
  color: var(--warning);
}

/* Seat Map */
.seat-map {
  background: var(--bg-secondary);
  border-radius: var(--radius-lg);
  padding: 28px;
}

.seat-grid {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.seat-row {
  display: flex;
  gap: 10px;
  justify-content: center;
}

.seat {
  width: 72px;
  height: 68px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 4px;
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: all var(--transition-fast);
  border: 2px solid var(--border-primary);
  background: var(--bg-card);
}

.seat:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
}

.seat-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 28px;
  height: 28px;
  border-radius: var(--radius-sm);
  background: var(--bg-elevated);
  color: var(--text-secondary);
}

.seat-number {
  font-size: 0.75rem;
  font-weight: 600;
  color: var(--text-primary);
}

.seat.available {
  background: var(--success-light);
  border-color: var(--success);
}

.seat.available .seat-icon {
  background: var(--success-bg);
  color: var(--success);
}

.seat.available:hover {
  background: var(--success-bg);
  border-color: var(--success);
  box-shadow: 0 4px 12px rgba(16, 185, 129, 0.2);
}

.seat.occupied {
  background: var(--error-light);
  border-color: var(--error);
  cursor: not-allowed;
  opacity: 0.7;
}

.seat.occupied .seat-icon {
  background: var(--error-bg);
  color: var(--error);
}

.seat.selected {
  background: var(--accent-light);
  border-color: var(--accent-primary);
  box-shadow: 0 4px 16px rgba(6, 182, 212, 0.25);
}

.seat.selected .seat-icon {
  background: var(--accent-lighter);
  color: var(--accent-primary);
}

/* Empty State */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  color: var(--text-muted);
  text-align: center;
}

.empty-state svg {
  margin-bottom: 16px;
  opacity: 0.5;
}

/* Summary */
.summary-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-bottom: 24px;
}

.summary-item {
  padding: 16px;
  background: var(--bg-secondary);
  border-radius: var(--radius-md);
  text-align: center;
}

.summary-label {
  display: block;
  font-size: 0.8rem;
  color: var(--text-muted);
  margin-bottom: 8px;
}

.summary-value {
  font-size: 1rem;
  font-weight: 600;
  color: var(--text-primary);
}

.summary-value.highlight {
  color: var(--accent-primary);
}

.confirm-btn {
  height: 52px !important;
  font-size: 1rem !important;
  font-weight: 600 !important;
}

/* Responsive */
@media (max-width: 768px) {
  .time-form {
    grid-template-columns: 1fr;
  }
  
  .summary-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .card-header {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .seat-type-legend {
    flex-wrap: wrap;
  }
  
  .seat {
    width: 60px;
    height: 50px;
  }
}
</style>
