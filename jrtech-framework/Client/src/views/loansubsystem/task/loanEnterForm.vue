
<template>
<div>
  <el-tabs v-model="tabActive" @tab-click="handleTabClick" tab-position="right" class="fixed-header">
    <el-row :gutter="20">
      <el-col>
        <h4>基本信息</h4>
        <hr class="el-row-hr" />
      </el-col>
    </el-row>
    <el-tab-pane label="客户资料" name="baseInfo">
      <el-form :model="docEntity" ref="ruleForm" @keyup.enter.native="doSubmit()" label-width="160px">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="申请编号" prop="customerCode" verify can-be-empty :maxLength="50">
              <el-input v-model="docEntity.customerCode" placeholder="系统生成，无需输入" readOnly="true"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="产品类别" prop="loanProductSubTypeId" verify :maxLength="50" class="is-required">
              <pl-loanProducttype-select v-model="docEntity.loanProductSubTypeId" @change="onLoanProductSubTypeChanged" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="客户经理" prop="trackingPersonInfoMRId" verify :maxLength="50" class="is-required">
              <base-user-select role-category="base_rolecategory_trackingpersoninfomr" v-model="docEntity.trackingPersonInfoMRId" :text="docEntity.trackingPersonInfoMRName" placeholder="请选择">
              </base-user-select>
            </el-form-item>
          </el-col>
        </el-row>
        <gxd-edituc ref="gxdEditUC" v-if="docEntity.formTemplateTypeId == 'pl_formtemplatetype_gxd'"></gxd-edituc>
        <company-edituc ref="comapnyEditUC" v-if="docEntity.formTemplateTypeId == 'pl_formtemplatetype_company'"></company-edituc>
      </el-form>
    </el-tab-pane>
    <el-tab-pane label="关联人信息" name="loanEnterCustomerRelationList">
      <div style="margin-bottom:20px;">
        <loan-enter-customerrelation-list ref="loanEnterCustomerRelationList" :loanDocId="docId" v-if="tabActive =='loanEnterCustomerRelationList'">
        </loan-enter-customerrelation-list>
      </div>
    </el-tab-pane>
    <el-tab-pane label="附件" name="assetManagement">
      <div style="margin-bottom:20px;">
        <base-asset-multi-management ref="assetManagement" :assetCategoryClassifications="assetCategoryClassifications" :businessDocId="docId" v-if="tabActive =='assetManagement'">
        </base-asset-multi-management>
      </div>
    </el-tab-pane>
    <el-tab-pane label="审批过程" name="processtrack" v-if=" taskId !=null && taskId.length>0 ">
      <div style="margin-bottom:20px;">
        <wf-processtrack ref="processtrack" :procInstId="procInstId" v-if="tabActive =='processtrack'">
        </wf-processtrack>
      </div>
    </el-tab-pane>
  </el-tabs>
  <el-row :gutter="20">
    <el-col :span="24">
      <div style="float:left">
        <base-export-template :templateUrl="exportTemplateUrl" :templateCategoryId="'base_exporttemplate_pl_contract'" :title="'打印合同'" :docId="docId"></base-export-template>
      </div>
      <div style="float:right" v-if="formResult == 0">
        <el-button @click="doSave()">保存</el-button>
        <el-button type="primary" @click="doSubmit()">提交</el-button>
        <el-button type="danger" @click="doDelete()">放弃</el-button>
      </div>
    </el-col>
  </el-row>

</div>
</template>

<script>
import moment from 'moment';
import gxdEdituc from './loanEnterGxdEditUC.vue'
import companyEdituc from './loanEnterCompanyEditUC.vue'
import loanEnterCustomerrelationList from './loanEnterCustomerRelationList.vue'

