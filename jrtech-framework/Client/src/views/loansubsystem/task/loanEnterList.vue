<template>
<div class="mod-role">
  <el-form :inline="true" @keyup.enter.native="doSearch()">
    <el-form-item>
      <el-input style="width:300px" prefix-icon="el-icon-search" @keyup.enter.native="doSearch()" v-model="gridOptions.dataSource.serviceInstanceInputParameters.searchKey" placeholder="申请编号、客户名称、身份证号" clearable></el-input>
    </el-form-item>
    <el-form-item>
      <el-button @click="doSearch()" icon="el-icon-search">查询</el-button>
      <el-button type="primary" @click="doNew()" icon="el-icon-plus">新增</el-button>
      <el-button icon="el-icon-download"  @click="doExportExcel()">
        <i class="fa fa-lg fa-level-down"></i>导出
      </el-button>
    </el-form-item>
  </el-form>
  <t-grid ref="searchReulstList" :options="gridOptions">
  </t-grid>
</div>
</template>

<script>
export default {
  data() {
    return {
      gridOptions: {
        dataSource: {
          serviceInstance: tapp.services.pL_LoanDocTask.getDraftedOrReRequestList,
          serviceInstanceInputParameters: {
            searchKey: null,
          }
        },
        grid: {
          mutiSelect: false,
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
              prop: 'customerCode',
              label: '申请编号',
              sortable: true,
              width: 120
            },
            {
              prop: 'customerName',
              label: '客户名称',
              sortable: true,
              width: 100
            },
            {
              prop: 'customerMobile',
              label: '联系电话',
              sortable: true,
              width: 120
            },
            {
              prop: 'loanProductTypePath',
              label: '产品类型',
              sortable: true,
              width: 150
            },
            {
              prop: 'loanMoneyAmount',
              label: '申请金额',
              sortable: true,
              width: 120,
              formatter: (row, column, cellValue) => {
                return this.$util.moneyFormat(row.loanMoneyAmount);
              }
            },
            {
              prop: 'loanTermCount',
              label: '借款期限',
              sortable: true,
              width: 100
            },

            {
              prop: 'loanApplyDate',
              label: '申请日期',
              sortable: true,
              width: 120,
              formatter: (row, column, cellValue) => {
                return this.$util.dateFormat(row.loanApplyDate);
              }
            },
            {
              prop: 'processInstStatus',
              columnKey: 'processInstStatus',
              label: '状态',
              width: 80,
              filters: [{
                text: '草稿',
                value: 'Draft'
              }, {
                text: '驳回',
                value: 'ReRequest'
              }],
              formatter: (row, column, cellValue) => {
                return row.processInstStatus === 'ReRequest' ? '驳回' : '草稿';
              },
              render: (h, params) => {
                return h('el-tag', {
                  props: {
                    type: params.row.processInstStatus === 'ReRequest' ?  'warning' : 'info'
                  } // 组件的props
                }, params.row.processInstStatus === 'ReRequest' ? '驳回' : '草稿')
              }
            },
            {
              prop: 'trackingPersonInfoMRName',
              label: '客户经理',
              sortable: true,
              width: 100
            },
            {
              prop: 'docOwnDepartmentName',
              label: '所属营业部',
              sortable: true,
              minWidth: 150,
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
        name: 'pl_loanapplyInput',
      });
    },
    doEdit(key, row) {
      let tpath = row.taskFormUrl != null ? row.taskFormUrl : '/loansubsystem/task/loanEnterForm?id=' + row.id;

      this.$router.push({
        path: tpath
      });
    },
    doExportExcel() {
      this.$refs.searchReulstList.exportCSV('借款单据列表');
    },
    doSearch() {
      this.$refs.searchReulstList.refresh();
    }
  }
}
</script>
