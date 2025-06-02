<script setup lang="ts">
import apiClient from '@/api/httpClient.ts'
import { useRouter } from 'vue-router'
import { ref, onMounted } from 'vue'
import IconFfmpeg from '@/components/icons/IconFfmpeg.vue'

const router = useRouter()
const isLoggedIn = ref(false)

// 检查用户是否已登录
const checkLoginStatus = () => {
  const tokenValue = localStorage.getItem('tokenValue')
  isLoggedIn.value = !!tokenValue // 如果token存在，认为用户已登录
}

onMounted(() => {
  checkLoginStatus()
})

function logout() {
  const tokenName = localStorage.getItem('tokenName')
  const tokenValue = localStorage.getItem('tokenValue')
  localStorage.clear()
  apiClient.post('/api/auth/logout', {
    tokenName: tokenName,
    tokenValue: tokenValue
  })
  router.push('/login')
}
</script>

<template>
  <el-header class="page-header">
    <div class="header-content">
      <div class="left-content">
        <icon-ffmpeg width="40" height="40" fill="#409EFF" />
        <span class="title-text">FFmpeg Online</span>
      </div>
      <div class="right-content">
        <el-button type="primary" v-show="!isLoggedIn" @click="router.push('/login')">登录</el-button>
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
  padding: 0 1.5rem; /* 相当于px-6 */
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
  gap: 0.5rem;
}

.title-text {
  font-size: 1.4em;
  font-weight: bold;
}

.right-content {
  display: flex;
  align-items: center;
  gap: 0.75rem; /* 相当于space-x-3 */
}
</style>
