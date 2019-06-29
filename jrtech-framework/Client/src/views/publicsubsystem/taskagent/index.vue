<template>
<div class="mod-role">
  <el-form :inline="true" @keyup.enter.native="doRefresh()">
    <el-form-item>
      <el-button type="primary" @click="doNew()" icon="el-icon-plus">新增</el-button>
      <el-button icon="el-icon-delete" type="danger" @click="doBatchDisable()" :disabled="selectedRows.length <= 0">批量禁用</el-button>
      <el-button icon="el-icon-delete" type="danger" @click="doBatchEnable()" :disabled="selectedRows.length <= 0">批量启用</el-button>
      <el-button icon="el-icon-download" @click="doExportExcel()">
        <i class="fa fa-lg fa-level-down"></i>导出
      </el-button>
    </el-form-item>
  </el-form>
  <t-grid ref="searchReulstList" :options="gridOptions" @selection-change="handleSelectionChange">
  </t-grid>
  <edit-form v-if="editFormVisible" ref="editForm" @change="doRefresh"></edit-form>
</div>
</template>
<script>
import EditForm from './edit'
export default {
  data() {
    return {
      editFormVisible: false,
      selectedRows: [],
      gridOptions: {
        dataSource: {
          serviceInstance: tapp.services.wF_TaskAgent.getMyPagedList,
          serviceInstanceInputParameters: {
            searchKey: null,
          }
        },
        grid: {
          mutiSelect: true,
          columns: [{
              prop: 'processDefinitionName',
              columnKey: 'processDefinitionName',
              label: '流程名称',
              sortable: true,
              width: 120,
            }, {
              prop: 'attorneyName',
              columnKey: 'attorneyName',
              label: '被授权人',
              sortable: true,
              width: 120,
            },
            {
              prop: 'startTime',
              columnKey: 'startTime',
              label: '开始日期',
              sortable: true,
              width: 120,
              formatter: (row, column, cellValue) => {
                return this.$util.dateFormat(row.startTime);
              }
            },
            {
              prop: 'endTime',
              columnKey: 'endTime',
              label: '结束日期',
              sortable: true,
              width: 120,
              formatter: (row, column, cellValue) => {
                return this.$util.dateFormat(row.endTime);
              }
            },
            {
              prop: 'showStatus',
              columnKey: 'showStatus',
              label: '状态',
              sortable: false,
              width: 120,
              formatter: (row, column, cellValue) => {
                switch (row.showStatus) {
                  case 'enabled':
                    return '启用';
                  case 'overdued':
                    return '过期';
                  case 'disabled':
                    return '禁用';
                  default:
                    return row.status;
                }
              },
            },
            {
              prop: 'gmtCreatedOn',
              columnKey: 'gmtCreatedOn',
              label: '添加日期',
              sortable: true,
              minWidth: 120,
              formatter: (row, column, cellValue) => {
                return this.$util.datetimeFormat(row.gmtCreatedOn);
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
  },
  created() {},
  methods: {
    doNew() {
      this.editFormVisible = true;
      this.$nextTick(() => {
        this.$refs.editForm.init(null, null);
      })
    },
    doEdit(key, row) {
      this.editFormVisible = true
      this.$nextTick(() => {
        this.$refs.editForm.init(row.id);
      })
    },
    handleSelectionChange(val) {
      this.selectedRows = val;
    },
    doBatchDisable() {
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
      self.$confirm('此操作将禁用' + ids.length + '个任务授权, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        tapp.services.wF_TaskAgent.batchDisable(ids).then(function(result) {
          self.$notify.success({
            title: '系统禁用成功',
            message: '任务授权已禁用成功！'
          });
          self.$refs.searchReulstList.refresh();
        })
      });
    },
    doBatchEnable() {
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
      self.$confirm('此操作将启用' + ids.length + '个任务授权, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        tapp.services.wF_TaskAgent.batchEnable(ids).then(function(result) {
          self.$notify.success({
            title: '系统启用成功',
            message: '任务授权已启用成功！'
          });
          self.$refs.searchReulstList.refresh();
        })
      });
    },
    doExportExcel() {
      this.$refs.searchReulstList.exportCSV('任务授权列表');
    },
    doRefresh() {
      this.$refs.searchReulstList.refresh();
    }
  }
}
</script>
