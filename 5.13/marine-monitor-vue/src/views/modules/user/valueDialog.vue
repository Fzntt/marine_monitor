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
          <el-col :span="2">
            <el-button @click="getDataListAndRefreshChart()" type="primary">查询</el-button>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <div ref="chart" :style="{ width: '100%', height: '50vh' }"></div>

    <!-- 预警信息和解决方案 -->
    <div v-if="warningInfo.visible" class="warning-info">
      <el-alert
        :title="warningInfo.title"
        type="warning"
        :description="warningInfo.description"
        show-icon
        :closable="false">
      </el-alert>
      <div class="solution-section">
        <h4>处理方案：</h4>
        <p>{{ warningInfo.solution }}</p>
        <h4>潜在危害：</h4>
        <p>{{ warningInfo.harm }}</p>
      </div>
    </div>
  </el-dialog>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'valueDialog',
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
      chartInstance: null,
      // 预警阈值配置
      warningThresholds: {
        PH: {}, // PH值正常范围6.5-8.5
        RJY: {}, // 溶解氧最低5mg/L
        HXXYL: {}, // 化学需氧量最高30mg/L
        WJD: {}, // 无机氮最高0.5mg/L
        HXLSY: {}, // 活性磷酸盐最高0.03mg/L
        SYL: {} // 石油类最高0.05mg/L
      },
      // 预警信息
      warningInfo: {
        visible: false,
        title: '',
        description: '',
        solution: '',
        harm: ''
      },
      // 解决方案和危害信息
      solutionAndHarm: {
        PH: {
          low: {
            solution: '1. 检查并调整水体酸碱度\n2. 添加碱性物质如石灰调节\n3. 检查是否有酸性废水排放',
            harm: '酸性水体会腐蚀设备，危害水生生物，影响生态系统平衡'
          },
          high: {
            solution: '1. 检查并调整水体酸碱度\n2. 添加酸性物质如硫酸铝调节\n3. 检查是否有碱性废水排放',
            harm: '碱性水体影响水生生物生长，可能导致鱼类死亡，破坏生态平衡'
          }
        },
        RJY: {
          low: {
            solution: '1. 增加水体流动和曝气\n2. 减少有机污染物排放\n3. 控制藻类过度繁殖',
            harm: '溶解氧不足会导致水生生物窒息死亡，水体发臭，形成死水区'
          }
        },
        HXXYL: {
          high: {
            solution: '1. 减少有机污染物排放\n2. 加强污水处理\n3. 增加水体自净能力',
            harm: '高化学需氧量表明水体有机污染严重，消耗溶解氧，危害水生生物'
          }
        },
        WJD: {
          high: {
            solution: '1. 控制农业化肥使用\n2. 减少生活污水排放\n3. 建设人工湿地净化',
            harm: '无机氮过高会导致水体富营养化，藻类大量繁殖，破坏生态平衡'
          }
        },
        HXLSY: {
          high: {
            solution: '1. 控制含磷洗涤剂使用\n2. 减少农业磷肥流失\n3. 实施磷去除工艺',
            harm: '活性磷酸盐过高会导致水体富营养化，引发藻华现象'
          }
        },
        SYL: {
          high: {
            solution: '1. 控制石油类污染物排放\n2. 设置油水分离设施\n3. 使用吸油材料清理',
            harm: '石油类污染会形成油膜阻碍氧气交换，毒害水生生物，破坏生态环境'
          }
        }
      }
    }
  },
  methods: {
    init (val) {
      this.type = val
      this.dialogVisible = true
      this.selectProvinceList()
      this.selectWarnConfig()
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
    splitString (str) {
      console.log(str)
      const [first = '', second = ''] = str.split('_')
      return { first, second }
    },
    selectWarnConfig () {
      this.$http({
        url: this.$http.adornUrl('/generator/warnconfig/allList'),
        method: 'get',
        params: this.$http.adornParams({})
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.updateAllThresholds(data.list)
        }
      })
    },
    updateAllThresholds (configList) {
      const thresholdKeys = ['PH', 'RJY', 'HXXYL', 'WJD', 'HXLSY', 'SYL']

      thresholdKeys.forEach((key, index) => {
        const { first: minVal, second: maxVal } = this.splitString(configList[index].warnValue)

        // 更新最小值（如果存在）
        if (minVal !== '') {
          this.warningThresholds[key].min = minVal
        }

        // 更新最大值（如果存在）
        if (maxVal !== '') {
          this.warningThresholds[key].max = maxVal
        }

        this.warningThresholds[key].prompt = configList[index].prompt
        this.warningThresholds[key].prompt2 = configList[index].prompt2
      })
    },
    // 初始化图表
    createECharts () {
      this.$nextTick(() => {
        if (!this.chartInstance) {
          this.chartInstance = echarts.init(this.$refs.chart)
          window.addEventListener('resize', this.resizeChart)
        }

        const option = {
          title: {
            text: this.getName(),
            left: 'center'
          },
          tooltip: {
            trigger: 'axis',
            formatter: params => {
              let result = `${params[0].axisValue}<br/>`
              params.forEach(param => {
                let mark = ''
                if (this.isValueWarning(param.data)) {
                  mark = '预警!'
                }
                result += `${param.marker} ${param.seriesName}: ${param.data} ${mark}<br/>`
              })
              return result
            }
          },
          legend: {
            data: [],
            bottom: 10
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '5%',
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
              rotate: 30
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

    // 判断数值是否超过预警阈值
    isValueWarning (value) {
      const threshold = this.warningThresholds[this.type]
      if (!threshold) return false

      if (threshold.min !== undefined && value < threshold.min) return true
      if (threshold.max !== undefined && value > threshold.max) return true

      return false
    },

    // 获取数据并刷新图表
    async getDataListAndRefreshChart () {
      try {
        const dataList = await this.getDataList()
        this.updateChart(dataList)
        this.checkWarning(dataList)
      } catch (error) {
        console.error('获取数据失败:', error)
        this.$message.error('获取数据失败')
      }
    },

    // 检查数据是否有预警
    checkWarning (dataList) {
      this.warningInfo.visible = false
      const threshold = this.warningThresholds[this.type]
      if (!threshold) return

      // 检查是否有数据超过阈值
      const hasWarning = dataList.some(item => this.isValueWarning(item.value))
      if (!hasWarning) return

      // 设置预警信息
      this.warningInfo.visible = true
      this.warningInfo.title = `${this.getName()}数据异常预警`
      this.warningInfo.description = `检测到${this.getName()}数据超过安全阈值，请及时处理`

      this.warningInfo.solution = this.warningThresholds[this.type].prompt2
      this.warningInfo.harm = this.warningThresholds[this.type].prompt
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

      const xAxisData = dataList.map(item => item.month + '月')
      const seriesData = dataList.map(item => item.value)

      // 标记预警点
      const markPoints = []
      dataList.forEach((item, index) => {
        if (this.isValueWarning(item.value)) {
          markPoints.push({
            name: '预警点',
            value: item.value,
            xAxis: index,
            yAxis: item.value,
            itemStyle: {
              color: 'red'
            },
            label: {
              color: 'red',
              formatter: '预警'
            }
          })
        }
      })

      // 获取预警线配置
      const markLines = this.getWarningLines()

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
          },
          markPoint: {
            data: markPoints,
            symbol: 'circle',
            symbolSize: 10
          },
          markLine: {
            data: markLines,
            label: {
              position: 'end',
              formatter: params => params.name
            },
            lineStyle: {
              type: 'dashed',
              color: '#ff0000'
            }
          }
        }]
      }

      this.chartInstance.setOption(option)
    },

    // 获取预警线配置
    getWarningLines () {
      const threshold = this.warningThresholds[this.type]
      if (!threshold) return []

      const lines = []

      if (threshold.min !== undefined) {
        lines.push({
          name: '最低安全值',
          type: 'min',
          yAxis: threshold.min
        })
      }

      if (threshold.max !== undefined) {
        lines.push({
          name: '最高安全值',
          type: 'max',
          yAxis: threshold.max
        })
      }

      return lines
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
      this.warningInfo.visible = false
      if (this.chartInstance) {
        window.removeEventListener('resize', this.resizeChart)
        this.chartInstance.dispose()
        this.chartInstance = null
      }
    },

    getName () {
      const names = {
        PH: 'PH',
        RJY: '溶解氧',
        HXXYL: '化学需氧量',
        WJD: '无机氮',
        HXLSY: '活性磷酸盐',
        SYL: '石油类'
      }
      return names[this.type] || this.type
    }
  },
  beforeDestroy () {
    if (this.chartInstance) {
      window.removeEventListener('resize', this.resizeChart)
      this.chartInstance.dispose()
    }
  }
}
</script>

<style scoped>
.warning-info {
  margin-top: 20px;
  padding: 15px;
  background-color: #fff8e6;
  border-radius: 4px;
  border-left: 4px solid #faad14;
}

.solution-section {
  margin-top: 15px;
  padding: 10px;
  background-color: #fff8e6;
  border-radius: 4px;
}

.solution-section h4 {
  margin: 10px 0 5px 0;
  color: #333;
}

.solution-section p {
  margin: 0 0 10px 0;
  white-space: pre-line;
  line-height: 1.6;
}
</style>
