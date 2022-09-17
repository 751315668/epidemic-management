<template>
    <el-container class="container">
        <!-- 顶部轮播图 -->
        <el-header id="banner" :height="460 + 'px'">
            <!-- 指示器在图片外部的轮播图 -->
            <el-carousel indicator-position="outside" :height="bannerHeight + 'px'">
                <el-carousel-item v-for="img in imgList" :key="img.id">
                    <el-image :src="img.url" :preview-src-list="[img.url]" :fit="'fill'"
                        style="width:100%; height: 100%;" alt="抱歉！图片暂时无法显示"></el-image>
                </el-carousel-item>
            </el-carousel>
        </el-header>

        <el-divider></el-divider>

        <p class="news-title">社&nbsp;区&nbsp;发&nbsp;布</p>

        <!-- 新闻列表 -->
        <el-main class="main">
            <div>
                <ul>
                    <li v-for="news in newsList" class="news-card">
                        <news-card :news="news" :key="news.id"></news-card>
                    </li>
                </ul>
            </div>
        </el-main>

        <pagination v-show="total > 0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit"
            @pagination="selectNews" />
    </el-container>
</template>

<script>
    import Pagination from '@/components/Pagination'
    import NewsCard from '@/views/news/component/news-card'
    import { getNewsList } from '@/api/news'
    import { Message } from 'element-ui'

    export default {
        name: 'NewsShow',
        components: {
            'news-card': NewsCard,
            Pagination
        },
        data() {
            return {
                total: 0,
                listQuery: {
                    page: 1,
                    limit: 10
                },
                newsList: [],
                imgList: [
                    { url: require('@/assets/news/banner1.jpg'), id: 'news01' },
                    { url: require('@/assets/news/news2.jpg'), id: 'news02' },
                    { url: require('@/assets/news/news3.jpg'), id: 'news03' },
                    { url: require('@/assets/news/news4.jpg'), id: 'news04' },
                ],
                // 轮播图父容器高度
                bannerHeight: 800,
                // 浏览器宽度
                screenWidth: 0,
            }
        },
        created() {
            this.selectNews()
        },
        mounted() {
            this.screenWidth = window.innerWidth
            this.setBannerSize()
            // 窗口大小改变时调用
            window.onresize = () => {
                this.screenWidth = window.innerWidth
                this.setBannerSize()
            }
        },
        methods: {
            setBannerSize() {
                this.bannerHeight = (600 / 1920) * this.screenWidth
                // console.log('bannerHeight', this.bannerHeight)
            },
            selectNews() {
                getNewsList(this.listQuery.page, this.listQuery.limit).then(res => {
                    const { info } = res.data
                    this.total = info.total
                    this.newsList = info.list
                }).catch(err => { 
                    Message.error({
                        type: 'error',
                        message: '请求失败~'
                    })
                })
            }
        },

    }
</script>

<style scoped>
    #banner {
        /* height: 800px;
        width: 900px; */

    }

    .news-card {
        height: 400px;
        width: 400px;
        margin-left: 50px;
        /* margin-right: 80px; */
        margin-bottom: 50px;

        display: flex;
        float: left;
        border: 1px solid rgb(183, 187, 188);
    }

    .main {
        overflow: scroll;
        height: 100%;
    }

    .container {
        height: 100%;
    }

    html,
    body {
        height: 100%;
    }

    .news-title {
        height: 10px;
        font-size: 24px;
        font-family: "微软雅黑";
        text-align: center;
        /* margin-top: 30px;
        margin-bottom: 30px; */

    }

    .el-carousel__item:nth-child(2n) {
        background-color: #99a9bf;
    }

    .el-carousel__item:nth-child(2n+1) {
        background-color: #d3dce6;
    }
</style>