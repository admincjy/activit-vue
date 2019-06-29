<template>
<div class="mod-role">
  <el-form :inline="true" @keyup.enter.native="refresh()">
    <el-form-item>
      <el-button type="primary" icon="el-icon-plus" @click="doRoleNew()" :disabled="organizationId ==null">新增</el-button>
      <el-button type="danger" icon="el-icon-delete" @click="doRoleBatchDelete()" :disabled="selectedRoleRows.length <= 0">批量删除</el-button>

      <el-button icon="el-icon-download" @click="doExportExcel()">
        <i class="fa fa-lg fa-level-down"></i>导出
      </el-button>
    </el-form-item>
  </el-form>
  <t-grid ref="roleGrid" :options="roleGridOptions" @selection-change="handleRoleSelectionChange">
  </t-grid>
  <!-- 弹窗, 新增 / 修改 -->
  <role-edit-form v-if="roleEditFormVisible" ref="roleEditForm" @change="refresh"></role-edit-form>
</div>
</template>

<script>
import RoleEditForm from './roleEdit'

export default {
  props: {
    organizationId: null,
  },
  data() {
    return {
      roleEditFormVisible: false,
      selectedRoleRows: [],
      roleGridOptions: {
        dataSource: {
          loadDataOnFirst: false,
          serviceInstance: tapp.services.base_Role.getRolesOfOrg,
          serviceInstanceInputParameters: {
            categoryId: null,
          }
        },
        grid: {
          pageable: false,
          operates: {
            width: 100,
            fixed: 'left',
            list: [{
              type: 'text',
              show: true,
              label: '查看',
              method: this.doRoleEdit,
            }, {
              type: 'text',
              show: true,
              label: '复制',
              method: this.doRoleCopy,
            }]
          }, // 列操作按钮
          columns: [{
              prop: 'id',
              label: '编码',
              sortable: true,
              width: 180
            }, {
              prop: 'name',
              label: '名称',
              sortable: true,
              width: 120
            },
            {
              prop: 'roleCategoryName',
              label: '类型',
              sortable: true,
              minWidth: 100,
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
    RoleEditForm
  },
  created() {

  },
  computed: {},
  watch: {
    'organizationId': {　　　　
      handler(newValue, oldValue) {　
        if (!newValue) {
          return;
        }
        this.roleGridOptions.dataSource.serviceInstanceInputParameters.categoryId = newValue;
        this.refresh();
      },
    },
  },
  methods: {
    doRoleNew() {
      if (this.organizationId == null) {
        this.$notify.info({
          title: '系统提示',
          message: '未选择组织机构！'
        });
        return;
      }
      this.roleEditFormVisible = true
      this.$nextTick(() => {
        this.$refs.roleEditForm.init(this.organizationId, null)
      })

    },
    doRoleEdit(key, row) {
      if (this.organizationId == null) {
        this.$notify.info({
          title: '系统提示',
          message: '未选择组织机构！'
        });
        return;
      }
      this.roleEditFormVisible = true
      this.$nextTick(() => {
        this.$refs.roleEditForm.init(this.organizationId, row.id)
      })
    },
    doRoleCopy(key, row) {
      if (this.organizationId == null) {
        this.$notify.info({
          title: '系统提示',
          message: '未选择组织机构！'
        });
        return;
      }
        let self = this;
      tapp.services.base_Role.copy(row.id).then(function(result) {
        self.refresh();
        self.$notify.success({
          title: '操作成功',
          message: '系统复制成功！'
        });
      })
    },
    handleRoleSelectionChange(val) {
      this.selectedRoleRows = val;
    },
    doRoleBatchDelete() {
      let self = this;
      if (!self.selectedRoleRows || self.selectedRoleRows.length == 0) {
        self.$notify.info({
          title: '系统提示',
          message: '您没选择任何行，无法操作！'
        });
        return;
      }
      let ids = self.selectedRoleRows.map(function(row) {
        return row.id;
      });
      self.$confirm('确认要删除共' + ids.length + '项吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        tapp.services.base_Role.batchDelete(ids).then(function(result) {
          self.refresh();
          self.$notify.success({
            title: '操作成功',
            message: '系统删除成功！'
          });
        })
      });
    },
    refresh() {
      this.$refs.roleGrid.refresh();
    },
    doExportExcel() {
      this.$refs.roleGrid.exportCSV('岗位列表');
    },
  }
}
</script>
