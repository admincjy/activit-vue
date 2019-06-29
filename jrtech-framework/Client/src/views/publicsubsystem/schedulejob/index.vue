<template>
<div class="mod-role">
  <el-form :inline="true" @keyup.enter.native="doSearch()">
    <el-form-item>
      <el-input v-model="gridOptions.dataSource.serviceInstanceInputParameters.searchKey" placeholder="名称" clearable></el-input>
    </el-form-item>
    <el-form-item>
      <el-button icon="el-icon-search" @click="doSearch()">查询</el-button>
      <el-button type="primary" icon="el-icon-plus" @click="doNew()">新增</el-button>
      <el-button type="danger" icon="el-icon-delete" @click="doBatchDelete()" :disabled="selectedRows.length <= 0">批量删除</el-button>
      <el-button type="danger" @click="doPause()" :disabled="selectedRows.length <= 0">批量暂停</el-button>
      <el-button type="danger" @click="doResume()" :disabled="selectedRows.length <= 0">批量恢复</el-button>
      <el-button type="danger" @click="doRun()" :disabled="selectedRows.length <= 0">批量立即执行</el-button>
      <el-button icon="el-icon-download" @click="doExportExcel()">导出</el-button>
    </el-form-item>
  </el-form>
  <t-grid ref="searchReulstList" :options="gridOptions" @selection-change="handleSelectionChange">
  </t-grid>
  <!-- 弹窗, 新增 / 修改 -->
  <edit-form v-if="editFormVisible" ref="editForm" @change="doSearch"></edit-form>
  <log-form v-if="logFormVisible" ref="logForm"></log-form>
</div>
</template>

