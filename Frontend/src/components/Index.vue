<script setup>
import {ref, onMounted, reactive} from 'vue'
import {ElMessage} from "element-plus";
import {useRouter} from "vue-router";
let path =ref('')

const router = useRouter()
let go=()=>{
  let user = reactive(JSON.parse(localStorage.getItem('user') || '{}'))
  if(user.username){
    path.value='/dictionary'
  }else{
    path.value='/login'
    ElMessage.warning("如需获取服务，请先登录")
  }
  router.push(path.value)
}
onMounted(() => {

})
</script>

<template>
  <div class="background">
    <div class="header">
      <img src="/logo.png" alt="Logo" class="logo" />
      <h1 class="title">手语心弦</h1>
    </div>
    <div class="introduction">
      <p>手语是听障人士与世界沟通的桥梁，</p>
      <p>“手语心弦”致力于打造无障碍交流平台，</p>
      <p>让语言不再成为隔阂。</p>
    </div>
    <!-- 中间内容 -->
    <div class="center-content">
      <el-button type="success" size="large" @click="go">开始使用</el-button>
    </div>
  </div>
</template>

<style scoped>
.background {
  width: 100%;
  height: 100vh;
  background-image: url('/index_bg.png');
  background-size: cover;
  background-position: center;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: white;
  position: relative;
}

.header {
  position: absolute;
  top: 40px;
  left: 40px;
  display: flex;
  align-items: center;
}

.logo {
  width: 50px;
  height: 50px;
  margin-right: 15px;
}

.title {
  font-size: 32px;
  font-weight: bold;
  color: #ffffff;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
}

.center-content {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 80px;
}

.center-content .el-button {
  padding: 15px 30px;
  font-size: 20px;
}
.introduction {
  position: absolute;
  top: 200px; /* 距离顶部适当留白 */
  left: 50%;
  transform: translateX(-50%);
  text-align: center;
  max-width: 600px;
  font-size: 18px;
  line-height: 1.6;
  color: #ffffff;
  text-shadow: 2px 2px 6px rgba(0, 0, 0, 0.7);
  padding: 0 20px;
}
</style>