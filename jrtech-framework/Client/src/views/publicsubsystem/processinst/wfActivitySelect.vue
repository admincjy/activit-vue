
<template>
<el-dialog title="流程节点信息" :visible.sync="visible" :append-to-body="true">
  <el-form ref="ruleForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="流程节点">
      <el-select placeholder="流程节点" v-model="selectedActivityId">
        <el-option v-for="(item, index) in activitylist" :key='item.id' :label="item.name" :value="item.id"></el-option>
      </el-select>
    </el-form-item>
  </el-form>
  <span slot="footer" class="dialog-footer">
      <el-button type="primary" @click="onOk()">确定</el-button>
      <el-button @click="visible = false">取消</el-button>
    </span>
</el-dialog>
</template>

<script>
export default {
  data() {
    return {
      visible: false,
      processDefinationKey: null,
      activitylist: {},
      selectedActivityId: null,
    }
  },
  created() {

  },
  activated() {

  },
  methods: {
    // 初始化
    init(processDefinationKey) {
      this.visible = true
      this.processDefinationKey = processDefinationKey;
      let self = this;
      tapp.services.wf_Model.getProcessActivities(this.processDefinationKey).then(function(result) {
        self.activitylist = result.filter(p => {
          return p.implementation != undefined || p.assignee != undefined
        });

      });
    },
    onOk() {
      let self = this;
      if (!this.selectedActivityId) {
        self.$notify.error({
          title: '错误',
          message: '未选择流程节点！'
        });
        return false;
      }
      this.visible = false;
      this.$emit('select', this.selectedActivityId);
    },
  }
}
</script>
