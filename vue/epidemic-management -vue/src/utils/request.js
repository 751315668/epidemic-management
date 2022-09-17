import axios from 'axios'
import { MessageBox, Message } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'
import router from '@/router/index'

// 创建axios实例
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  // withCredentials: true, // 当跨域请求时发送cookie
  timeout: 5000 // 请求超时时间
})

// request拦截器
service.interceptors.request.use(
  config => {
    // 发送请求前的执行逻辑
    if (store.getters.token) {
      // 使每个request都携带token
      config.headers['user_token'] = getToken()
    }
    return config
  },
  error => {
    // 请求错误时的执行逻辑
    Message.error({
      type: "error",
      message: '出错了~',
    });
    console.log(error)
    return Promise.reject(error)
  }
)

// response拦截器
service.interceptors.response.use(
  /**
   * If you want to get http information such as headers or status
   * Please return  response => response
  */

  /**
   * Determine the request status by custom code
   * Here is just an example
   * You can also judge the status by HTTP Status Code
   */
  response => {
    const res = response.data

    // 请求错误
    if (res.code !== 200) {
      // Message({
      //   message: res.message || 'Error',
      //   type: 'error',
      //   duration: 5 * 1000
      // })

      // 500:服务器错误; 501:token过期
      if (res.code == 500) {
        Message.warning({
          type: 'error',
          message: '出错了~'
        })
      }
      return Promise.reject(new Error(res.message || 'Error'))
    } else if (res.code == 501) {
      MessageBox.confirm('您已被注销，请重新登录~', '通知', {
        confirmButtonText: '重新登录',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        store.dispatch('user/resetToken').then(() => {
          location.reload()
           router.push('/login')
        })
      })
    } else {
      return res
    }
  },
  error => {
    console.log('err' + error) // for debug
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
