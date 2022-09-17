<template>
    <el-container class="container">
        <el-header height="200px">
            <div class="row">
                <p>任务详情</p>
            </div>
            <el-steps :space="350" :active="handleNum(task.status)" finish-status="success" align-center>
                <el-step :title="handleTitle(task.status)" description="管理员审核"></el-step>
                <el-step title="进行中" description="任务进行中"></el-step>
                <el-step title="已完成" description="任务已完成"></el-step>
            </el-steps>
        </el-header>

        <el-main class="main">
            <el-descriptions class="margin-top" direction="vertical" title="志愿任务" :column="3" border>
                <template slot="extra">
                    <el-button type="primary" v-if="task.status < 2" @click="handleJoin()" v-permission="['volunteer']"
                        size="medium">加入任务</el-button>
                    <el-button type="primary" v-if="task.status == 0" @click="handleAccept()" v-permission="['admin']"
                        size="medium">同意任务</el-button>
                    <el-button type="primary" v-if="task.status == 1" @click="handleAccomplish()"
                        v-permission="['volunteer']" size="medium">确认完成</el-button>
                </template>
                <el-descriptions-item>
                    <template slot="label">
                        <i class="el-icon-s-flag"></i>
                        任务名
                    </template>
                    {{ task.name }}
                </el-descriptions-item>
                <el-descriptions-item>
                    <template slot="label">
                        <i class="el-icon-s-custom"></i>
                        已参与人数
                    </template>
                    <el-popover placement="top-start" :title="handleVolListTitle()" width="250" trigger="hover">
                        <el-tag size="medium" slot="reference"><a>{{ task.participantCnt }}</a></el-tag>
                        <div v-if="participantList.length > 0" v-for="vol in participantList">
                            <div>姓名：<el-tag size="small">{{ vol.name }}</el-tag>
                            </div>
                            <div>性别：{{ vol.sex }}</div>
                            <div>联系方式：{{ vol.phone }}</div>
                            <el-divider></el-divider>
                        </div>
                    </el-popover>
                </el-descriptions-item>
                <el-descriptions-item>
                    <template slot="label">
                        <i class="el-icon-magic-stick"></i>
                        状态
                    </template>
                    <el-tag size="medium" :type="handleStatus(task.status)">{{ task.status | statusFilter }}</el-tag>
                </el-descriptions-item>
                <el-descriptions-item span="3">
                    <template slot="label">
                        <i class="el-icon-chat-line-round"></i>
                        任务说明
                    </template>
                    {{ task.desc }}
                </el-descriptions-item>
                <el-descriptions-item span="1">
                    <template slot="label">
                        <i class="el-icon-user"></i>
                        发布人
                    </template>
                    <el-popover placement="top-start" title="说明" width="200" trigger="hover"
                        :content="handleContent(task.publisher)">
                        <el-tag size="medium" slot="reference"><a>{{ task.publisher }}</a></el-tag>
                        <div v-if="task.publisherId != 1">
                            <div>发布者：{{ publisher.name }}</div>
                            <div>性别：{{ publisher.sex }}</div>
                            <div>联系方式：{{ publisher.phone }}</div>
                        </div>
                    </el-popover>
                </el-descriptions-item>
                <el-descriptions-item>
                    <template slot="label">
                        <i class="el-icon-upload2"></i>
                        开始时间
                    </template>
                    <el-tag size="medium">{{ task.startTime | dateFormat }}</el-tag>
                </el-descriptions-item>
                <el-descriptions-item>
                    <template slot="label">
                        <i class="el-icon-download"></i>
                        结束时间
                    </template>
                    <el-tag size="medium">{{ task.endTime | dateFormat }}</el-tag>
                </el-descriptions-item>
            </el-descriptions>
        </el-main>
    </el-container>
</template>

