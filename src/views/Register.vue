<template>
  <div class="register-page">
    <!-- Background Effects -->
    <div class="bg-effects">
      <div class="gradient-orb orb-1"></div>
      <div class="gradient-orb orb-2"></div>
      <div class="gradient-orb orb-3"></div>
      <div class="grid-overlay"></div>
    </div>

    <!-- Register Card -->
    <div class="register-container">
      <div class="register-card">
        <!-- Back Link -->
        <router-link to="/login" class="back-link">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M19 12H5M12 19l-7-7 7-7"/>
          </svg>
          返回登录
        </router-link>

        <!-- Header -->
        <div class="card-header">
          <div class="logo-wrapper">
            <div class="logo-icon">
              <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M16 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/>
                <circle cx="8.5" cy="7" r="4"/>
                <line x1="20" y1="8" x2="20" y2="14"/>
                <line x1="23" y1="11" x2="17" y2="11"/>
              </svg>
            </div>
            <div class="logo-text">
              <h1 class="logo-title">创建账号</h1>
              <p class="logo-subtitle">Join the study room reservation system</p>
            </div>
          </div>
        </div>

        <!-- Form -->
        <a-form
          :model="form"
          @finish="handleRegister"
          class="register-form"
          layout="vertical"
        >
          <a-form-item
            name="username"
            :rules="[{ required: true, message: '请输入学号' }]"
          >
            <label class="input-label">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <rect x="2" y="4" width="20" height="16" rx="2"/>
                <path d="M7 15h0M2 9.5h20"/>
              </svg>
              学号
            </label>
            <a-input 
              v-model:value="form.username" 
              placeholder="请输入学号" 
              size="large"
              class="form-input"
            >
              <template #prefix>
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="input-prefix-icon">
                  <rect x="2" y="4" width="20" height="16" rx="2"/>
                  <path d="M7 15h0M2 9.5h20"/>
                </svg>
              </template>
            </a-input>
          </a-form-item>

          <a-form-item
            name="realName"
            :rules="[{ required: true, message: '请输入真实姓名' }]"
          >
            <label class="input-label">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
                <circle cx="12" cy="7" r="4"/>
              </svg>
              真实姓名
            </label>
            <a-input 
              v-model:value="form.realName" 
              placeholder="请输入真实姓名" 
              size="large"
              class="form-input"
            >
              <template #prefix>
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="input-prefix-icon">
                  <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
                  <circle cx="12" cy="7" r="4"/>
                </svg>
              </template>
            </a-input>
          </a-form-item>

          <a-form-item
            name="password"
            :rules="[{ required: true, message: '请输入密码' }, { min: 6, message: '密码至少6位' }]"
          >
            <label class="input-label">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <rect x="3" y="11" width="18" height="11" rx="2" ry="2"/>
                <path d="M7 11V7a5 5 0 0 1 10 0v4"/>
              </svg>
              密码
            </label>
            <a-input-password 
              v-model:value="form.password" 
              placeholder="请输入密码（至少6位）" 
              size="large"
              class="form-input"
            >
              <template #prefix>
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="input-prefix-icon">
                  <rect x="3" y="11" width="18" height="11" rx="2" ry="2"/>
                  <path d="M7 11V7a5 5 0 0 1 10 0v4"/>
                </svg>
              </template>
            </a-input-password>
          </a-form-item>

          <a-form-item
            name="confirmPassword"
            :rules="[{ required: true, message: '请确认密码' }, { validator: validateConfirm }]"
          >
            <label class="input-label">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/>
                <polyline points="9 12 11 14 15 10"/>
              </svg>
              确认密码
            </label>
            <a-input-password 
              v-model:value="form.confirmPassword" 
              placeholder="请确认密码" 
              size="large"
              class="form-input"
            >
              <template #prefix>
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="input-prefix-icon">
                  <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/>
                  <polyline points="9 12 11 14 15 10"/>
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
                  <path d="M16 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/>
                  <circle cx="8.5" cy="7" r="4"/>
                  <line x1="20" y1="8" x2="20" y2="14"/>
                  <line x1="23" y1="11" x2="17" y2="11"/>
                </svg>
              </template>
              {{ loading ? '注册中...' : '立即注册' }}
            </a-button>
          </a-form-item>
        </a-form>

        <!-- Footer -->
        <div class="card-footer">
          <p class="login-hint">
            已有账号？
            <router-link to="/login" class="login-link">
              立即登录
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M5 12h14M12 5l7 7-7 7"/>
              </svg>
            </router-link>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { register } from '@/api/auth'

