<template>
<div class="mod-role">
  <el-form :inline="true">
    <el-form-item>
      <el-input v-model="gridOptions.dataSource.serviceInstanceInputParameters.searchKey" placeholder="标题" clearable></el-input>
    </el-form-item>
    <el-form-item>
      <el-button @click="doSearch()">查询</el-button>
      <el-button @click="doRead()" :disabled="selectedRows.length <= 0">标为已读</el-button>
      <el-button type="primary" v-if="$util.hasPermission('gl_notification_send')" @click="doSend()"><i class="fa fa-send" style="margin-right: 15px"></i>发送系统消息
      </el-button>
    </el-form-item>
  </el-form>
  <t-grid ref="searchReulstList" :options="gridOptions" @selection-change="handleSelectionChange" @data-change="handleDataChange">
  </t-grid>
  <div style="text-align: left">
    <send-form v-if="sendFormVisible" ref="sendForm" @change="doSearch"></send-form>
  </div>
</div>
</template>
<script>
import SendForm from './send'
export default {
  data() {
    return {
      sendFormVisible: false,
      selectedRows: [],
      gridOptions: {
        dataSource: {
          serviceInstance: tapp.services.base_Notification.getList,
          serviceInstanceInputParameters: {
            searchKey: null,
          }
        },
        grid: {
          columns: [{
              prop: 'title',
              label: '标题',
              sortable: false,
              render: (h, params) => {
                var self = this;
                return h('t-popover-hyperlink', {
                  props: {
                    id: params.row.id,
                    text: params.row.title,
                    content: params.row.content,
                    url: params.row.url,
                  }, // 组件的props
                  on: { 
                    go: function(id) {
                      self.doReadOnHide(id);
                    },
                  }
                })
              }
            },
            {
              prop: 'gmtCreatedOn',
              label: '发送时间',
              sortable: true,
              width: 180,
              formatter: (row, column, cellValue) => {
                return this.$util.dateFormat(row.gmtCreatedOn, 'YYYY-MM-DD HH:mm:ss');
              }
            },

          ], // 需要展示的列
          defaultSort: {
            prop: 'id',
            order: 'descending'
          },
        }
      },
    }
  },
  components: {
    SendForm,
  },
  mounted() {},
  computed: {

  },
  methods: {
    handleSelectionChange(val) {
      this.selectedRows = val;
    },
    handleDataChange(data) {
      this.$store.commit('setNotificationNum', data.totalCount);
    },
    doRead() {
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
      tapp.services.base_Notification.read(ids).then(function(result) {
        self.doSearch();
        self.$notify.success({
          title: '操作成功',
          message: '标记成功！',
        });
      });
    },
    doReadOnHide(id) {
      let self = this;
      let ids = [id];
      tapp.services.base_Notification.read(ids).then(function(result) {
        self.doSearch();
        self.$notify.success({
          title: '操作成功',
          message: '标记成功！',
        });
      });
    },
    doSend() {
      this.sendFormVisible = true;
      this.$nextTick(() => {
        this.$refs.sendForm.init()
      })
    },
    doSearch() {
      this.$refs.searchReulstList.refresh();
    }
  }
}
</script>
