<template>
    <el-container class="container">
        <!-- 留言栏 -->
        <el-header height="380px">
            <div class="msg_form">
                <div class="chl_box">
                    <el-row class="title">
                        <el-col :span="20">
                            <h3>留言板</h3>
                        </el-col>
                        <el-col :span="4">
                            <h3>共 {{ total }} 条留言</h3>
                        </el-col>
                    </el-row>

                    <el-divider class="divider"></el-divider>

                    <div class="info_input">
                        <el-row>
                            <el-input class="msg-input" type="textarea" :rows="5" :placeholder="placeholder"
                                v-model="msg.content">
                            </el-input>
                        </el-row>
                        <el-row>
                            <!-- <el-col :span="10" class="name_input">
                                <el-input v-model="name" prefix-icon="el-icon-user" placeholder="请输入昵称">
                                </el-input>
                            </el-col>
                            <el-col :span="10" class="phone_input">
                                <el-input v-model="phone" prefix-icon="el-icon-phone-outline" placeholder="请输入联系方式">
                                </el-input>
                                </el-input>
                            </el-col> -->
                            <el-col :span="4" class="btn">
                                <el-button type="primary" @click="submitMsg">提交留言</el-button>
                            </el-col>
                        </el-row>
                    </div>
                </div>
            </div>
        </el-header>

        <!-- 留言展示 -->
        <el-main class="main">
            <div class="msg_show">
                <div class="msg_show_chl">
                    <el-row class="title">
                        <el-col :span="20">
                            <h3>留言详情</h3>
                        </el-col>
                    </el-row>

                    <el-divider class="divider"></el-divider>

                    <div class="comment_container">
                        <!-- <div v-if style="padding: 10px 0; text-align: center;">
                            还没有人来留言
                        </div> -->
                        <div class="comment" v-for="(item, index) in msgList" :key="index">
                            <div>
                                <!-- 头像 -->
                                <el-col :span="8">
                                    <el-image class="user_img" :src="handleImg(item.imgUrl)" :fit="'fill'"></el-image>
                                </el-col>
                                <el-row>
                                    <el-col :span="6" class="user_name">{{item.name}}</el-col>
                                    <el-col :span="5" class="time">{{item.insertTime | dateFormat}}</el-col>
                                    <el-col :span="1">
                                        <el-tag type="info" @click="getMsgId(item)" style="margin-top: 10px;"><a>回复</a>
                                        </el-tag>
                                    </el-col>
                                    <div class="content">
                                        <p>{{item.content}}</p>
                                    </div>
                                </el-row>
                                <!-- <el-row >
                                </el-row> -->
                            </div>
                            <!-- 二级留言 -->
                            <el-row>
                                <div class="chl_comment" v-for="(child, index) in item.childrenList" :key="index">
                                    <el-row>
                                        <el-col :span="4">
                                            <el-image class="chl_user_img" :src="handleImg(child.imgUrl)" :fit="'fill'">
                                            </el-image>
                                        </el-col>
                                        <el-col :span="6" class="chl_user_name">{{child.name}}</el-col>
                                        <el-col :span="6" class="chl_time">{{child.insertTime | dateFormat}}</el-col>
                                        <div class="chl_content">
                                            <p>{{child.content}}</p>
                                        </div>
                                    </el-row>
                                </div>
                            </el-row>
                        </div>
                    </div>
                </div>
            </div>
        </el-main>

        <pagination v-show="total > 0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit"
            @pagination="getList" />
    </el-container>
</template>

