
<template>
<div class="mod-role">
  <el-form :inline="true" @keyup.enter.native="doRefresh()" >
    <el-form-item>
      <el-input style="width:300px" prefix-icon="el-icon-search" @keyup.enter.native="doRefresh()" v-model="gridOptions.dataSource.serviceInstanceInputParameters.searchKey" placeholder="编码" clearable></el-input>
    </el-form-item>
    <el-form-item>
      <el-button @click="doRefresh()" icon="el-icon-search">查询</el-button>
      <el-button type="primary" @click="doNew()" icon="el-icon-plus">新增</el-button>
      <el-button icon="el-icon-delete" type="danger" @click="doBatchDelete()" :disabled="selectedRows.length <= 0">批量删除</el-button>
      <el-button icon="el-icon-download" @click="doExportExcel()">
        <i class="fa fa-lg fa-level-down"></i>导出
      </el-button> 
    </el-form-item>
  </el-form>
  <t-grid ref="searchReulstList" :options="gridOptions" @selection-change="handleSelectionChange">
  </t-grid>
</div>
</template>
<script>
export default {
  data() {
    return {
      selectedRows: [],
      gridOptions: {
        dataSource: {
          serviceInstance: tapp.services.base_Information.getPagedList,
          serviceInstanceInputParameters: {
            searchKey: null,
          }
        },
        grid: {
          mutiSelect: true,
          operates: {
            width: 60,
            fixed: 'left',
            list: [{
              type: 'text',
              show: true,
              label: '查看',
              method: this.doEdit,
            }, ]
          }, // 列操作按钮
          columns: [

            {
              prop: 'title',
              columnKey: 'title',
              label: '标题',
              sortable: true,
              minWidth: 120,
            },
            {
              prop: 'classificationId',
              columnKey: 'classificationId',
              label: '分类',
              sortable: true,
              width: 120,
              filters: this.$util.getListDataDicFilters('base_information_classification'),
              formatter: (row, column, cellValue) => {
                return this.$util.dataDicFormat('base_information_classification', row.classificationId);
              }
            },
            {
              prop: 'type',
              columnKey: 'type',
              label: '类别',
              sortable: true,
              width: 120,
              filters: this.$util.getListDataDicFilters('base_information_type'),
              formatter: (row, column, cellValue) => {
                return this.$util.dataDicFormat('base_information_type', row.type);
              }
            },
            {
              prop: 'status',
              columnKey: 'status',
              label: '状态',
              sortable: true,
              width: 100,
              filters: this.$util.getListDataDicFilters('base_information_status'),
              formatter: (row, column, cellValue) => {
                return this.$util.dataDicFormat('base_information_status', row.status);
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
  components: {},
  created() {

  },
  methods: {
    doNew() {
      this.$router.push({
        path: '/publicsubsystem/information/edit',
      });
    },
    doEdit(key, row) {
      let path = '/publicsubsystem/information/edit?id=' + row.id;
      this.$router.push({
        path: path,
      });
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
      self.$confirm('此操作将永久删除' + ids.length + '个信息公告, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        tapp.services.base_Information.batchDelete(ids).then(function(result) {
          self.$notify.success({
            title: '系统删除成功',
            message: '信息公告已删除成功！'
          });
          self.$refs.searchReulstList.refresh();
        })
      });
    },
    doExportExcel() {
      this.$refs.searchReulstList.exportCSV('信息公告列表');
    },
    doRefresh() {
      this.$refs.searchReulstList.refresh();
    }
  }
}
</script>
