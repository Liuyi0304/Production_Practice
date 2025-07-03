<script setup>
  import { onMounted} from 'vue'
  import { useRouter } from 'vue-router'
  const router = useRouter()
  const menus=[
      {text: '手语词典', path: '/dictionary' },
      {text: '视频识别',path:'/prediction'},
      {text:'手语翻译',path:'/sign_translation'}
  ]
  let user = JSON.parse(localStorage.getItem("user") || '{}')
  function logout(){
    localStorage.removeItem("user");
    router.push("/login");
  }
  onMounted(() => {
    console.log(user)
  })
</script>

<template>
  <div>
    <!--头部-->
    <div class="header">
      <div class="header-left" style="cursor: pointer;" @click="$router.push('/')">
        <img src="/logo.png" style="height: 50px">
        <div class="title">手语心弦</div>
      </div>
      <div class="header-center">
        <div @click="$router.push(item.path)" class="menu" v-for="item in menus" :key="item.path"
             :class="{'menu-active':item.path===$route.path}" style="cursor: pointer;">{{item.text}}</div>
      </div>
      <div class="header-right">
        <div v-if="!user.username">
          <el-button @click="$router.push('/login')">登录</el-button>
          <el-button @click="$router.push('/register')">注册</el-button>
        </div>
        <div v-else>
          <el-dropdown>
            <div class="header-dropdown">
              <img :src="user.avatar" alt="" style="border-radius: 50%;width: 40px;height: 40px;">
              <div style="color:black; font-size: 18px;cursor: pointer">
                <span style="margin-left: 5px">{{ user.username }}</span>
                <el-icon style="margin-left: 5px"><ArrowDown /></el-icon>
              </div>
            </div>
            <template #dropdown>
              <el-dropdown-menu  slot="dropdown">
                <el-dropdown-item>
                  <div @click="$router.push('/person')">个人信息</div>
                </el-dropdown-item>
                <el-dropdown-item>
                  <div style="text-decoration: none" @click="logout">退出登录</div>
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>
    <!--主体-->
    <div class="main-body">
      <router-view ref="child"/>
    </div>
  </div>
</template>

<style scoped>
  .header-dropdown {
    position: relative;
    z-index: 1;
  }
  .el-dropdown-menu {
    z-index: 9999;
  }
  .header{
    display: flex;
    height: 60px;
    line-height: 60px;
    border-bottom: 1px solid #eee;
    background: #d7deff;
  }
  .header-left{
    width: 150px;
    display: flex;
    align-items: center;
    padding-left: 30px;
    margin-right: 350px;
  }
  .header-left .title{
    flex: 1;
    color: #355476;
    margin-left: 10px;
    font-size: 20px;
    font-weight: bold;
    cursor: pointer;
  }
  .header-center {
    display: flex;
    flex: 1;
  }
  .header-right {
    width: 200px;
    padding-right: 20px;
    text-align: right;
  }
  .header-dropdown {
    margin-top: 10px;
    display: flex;
    align-items: center;
    justify-content: right;
  }
  .main-body{
    min-height: calc(100vh - 170px);
  }
  .menu{
    display: flex;
    align-items: center;
    justify-content: right;
    margin-right: 40px;
    font-size: 20px;
  }
</style>