<script>
import EditForm from './edit'
import LogForm from './log'
export default {
  data() {
    return {
      editFormVisible: false,
      logFormVisible: false,
      selectedRows: [],
      gridOptions: {
        dataSource: {
          serviceInstance: tapp.services.base_ScheduleJob.getList,
          serviceInstanceInputParameters: {
            searchKey: null,
          }
        },
        grid: {
          operates: {
            width: 120,
            fixed: 'left',
            list: [{
                type: 'text',
                show: true,
                label: '查看',
                method: this.doEdit,
              },
              {
                type: 'text',
                show: true,
                label: '执行日志',
                method: this.doViewLog,
              },
            ]
          }, // 列操作按钮
          columns: [{
              prop: 'name',
              label: '名称',
              sortable: true,
              width: 300
            },
            {
              prop: 'cronExpression',
              label: 'cron表达式',
              sortable: true,
              width: 150
            },
            {
              prop: 'status',
              columnKey: 'status',
              label: '状态',
              width: 80,
              filters: [{
                text: '正常',
                value: 'normal'
              }, {
                text: '暂停',
                value: 'pause'
              }],
              formatter: (row, column, cellValue) => {
                return row.status === 'pause' ? '暂停' : '正常';
              },
              render: (h, params) => {
                return h('el-tag', {
                  props: {
                    type: params.row.status === 'pause' ? 'danger' : 'success'
                  } // 组件的props
                }, params.row.status === 'pause' ? '暂停' : '正常')
              }
            },
            {
              prop: 'lastLogExecuteTime',
              label: '上次执行时间',
              sortable: true,
              width: 180,
              formatter: (row, column, cellValue) => {
                return row.lastLogExecuteTime == null ? '' : this.$util.datetimeFormat(row.lastLogExecuteTime);
              }
            },
            {
              prop: 'lastLogResult',
              columnKey: 'lastLogResult',
              label: '上次执行结果',
              minWidth: 200,
              filters: [{
                text: '成功',
                value: 'success'
              }, {
                text: '执行中',
                value: 'executing'
              }, {
                text: '失败',
                value: 'error'
              }],
              formatter: (row, column, cellValue) => {
                if (!row.lastLogResult) {
                  return '';
                }
                if (row.lastLogResult === 'success') {
                  return '成功';
                }
                if (row.lastLogResult === 'executing') {
                  return '执行中';
                }
                if (row.lastLogResult === 'error') {
                  return '失败';
                }
                return '未知';
              },
              render: (h, params) => {
                return h('el-tag', {
                  props: {
                    type: params.row.lastLogResult === 'error' ? 'error' : 'success'
                  } // 组件的props
                }, params.row.lastLogResult ? (params.row.lastLogResult === 'success' ? '成功' : (params.row.lastLogResult === 'error' ? '失败' : '执行中')) : '')
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
    EditForm,
    LogForm
  },
  created() {

  },
  methods: {
    doNew() {
      this.editFormVisible = true
      this.$nextTick(() => {
        this.$refs.editForm.init(null)
      })
    },
    doEdit(key, row) {
      this.editFormVisible = true
      this.$nextTick(() => {
        this.$refs.editForm.init(row.id)
      })
    },
    doPause() {
      let self = this;
      if (!self.selectedRows || self.selectedRows.length == 0) {
        self.$notify.info({
          title: '系统提示',
          message: '您没选择任何行，无法操作！'
        });
        return;
      }
      let ids = self.selectedRows.map(function(row) {
        return row.id;
      });
      tapp.services.base_ScheduleJob.pause(ids).then(function(result) {
        self.$notify.success({
          title: '操作成功',
          message: '定时任务已暂停执行！'
        });
        self.$refs.searchReulstList.refresh();
      });
    },
    doResume() {
      let self = this;
      if (!self.selectedRows || self.selectedRows.length == 0) {
        self.$notify.info({
          title: '系统提示',
          message: '您没选择任何行，无法操作！'
        });
        return;
      }
      let ids = self.selectedRows.map(function(row) {
        return row.id;
      });
      tapp.services.base_ScheduleJob.resume(ids).then(function(result) {
        self.$notify.success({
          title: '操作成功',
          message: '定时任务已提交恢复执行！'
        });
        self.$refs.searchReulstList.refresh();
      });
    },
    doRun() {
      let self = this;
      if (!self.selectedRows || self.selectedRows.length == 0) {
        self.$notify.info({
          title: '系统提示',
          message: '您没选择任何行，无法操作！'
        });
        return;
      }
      let ids = self.selectedRows.map(function(row) {
        return row.id;
      });
      tapp.services.base_ScheduleJob.run(ids).then(function(result) {
        self.$notify.success({
          title: '操作成功',
          message: '定时任务已提交执行！'
        });
        self.$refs.searchReulstList.refresh();
      });
    },
    doBatchDelete() {
      let self = this;
      if (!self.selectedRows || self.selectedRows.length == 0) {
        self.$notify.info({
          title: '系统提示',
          message: '您没选择任何行，无法操作！'
        });
        return;
      }
      let ids = self.selectedRows.map(function(row) {
        return row.id;
      });
      self.$confirm('此操作将永久删除' + ids.length + '条定时任务, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        tapp.services.base_ScheduleJob.batchDelete(ids).then(function(result) {
          self.$notify.success({
            title: '操作成功',
            message: '定时任务已删除成功！'
          });
          self.$refs.searchReulstList.refresh();
        })
      });
    },
    doViewLog(key, row) {
      this.logFormVisible = true
      this.$nextTick(() => {
        this.$refs.logForm.init(row.id)
      })
    },
    handleSelectionChange(val) {
      this.selectedRows = val;
    },
    doBatchDelete() {
      let self = this;
      if (!self.selectedRows || self.selectedRows.length == 0) {
        self.$notify.info({
          title: '系统提示',
          message: '您没选择任何行，无法操作！'
        });
        return;
      }
      let ids = self.selectedRows.map(function(row) {
        return row.id;
      });
      self.$confirm('此操作将永久删除' + ids.length + '个定时任务, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        tapp.services.base_ScheduleJob.batchDelete(ids).then(function(result) {
          self.$notify.success({
            title: '系统删除成功',
            message: '定时任务已删除成功！'
          });
          self.$refs.searchReulstList.refresh();
        })
      });
    },
    doExportExcel() {
      this.$refs.searchReulstList.exportCSV('定时任务列表');
    },
    doSearch() {
      this.$refs.searchReulstList.refresh();
    }
  }
}
</script>
