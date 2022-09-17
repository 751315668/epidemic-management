<template>
    <el-container class="container">
        <!-- 条件筛选 -->
        <el-header height="80px">
            <el-row class="row">
                <el-col>
                    <el-input class="input" v-model="input" placeholder="请输入申请人"></el-input>
                    <el-select class="select" v-model="selected" placeholder="请选择类型">
                        <el-option label="已提交" value="已提交"></el-option>
                        <el-option label="进行中" value="进行中"></el-option>
                        <el-option label="已完成" value="已完成"></el-option>
                    </el-select>
                    <el-date-picker class="date-picker" v-model="date" type="daterange" align="center" unlink-panels
                        range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期"
                        :picker-options="pickerOptions">
                    </el-date-picker>
                    <el-button class="btn" type="primary" icon="el-icon-search">搜索</el-button>
                </el-col>
            </el-row>
        </el-header>

        <!-- 申请列表 -->
        <el-main class="main">
            <el-table v-loading="listLoading" :data="list" border fit highlight-current-row style="width: 100%">
                <el-table-column align="center" label="ID" width="80">
                    <template slot-scope="scope">
                        <span>{{ scope.row.id }}</span>
                    </template>
                </el-table-column>

                <el-table-column width="180px" align="center" label="申请人">
                    <template slot-scope="scope">
                        <span>{{ scope.row.applicantName }}</span>
                    </template>
                </el-table-column>

                <el-table-column width="120px" align="center" label="物品名">
                    <template slot-scope="scope">
                        <span>{{ scope.row.supply }}</span>
                    </template>
                </el-table-column>

                <el-table-column width="100px" align="center" label="申请数量">
                    <template slot-scope="scope">
                        <span>{{ scope.row.number }}</span>
                    </template>
                </el-table-column>

                <el-table-column class-name="status-col" align="center" label="状态" width="110">
                    <template slot-scope="{row}">
                        <el-tag :type="row.status | statusFilter">
                            {{ row.status }}
                        </el-tag>
                    </template>
                </el-table-column>

                <el-table-column min-width="300px" label="描述">
                    <template slot-scope="{row}">
                        <span>{{ row.desc }}</span>
                    </template>
                </el-table-column>

                <el-table-column align="center" label="动作" width="120">
                    <template slot-scope="scope">
                        <template>
                            <router-link :to="'/repository/applyInfo/' + scope.row.id">
                                <el-button type="primary" size="small" icon="el-icon-view">
                                    查看
                                </el-button>
                            </router-link>
                        </template>
                    </template>
                </el-table-column>
            </el-table>

            <pagination v-show="total > 0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit"
                @pagination="getList" />
        </el-main>

    </el-container>
</template>

<script>
    import Pagination from '@/components/Pagination'
    import permission from '@/directive/permission/index.js'

    export default {
        name: 'Apply',
        components: {
            'Pagination': Pagination
        },
        directives: {
            permission
        },
        filters: {
            statusFilter(status) {
                const statusMap = {
                    '已提交': 'success',
                    '进行中': 'warning',
                    '已完成': 'info'
                }
                return statusMap[status]
            }
        },
        data() {
            return {
                input: '',
                selected: '',
                listLoading: true,
                list: [],
                listQuery: {
                    page: 1,
                    limit: 20
                },
                total: 5,
                date: '',
                // 日期选择栏按钮
                pickerOptions: {
                    shortcuts: [{
                        text: '最近一周', // 按钮显示的文本
                        onClick(picker) { // 选中按钮后的回调，通过触发'pick'事件设置选择器的值
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                            picker.$emit('pick', [start, end]);
                        }
                    }, {
                        text: '最近一个月',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                            picker.$emit('pick', [start, end]);
                        }
                    }, {
                        text: '最近三个月',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                            picker.$emit('pick', [start, end]);
                        }
                    }]
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
                this.list = [
                    { id: '1', applicantName: '小明', supply: '口罩', number: 5, status: '已提交', desc: '供十一号楼使用' },
                    { id: '2', applicantName: '小红', supply: '测温仪', number: 5, status: '进行中', desc: '供门卫使用' },
                ]
                this.listLoading = false
            },
        },

    }
</script>

<style scoped>
    .row {
        margin-top: 40px;
        margin-bottom: 20px;
    }

    .select {
        width: 120px;
    }

    .input {

        width: 180px;
    }

    .date-picker {
        margin-left: 10px;
    }

    .btn {
        margin-left: 10px;
    }
</style>