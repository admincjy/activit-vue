
<template>
<el-dialog :title="'发送系统消息'" :close-on-click-modal="false" :visible.sync="visible">
  <el-form :model="model" ref="ruleForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="标题" prop="title" verify :maxLength="255">
      <el-input v-model="model.title"></el-input>
    </el-form-item>
    <el-form-item label="内容" prop="content" verify :maxLength="500">
      <t-editor v-model="model.content"></t-editor>
    </el-form-item>
  </el-form>
  <span slot="footer" class="dialog-footer">
      <el-button type="primary" @click="dataFormSubmit()">发送</el-button>
      <el-button @click="visible = false">取消</el-button>
    </span>
</el-dialog>
</template>

<script>
export default {
  data() {
    return {
      visible: false,
      model: {
        title: '',
        content: '',

      },
    };
  },
  created() {


  },
  activated() {},
  methods: {
    init() {
      this.visible = true;
      this.model = {
      };

      this.$nextTick(() => {
        this.$refs.ruleForm.resetFields();
      });
    },
    dataFormSubmit() {
      let self = this;
      self.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          let model = self.model;
          tapp.services.base_Notification.saveAndNotify(model).then(function(result) {
            self.$notify.success({
              title: '操作成功',
              message: '发送成功！',
              duration: 1500,
              onClose: () => {
                self.visible = false;
                self.$emit('change');
              }
            });
          });

        } else {
          self.$notify.error({
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

<style> 
.edui-editor{
  z-index: 2000;
}
</style>
