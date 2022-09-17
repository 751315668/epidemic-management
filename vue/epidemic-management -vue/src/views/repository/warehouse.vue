<template>
    <el-container class="container">
        <!-- 条件筛选 -->
        <el-header height="80px">
            <el-row class="row">
                <el-col :span="16">
                    <el-autocomplete class="searchClass" v-model="input" :fetch-suggestions="querySearch"
                        placeholder="请输入物资名称">
                        <!-- 复合型输入框：将分类选择按钮前置 -->
                        <div slot="prepend">
                            <div class="centerClass">
                                <el-select @change="changeTips" class="select" v-model="type" clearable
                                    placeholder="请选择物资类型">
                                    <el-option v-for="(item, index) in types" :key="index" :label="item" :value="item">
                                    </el-option>
                                </el-select>
                                <div class="centerClass">
                                    <div class="line"></div>
                                </div>
                            </div>
                        </div>
                        <!-- 物资搜索 -->
                        <el-button slot="append" icon="el-icon-search" @click="handleSearch"></el-button>
                    </el-autocomplete>
                </el-col>
                <el-col :span="8">
                    <div class="supply_publish">
                        <el-button type="primary" icon="el-icon-plus" v-permission="['admin', 'volunteer']"
                            @click="openPublishDialog">
                            添加物资
                        </el-button>
                    </div>
                </el-col>
            </el-row>
        </el-header>


        <!-- 添加物资dialog -->
        <el-dialog title="物资添加" center :visible.sync="dialogFormVisible" width="70%">
            <el-form :model="supply" :rules="rules" ref="supplyForm" label-width="100px" class="ruleForm">
                <el-form-item label="物品名" prop="name" style="width: 300px; display: inline-block;">
                    <el-input v-model="supply.name" placeholder="请输入物品名"></el-input>
                </el-form-item>
                <el-form-item label="物资类型" prop="type" style="width: 300px; display: inline-block;">
                    <el-select v-model="supply.type" placeholder="请选择">
                        <el-option v-for="(item, index) in types" :key="index" :label="item" :value="item"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="物资数量" prop="quantity" style="width: 300px; display: inline-block;">
                    <el-input v-model.number="supply.quantity" placeholder="请输入物品数量">
                        <el-button slot="append" icon="el-icon-plus" @click.native="handlePlusTen">10</el-button>
                    </el-input>
                </el-form-item>
                <el-form-item label="物资说明" prop="desc" style="width: 700px;">
                    <el-input type="textarea" rows="4" v-model="supply.desc" placeholder="说明"></el-input>
                </el-form-item>
                <el-form-item label="物品图片">
                    <el-upload action="none" list-type="picture-card" :auto-upload="false" :multiple="false"
                        accept="image/jpeg,image/jpg,image/png" :on-preview="handlePictureCardPreview"
                        :on-change="handleChange" :before-upload="beforeUpload" :limit="1" :on-exceed="handleExceed">
                        <i class="el-icon-plus"></i>
                    </el-upload>
                </el-form-item>
                <!-- 图片预览弹出的dialog -->
                <el-dialog :visible.sync="dialogVisible" append-to-body>
                    <img width="100%" :src="dialogImageUrl" alt="">
                </el-dialog>
                <el-form-item style="text-align: center;">
                    <el-button type="primary" @click="submitForm('supplyForm')">添加</el-button>
                    <el-button @click="cancel">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>

        <!-- 列表展示 -->
        <el-main class="main">
            <el-table v-loading="listLoading" :data="list" @current-change="curChange" border fit highlight-current-row
                style="width: 100%">
                <!-- <el-table-column align="center" label="ID" width="80">
                    <template slot-scope="scope">
                        <span>{{ scope.row.id }}</span>
                    </template>
                </el-table-column> -->
                <el-table-column align="center" type="index" width="70">
                </el-table-column>
                <el-table-column align="center" label="名称" width="110">
                    <template slot-scope="scope">
                        <el-input v-show="scope.row.tableEdit" v-model="scope.row.name" placeholder="请输入内容"></el-input>
                        <span v-show="!scope.row.tableEdit">{{ scope.row.name }}</span>
                    </template>
                </el-table-column>
                <el-table-column align="center" label="种类" width="150">
                    <template slot-scope="scope">
                        <el-select v-show="scope.row.tableEdit" v-model="scope.row.type" placeholder="请选择">
                            <el-option v-for="(item, index) in types" :key="index" :label="item" :value="item">
                            </el-option>
                        </el-select>
                        <span v-show="!scope.row.tableEdit">{{ scope.row.type }}</span>
                    </template>
                </el-table-column>
                <el-table-column align="center" label="图片" width="180">
                    <template slot-scope="scope">
                        <el-popover v-if="scope.row.imgUrl" placement="top-start" title="" trigger="hover">
                            <img slot="reference" :src="handleUrl(scope.row.imgUrl)"
                                style="width: 90px; height: 90px" />
                            <!-- 图片弹出框 -->
                            <img :src="handleUrl(scope.row.imgUrl)" alt="" style="width: 200px; height: 200px" />
                        </el-popover>
                        <!-- <el-tag size="medium" v-else>暂无图片</el-tag> -->
                        <el-empty :image-size="80" v-else description="暂无图片"></el-empty>
                    </template>
                </el-table-column>
                <el-table-column align="center" label="说明">
                    <template slot-scope="scope">
                        <el-input v-show="scope.row.tableEdit" v-model="scope.row.desc" placeholder="请输入内容"></el-input>
                        <span v-show="!scope.row.tableEdit">{{ scope.row.desc }}</span>
                    </template>
                </el-table-column>
                <el-table-column align="center" label="总数" width="100">
                    <template slot-scope="scope">
                        <!-- 正则：将所有非数字内容替换为空字符串 -->
                        <el-input type="number" onkeyup="value=value.replace(/[^\d]/g, '')" v-show="scope.row.tableEdit"
                            v-model="scope.row.quantity" placeholder="请输入数量">
                        </el-input>
                        <span v-show="!scope.row.tableEdit">{{ scope.row.quantity }}</span>
                    </template>
                </el-table-column>
                <el-table-column align="center" label="剩余数量" width="100">
                    <template slot-scope="scope">
                        <span>{{ scope.row.leftNum }}</span>
                    </template>
                </el-table-column>
                <el-table-column align="center" label="动作" width="120">
                    <template slot-scope="scope">
                        <div v-permission="['admin', 'volunteer']">
                            <!-- <router-link :to="'/volunteer/edit/'+scope.row.id"> -->
                            <el-button type="primary" v-if="!scope.row.tableEdit" @click="openEdit(scope.row)"
                                size="small" icon="el-icon-edit">
                                编辑
                            </el-button>
                            <el-button type="primary" v-if="scope.row.tableEdit" @click="save(scope.row)" size="small"
                                icon="el-icon-edit">
                                保存
                            </el-button>
                            <!-- </router-link> -->
                        </div>
                        <div style="margin-top: 10px">
                            <router-link :to="'/repository/supplyInfo/'+scope.row.id">
                                <el-button type="primary" size="small" icon="el-icon-view">
                                    查看
                                </el-button>
                            </router-link>
                        </div>
                    </template>
                </el-table-column>
            </el-table>
            <div class="take" v-if="!isAdmin">
                <div>
                    <el-tag size="medium" class="take_title">物资取用</el-tag>
                </div>
                <div>
                    <el-input-number class="take_input" v-model.number="takeInput" placeholder="数量"></el-input-number>
                </div>
                <div>
                    <el-button class="take_btn" type="primary" @click="confirmTake" size="small" icon="el-icon-edit">
                        确定
                    </el-button>
                </div>
            </div>

            <pagination v-show="total > 0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit"
                @pagination="getList" />
        </el-main>

    </el-container>
