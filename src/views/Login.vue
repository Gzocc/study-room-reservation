<template>
  <div class="login-page">
    <!-- Background Effects -->
    <div class="bg-effects">
      <div class="gradient-orb orb-1"></div>
      <div class="gradient-orb orb-2"></div>
      <div class="gradient-orb orb-3"></div>
      <div class="grid-overlay"></div>
    </div>

    <!-- Login Card -->
    <div class="login-container">
      <div class="login-card">
        <!-- Logo Section -->
        <div class="card-header">
          <div class="logo-wrapper">
            <div class="logo-icon">
              <svg width="32" height="32" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M12 2L2 7L12 12L22 7L12 2Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                <path d="M2 17L12 22L22 17" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                <path d="M2 12L12 17L22 12" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </div>
            <div class="logo-text">
              <h1 class="logo-title">自习室预约</h1>
              <p class="logo-subtitle">Study Room Reservation</p>
            </div>
          </div>
        </div>

        <!-- Form -->
        <a-form
          :model="form"
          @finish="handleLogin"
          class="login-form"
          layout="vertical"
        >
          <a-form-item
            name="username"
            :rules="[{ required: true, message: '请输入用户名' }]"
          >
            <label class="input-label">
              <svg class="input-icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
                <circle cx="12" cy="7" r="4"/>
              </svg>
              用户名
            </label>
            <a-input 
              v-model:value="form.username" 
              placeholder="请输入用户名" 
              size="large"
              class="form-input"
            >
              <template #prefix>
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="input-prefix-icon">
                  <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
                  <circle cx="12" cy="7" r="4"/>
                </svg>
              </template>
            </a-input>
          </a-form-item>

          <a-form-item
            name="password"
            :rules="[{ required: true, message: '请输入密码' }]"
          >
            <label class="input-label">
              <svg class="input-icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <rect x="3" y="11" width="18" height="11" rx="2" ry="2"/>
                <path d="M7 11V7a5 5 0 0 1 10 0v4"/>
              </svg>
              密码
            </label>
            <a-input-password 
              v-model:value="form.password" 
              placeholder="请输入密码" 
              size="large"
              class="form-input"
            >
              <template #prefix>
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="input-prefix-icon">
                  <rect x="3" y="11" width="18" height="11" rx="2" ry="2"/>
                  <path d="M7 11V7a5 5 0 0 1 10 0v4"/>
                </svg>
              </template>
            </a-input-password>
          </a-form-item>

          <a-form-item class="submit-item">
            <a-button 
              type="primary" 
              html-type="submit" 
              :loading="loading" 
              block 
              size="large"
              class="submit-btn"
            >
              <template #icon>
                <svg v-if="!loading" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M15 3h4a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2h-4"/>
                  <polyline points="10 17 15 12 10 7"/>
                  <line x1="15" y1="12" x2="3" y2="12"/>
                </svg>
              </template>
              {{ loading ? '登录中...' : '开始使用' }}
            </a-button>
          </a-form-item>
        </a-form>

        <!-- Footer -->
        <div class="card-footer">
          <p class="register-hint">
            还没有账号？
            <router-link to="/register" class="register-link">
              立即注册
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M5 12h14M12 5l7 7-7 7"/>
              </svg>
            </router-link>
          </p>
        </div>
      </div>

      <!-- Features Preview -->
      <div class="features-preview">
        <div class="feature-item" v-for="(feature, index) in features" :key="index" :style="{ animationDelay: `${index * 0.1}s` }">
          <div class="feature-icon" :style="{ background: feature.bg }">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" v-html="feature.icon"></svg>
          </div>
          <div class="feature-text">
            <div class="feature-title">{{ feature.title }}</div>
            <div class="feature-desc">{{ feature.desc }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { login } from '@/api/auth'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)

const form = reactive({
  username: '',
  password: ''
})

