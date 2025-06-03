import { createRouter, createWebHistory } from 'vue-router'
import routes from '@/router/routes.ts'

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(to, from, savedPosition) {
    // 控制滚动行为
    return savedPosition || { top: 0 }
  },
})

// 全局前置守卫
router.beforeEach((to, from, next) => {
  const isAuthenticated = localStorage.getItem('tokenValue')
  if (to.meta.requiresAuth && !isAuthenticated) {
    next({ path: '/login' })
  } else {
    next()
  }
})

export default router
