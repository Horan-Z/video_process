import Login from '@/views/LoginView.vue'
import Upload from '@/views/UploadView.vue'
import Single from '@/views/SingleFileProcess.vue'
import Multi from '@/views/MultipleFileProcess.vue'

const routes = [
  {
    path: '/',
    component: Upload,
    meta: { requiresAuth: true, keepAlive: true, name: '视频上传' },
  },
  {
    path: '/upload',
    component: Upload,
    meta: { requiresAuth: true, keepAlive: true, name: '视频上传' },
  },
  {
    path: '/single',
    component: Single,
    meta: { requiresAuth: true, keepAlive: true, name: '单视频处理' },
  },
  {
    path: '/multi',
    component: Multi,
    meta: { requiresAuth: true, keepAlive: true, name: '多视频拼接' },
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
