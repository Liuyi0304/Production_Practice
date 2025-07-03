import axios from "axios";


const instance = axios.create({
    baseURL: 'http://localhost:7877',
    timeout: 500000
})

instance.interceptors.request.use(
    (config)=>{
        config.headers['Content-Type'] = 'application/json;charset=utf-8';        // 设置请求头格式
        let user = JSON.parse(localStorage.getItem("user") || '{}')  // 获取缓存的用户信息
        config.headers['token'] = user.token  // 设置请求头

        return config
    },
    (error)=>{
        console.error('request error: ' + error)
        return Promise.reject(error)
    }
)

instance.interceptors.response.use(
    (response)=>{
        let res = response.data;
        // 兼容服务端返回的字符串数据
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }
        return res;
    },
    (error)=>{
        console.error('response error: ' + error)
        return Promise.reject(error)
    }
)

export default instance