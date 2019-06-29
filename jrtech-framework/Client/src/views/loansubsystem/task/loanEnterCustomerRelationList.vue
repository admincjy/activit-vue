<template>
<div>
  <el-row :gutter="20">
    <el-col>
      <el-form :inline="true" @keyup.enter.native="doSearch()">
        <el-form-item>
          <el-button icon="el-icon-plus" type="primary" @click="doNewPersonal()">新增个人</el-button>
          <el-button icon="el-icon-plus" type="primary" @click="doNewCompany()">新增企业</el-button>
          <el-button icon="el-icon-delete" type="danger" @click="doBatchDelete()" :disabled="selectedRows.length <= 0">批量删除</el-button>
          <el-button icon="el-icon-download" @click="doExportExcel()">导出</el-button>
        </el-form-item>
      </el-form>
      <t-grid ref="searchReulstList" :options="gridOptions" @selection-change="handleSelectionChange">
      </t-grid>
      <!-- 弹窗, 新增 / 修改 -->
      <personal-edit-form v-if="personalEditFormVisible" ref="personalEditForm" @change="doSearch"></personal-edit-form>
      <company-edit-form v-if="companyEditFormVisible" ref="companyEditForm" @change="doSearch"></company-edit-form>
    </el-col>
  </el-row>
</div>
</template>
<script>
import PersonalEditForm from './loanEnterCustomerRelationPersonalForm.vue'
import CompanyEditForm from './loanEnterCustomerRelationCompanyForm.vue'
export default {
  props: {
    loanDocId: null,
  },
  data() {
    return {
      personalEditFormVisible: false,
      companyEditFormVisible: false,
      selectedRows: [],
      gridOptions: {
        dataSource: {
          serviceInstance: tapp.services.pL_LoanEnterCustomerRelation.getList,
          serviceInstanceInputParameters: {
            categoryId: this.loanDocId
          }
        },
        grid: {
          pageable: false,
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
          columns: [{
              prop: 'relationCategoryName',
              label: '关联关系',
              sortable: true,
              width: 100,
            },
            {
              prop: 'categoryName',
              label: '关联人类型',
              sortable: true,
              width: 150,
            },
            {
              prop: 'name',
              label: '关联人名称',
              sortable: true,
              width: 150,
            },
            {
              prop: 'tel',
              label: '联系电话',
              width: 150,
            },
            {
              prop: 'houseAddress',
              label: '联系地址',
              minWidth: 200,
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
    PersonalEditForm,
    CompanyEditForm
  },
  created() {

  },
  methods: {
    doNewPersonal() {
      this.personalEditFormVisible = true
      this.$nextTick(() => {
        this.$refs.personalEditForm.init(this.loanDocId, null)
      })
    },
    doNewCompany() {
      this.companyEditFormVisible = true
      this.$nextTick(() => {
        this.$refs.companyEditForm.init(this.loanDocId, null)
      })
    },
    doEdit(key, row) {
      let categoryId = row.categoryId;
      if (categoryId === 'pl_loanenter_customerrelation_category_personal') {
        this.personalEditFormVisible = true
        this.$nextTick(() => {
          this.$refs.personalEditForm.init(this.loanDocId, row.id)
        })
      } else if (categoryId === 'pl_loanenter_customerrelation_category_company') {
        this.companyEditFormVisible = true
        this.$nextTick(() => {
          this.$refs.companyEditForm.init(this.loanDocId, row.id)
        })
      } else {
        alert('what?');
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
      self.$confirm('此操作将永久删除' + ids.length + '个关联人信息, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        tapp.services.pL_LoanEnterCustomerRelation.batchDelete(ids).then(function(result) {
          self.$notify.success({
            title: '操作成功',
            message: '关联人信息已删除成功！'
          });
          self.$refs.searchReulstList.refresh();
        })
      });
    },
    doExportExcel() {
      this.$refs.searchReulstList.exportCSV('关联人信息列表');
    },
    doSearch() {
      this.$refs.searchReulstList.refresh();
    }
  }
}
</script>
