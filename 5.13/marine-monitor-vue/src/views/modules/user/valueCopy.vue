<template>
  <el-dialog
    title="详情"
    :visible.sync="dialogVisible"
    width="80%"
    :close-on-click-modal="false"
    @opened="handleDialogOpened"
    @closed="handleDialogClosed">
    <div>
      <el-form>
        <el-row :gutter="24">
          <el-col :span="4">
            <el-form-item label="年份">
              <el-select v-model="dataForm.year" placeholder="请选择年份" style="width: 100px">
                <el-option
                  v-for="item in yearOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="区域">
              <el-select v-model="dataForm.province" placeholder="请选择区域" style="width: 100px">
                <el-option
                  v-for="item in provinceList"
                  :key="item"
                  :label="item"
                  :value="item">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col span="2">
            <el-button @click="getDataListAndRefreshChart()" type="primary">查询</el-button>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <div ref="chart" :style="{ width: '100%', height: '50vh' }"></div>
  </el-dialog>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'valueCopy',
  data () {
    return {
      dialogVisible: false,
      type: null,
      dataForm: {
        year: '2024',
        province: '福建'
      },
      yearOptions: [
        { value: '2024', label: '2024' },
        { value: '2023', label: '2023' },
        { value: '2022', label: '2022' },
        { value: '2021', label: '2021' },
        { value: '2020', label: '2020' }
      ],
      monthOptions: [
        { value: '10', label: '10' },
        { value: '11', label: '11' }
      ],
      provinceList: [],
      chartInstance: null // 存储 ECharts 实例
    }
  },
  methods: {
    init (val) {
      this.type = val
      this.dialogVisible = true
      this.selectProvinceList()
    },

    selectProvinceList () {
      this.$http({
        url: this.$http.adornUrl('/generator/waterquality/selectProvinceList'),
        method: 'get',
        params: this.$http.adornParams({})
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.provinceList = data.list
        }
      })
    },

    // 初始化图表
    createECharts () {
      // 确保 DOM 已经渲染
      this.$nextTick(() => {
        if (!this.chartInstance) {
          this.chartInstance = echarts.init(this.$refs.chart)
          // 添加窗口大小变化监听
          window.addEventListener('resize', this.resizeChart)
        }

        // 设置默认的空配置
        const option = {
          title: {
            text: this.getName(),
            left: 'center'
          },
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: [],
            bottom: 10
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '15%',
            containLabel: true
          },
          xAxis: {
            type: 'category',
            data: [],
            axisTick: {
              alignWithLabel: true
            },
            axisLabel: {
              interval: 0,
              rotate: 30 // 如果标签文字过长可以旋转
            }
          },
          yAxis: {
            type: 'value',
            name: '数值'
          },
          series: []
        }

        this.chartInstance.setOption(option)
      })
    },

    // 获取数据并刷新图表
    async getDataListAndRefreshChart () {
      try {
        const dataList = await this.getDataList()
        this.updateChart(dataList)
      } catch (error) {
        console.error('获取数据失败:', error)
        this.$message.error('获取数据失败')
      }
    },

    // 获取数据
    async getDataList () {
      try {
        const {data} = await this.$http({
          url: this.$http.adornUrl('/generator/waterquality/selectLegendList'),
          method: 'get',
          params: this.$http.adornParams({
            year: this.dataForm.year,
            type: this.type,
            province: this.dataForm.province
          })
        })
        return data.code === 0 ? data.list : []
      } catch (error) {
        return []
      }
    },

    // 更新图表数据
    updateChart (dataList) {
      if (!this.chartInstance) {
        this.createECharts()
      }
      this.getName()
      const xAxisData = dataList.map(item => item.month + '月')
      const seriesData = dataList.map(item => item.value)

      const option = {
        xAxis: {
          data: xAxisData
        },
        series: [{
          name: this.getName(),
          type: 'line',
          data: seriesData,
          smooth: true,
          itemStyle: {
            color: '#409EFF'
          },
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: 'rgba(64, 158, 255, 0.5)' },
              { offset: 1, color: 'rgba(64, 158, 255, 0.1)' }
            ])
          }
        }]
      }

      this.chartInstance.setOption(option)

      // 如果需要可以显示加载动画
      // this.chartInstance.showLoading();
      // 数据加载完成后
      // this.chartInstance.hideLoading();
    },

    // 调整图表大小
    resizeChart () {
      if (this.chartInstance) {
        this.chartInstance.resize()
      }
    },

    // 对话框打开时初始化图表
    handleDialogOpened () {
      this.createECharts()
      this.getDataListAndRefreshChart()
    },

    // 对话框关闭时销毁图表
    handleDialogClosed () {
      this.dataForm.province = '福建'
      this.dataForm.year = '2024'
      if (this.chartInstance) {
        window.removeEventListener('resize', this.resizeChart)
        this.chartInstance.dispose()
        this.chartInstance = null
      }
    },
    getName () {
      if (this.type === 'PH') {
        return 'PH'
      }
      if (this.type === 'RJY') {
        return '溶解氧'
      }
      if (this.type === 'HXXYL') {
        return '化学需氧量'
      }
      if (this.type === 'WJD') {
        return '无机氮'
      }
      if (this.type === 'HXLSY') {
        return '活性磷酸盐'
      }
      if (this.type === 'SYL') {
        return '石油类'
      }
    }
  },
  beforeDestroy () {
    // 组件销毁时确保移除监听和销毁图表
    if (this.chartInstance) {
      window.removeEventListener('resize', this.resizeChart)
      this.chartInstance.dispose()
    }
  }
}
</script>

<style scoped>
/* 可以根据需要添加样式 */
</style>