<script>
    import { searchById, joinTask, getParticipant, agreeTask, accomplishTask } from '@/api/task'
    import { findVolById } from '@/api/volunteer'
    import { Message } from 'element-ui'
    import { mapGetters } from 'vuex'
    import permission from '@/directive/permission/index.js'

    export default {
        name: 'TaskDetail',
        components: {

        },
        directives: {
            permission
        },
        filters: {
            statusFilter(status) {
                const statusMap = {
                    '0': '审核中',
                    '1': '进行中',
                    '2': '已完成',
                    '3': '已取消'
                }
                return statusMap[status]
            }
        },
        data() {
            return {
                id: 0, // 当前任务ID
                task: {},
                publisher: {}, // 任务发布者
                participantList: [], // 参与任务志愿者列表 
            }
        },
        // computed: {
        //    
        // },
        created() {

        },
        async mounted() {
            this.id = this.$route.params.id
            await this.searchById()
            if (this.task.publisherId != 1) {
                await this.findPublisher()
            }
            await this.getParticipant()
        },
        methods: {
            searchById() {
                return searchById(this.id).then(res => {
                    if (res.data) {
                        this.task = res.data.task
                    }
                }).catch(err => {
                    Message.error({
                        type: 'error',
                        message: '查询失败~'
                    })
                })
            },
            // 根据任务发布者ID找到发布任务的志愿者
            findPublisher() {
                return findVolById(this.task.publisherId).then(res => {
                    if (res.data) {
                        this.publisher = res.data.volunteer
                    }
                }).catch(err => {
                    Message.error({
                        type: 'error',
                        message: '获取发布者失败~'
                    })
                })
            },
            // 根据任务id找到参与任务的志愿者
            getParticipant() {
                return getParticipant(this.id).then(res => {
                    if (res.data) {
                        this.participantList = res.data.volList
                    }
                }).catch(err => {
                    Message.error({
                        type: 'error',
                        message: '获取志愿者列表失败~'
                    })
                })
            },
            handleNum(num) {
                if (num >= 2) {
                    return 3
                }
                const map = {
                    '0': 0,
                    '1': 1,
                    '2': 2,
                    '3': 3
                }
                return map[num]
            },
            handleTitle(status) {
                const map = {
                    '0': '审核中',
                    '1': "审核完成",
                    '2': "审核完成",
                    '3': "审核完成"
                }
                return map[status]
            },
            handleStatus(status) {
                const statusMap = {
                    '0': 'info', // 审核中
                    '1': '', // 进行中
                    '2': 'success', // 已完成
                    '3': 'warning' // 已取消
                }
                return statusMap[status]
            },
            handleVolListTitle() {
                if (this.participantList.length > 0) {
                    return '已参与的志愿者'
                } else {
                    return '暂无参与的志愿者'
                }
            },
            handleContent(publisher) {
                if (this.task.publisherId == 1) {
                    return '该任务由管理员发布'
                } else return ''
            },
            // 加入任务
            handleJoin() {
                if (this.task.status == '0') {
                    Message.warning({
                        type: 'warning',
                        message: '任务审核中，无法加入~'
                    })
                } else {
                    // 当前登录的志愿者id
                    let curVolId = this.$store.getters.volunteer.id
                    let flag = false // 记录是否已经加入任务
                    for (let i = 0; i < this.participantList.length; i++) {
                        if (curVolId == this.participantList[i].id) {
                            flag = true
                        }
                    }
                    if (!flag) {
                        let map = {
                            taskId: this.task.id,
                            volId: curVolId
                        }
                        joinTask(map).then(res => {
                            if (res.code == 200) {
                                Message.success({
                                    type: 'success',
                                    message: '加入任务成功!'
                                })
                                location.reload()
                            }
                        }).catch(err => {
                            Message.error({
                                type: 'error',
                                message: '加入任务失败~'
                            })
                        })
                    } else {
                        Message.warning({
                            type: 'warning',
                            message: '不能重复加入任务~'
                        })
                    }
                }
            },
            // 同意任务
            handleAccept() {
                let map = {
                    taskId: this.task.id,
                }
                agreeTask(map).then(res => {
                    if (res.code == 200) {
                        Message.success({
                            type: 'success',
                            message: '同意任务成功!'
                        })
                    }
                    location.reload()
                }).catch(err => {
                    Message.error({
                        type: 'error',
                        message: '同意任务失败~'
                    })
                })
            },
            // 完成任务
            handleAccomplish() {
                let curVolId
                if (this.$store.getters.volunteer) {
                    curVolId = this.$store.getters.volunteer.id
                }
                let flag = false // 记录是否已经加入任务
                for (let i = 0; i < this.participantList.length; i++) {
                    if (curVolId == this.participantList[i].id) {
                        flag = true
                    }
                }
                // 未加入任务不能完成任务
                if (!flag) {
                    Message.warning({
                        type: 'warning',
                        message: '还未加入任务，无法进行操作~'
                    })
                } else {
                    if (this.task.status == '0') {
                        Message.warning({
                            type: 'warning',
                            message: '任务审核中，还未开始~'
                        })
                    } else {
                        let map = {
                            taskId: this.task.id,
                        }
                        accomplishTask(map).then(res => {
                            if (res.code == 200) {
                                Message.success({
                                    type: 'success',
                                    message: '操作成功!'
                                })
                            }
                            location.reload()
                        }).catch(err => {
                            Message.error({
                                type: 'error',
                                message: '操作失败~'
                            })
                        })
                    }
                }
            }
        },

    }
</script>

<style scoped>
    .row {
        text-align: center;
        margin-top: 30px;
        font-family: '微软雅黑';
        font-size: 24px;
        letter-spacing: 2px;
        color: rgba(235, 13, 57, 0.966)
    }
</style>