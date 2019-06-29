
<template>
<el-dialog :title="formAction == 0 ? '新增定时任务' : '修改定时任务'" :visible.sync="visible" :append-to-body="true">
  <el-form :model="model"  ref="ruleForm" @keyup.enter.native="dataFormSubmit()" label-width="120px">
    <el-form-item label="名称" prop="name" verify :maxLength="50"  class="is-required">
      <el-input v-model="model.name"></el-input>
    </el-form-item>
    <el-form-item label="bean名称" prop="beanName" verify :maxLength="200"  class="is-required">
      <el-input v-model="model.beanName"></el-input>
    </el-form-item>
    <el-form-item label="方法名称" prop="methodName" verify :maxLength="100"  class="is-required">
      <el-input v-model="model.methodName"></el-input>
    </el-form-item>
    <el-form-item prop="params" label="参数" verify  can-be-empty  :maxLength="2000">
      <el-input v-model="model.params"></el-input>
    </el-form-item>
    <el-form-item prop="cronExpression" label="cron表达式" verify :maxLength="100"  class="is-required">
      <el-input v-model="model.cronExpression"></el-input>
    </el-form-item>
    <el-form-item label="描述" prop="remark" verify can-be-empty :maxLength="255">
      <el-input type="textarea" v-model="model.remark"></el-input>
    </el-form-item>
  </el-form>
  <span slot="footer" class="dialog-footer">
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
          <el-button @click="visible = false">取消</el-button>
    </span>
</el-dialog>
</template>

<script>
export default {
  data() {
    return {
      visible: false,
      formAction:0,
      model: {},
    }
  },
  created() {

  },
  activated() {

  },
  methods: {
    // 初始化
    init(id) {
      this.visible = true;
      if (id) {
        let self = this;
        tapp.services.base_ScheduleJob.get(id).then(function(result) {
          self.model = result;
          self.formAction = 1;
          self.$refs.ruleForm.resetFields();
        });
      } else {
        this.$nextTick(() => {
          this.model = {};
          this.formAction = 0;
          this.$refs.ruleForm.resetFields();
        })
      }
    },
    dataFormSubmit(formName) {
      let self = this;
      self.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          tapp.services.base_ScheduleJob.save(self.model).then(function(result) {
            self.$notify.success({
              title: '操作成功！',
              message: '修改定时任务成功！',
              onClose: function() {
                self.visible = false;
                self.$emit('change');
              }
            });
          });
        } else {
          this.$notify.error({
            title: '错误',
            message: '系统输入验证失败！'
          });
          return false;
        }
      });
    },
  }
}
</script>
