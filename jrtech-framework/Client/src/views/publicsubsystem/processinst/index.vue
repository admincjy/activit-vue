<template>
<div class="mod-role">
  <el-form  @keyup.enter.native="doRefresh()" label-width="120px">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-form-item label="流程类别">
          <el-select placeholder="请选择流程类别" v-model="gridOptions.dataSource.serviceInstanceInputParameters.processDefinationKey" clearable>
            <el-option v-for="(item, index) in processDefinationlist" :key='item.key' :label="item.name" :value="item.key"></el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="14">
        <el-form-item label="流程发起时间">
          <t-datetime-range-picker v-model="startDateRange" @change="onStartDateRangeChanged">
          </t-datetime-range-picker>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="8">
        <el-form-item label="关键字">
          <el-input  @keyup.enter.native="doRefresh()" v-model="gridOptions.dataSource.serviceInstanceInputParameters.searchKey" placeholder="单号或描述或发起人" clearable></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="14">
        <el-form-item>
          <el-button @click="doRefresh()" icon="el-icon-search">查询</el-button>
          <el-button icon="el-icon-delete" type="danger" @click="doBatchDelete()" :disabled="checkededRows.length <= 0">批量删除</el-button>
          <el-button icon="el-icon-download" @click="doExportExcel()">
            <i class="fa fa-lg fa-level-down"></i>导出
          </el-button>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
  <t-grid ref="searchReulstList" :options="gridOptions" @selection-change="handleSelectionChange">
  </t-grid>
  <user-open-dialog v-if="setAsigneeOpenDialogVisible" ref="setAsigneeOpenDialog" @select="onSetAsigneeOpenDialogSelect"></user-open-dialog>
  <wf-activity-select v-if="gotoDialogVisible" ref="gotoDialog" @select="onGotoDialogSelect"></wf-activity-select>
