import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/login',
    method: 'post',
    data
  })
}

export function getInfo() {
  return request({
    url: '/getUserInfo',
    method: 'get',
  })
}

export function logout() {
  return request({
    url: '/logout',
    method: 'post'
  })
}

export function updateUser(data) {
  return request({
    url: '/updateUser',
    method: 'post',
    data
  })
}

export function findById(id) {
  return request({
    url: '/findById',
    method: 'get',
    params: {
      id
    }
  })
}

export function checkName(name) {
  return request({
    url: '/checkName',
    method: 'get',
    params: {
      name
    }
  })
}

export function getChartData() {
  return request({
    url: '/getChartData',
    method: 'get',
  })
}
