import { login, logout, getInfo } from '@/api/user'
import { getToken, setToken, removeToken } from '@/utils/auth'
import { resetRouter } from '@/router'

const getDefaultState = () => {
  return {
    token: getToken(),
    name: '',
    avatar: '',
    id: 0,
    roles: [],
    volunteer: {}
  }
}

const state = getDefaultState()

const mutations = {
  RESET_STATE: (state) => {
    Object.assign(state, getDefaultState())
  },
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_ROLES(state, roles) {
    state.roles = roles
  },
  SET_ID(state, id) {
    state.id = id
  },
  SET_VOLUNTEER(state, volunteer) {
    state.volunteer = volunteer
  }
}

const actions = {
  // 用户登录
  login({ commit }, userInfo) {
    const { username, password } = userInfo
    return new Promise((resolve, reject) => {
      // api/user下login请求
      login({ username: username.trim(), password: password }).then(response => {
        const { data } = response
        // 保存数据至state
        commit('SET_TOKEN', data.token)
        commit('SET_NAME', data.nickname)
        // 保存cookie
        setToken(data.token)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // 获取用户角色信息
  getInfo({ commit }) {
    return new Promise((resolve, reject) => {
      getInfo().then(response => {
        const { data } = response

        if (!data) {
          return reject('验证失败，请重新登录')
        }
        const { roles } = data
        if (!roles || roles.length <= 0) {
          reject('getInfo: 角色属性为空')
        }
        commit('SET_ROLES', roles)
        commit('SET_NAME', data.name)
        commit('SET_AVATAR', data.avatar)
        commit('SET_ID', data.id);
        commit('SET_VOLUNTEER', data.volunteer);
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // 注销
  logout({ commit, state, dispatch }) {
    return new Promise((resolve, reject) => {
      logout(state.token).then(() => {
        removeToken() // 必须先移除token
        resetRouter()
        commit('SET_TOKEN', '')
        commit('SET_ROLES', [])
        commit('RESET_STATE')

        // dispatch('tagsView/delAllViews', null, { root: true })
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      removeToken() // must remove  token  first
      commit('RESET_STATE')
      resolve()
    })
  },

}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

