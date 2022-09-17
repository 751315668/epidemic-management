<template>
    <!-- 新闻卡片组件 -->
    <div class="main-space">
        <!-- 点击弹出新闻详情Drawer -->
        <div class="img_div">
            <el-link @click="drawer = true">
                <!-- <el-image style="width: 200px; height: 200px" :src="'newsimg/' + news.imgUrl"></el-image> -->
                <img class="news_img" :src="handleImg(news.imgUrl)" />
            </el-link>
        </div>
        <div class="title_div" @click="drawer = true">
            <el-link @click="drawer = true">
                <p class="news_title">
                    {{news.title}}
                </p>
            </el-link>
        </div>
        <div class="desc_div" @click="drawer = true">
            <el-link @click="drawer = true">
                <p class="news_desc">
                    {{news.desc}}
                </p>
            </el-link>
        </div>

        <!-- 新闻详情Drawer -->
        <el-drawer class="new-drawer" :title="news.title" :visible.sync="drawer" :direction="direction"
            :close-on-press-escape="true" :size="'40%'">
            <!-- <span class="info">{{news.content}}</span> -->
            <div class="html_div" v-html="news.content"></div>
            <div class="time">{{news.createTime | dateFormat}}</div>
        </el-drawer>
    </div>
</template>

<script>
    export default {
        name: 'News-card',
        props: {
            news: Object
        },
        data() {
            return {
                id: null,
                // 打开新闻详情
                drawer: false,
                direction: 'rtl'
            }
        },
        created() {
            this.id = this.$route.params.id;
        },
        methods: {
            handleImg(imgName) {
                const url = 'http://localhost:8080/newsimg/' + imgName
                return url
            }
        }

    }
</script>

<style scoped>
    .main-space {
        width: 450px;
    }

    .news_img {
        /* width: 70%;
        height: 70%; */
        max-width: 100%;
        max-height: 100%;
        display: block;
        margin: auto;
    }

    .title_div {
        text-align: center;
    }

    .news_title {
        line-height: 1.5;
        font-size: 18px;
        font-family: 'Hiragino Sans GB';
    }

    .desc_div {
        margin: 0 20px;
    }

    .news_desc {
        line-height: 1.3;
        font-size: 16px;
        font-family: 'Hiragino Sans GB';
    }

    .info {
        margin-left: 20px;
        font-size: 18px;
        font-family: "微软雅黑";
        text-align: center;
    }

    /* 抽屉弹出框的文本 */
    .html_div {
        margin-left: 40px;
        width: 400px;
        font-size: 18px;
        word-break: break-all; /* 英文按字母换行 */
        font-family: '微软雅黑';
        line-height: 1.8;
        text-indent: 2em;
    }

    .html_div p {
        margin-right: 30px;
    }

    .time {
        text-align: right;
        margin-top: 30px;
        margin-right: 40px;
        font-size: 18px;
        font-family: '微软雅黑';
        line-height: 1.8;
    }
</style>