const router = useRouter()
const loading = ref(false)

const form = reactive({
  username: '',
  realName: '',
  password: '',
  confirmPassword: ''
})

const validateConfirm = async (rule, value) => {
  if (value !== form.password) {
    throw new Error('两次密码不一致')
  }
}

const handleRegister = async () => {
  loading.value = true
  try {
    await register({
      username: form.username,
      password: form.password,
      realName: form.realName,
      role: 0
    })
    message.success('注册成功，请登录')
    router.push('/login')
  } catch (e) {
    // Error handled by interceptor
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-page {
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
  background: radial-gradient(circle, rgba(139, 92, 246, 0.25) 0%, transparent 70%);
  top: -200px;
  left: -200px;
}

.orb-2 {
  width: 500px;
  height: 500px;
  background: radial-gradient(circle, rgba(6, 182, 212, 0.2) 0%, transparent 70%);
  bottom: -150px;
  right: -150px;
  animation-delay: -7s;
}

.orb-3 {
  width: 400px;
  height: 400px;
  background: radial-gradient(circle, rgba(16, 185, 129, 0.15) 0%, transparent 70%);
  top: 40%;
  left: 30%;
  animation-delay: -14s;
}

.grid-overlay {
  position: absolute;
  inset: 0;
  background-image: 
    linear-gradient(rgba(139, 92, 246, 0.03) 1px, transparent 1px),
    linear-gradient(90deg, rgba(139, 92, 246, 0.03) 1px, transparent 1px);
  background-size: 50px 50px;
}

@keyframes orbFloat {
  0%, 100% { transform: translate(0, 0) scale(1); }
  33% { transform: translate(40px, -40px) scale(1.05); }
  66% { transform: translate(-30px, 30px) scale(0.95); }
}

/* Container */
.register-container {
  max-width: 440px;
  width: 100%;
  position: relative;
  z-index: 1;
}

/* Register Card */
.register-card {
  background: var(--bg-card);
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-xl);
  padding: 40px;
  backdrop-filter: blur(20px);
  box-shadow: var(--shadow-xl), 0 0 40px rgba(139, 92, 246, 0.1);
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

/* Back Link */
.back-link {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  color: var(--text-secondary);
  font-size: 0.875rem;
  font-weight: 500;
  text-decoration: none;
  margin-bottom: 24px;
  transition: all var(--transition-fast);
}

.back-link:hover {
  color: var(--accent-primary);
  gap: 10px;
}

/* Card Header */
.card-header {
  margin-bottom: 28px;
}

.logo-wrapper {
  display: flex;
  align-items: center;
  gap: 14px;
}

.logo-icon {
  width: 52px;
  height: 52px;
  background: linear-gradient(135deg, #8b5cf6 0%, #7c3aed 100%);
  border-radius: var(--radius-lg);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  box-shadow: 0 8px 24px rgba(139, 92, 246, 0.3);
}

.logo-text {
  display: flex;
  flex-direction: column;
}

.logo-title {
  font-size: 1.3rem;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0;
  letter-spacing: -0.02em;
}

.logo-subtitle {
  font-size: 0.75rem;
  color: var(--text-muted);
  margin: 2px 0 0;
  font-weight: 500;
}

/* Form */
.register-form {
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

.input-label svg {
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
  margin-top: 24px;
  margin-bottom: 0;
}

.submit-btn {
  height: 52px !important;
  font-size: 1rem !important;
  font-weight: 600 !important;
  border-radius: var(--radius-md) !important;
  background: linear-gradient(135deg, #8b5cf6 0%, #7c3aed 100%) !important;
  border: none !important;
  box-shadow: 0 8px 24px rgba(139, 92, 246, 0.3) !important;
  transition: all var(--transition-normal) !important;
}

.submit-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 32px rgba(139, 92, 246, 0.4) !important;
}

.submit-btn:active {
  transform: translateY(0);
}

/* Footer */
.card-footer {
  margin-top: 24px;
  padding-top: 20px;
  border-top: 1px solid var(--border-subtle);
}

.login-hint {
  text-align: center;
  font-size: 0.9rem;
  color: var(--text-secondary);
  margin: 0;
}

.login-link {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  color: #8b5cf6;
  font-weight: 600;
  text-decoration: none;
  margin-left: 6px;
  transition: all var(--transition-fast);
}

.login-link:hover {
  color: #a78bfa;
  gap: 8px;
}

/* Responsive */
@media (max-width: 480px) {
  .register-page {
    padding: 1rem;
  }
  
  .register-card {
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
