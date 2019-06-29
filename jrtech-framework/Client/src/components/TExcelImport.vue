<template>
<div class="tips-import">
  <el-dialog :title="title" :visible.sync="visible" width="600px">
    <el-row :gutter="20" style="margin-top:0px">
      <el-col :span="24">
        <div role="alert" class="el-alert el-alert--info"><i class="el-alert__icon el-icon-info"></i>
          <div class="el-alert__content"><span class="el-alert__title">{{desc}} </span><i class="el-alert__closebtn el-icon"> <a target="_blank" :href="excelTemplateDownloadUrl">{{templateName}}</a></i></div>
        </div>

      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="12" :offset="4">
        <el-upload ref="upload" class="" :drag="true" :multiple="false" action="" :show-file-list="false" :auto-upload="false" accept=".csv,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/vnd.ms-excel" :on-change="readExcel">
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        </el-upload>
      </el-col>
    </el-row>
    <el-row :gutter="20" v-if="fileSelectVisible">
      <el-col :span="24">
        {{fileName}}
        <el-progress :percentage="progressPercentage" status="success"></el-progress>
      </el-col>
    </el-row>
    <el-row :gutter="20" v-show="importFailureVisible">
      <el-col :span="24">
        <div role="alert" class="el-alert el-alert--error"><i class="el-alert__icon el-icon-error"></i>
          <div class="el-alert__content"><span class="el-alert__title">导入失败</span></div>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="20" v-show="importFailureVisible">
      <el-col :span="24">
        <t-grid ref="errorGrid" :options="errorGridOptions">
        </t-grid>
      </el-col>
    </el-row>
  </el-dialog>
</div>
</template>
<script>
import XLSX from 'xlsx';
import AsyncValidator from 'async-validator'

/*
  Server: {
      serviceInstance: null,
      serviceInstanceInputParameters: null
    }
*/
export default {
  props: {
    title: {
      type: String,
      default: 'excel导入',
      required: false
    },
    desc: {
      type: String,
      default: '导入说明：请先下载导入模板',
      required: false
    },
    templatePath: {
      type: String,
      default: '',
      required: false
    },
    templateName: {
      type: String,
      default: '下载导入模版',
      required: false
    },
    rowRule: {
      type: Object,
      default: null,
      required: false
    },
    service: {
      type: Function,
      default: null,
      required: false
    },
  },
  data() {
    return {
      visible: false,
      fileName: null,
      excelTemplateDownloadUrl: '',
      progressPercentage: 0,
      excelData: [],
      fileSelectVisible: false,
      importFailureVisible: false,
      errorGridOptions: {
        dataSource: [],
        grid: {
          mutiSelect: false,
          pageable: false,
          rowClassName: "warning-row",
          columns: [{
            prop: 'errorInfo',
            label: '错误信息',
          }, ], // 需要展示的列
          defaultSort: {
            prop: 'errorInfo',
            order: 'descending'
          },
        }
      }
    }

  },
  computed: {
    validateRule: {
      // 动态计算currentValue的值
      get: function() {
        let rowRule = this.rowRule;
        if (!rowRule) {
          return null;
        }
        let rules = {};
        Object.keys(rowRule).forEach(key => {
          rules[key] = rowRule[key].rule;
        });
        return rules;
      },
    },
  },
  mounted() {},
  methods: {
    show() {
      this.excelTemplateDownloadUrl = window.SITE_CONFIG['serverUrl'] +'/authapi/base_ExcelExportManagement/downloadTemplate?f=' +this.templatePath;

      this.visible = true;
      this.excelData = [];
      this.fileName = null;
      this.progressPercentage = 0;
      this.importFailureVisible = false;
      this.$nextTick(() => {
        this.$refs.upload.clearFiles();
      })
    },
    readExcel(file) {

      this.excelData = [];
      this.progressPercentage = 0;
      this.importFailureVisible = false;

      this.fileName = file.name;
      this.fileSelectVisible = true;
      const fileReader = new FileReader();
      fileReader.onload = (ev) => {
        try {
          this.progressPercentage = 10;
          const data = ev.target.result;
          const workbook = XLSX.read(data, {
            type: 'binary'
          });
          this.progressPercentage = 20;
          let idx = 0;
          this.excelData.length = 0;

          for (let sheet in workbook.Sheets) {
            idx++;
            const sheetArray = XLSX.utils.sheet_to_json(workbook.Sheets[sheet], {
              range: 2
            });
            this.excelData.push(...sheetArray);
            this.progressPercentage = parseInt(80 / workbook.Sheets) * idx;
          }
        } catch (e) {
          this.showError(['文件类型不正确！']);
          return false;
        }
        this.progressPercentage = 100;
        this.importSubmit();

      };
      fileReader.readAsBinaryString(file.raw);
    },
    importSubmit() {
      if (!this.excelData.length) {
        this.$message.error({
          message: '请上传文件'
        });
        return false;
      }

      this.excelData.forEach((entity, index) => {
        if (entity == null) {
          return;
        }
        this.processRow(entity);
        if (entity.hasError) {
          entity.errorInfo = "第" + (index + 1) + "行," + entity.errorInfo;
        }

        //直接修改数组对象的值无法触发UI更新，手工更新
        //注意，动态增加的对象属性，也无法触发UI更新
        this.excelData.splice(index, 1, entity);
      });

      let errorRecords = this.excelData.filter(p => p.hasError != null && p.hasError === true);
      if (errorRecords.length > 0) {
        this.showError(errorRecords);
        return;
      }

      let self = this;
      self.service(self.excelData).then(function(result) {
        if (result.length > 0) {
          let errorRecords = result.map(er => {
            return {
              "errorInfo": er,
            }
          });

          self.showError(errorRecords);
        } else {
          self.$notify.success({
            title: 'excel导入成功',
            message: '共计'+self.excelData.length + '条数据导入成功'
          });

          self.visible = false;
          self.$emit('change', self.excelData);
        }
      });
    },
    processRow(innerEntity) {
      let validateRule = this.validateRule;
      if (validateRule != null) {
        var validator = new AsyncValidator(validateRule)
        validator.validate(innerEntity, {
          firstFields: true
        }, (errors, fields) => {
          if (errors != null && errors.length > 0) {
            innerEntity.hasError = true;
            innerEntity.errorInfo = errors.map(p => p.message).join(",");
            return false;
          } else {
            innerEntity.hasError = false;
            innerEntity.errorInfo = null;
            return true;
          }
        })
      } else {
        innerEntity.hasError = false;
        innerEntity.errorInfo = null;
        return true;
      }
      let rowRule = this.rowRule;
      if (rowRule != null) {
        Object.keys(rowRule).forEach(k => {
          let col = rowRule[k];
          if (col.formatter) {
            let prop = k;
            let val = innerEntity[prop];
            val = col.formatter(innerEntity, col, val)
            innerEntity[prop] = val;;
          }
        })
      }
    },
    showError(errors) {
      this.importFailureVisible = true;
      this.progressPercentage = 100;
      this.errorGridOptions.dataSource = errors;
      this.$refs.upload.clearFiles();
    }

  }
}
</script>
<style  scoped>
.el-row {
  margin-top: 20px;
}
.warning-row {
    background-color: #fef0f0;
}
</style>
