<script setup lang="ts">
import UploadItem from './components/UploadItem.vue'
</script>

<template>
  <div class="common-layout">
    <el-container>
      <el-header style="text-align: right; font-size: 12px"> </el-header>
      <br />
      <el-main><UploadItem /></el-main>
    </el-container>
  </div>
</template>

<style scoped>
.common-layout .el-header {
  position: relative;
  background-color: #ffffff;
  color: var(--el-text-color-primary);
  box-shadow: var(--el-box-shadow);
}
</style>
