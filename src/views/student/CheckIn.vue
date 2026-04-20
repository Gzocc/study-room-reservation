<template>
  <div class="checkin-page">
    <!-- Page Header -->
    <div class="page-header">
      <div class="header-content">
        <h2 class="page-title">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/>
            <polyline points="22 4 12 14.01 9 11.01"/>
          </svg>
          签到签退
        </h2>
        <p class="page-desc">选择教室查看座位并进行签到</p>
      </div>
    </div>

    <!-- Room Selection -->
    <div class="rooms-section">
      <h3 class="section-title">
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/>
          <polyline points="9 22 9 12 15 12 15 22"/>
        </svg>
        选择自习室
      </h3>
      <div class="rooms-grid">
        <div 
          class="room-card" 
          v-for="room in rooms" 
          :key="room.id"
          :class="{ selected: selectedRoom?.id === room.id }"
          @click="selectRoom(room)"
        >
          <div class="room-icon">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/>
              <polyline points="9 22 9 12 15 12 15 22"/>
            </svg>
          </div>
          <div class="room-info">
            <div class="room-name">{{ room.name }}</div>
            <div class="room-location">{{ room.building }} {{ room.floor }}层</div>
          </div>
          <div class="room-status" :class="{ active: room.status === 1 }">
            <span class="status-dot"></span>
          </div>
        </div>
      </div>
    </div>

    <!-- Seat Map -->
    <div class="seat-section" v-if="selectedRoom">
      <div class="section-header">
        <h3 class="section-title">
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <rect x="2" y="7" width="20" height="14" rx="2" ry="2"/>
            <path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16"/>
          </svg>
          {{ selectedRoom.name }} - 座位图
        </h3>
        <div class="legend">
          <div class="legend-item">
            <span class="legend-dot available"></span>
            <span>空闲</span>
          </div>
          <div class="legend-item">
            <span class="legend-dot reserved"></span>
            <span>已预约</span>
          </div>
          <div class="legend-item">
            <span class="legend-dot mine"></span>
            <span>我的预约</span>
          </div>
          <div class="legend-item">
            <span class="legend-dot checked-in"></span>
            <span>已签到</span>
          </div>
        </div>
      </div>

      <div class="seat-map">
        <div class="seat-grid" v-if="seats.length > 0">
          <div class="seat-row" v-for="row in seatRows" :key="row">
            <div
              v-for="seat in getRowSeats(row)"
              :key="seat.id"
              :class="['seat', getSeatClass(seat)]"
              @click="handleSeatClick(seat)"
              :title="seat.type === 0 ? '普通' : seat.type === 1 ? '靠窗' : '有电源'"
            >
              <div class="seat-icon">
                <!-- 普通座位 -->
                <svg v-if="seat.type === 0" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <rect x="4" y="2" width="16" height="20" rx="2"/>
                  <line x1="8" y1="6" x2="16" y2="6"/>
                  <line x1="8" y1="10" x2="16" y2="10"/>
                </svg>
                <!-- 靠窗座位 -->
                <svg v-else-if="seat.type === 1" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <rect x="3" y="3" width="18" height="18" rx="2"/>
                  <line x1="3" y1="9" x2="21" y2="9"/>
                  <line x1="9" y1="3" x2="9" y2="21"/>
                </svg>
                <!-- 有电源座位 -->
                <svg v-else-if="seat.type === 2" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M13 2L3 14h9l-1 8 10-12h-9l1-8z"/>
                </svg>
                <!-- 默认普通座位 -->
                <svg v-else width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <rect x="4" y="2" width="16" height="20" rx="2"/>
                  <line x1="8" y1="6" x2="16" y2="6"/>
                  <line x1="8" y1="10" x2="16" y2="10"/>
                </svg>
              </div>
              <span class="seat-no">{{ seat.seatNo }}</span>
              <svg v-if="seat.myReservation?.status === 1" class="seat-check" width="10" height="10" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3">
                <polyline points="20 6 9 17 4 12"/>
              </svg>
            </div>
          </div>
        </div>
        <div class="empty-seats" v-else>
          <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
            <rect x="2" y="7" width="20" height="14" rx="2" ry="2"/>
            <path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16"/>
          </svg>
          <p>该教室暂无座位</p>
        </div>
      </div>
    </div>

    <!-- Today's Reservations -->
    <div class="reservations-section">
      <h3 class="section-title">
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <rect x="3" y="4" width="18" height="18" rx="2" ry="2"/>
          <line x1="16" y1="2" x2="16" y2="6"/>
          <line x1="8" y1="2" x2="8" y2="6"/>
          <line x1="3" y1="10" x2="21" y2="10"/>
        </svg>
        今日我的预约
      </h3>
      <div class="reservations-list" v-if="myReservations.length > 0">
        <div 
          class="reservation-card" 
          v-for="r in myReservations" 
          :key="r.id"
        >
          <div class="reservation-time">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="12" cy="12" r="10"/>
              <polyline points="12 6 12 12 16 14"/>
            </svg>
            <span class="time-value">{{ r.startTime }} - {{ r.endTime }}</span>
          </div>
          <div class="reservation-info">
            <div class="info-room">
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/>
                <polyline points="9 22 9 12 15 12 15 22"/>
              </svg>
              {{ r.roomName }}
            </div>
            <div class="info-seat">座位 {{ r.seatNo }}</div>
          </div>
          <div class="reservation-action">
            <span class="status-badge" :class="statusClass(r.status)">
              {{ statusTexts[r.status] }}
            </span>
            <a-button 
              v-if="r.status === 0" 
              type="primary" 
              size="small"
              @click="handleCheckinById(r)"
            >
              签到
            </a-button>
            <a-button 
              v-if="r.status === 1" 
              type="primary" 
              size="small"
              @click="handleCheckoutById(r)"
            >
              签退
            </a-button>
          </div>
        </div>
      </div>
      <div class="empty-reservations" v-else>
        <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
          <rect x="3" y="4" width="18" height="18" rx="2" ry="2"/>
          <line x1="16" y1="2" x2="16" y2="6"/>
          <line x1="8" y1="2" x2="8" y2="6"/>
          <line x1="3" y1="10" x2="21" y2="10"/>
        </svg>
        <p>今日暂无预约</p>
        <a-button type="primary" size="small" @click="$router.push('/student/rooms')">去预约</a-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { getRooms } from '@/api/room'
