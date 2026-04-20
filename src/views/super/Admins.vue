<template>
  <div class="admins">
    <a-card title="管理员管理">
      <template #extra>
        <a-button type="primary" @click="showAddModal">
          <template #icon><PlusOutlined /></template>
          添加管理员
        </a-button>
      </template>

      <a-table
        :columns="columns"
        :data-source="admins"
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
              <a-popconfirm title="确定删除此管理员吗？" @confirm="handleDelete(record)">
                <a-button type="link" danger>删除</a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>

    <a-modal v-model:open="modalVisible" title="添加管理员" @ok="handleAdd">
      <a-form :model="form" layout="vertical">
        <a-form-item label="用户名" required>
          <a-input v-model:value="form.username" />
        </a-form-item>
        <a-form-item label="姓名" required>
          <a-input v-model:value="form.realName" />
        </a-form-item>
        <a-form-item label="密码" required>
          <a-input-password v-model:value="form.password" />
        </a-form-item>
      </a-form>
    </a-modal>

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
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { PlusOutlined } from '@ant-design/icons-vue'
import { getAdmins, addAdmin, deleteAdmin, changeAdminStatus, getUserPassword, resetUserPassword } from '@/api/super'

const admins = ref([])
const loading = ref(false)
const modalVisible = ref(false)
const form = reactive({ username: '', realName: '', password: '' })
const pagination = ref({ current: 1, pageSize: 10, total: 0 })
const passwordModalVisible = ref(false)
const selectedUser = ref(null)
const selectedPassword = ref('')

const columns = [
  { title: '用户名', dataIndex: 'username', key: 'username' },
  { title: '姓名', dataIndex: 'realName', key: 'realName' },
  { title: '状态', dataIndex: 'status', key: 'status' },
  { title: '创建时间', dataIndex: 'createTime', key: 'createTime' },
  { title: '操作', key: 'action', width: 320 }
]

const loadAdmins = async () => {
  loading.value = true
  try {
    const res = await getAdmins({
      pageNum: pagination.value.current,
      pageSize: pagination.value.pageSize
    })
    admins.value = res.data.records || []
    pagination.value.total = res.data.total || 0
  } catch (e) {
    message.error('加载失败')
  } finally {
    loading.value = false
  }
}

const handleTableChange = (pag) => {
  pagination.value.current = pag.current
  loadAdmins()
}

const showAddModal = () => {
  Object.assign(form, { username: '', realName: '', password: '' })
  modalVisible.value = true
}

const handleAdd = async () => {
  if (!form.username || !form.realName || !form.password) {
    message.warning('请填写必填项')
    return
  }
  try {
    await addAdmin(form)
    message.success('添加成功')
    modalVisible.value = false
    loadAdmins()
  } catch (e) {
    // 错误已在拦截器处理
  }
}

const toggleStatus = async (record, checked) => {
  try {
    await changeAdminStatus(record.id, checked ? 1 : 0)
    message.success('修改成功')
    loadAdmins()
  } catch (e) {
    // 错误已在拦截器处理
  }
}

const handleDelete = async (record) => {
  try {
    await deleteAdmin(record.id)
    message.success('删除成功')
    loadAdmins()
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
  loadAdmins()
})
</script>

<style scoped>
.admins {
  max-width: 1200px;
  margin: 0 auto;
}
</style>
