<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import apiClient from '@/api/httpClient.ts'
import type { HttpResponse } from '@/types/Response'
import { ElNotification } from 'element-plus'

const router = useRouter()
const loginInfo = reactive({
  username: '',
  password: '',
})

interface LoginRequest {
  username: string
  password: string
}

interface LoginResponseData {
  username: string
  uuid: string
  tokenInfo: {
    tokenName: string
    tokenValue: string
  }
}

const isFormValid = ref(false)

// 输入验证
const validateInput = () => {
  // 基本验证
  isFormValid.value = !!(loginInfo.username && loginInfo.password)
}

const onSubmit = () => {
  handleLogin()
}

// 登录处理
const handleLogin = async () => {
  // 防止XSS攻击
  const xssPattern = /(~|{|}"|'|<|>|\?)/g
  if (xssPattern.test(loginInfo.username) || xssPattern.test(loginInfo.password)) {
    errorMessage('输入内容包含非法字符')
    return
  }
  try {
    // 使用示例
    const response = await apiClient.post<HttpResponse<LoginResponseData>, LoginRequest>(
      '/api/auth/login',
      {
        username: loginInfo.username,
        password: loginInfo.password,
      },
    )
    if (response.code == 401) {
      errorMessage(response.msg)
    } else if (response.code == 200) {
      loginSuccess(response)
      // 跳转到主页
      await router.push('/')
    } else {
      console.log(response)
      errorMessage(response.code.toString())
    }
  } catch (e) {
    console.log(e)
    errorMessage('登录失败')
  }
  loginInfo.password = ''
}

const loginSuccess = (response: HttpResponse<LoginResponseData>) => {
  localStorage.setItem('tokenName', response.data.tokenInfo.tokenName)
  localStorage.setItem('tokenValue', response.data.tokenInfo.tokenValue)
  localStorage.setItem('userUuid', response.data.uuid)
  ElNotification({
    title: '登录成功',
    message: `用户${response.data.username}已登录`,
    type: 'success',
  })
}

// 错误提示
const errorMessage = (text: string) => {
  ElNotification({
    title: 'Error',
    message: text,
    type: 'error',
  })
}

onMounted(() => {
  validateInput()
})
</script>

<template>
  <div class="login-wrapper">
    <div class="login-container">
      <div class="form-header">
        <h2>用户登录</h2>
      </div>
      <el-form
        label-width="auto"
        :model="loginInfo"
        style="max-width: 600px"
        class="floating-form"
        @submit.prevent="validateInput"
      >
        <el-form-item label="用户名">
          <el-input v-model="loginInfo.username" type="text" @input="validateInput" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input
            v-model="loginInfo.password"
            type="password"
            show-password
            @input="validateInput"
          />
        </el-form-item>
        <el-button
          type="primary"
          @click="onSubmit"
          round
          class="submit-btn"
          :disabled="!isFormValid"
        >
          <span>登录</span>
          <i class="arrow-icon"></i>
        </el-button>
        <div class="form-footer">
          <span>还没有账号？</span>
          <a href="/register">立即注册</a>
        </div>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
.login-wrapper {
  min-height: 93.5vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 20px;
}

.login-container {
  width: 100%;
  max-width: 480px;
  background: white;
  border-radius: 20px;
  padding: 40px;
  box-shadow: var(--el-box-shadow);
}

.form-header {
  text-align: center;
  margin-bottom: 40px;
}

.form-header h2 {
  color: #2c3e50;
  font-size: 32px;
  margin-bottom: 10px;
  font-weight: 700;
}

.submit-btn {
  width: 100%;
  padding: 7px;
  background: linear-gradient(to right, #3498db, #2980b9);
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 18px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.submit-btn:hover {
  transform: translateY(-2px);
  box-shadow: var(--el-box-shadow-light);
}

.arrow-icon {
  border: solid white;
  border-width: 0 2px 2px 0;
  display: inline-block;
  padding: 3px;
  transform: rotate(-45deg);
}

.form-footer {
  text-align: center;
  margin-top: 20px;
  color: #95a5a6;
}

.form-footer a {
  color: #3498db;
  text-decoration: none;
  margin-left: 5px;
  font-weight: 600;
}

.form-footer a:hover {
  text-decoration: underline;
}
</style>
