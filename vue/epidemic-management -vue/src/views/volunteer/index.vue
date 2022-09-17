<template>
    <el-container class="container">
        <el-header height="70px">
            <div>
                <el-row class="header_row">
                    <el-col :span="8" class="search">
                        <!-- 搜索框 -->
                        <el-input class="input-content" v-model="search" @focus="focus" @blur="blur"
                            @keyup.enter.native="searchHandler" placeholder="搜索志愿者">
                            <el-button slot="append" icon="el-icon-search" id="search" @click="searchHandler">
                            </el-button>
                        </el-input>
                        <!-- 设置z-index优先级 -->
                        <el-card @mouseenter="enterSearchBoxHanlder" v-if="isSearch" class="box-card" id="search-box"
                            style="position:relative; z-index:15">
                            <dl v-if="isHistorySearch">
                                <dt class="search-title" v-show="history">历史搜索</dt>
                                <dt class="remove-history" v-show="history">
                                    <a class="el-icon-delete" @click="removeHistory">清空历史记录</a>
                                </dt>
                                <a>
                                    <el-tag @click="addToSearch(search)" v-for="search in historySearchList"
                                        :key="search.id" closable :type="search.type" @close="closeHandler(search)"
                                        style="margin-right:5px; margin-bottom:5px;">{{search.name}}</el-tag>
                                </a>
                            </dl>
                        </el-card>
                    </el-col>
                    <div class="add_btn">
                        <el-button type="primary" icon="el-icon-user" v-permission="['admin']" @click="checkApplyList">
                            查看申请名单
                        </el-button>
                    </div>
                </el-row>
            </div>
        </el-header>

        <!-- 志愿者申请列表dialog -->
        <el-dialog title="申请列表" center :visible.sync="applyDialogFormVisible" width="60%">
            <div v-if="applyList.length > 0">
                <el-descriptions title="居民信息" v-for="(item, index) in applyList" :key="index">
                    <el-descriptions-item label="姓名">{{item.name}}</el-descriptions-item>
                    <el-descriptions-item label="年龄">{{item.age}}</el-descriptions-item>
                    <el-descriptions-item label="性别">{{item.sex}}</el-descriptions-item>
                    <el-descriptions-item label="手机">{{item.phone}}</el-descriptions-item>
                    <el-descriptions-item label="政治面貌">
                        <el-tag size="small">{{item.politicsStatus}}</el-tag>
                    </el-descriptions-item>
                    <el-descriptions-item label="疫苗接种状态">
                        <el-tag size="small">{{item.vaccineStatus}}</el-tag>
                    </el-descriptions-item>
                    <el-descriptions-item label="居住地址">{{item.detailedAddress}}</el-descriptions-item>
                    <el-descriptions-item label="个人说明">{{item.desc}}</el-descriptions-item>
                    <el-descriptions-item label="申请时间">{{item.insertTime | dateFormat}}</el-descriptions-item>
                    <el-descriptions-item label="健康码">
                        <el-image :src="handlePhotoImg(item.imgUrl)" style="width: 80%; height: 80%"
                            :preview-src-list="['http://localhost:8080/newsimg/' + item.imgUrl]"></el-image>
                    </el-descriptions-item>
                    <el-descriptions-item label="操作">
                        <el-button type="primary" icon="el-icon-user" size="small" @click="accept(item.id)">同意申请
                        </el-button>
                        <el-button type="info" icon="el-icon-user" size="small" @click="deny(item.id)">拒绝申请</el-button>
                    </el-descriptions-item>
                </el-descriptions>
                <el-pagination layout="prev, pager, next" :page-size="applyListQuery.limit"
                    :current-page="applyListQuery.page" @current-change="handleCurrentChange" :total="applyListTotal"
                    :small="true"></el-pagination>
            </div>
            <div class="info" v-else>暂无申请居民</div>
        </el-dialog>

        <!-- 修改志愿者信息dialog -->
        <el-dialog title="修改信息" center :visible.sync="modifyDialogFormVisible" width="60%">
            <el-form :model="needModifyVolunteer" :rules="rules" ref="ruleForm" label-width="100px" class="ruleForm">
                <!-- prop指定为待校验(rules)的字段名 -->
                <el-form-item label="姓名" prop="name" style="width: 300px; display: inline-block;">
                    <el-input v-model="needModifyVolunteer.name"></el-input>
                </el-form-item>
                <el-form-item label="年龄" prop="age" type="number" style="width: 290px; display: inline-block;">
                    <el-input v-model.number="needModifyVolunteer.age"></el-input>
                </el-form-item>
                <el-form-item label="性别" prop="sex" style="width: 300px; display: inline-block;">
                    <el-radio-group v-model="needModifyVolunteer.sex">
                        <el-radio label="男"></el-radio>
                        <el-radio label="女"></el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="手机号码" prop="phone" style="width: 300px; display: inline-block;">
                    <el-input v-model="needModifyVolunteer.phone"></el-input>
                </el-form-item>
                <el-form-item label="政治面貌" prop="politicsStatus" style="display: inline-block;">
                    <el-select v-model="needModifyVolunteer.politicsStatus" placeholder="请选择">
                        <el-option label="共产党员" value="共产党员"></el-option>
                        <el-option label="共青团员" value="共青团员"></el-option>
                        <el-option label="群众" value="群众"></el-option>
                        <el-option label="其他" value="其他"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="是否接种疫苗" prop="vaccineStatus" label-width="150px" style="display: inline-block;">
                    <el-select v-model="needModifyVolunteer.vaccineStatus" placeholder="请选择">
                        <el-option label="已接种完成" value="已接种完成"></el-option>
                        <el-option label="已接种一针/两针" value="已接种一针/两针"></el-option>
                        <el-option label="未接种" value="未接种"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="居住住址" prop="detailedAddress" style="width: 700px;">
                    <el-cascader :options="areas" v-model="preAddress" clearable></el-cascader>
                    <el-input v-model="needModifyVolunteer.detailedAddress" style="width: 320px;"></el-input>
                </el-form-item>
                <el-form-item label="个人说明" prop="desc" style="width: 600px;">
                    <el-input type="textarea" v-model="needModifyVolunteer.desc" placeholder="其他说明"></el-input>
                </el-form-item>
                <el-form-item label="头像" prop="photoUrl" label-width="80px" style="display: inline-block;">
                    <ul class="imgUl">
                        <li class="photo" :class="{'avatar': item.id == isSelect}" v-for="(item, index) in photoList"
                            :key="index">
                            <a>
                                <el-avatar shape="circle" :size="100" fit="fill" v-model="needModifyVolunteer.photoUrl"
                                    :src="handlePhotoImg(item.imgName)"
                                    @click.native="handlePhotoSelect(item.id, item.imgName)"></el-avatar>
                            </a>
                        </li>
                    </ul>
                </el-form-item>
                <el-form-item style=" margin-left: 180px;">
                    <el-button type="primary" @click="submitModify('ruleForm')">立即提交</el-button>
                    <el-button @click="handleCancel">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>

        <!-- 志愿者信息卡片列表 -->
        <el-main class="main">
            <el-row>
                <el-col :span="8" v-for="(volunteer, index) in volunteerList" :key="volunteer.id">
                    <el-card :body-style="{ padding: '0px' }">
                        <el-image class="image" :src="handleImg(volunteer.photoUrl)" :fit="'fill'"></el-image>
                        <div class="volInfo">
                            <span>{{volunteer.name}}</span>
                            <br />
                            <span>{{volunteer.sex}}</span>
                            <br />
                            <span>{{ volunteer.phone }}</span>
                            <br />
                            <el-tag size="medium">加入时间：{{volunteer.insertTime | dateFormat}}</el-tag>
                            <div class="bottom clearfix">
                                <el-button v-permission="['admin']" type="text" @click="modifyInfo(volunteer)"
                                    class="button">修改信息</el-button>
                            </div>
                            <div class="clearfix">
                                <el-button style="color: red" v-permission="['admin']" type="text"
                                    @click="openConfirm(volunteer)" class="button">移除志愿者</el-button>
                            </div>
                        </div>
                    </el-card>
                </el-col>
            </el-row>
        </el-main>
        <!-- 
        <pagination v-show="total > 0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit"
            @pagination="getList" /> -->
        <el-pagination @size-change="sizeChange" @current-change="currentChange" :page-size="listQuery.limit"
            :current-page="listQuery.page" layout="total, prev, pager, next, jumper" :total="total">
        </el-pagination>
    </el-container>
