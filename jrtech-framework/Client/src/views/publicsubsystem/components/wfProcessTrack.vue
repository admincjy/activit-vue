<template>
<div>
  <t-grid ref="searchReulstList" :options="gridOptions">
  </t-grid>
  <div style="overflow:scroll;">
    <img v-lazy="url" style="margin-top:20px;" />
  </div>
</div>
</template>
<style>

</style>
<script>
export default {
  // 使用时请使用 :url.sync=""传值
  props: {
    procInstId: {
      required: true
    }
  },
  data() {
    return {
      url: '',
      gridOptions: {
        dataSource: {
          loadDataOnFirst: false,
          serviceInstance: tapp.services.wF_ProcessInstHistory.getProcessInstHistoryList,
          serviceInstanceInputParameters: {
            categoryId: null,
          }
        },
        grid: {
          pageable: false,
          mutiSelect: false,
          columns: [{
              prop: 'actName',
              label: '节点名称',
              sortable: true,
              width: 150
            },
            {
              prop: 'actUserName',
              label: '经办人',
              sortable: true,
              width: 120
            },
            {
              prop: 'endDate',
              label: '办理时间',
              sortable: true,
              width: 180,
              formatter: (row, column, cellValue) => {
                return this.$util.datetimeFormat(row.endDate);
              }
            },
            {
              prop: 'result',
              label: '办理结果',
              sortable: true,
              width: 120,
            },
            {
              prop: 'remark',
              label: '审批意见',
              sortable: false,
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

  watch: {
    procInstId: {　　　　
      handler(newValue, oldValue) {　
        if(!newValue){
          return;
        }
        this.url = window.SITE_CONFIG['serverUrl'] + '/wf/processTrackImage.html?ProcessInstanceId=' + newValue;
        this.$nextTick(() => {
          this.gridOptions.dataSource.serviceInstanceInputParameters.categoryId = newValue;
          this.$refs.searchReulstList.refresh();
        });
      },
      deep: true,
      immediate: true,
      　
    },
  },
  created() { 
  },
  methods: {}
}
</script>