import { getSeatsByRoom } from '@/api/seat'
import { getMyReservations, checkin, checkout } from '@/api/reservation'

const rooms = ref([])
const selectedRoom = ref(null)
const seats = ref([])
const myReservations = ref([])

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

const seatRows = computed(() => {
  const rows = [...new Set(seats.value.map(s => s.rowNum))]
  return rows.sort((a, b) => a - b)
})

const getRowSeats = (row) => {
  return seats.value.filter(s => s.rowNum === row).sort((a, b) => a.colNum - b.colNum)
}

const getSeatClass = (seat) => {
  if (seat.myReservation) {
    if (seat.myReservation.status === 1) return 'checked-in'
    if (seat.myReservation.status === 0) return 'mine'
  }
  if (seat.reserveStatus === 1) return 'reserved'
  if (seat.status === 0) return 'disabled'
  return 'available'
}

const selectRoom = (room) => {
  selectedRoom.value = room
  loadSeats(room.id)
}

const handleSeatClick = async (seat) => {
  if (!seat.myReservation) {
    message.info('你没有预约这个座位')
    return
  }
  
  if (seat.myReservation.status === 1) {
    await handleCheckoutById(seat.myReservation)
  } else if (seat.myReservation.status === 0) {
    await handleCheckinById(seat.myReservation)
  }
}

const handleCheckinById = async (record) => {
  try {
    await checkin(record.id)
    message.success('签到成功！')
    loadData()
  } catch (e) {}
}

const handleCheckoutById = async (record) => {
  try {
    await checkout(record.id)
    message.success('签退成功')
    loadData()
  } catch (e) {}
}

const loadSeats = async (roomId) => {
  const today = new Date().toISOString().split('T')[0]
  try {
    const res = await getSeatsByRoom(roomId, today, '08:00', '22:00')
    const myMap = {}
    myReservations.value.forEach(r => {
      if (r.roomId === roomId) {
        myMap[r.seatId] = r
      }
    })
    seats.value = (res.data || []).map(s => ({
      ...s,
      myReservation: myMap[s.id]
    }))
  } catch (e) {
    message.error('加载座位失败')
  }
}

const loadMyReservations = async () => {
  try {
    const res = await getMyReservations({ pageSize: 20 })
    myReservations.value = (res.data.records || []).filter(r => 
      r.date === new Date().toISOString().split('T')[0] &&
      (r.status === 0 || r.status === 1)
    )
  } catch (e) {}
}

const loadData = () => {
  loadMyReservations()
  if (selectedRoom.value) {
    loadSeats(selectedRoom.value.id)
  }
}

onMounted(async () => {
  const res = await getRooms()
  rooms.value = res.data || []
  loadMyReservations()
})
</script>

<style scoped>
.checkin-page {
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

/* Sections */
.section-title {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 1rem;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 16px;
}

.section-title svg {
  color: var(--accent-primary);
}

/* Rooms Section */
.rooms-section {
  margin-bottom: 24px;
}

.rooms-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 16px;
}

