<script setup>
  import {onMounted,ref} from "vue";
  const videoStreamUrl = 'http://localhost:5000/video_feed'; // Flask提供的视频流地址
  let prediction='';
  const text = ref('');
  let predictionInterval=null;
  function clearText(){
    clearInterval(predictionInterval);
    text.value = '';
  }
  function exitApp(){
    window.close();
  }
  function fetchPrediction() {
    fetch('http://localhost:5000/get_prediction')
        .then(response => response.json())
        .then(data => {
          if (data.prediction) {
            // 直接将新的预测内容赋值给text
            text.value = data.prediction;
          }
        });
  }
  onMounted(()=>{
    predictionInterval = setInterval(() => fetchPrediction(), 1000); // 这里修改了setInterval的回调函数写法
  })
</script>

<template>
  <div class="container">
    <img :src="videoStreamUrl" alt="Video Stream">
    <p>Prediction: </p>
    <button @click="clearText">Clear Text</button>
    <button @click="exitApp">Exit</button>
    <textarea v-model="text" rows="2" cols="40"></textarea>
  </div>
</template>

<style scoped>
  .container {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    min-height: 100vh;
  }
  img {
    width: 640px;
    height: 480px;
  }
</style>