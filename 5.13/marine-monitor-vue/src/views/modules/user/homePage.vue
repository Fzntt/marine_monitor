<template>
  <div class="root-wrapper">
    <div class="container">
      <!-- 地图背景 - 修改为绝对定位 -->
      <div class="map-container">
        <l-map :zoom="zoom" :center="center"
               class="leaflet-map" ref="map">
          <l-tile-layer
            :url="tileUrl"
            :attribution="attribution"
            :subdomains="subdomains"
          />
        </l-map>
      </div>

      ```
      <!-- 固定在背景顶部的元素 - 增加z-index -->
      <div class="top-element">
        <el-select v-model="dataForm.year" placeholder="请选择" style="width: 100px"
                   @change="rush()">
          <el-option
            v-for="item in yearOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
        <span>年</span>
        <el-select v-model="dataForm.month" placeholder="请选择" style="width: 100px"
                   @change="rush()">
          <el-option
            v-for="item in monthOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
        <span>月</span>
      </div>

      <!-- 固定在背景左边的元素 - 增加z-index -->
      <div class="left-element">
        <div class="vertical-item" @click="changeType('PH')"
             :style="dataForm.type === 'PH' ? 'background-color: #4EBEF1EF;' : 'background-color: transparent;'">
          <div>PH</div>
          <div><el-button type="text" @click="openDialog('PH')">查看图例</el-button></div>
        </div>
        <div class="vertical-item" @click="changeType('RJY')"
             :style="dataForm.type === 'RJY' ? 'background-color: #4EBEF1EF;' : 'background-color: transparent;'">
          <div>溶解氧</div>
          <div><el-button type="text" @click="openDialog('RJY')">查看图例</el-button></div>
        </div>
        <div class="vertical-item" @click="changeType('HXXYL')"
             :style="dataForm.type === 'HXXYL' ? 'background-color: #4EBEF1EF;' : 'background-color: transparent;'">
          <div>化学需氧量</div>
          <div><el-button type="text" @click="openDialog('HXXYL')">查看图例</el-button></div>
        </div>
        <div class="vertical-item" @click="changeType('WJD')"
             :style="dataForm.type === 'WJD' ? 'background-color: #4EBEF1EF;' : 'background-color: transparent;'">
          <div>无机氮</div>
          <div><el-button type="text" @click="openDialog('WJD')">查看图例</el-button></div>
        </div>
        <div class="vertical-item" @click="changeType('HXLSY')"
             :style="dataForm.type === 'HXLSY' ? 'background-color: #4EBEF1EF;' : 'background-color: transparent;'">
          <div>活性磷酸盐</div>
          <div><el-button type="text" @click="openDialog('HXLSY')">查看图例</el-button></div>
        </div>
        <div class="vertical-item" @click="changeType('SYL')"
             :style="dataForm.type === 'SYL' ? 'background-color: #4EBEF1EF;' : 'background-color: transparent;'">
          <div>石油类</div>
          <div><el-button type="text" @click="openDialog('SYL')">查看图例</el-button></div>
        </div>
      </div>

      <div class="content" >
        <div style="width: 100%;height: 30px;line-height: 30px;text-align: center;font-size: 20px">
          劣质水域预警
        </div>
        <div style="width: 100%;height: 60vh;overflow-y: scroll">
          <div v-for="item in inferiorList" style="padding-bottom: 15px;">
            <span>{{item.sea}}海域 {{item.city}}地市 共{{item.count}}处 水质类别为劣四类</span>
          </div>
        </div>
      </div>

      <valueDialog v-if="valueDialogVisible" ref="valueDialog" ></valueDialog>
      ```

    </div>
  </div>
</template>

<script>
import {LMap, LTileLayer} from 'vue2-leaflet'
import 'leaflet/dist/leaflet.css'
import 'leaflet.heat'
import valueDialog from './valueDialog'