const features = [
  {
    title: '实时预约',
    desc: '查看自习室可用状态',
    bg: 'linear-gradient(135deg, rgba(6, 182, 212, 0.2), rgba(6, 182, 212, 0.1))',
    icon: '<rect x="3" y="4" width="18" height="18" rx="2" ry="2"/><line x1="16" y1="2" x2="16" y2="6"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/>'
  },
  {
    title: '快速签到',
    desc: '扫码签到签退',
    bg: 'linear-gradient(135deg, rgba(16, 185, 129, 0.2), rgba(16, 185, 129, 0.1))',
    icon: '<path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/><polyline points="22 4 12 14.01 9 11.01"/>'
  },
  {
    title: '预约提醒',
    desc: '准时提醒不错过',
    bg: 'linear-gradient(135deg, rgba(245, 158, 11, 0.2), rgba(245, 158, 11, 0.1))',
    icon: '<path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"/><path d="M13.73 21a2 2 0 0 1-3.46 0"/>'
  },
  {
    title: '违约记录',
    desc: '信用管理更透明',
    bg: 'linear-gradient(135deg, rgba(239, 68, 68, 0.2), rgba(239, 68, 68, 0.1))',
    icon: '<path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"/><line x1="12" y1="9" x2="12" y2="13"/><line x1="12" y1="17" x2="12.01" y2="17"/>'
  }
]

const handleLogin = async () => {
  loading.value = true
  try {
    const res = await login(form)
    userStore.setToken(res.data)
    
    const payloadStr = res.data.split('.')[1]
    const payload = JSON.parse(decodeURIComponent(escape(atob(payloadStr))))
    
    userStore.setUserInfo({
      username: payload.username,
      realName: payload.realName || payload.username,
      role: payload.role
    })
    message.success('登录成功')
    
    if (payload.role === 0) {
      router.push('/student/home')
    } else if (payload.role === 1) {
      router.push('/admin/dashboard')
    } else {
      router.push('/super/admins')
    }
  } catch (e) {
    message.error('登录失败，请检查用户名和密码')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--bg-primary);
  position: relative;
  overflow: hidden;
  padding: 2rem;
}

/* Background Effects */
.bg-effects {
  position: fixed;
  inset: 0;
  pointer-events: none;
  z-index: 0;
}

.gradient-orb {
  position: absolute;
  border-radius: 50%;
  filter: blur(100px);
  opacity: 0.4;
  animation: orbFloat 20s ease-in-out infinite;
}

.orb-1 {
  width: 600px;
  height: 600px;
  background: radial-gradient(circle, rgba(6, 182, 212, 0.3) 0%, transparent 70%);
  top: -200px;
  right: -200px;
}

.orb-2 {
  width: 500px;
  height: 500px;
  background: radial-gradient(circle, rgba(16, 185, 129, 0.2) 0%, transparent 70%);
  bottom: -150px;
  left: -150px;
  animation-delay: -7s;
}

.orb-3 {
  width: 400px;
  height: 400px;
  background: radial-gradient(circle, rgba(139, 92, 246, 0.15) 0%, transparent 70%);
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  animation-delay: -14s;
}

.grid-overlay {
  position: absolute;
  inset: 0;
  background-image: 
    linear-gradient(rgba(6, 182, 212, 0.03) 1px, transparent 1px),
    linear-gradient(90deg, rgba(6, 182, 212, 0.03) 1px, transparent 1px);
  background-size: 50px 50px;
}

@keyframes orbFloat {
  0%, 100% { transform: translate(0, 0) scale(1); }
  33% { transform: translate(40px, -40px) scale(1.05); }
  66% { transform: translate(-30px, 30px) scale(0.95); }
}

/* Container */
.login-container {
  display: flex;
  gap: 60px;
  align-items: center;
  max-width: 1000px;
  width: 100%;
  position: relative;
  z-index: 1;
}

/* Login Card */
.login-card {
  flex: 1;
  max-width: 420px;
  background: var(--bg-card);
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-xl);
  padding: 40px;
  backdrop-filter: blur(20px);
  box-shadow: var(--shadow-xl), 0 0 40px rgba(6, 182, 212, 0.1);
  animation: cardAppear 0.8s cubic-bezier(0.16, 1, 0.3, 1);
}