</template>

<script>
    import Pagination from '@/components/Pagination'
    import { getVolunteerList, getApplyList, updateVolunteer, removeVolunteer, acceptApply, denyApply, searchVolunteerList } from '@/api/volunteer'
    import permission from '@/directive/permission/index.js' // 权限判断指令
    import { Message } from 'element-ui'

    export default {
        name: 'VolunteerInfo',
        components: {
            Pagination
        },
        directives: {
            permission
        },
        data() {
            return {
                // 志愿者申请列表
                applyList: [],
                total: 0,
                applyListTotal: 0,
                listQuery: {
                    page: 1,
                    limit: 6
                },
                applyListQuery: {
                    page: 1,
                    limit: 2
                },
                applyDialogFormVisible: false, // 申请列表
                modifyDialogFormVisible: false, // 修改信息
                needModifyVolunteer: {}, // 保存待修改对象
                preAddress: '', // 前缀选择的地址
                // 志愿者信息列表
                volunteerList: [],
                photoList: [
                    { id: 1, imgName: 'h1.png' },
                    { id: 2, imgName: 'h2.png' },
                    { id: 3, imgName: 'h3.png' },
                    { id: 4, imgName: 'h4.png' },
                    { id: 5, imgName: 'h5.png' },
                    { id: 6, imgName: 'h6.png' },
                ],
                isSelect: false, // 修改志愿者信息时，记录所选择的头像id
                rules: {
                    name: [
                        { required: true, message: '请输入姓名', trigger: 'blur' },
                        { min: 2, max: 5, message: '长度在 2 到 5 个字符', trigger: 'blur' }
                    ],
                    age: [
                        // transform属性：给验证规则添加一个函数，使得原本string在验证之前转换为number
                        { type: 'number', required: true, min: 5, max: 70, message: '请正确输入年龄', trigger: 'blur', transform: (value) => Number(value) },
                    ],
                    sex: [
                        { required: true, message: '请选择性别', trigger: 'change' }
                    ],
                    phone: [
                        { required: true, message: '请输入手机号码', trigger: 'blur' },
                        { min: 11, max: 11, message: '请输入11位手机号码', trigger: 'blur' },
                        // 匹配以1开头且第二位是[3, 5, 7, 8, 9]任意一个的11位数字
                        { pattern: /^1[3|5|7|8|9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'change' }
                    ],
                    politicsStatus: [
                        { required: true, message: '请选择政治面貌', trigger: 'change' }
                    ],
                    vaccineStatus: [
                        { required: true, message: '请选择疫苗接种情况', trigger: 'change' }
                    ],
                    detailedAddress: [
                        { required: true, message: '请填写居住地址', trigger: 'blur' }
                    ],
                    photoUrl: [
                        { required: true, message: '请填写居住地址', trigger: 'blur' }
                    ]
                },
                areas: [{
                    value: '郑州市',
                    label: '郑州市',
                    children: [
                        {
                            value: '中原区',
                            label: '中原区',
                        },
                        {
                            value: '二七区',
                            label: '二七区',
                        },
                        {
                            value: '管城回族区',
                            label: '管城回族区',
                        },
                        {
                            value: '金水区',
                            label: '金水区',
                        },
                        {
                            value: '上街区',
                            label: '上街区',
                        },
                        {
                            value: '惠济区',
                            label: '惠济区',
                        },
                        {
                            value: '巩义市',
                            label: '巩义市',
                        },
                        {
                            value: '荥阳市',
                            label: '荥阳市',
                        },
                        {
                            value: '新密市',
                            label: '新密市',
                        },
                        {
                            value: '新郑市',
                            label: '新郑市',
                        },
                        {
                            value: '登封市',
                            label: '登封市',
                        },
                        {
                            value: '中牟县',
                            label: '中牟县',
                        },
                    ]
                }],
                localStorageKey: 'searchHistory',
                search: '', // 搜索输入框的值
                isFocus: false,
                historySearchList: [], // 历史搜索数据
                history: true, // 是否展示搜索历史
                types: ['', 'success', 'info', 'warning', 'danger'] // 搜索历史tag式样
            }
        },
        created() {

        },
        mounted() {
            this.getList()
            this.getApplyList(this.applyListQuery.page, this.applyListQuery.limit)
        },
        methods: {
            sizeChange(val) {
                this.listQuery.limit = val
            },
            // 当前页码改变时
            currentChange(val) {
                this.listQuery.page = val
                this.getList()
            },
            getList() {
                getVolunteerList(this.listQuery.page, this.listQuery.limit).then(res => {
                    const { info } = res.data
                    this.total = info.total
                    this.volunteerList = info.list
                }).catch(err => {
                    Message.error({
                        type: 'error',
                        message: '请求失败~'
                    })
                })
            },
            getApplyList(pageNum, pageSize) {
                getApplyList(pageNum, pageSize).then(res => {
                    const { info } = res.data
                    this.applyList = info.list
                    this.applyListTotal = info.total
                }).catch(err => {
                    Message.error({
                        type: 'error',
                        message: '请求失败~'
                    })
                })
            },
            getRandomNumber(min, max) {
                return parseInt(Math.random() * (max - min)) + min
            },
            // 保存搜索历史
            saveHistory(arr) {
                localStorage.setItem(this.localStorageKey, JSON.stringify(arr))
            },
            // 获得搜索历史
            loadHistory() {
                return JSON.parse(localStorage.getItem(this.localStorageKey))
            },
            removeHistory() {
                localStorage.removeItem(this.localStorageKey)
                this.isFocus = false
            },
            addToSearch(item) {
                this.search = item.name
            },
            focus() {
                this.isFocus = true;
                this.historySearchList =
                    this.loadHistory() == null ? [] : this.loadHistory();
                this.history = this.historySearchList.length == 0 ? false : true;
            },
            // 搜索框失焦时关闭展示的历史记录
            blur() {
                var self = this;
                // 设置定时器以取消点击搜索按钮后的关闭历史展示效果
                this.searchBoxTimeout = setTimeout(function () {
                    self.isFocus = false;
                }, 200);
            },
            enterSearchBoxHanlder() {
                clearTimeout(this.searchBoxTimeout);
            },
            searchHandler() {
                if (this.search.length > 0) {
                    // 随机生成搜索历史tag式样
                    let n = this.getRandomNumber(0, 5);
                    // 将不存在历史集合的输入内容加入至集合
                    let exist = this.historySearchList.filter(value => {
                        return value.name == this.search;
                    }).length == 0 ? false : true;
                    if (!exist) {
                        this.historySearchList.push({ name: this.search, type: this.types[n] });
                        this.saveHistory(this.historySearchList);
                    }
                    this.history = this.historySearchList.length == 0 ? false : true;
                    clearTimeout(this.searchBoxTimeout)
                }

                searchVolunteerList(this.listQuery.page, this.listQuery.limit, this.search).then(res => {
                    const { info } = res.data
                    this.total = info.total
                    this.volunteerList = info.list
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
            closeHandler(search) {
                // 从历史记录中删除一项
                this.historySearchList.splice(this.historySearchList.indexOf(search), 1);
                this.saveHistory(this.historySearchList);
                // 取消定时器
                // clearTimeout(this.searchBoxTimeout);
                if (this.historySearchList.length == 0) {
                    this.history = false;
                }
            },
            // 打开申请志愿者列表dialog
            checkApplyList() {
                this.applyDialogFormVisible = true;
            },
            handleImg(imgName) {
                const url = 'http://localhost:8080/photo/' + imgName
                return url
            },
            handlePhotoImg(imgName) {
                const url = 'http://localhost:8080/newsimg/' + imgName
                return url
            },
            async accept(id) {
                await acceptApply(id).then(res => {
                    Message.success({
                        type: 'success',
                        message: '操作成功~'
                    })
                }).catch(err => {
                    Message.error({
                        type: 'error',
                        message: '操作失败~'
                    })
                })
                this.applyDialogFormVisible = false
                this.getApplyList(this.applyListQuery.page, this.applyListQuery.limit)
                location.reload()
            },
            async deny(id) {
                await denyApply(id).then(res => {
                    Message.success({
                        type: 'success',
                        message: '操作成功~'
                    })
                }).catch(err => {
                    Message.error({
                        type: 'error',
                        message: '操作失败~'
                    })
                })
                this.applyDialogFormVisible = false
                this.getApplyList(this.applyListQuery.page, this.applyListQuery.limit)
            },
            modifyInfo(volunteer) {
                this.modifyDialogFormVisible = true
                this.needModifyVolunteer = Object.assign({}, volunteer)
                let add = volunteer.detailedAddress.split(/[市县区]/)
                let preAdd = Array.from(add)
                this.needModifyVolunteer.detailedAddress = preAdd[2]
                this.isSelect = this.needModifyVolunteer.photoUrl[1]
            },
            openConfirm(volunteer) {
                this.$confirm('确定移除志愿者吗?', '请确认', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    removeVolunteer(volunteer.id).then(res => {
                        Message.success({
                            type: "success",
                            message: '移除成功',
                        });
                    }).catch(err => {
                        Message.error({
                            type: "error",
                            message: '移除失败',
                        });
                    })
                    location.reload()
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '取消删除'
                    });
                })
            },
            submitModify(formName) {
                this.$refs[formName].validate(async (valid) => {
                    if (valid) {
                        let item = Object.assign({}, this.needModifyVolunteer)
                        item.detailedAddress = this.address
                        await updateVolunteer(item).then(res => {
                            Message.success({
                                type: "success",
                                message: '更新成功',
                            });
                        }).catch(err => {
                            Message.error({
                                type: "error",
                                message: '更新失败',
                            });
                        })
                        this.modifyDialogFormVisible = false;
                        location.reload()
                    } else {
                        Message.warning({
                            type: "warning",
                            message: '请正确输入内容',
                        });
                    }
                });
            },
            handleCancel() {
                this.modifyDialogFormVisible = false
            },
            // 修改时的头像选择
            handlePhotoSelect(id, imgName) {
                if (this.isSelect == id) { // 点击的头像已是被选择的时
                    this.isSelect = !this.isSelect
                } else { // 若不是，保存所选择的头像id
                    this.isSelect = id
                }
                this.needModifyVolunteer.photoUrl = imgName
            },
            // 申请列表分页
            handleCurrentChange(val) {
                this.getApplyList(val, this.applyListQuery.limit)
            }
        },
        computed: {
            isHistorySearch() {
                return this.isFocus;
            },
            isSearchList() {
                return this.isFocus && this.search;
            },
            isSearch() {
                return this.isFocus;
            },
            address() {
                return this.preAddress.toString().replace(/,/g, "") + this.needModifyVolunteer.detailedAddress;
            }
        }

    }
</script>

<style scoped>
    /* .title {
        font-size: 20px;
        font-family: '微软雅黑';
        margin-top: 10px;
        margin-left: 100px;
        margin-bottom: 100px;
    } */

    .header_row {
        position: relative;
    }

    /* input-content {
        margin-top: 20px;
    } */

    .search {
        position: absolute;
        margin: auto;
        margin-top: 30px;
        top: 0;
        left: 0;
        bottom: 0;
        right: 0;
    }

    .add_btn {
        /* position: fixed; */
        margin-left: 75%;
        margin-top: 30px;
    }

    #search {
        background-color: #E4E7ED;
        border-radius: 10%;
    }

    /* 
    .search-title {
        color: #bdbaba;
        font-size: 15px;
        margin-bottom: 20px;
    } */

    .remove-history {
        color: #bdbaba;
        font-size: 15px;
        float: right;
        margin-top: -35px;
    }

    #search-box {
        width: 555px;
        height: 300px;
        margin-top: 0px;
        padding-bottom: 20px;
    }

    /*******************/

    .time {
        font-size: 13px;
        color: #999;
    }

    .bottom {
        margin-top: 13px;
        line-height: 12px;
    }

    .image {
        margin-top: 15px;
        margin-left: 40px;
        width: 80%;
        display: block;
    }

    .clearfix:before,
    .clearfix:after {
        display: table;
        content: "";
    }

    .clearfix:after {
        clear: both
    }

    .volInfo {
        padding: 14px;
        text-align: center;
    }

    .volInfo span {
        margin-top: 15px;
    }

    .info {
        font-family: '微软雅黑';
        font-size: 20px;
        text-align: center;
    }

    .imgUl {
        list-style-type: none;
    }

    .imgUl li {
        float: left;
    }

    .photo {
        border: 1px solid #dcdfe6;
        margin: 5px 5px;
    }

    .photo:hover {
        border: 1px solid black;
    }

    .photo:focus,
    .avatar {
        border: 1px solid black;
    }
</style>