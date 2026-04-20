<template>
  <div class="students">
    <a-card title="学生管理">
      <a-form layout="inline" style="margin-bottom: 16px;">
        <a-form-item label="搜索">
          <a-input v-model:value="keyword" placeholder="用户名/姓名" style="width: 200px" />
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="loadStudents">查询</a-button>
        </a-form-item>
      </a-form>

      <a-table
        :columns="columns"
        :data-source="students"
        :loading="loading"
        :pagination="pagination"
        @change="handleTableChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'status'">
            <a-tag :color="record.status === 1 ? 'green' : 'red'">
              {{ record.status === 1 ? '正常' : '禁用' }}
            </a-tag>
          </template>
          <template v-if="column.key === 'action'">
            <a-space>
              <a-button type="link" @click="viewPassword(record)">查看密码</a-button>
              <a-popconfirm title="确定将密码重置为123456吗？" @confirm="handleResetPassword(record)">
                <a-button type="link">重置密码</a-button>
              </a-popconfirm>
              <a-switch
                :checked="record.status === 1"
                @change="(checked) => toggleStatus(record, checked)"
              />
              <a-popconfirm title="确定删除此学生吗？" @confirm="handleDelete(record)">
                <a-button type="link" danger>删除</a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>

    <a-modal v-model:open="passwordModalVisible" title="密码信息" :footer="null">
      <div style="margin-bottom: 16px;">
        <p><strong>用户名：</strong>{{ selectedUser?.username }}</p>
        <p><strong>姓名：</strong>{{ selectedUser?.realName }}</p>
        <p><strong>密码哈希：</strong></p>
        <a-textarea :value="selectedPassword" :rows="3" readonly />
      </div>
      <a-alert message="注：密码经过BCrypt加密存储，无法解密。如需重置密码请使用重置功能。" type="info" show-icon />
    </a-modal>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { getStudents, changeStudentStatus, deleteStudent, getUserPassword, resetUserPassword } from '@/api/super'

const students = ref([])
const loading = ref(false)
const keyword = ref('')
const pagination = ref({ current: 1, pageSize: 10, total: 0 })
const passwordModalVisible = ref(false)
const selectedUser = ref(null)
const selectedPassword = ref('')

const columns = [
  { title: '用户名', dataIndex: 'username', key: 'username' },
  { title: '姓名', dataIndex: 'realName', key: 'realName' },
  { title: '违约次数', dataIndex: 'violationCount', key: 'violationCount' },
  { title: '状态', dataIndex: 'status', key: 'status' },
  { title: '创建时间', dataIndex: 'createTime', key: 'createTime' },
  { title: '操作', key: 'action', width: 320 }
]

const loadStudents = async () => {
  loading.value = true
  try {
    const res = await getStudents({
      keyword: keyword.value,
      pageNum: pagination.value.current,
      pageSize: pagination.value.pageSize
    })
    students.value = res.data.records || []
    pagination.value.total = res.data.total || 0
  } catch (e) {
    message.error('加载失败')
  } finally {
    loading.value = false
  }
}

const handleTableChange = (pag) => {
  pagination.value.current = pag.current
  loadStudents()
}

const toggleStatus = async (record, checked) => {
  try {
    await changeStudentStatus(record.id, checked ? 1 : 0)
    message.success('修改成功')
    loadStudents()
  } catch (e) {
    // 错误已在拦截器处理
  }
}

const handleDelete = async (record) => {
  try {
    await deleteStudent(record.id)
    message.success('删除成功')
    loadStudents()
  } catch (e) {
    // 错误已在拦截器处理
  }
}

const handleResetPassword = async (record) => {
  try {
    await resetUserPassword(record.id)
    message.success('密码已重置为：123456')
  } catch (e) {
    // 错误已在拦截器处理
  }
}

const viewPassword = async (record) => {
  selectedUser.value = record
  try {
    const res = await getUserPassword(record.id)
    selectedPassword.value = res.data
    passwordModalVisible.value = true
  } catch (e) {
    message.error('获取密码失败')
  }
}

onMounted(() => {
  loadStudents()
})
</script>

<style scoped>
.students {
  max-width: 1200px;
  margin: 0 auto;
}
</style>
