<template>
  <div class="system">
    <a-card title="系统配置">
      <a-table
        :columns="columns"
        :data-source="configs"
        :loading="loading"
        :pagination="false"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'configValue'">
            <a-input-number
              v-if="editingId === record.id"
              v-model:value="editValue"
              :min="1"
              style="width: 100px"
            />
            <span v-else>{{ record.configValue }}</span>
          </template>
          <template v-if="column.key === 'action'">
            <a-space>
              <template v-if="editingId === record.id">
                <a-button type="primary" size="small" @click="handleSave(record)">保存</a-button>
                <a-button size="small" @click="editingId = null">取消</a-button>
              </template>
              <a-button v-else type="link" @click="startEdit(record)">编辑</a-button>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { getSystemConfigs, updateSystemConfig } from '@/api/super'

const configs = ref([])
const loading = ref(false)
const editingId = ref(null)
const editValue = ref(null)

const columns = [
  { title: '配置项', dataIndex: 'description', key: 'description' },
  { title: '配置键', dataIndex: 'configKey', key: 'configKey' },
  { title: '配置值', dataIndex: 'configValue', key: 'configValue' },
  { title: '操作', key: 'action', width: 150 }
]

const loadConfigs = async () => {
  loading.value = true
  try {
    const res = await getSystemConfigs()
    configs.value = res.data || []
  } catch (e) {
    message.error('加载失败')
  } finally {
    loading.value = false
  }
}

const startEdit = (record) => {
  editingId.value = record.id
  editValue.value = parseInt(record.configValue)
}

const handleSave = async (record) => {
  try {
    await updateSystemConfig({
      id: record.id,
      configValue: editValue.value.toString()
    })
    message.success('保存成功')
    editingId.value = null
    loadConfigs()
  } catch (e) {
    // 错误已在拦截器处理
  }
}

onMounted(() => {
  loadConfigs()
})
</script>

<style scoped>
.system {
  max-width: 800px;
  margin: 0 auto;
}
</style>
