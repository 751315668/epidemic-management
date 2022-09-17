<template>
    <div>
        <editor id="tinymce" v-model="value" :init="init"></editor>
    </div>
</template>

<script>
    //引入基础文件：
    import tinymce from 'tinymce/tinymce'
    import Editor from '@tinymce/tinymce-vue'
    import 'tinymce/themes/silver'
    import 'tinymce/plugins/image' // 插入上传图片插件
    // import 'tinymce/plugins/link' // 插入链接
    // import 'tinymce/plugins/code' // 插入代码
    import 'tinymce/plugins/preview' // 插入预览
    import 'tinymce/icons/default'

    import { postNews } from '@/api/news'
    export default {
        name: "tinymce",
        components: {
            Editor
        },
        props: {
            curValue: {
                type: String,
                default: ''
            }
        },
        data() {
            return {
                init: {
                    selector: "#tinymce",
                    language_url: '/tinymce/langs/zh_CN.js', // 语言包的路径
                    language: 'zh_CN', // 语言
                    skin_url: '/tinymce/skins/ui/oxide', // skin路径
                    height: 300, // 编辑器高度
                    branding: false, // 是否禁用“Powered by TinyMCE”
                    menubar: false, // 顶部菜单栏显示
                    elementpath: false, // 禁用编辑器底部的状态栏
                    paste_data_images: true, // 允许粘贴图像
                    plugins: ['image', 'preview'], // 引入插件
                    toolbar: ['formatselect | bold italic | alignleft aligncenter alignright alignjustify |bullist numlist outdent indent | lists media table | removeformat | preview'],
                    // 参数：图片数据、成功回调、失败回调
                    images_upload_handler: (blobInfo, success, failure) => {
                        let formdata = new FormData()
                        formdata.set('file', blobInfo.blob())
                        postNews(formdata).then(res => {
                            success("http://localhost:8080/newsimg/" + res.data)
                        }).catch(res => {
                            failure('error')
                        })
                    }
                },
                value: this.curValue
            }
        },
        mounted() {
            tinymce.init({})
        },
        watch: {
            // 动态传参
            curValue(newValue) {
                this.value = newValue
            },
            // 读取输入框内的数据
            value(newValue) {
                this.$emit('input', newValue)
            }
        }
    }
</script>

<style scoped>

</style>