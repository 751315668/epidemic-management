import request from '@/utils/request'

export function getNewsList(pageNum, pageSize) {
  return request({
    url: '/getNewsList',
    method: 'get',
    params: {
      pageNum: pageNum,
      pageSize: pageSize
    },
  })
}

export function publishNews(data) {
  return request({
    url: '/publishNews',
    method: 'post',
    data
  })
}

export function uploadImg(data) {
  return request({
    url: '/uploadImg',
    method: 'post',
    data
  })
}