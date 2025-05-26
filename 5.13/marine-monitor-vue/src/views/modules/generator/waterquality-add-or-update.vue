<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="海域名称" prop="sea">
      <el-input v-model="dataForm.sea" placeholder="海域名称"></el-input>
    </el-form-item>
    <el-form-item label="省份" prop="province">
      <el-input v-model="dataForm.province" placeholder="省份"></el-input>
    </el-form-item>
    <el-form-item label="城市" prop="city">
      <el-input v-model="dataForm.city" placeholder="城市"></el-input>
    </el-form-item>
    <el-form-item label="监测站点编号" prop="site">
      <el-input v-model="dataForm.site" placeholder="监测站点编号"></el-input>
    </el-form-item>
    <el-form-item label="经度" prop="lon">
      <el-input v-model="dataForm.lon" placeholder="经度"></el-input>
    </el-form-item>
    <el-form-item label="纬度" prop="lat">
      <el-input v-model="dataForm.lat" placeholder="纬度"></el-input>
    </el-form-item>
    <el-form-item label="监测月份(YYYY-MM)" prop="minitorMonth">
      <el-input v-model="dataForm.minitorMonth" placeholder="监测月份(YYYY-MM)"></el-input>
    </el-form-item>
    <el-form-item label="pH值" prop="ph">
      <el-input v-model="dataForm.ph" placeholder="pH值"></el-input>
    </el-form-item>
    <el-form-item label="溶解氧(mg/L)" prop="rjy">
      <el-input v-model="dataForm.rjy" placeholder="溶解氧(mg/L)"></el-input>
    </el-form-item>
    <el-form-item label="化学需氧量(mg/L)" prop="hxxyl">
      <el-input v-model="dataForm.hxxyl" placeholder="化学需氧量(mg/L)"></el-input>
    </el-form-item>
    <el-form-item label="无机氮(mg/L)" prop="wjd">
      <el-input v-model="dataForm.wjd" placeholder="无机氮(mg/L)"></el-input>
    </el-form-item>
    <el-form-item label="活性磷酸盐" prop="hxlxy">
      <el-input v-model="dataForm.hxlxy" placeholder="活性磷酸盐"></el-input>
    </el-form-item>
    <el-form-item label="石油类(mg/L)" prop="syl">
      <el-input v-model="dataForm.syl" placeholder="石油类(mg/L)"></el-input>
    </el-form-item>
    <el-form-item label="水质类别" prop="szlb">
      <el-input v-model="dataForm.szlb" placeholder="水质类别"></el-input>
    </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  export default {
    data () {
      return {
        visible: false,
        dataForm: {
          id: 0,
          sea: '',
          province: '',
          city: '',
          site: '',
          lon: '',
          lat: '',
          minitorMonth: '',
          ph: '',
          rjy: '',
          hxxyl: '',
          wjd: '',
          hxlxy: '',
          syl: '',
          szlb: ''
        },
        dataRule: {
          sea: [
            { required: true, message: '海域名称不能为空', trigger: 'blur' }
          ],
          province: [
            { required: true, message: '省份不能为空', trigger: 'blur' }
          ],
          city: [
            { required: true, message: '城市不能为空', trigger: 'blur' }
          ],
          site: [
            { required: true, message: '监测站点编号不能为空', trigger: 'blur' }
          ],
          lon: [
            { required: true, message: '经度不能为空', trigger: 'blur' }
          ],
          lat: [
            { required: true, message: '纬度不能为空', trigger: 'blur' }
          ],
          minitorMonth: [
            { required: true, message: '监测月份(YYYY-MM)不能为空', trigger: 'blur' }
          ],
          ph: [
            { required: true, message: 'pH值不能为空', trigger: 'blur' }
          ],
          rjy: [
            { required: true, message: '溶解氧(mg/L)不能为空', trigger: 'blur' }
          ],
          hxxyl: [
            { required: true, message: '化学需氧量(mg/L)不能为空', trigger: 'blur' }
          ],
          wjd: [
            { required: true, message: '无机氮(mg/L)不能为空', trigger: 'blur' }
          ],
          hxlxy: [
            { required: true, message: '活性磷酸盐不能为空', trigger: 'blur' }
          ],
          syl: [
            { required: true, message: '石油类(mg/L)不能为空', trigger: 'blur' }
          ],
          szlb: [
            { required: true, message: '水质类别不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.id = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.id) {
            this.$http({
              url: this.$http.adornUrl(`/generator/waterquality/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.sea = data.waterQuality.sea
                this.dataForm.province = data.waterQuality.province
                this.dataForm.city = data.waterQuality.city
                this.dataForm.site = data.waterQuality.site
                this.dataForm.lon = data.waterQuality.lon
                this.dataForm.lat = data.waterQuality.lat
                this.dataForm.minitorMonth = data.waterQuality.minitorMonth
                this.dataForm.ph = data.waterQuality.ph
                this.dataForm.rjy = data.waterQuality.rjy
                this.dataForm.hxxyl = data.waterQuality.hxxyl
                this.dataForm.wjd = data.waterQuality.wjd
                this.dataForm.hxlxy = data.waterQuality.hxlxy
                this.dataForm.syl = data.waterQuality.syl
                this.dataForm.szlb = data.waterQuality.szlb
              }
            })
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/generator/waterquality/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'sea': this.dataForm.sea,
                'province': this.dataForm.province,
                'city': this.dataForm.city,
                'site': this.dataForm.site,
                'lon': this.dataForm.lon,
                'lat': this.dataForm.lat,
                'minitorMonth': this.dataForm.minitorMonth,
                'ph': this.dataForm.ph,
                'rjy': this.dataForm.rjy,
                'hxxyl': this.dataForm.hxxyl,
                'wjd': this.dataForm.wjd,
                'hxlxy': this.dataForm.hxlxy,
                'syl': this.dataForm.syl,
                'szlb': this.dataForm.szlb
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.visible = false
                    this.$emit('refreshDataList')
                  }
                })
              } else {
                this.$message.error(data.msg)
              }
            })
          }
        })
      }
    }
  }
</script>
