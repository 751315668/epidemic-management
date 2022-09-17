<template>
    <el-container class="container">
        <el-header :height="100 + 'px'">
            <h3 class="header">发布新闻 / 通知</h3>
        </el-header>

        <el-main class="main">
            <el-row class="title_row">
                <el-input class="title" v-model="form.title" placeholder="请输入标题">
                    <template slot="prepend">标题</template>
                </el-input>
                <el-input class="img" v-model="imgShowName" placeholder="上传缩略图">
                    <template slot="prepend">图片</template>
                    <el-button slot="append" icon="el-icon-plus" @click="handleImgSelect()">请选择</el-button>
                </el-input>
            </el-row>
            <div class="tinymce-editor">
                <editor :curValue="form.content" @input="newContent" v-model="form.content"></editor>
            </div>
            <div class="btn">
                <el-button type="primary" round @click="handlePublish()">立即发布</el-button>
            </div>
        </el-main>
        <!-- 点击图片上传按钮时触发 -->
        <input ref="imgInput" type="file" style="opacity: 0; pointer-events: none;" accept=".jpg,.png.jpeg"
            @change="upload($event)">
    </el-container>
</template>

<script>
    import Tinymce from "@/components/tinymce/index";
    import { publishNews, uploadImg } from '@/api/news'
    import { Message } from 'element-ui'
    import axios from 'axios'

    export default {
        name: 'NewsPublish',
        components: {
            'editor': Tinymce
        },
        data() {
            return {
                form: {
                    title: '',
                    content: '',
                    imgUrl: ''
                },
                imgShowName: '',
                img: null
            }
        },
        created() {

        },
        mounted() {

        },
        methods: {
            // tinymce富文本编辑器输入内容的事件
            newContent(val) {
                // console.log('val', val);
            },
            handleImgSelect() {
                let input = this.$refs.imgInput;
                input.click()
            },
            // 图片上传
            upload(e) {
                let file = e.target.files[0];
                this.imgShowName = file.name
                this.img = file
            },
            step1() {
                return new Promise((resolve, reject) => {
                    let form = new FormData();
                    form.append("file", this.img);
                    uploadImg(form).then(res => {
                        this.form.imgUrl = res.data.imgName
                        resolve(res)
                    }).catch(err => {
                        reject(err)
                    })
                })
            },
            step2() {
                return new Promise((resolve, reject) => {
                    publishNews(this.form).then(res => {
                        resolve(res)
                    }).catch(err => {
                        reject(err)
                    })
                })
            },
            async handlePublish() {
                try {
                    await this.step1()
                    await this.step2()
                    this.form.title = ''
                    this.form.content = ''
                    this.imgShowName = ''
                    Message.success({
                        type: 'success',
                        message: '发布成功'
                    })
                } catch (e) {
                    Message.error({
                        type: "error",
                        message: '发布失败',
                    });
                }
                // console.log('v1', v1);
                // console.log('v2', v2);
                // let form = new FormData();
                // form.append("file", this.img);
                // let r1 = await


                // Promise.all([r1, r2]).then(res => {
                //     console.log('resss', res)
                // })
                // publishNews(this.form).then(res => {
                //     console.log('res', res);
                // }).catch(err => {
                //     Message.error({
                //         type: "error",
                //         message: err || '发布失败',
                //     });
                // })
            },
        },

    }
</script>

<style scoped>
    .header {
        margin-top: 50px;
        text-align: center;
    }

    /* .main {
        text-align: center;
    } */

    .title_row {
        text-align: center;
        margin-bottom: 20px;
    }

    .title,
    .img {
        width: 400px;
    }

    .img {
        margin-left: 20px;
    }

    .tinymce-editor {
        height: 300px;
        width: 800px;
        margin: 0 auto;
        /* position: absolute;
        top: 0;
        bottom: 0;
        left: 0;
        right: 0;
        margin: auto; */
    }

    .btn {
        margin-top: 20px;
        text-align: center;
    }
</style>