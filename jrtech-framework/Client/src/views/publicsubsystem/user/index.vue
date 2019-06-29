<template>
<div class="mod-role">
  <el-form :inline="true" @keyup.enter.native="doSearch()">
    <el-form-item>
      <el-input prefix-icon="el-icon-search" v-model="gridOptions.dataSource.serviceInstanceInputParameters.searchKey" placeholder="登陆名或者姓名" clearable></el-input>
    </el-form-item>
    <el-form-item>
      <el-button @click="doSearch()" icon="el-icon-search">查询</el-button>
      <el-button icon="el-icon-plus" type="primary" @click="doNew()">新增</el-button>
      <el-button icon="el-icon-delete" type="danger" @click="doBatchDelete()" :disabled="selectedRows.length <= 0">批量删除</el-button>
      <el-button icon="el-icon-download" @click="doExportExcel()">导出</el-button>
      <el-button icon="el-icon-upload2" @click="doImportExcel()">导入</el-button>
    </el-form-item>
  </el-form>
  <t-grid ref="searchReulstList" :options="gridOptions" @selection-change="handleSelectionChange">
  </t-grid>
  <!-- 弹窗, 新增 / 修改 -->
  <edit-form v-if="editFormVisible" ref="editForm" @change="doSearch"></edit-form>
  <admin-change-password-form v-if="adminChangePasswordFormVisible" ref="adminChangePasswordForm"></admin-change-password-form>
  <t-excel-import @change="doSearch" v-if="importExcelVisible" ref="importExcel" :service="importExcelService" :rowRule="importExcelRowRule" title="用户Excel导入" template-path="用户导入模板.xlsx"></t-excel-import>
</div>
</template>

<script>
import EditForm from './edit'
import AdminChangePasswordForm from './adminChangePassword'
import moment from 'moment';
import util from '@/util'
export default {
  data() {
    return {
      editFormVisible: false,
      adminChangePasswordFormVisible: false,
      importExcelVisible: false,
      importExcelService: tapp.services.base_User.batchInsert,
      importExcelRowRule: {
        loginId: {
          rule: [{
            required: true,
            message: '请输入登陆名',
            max: 20,
          }]
        },
        name: {
          rule: [{
            required: true,
            message: '请输入姓名,50内个字符',
            max: 20,
          }]
        },
        email: {
          rule: [{
            required: false,
            message: '邮箱50内个字符',
            max: 50,
          }]
        },
        activited: {
          rule: [{
            required: true,
            message: '请输入是否可用',
          }],
          formatter: (row, column, cellValue) => {
            return cellValue == '是' ? true : false;
          }
        },
        birthday: {
          rule: [{
            required: true,
            message: '请输入出生日期',
          }],
          formatter: (row, column, cellValue) => {
            if(cellValue == null){
              return null;
            }
            let momentdDate = moment(cellValue);
            return util.datetimeFormat(momentdDate);
          }
        },
        roleIds: {
          rule: [{
            required: true,
            message: '请输入所属岗位编码',
          }],
          formatter: (row, column, cellValue) => {
            return cellValue == null ? [] : cellValue.split(',');
          }
        },
      },
      selectedRows: [],
      gridOptions: {
        dataSource: {
          serviceInstance: tapp.services.base_User.getAllUsers,
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
                label: '修改密码',
                method: this.doAdminChangePassword,
              },
            ]
          }, // 列操作按钮
          columns: [{
              prop: 'loginId',
              label: '登陆名',
              sortable: true,
              width: 120
            },
            {
              prop: 'name',
              label: '姓名',
              sortable: true,
              width: 120
            },
            {
              prop: 'email',
              label: '邮箱',
              sortable: true,
              width: 200
            },
            {
              prop: 'mobile',
              label: '电话',
              sortable: true,
              width: 120
            },
            {
              prop: 'activited',
              columnKey: 'activited',
              label: '是否启用',
              width: 150,
              filters: [{
                text: '是',
                value: 1
              }, {
                text: '否',
                value: 0
              }],
              formatter: (row, column, cellValue) => {
                return row.activited === 0 ? '否' : '是';
              },
              render: (h, params) => {
                return h('el-tag', {
                  props: {
                    type: params.row.activited === true ? 'success' : 'info'
                  } // 组件的props
                }, params.row.activited === true ? '是' : '否')
              }
            },
            {
              prop: 'lastLoginTime',
              label: '最后登陆时间',
              sortable: true,
              width: 170,
              formatter: (row, column, cellValue) => {
                return this.$util.datetimeFormat(row.lastLoginTime);
              }
            },
            {
              prop: 'departmentNames',
              label: '所属门店',
              minWidth: 150,
              sortable: true,
            },

          ], // 需要展示的列
          defaultSort: {
            prop: 'id',
            order: 'ascending'
          },
        }
      }
    }
  },
  components: {
    EditForm,
    AdminChangePasswordForm
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
    doAdminChangePassword(key, row) {
      this.adminChangePasswordFormVisible = true
      this.$nextTick(() => {
        this.$refs.adminChangePasswordForm.init(row.id)
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
      self.$confirm('此操作将永久删除' + ids.length + '个用户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        tapp.services.base_User.batchDelete(ids).then(function(result) {
          self.$notify.success({
            title: '系统删除成功',
            message: '用户信息已删除成功！'
          });
          self.$refs.searchReulstList.refresh();
        })
      });
    },
    doExportExcel() {
      this.$refs.searchReulstList.exportCSV('用户列表');
    },
    doImportExcel() {
      this.importExcelVisible = true;
      this.$nextTick(() => {
        this.$refs.importExcel.show();
      })
    },
    doSearch() {
      this.$refs.searchReulstList.refresh();
    }
  }
}
</script>
