<template>
  <div class="dashboard-editor-container">
    <panel-group @handleSetLineChartData="handleSetLineChartData" :vol-size="volSize" :task-size="taskSize" :supply-size="supplySize" />

    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <line-chart :chart-data="lineChartData" :x-data="xdata" />
    </el-row>

  </div>
</template>

<script>
  import PanelGroup from './components/PanelGroup'
  import LineChart from './components/LineChart'
  import { getChartData } from '@/api/user'

  const lineChartData = {
    volunteer: {
      expectedData: [],
    },
    task: {
      expectedData: [],
    },
    supply: {
      expectedData: [],
    },
  }

  const xdata = {
    volunteer: {
      data: ['男', '女']
    },
    task: {
      data: ['审核中', '进行中', '已完成', '已取消']
    },
    supply: {
      data: ['口罩类', '服装穿戴类', '电子仪器类', '消毒用品类', '食品类', '其他']
    },
  }

  export default {
    name: 'Charts',
    components: {
      PanelGroup,
      LineChart,
    },
    created() {
      this.getChartData()
    },
    data() {
      return {
        // 图标展示的初始数据
        lineChartData: lineChartData.volunteer,
        xdata: xdata.volunteer,
        volSize: 0,
        taskSize: 0,
        supplySize: 0
      }
    },
    methods: {
      // 点击类型时更新图表的数据
      handleSetLineChartData(type) {
        this.lineChartData = lineChartData[type]
        this.xdata = xdata[type]
      },
      getChartData() {
        getChartData().then(res => {
          const { data } = res
          lineChartData.volunteer.expectedData = data.volChartData
          lineChartData.task.expectedData = data.taskChartData
          lineChartData.supply.expectedData = data.supplyChartData
          this.volSize = data.volSize
          this.taskSize = data.taskSize
          this.supplySize = data.supplySize
        })
      }
    }
  }
</script>

<style lang="scss" scoped>
  .dashboard-editor-container {
    padding: 32px;
    background-color: rgb(240, 242, 245);
    position: relative;

    .github-corner {
      position: absolute;
      top: 0px;
      border: 0;
      right: 0;
    }

    .chart-wrapper {
      background: #fff;
      padding: 16px 16px 0;
      margin-bottom: 32px;
    }
  }

  @media (max-width:1024px) {
    .chart-wrapper {
      padding: 8px;
    }
  }
</style>