<script>
    import Pagination from '@/components/Pagination'
    import { getMsgList, publishMsg, replyMsg } from '@/api/message'
    import { Message } from 'element-ui'

    export default {
        name: 'Message',
        components: {
            Pagination
        },
        data() {
            return {
                msgList: [],
                messageInput: '',
                msg: {
                    parent: null,
                    userId: null,
                    name: '',
                    content: '',
                    imgUrl: '',
                },
                placeholder: '请输入内容',
                total: 0,
                listQuery: {
                    page: 1,
                    limit: 10
                },
            }
        },
        created() {
            this.getList()
        },
        mounted() {

        },
        methods: {
            getList() {
                getMsgList(this.listQuery.page, this.listQuery.limit).then(res => {
                    const { info } = res.data
                    this.total = info.total
                    this.msgList = info.list
                }).catch(err => {
                    Message.error({
                        type: 'error',
                        message: '请求失败~'
                    })
                })
            },
            handleImg(imgName) {
                const url = 'http://localhost:8080/photo/' + imgName
                return url
            },
            submitMsg() {
                let str = this.msg.content
                if (str == null || str == undefined || str == '') {
                    Message.warning({
                        type: 'warning',
                        message: '请输入内容~'
                    })
                } else {
                    if (this.$store.getters.name == '游客') {
                        this.msg.userId = 0
                        this.msg.name = '游客'
                    } else if (this.$store.getters.name == '管理员') {
                        this.msg.userId = 1
                        this.msg.name = '管理员'
                    } else {
                        this.msg.userId = this.$store.getters.volunteer.id
                        this.msg.name = this.$store.getters.volunteer.name
                    }
                    this.msg.imgUrl = this.$store.getters.avatar
                    publishMsg(this.msg).then(res => {
                        Message.success({
                            type: 'success',
                            message: '留言成功!'
                        })
                    }).catch(err => {
                        Message.error({
                            type: 'error',
                            message: '留言失败~'
                        })
                    })
                    location.reload()
                }
            },
            // 回复消息：保存所要回复的消息id
            getMsgId(item) {
                this.msg.parent = item.id
                this.placeholder = "回复 " + item.name;
            }

        },

    }
</script>

<style scoped>
    .el-col {
        border-radius: 4px;
    }

    .msg_form {
        width: 900px;
        /* height: 350px; */
        height: auto;
        margin-top: 30px;
        margin-left: 70px;
        border: 1px solid #000000;
        position: relative;
        background: #f6f4f4;
    }

    .chl_box {
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        /* margin: auto; */
    }

    .info_input {
        margin-bottom: 40px;
    }

    .divider {
        margin-top: 10px;

    }

    .title {
        text-align: left;
        margin-top: 10px;
        margin-bottom: 10px;
        margin-left: 30px;
        font-family: 'Franklin Gothic Medium', 'Arial Narrow', Arial, sans-serif;
        color: #409EFF;
    }

    /* .comment_container {
        border-top: 2px solid #00b5ad !important;
    } */

    .msg-input {
        width: 800px;
        margin-left: 40px;
        font-size: 16px;
    }

    .name_input {
        margin-top: 20px;
        margin-left: 40px;
        width: 250px;
    }

    .phone_input {
        margin-top: 20px;
        margin-left: 30px;
        width: 250px;
    }

    .btn {
        margin-top: 20px;
        margin-left: 45%;

        width: 250px;
    }

    .msg_show {
        width: 900px;
        height: auto;
        margin-top: 30px;
        margin-left: 70px;
        border: 1px solid #000000;
        position: relative;
        background: #f6f4f4;
    }

    .msg_show_chl {
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        margin: auto;
    }

    .comment,
    .chl_comment {
        margin-left: 20px;
        margin-right: 20px;
        margin-bottom: 20px;
    }

    .comment {
        border: 1px solid #000000;
        border-top: 2px solid #409EFF !important;
    }

    .chl_comment {
        margin-top: 40px;
        box-sizing: inherit;
    }

    .user_img,
    .chl_user_img {
        height: 30%;
        width: 30%;
        margin-left: 5px;
        margin-top: 5px;
    }

    .chl_user_img {
        margin-left: 30px;
    }

    .user_name,
    .chl_user_name {
        margin-top: 10px;
        margin-left: -150px;
        font-family: '微软雅黑';
        font-size: 20px;
        /* color: lightskyblue */
    }

    .chl_user_name {
        margin-left: -20px;
    }

    .time,
    .chl_time {
        margin-top: 10px;
        margin-left: -120px;
        font-family: '微软雅黑';
        font-size: 16px;
        color: rgba(26, 29, 30, 0.4);
        display: inline-block;
    }

    .chl_time {
        margin-left: -100px;
    }

    /* .content,
    .chl_content {
        margin-top: 30px;
        margin-left: -300px;
        font-size: 1em;
        word-wrap: break-word;
    } */

    .content {
        margin-bottom: 10px;
        margin-top: 35px;
        margin-left: -300px;
        display: inline-block;
        font-family: '微软雅黑';
        font-size: 18px;
    }

    .chl_content {
        margin-top: 50px;
        margin-left: -300px;
        font-family: '微软雅黑';
        font-size: 18px;
    }
</style>