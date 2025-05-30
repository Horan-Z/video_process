<script setup lang="ts">
import { reactive, ref } from 'vue'

const loginInfo = reactive({
  username: '',
  password: '',
})
</script>

<template>
  <div class="login-wrapper">
    <div class="login-container">
      <div class="form-header">
        <h2>用户登陆</h2>
      </div>
      <el-form
        :label-position="right"
        label-width="auto"
        :model="loginInfo"
        style="max-width: 600px"
      >
        <el-form-item label="Username" :label-position="right">
          <el-input v-model="loginInfo.username" />
        </el-form-item>
        <el-form-item label="Password" :label-position="right">
          <el-input v-model="loginInfo.password" />
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<style scoped>

</style>
