<template>
<div class="mod-role">
  <el-form :inline="true" @keyup.enter.native="doSearch()">
    <el-form-item>
      <el-input style="width:300px" v-model="gridOptions.dataSource.serviceInstanceInputParameters.searchKey" placeholder="申请编号、客户名称、身份证号" clearable></el-input>
    </el-form-item>
    <el-form-item>
      <el-button icon="el-icon-search" type="primary" @click="doSearch()">查询</el-button>
      <el-button icon="el-icon-download" @click="doExportExcel()">导出</el-button>
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
          loadDataOnFirst: false,
          serviceInstance: tapp.services.pL_LoanDocTask.getTaskList,
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
              label: '审批',
              render: this.renderOpBtnLabel,
              method: this.doEdit,
            }, ]
          }, // 列操作按钮
          columns: [{
              prop: 'customerCode',
              label: '申请编号',
              sortable: true,
              width: 150
            },
            {
              prop: 'customerName',
              label: '客户名称',
              sortable: true,
              width: 120
            },
            {
              prop: 'customerCardNO',
              label: '身份证号',
              sortable: true,
              width: 180
            },
            {
              prop: 'loanProductTypePath',
              label: '产品类型',
              sortable: true,
              width: 180
            },
            {
              prop: 'loanMoneyAmount',
              label: '金额',
              sortable: true,
              width: 100,
              formatter: (row, column, cellValue) => {
                return this.$util.moneyFormat(row.loanMoneyAmount);
              }
            },
            {
              prop: 'loanTermCount',
              label: '期数',
              sortable: true,
              width: 80
            },
            {
              prop: 'taskActName',
              label: '审批结点',
              sortable: true,
              width: 120
            },
              {
              prop: 'taskActUserName',
              label: '审批人',
              sortable: true,
              width: 120
            },
            {
              prop: 'taskCreateDate',
              label: '任务创建日期',
              sortable: true,
              minWidth: 150,
              formatter: (row, column, cellValue) => {
                return this.$util.dateFormat(row.taskCreateDate);
              }
            },
          ], // 需要展示的列
          defaultSort: {
            prop: 'taskId',
            order: 'descending'
          },
        }
      }
    }
  },
  components: {},
  created() {

    this.$nextTick(() => {
      this.doSearch();
    });
  },
  methods: {
    renderOpBtnLabel(key, row) {
      if (row.taskAssignee != null && row.taskAssignee.length > 0) {
        return "办理";
      } else if (row.taskCandidate != null && row.taskCandidate.length > 0) {
        return "签收";
      }
      return "未知";
    },
    doEdit(key, row) {
      let self = this;
      if (row.taskAssignee != null && row.taskAssignee.length > 0) {
        //办理
        self.$router.push({
          path: row.taskFormUrl,
        });
        return;
      } else if (row.taskCandidate != null && row.taskCandidate.length > 0) {
        // "签收";
        tapp.services.pL_LoanAudit.claim({
          taskId: row.taskId,
          claim: true,
          action: 'claim',
          result:'签收',
          taskRemark: '',
          docId: self.docId
        }).then(function(result) {
          self.doSearch();
          self.$notify.success({
            title: '操作成功！',
            message: '签收任务成功！',
          });
        });
      } else {
        alert('do what?')
      }
    },
    doExportExcel() {
      this.$refs.searchReulstList.exportCSV('待办借款单据列表');
    },
    doSearch() {
      this.$refs.searchReulstList.refresh();
    }
  }
}
</script>
