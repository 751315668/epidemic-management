<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
  import echarts from 'echarts'
  require('echarts/theme/macarons') // echarts theme
  import resize from './mixins/resize'

  export default {
    mixins: [resize],
    props: {
      className: {
        type: String,
        default: 'chart'
      },
      width: {
        type: String,
        default: '100%'
      },
      height: {
        type: String,
        default: '350px'
      },
      autoResize: {
        type: Boolean,
        default: true
      },
      chartData: {
        type: Object,
        required: true
      },
      xData: {
        type: Object,
        required: true
      }
    },
    data() {
      return {
        chart: null
      }
    },
    watch: {
      chartData: {
        deep: true,
        handler(val) {
          this.setOptions(val, this.xData)
        }
      },
      xData: {
        // deep: true,
        handler(newVal, oldVal) {
          this.setOptions(this.chartData, newVal)
        }
      }
    },
    // created() {
    //   this.initChart()
    // },
    mounted() {
      this.$nextTick(() => {
        this.initChart()
      })
    },
    beforeDestroy() {
      if (!this.chart) {
        return
      }
      this.chart.dispose()
      this.chart = null
    },
    methods: {
      initChart() {
        // 初始化echarts
        this.chart = echarts.init(this.$el, 'macarons')
        this.setOptions(this.chartData, this.xData)
      },
      setOptions({ expectedData }, { data } = {}) {
        this.chart.setOption({
          xAxis: { // x轴
            // data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
            data: data, // 底部标题
            boundaryGap: true, // 坐标轴两侧空白策略
            axisTick: { // 坐标轴刻度
              show: false
            }
          },
          grid: { // 主体网格区域
            left: 30, // margin
            right: 30,
            bottom: 20,
            top: 30,
            containLabel: true // 是否包含坐标轴的刻度标签
          },
          tooltip: {
            trigger: 'axis', // 坐标轴指示器，坐标轴触发有效
            axisPointer: {
              type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
            },
            padding: [5, 10]
          },
          yAxis: { // y轴
            axisTick: { // 坐标轴轴线
              show: false
            }
          },
          legend: {
            data: ['数量']
          },
          series: [{
            name: '数量',
            itemStyle: {
              normal: {
                color: '#FF005A',
                lineStyle: {
                  color: '#FF005A',
                  width: 2
                }
              }
            },
            smooth: true,
            type: 'bar',
            barWidth: '80',
            barMaxWidth: '200',
            barGap: '30%',
            // barCategoryGap: '50%',
            data: expectedData, // 图表数据
            animationDuration: 2000, // 动画时长
            animationEasing: 'cubicInOut' // 动画类型
          }]
        })
      }
    }
  }
</script>