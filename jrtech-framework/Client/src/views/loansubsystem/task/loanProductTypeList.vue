<template>
<div class="mod-role">
  <el-form :inline="true" @keyup.enter.native="doSearch()">
    <el-form-item>
      <el-input v-model="gridOptions.dataSource.serviceInstanceInputParameters.searchKey" placeholder="名称" clearable></el-input>
    </el-form-item>
    <el-form-item>
      <el-button icon="el-icon-search" @click="doSearch()">查询</el-button>
      <el-button icon="el-icon-plus" type="primary" @click="doNew()">新增</el-button>
      <el-button icon="el-icon-delete" type="danger" @click="doBatchDelete()" :disabled="selectedRows.length <= 0">批量删除</el-button>
      <el-button icon="el-icon-download" @click="doExportExcel()">导出</el-button>
    </el-form-item>
  </el-form>
  <t-grid ref="searchReulstList" :options="gridOptions" @selection-change="handleSelectionChange">
  </t-grid>
  <!-- 弹窗, 新增 / 修改 -->
  <edit-form v-if="editFormVisible" ref="editForm" @change="doSearch"></edit-form>
</div>
</template>

<script>
import EditForm from './loanProductTypeForm'
export default {
  data() {
    return {
      editFormVisible: false,
      selectedRows: [],
      gridOptions: {
        dataSource: {
          serviceInstance: tapp.services.pL_LoanProductType.getList,
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
            ]
          }, // 列操作按钮
          columns: [
            {
              prop: 'name',
              label: '名称',
              sortable: true,
              width: 180,
            },
            {
              prop: 'remark',
              label: '备注',
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
      self.$confirm('此操作将永久删除' + ids.length + '个产品类型, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        tapp.services.pL_LoanProductType.batchDelete(ids).then(function(result) {
          self.$notify.success({
            title: '操作成功',
            message: '产品类型已删除成功！'
          });
          self.$refs.searchReulstList.refresh();
        })
      });
    },
    doExportExcel() {
      this.$refs.searchReulstList.exportCSV('产品类型列表');
    },
    doSearch() {
      this.$refs.searchReulstList.refresh();
    }
  }
}
</script>