@keyframes cardAppear {
  from {
    opacity: 0;
    transform: translateY(40px) scale(0.96);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

/* Card Header */
.card-header {
  margin-bottom: 32px;
}

.logo-wrapper {
  display: flex;
  align-items: center;
  gap: 16px;
}

.logo-icon {
  width: 56px;
  height: 56px;
  background: linear-gradient(135deg, var(--accent-primary) 0%, #0891b2 100%);
  border-radius: var(--radius-lg);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--bg-primary);
  box-shadow: 0 8px 24px rgba(6, 182, 212, 0.3);
}

.logo-text {
  display: flex;
  flex-direction: column;
}

.logo-title {
  font-size: 1.35rem;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0;
  letter-spacing: -0.02em;
}

.logo-subtitle {
  font-size: 0.8rem;
  color: var(--text-muted);
  margin: 2px 0 0;
  font-weight: 500;
}

/* Form */
.login-form {
  margin-top: 8px;
}

.input-label {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.875rem;
  font-weight: 600;
  color: var(--text-secondary);
  margin-bottom: 8px;
}

.input-icon {
  color: var(--accent-primary);
}

.form-input {
  background: var(--bg-secondary) !important;
  border: 1px solid var(--border-subtle) !important;
  border-radius: var(--radius-md) !important;
  height: 50px !important;
  transition: all var(--transition-normal) !important;
}

.form-input:hover {
  border-color: var(--border-primary) !important;
}

.form-input:focus,
.form-input:focus-within {
  border-color: var(--accent-primary) !important;
  box-shadow: 0 0 0 4px var(--accent-glow) !important;
  background: var(--bg-secondary) !important;
}

.form-input :deep(.ant-input) {
  background: transparent !important;
  color: var(--text-primary) !important;
}

.form-input :deep(.ant-input::placeholder) {
  color: var(--text-disabled) !important;
}

.input-prefix-icon {
  color: var(--text-muted);
  margin-right: 8px;
}

:deep(.ant-input-password-icon) {
  color: var(--text-muted) !important;
}

/* Submit Button */
.submit-item {
  margin-top: 28px;
  margin-bottom: 0;
}

.submit-btn {
  height: 52px !important;
  font-size: 1rem !important;
  font-weight: 600 !important;
  border-radius: var(--radius-md) !important;
  background: linear-gradient(135deg, var(--accent-primary) 0%, #0891b2 100%) !important;
  border: none !important;
  box-shadow: 0 8px 24px rgba(6, 182, 212, 0.3) !important;
  transition: all var(--transition-normal) !important;
}

.submit-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 32px rgba(6, 182, 212, 0.4) !important;
}

.submit-btn:active {
  transform: translateY(0);
}

/* Footer */
.card-footer {
  margin-top: 28px;
  padding-top: 24px;
  border-top: 1px solid var(--border-subtle);
}

.register-hint {
  text-align: center;
  font-size: 0.9rem;
  color: var(--text-secondary);
  margin: 0;
}

.register-link {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  color: var(--accent-primary);
  font-weight: 600;
  text-decoration: none;
  margin-left: 6px;
  transition: all var(--transition-fast);
}

.register-link:hover {
  color: var(--accent-secondary);
  gap: 8px;
}

/* Features Preview */
.features-preview {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 20px;
  animation: fadeSlideIn 0.8s ease-out 0.2s both;
}

@keyframes fadeSlideIn {
  from {
    opacity: 0;
    transform: translateX(30px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
  background: var(--bg-card);
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-lg);
  transition: all var(--transition-normal);
  cursor: pointer;
}

.feature-item:hover {
  border-color: var(--border-primary);
  transform: translateX(8px);
  box-shadow: var(--shadow-lg);
}

.feature-icon {
  width: 48px;
  height: 48px;
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--accent-primary);
  flex-shrink: 0;
}

.feature-title {
  font-size: 1rem;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 4px;
}

.feature-desc {
  font-size: 0.85rem;
  color: var(--text-muted);
}

/* Responsive */
@media (max-width: 900px) {
  .login-container {
    flex-direction: column;
    gap: 40px;
  }
  
  .features-preview {
    display: none;
  }
  
  .login-card {
    max-width: 100%;
  }
}

@media (max-width: 480px) {
  .login-page {
    padding: 1rem;
  }
  
  .login-card {
    padding: 28px 24px;
    border-radius: var(--radius-lg);
  }
  
  .logo-icon {
    width: 48px;
    height: 48px;
  }
  
  .logo-title {
    font-size: 1.2rem;
  }
}
</style>
