<template>
<el-dialog :title="'选择用户信息'" :close-on-click-modal="false" :visible.sync="visible" :modal="false">
  <div class="mod-role">
    <el-form :inline="true" @keyup.enter.native="doSearch()">
      <el-form-item>
        <el-input v-model="gridOptions.dataSource.serviceInstanceInputParameters.searchKey" placeholder="登陆名或者姓名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="doSearch()">查询</el-button>
        <el-button @click="doExportExcel()">导出</el-button>
      </el-form-item>
    </el-form>
    <t-grid ref="searchReulstList" :options="gridOptions">
    </t-grid>
  </div>
</el-dialog>
</template>

<script>
export default {
  data() {
    return {
      visible: false,
      gridOptions: {
        dataSource: {
          loadDataOnFirst: false,
          serviceInstance: tapp.services.base_User.getRoleCategoryUsers,
          serviceInstanceInputParameters: {
            searchKey: null,
            roleCategoryId:null,
          }
        },
        grid: {
          mutiSelect: false,
          operates: {
            width: 120,
            fixed: 'left',
            list: [{
              type: 'text',
              show: true,
              label: '选择',
              method: this.doSelect,
            }, ]
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
              prop: 'mobile',
              label: '电话',
              sortable: true,
              minWidth: 120
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
  components: {},
  created() {

  },
  methods: {
    // 初始化
    init(roleCategoryId) {
      this.visible = true;

      this.$nextTick(() => {
        this.gridOptions.dataSource.serviceInstanceInputParameters.roleCategoryId = roleCategoryId;
        this.$refs.searchReulstList.refresh();
      })
    },
    doSelect(key, row) {
      this.visible = false;
      this.$emit('select', row);
    },
    doExportExcel() {
      this.$refs.searchReulstList.exportCSV('用户列表');
    },
    doSearch() {
      this.$refs.searchReulstList.refresh();
    }
  }
}
</script>
