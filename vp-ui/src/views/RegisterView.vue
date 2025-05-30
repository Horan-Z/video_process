<script setup lang="ts">
import { reactive, ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const registerInfo = reactive({
  username: '',
  password: '',
})

const errorMsg = ref('')
const isFormValid = ref(false)

interface res {
  data: {
    code: number
    msg: string
    data: object
  }
}

// 输入验证
const validateInput = () => {
  console.log(123)
  // 基本验证
  if (registerInfo.username && registerInfo.password) {
    isFormValid.value = true
    errorMsg.value = ''
  } else {
    isFormValid.value = false
  }
}

const onSubmit = () => {
  handleRegister()
}

// 注册处理
const handleRegister = async () => {
  // 防止XSS攻击
  const xssPattern = /(~|{|}"|'|<|>|\?)/g
  if (xssPattern.test(registerInfo.username) || xssPattern.test(registerInfo.password)) {
    errorMessage('输入内容包含非法字符')
    return
  }
  try {
    const response: res = await axios.post('http://localhost:8080/api/auth/register', {
      username: registerInfo.username,
      password: registerInfo.password,
    })

    if (response.data.code == 401) {
      errorMessage(response.data.msg)
    } else if (response.data.code == 200) {
      console.log('注册成功')
      // 跳转到登录页
      router.push('/login')
    } else {
      errorMessage(response.data.code.toString())
    }
  } catch (e) {
    console.log(e)
    errorMessage('注册失败:')
  }
}

// 错误提示
const errorMessage = (text: string) => {
  errorMsg.value = text
  setTimeout(() => {
    errorMsg.value = ''
  }, 3000)
}

onMounted(() => {
  validateInput()
})
</script>

<template>
  <div class="register-wrapper">
    <div class="register-container">
      <div class="form-header">
        <h2>用户注册</h2>
      </div>
      <el-form
        label-width="auto"
        :model="registerInfo"
        style="max-width: 600px"
        class="floating-form"
        @submit.prevent="validateInput"
      >
        <el-form-item label="用户名">
          <el-input v-model="registerInfo.username" type="text" @input="validateInput" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input
            v-model="registerInfo.password"
            type="password"
            show-password
            @input="validateInput"
          />
        </el-form-item>

        <div class="error-message" v-if="errorMsg">{{ errorMsg }}</div>
        <el-button
          type="primary"
          @click="onSubmit"
          round
          class="submit-btn"
          :disabled="!isFormValid"
        >
          <span>注册</span>
          <i class="arrow-icon"></i>
        </el-button>
        <div class="form-footer">
          <span>已有账号？</span>
          <a href="/login">立即登录</a>
        </div>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
.register-wrapper {
  min-height: 93.5vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 20px;
}

.register-container {
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

.error-message {
  color: #f56c6c;
  font-size: 14px;
  text-align: center;
  margin-bottom: 20px;
}
</style>
