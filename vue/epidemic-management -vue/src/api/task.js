import request from '@/utils/request'

export function getTaskList(pageNum, pageSize) {
  return request({
    url: '/getTaskList',
    method: 'get',
    params: {
      pageNum: pageNum,
      pageSize: pageSize
    },
  })
}

export function publishTask(data) {
  return request({
    url: '/publishTask',
    method: 'post',
    data
  })
}

export function searchTask(pageNum, pageSize, status, input) {
  return request({
    url: '/searchTask',
    method: 'get',
    params: {
      pageNum,
      pageSize,
      status,
      input
    }
  })
}

export function searchById(id) {
  return request({
    url: '/searchById',
    method: 'get',
    params: {
      id
    }
  })
}

export function joinTask(data) {
  return request({
    url: '/joinTask',
    method: 'post',
    data
  })
}

export function getParticipant(id) {
  return request({
    url: '/getParticipant',
    method: 'get',
    params: {
      id
    }
  })
}

export function agreeTask(data) {
  return request({
    url: '/agreeTask',
    method: 'post',
    data
  })
}

export function accomplishTask(data) {
  return request({
    url: '/accomplishTask',
    method: 'post',
    data
  })
}