export default {
  name: 'WaterQualityHeatmap',
  components: {
    LMap,
    LTileLayer,
    valueDialog
  },
  data () {
    return {
      heatLayer: null,
      dataForm: {
        year: '2024',
        month: '11',
        type: 'RJY'
      },
      zoom: 6,
      center: [25, 120],
      // tileUrl: 'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
      tileUrl: 'https://webrd0{s}.is.autonavi.com/appmaptile?lang=zh_cn&size=1&scale=1&style=8&x={x}&y={y}&z={z}',
      attribution: '© 数据来源：国家海洋环境监测中心',
      subdomains: ['1', '2', '3', '4'],
      // 模拟的水质数据（lat, lng, intensity）
      waterData: [
        [23.1, 113.3, 0.2],  // pH 6.0 (酸性)
        [24.5, 118.1, 0.5],  // pH 7.0 (中性)
        [26.0, 121.0, 0.8],  // pH 8.0 (碱性)
        [22.6, 120.5, 0.4],  // pH 6.7
        [25.8, 119.6, 0.7]   // pH 7.8
      ],
      yearOptions: [
        {
          value: '2024',
          label: '2024'
        }, {
          value: '2023',
          label: '2023'
        }, {
          value: '2022',
          label: '2022'
        }, {
          value: '2021',
          label: '2021'
        }, {
          value: '2020',
          label: '2020'
        }
      ],
      monthOptions: [
        {
          value: '01',
          label: '1'
        }, {
          value: '02',
          label: '2'
        }, {
          value: '03',
          label: '3'
        }, {
          value: '04',
          label: '4'
        }, {
          value: '05',
          label: '5'
        },
        {
          value: '06',
          label: '6'
        }, {
          value: '07',
          label: '7'
        }, {
          value: '08',
          label: '8'
        }, {
          value: '09',
          label: '9'
        },
        {
          value: '10',
          label: '10'
        }, {
          value: '11',
          label: '11'
        }, {
          value: '12',
          label: '12'
        }
      ],
      markerLayer: null,
      valueDialogVisible: false,
      inferiorList: []
    }
  },
  mounted () {
    this.rush()

    this.$nextTick(() => {
      setTimeout(() => {
        this.$refs.map.mapObject.invalidateSize()
      }, 300) // 延迟让地图容器真正稳定下来
    })
  },
  methods: {
    async rush () {
      const ok = await this.selectList()
      if (ok) {
        await this.addMap()
      }
      this.selectInferiorList()
    },
    async addMap () {
      // 清理旧图层
      this.clearHeatLayer()

      // 添加热力图
      this.heatLayer = window.L.heatLayer(this.waterData, {
        radius: 20,
        blur: 25,
        maxZoom: 9,
        gradient: {
          0.0: '#5f00d7',
          0.2: '#0800ff',
          0.4: '#00ffff',
          0.6: '#7fff00',
          0.7: '#ffff00',
          0.8: '#ff7f00',
          0.9: '#ff0000',
          1.0: '#7f0000'
        }
      }).addTo(this.$refs.map.mapObject)

      // 添加透明圆点用于 hover 显示数值
      this.markerLayer = window.L.layerGroup()
      this.waterData.forEach(item => {
        const [lat, lon, value] = item
        const marker = window.L.circleMarker([lat, lon], {
          radius: 5,
          color: 'transparent',
          fillColor: 'transparent',
          fillOpacity: 0,
          weight: 0
        }).bindTooltip(`值：${value}`, {
          permanent: false,
          direction: 'top',
          offset: [0, -10],
          className: 'heatmap-tooltip'
        })

        this.markerLayer.addLayer(marker)
      })

      this.markerLayer.addTo(this.$refs.map.mapObject)
    },
    clearHeatLayer () {
      if (this.heatLayer) {
        this.$refs.map.mapObject.removeLayer(this.heatLayer)
      }
      if (this.markerLayer) {
        this.$refs.map.mapObject.removeLayer(this.markerLayer)
      }
    },
    async selectList () {
      let dataValue = []
      try {
        const { data } = await this.$http({
          url: this.$http.adornUrl(`/generator/waterquality/selectList`),
          method: 'get',
          params: this.$http.adornParams({
            year: this.dataForm.year,
            month: this.dataForm.month,
            type: this.dataForm.type
          })
        })

        if (data && data.code === 0) {
          for (let i in data.list) {
            let p = []
            p.push(+data.list[i].lat)
            p.push(+data.list[i].lon)
            p.push(+data.list[i].value)
            dataValue.push(p)
          }
        }

        this.waterData = dataValue
        return true
      } catch (e) {
        console.error('获取数据失败', e)
        return false
      }
    },
    changeType (VAL) {
      this.dataForm.type = VAL
      this.rush()
    },
    openDialog (val) {
      this.valueDialogVisible = true
      this.$nextTick(() => {
        this.$refs.valueDialog.init(val)
      })
    },
    selectInferiorList () {
      this.$http({
        url: this.$http.adornUrl('/generator/waterquality/selectInferiorList'),
        method: 'get',
        params: this.$http.adornParams({
          year: this.dataForm.year,
          month: this.dataForm.month
        })
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.inferiorList = data.list
        }
      })
    }
  }
}
</script>


<style scoped>
.container {
  //position: relative;
  height: 100vh;
  width: 100%;
  overflow: hidden;
}

.map-container {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
  max-width: 100vw;
}

.background {
  width: 100%;
  height: 100%;
}

.top-element {
  position: fixed;
  top: 20px;
  left: 50%;
  transform: translateX(-50%);
  background-color: rgba(255, 255, 255, 0.8);
  padding: 10px 20px;
  border-radius: 5px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  z-index: 1000;
}

.left-element {
  position: fixed;
  left: 20px;
  top: 50%;
  transform: translateY(-50%);
  background-color: rgba(255, 255, 255, 0.8);
  padding: 15px 10px;
  border-radius: 5px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.vertical-item {
  writing-mode: horizontal-tb;
  text-align: center;
  white-space: nowrap;
  padding: 5px 10px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
}

.vertical-item:hover {
  background-color: rgba(78, 190, 241, 0.94) !important;
  cursor: pointer;
}

.content {
  position: fixed;
  right: 80px;
  z-index: 500;
  background-color: rgba(255, 255, 255, 0.7);
  max-width: 400px;
  margin: 100px 20px 0 auto;
  padding: 20px;
  border-radius: 8px;
  height: 70vh;
}

.heatmap-tooltip {
  background-color: rgba(0, 0, 0, 0.75);
  color: #fff;
  font-size: 12px;
  padding: 2px 6px;
  border-radius: 4px;
  pointer-events: none;
}

/* ✅ 修复 leaflet 容器撑破页面的问题 */
/deep/ .leaflet-container,
/deep/ .leaflet-pane,
/deep/ .leaflet-map-pane,
/deep/ .leaflet-tile-pane,
/deep/ .leaflet-layer,
/deep/ .leaflet-tile-container,
/deep/ .leaflet-zoom-animated,
/deep/ .leaflet-overlay-pane {
  max-width: 100vw !important;
  box-sizing: border-box;
}
</style>
