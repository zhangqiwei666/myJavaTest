<template>
  <div class="login-container">
    <div class="login-background">
      <div class="gradient-sphere sphere-1"></div>
      <div class="gradient-sphere sphere-2"></div>
    </div>

    <div class="login-box glass-card">
      <div class="login-header">
        <div class="brand-logo">CRM</div>
        <h2 class="title">CRM 客户管理系统</h2>
        <p class="subtitle">Spring Boot 3 + Vue 3 现代化后端管理平台</p>
      </div>

      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        size="large"
        class="login-form"
        @keyup.enter="handleLogin"
      >
        <el-form-item prop="username">
          <el-input
            v-model="form.username"
            placeholder="请输入账号 (例: admin)"
            :prefix-icon="User"
          />
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="请输入密码 (默认: 123456)"
            show-password
            :prefix-icon="Lock"
          />
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            class="submit-btn"
            :loading="loading"
            @click="handleLogin"
          >
            登 录
          </el-button>
        </el-form-item>
      </el-form>

      <div class="quick-login">
        <span class="label">快捷填充演示账号：</span>
        <div class="btn-group">
          <el-tag class="tag-btn" type="primary" @click="fillAccount('admin', '123456')">
            超级管理员 (admin)
          </el-tag>
          <el-tag class="tag-btn" type="success" @click="fillAccount('seller', '123456')">
            销售员 (seller)
          </el-tag>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { User, Lock } from '@element-plus/icons-vue'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref<FormInstance>()
const loading = ref(false)

const form = reactive({
  username: '',
  password: ''
})

const rules: FormRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await userStore.login(form)
        ElMessage.success('登录成功！欢迎回来')
        router.push('/dashboard')
      } catch (err) {
        // error handled by axios interceptor
      } finally {
        loading.value = false
      }
    }
  })
}

const fillAccount = (u: string, p: string) => {
  form.username = u
  form.password = p
}
</script>

<style scoped>
.login-container {
  width: 100vw;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #0f172a;
  position: relative;
  overflow: hidden;
}

.login-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.gradient-sphere {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
  opacity: 0.5;
}

.sphere-1 {
  width: 400px;
  height: 400px;
  background: radial-gradient(circle, #6366f1 0%, #4f46e5 100%);
  top: -100px;
  left: -100px;
}

.sphere-2 {
  width: 500px;
  height: 500px;
  background: radial-gradient(circle, #06b6d4 0%, #3b82f6 100%);
  bottom: -150px;
  right: -150px;
}

.login-box {
  width: 420px;
  padding: 40px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
  z-index: 1;
}

.login-header {
  text-align: center;
  margin-bottom: 32px;
}

.brand-logo {
  display: inline-flex;
  width: 56px;
  height: 56px;
  background: linear-gradient(135deg, #4f46e5 0%, #6366f1 100%);
  color: white;
  font-size: 20px;
  font-weight: 800;
  align-items: center;
  justify-content: center;
  border-radius: 14px;
  box-shadow: 0 8px 20px rgba(79, 70, 229, 0.3);
  margin-bottom: 16px;
}

.title {
  font-size: 24px;
  font-weight: 800;
  color: #0f172a;
}

.subtitle {
  font-size: 13px;
  color: #64748b;
  margin-top: 6px;
}

.submit-btn {
  width: 100%;
  height: 44px;
  background: linear-gradient(135deg, #4f46e5 0%, #6366f1 100%);
  border: none;
  font-size: 16px;
  font-weight: 600;
  border-radius: 10px;
  margin-top: 8px;
  box-shadow: 0 4px 12px rgba(79, 70, 229, 0.3);
}

.quick-login {
  margin-top: 24px;
  padding-top: 20px;
  border-top: 1px dashed #e2e8f0;
  font-size: 12px;
  color: #64748b;
}

.quick-login .label {
  display: block;
  margin-bottom: 8px;
}

.btn-group {
  display: flex;
  gap: 8px;
}

.tag-btn {
  cursor: pointer;
  transition: transform 0.2s;
}

.tag-btn:hover {
  transform: translateY(-2px);
}
</style>
