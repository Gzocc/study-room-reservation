<template>
  <div class="seats">
    <a-card title="座位管理">
      <template #extra>
        <a-button @click="$router.back()">返回</a-button>
      </template>

      <div class="seat-grid" v-if="seats.length > 0">
        <div class="seat-row" v-for="row in seatRows" :key="row">
          <div
            v-for="seat in getRowSeats(row)"
            :key="seat.id"
            :class="['seat', seat.status === 0 ? 'disabled' : '']"
            @click="showSeatDetail(seat)"
          >
            {{ seat.seatNo }}
          </div>
        </div>
      </div>

      <a-empty v-else description="暂无座位" />
    </a-card>

    <a-modal v-model:open="detailVisible" title="座位详情" :footer="null">
      <a-descriptions :column="1" v-if="currentSeat">
        <a-descriptions-item label="座位号">{{ currentSeat.seatNo }}</a-descriptions-item>
        <a-descriptions-item label="位置">第{{ currentSeat.rowNum }}行 第{{ currentSeat.colNum }}列</a-descriptions-item>
        <a-descriptions-item label="类型">
          <a-select v-model:value="currentSeat.type" style="width: 120px" @change="handleTypeChange">
            <a-select-option :value="0">普通</a-select-option>
            <a-select-option :value="1">靠窗</a-select-option>
            <a-select-option :value="2">电源插座</a-select-option>
          </a-select>
        </a-descriptions-item>
        <a-descriptions-item label="状态">
          <a-switch
            :checked="currentSeat.status === 1"
            @change="handleStatusChange"
            checked-children="可用"
            un-checked-children="禁用"
          />
        </a-descriptions-item>
      </a-descriptions>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { message } from 'ant-design-vue'
import { getAdminSeats, changeSeatStatus, changeSeatType } from '@/api/seat'

const route = useRoute()
const roomId = route.params.roomId
const seats = ref([])
const detailVisible = ref(false)
const currentSeat = ref(null)

const seatRows = computed(() => {
  const rows = [...new Set(seats.value.map(s => s.rowNum))]
  return rows.sort((a, b) => a - b)
})

const getRowSeats = (row) => {
  return seats.value
    .filter(s => s.rowNum === row)
    .sort((a, b) => a.colNum - b.colNum)
}

const loadSeats = async () => {
  try {
    const res = await getAdminSeats(roomId)
    seats.value = res.data || []
  } catch (e) {
    message.error('加载失败')
  }
}

const showSeatDetail = (seat) => {
  currentSeat.value = { ...seat }
  detailVisible.value = true
}

const handleStatusChange = async (checked) => {
  try {
    await changeSeatStatus(currentSeat.value.id, checked ? 1 : 0)
    message.success('修改成功')
    currentSeat.value.status = checked ? 1 : 0
    loadSeats()
  } catch (e) {
    // 错误已在拦截器处理
  }
}

const handleTypeChange = async (type) => {
  try {
    await changeSeatType(currentSeat.value.id, type)
    message.success('修改成功')
    loadSeats()
  } catch (e) {
    // 错误已在拦截器处理
  }
}

onMounted(() => {
  loadSeats()
})
</script>

<style scoped>
.seats {
  max-width: 1000px;
  margin: 0 auto;
}

.seat-grid {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.seat-row {
  display: flex;
  gap: 8px;
}

.seat {
  width: 60px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  background: #f6ffed;
  border: 1px solid #b7eb8f;
  transition: all 0.2s;
}

.seat:hover {
  background: #d9f7be;
}

.seat.disabled {
  background: #fff2f0;
  border-color: #ffccc7;
}
</style>
