<script setup lang="ts">
import apiClient from '@/api/httpClient.ts'
import { useRouter } from 'vue-router'
import { onBeforeUnmount, onMounted, ref } from 'vue'
import IconFfmpeg from '@/components/icons/IconFfmpeg.vue'

const router = useRouter()
const isLoggedIn = ref(false)
const windowWidth = ref(window.innerWidth)

// 检查用户是否已登录
const checkLoginStatus = () => {
  const tokenValue = localStorage.getItem('tokenValue')
  isLoggedIn.value = !!tokenValue // 如果token存在，认为用户已登录
}

function logout() {
  const tokenName = localStorage.getItem('tokenName')
  const tokenValue = localStorage.getItem('tokenValue')
  localStorage.clear()
  apiClient.post('/api/auth/logout', {
    tokenName: tokenName,
    tokenValue: tokenValue,
  })
  router.push('/login')
}

// 窗口大小变化处理
const updateWindowWidth = () => {
  windowWidth.value = window.innerWidth
}

// 生命周期钩子
onMounted(() => {
  checkLoginStatus()
  // 初始化时设置窗口宽度
  updateWindowWidth()
  // 添加窗口大小变化监听
  window.addEventListener('resize', updateWindowWidth)
})

onBeforeUnmount(() => {
  // 移除监听，避免内存泄漏
  window.removeEventListener('resize', updateWindowWidth)
})
</script>

<template>
  <el-header class="page-header">
    <div class="header-content">
      <div class="left-content">
        <icon-ffmpeg width="40" height="40" fill="#409EFF" />
        <span class="title-text">FFmpeg <br v-if="windowWidth <= 440" />Online</span>
        <div class="menu">
          <!-- 桌面端水平菜单 -->
          <el-menu
            v-if="windowWidth >= 650"
            :default-active="$route.path"
            :ellipsis="false"
            router
            mode="horizontal"
          >
            <el-menu-item index="/">视频上传</el-menu-item>
            <el-menu-item index="/single">单视频处理</el-menu-item>
            <el-menu-item index="/multi">多视频拼接</el-menu-item>
          </el-menu>

          <!-- 移动端下拉菜单 -->
          <el-menu
            menu-trigger="click"
            v-else
            :default-active="$route.path"
            :ellipsis="false"
            router
            mode="horizontal"
          >
            <el-sub-menu index="1" close-on-click-outside>
              <template #title>{{ $route.meta.name || '菜单' }}</template>
              <el-menu-item index="/">视频上传</el-menu-item>
              <el-menu-item index="/single">单视频处理</el-menu-item>
              <el-menu-item index="/multi">多视频拼接</el-menu-item>
            </el-sub-menu>
          </el-menu>
        </div>
      </div>
      <div class="right-content">
        <el-button type="primary" v-show="!isLoggedIn" @click="router.push('/login')"
          >登录
        </el-button>
        <el-button type="danger" plain v-show="isLoggedIn" @click="logout">注销</el-button>
      </div>
    </div>
  </el-header>
</template>

<style scoped>
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 0.5rem; /* 相当于px-6 */
  box-shadow: var(--el-box-shadow-light);
}

.header-content {
  display: flex;
  justify-content: space-between;
  width: 100%;
}

.left-content {
  display: flex;
  align-items: center;
  gap: 0;
}

.menu {
  margin-left: 0.5rem;
}

.title-text {
  display: inline-flex;
  margin-left: 0.5rem;
  line-height: 1.1em;
  font-size: 1.4em;
  font-weight: bold;
}

.right-content {
  float: right;
  display: flex;
  align-items: center;
  gap: 0.75rem; /* 相当于space-x-3 */
}
</style>
