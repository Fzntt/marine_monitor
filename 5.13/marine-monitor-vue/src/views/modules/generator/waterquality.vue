<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item label="年份">
        <el-select v-model="dataForm.year" placeholder="请选择" clearable>
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="月份">
        <el-select v-model="dataForm.month" placeholder="请选择" clearable>
          <el-option
            v-for="item in monthOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="区域">
        <el-select v-model="dataForm.province" placeholder="请选择" clearable>
          <el-option
            v-for="item in provinceList"
            :key="item"
            :label="item"
            :value="item">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button type="primary" @click="asyncData()">同步更新</el-button>
<!--        <el-button v-if="isAuth('generator:waterquality:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>-->
<!--        <el-button v-if="isAuth('generator:waterquality:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>-->
      </el-form-item>
    </el-form>
    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      @selection-change="selectionChangeHandle"
      style="width: 100%;">
      <el-table-column
        prop="sea"
        header-align="center"
        align="center"
        label="海域名称"
      width="150">
      </el-table-column>
      <el-table-column
        prop="province"
        header-align="center"
        align="center"
        label="省份"
        width="150">
      </el-table-column>
      <el-table-column
        prop="city"
        header-align="center"
        align="center"
        label="城市"
        width="150">
      </el-table-column>
      <el-table-column
        prop="site"
        header-align="center"
        align="center"
        label="监测站点编号"
        width="150">
      </el-table-column>
      <el-table-column
        prop="lon"
        header-align="center"
        align="center"
        label="经度"
        width="150">
      </el-table-column>
      <el-table-column
        prop="lat"
        header-align="center"
        align="center"
        label="纬度"
        width="150">
      </el-table-column>
      <el-table-column
        prop="minitorMonth"
        header-align="center"
        align="center"
        label="监测月份"
        width="150">
      </el-table-column>
      <el-table-column
        prop="ph"
        header-align="center"
        align="center"
        label="pH值"
        width="150">
      </el-table-column>
      <el-table-column
        prop="rjy"
        header-align="center"
        align="center"
        label="溶解氧(mg/L)"
        width="150">
      </el-table-column>
      <el-table-column
        prop="hxxyl"
        header-align="center"
        align="center"
        label="化学需氧量(mg/L)"
        width="150">
      </el-table-column>
      <el-table-column
        prop="wjd"
        header-align="center"
        align="center"
        label="无机氮(mg/L)"
        width="150">
      </el-table-column>
      <el-table-column
        prop="hxlxy"
        header-align="center"
        align="center"
        label="活性磷酸盐"
        width="150">
      </el-table-column>
      <el-table-column
        prop="syl"
        header-align="center"
        align="center"
        label="石油类(mg/L)"
        width="150">
      </el-table-column>
      <el-table-column
        prop="szlb"
        header-align="center"
        align="center"
        label="水质类别"
        width="150">
      </el-table-column>
<!--      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
          <el-button type="text" size="small" @click="deleteHandle(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>-->
    </el-table>
    <el-pagination
      @size-change="sizeChangeHandle"
      @current-change="currentChangeHandle"
      :current-page="pageIndex"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="pageSize"
      :total="totalPage"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
  </div>
</template>

<script>
  import AddOrUpdate from './waterquality-add-or-update'
  export default {
    data () {
      return {
        dataForm: {
          key: '',
          year: '2024'
        },
        dataList: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataListLoading: false,
        dataListSelections: [],
        addOrUpdateVisible: false,
        options: [{
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
        }],
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
        provinceList: []
      }
    },
    components: {
      AddOrUpdate
    },
    activated () {
      this.getDataList()
      this.selectProvinceList()
    },
    methods: {
      // 获取数据列表
      getDataList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/generator/waterquality/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': this.pageSize,
            'year': this.dataForm.year,
            'month': this.dataForm.month,
            'province': this.dataForm.province
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.dataList = data.page.list
            this.totalPage = data.page.totalCount
          } else {
            this.dataList = []
            this.totalPage = 0
          }
          this.dataListLoading = false
        })
      },
      asyncData () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl(`/generator/waterquality/asyncData/${this.dataForm.year}`),
          method: 'get',
          params: this.$http.adornParams({})
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.getDataList()
          }
          this.dataListLoading = false
        })
      },
      // 每页数
      sizeChangeHandle (val) {
        this.pageSize = val
        this.pageIndex = 1
        this.getDataList()
      },
      // 当前页
      currentChangeHandle (val) {
        this.pageIndex = val
        this.getDataList()
      },
      // 多选
      selectionChangeHandle (val) {
        this.dataListSelections = val
      },
      // 新增 / 修改
      addOrUpdateHandle (id) {
        this.addOrUpdateVisible = true
        this.$nextTick(() => {
          this.$refs.addOrUpdate.init(id)
        })
      },
      // 删除
      deleteHandle (id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.id
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/generator/waterquality/delete'),
            method: 'post',
            data: this.$http.adornData(ids, false)
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.getDataList()
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        })
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
      }
    }
  }
</script>
