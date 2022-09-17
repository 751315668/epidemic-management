<template>
    <div class="app-container">
        <!-- 任务搜索框 -->
        <el-col :span="8" class="search">
            <!-- <el-input class="input-content" v-model="search" @focus="focus" @blur="blur"
                @keyup.enter.native="searchHandler" placeholder="搜索任务">
                <el-button slot="append" icon="el-icon-search" id="search" @click="searchHandler">
                </el-button>
            </el-input> -->

            <el-input placeholder="请输入任务名" v-model="inputContent" class="searchClass">
                <div slot="prepend">
                    <div class="centerClass">
                        <el-select v-model="selected" placeholder="请选择" clearable style="width: 110px">
                            <el-option v-for="(item, index) in selectList" :value="item.value" :label="item.label"
                                :key="index">
                            </el-option>
                        </el-select>
                    </div>
                    <div class="centerClass">
                        <div class="line"></div>
                    </div>
                </div>
                <el-button slot="append" icon="el-icon-search" @click="searchHandler"></el-button>
            </el-input>
        </el-col>

        <div class="task_publish">
            <el-button type="primary" icon="el-icon-plus" v-permission="['admin', 'volunteer']"
                @click="openPublishDialog">
                任务发布
            </el-button>
        </div>

        <!-- 任务发布dialog -->
        <el-dialog title="任务发布" center :visible.sync="dialogFormVisible" width="60%">
            <el-form :model="needPublishTask" :rules="rules" ref="ruleForm" label-width="100px" class="ruleForm">
                <el-form-item label="任务名" prop="name" style="width: 500px; display: inline-block;">
                    <el-input v-model="needPublishTask.name"></el-input>
                </el-form-item>
                <el-form-item label="任务时间" prop="pickedTime" label-width="100px">
                    <el-date-picker v-model="needPublishTask.pickedTime" :picker-options="pickerOptions"
                        type="datetimerange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="任务描述" prop="desc" style="width: 600px;">
                    <el-input type="textarea" v-model="needPublishTask.desc" rows="4" placeholder="说明"></el-input>
                </el-form-item>
                <el-form-item style=" margin-left: 180px;">
                    <el-button type="primary" @click="publishTask('ruleForm')">立即提交</el-button>
                    <el-button @click="handleCancel">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>

        <!-- 任务Table -->
        <el-table v-loading="listLoading" :data="list" border fit highlight-current-row style="width: 100%">
            <el-table-column align="center" type="index" width="70">
            </el-table-column>
            <el-table-column type="expand">
                <template slot-scope="scope">
                    <el-form label-position="center" inline class="demo-table-expand">
                        <el-form-item label="开始时间">
                            <el-tag size="medium">{{ scope.row.startTime | dateFormat }}</el-tag>
                        </el-form-item>
                        <el-form-item label="结束时间">
                            <el-tag size="medium">{{ scope.row.endTime | dateFormat }}</el-tag>
                        </el-form-item>
                    </el-form>
                </template>
            </el-table-column>
            <el-table-column width="140px" align="center" label="任务名">
                <template slot-scope="scope">
                    <span>{{ scope.row.name }}</span>
                </template>
            </el-table-column>
            <el-table-column width="90px" align="center" label="发布者">
                <template slot-scope="scope">
                    <span>{{ scope.row.publisher }}</span>
                </template>
            </el-table-column>
            <el-table-column width="100px" align="center" label="已参与人数">
                <template slot-scope="scope">
                    <span>{{ scope.row.participantCnt }}</span>
                </template>
            </el-table-column>
            <el-table-column class-name="status-col" align="center" label="状态" width="110">
                <template slot-scope="{row}">
                    <el-tag :type="row.status | statusFilter">
                        {{ row.status | statusFilter2 }}
                    </el-tag>
                </template>
            </el-table-column>
            <el-table-column min-width="200px" label="描述">
                <template slot-scope="{row}">
                    <span>{{ row.desc }}</span>
                </template>
            </el-table-column>
            <el-table-column align="center" label="动作" width="180">
                <template slot-scope="scope">
                    <!-- <div v-permission="['admin']">
                        <router-link :to="'/volunteer/edit/'+scope.row.id">
                            <el-button type="primary" size="small" icon="el-icon-edit">
                                编辑
                            </el-button>
                        </router-link>
                    </div> -->
                    <div style="margin-top: 10px">
                        <router-link :to="'/volunteer/detail/'+scope.row.id">
                            <el-button type="primary" size="small" icon="el-icon-view">
                                查看
                            </el-button>
                        </router-link>
                    </div>
                </template>
            </el-table-column>
        </el-table>

        <!-- 页面跳转工具栏 -->
        <pagination v-show="total > 0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit"
            @pagination="getList" />
    </div>
</template>