</div>
</template>
<script>
import UserOpenDialog from '@/views/publicsubsystem/components/userOpenDialog'
import WfActivitySelect from './wfActivitySelect'
export default {
  data() {
    return {
      setAsigneeOpenDialogVisible: false,
      setAsigneeSelectedRow: null,
      gotoDialogVisible: false,
      gotoSelectedRow: null,
      checkededRows: [],
      processDefinationlist: [],
      startDateRange: null,
      gridOptions: {
        dataSource: {
          serviceInstance: tapp.services.wF_ProcessInst.getPagedList,
          serviceInstanceInputParameters: {
            searchKey: null,
          }
        },
        grid: {
          mutiSelect: true,
          operates: {
            width: 100,
            fixed: 'left',
            list: [{
                type: 'text',
                show: true,
                label: '跟踪',
                style:'margin-left:10px',
                method: this.doViewProcessHistory,
              },
              {
              type: 'text',
              show: true,
              label: '审批',
              render: this.renderTaskBtnLabel,
              method: this.doTask,
              },
              {
                type: 'text',
                show: true,
                label: '转办',
                method: this.setAsignee,
              },
              {
                type: 'text',
                show: true,
                label: '跳转',
                method: this.doGoto,
              },
              {
                type: 'text',
                show: true,
                label: '',
                method: this.doActiveOrSuspend,
                render: (key, row) => {
                  return row.status == 'Suspended' ? '激活' : '挂起';
                }
              },
              {
                type: 'text',
                show: true,
                label: '删除',
                method: this.doDelete,
              },
            ]
          }, // 列操作按钮
          columns: [{
              prop: 'processDefinationName',
              label: '流程名称',
              sortable: true,
              width: 100,
            },
            {
              prop: 'origiatorName',
              label: '提单人',
              sortable: true,
              width: 100,
            },
            {
              prop: 'startDate',
              label: '提单日期',
              sortable: true,
              width: 110,
              formatter: (row, column, cellValue) => {
                return this.$util.dateFormat(row.startDate, 'YYYY-MM-DD');
              }
            },
            {
              prop: 'endDate',
              label: '结束日期',
              sortable: true,
              width: 110,
              formatter: (row, column, cellValue) => {
                return this.$util.dateFormat(row.endDate, 'YYYY-MM-DD');
              }
            },
            {
              prop: 'folio',
              label: '说明',
              sortable: true,
              width: 120,
            },
            {
              prop: 'status',
              columnKey: 'status',
              label: '状态',
              sortable: true,
              width: 90,
              filters: [{
                text: '审批中',
                value: 'Approving'
              }, {
                text: '驳回提单',
                value: 'ReRequest'
              }, {
                text: '挂起',
                value: 'Suspended'
              }, {
                text: '审批完成',
                value: 'Finished'
              }, {
                text: '拒绝',
                value: 'Declined'
              }],
              formatter: (row, column, cellValue) => {
                switch (row.status) {
                  case 'Approving':
                    return '审批中';
                  case 'ReRequest':
                    return '驳回提单';
                  case 'Suspended':
                    return '挂起';
                  case 'Finished':
                    return '审批完成';
                  case 'Declined':
                    return '拒绝';
                  default:
                    return row.status;
                }
              },
            },
            {
              prop: 'taskActName',
              label: '审批结点',
              sortable: true,
              width: 100,
            },
            {
              prop: 'taskAssigneeName',
              label: '审批人',
              sortable: true,
              width: 100,
            },
            {
              prop: 'taskCandidateName',
              label: '待签收人',
              sortable: true,
              width: 110,
            },
            {
              prop: 'taskCreateDate',
              label: '任务创建时间',
              sortable: true,
              width: 150,
              formatter: (row, column, cellValue) => {
                return this.$util.dateFormat(row.taskCreateDate, 'YYYY-MM-DD');
              }
            },
          ], // 需要展示的列
          defaultSort: {
            prop: 'id',
            order: 'descending'
          },
        }
      }
    }
  },
  components: {
    UserOpenDialog,
    WfActivitySelect
  },
  created() {
    this.loadProcessDefList();
  },
  methods: {
    loadProcessDefList() {
      let self = this;
      tapp.services.wf_Model.getMadelList().then(function(result) {
        self.processDefinationlist = result;
      })
    },
    onStartDateRangeChanged(val) {
      this.gridOptions.dataSource.serviceInstanceInputParameters.startDateBegin = val[0];
      this.gridOptions.dataSource.serviceInstanceInputParameters.startDateEnd = val[1];
    },
    renderTaskBtnLabel(key, row) {
      if (row.taskAssignee != null && row.taskAssignee.length > 0) {
        return "办理";
      } else if (row.taskCandidate != null && row.taskCandidate.length > 0) {
        return "签收";
      }
      return "未知";
    },
    doTask(key, row) {
      if (row.taskAssignee != null && row.taskAssignee.length > 0) {
        //办理
        this.$router.push({
          path: row.taskFormUrl,
        });
        return;
      } else if (row.taskCandidate != null && row.taskCandidate.length > 0) {
        // "签收";
        tapp.services.wF_Workflow.handleTask({
          taskId: row.taskId,
          claim: true,
          action: 'claim',
          taskRemark: '签收',
          docId: row.businessId
        }).then(function(result) {
          self.doRefresh();
          self.$notify.success({
            title: '操作成功！',
            message: '签收任务成功！',
          });
        });
      } else {
        alert('do what?')
      }
    },
    doViewProcessHistory(key, row) {
      let path = '/publicsubsystem/processinst/processHistory?procInstId=' + row.id;
      this.$router.push({
        path: path,
      });
    },
    setAsignee(key, row) {
      let taskId = row.taskId;
      let status = row.status;
      if (!taskId) {
        this.$notify.info({
          title: '系统提示',
          message: '流程审批已完成，无法操作！'
        });
        return;
      }
      if (status === 'Suspended') {
        this.$notify.info({
          title: '系统提示',
          message: '流程实例已挂起，无法操作！'
        });
        return;
      }
      this.setAsigneeSelectedRow = row;
      this.setAsigneeOpenDialogVisible = true
      this.$nextTick(() => {
        this.$refs.setAsigneeOpenDialog.init(null);
      })
    },
    onSetAsigneeOpenDialogSelect(val) {
      let self = this;
      let taskId = self.setAsigneeSelectedRow.taskId;
      tapp.services.wF_Workflow.setAssignee({
        'taskId': taskId,
        'assignee': val.id,
      }).then(function(result) {
        self.$notify.success({
          title: '系统提示',
          message: '任务转办成功！'
        });
        self.$refs.searchReulstList.refresh();
      })
    },
    doGoto(key, row) {
      let taskId = row.taskId;
      let status = row.status;
      if (!taskId) {
        this.$notify.info({
          title: '系统提示',
          message: '流程审批已完成，无法操作！'
        });
        return;
      }
      if (status === 'Suspended') {
        this.$notify.info({
          title: '系统提示',
          message: '流程实例已挂起，无法操作！'
        });
        return;
      }
      this.gotoSelectedRow = row;
      this.gotoDialogVisible = true
      this.$nextTick(() => {
        this.$refs.gotoDialog.init(row.processDefinationKey);
      })
    },
    onGotoDialogSelect(val) {
      let self = this;
      let taskId = self.gotoSelectedRow.taskId;
      let status = self.gotoSelectedRow.status;
      let docId = self.gotoSelectedRow.businessId;
      tapp.services.wF_Workflow.goTo({
        'taskId': taskId,
        'docId': docId,
        'targetTaskDefinitionKey': val,
        'action': '跳转',
        'isClaim': false,
        'taskStatus': '跳转',
        'result': '跳转',
        'taskRemark': null,
      }).then(function(result) {
        self.$notify.success({
          title: '系统提示',
          message: '任务跳转成功！'
        });
        self.$refs.searchReulstList.refresh();
      })
    },
    doActiveOrSuspend(key, row) {
      let self = this;
      let taskId = row.taskId;
      if (!taskId) {
        self.$notify.info({
          title: '系统提示',
          message: '流程审批已完成，无法操作！'
        });
        return;
      }
      if (row.status === 'Suspended') {
        tapp.services.wF_Workflow.activateProcessInstance(row.id).then(function(result) {
          self.$notify.success({
            title: '系统提示',
            message: '流程实例激活成功！'
          });
          self.$refs.searchReulstList.refresh();
        })
      } else {

        tapp.services.wF_Workflow.suspendProcessInstance(row.id).then(function(result) {
          self.$notify.success({
            title: '系统提示',
            message: '流程实例挂起成功！'
          });
          self.$refs.searchReulstList.refresh();
        })
      }
    },
    doDelete(key, row) {
      let self = this;
      self.$confirm('此操作将永久删除, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        tapp.services.wF_ProcessInst.delete(row.id).then(function(result) {
          self.$notify.success({
            title: '系统删除成功',
            message: '流程实例已删除成功！'
          });
          self.$refs.searchReulstList.refresh();
        })
      });
    },
    handleSelectionChange(val) {
      this.checkededRows = val;
    },
    doBatchDelete() {
      let self = this;
      if (!self.checkededRows || self.checkededRows.length == 0) {
        self.$notify.info({
          title: '系统提示',
          message: '您没选择任何行，无法操作！'
        });
        return;
      }
      let ids = self.checkededRows.map(function(row) {
        return row.id;
      });
      self.$confirm('此操作将永久删除' + ids.length + '个流程实例, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        tapp.services.wF_ProcessInst.batchDelete(ids).then(function(result) {
          self.$notify.success({
            title: '系统删除成功',
            message: '流程实例已删除成功！'
          });
          self.$refs.searchReulstList.refresh();
        })
      });
    },
    doExportExcel() {
      this.$refs.searchReulstList.exportCSV('流程实例列表');
    },
    doRefresh() {
      this.$refs.searchReulstList.refresh();
    }
  }
}
</script>
