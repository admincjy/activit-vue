<template>
<div class="mod-role">
<el-form ref="ruleForm" @keyup.enter.native="doSearch()" label-width="120px">
  <el-row :gutter="20">
    <el-col :span="9">
      <el-form-item label="组织机构">
        <base-organization-select v-model="gridOptions.dataSource.serviceInstanceInputParameters.organizationId" placeholder="请选择">
        </base-organization-select>
      </el-form-item>
    </el-col>
    <el-col :span="7">
      <el-form-item label="产品类别">
        <pl-loanProducttype-select v-model="gridOptions.dataSource.serviceInstanceInputParameters.loanProductSubTypeId" />
      </el-form-item>
    </el-col>
    <el-col :span="8">
      <el-form-item label="客户经理">
        <base-user-select role-category="base_rolecategory_trackingpersoninfomr" v-model="gridOptions.dataSource.serviceInstanceInputParameters.docOwnUserId" placeholder="请选择">
        </base-user-select>
      </el-form-item>
    </el-col>
  </el-row>
  <el-row :gutter="20">
    <el-col :span="9">
      <el-form-item label="任务分配时间">
        <t-datetime-range-picker v-model="dateRange" @change="onDateRangeChanged">
        </t-datetime-range-picker>
      </el-form-item>
    </el-col>
    <el-col :span="7">
      <el-form-item label="通用查询">
        <el-input  v-model="gridOptions.dataSource.serviceInstanceInputParameters.searchKey" placeholder="申请编号、客户名称、身份证号" clearable></el-input>
      </el-form-item>
    </el-col>
    <el-col :span="8">
      <el-form-item>
        <el-button icon="el-icon-search" type="primary" @click="doSearch()">查询</el-button>
        <el-button icon="el-icon-download" @click="doExportExcel()">导出</el-button>
      </el-form-item>
    </el-col>
  </el-row>
</el-form>
  <t-grid ref="searchReulstList" :options="gridOptions">
  </t-grid>
</div>
</template>

<script>
export default {
  data() {
    return {
      dateRange: null,
      gridOptions: {
        dataSource: {
          serviceInstance: tapp.services.pL_Report.getLoanApplyProgessList,
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
              label: '详情',
              method: this.doDetail,
            }, ]
          }, // 列操作按钮
          columns: [{
              prop: 'customerCode',
              label: '业务编号',
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
              prop: 'customerCardNO',
              label: '身份证号',
              sortable: true,
              width: 170
            },
            {
              prop: 'loanProductTypePath',
              label: '产品类型',
              sortable: true,
              width: 150
            },
            {
              prop: 'loanApplyDate',
              label: '申请日期',
              sortable: true,
              width: 100,
              formatter: (row, column, cellValue) => {
                return this.$util.dateFormat(row.loanApplyDate);
              }
            },
            {
              prop: 'processInstEndDate',
              label: '结束日期',
              sortable: true,
              width: 110,
              formatter: (row, column, cellValue) => {
                return this.$util.dateFormat(row.processInstEndDate, 'YYYY-MM-DD');
              }
            },
            {
              prop: 'processInstStatus',
              columnKey: 'processInstStatus',
              label: '状态',
              sortable: true,
              width: 90,
              filters: [{
                text: '审批中',
                value: 'Approving'
              }, {
                text: '驳回提单',
                value: 'ReRequest'
              }, {
                text: '挂起',
                value: 'Suspended'
              }, {
                text: '审批完成',
                value: 'Finished'
              }, {
                text: '拒绝',
                value: 'Declined'
              }],
              formatter: (row, column, cellValue) => {
                switch (row.processInstStatus) {
                  case 'Approving':
                    return '审批中';
                  case 'ReRequest':
                    return '驳回提单';
                  case 'Suspended':
                    return '挂起';
                  case 'Finished':
                    return '审批完成';
                  case 'Declined':
                    return '拒绝';
                  default:
                    return row.processInstStatus;
                }
              },
            },
            {
              prop: 'taskAssigneeName',
              label: '当前审批人',
              sortable: true,
              width: 120
            },
            {
              prop: 'taskActName',
              label: '任务结点',
              sortable: true,
              width: 100
            },
            {
              prop: 'taskCreateDate',
              label: '任务分配时间',
              sortable: true,
              width: 150,
              formatter: (row, column, cellValue) => {
                return this.$util.datePatternFormat(row.taskCreateDate, 'YYYY-MM-DD HH:mm');
              }
            },
            {
              prop: 'trackingPersonInfoMRName',
              label: '客户经理',
              sortable: true,
              width: 110
            },
            {
              prop: 'docOwnDepartmentName',
              label: '所属营业部',
              sortable: true,
              minWidth: 150,
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

  },
  methods: {
    doDetail(key, row) {　
      let path = '/loansubsystem/report/loanApplyProcessDetail?procInstId=' + row.processInstId + '&id=' + row.id;
      //办理
      this.$router.push({
        path: path,
      });
    },
    onDateRangeChanged(val) {
      this.gridOptions.dataSource.serviceInstanceInputParameters.startDate = val[0];
      this.gridOptions.dataSource.serviceInstanceInputParameters.endDate = val[1];
    },
    doExportExcel() {
      this.$refs.searchReulstList.exportCSV('进度查询');
    },
    doSearch() {
      this.$refs.searchReulstList.refresh();
    }
  }
}
</script>
