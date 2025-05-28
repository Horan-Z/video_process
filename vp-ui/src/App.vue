<script setup lang="ts">
import UploadItem from './components/UploadItem.vue'
</script>

<template>
  <div class="common-layout">
    <el-container>
      <el-header>
        <h1 style="font-size: 44px">FFmpeg Online</h1>
      </el-header>
      <br />
      <el-main><UploadItem /></el-main>
    </el-container>
  </div>
</template>

<style scoped>
.common-layout .el-header {
  display: grid;
  place-items: center;
  position: relative;
  background-color: #ffffff;
  color: var(--el-text-color-primary);
  /* box-shadow: var(--el-box-shadow); */
}

.common-layout .el-main {
  width: 100%;
  max-width: 600px;
  margin: 0 auto;
}
</style>
