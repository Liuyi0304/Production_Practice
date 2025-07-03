<script setup>

import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import request from "../utils/request.js"
import {ElMessage} from "element-plus";

const router = useRouter()

const form = reactive({
  role: 'USER',
  username: '',
  password: ''
})

const rules = {
  username: [
    { required: true, message: '请输入账号', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
  ]
}

const formRef = ref()

const login = () => {
  formRef.value.validate(valid => {
    if (valid) {
      request.post('/login', form).then(res => {
        console.log(res)
        if (res.code === '200') {
          localStorage.setItem("user", JSON.stringify(res.data))
          if (res.data.role === 'USER') {
            router.push('/')
          }
          ElMessage.success('登录成功')
        } else {
          ElMessage.error(res.msg)
        }
      }).catch(() => {
        ElMessage.error('登录失败，请重试')
      })
    }
  })
}
</script>

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
            <div style="border-bottom: 2px solid orangered;padding-bottom: 10px">登录</div>
            <a style="color: black" href="/register">
              <div style="margin-left: 10px">注册</div>
            </a>
          </div>
          <el-form :model="form" :rules="rules" ref="formRef">
            <el-form-item prop="username">
              <el-input size="medium"  prefix-icon="el-icon-user" placeholder="请输入账号" v-model="form.username"></el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input size="medium"  prefix-icon="el-icon-lock" placeholder="请输入密码" show-password  v-model="form.password"></el-input>
            </el-form-item>
<!--            <el-form-item prop="role">-->
<!--              <el-select v-model="form.role" style="width: 100%">-->
<!--                <el-option label="志愿者" value="VOLUNTEER"></el-option>-->
<!--                <el-option label="活动组织者" value="HOST"></el-option>-->
<!--                <el-option label="管理员" value="ADMIN"></el-option>-->
<!--              </el-select>-->
<!--            </el-form-item>-->
            <!--            <el-form-item prop="verCode">-->
            <!--              <div style="display: flex;justify-content: center">-->
            <!--                <el-input  prefix-icon="el-icon-message" style="margin-right: 10px" placeholder="请输入验证码" v-model="form.verCode"></el-input>-->
            <!--                <img :src="captureImg" @click="loadImg()" width="140px" height="33px">-->
            <!--              </div>-->
            <!--            </el-form-item>-->
            <el-form-item>
              <el-button size="medium" style="width: 100%; background-color: orangered; border-color: orangered; color: white" @click="login">登 录</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>

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