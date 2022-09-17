import request from '@/utils/request'

export function getVolunteerList(pageNum, pageSize) {
  return request({
    url: '/getVolunteerList',
    method: 'get',
    params: {
      pageNum: pageNum,
      pageSize: pageSize
    },
  })
}

export function searchVolunteerList(pageNum, pageSize, param) {
  return request({
    url: '/searchVolunteerList',
    method: 'get',
    params: {
      pageNum: pageNum,
      pageSize: pageSize,
      param: param
    },
  })
}

export function findVolById(id) {
  return request({
    url: '/findVolById',
    method: 'get',
    params: {
      id
    }
  })
}

export function insertVolunteer(data) {
  return request({
    url: '/insertVolunteer',
    method: 'post',
    data
  })
}

export function updateVolunteer(data) {
  return request({
    url: '/updateVolunteer',
    method: 'put',
    data
  })
}

export function removeVolunteer(id) {
  return request({
    url: '/removeVolunteer',
    method: 'get',
    params: {
      id
    }
  })
}

export function getApplyList(pageNum, pageSize) {
  return request({
    url: 'getApplyList',
    method: 'get',
    params: {
      pageNum: pageNum,
      pageSize: pageSize
    },
  })
}

export function acceptApply(id) {
  return request({
    url: 'acceptApply',
    method: 'put',
    data: {
      id: id
    }
  })
}

export function denyApply(id) {
  return request({
    url: 'denyApply',
    method: 'put',
    data: {
      id: id
    }
  })
}