<script>
    import Pagination from '@/components/Pagination'
    import { getTaskList, publishTask, searchTask } from '@/api/task'
    import { Message } from 'element-ui'
    import permission from '@/directive/permission/index.js'

    export default {
        name: 'TaskInfo',
        components: {
            'Pagination': Pagination
        },
        directives: {
            permission
        },
        // 任务状态过滤器
        filters: {
            statusFilter(status) {
                const statusMap = {
                    '0': 'info', // 审核中
                    '1': '', // 进行中
                    '2': 'success', // 已完成
                    '3': 'warning' // 已取消
                }
                return statusMap[status]
            },
            statusFilter2(status) {
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
                needPublishTask: {
                    name: '',
                    publisher: '',
                    publisherId: 0,
                    participantCnt: 0,
                    status: '0',
                    desc: '',
                    pickedTime: [],
                },
                dialogFormVisible: false, // 任务发布dialog
                selected: '', // 单选栏所选的项
                inputContent: '', // 搜索栏
                selectList: [
                    { label: '全部', value: '' },
                    { label: '审核中', value: '0' },
                    { label: '进行中', value: '1' },
                    { label: '已完成', value: '2' }
                ],

                list: [], // 任务集合
                total: 0,
                listLoading: true, // 是否显示加载中动画效果
                listQuery: {
                    page: 1,
                    limit: 10
                },
                rules: {
                    name: [
                        { required: true, message: '请输入任务名', trigger: 'blur' },
                    ],
                    pickedTime: [
                        { required: true, message: '请选择任务时间', trigger: 'blur' },
                    ],
                    desc: [
                        { required: true, message: '请输入任务说明', trigger: 'change' }
                    ],
                },
                pickerOptions: {
                    // disabledDate(time) {
                    //     return time.getTime() < Date.now() - (24 * 60 * 60 * 1000);
                    // }
                },
            }
        },
        created() {
            this.getList()
        },
        methods: {
            // 当limit或page发生改变时触发
            getList() {
                getTaskList(this.listQuery.page, this.listQuery.limit).then(response => {
                    const { info } = response.data
                    this.total = info.total
                    this.list = info.list
                }).catch(err => {
                    Message.error({
                        type: 'error',
                        message: '请求失败~'
                    })
                })
                this.listLoading = false
            },
            searchHandler() {
                searchTask(this.listQuery.page, this.listQuery.limit, this.selected, this.inputContent).then(res => {
                    const { info } = res.data
                    this.total = info.total
                    this.list = info.list
                    Message.success({
                        type: 'success',
                        message: '查询成功'
                    })
                }).catch(err => {
                    Message.error({
                        type: 'error',
                        message: '查询失败~'
                    })
                })
            },
            openPublishDialog() {
                this.dialogFormVisible = true;
            },
            handleCancel() {
                this.dialogFormVisible = false
            },
            publishTask(formName) {
                this.$refs[formName].validate(async (valid) => {
                    if (valid) {
                        // let data = Object.assign({}, this.needPublishTask)
                        let date = this.needPublishTask.pickedTime
                        this.needPublishTask.startTime = Date.parse(date[0])
                        this.needPublishTask.endTime = Date.parse(date[1])
                        // 如果登录用户是志愿者
                        if (this.$store.getters.id != 1) {
                            this.needPublishTask.publisher = this.$store.getters.volunteer.name
                            this.needPublishTask.publisherId = this.$store.getters.volunteer.id
                        } else { // 否则任务发布者为管理员
                            this.needPublishTask.publisher = this.$store.getters.name
                            this.needPublishTask.publisherId = this.$store.getters.id
                        }
                        publishTask(this.needPublishTask).then(res => {
                            Message.success({
                                type: "success",
                                message: '发布成功',
                            });
                        }).catch(err => {
                            Message.error({
                                type: "error",
                                message: '发布失败~',
                            });
                        })
                        this.dialogFormVisible = false;
                        location.reload()
                    } else {
                        Message.warning({
                            type: "warning",
                            message: '请正确输入内容',
                        });
                    }
                })
            }
        },


    }
</script>

<style>
    /* .edit-input {
        padding-right: 100px;
    } */

    /* .cancel-btn {
        position: absolute;
        right: 15px;
        top: 10px;
    } */

    .demo-table-expand {
        font-size: 0;
    }

    .demo-table-expand label {
        width: 90px;
        color: #99a9bf;
    }

    .demo-table-expand .el-form-item {
        margin-right: 0;
        margin-bottom: 0;
        width: 50%;
    }

    .task_publish {
        /* position: fixed; */
        margin-left: 80%;
        margin-bottom: 10px;
    }

    .searchClass {
        border: 1px solid #c5c5c5;
        border-radius: 20px;
        background: #f4f4f4;
        margin-left: 10px;
        margin-bottom: 10px;
        font-weight: bolder;
        font-family: '微软雅黑';
        /* font-size: 14px; */
    }

    .searchClass .el-input-group__prepend {
        border: none;
        background-color: transparent;
        padding: 0 10px 0 30px;
    }

    .searchClass .el-input-group__append {
        border: none;
        background-color: transparent;
    }

    .searchClass .el-input__inner {
        height: 36px;
        line-height: 36px;
        border: none;
        background-color: transparent;
    }

    .searchClass .el-icon-search {
        font-size: 16px;
    }

    .searchClass .centerClass {
        height: 100%;
        line-height: 100%;
        display: inline-block;
        vertical-align: middle;
        text-align: right;
    }

    .searchClass .line {
        width: 1px;
        height: 26px;
        background-color: #c5c5c5;
        margin-left: 14px;
    }

    .searchClass:hover {
        border: 1px solid #7e7f80;
        background: rgb(255, 255, 255);
    }

    .searchClass:hover .line {
        background-color: #879599;
    }

    .searchClass:hover .el-icon-search {
        color: #65696d;
        font-size: 16px;
    }
</style>