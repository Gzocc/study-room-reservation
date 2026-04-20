import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const username = ref(localStorage.getItem('username') || '')
  const realName = ref(localStorage.getItem('realName') || '')
  const role = ref(parseInt(localStorage.getItem('role') || '0'))
  const violationCount = ref(parseInt(localStorage.getItem('violationCount') || '0'))

  const isLoggedIn = computed(() => !!token.value)
  const isStudent = computed(() => role.value === 0)
  const isAdmin = computed(() => role.value === 1)
  const isSuperAdmin = computed(() => role.value === 2)

  function setToken(t) {
    token.value = t
    localStorage.setItem('token', t)
  }

  function setUserInfo(info) {
    username.value = info.username
    realName.value = info.realName
    role.value = info.role
    violationCount.value = info.violationCount || 0
    localStorage.setItem('username', info.username)
    localStorage.setItem('realName', info.realName)
    localStorage.setItem('role', info.role.toString())
    localStorage.setItem('violationCount', (info.violationCount || 0).toString())
  }

  function logout() {
    token.value = ''
    username.value = ''
    realName.value = ''
    role.value = 0
    violationCount.value = 0
    localStorage.removeItem('token')
    localStorage.removeItem('username')
    localStorage.removeItem('realName')
    localStorage.removeItem('role')
    localStorage.removeItem('violationCount')
  }

  return {
    token,
    username,
    realName,
    role,
    violationCount,
    isLoggedIn,
    isStudent,
    isAdmin,
    isSuperAdmin,
    setToken,
    setUserInfo,
    logout
  }
})
