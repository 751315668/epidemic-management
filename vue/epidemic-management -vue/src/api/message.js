import request from '@/utils/request'

export function getMsgList(pageNum, pageSize) {
    return request({
        url: '/getMsgList',
        method: 'get',
        params: {
            pageNum: pageNum,
            pageSize: pageSize
        },
    })
}

export function publishMsg(data) {
    return request({
        url: '/publishMsg',
        method: 'post',
        data
    })
}

export function replyMsg(data) {
    return request({
        url: '/replyMsg',
        method: 'post',
        data
    })
}