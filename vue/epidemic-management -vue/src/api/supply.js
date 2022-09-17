import request from '@/utils/request'

export function getSupplyList(pageNum, pageSize) {
  return request({
    url: '/getSupplyList',
    method: 'get',
    params: {
      pageNum: pageNum,
      pageSize: pageSize
    },
  })
}

export function getTypeList(pageNum, pageSize, type) {
  return request({
    url: '/getTypeList',
    method: 'get',
    params: {
      pageNum: pageNum,
      pageSize: pageSize,
      type
    },
  })
}

export function getSearchList(pageNum, pageSize, name, type) {
  return request({
    url: '/getSearchList',
    method: 'get',
    params: {
      pageNum: pageNum,
      pageSize: pageSize,
      name,
      type
    },
  })
}

export function getTakeInfoList(pageNum, pageSize, id) {
  return request({
    url: '/getTakeInfoList',
    method: 'get',
    params: {
      pageNum: pageNum,
      pageSize: pageSize,
      id
    },
  })
}

export function getById(id) {
  return request({
    url: '/getById',
    method: 'get',
    params: {
      id
    },
  })
}

export function supplyTake(data) {
  return request({
    url: '/supplyTake',
    method: 'post',
    data
  })
}

export function uploadSupplyImg(data) {
  return request({
    url: '/uploadSupplyImg',
    method: 'post',
    data
  })
}

export function insertSupply(data) {
  return request({
    url: '/insertSupply',
    method: 'post',
    data
  })
}

export function updateSupply(data) {
  return request({
    url: '/updateSupply',
    method: 'post',
    data
  })
}