.room-card {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 18px;
  background: var(--bg-card);
  border: 2px solid var(--border-subtle);
  border-radius: var(--radius-lg);
  cursor: pointer;
  transition: all var(--transition-normal);
}

.room-card:hover {
  border-color: var(--border-primary);
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
}

.room-card.selected {
  border-color: var(--accent-primary);
  background: var(--accent-glow);
}

.room-icon {
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

.room-info {
  flex: 1;
}

.room-name {
  font-size: 0.95rem;
  font-weight: 600;
  color: var(--text-primary);
}

.room-location {
  font-size: 0.8rem;
  color: var(--text-muted);
  margin-top: 4px;
}

.room-status {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background: var(--text-disabled);
  flex-shrink: 0;
}

.room-status.active {
  background: var(--success);
  box-shadow: 0 0 8px var(--success);
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}

/* Seat Section */
.seat-section {
  background: var(--bg-card);
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-lg);
  padding: 24px;
  margin-bottom: 24px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  flex-wrap: wrap;
  gap: 16px;
}

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
  background: var(--bg-secondary);
  border: 1px solid var(--border-primary);
}

.legend-dot.reserved {
  background: var(--warning-bg);
  border: 1px solid var(--warning);
}

.legend-dot.mine {
  background: var(--info-bg);
  border: 1px solid var(--info);
}

.legend-dot.checked-in {
  background: var(--success-bg);
  border: 1px solid var(--success);
}

/* Seat Map */
.seat-map {
  background: var(--bg-secondary);
  border-radius: var(--radius-lg);
  padding: 32px;
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
  width: 56px;
  height: 60px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 2px;
  border-radius: var(--radius-md);
  cursor: pointer;
  font-size: 0.75rem;
  font-weight: 600;
  transition: all var(--transition-fast);
  position: relative;
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
  width: 24px;
  height: 24px;
  border-radius: var(--radius-sm);
  background: var(--bg-elevated);
  color: var(--text-muted);
}

.seat-no {
  font-size: 0.7rem;
  color: var(--text-primary);
}

.seat-check {
  position: absolute;
  top: 2px;
  right: 2px;
  color: var(--success);
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

.seat.reserved {
  background: var(--warning-light);
  border-color: var(--warning);
  cursor: not-allowed;
}

.seat.reserved .seat-icon {
  background: var(--warning-bg);
  color: var(--warning);
}

.seat.mine {
  background: var(--accent-light);
  border-color: var(--accent-primary);
}

.seat.mine .seat-icon {
  background: var(--accent-lighter);
  color: var(--accent-primary);
}

.seat.mine:hover {
  box-shadow: 0 4px 12px rgba(6, 182, 212, 0.25);
}

.seat.checked-in {
  background: var(--success-light);
  border-color: var(--success);
}

.seat.checked-in .seat-icon {
  background: var(--success-bg);
  color: var(--success);
}

.seat.disabled {
  background: var(--bg-elevated);
  border-color: var(--border-primary);
  cursor: not-allowed;
  opacity: 0.5;
}

.seat.disabled .seat-icon {
  background: var(--bg-primary);
  color: var(--text-disabled);
}

.empty-seats {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  color: var(--text-muted);
  text-align: center;
}

.empty-seats svg {
  margin-bottom: 16px;
  opacity: 0.5;
}

/* Reservations Section */
.reservations-section {
  margin-bottom: 24px;
}

.reservations-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.reservation-card {
  display: flex;
  align-items: center;
  gap: 24px;
  padding: 18px 20px;
  background: var(--bg-card);
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-lg);
  transition: all var(--transition-fast);
}

.reservation-card:hover {
  border-color: var(--border-primary);
}

.reservation-time {
  display: flex;
  align-items: center;
  gap: 8px;
  min-width: 160px;
}

.reservation-time svg {
  color: var(--accent-primary);
}

.time-value {
  font-size: 0.9rem;
  font-weight: 600;
  color: var(--text-primary);
}

.reservation-info {
  flex: 1;
}

.info-room {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.9rem;
  font-weight: 500;
  color: var(--text-primary);
}

.info-room svg {
  color: var(--text-muted);
}

.info-seat {
  font-size: 0.8rem;
  color: var(--text-muted);
  margin-top: 4px;
  margin-left: 22px;
}

.reservation-action {
  display: flex;
  align-items: center;
  gap: 12px;
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

.empty-reservations {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  background: var(--bg-card);
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-lg);
  color: var(--text-muted);
  text-align: center;
}

.empty-reservations svg {
  margin-bottom: 16px;
  opacity: 0.5;
}

.empty-reservations p {
  margin-bottom: 16px;
}

/* Responsive */
@media (max-width: 768px) {
  .section-header {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .seat {
    width: 44px;
    height: 44px;
  }
  
  .reservation-card {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
}
</style>
