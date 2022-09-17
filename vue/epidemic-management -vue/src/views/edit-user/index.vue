<template>
    <el-container>
        <el-header style="text-align: center;">
            <div class="title">修改信息</div>
        </el-header>

        <el-main>
            <el-form :model="user" ref="form" label-width="100px" class="form">
                <el-form-item label="登录账号" style="width: 300px; display: inline-block;">
                    <el-input v-model="user.name" ref="username" :placeholder="user.name" type="text" tabindex="1">
                    </el-input>
                </el-form-item>
                <el-form-item label="密码" style="width: 300px; display: inline-block;">
                    <el-input v-model="user.password" tabindex="2" :placeholder="user.password" :type="passwordType"
                        ref="password"></el-input>
                    <span class="show-pwd" @click="showPwd">
                        <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
                    </span>
                </el-form-item>
            </el-form>
            <div style="text-align: center;">
                <el-button type="primary" @click="submitForm">立即提交</el-button>
                <el-button @click="resetForm('form')">重置</el-button>
            </div>
        </el-main>
    </el-container>
</template>

<script>
    import { findById, updateUser } from '@/api/user'
    import { Message } from 'element-ui'

    export default {
        name: 'EditUser',
        data() {
            return {
                id: 0,
                user: {
                    name: '',
                    password: '',
                },
                passwordType: 'password',
            }
        },
        created() {
            this.id = this.$route.params.id
            this.findById(this.id)
        },
        methods: {
            findById(id) {
                findById(id).then(res => {
                    this.user = res.data.user
                }).catch(err => {
                    Message.error({
                        type: "error",
                        message: '获取用户失败',
                    });
                })
            },
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
            submitForm() {
                updateUser(this.user).then(res => {
                    Message.success({
                        type: "success",
                        message: '更新成功',
                    });
                    location.reload()
                }).catch(err => {
                    Message.error({
                        type: "error",
                        message: '更新失败',
                    });
                })
            },
            resetForm(formName) {
                // this.$refs[formName].resetFields();
                this.user.name = ''
                this.user.password = ''
            },
        },

    }
</script>

<style scoped>
    .title {
        margin-top: 80px;
        font-family: '微软雅黑';
        font-size: 24px;
        letter-spacing: 2px;
        color: rgba(15, 13, 13, 0.966)
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

    .form {
        margin-top: 80px;
        text-align: center;
    }
</style>