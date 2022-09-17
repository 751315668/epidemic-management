import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   当设置 true 的时候该路由不会在侧边栏出现 如401，login等页面，或者如一些编辑页面/edit/1
 * alwaysShow: true               是否总是显示根路由
 * redirect: noRedirect           当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
 * 
 * name:'router-name'             设定路由的名字，一定要填写不然使用<keep-alive>时会出现各种问题
 * meta : {
    roles: ['admin','editor']    设置该路由进入的权限，支持多个权限叠加
    title: 'title'               设置该路由在侧边栏和面包屑中展示的名字
    icon: 'svg-name'/'el-icon-x' 设置该路由的图标，支持 svg-class，也支持 el-icon-x element-ui 的 icon
    breadcrumb: false            如果设置为false，则不会在breadcrumb面包屑中显示(默认 true)
    activeMenu: '/example/list'  当路由设置了该属性，则会高亮相对应的侧边栏。
  }
 */

/**
 * 不需要动态判断权限的路由，如登录页、404、等通用页面。
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

]

//异步挂载的路由
//动态需要根据权限加载的路由表 
export const asyncRouterMap = [
  {
    path: '/',
    component: Layout,
    redirect: '/news',
    meta: { title: '社区发布', icon: 'main' },
    children: [
      {
        path: 'news',
        name: 'NewsShow',
        component: () => import('@/views/news/index'),
        meta: { title: '新闻浏览', icon: 'main' }
      },
      {
        path: 'publish',
        name: 'NewsPublish',
        component: () => import('@/views/news/publish'),
        meta: { title: '新闻发布', icon: 'publish', roles: ['admin'] }
      },
      {
        path: 'charts',
        name: 'Charts',
        component: () => import('@/views/charts/index'),
        meta: { title: '图表总览', icon: 'publish' }
      }
    ]
  },

  {
    path: '/editUser/:id(\\d+)',
    component: Layout,
    redirect: '/editUser/:id(\\d+)/index',
    hidden: true,
    children: [
      {
        path: 'index',
        name: 'EditUser',
        component: () => import('@/views/edit-user/index'),
        meta: { title: '修改个人信息', icon: 'el-icon-edit' }
      }
    ]
  },

  {
    path: '/volunteer',
    component: Layout,
    redirect: '/volunteer/info',
    name: 'Volunteer',
    meta: { title: '志愿者及任务', icon: 'volunteer-info' },
    children: [
      {
        path: 'info',
        name: 'VolunteerInfo',
        component: () => import('@/views/volunteer/index'),
        meta: { title: '志愿者信息', icon: 'volunteer' }
      },
      {
        path: 'task',
        name: 'VolunteerTask',
        component: () => import('@/views/volunteer/task'),
        meta: { title: '志愿任务', icon: 'task' },
      },
      {
        path: 'apply',
        name: 'VolunteerApply',
        component: () => import('@/views/volunteer/volunteer-apply'),
        meta: { title: '志愿者申请', icon: 'volunteerapply', roles: ['guest'] }
      },
      // {
      //   path: 'edit/:id(\\d+)',
      //   name: 'TaskEdit',
      //   component: () => import('@/views/volunteer/component/edit'),
      //   meta: { title: '志愿任务修改', noCache: false, activeMenu: '/volunteer/task' },
      //   hidden: true
      // },
      {
        path: 'detail/:id(\\d+)',
        name: 'TaskDetail',
        component: () => import('@/views/volunteer/component/detail'),
        meta: { title: '志愿任务详情', noCache: false, activeMenu: '/volunteer/task' },
        hidden: true
      },
    ]
  },

  {
    path: '/repository',
    component: Layout,
    redirect: '/repository/warehouse',
    name: 'Repository',
    meta: { title: '防疫物资', icon: 'repository' },
    children: [
      {
        path: 'warehouse',
        name: 'Warehouse',
        component: () => import('@/views/repository/warehouse'),
        meta: { title: '仓库', icon: 'repository' },
      },
      {
        path: 'supplyInfo/:id(\\d+)',
        name: 'SupplyInfo',
        component: () => import('@/views/repository/supply-info'),
        meta: { title: '物资详情', icon: 'apply' },
        hidden: true
      },
      // {
      //   path: 'apply',
      //   name: 'Apply',
      //   component: () => import('@/views/repository/apply'),
      //   meta: { title: '物资申请', icon: 'apply', roles: ['volunteer'] },
      // },
      // {
      //   path: 'applyInfo/:id(\\d+)',
      //   name: 'ApplyInfo',
      //   component: () => import('@/views/repository/apply-info'),
      //   meta: { title: '物资申请详情', icon: 'apply' },
      //   hidden: true
      // },

    ]
  },

  {
    path: '/message',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'Message',
        component: () => import('@/views/message/index'),
        meta: { title: '留言板', icon: 'el-icon-message' }
      }
    ]
  },

  // 404 页面一定要最后加载，如果放在 constantRoutes 一同声明了 404 ，后面的所有页面都会被拦截到404 
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  mode: 'history',
  // base: '/',
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
