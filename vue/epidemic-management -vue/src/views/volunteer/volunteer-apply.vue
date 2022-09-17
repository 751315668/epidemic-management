<template>
    <el-container class="container">

        <el-header class="header" height="170px">
            <el-row class="row">
                <span>申请成为志愿者</span>
            </el-row>
            <div class="login_title">登录账户</div>
            <el-form :model="loginForm" :rules="loginRules" ref="loginForm" label-width="100px" class="loginForm">
                <el-form-item label="登录账号" prop="name" style="width: 300px; display: inline-block; margin-left: 30px">
                    <el-input v-model="loginForm.name" ref="username" placeholder="请输入用户名" type="text" tabindex="1">
                    </el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password" style="width: 300px; display: inline-block;">
                    <el-input v-model="loginForm.password" tabindex="2" placeholder="请输入密码" :type="passwordType"
                        ref="password"></el-input>
                    <span class="show-pwd" @click="showPwd">
                        <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
                    </span>
                </el-form-item>
            </el-form>
        </el-header>

        <el-divider></el-divider>

        <!-- 申请表单 -->
        <el-main class="main">
            <div class="form_title">个人信息</div>
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="ruleForm">
                <!-- prop指定为待校验(rules)的字段名 -->
                <el-form-item label="姓名" prop="name" style="width: 300px; display: inline-block;">
                    <el-input v-model="ruleForm.name"></el-input>
                </el-form-item>
                <el-form-item label="年龄" prop="age" type="number" style="width: 290px; display: inline-block;">
                    <el-input v-model.number="ruleForm.age"></el-input>
                </el-form-item>
                <el-form-item label="性别" prop="sex" style="width: 300px; display: inline-block; margin-left: 20px;">
                    <el-radio-group v-model="ruleForm.sex">
                        <el-radio label="男"></el-radio>
                        <el-radio label="女"></el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="手机号码" prop="phone" style="width: 300px; display: inline-block;">
                    <el-input v-model="ruleForm.phone"></el-input>
                </el-form-item>
                <el-form-item label="政治面貌" prop="politicsStatus" style="display: inline-block;">
                    <el-select v-model="ruleForm.politicsStatus" placeholder="请选择">
                        <el-option label="共产党员" value="共产党员"></el-option>
                        <el-option label="共青团员" value="共青团员"></el-option>
                        <el-option label="群众" value="群众"></el-option>
                        <el-option label="其他" value="其他"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="是否接种疫苗" prop="vaccineStatus" label-width="130px" style="display: inline-block;">
                    <el-select v-model="ruleForm.vaccineStatus" placeholder="请选择">
                        <el-option label="已接种完成" value="已接种完成"></el-option>
                        <el-option label="已接种一针/两针" value="已接种一针/两针"></el-option>
                        <el-option label="未接种" value="未接种"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="居住住址" prop="detailedAddress" style="width: 1000px;">
                    <el-cascader :options="areas" v-model="preAddress" clearable></el-cascader>
                    <el-input v-model="ruleForm.detailedAddress" style="width: 600px;"></el-input>
                </el-form-item>
                <el-form-item label="个人说明" prop="desc" style="width: 1000px;">
                    <el-input type="textarea" v-model="ruleForm.desc" placeholder="其他说明"></el-input>
                </el-form-item>
                <el-form-item label="上传健康码">
                    <el-upload action="none" list-type="picture-card" :auto-upload="false" :multiple="false"
                        accept="image/jpeg,image/jpg,image/png" :on-preview="handlePictureCardPreview"
                        :on-change="handleChange" :before-upload="beforeUpload" :limit="1" :on-exceed="handleExceed">
                        <i class="el-icon-plus"></i>
                    </el-upload>
                </el-form-item>
                <!-- 图片预览弹出的dialog -->
                <el-dialog :visible.sync="dialogVisible">
                    <img width="100%" :src="dialogImageUrl" alt="">
                </el-dialog>
                <el-form-item style="text-align: center;">
                    <el-button type="primary" @click="submitForm('ruleForm')">立即提交</el-button>
                    <el-button @click="resetForm('ruleForm')">重置</el-button>
                </el-form-item>
            </el-form>
        </el-main>
    </el-container>
</template>

