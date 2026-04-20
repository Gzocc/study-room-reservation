import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue')
  },
  {
    path: '/',
    redirect: '/login'
  },
  // 学生端路由
  {
    path: '/student',
    component: () => import('@/layouts/StudentLayout.vue'),
    children: [
      {
        path: '',
        redirect: '/student/home'
      },
      {
        path: 'home',
        name: 'StudentHome',
        component: () => import('@/views/student/Home.vue')
      },
      {
        path: 'rooms',
        name: 'StudentRooms',
        component: () => import('@/views/student/Rooms.vue')
      },
      {
        path: 'reserve/:roomId',
        name: 'StudentReserve',
        component: () => import('@/views/student/Reserve.vue')
      },
      {
        path: 'records',
        name: 'StudentRecords',
        component: () => import('@/views/student/Records.vue')
      },
      {
        path: 'checkin',
        name: 'StudentCheckIn',
        component: () => import('@/views/student/CheckIn.vue')
      },
      {
        path: 'profile',
        name: 'StudentProfile',
        component: () => import('@/views/student/Profile.vue')
      }
    ]
  },
  // 管理员路由
  {
    path: '/admin',
    component: () => import('@/layouts/AdminLayout.vue'),
    children: [
      {
        path: '',
        redirect: '/admin/dashboard'
      },
      {
        path: 'dashboard',
        name: 'AdminDashboard',
        component: () => import('@/views/admin/Dashboard.vue')
      },
      {
        path: 'rooms',
        name: 'AdminRooms',
        component: () => import('@/views/admin/Rooms.vue')
      },
      {
        path: 'seats/:roomId',
        name: 'AdminSeats',
        component: () => import('@/views/admin/Seats.vue')
      },
      {
        path: 'reservations',
        name: 'AdminReservations',
        component: () => import('@/views/admin/Reservations.vue')
      },
      {
        path: 'violations',
        name: 'AdminViolations',
        component: () => import('@/views/admin/Violations.vue')
      }
    ]
  },
  // 超级管理员路由
  {
    path: '/super',
    component: () => import('@/layouts/SuperLayout.vue'),
    children: [
      {
        path: '',
        redirect: '/super/admins'
      },
      {
        path: 'admins',
        name: 'SuperAdmins',
        component: () => import('@/views/super/Admins.vue')
      },
      {
        path: 'students',
        name: 'SuperStudents',
        component: () => import('@/views/super/Students.vue')
      },
      {
        path: 'system',
        name: 'SuperSystem',
        component: () => import('@/views/super/System.vue')
      },
      {
        path: 'logs',
        name: 'SuperLogs',
        component: () => import('@/views/super/Logs.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const role = localStorage.getItem('role')

  if (to.path === '/login' || to.path === '/register') {
    next()
    return
  }

  if (!token) {
    next('/login')
    return
  }

  // 角色路由守卫
  if (to.path.startsWith('/student') && role !== '0') {
    next('/login')
    return
  }

  if (to.path.startsWith('/admin') && role !== '1') {
    next('/login')
    return
  }

  if (to.path.startsWith('/super') && role !== '2') {
    next('/login')
    return
  }

  next()
})

export default router
