<script setup>
  import {Search} from "@element-plus/icons-vue";
  import { ref } from 'vue'
  import VideoView from "./VideoView.vue";
  import request from "../utils/request.js";
  import {ElMessage} from "element-plus";
  const input = ref('')
  // const videoUrl = ref('http://vjs.zencdn.net/v/oceans.mp4')
  let word=ref({
    text:"你好",
    video:"http://localhost:7877/videos/你好.mp4",
    pose:"一手食指指向对方，一手握拳，向上伸出拇指。",
    relation:""
  })
  function load(title){
      request.get(`/video/${title}`).then(res=>{
        console.log(res)
        if(res.code === '200'){
          word.value.video =res.data?.path
          word.value.text = res.data?.title
          word.value.pose = res.data?.description
        }else{
          ElMessage.error(res.msg)
        }
      })
  }

</script>

<template>
  <div>
    <div class="bg">
      <div style="width: 600px">
        <div style="text-align: center;color: #ffffff;font-size: 30px;margin-bottom: 20px" >手语词典</div>
        <div style="display: flex">
          <el-input v-model="input" size="default" placeholder="请输入关键词" >
            <template #prefix>
              <el-icon :size="18">
                <Search />
              </el-icon>
            </template>
          </el-input>
          <el-button type="primary" style="color: blue;background:blue; border:black" @click="load(input)"><el-icon color="white" :size="18"><Search /></el-icon></el-button>
        </div>
      </div>
    </div>
    <div style="display: flex;margin-right: 50px;margin-left: 50px">
      <div style="width: 800px">
        <div>
          <el-text style="font-weight: bold;font-size:26px;display: flex;justify-content: center;text-align: center;" size="large">
            {{word.text}}</el-text>
        </div>
        <div>
          <VideoView :src="word.video" title="aaaa" ></VideoView>
        </div>
      </div>
      <div style="width:calc(100vw - 900px);margin-left: 50px">
        <div>
          <el-text style="font-weight: bold;font-size: 26px" size="large">手势描述</el-text>
        </div>
        <div>
          <el-text style="font-size: 20px" size="large">{{word.pose}}</el-text>
        </div>
      </div>
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