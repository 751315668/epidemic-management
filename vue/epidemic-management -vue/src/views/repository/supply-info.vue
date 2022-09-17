<template>
    <el-container class="container">
        <el-header height="400px">
            <el-row class="title_img">
                <el-image :src="handleUrl(supply.imgUrl)" alt="" style="width: 200px; height: 200px"></el-image>
            </el-row>
            <el-row class="title_desc">
                <el-descriptions title="物资信息" class="title_desc_content">
                    <el-descriptions-item label="物品名">{{ supply.name }}</el-descriptions-item>
                    <el-descriptions-item label="物品类型">{{ supply.type }}</el-descriptions-item>
                    <el-descriptions-item label="剩余数量">
                        <el-tag size="medium">{{ supply.leftNum }}</el-tag>
                    </el-descriptions-item>
                    <el-descriptions-item label="说明">{{ supply.desc }}</el-descriptions-item>
                    <el-descriptions-item label="入库时间">
                        <el-tag size="medium">{{ supply.insertTime | dateFormat }}</el-tag>
                    </el-descriptions-item>
                </el-descriptions>
            </el-row>
        </el-header>

        <el-main class="main">
            <div class="detail_title">
                物资取用记录
            </div>
            <el-table :data="detailList" fit stripe style="width: 100%">
                <el-table-column align="center" type="index" width="80"></el-table-column>
                <el-table-column prop="name" label="姓名" width="120">
                </el-table-column>
                <el-table-column  align="center" prop="sex" label="性别" width="120">
                </el-table-column>
                <el-table-column  align="center" prop="phone" label="手机">
                </el-table-column><el-table-column  align="center" prop="phone" label="手机">
                </el-table-column>
                <el-table-column  align="center" label="数量">
                    <template slot-scope="scope">
                        <el-tag size="medium">{{ scope.row.cnt }} </el-tag>
                     </template>
                </el-table-column>
                <el-table-column  align="center" label="取用时间">
                    <template slot-scope="scope">
                       {{ scope.row.insertTime | dateFormat }} 
                    </template>
                </el-table-column>
            </el-table>

            <pagination v-show="total > 0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit"
                @pagination="getTakeInfoList" />
        </el-main>

    </el-container>
</template>

<script>
    import { getById, getTakeInfoList } from '@/api/supply'
    import { Message } from 'element-ui'
    import Pagination from '@/components/Pagination'

    export default {
        name: 'SpplyInfo',
        components: {
            'Pagination': Pagination
        },
        data() {
            return {
                id: null,
                supply: {},
                detailList: [],
                listQuery: {
                    page: 1,
                    limit: 10
                },
                total: 0
            }
        },
        created() {
            this.id = this.$route.params.id
            this.getSupply()
            this.getTakeInfoList()
        },
        mounted() {

        },
        methods: {
            getSupply() {
                getById(this.id).then(res => {
                    this.supply = res.data.supply
                }).catch(err => {
                    Message.warning({
                        type: 'warning',
                        message: '获取数据失败~'
                    })
                })
            },
            getTakeInfoList() {
                getTakeInfoList(this.listQuery.page, this.listQuery.limit, this.id).then(res => {
                    const { infoList } = res.data
                    this.total = infoList.total
                    this.detailList = infoList.list
                }).catch(err => {
                    Message.warning({
                        type: 'warning',
                        message: '获取数据失败~'
                    })
                })
            },
            handleUrl(imgName) {
                const url = 'http://localhost:8080/supplyimg/' + imgName
                return url
            },
        },

    }
</script>

<style scoped>
    .title_img {
        margin-top: 50px;
        text-align: center;
    }

    .title_desc {
        justify-content: center;
        align-items: center;
    }

    .title_desc_content {
        margin: 10px 50px;
        font-size: 16px;
        font-family: '微软雅黑';
    }

    .detail_title {
        text-align: center;
        margin-top: 20px;
        font-size: 20px;
        font-family: '微软雅黑';
        color: rgba(235, 13, 57, 0.966);
        letter-spacing: 0.1em;
    }
</style>