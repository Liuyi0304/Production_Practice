import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import router from './router/index'
import * as ElIconModules from '@element-plus/icons'
import vue3videoPlay from 'vue3-video-play' // 引入组件
import 'vue3-video-play/dist/style.css' // 引入css
import 'element-plus/dist/index.css'
import request from'./utils/request.js'
import * as ElIconsVue from '@element-plus/icons-vue'
const app = createApp(App)
for(let iconName in ElIconModules){
    app.component(iconName,ElIconModules[iconName])
}
for (const [key, component] of Object.entries(ElIconsVue)) {
    app.component(key, component)
}
app.config.globalProperties.$request=request
app.use(router).use(vue3videoPlay).mount('#app')