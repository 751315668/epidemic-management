import { asyncRouterMap, constantRoutes } from '@/router';

// 判断用户是否有路由的权限
function hasPermission(roles, route) {
    if (route.meta && route.meta.roles) {
        return roles.some(role => route.meta.roles.includes(role))
    } else {
        return true
    }
}

// 路由权限判断逻辑
export function filterAsyncRoutes(routes, roles) {
    const res = []
    routes.forEach(route => { // 循环动态添加的路由钩子
        const tmp = { ...route }
        if (hasPermission(roles, tmp)) { // 判断路由是否满足roles
            if (tmp.children) { // 有子路由判断子路由
                tmp.children = filterAsyncRoutes(tmp.children, roles)
            }
            res.push(tmp)
        }
    })
    return res
}

const state = {
    routes: [],
    addRoutes: []
}
const mutations = {
    SET_ROUTES: (state, routes) => {
        state.addRoutes = routes
        state.routes = constantRoutes.concat(routes)
    }
}

const actions = {
    generateRoutes({ commit }, roles) {
        return new Promise(resolve => {
            let accessedRoutes
            // if (roles.includes('admin')) {
            //     accessedRoutes = asyncRouterMap || []
            // } else {
            accessedRoutes = filterAsyncRoutes(asyncRouterMap, roles)
            // }
            commit('SET_ROUTES', accessedRoutes)
            resolve(accessedRoutes)
        })
    }
}

export default {
    namespaced: true,
    state,
    mutations,
    actions
}