</template>

<script>
    import Pagination from '@/components/Pagination'
    import permission from '@/directive/permission/index.js'
    import { Message } from 'element-ui'
    import { uploadSupplyImg, supplyTake, insertSupply, getSupplyList, updateSupply, getTypeList, getSearchList } from '@/api/supply'

    export default {
        name: 'Warehouse',
        components: {
            'Pagination': Pagination
        },
        directives: {
            permission
        },
        data() {
            return {
                total: 0,
                listLoading: true, // 是否显示加载中动画效果
                listQuery: {
                    page: 1,
                    limit: 10
                },
                dialogFormVisible: false,
                dialogVisible: false,
                dialogImageUrl: '',
                img: null,
                supply: {
                    name: '',
                    type: '',
                    quantity: null,
                    imgUrl: ''
                },
                isAdmin: false,
                take: {}, // 存储当前单击行
                takeInput: null, // 取用数量
                types: [
                    '口罩类',
                    '服装穿戴类',
                    '电子仪器类',
                    '消毒用品类',
                    '食品类',
                    '其他'
                ],
                rules: {
                    name: [
                        { required: true, message: '请输入物品名', trigger: 'blur' },
                    ],
                    type: [
                        { required: true, message: '请选择分类', trigger: 'change' }
                    ],
                    quantity: [
                        { type: 'number', required: true, min: 1, max: 10000, message: '请输入正确物品数量', trigger: 'change', transform: (value) => Number(value) },
                    ]
                },
                tips: [], // 输入提示
                type: '',
                input: '',
                // 防疫物资种类：电子物品类（各种测温仪）、服装类（防护服）、眼镜类（包括面罩）、口罩类、
                // 防护手套类、防护鞋类、消毒类（酒精，消毒液，来苏等）、消毒用具类（肩背式喷雾器，喷壶）
                list: [
                    // { id: 1, name: '口罩', type: '口罩类', quantity: 10, leftNum: 5, url: require('@/assets/supply/mask.jpg') },
                ],


            }
        },
        // computed: {
        // },
        // created() {
        // },
        mounted() {
            this.isAdmin = this.$store.getters.volunteer == undefined
            this.getList()
            // this.tips = this.loadAll()
        },
        methods: {
            getList() {
                return new Promise((resolve, reject) => {
                    getSupplyList(this.listQuery.page, this.listQuery.limit).then(res => {
                        const { info } = res.data
                        this.total = info.total
                        this.list = info.list
                        for (let i = 0, len = this.list.length; i < len; i++) {
                            this.$set(this.list[i], 'tableEdit', false)
                        }
                        let list = []
                        let len = this.list.length
                        for (let i = 0; i < len; i++) {
                            list[i] = { "value": this.list[i].name }
                        }
                        this.tips = list
                        this.listLoading = false
                    }).catch(err => {
                        Message.error({
                            type: 'error',
                            message: '请求失败~'
                        })
                    })
                })

            },
            // 返回搜索建议(参数：input框的输入值)
            querySearch(queryString, callback) {
                var tipList = this.tips;
                var results = queryString ? tipList.filter(this.createFilter(queryString)) : tipList;
                // console.log('resulits', results)
                // if (results.length == 0) {
                //     results.push()
                // }
                // 调用 callback 返回建议列表的数据至autocomplete组件
                callback(results);
            },
            // 过滤方法
            createFilter(queryString) {
                return (name) => {
                    return (name.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
                };
            },
            // 搜索建议：返回所选分类下的所有物品
            // loadAll() {
            //      return [
            //          { "value": "口罩" },
            //          { "value": "测温仪" },
            //          { "value": "防护服" },
            //      ]
            // },
            // 根据所选分类提供分类下的物品搜索建议
            changeTips(val) {
                getTypeList(this.listQuery.page, this.listQuery.limit, val).then(res => {
                    const { info } = res.data
                    let resList = info.list
                    let list = []
                    let len = resList.length
                    for (let i = 0; i < len; i++) {
                        list[i] = { "value": resList[i].name }
                    }
                    this.tips = list
                }).catch(err => {
                    Message.warning({
                        type: 'warning',
                        message: '获取失败~'
                    })
                })
            },
            openPublishDialog() {
                this.dialogFormVisible = true;
            },
            // 图片预览
            handlePictureCardPreview(file) {
                this.dialogImageUrl = file.url;
                this.dialogVisible = true;
            },
            handleChange(file, fileList) {
                this.img = file.raw;
            },
            // 上传图片前的检测
            beforeUpload(file) {
                const isJPG = file.type === 'image/jpeg' || 'image/jpg' || 'image/png';
                const isLt3M = file.size / 1024 / 1024 < 3;

                if (!isJPG) {
                    Message.warning({
                        type: 'warning',
                        message: '请选择图片~'
                    });
                }
                if (!isLt3M) {
                    Message.warning({
                        type: 'warning',
                        message: '图片最大为3MB~'
                    });
                }
                return isJPG && isLt3M;
            },
            // 超出图片上传限制(1)的钩子
            handleExceed(file, fileList) {
                Message.warning({
                    type: 'warning',
                    message: '已经有一张图片了~'
                })
            },
            submitForm(formName) {
                this.$refs[formName].validate(async (valid) => {
                    if (valid) {
                        try {
                            if (this.img) {
                                await this.step1()
                            }
                            await this.step2()
                            Message.success({
                                type: 'success',
                                message: '添加成功'
                            })
                            // Object.keys(this.ruleForm).forEach(key => {
                            //     this.ruleForm = ''
                            // })
                        } catch (e) {
                            Message.error({
                                type: "error",
                                message: '添加失败',
                            });
                        }
                        location.reload()
                    } else {
                        Message.warning({
                            type: "warning",
                            message: '请正确输入内容',
                        });
                    }
                });
            },
            // 上传物品图片
            step1() {
                return new Promise((resolve, reject) => {
                    let formData = new FormData()
                    formData.append('file', this.img);
                    uploadSupplyImg(formData).then(res => {
                        this.supply.imgUrl = res.data.imgName
                        resolve(res)
                    }).catch(err => {
                        reject(err)
                    })
                })
            },
            // 提交表单信息
            step2() {
                return new Promise((resolve, reject) => {
                    insertSupply(this.supply).then(res => {
                        resolve(res)
                    }).catch(err => {
                        reject(err)
                    })
                })
            },
            cancel() {
                this.dialogFormVisible = false;
            },
            handlePlusTen() {
                if (!this.supply.quantity || typeof (this.supply.quantity) == 'string') {
                    this.supply.quantity = 0
                }
                this.supply.quantity += 10
            },
            handleUrl(imgName) {
                const url = 'http://localhost:8080/supplyimg/' + imgName
                return url
            },
            openEdit(row) {
                row.tableEdit = true
            },
            // 物资信息编辑更新
            save(row) {
                console.log(row);
                row.tableEdit = false
                updateSupply(row).then(res => {
                    Message.success({
                        type: "success",
                        message: '保存成功!',
                    });
                }).catch(err => {
                    Message.error({
                        type: "error",
                        message: '保存失败~',
                    });
                })
                location.reload()
            },
            handleSearch() {
                getSearchList(this.listQuery.page, this.listQuery.limit, this.input, this.type).then(res => {
                    const { info } = res.data
                    this.total = info.total
                    this.list = info.list
                    for (let i = 0, len = this.list.length; i < len; i++) {
                        this.$set(this.list[i], 'tableEdit', false)
                    }
                    Message.success({
                        type: 'success',
                        message: '搜索成功!'
                    })
                }).catch(err => {
                    Message.warning({
                        type: 'warning',
                        message: '搜索失败~'
                    })
                })
            },
            curChange(val) {
                this.take = val
            },
            confirmTake() {
                if (Object.keys(this.take).length == 0) {
                    Message.warning({
                        type: 'warning',
                        message: '请选择物资~'
                    })
                } else if (!this.takeInput) {
                    Message.warning({
                        type: 'warning',
                        message: '请输入数量~'
                    })
                } else if (this.takeInput > this.take.leftNum) {
                    Message.warning({
                        type: 'warning',
                        message: '剩余数量不足~'
                    })
                } else {
                    let vol = this.$store.getters.volunteer
                    let data = {
                        volId: this.$store.getters.volunteer.id,
                        supplyId: this.take.id,
                        quantity: this.takeInput,
                        name: vol.name,
                        phone: vol.phone,
                        sex: vol.sex
                    }
                    supplyTake(data).then(res => {
                        Message.success({
                            type: 'success',
                            message: '取用成功!'
                        })
                        location.reload()
                    }).catch(err => {
                        Message.error({
                            type: 'error',
                            message: '取用失败~'
                        })
                    })
                }
            },

        }
    }
</script>

<style lang="scss" scoped>
    /* 类型选择框 */
    .searchClass {
        ::v-deep .el-input-group__prepend {
            border: none;
            background-color: transparent;
            padding: 0 10px 0 30px;
        }
    }

    .searchClass {
        ::v-deep .el-input-group__append {
            border: none;
            background-color: transparent;
        }
    }

    /* 输入框 */
    .searchClass {
        ::v-deep .el-input__inner {
            height: 36px;
            line-height: 36px;
            border: none;
            background-color: transparent;
        }
    }

    /* 搜索按钮 */
    .searchClass {
        ::v-deep .el-icon-search {
            font-size: 16px;
        }
    }

    .searchClass:hover {
        ::v-deep .el-icon-search {
            color: #65696d;
            font-size: 16px;
        }
    }

    .supply_publish {
        margin-left: 0%;
        margin-bottom: 20px;
    }

    .row {
        margin-top: 40px;
    }

    .select {
        width: 150px;
    }

    .input {
        width: 250px;
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

    .take {
        margin-left: 65%;
        display: inline-flex;
        justify-content: space-between;
    }

    .take_title {
        margin-top: 10px;
        font-family: '微软雅黑';
        font-size: 18px;
        letter-spacing: 0.1em;
    }

    .take_input {
        width: 150px;
        margin-top: 5px;
        margin-left: 10px
    }

    .take_btn {
        margin-top: 10px;
        margin-left: 10px;
    }
</style>