export default {
  data() {
    return {
      tabActive: 'baseInfo',
      procInstId: null,
      taskId: null,
      docId: null,
      docEntity: {},
      assetCategoryClassifications: ['pl_loanapplyInput'],
      formAction: 0, // 0：add,1:edit
      formResult: 0, //0:待处理，1:处理完成
      exportTemplateUrl: null,
    }
  },
  components: {
    gxdEdituc,
    companyEdituc,
    loanEnterCustomerrelationList,
  },
  created() {


    this.docId = this.$route.query.id;
    this.taskId = this.$route.query.taskId;
    this.procInstId = this.$route.query.procInstId;
    this.exportTemplateUrl = window.SITE_CONFIG['serverUrl'] + '/authapi/pl_loanenter/export?loanDocId=' + this.docId;

    let self = this;
    if (self.docId) {
      tapp.services.pL_LoanEnter.get(self.docId).then(function(result) {
        self.docEntity = result;
        if (!self.docEntity.loanApplyDate) {
          self.docEntity.loanApplyDate = self.$util.datetimeFormat(moment());
        }
        self.setEditViewModel();
        self.formAction = 1;
      });
    } else {
      tapp.services.base_Common.getSUIds(1).then(function(result) {
        //data恢复初始化数据
        Object.assign(self.$data, self.$options.data());
        self.docId = result[0];
        self.docEntity.id = self.docId;
        self.docEntity.formTemplateTypeId = 'pl_formtemplatetype_gxd';
        self.docEntity.loanApplyDate = self.$util.datetimeFormat(moment());
        self.setEditViewModel();
      });
    }
  },
  activated() {},
  computed: {},
  methods: {
    handleTabClick(tab, event) {
      if (!tab) {
        return;
      }
      this.tabActive = tab.name;
    },
    onLoanProductSubTypeChanged(val) {
      if (!val) {
        return;
      }
      this.docEntity.formTemplateTypeId = val.formTemplateTypeId;
      this.setEditViewModel();
    },
    setEditViewModel() {
      this.$nextTick(() => {
        if (this.docEntity.formTemplateTypeId == 'pl_formtemplatetype_gxd') {
          this.$refs.gxdEditUC.setEntity(this.docEntity)
        } else if (this.docEntity.formTemplateTypeId == 'pl_formtemplatetype_company') {
          this.$refs.comapnyEditUC.setEntity(this.docEntity)
        }
      });
    },
    doSave(formName) {
      let validResult = [];
      if (this.docEntity.loanProductSubTypeId == null || this.docEntity.loanProductSubTypeId.length == 0) {
        validResult.push('请补充产品类别输入');
      }
      if (this.docEntity.trackingPersonInfoMRId == null || this.docEntity.trackingPersonInfoMRId.length == 0) {
        validResult.push('请补充客户经理输入');
      }

      if (this.docEntity.formTemplateTypeId == 'pl_formtemplatetype_gxd') {
        let innerValidResult = this.$refs.gxdEditUC.saveVaild();
        validResult.push(...innerValidResult);
      } else if (this.docEntity.formTemplateTypeId == 'pl_formtemplatetype_company') {
        let innerValidResult = this.$refs.comapnyEditUC.saveVaild();
        validResult.push(...innerValidResult);
      }

      if (validResult.length > 0) {
        let validResultString = validResult.join(",");
        this.$notify.error({
          title: '系统输入验证失败',
          message: validResultString
        });
        return;
      }

      let self = this;
      let model = {
        "id": self.docEntity.id,
        "formTemplateTypeId": self.docEntity.formTemplateTypeId,
        "remark": self.docEntity.remark,
        "headerEntity": {}
      };

      let editUCModel = null;
      if (self.docEntity.formTemplateTypeId == 'pl_formtemplatetype_gxd') {
        editUCModel = this.$refs.gxdEditUC.getEntity();
      } else if (self.docEntity.formTemplateTypeId == 'pl_formtemplatetype_company') {
        editUCModel = this.$refs.comapnyEditUC.getEntity();
      }
      Object.assign(model, editUCModel);
      model.headerEntity.trackingPersonInfoMRId = self.docEntity.trackingPersonInfoMRId;
      model.headerEntity.loanProductSubTypeId = self.docEntity.loanProductSubTypeId;
      Object.assign(self.docEntity, model.headerEntity);
      let modelString = JSON.stringify(model);
      tapp.services.pL_LoanEnter.save({
        "jsonString": modelString
      }).then(function(result) {
        self.docEntity = Object.assign({}, self.docEntity, result);
        self.setEditViewModel();
        self.formAction = 1;

        self.$notify.success({
          title: '操作成功！',
          message: '保存客户进件成功！',
        });
      });
    },
    doSubmit() {
      let self = this;
      let editUcValidPromise = null;

      if (self.docEntity.formTemplateTypeId == 'pl_formtemplatetype_gxd') {
        editUcValidPromise = self.$refs.gxdEditUC.submitVaild();
      } else if (self.docEntity.formTemplateTypeId == 'pl_formtemplatetype_company') {
        editUcValidPromise = self.$refs.comapnyEditUC.submitVaild();
      }
      let selfValidPromise = self.$refs['ruleForm'].validate();
      Promise.all([editUcValidPromise, selfValidPromise])
        .then(resultList => {

          let model = {
            "sn": self.taskId,
            "id": self.docEntity.id,
            "formTemplateTypeId": self.docEntity.formTemplateTypeId,
            "remark": self.docEntity.remark
          };
          let editUCModel = null;
          if (self.docEntity.formTemplateTypeId == 'pl_formtemplatetype_gxd') {
            editUCModel = this.$refs.gxdEditUC.getEntity();
          } else if (self.docEntity.formTemplateTypeId == 'pl_formtemplatetype_company') {
            editUCModel = this.$refs.comapnyEditUC.getEntity();
          }
          Object.assign(model, editUCModel);
          model.headerEntity.trackingPersonInfoMRId = self.docEntity.trackingPersonInfoMRId;
          model.headerEntity.loanProductSubTypeId = self.docEntity.loanProductSubTypeId;
          Object.assign(self.docEntity, model.headerEntity);

          let modelString = JSON.stringify(model);

          tapp.services.pL_LoanEnter.submit({
            "jsonString": modelString
          }).then(function(result) {
            self.docEntity = Object.assign({}, self.docEntity, result);
            self.formResult = 1;
            self.$notify.success({
              title: '操作成功！',
              message: '提交客户进件成功！',
            });
          });
        }).catch(function(e) {
          self.$notify.error({
            title: '错误',
            message: '系统输入验证失败！'
          });
          return false;

        });

    },
    doDelete() {
      let self = this;
      self.$confirm('此操作将永久放弃客户跟进, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {

        let model = {
          "id": self.docId,
          "procInstId": self.procInstId
        };
        tapp.services.pL_LoanEnter.delete(model).then(function(result) {
          self.formAction = 1;
          self.$util.closeCurrentTabNav('pl_loanapplyInputList');
          self.$notify.success({
            title: '系统成功',
            message: '放弃客户跟进成功！'
          });

        })
      });
    },
  }
}
</script>
