<template>
<el-dialog :title="'执行日志'" :close-on-click-modal="false" :visible.sync="visible" width="80%" height="80%">
  <el-form :inline="true" @keyup.enter.native="doSearch()">

    <el-form-item>
      <el-button icon="el-icon-delete" type="danger" @click="doBatchDelete()" :disabled="selectedRows.length <= 0">批量删除</el-button>
      <el-button icon="el-icon-download" @click="doExportExcel()">导出</el-button>
    </el-form-item>
  </el-form>
  <t-grid ref="searchReulstList" :options="gridOptions" @selection-change="handleSelectionChange">
  </t-grid>
  <span slot="footer" class="dialog-footer">
    <el-button @click="visible = false">取消</el-button>
  </span>
</el-dialog>
</template>


<script>
export default {
  data() {
    return {
      visible: false,
      selectedRows: [],
      gridOptions: {
        dataSource: {
          loadDataOnFirst: false,
          serviceInstance: tapp.services.base_ScheduleJobLog.getList,
          serviceInstanceInputParameters: {
            categoryId:null,
            searchKey: null,
          }
        },
        grid: {
          columns: [
            {
              prop: 'result',
              columnKey: 'result',
              label: '执行结果',
              width: 100,
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
                if (!row.result) {
                  return '';
                }
                if (row.result === 'success') {
                  return '成功';
                }
                if (row.result === 'executing') {
                  return '执行中';
                }
                if (row.result === 'error') {
                  return '失败';
                }
                return '未知';
              },
              render: (h, params) => {
                return h('el-tag', {
                  props: {
                    type: params.row.result === 'error' ? 'error' : 'success'
                  } // 组件的props
                }, params.row.result ? (params.row.result === 'success' ? '成功' : (params.row.result === 'error' ? '失败' : '执行中')) : '')
              }
            },
            {
              prop: 'executeTime',
              label: '执行时间',
              sortable: true,
              width: 120,
              formatter: (row, column, cellValue) => {
                return this.$util.datetimeFormat(row.executeTime);
              }
            },
            {
              prop: 'duration',
              label: '执行时长(毫秒)',
              sortable: true,
              width: 140
            },
            {
                prop: 'errorStackTrace',
                label: '错误内容',
                sortable: true,
                minWidth: 200
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
  created() {

  },
  activated() {},
  methods: {
    init(id) {
      this.visible = true;
      let self = this;
      if (id) {
        self.gridOptions.dataSource.serviceInstanceInputParameters.categoryId = id;
        self.$nextTick(() => {
          self.$refs.searchReulstList.refresh();
        });

      } else {
        this.gridOptions.dataSource.serviceInstanceInputParameters.categoryId = null;

        self.$nextTick(() => {
          self.$refs.searchReulstList.clear();
        });

      }
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
      self.$confirm('此操作将永久删除' + ids.length + '个定时任务日志, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        tapp.services.base_ScheduleJobLog.batchDelete(ids).then(function(result) {
          self.$notify.success({
            title: '系统删除成功',
            message: '定时任务日志已删除成功！'
          });
          self.$refs.searchReulstList.refresh();
        })
      });
    },
    doExportExcel() {
      this.$refs.searchReulstList.exportCSV('定时任务日志列表');
    },
    doSearch() {
      this.$refs.searchReulstList.refresh();
    }
  }
}
</script>
