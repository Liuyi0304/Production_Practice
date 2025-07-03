<script setup>
import {Search} from "@element-plus/icons-vue";
import {ref,onMounted} from "vue";
import request from "../utils/request.js";
import { ElLoading ,ElMessage} from 'element-plus'
import VideoView from "./VideoView.vue";

const mergedVideoUrl = ref('');

const input = ref('')
let loadingInstance = null
const options={
  lock:true,
  text:'Loading……',
  spinner:"el-icon-loading",
}
const showLoading = ()=>{
  if(!loadingInstance){
    loadingInstance=ElLoading.service(options);
  }
}
const hideLoading=()=>{
  if(loadingInstance){
    loadingInstance.close();
    loadingInstance=null;
  }
}
function load(input){
  showLoading()
  request.get(`/words_to_videos/${input}`).then(res=>{
    console.log(res)
    hideLoading()
    if(res.code === '200'){
      mergedVideoUrl.value=res.data;
    }else{
      ElMessage.error(res.msg)
    }
  })
}


onMounted(()=>{


})
</script>

<template>
  <div>
    <div class="bg">
      <div style="width: 600px">
        <div style="text-align: center;color: #ffffff;font-size: 30px;margin-bottom: 20px" >手语翻译</div>
        <div style="display: flex">
          <el-input v-model="input" size="default" placeholder="请输入词句" >
            <template #prefix>
              <el-icon :size="18">
                <Search />
              </el-icon>
            </template>
          </el-input>
          <el-button type="primary" style="color: blue;background:blue; border:black;display:flex" @click="load(input)"><el-icon color="white" :size="18"><Search /></el-icon></el-button>
        </div>
      </div>
    </div>
    <!-- 播放器 -->
    <div style="display: flex; justify-content: center; /* 水平居中 */align-items: center; /* 垂直居中 */">
      <VideoView v-if="mergedVideoUrl" :src="mergedVideoUrl" title="aaaa" ></VideoView>
    </div>

  </div>
</template>

<style scoped>
.bg{
  height: 260px;
  background-image: url("/bg.jpg");
  background-size: 100%;
  background-position-y: center;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>