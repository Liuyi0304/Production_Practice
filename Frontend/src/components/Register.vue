<template>
  <div class="container">
    <div style="width: 560px;">
      <img src="/login_bg.png" alt="" style="width: 100%;">
    </div>
    <div style="flex:1;background-color: #f8f8f8">
      <div style="height: 50px; display: flex; align-items: center; background-color: white;padding-left: 20px">
        <img src="/logo.png" alt="" style="width: 40px">
        <span style="font-size: 24px;margin-left: 5px">手语心弦</span>
      </div>
      <div style="height: calc(100vh - 50px); display:flex;align-items: center;justify-content: center">
        <div style="width: 450px; padding: 40px; background-color: white; border-radius: 5px;">
          <div style="display:flex;margin-bottom: 30px;font-size: 24px">
            <a href="/login" style="color: black">
              <div>登录</div>
            </a>
            <div style="margin-left: 10px;border-bottom: 2px solid orangered;padding-bottom: 10px">注册</div>
          </div>
          <el-form :model="form" :rules="rules" ref="formRef">
            <el-form-item prop="username">
              <el-input size="medium"  prefix-icon="el-icon-user" placeholder="请输入账号" v-model="form.username"></el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input size="medium"  prefix-icon="el-icon-lock" placeholder="请输入密码" show-password  v-model="form.password"></el-input>
            </el-form-item>
            <el-form-item prop="confirmPassword">
              <el-input size="medium"  prefix-icon="el-icon-lock" placeholder="请确认密码" show-password  v-model="form.confirmPassword"></el-input>
            </el-form-item>
<!--            <el-form-item prop="role">-->
<!--              <el-select v-model="form.role" style="width: 100%">-->
<!--                <el-option label="志愿者" value="VOLUNTEER"></el-option>-->
<!--                <el-option label="活动组织者" value="HOST"></el-option>-->
<!--                &lt;!&ndash;                <el-option label="管理员" value="ADMIN"></el-option>&ndash;&gt;-->
<!--              </el-select>-->
<!--            </el-form-item>-->
            <el-form-item>
              <el-button size="medium" style="width: 100%; background-color: orangered; border-color: orangered; color: white" @click="register">注 册</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import request from '../utils/request.js'
import { ElMessage } from 'element-plus'

const router = useRouter()

// 表单数据
const form = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  role: 'USER'
})

// 自定义验证规则
const validatePassword = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请确认密码'))
  } else if (value !== form.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

// 表单验证规则
const rules = {
  username: [
    { required: true, message: '请输入账号', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
  ],
  confirmPassword: [
    { validator: validatePassword, trigger: 'blur' },
  ]
}

const formRef = ref()

// 注册方法
const register = () => {
  formRef.value.validate(valid => {
    if (valid) {
      request.post('/register', form).then(res => {
        if (res.code === '200') {
          router.push('/login')
          ElMessage.success('注册成功')
        } else {
          ElMessage.error(res.msg)
        }
      }).catch(() => {
        ElMessage.error('注册失败，请重试')
      })
    }
  })
}
</script>



<style scoped>
.container {
  height: 100vh;
  overflow: hidden;
  display: flex;
}
a {
  color: #2a60c9;
}
</style>