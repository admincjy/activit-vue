
<template>
<div>
  <el-tabs v-model="tabActive" @tab-click="handleTabClick" tab-position="right" class="fixed-header">
    <el-tab-pane label="客户资料" name="baseInfo">
      <el-form :model="docEntity" ref="ruleForm" label-width="160px" 　:disabled="true">
        <el-row :gutter="20">
          <el-col>
            <h4>基本信息</h4>
            <hr class="el-row-hr" />
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="申请编号" prop="customerCode" verify can-be-empty :maxLength="50">
              <el-input v-model="docEntity.customerCode" placeholder="系统生成，无需输入"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="产品类别" prop="loanProductSubTypeId" verify :maxLength="50" class="is-required">
              <pl-loanProducttype-select v-model="docEntity.loanProductSubTypeId" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="客户经理" prop="trackingPersonInfoMRId" verify :maxLength="50" class="is-required">
              <base-user-select role-category="base_rolecategory_trackingpersoninfomr" v-model="docEntity.trackingPersonInfoMRId" :text="docEntity.trackingPersonInfoMRName" placeholder="请选择">
              </base-user-select>
            </el-form-item>
          </el-col>
        </el-row>
        <gxd-edituc ref="gxdEditUC" v-if="docEntity.formTemplateTypeId == 'pl_formtemplatetype_gxd'" :disabled="true"></gxd-edituc>
        <company-edituc ref="comapnyEditUC" v-if="docEntity.formTemplateTypeId == 'pl_formtemplatetype_company'" :disabled="true"></company-edituc>

      </el-form>
      <el-form ref="ruleForm" :model="taskEntity" label-width="160px">

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="审批意见" prop="taskRemark" verify can-be-empty :maxLength="400">
              <el-input type="textarea" :rows="3" v-model="taskEntity.taskRemark"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <div style="float:right" v-if="formResult == 0">
              <el-button type="primary" @click="doApprove()">提交</el-button>
              <el-button type="primary" @click="doSetAsignee()">转办</el-button>
              <el-button type="primary" @click="doDelegateTask()">委派</el-button>
              <el-button type="danger" @click="doGotoEnter()">驳回</el-button>
              <el-button type="danger" @click="doDecline()">拒绝</el-button>
            </div>
          </el-col>
        </el-row>
      </el-form>

    </el-tab-pane>
    <el-tab-pane label="咨询附件" name="assetManagement">
      <div style="margin-bottom:20px;">
        <base-asset-multi-management ref="assetManagement" :assetCategoryClassifications="['pl_loanapplyInput']" :businessDocId="docId" v-if="tabActive =='assetManagement'" :disabled="true">
        </base-asset-multi-management>
      </div>
    </el-tab-pane>
    <el-tab-pane label="积分卡" name="loanAuditScore">
      <base-asset-multi-management ref="loanAuditScore" :assetCategory="'pl_loanauditscore_category'" :businessDocId="docId" v-if="tabActive =='loanAuditScore'">
      </base-asset-multi-management>
    </el-tab-pane>
    <el-tab-pane label="审批过程" name="processtrack" v-if=" taskId !=null && taskId.length>0 ">
      <div style="margin-bottom:20px;">
        <wf-processtrack ref="processtrack" :procInstId="procInstId" v-if="tabActive =='processtrack'">
        </wf-processtrack>
      </div>
    </el-tab-pane>
  </el-tabs>
  <user-open-dialog v-if="userOpenDialogVisible" ref="userOpenDialog" @select="onUserOpenDialogSelect"></user-open-dialog>
</div>
</template>

<script>
import moment from 'moment';
import gxdEdituc from './loanEnterGxdEditUC.vue'
import companyEdituc from './loanEnterCompanyEditUC.vue'
import UserOpenDialog from '@/views/publicsubsystem/components/userOpenDialog'

export default {
  data() {
    return {
      tabActive: 'baseInfo',
      userOpenDialogVisible: false,
      userOpenAction: null,
      procInstId: null,
      taskId: null,
      docId: null,
      docEntity: {},
      taskEntity: {},
      formResult: 0, //0:待处理，1:处理完成
    }
  },
  components: {
    gxdEdituc,
    companyEdituc,
    UserOpenDialog
  },
  created() {
    this.docId = this.$route.query.id;
    this.taskId = this.$route.query.taskId;
    this.procInstId = this.$route.query.procInstId;

    let self = this;
    if (self.docId) {
      tapp.services.pL_LoanEnter.get(self.docId).then(function(result) {
        self.docEntity = result;
        self.setEditViewModel();
      });
    } else {
      alert('error');
    }
  },
  activated() {

  },
  computed: {},
  methods: {
    handleTabClick(tab, event) {
      if (!tab) {
        return;
      }
      this.tabActive = tab.name;
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
    doSetAsignee() {
      this.userOpenDialogVisible = true;
      this.userOpenAction = "setAsignee";
      this.$nextTick(() => {
        this.$refs.userOpenDialog.init(null);
      })
    },
    doDelegateTask() {
      this.userOpenDialogVisible = true;
      this.userOpenAction = "delegateTask";
      this.$nextTick(() => {
        this.$refs.userOpenDialog.init(null);
      })
    },
    onUserOpenDialogSelect(val) {
      let self = this;
      if (this.userOpenAction == "setAsignee") {
        tapp.services.wF_Workflow.setAssignee({
          'taskId': this.taskId,
          'assignee': val.id,
          'remark': this.taskEntity.taskRemark
        }).then(function(result) {
          self.formResult = 1;
          self.$notify.success({
            title: '系统提示',
            message: '任务转办成功！'
          });
        })
      } else if (this.userOpenAction == "delegateTask") {
        tapp.services.wF_Workflow.delegateTask({
          'taskId': this.taskId,
          'userId': val.id,
          'remark': this.taskEntity.taskRemark
        }).then(function(result) {
          self.formResult = 1;
          self.$notify.success({
            title: '系统提示',
            message: '任务委派成功！'
          });
        })
      } else {
        alet('do what?');
      }
    },
    doApprove() {
      this.$refs['ruleForm'].validate((valid) => {
        if (valid) {

          let self = this;
          let requestModel = { ...self.taskEntity
          };
          requestModel.taskId = self.taskId;
          requestModel.action = 'approve';
          requestModel.result = '审批通过';
          requestModel.docId = self.docId;

          tapp.services.pL_LoanAudit.approve(requestModel).then(function(result) {
            self.formResult = 1;
            self.$notify.success({
              title: '操作成功！',
              message: '办理借款单据成功！！',
            });
          });
        } else {
          this.$notify.error({
            title: '错误',
            message: '系统输入验证失败！'
          });
          return false;
        }
      });

    },
    doGotoEnter() {
      let self = this;
      self.$confirm('确认要驳回?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {

        let model = {
          taskId: self.taskId,
          action: 'request',
          taskRemark: self.taskRemark,
          docId: self.docId
        };
        tapp.services.pL_LoanAudit.gotoEnter(model).then(function(result) {
          self.formResult = 1;
          self.$notify.success({
            title: '系统驳回成功',
            message: '驳回成功！'
          });
        })
      });
    },
    doDecline() {
      let self = this;
      self.$confirm('此操作将拒绝借款单据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {

        let model = {
          taskId: self.taskId,
          action: 'decline',
          taskRemark: self.taskRemark,
          docId: self.docId
        };
        tapp.services.pL_LoanAudit.decline(model).then(function(result) {
          self.formResult = 1;
          self.$notify.success({
            title: '系统拒绝成功',
            message: '借款单据已拒绝成功！'
          });
        })
      });
    },
  }
}
</script>
