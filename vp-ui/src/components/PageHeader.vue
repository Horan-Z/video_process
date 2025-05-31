<script setup lang="ts">
import apiClient from '@/api/httpClient.ts'
import { Delete } from '@element-plus/icons-vue'
import router from '@/router'

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
    <h1 style="font-size: 44px">FFmpeg Online</h1>
    <el-button type="danger" :icon="Delete" circle @click="logout" />
  </el-header>
  <br />
</template>

<style scoped>
.page-header {
  display: grid;
  place-items: center;
  position: relative;
  background-color: #ffffff;
  color: var(--el-text-color-primary);
  /* box-shadow: var(--el-box-shadow); */
}
</style>
