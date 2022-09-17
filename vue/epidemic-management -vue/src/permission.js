import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken } from '@/utils/auth' // get token from cookie
import getPageTitle from '@/utils/get-page-title'

NProgress.configure({ showSpinner: false }) // NProgress Configuration

const whiteList = ['/login'] // no redirect whitelist

// 路由权限验证：全局前置守卫：初始化时执行、每次路由切换前执行
router.beforeEach(async (to, from, next) => {
  // 进度条
  NProgress.start()

  // 更新页面标题
  document.title = getPageTitle(to.meta.title)

  // 确定用户是否登录
  const hasToken = getToken()

  if (hasToken) {
    if (to.path === '/login') {
      // 已登录则重定向到主页
      next({ path: '/' })
      NProgress.done()
    } else {
      const hasRoles = store.getters.roles && store.getters.roles.length > 0
      if (hasRoles) {
        next()
      } else { // 首次登录
        try {
          if (store.getters.routes.length === 0) {
            const { roles } = await store.dispatch('user/getInfo')
            // 获取角色对应的路由
            const accessRoutes = await store.dispatch('permission/generateRoutes', roles)
            router.addRoutes(accessRoutes)// 动态添加路由
            // router.options.routes = store.getters.routes; // 渲染侧导航
            next({ ...to, replace: true }) // hack方法，确保路由添加完成
          } else { // 当有用户权限的时候，说明所有可访问路由已生成 如访问没权限的全面会自动进入404页面
            next()
          }
        }
        catch (error) {
          // remove token and go to login page to re-login
          await store.dispatch('user/resetToken')
          // Message.error(error || 'Has Error')
          Message.warning({
            type: "warning",
            message: '请登录~',
          });
          next(`/login?redirect=${to.path}`)
          NProgress.done()
        }
      }
    }
  } else { // 没有token时返回登录页，重新登录
    if (whiteList.indexOf(to.path) !== -1) {
      // in the free login whitelist, go directly
      next()
    } else {
      next(`/login?redirect=${to.path}`)
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})