<script>
    // import { regionData } from 'element-china-area-data'
    import { Message } from 'element-ui'
    import { insertVolunteer } from '@/api/volunteer'
    import { uploadImg } from '@/api/news'
    import { checkName } from '@/api/user'

    export default {
        name: 'VolunteerApply',
        data() {
            // 自定义密码校验规则
            var validatePass = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入密码'));
                } else {
                    if (this.ruleForm.checkPass !== '') {
                        this.$refs.ruleForm.validateField('checkPass');
                    }
                    callback();
                }
            };
            return {
                // 账户表单数据
                loginForm: {
                    name: '',
                    password: ''
                },
                loginRules: {
                    name: [
                        { required: true, message: '请输入账号', trigger: 'blur' },
                        { validator: this.checkName, trigger: "blur" },
                    ],
                    password: [
                        { required: true, message: '请输入密码', trigger: 'blur' },
                    ]
                },
                passwordType: 'password',
                // 表单数据
                ruleForm: {
                    name: '',
                    age: '',
                    sex: '',
                    phone: '',
                    politicsStatus: '',
                    vaccineStatus: '',
                    detailedAddress: '', // 后缀输入的详细地址
                    imgUrl: '',
                    photoUrl: '',
                    desc: '',
                },
                img: null,
                preAddress: '', // 前缀选择的地址

                // 表单验证
                rules: {
                    name: [
                        { required: true, message: '请输入姓名', trigger: 'blur' },
                        { min: 2, max: 5, message: '长度在 2 到 5 个字符', trigger: 'blur' },
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
                    ]
                },
                // areas: regionData,

                // 区域选择数据
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
                dialogImageUrl: '',
                dialogVisible: false
            }


        },
        computed: {
            address() {
                return this.preAddress.toString().replace(/,/g, "") + this.ruleForm.detailedAddress;
            }
        },
        created() {

        },
        mounted() {

        },
        methods: {
            showPwd() {
                if (this.passwordType === 'password') {
                    this.passwordType = ''
                } else {
                    this.passwordType = 'password'
                }
                this.$nextTick(() => {
                    this.$refs.password.focus()
                })
            },
            // 用户账户重复性校验
            checkName(rule, value, callback) {
                if (value != '') {
                    checkName(value).then(res => {
                        if (res.msg == '验证通过') {
                            callback()
                        } else {
                            callback(new Error('用户名已存在'))
                        } 
                    })
                }
            },
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    this.$refs['loginForm'].validate(async (valid2) => {
                        if (valid && valid2) {
                            try {
                                await this.step1()
                                await this.step2()
                                Message.success({
                                    type: 'success',
                                    message: '发送成功'
                                })
                                // Object.keys(this.ruleForm).forEach(key => {
                                //     this.ruleForm = ''
                                // })
                            } catch (e) {
                                Message.error({
                                    type: "error",
                                    message: '发送失败',
                                });
                            }
                            location.reload()
                        } else {
                            Message.warning({
                                type: "warning",
                                message: '请正确输入内容',
                            });
                        }
                    })
                });
            },
            // 上传健康码图片
            step1() {
                return new Promise((resolve, reject) => {
                    let formData = new FormData()
                    formData.append('file', this.img);
                    uploadImg(formData).then(res => {
                        this.ruleForm.imgUrl = res.data.imgName
                        resolve(res)
                    }).catch(err => {
                        reject(err)
                    })
                })
            },
            // 提交表单信息
            step2() {
                return new Promise((resolve, reject) => {
                    let item = Object.assign({}, this.ruleForm)
                    item.detailedAddress = this.address
                    let random
                    if (item.sex == '男') {
                        random = this.getRandomNumber(1, 4)
                    } else {
                        random = this.getRandomNumber(5, 6)
                    }
                    item.photoUrl = 'h' + random + '.png'
                    let data = {
                        volunteer: item,
                        form: this.loginForm
                    }
                    insertVolunteer(data).then(res => {
                        resolve(res)
                    }).catch(err => {
                        reject(err)
                    })
                })
            },
            // 获取随机数
            getRandomNumber(min, max) {
                return parseInt(Math.random() * (max - min)) + min
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
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
            // 预览已上传图片时的钩子（弹出dialog）
            handlePictureCardPreview(file) {
                this.dialogImageUrl = file.url;
                this.dialogVisible = true;
            },
            // 参数：新选择的文件，已经选择过的文件列表
            handleChange(file, fileList) {
                this.img = file.raw;
            },
            // 超出图片上传限制(1)的钩子
            handleExceed(file, fileList) {
                Message.warning({
                    type: 'warning',
                    message: '已经有一张图片了~'
                })
            }
        },

    }
</script>

<style>
    .header {}

    .row {
        text-align: center;
        margin-top: 30px;
        font-family: '微软雅黑';
        font-size: 24px;
        letter-spacing: 2px;
        color: rgba(235, 13, 57, 0.966)
    }

    .ruleForm {
        /* margin-top: 100px; */
    }

    .loginForm {
        text-align: center;
    }

    .login_title {
        text-align: center;
        font-family: '微软雅黑';
        font-size: 18px;
        letter-spacing: 2px;
        margin-top: 30px;
        margin-bottom: 10px;
    }

    .form_title {
        margin-bottom: 20px;
        text-align: center;
        font-family: '微软雅黑';
        font-size: 18px;
        letter-spacing: 2px;
    }

    .show-pwd {
        position: absolute;
        right: 10px;
        top: 4px;
        font-size: 16px;
        color: #889aa4;
        cursor: pointer;
        user-select: none;
    }
</style>