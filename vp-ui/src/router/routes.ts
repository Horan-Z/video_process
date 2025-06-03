import Login from '@/views/LoginView.vue'
import Upload from '@/views/UploadView.vue'

const routes = [
  {
    path: '/',
    component: Upload,
    meta: { requiresAuth: true, keepAlive: true },
  },
  {
    path: '/upload',
    component: Upload,
    meta: { requiresAuth: true, keepAlive: true },
  },
  {
    path: '/register',
    component: () => import('@/views/RegisterView.vue'),
    meta: { requiresAuth: false, keepAlive: false },
  },
  {
    path: '/login',
    component: Login,
    meta: { requiresAuth: false, keepAlive: false },
  },
  {
    path: '/:pathMatch(.*)*', // 404页面
    name: 'NotFound',
    component: () => import('@/views/NotFound.vue'), // 动态导入
  },
]

export default routes
