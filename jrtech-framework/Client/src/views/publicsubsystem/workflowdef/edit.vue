
<template>
<el-dialog :title="formAction == 0 ? '新增' : '修改'" :close-on-click-modal="false" :visible.sync="visible">
  <el-form :model="model" ref="ruleForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="标识" prop="key" verify :maxLength="255" class="is-required">
      <el-input v-model="model.key"></el-input>
    </el-form-item>

    <el-form-item label="名称" prop="name" verify :maxLength="255" class="is-required">
      <el-input v-model="model.name"></el-input>
    </el-form-item>
    <el-form-item label="分类" prop="category" verify :maxLength="255" class="is-required">
      <el-input v-model="model.category"></el-input>
    </el-form-item>
    <el-form-item label="描述" prop="desc" verify can-be-empty :maxLength="200">
      <el-input type="textarea" v-model="model.desc"></el-input>
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
      formAction: 0, //0 add,//1,edit
      visible: false,
      model: {

      },
    };
  },
  created() {

  },
  activated() {},
  methods: {
    init(id) {
      this.visible = true;
      if (id) {
        let self = this;
        tapp.services.wf_Model.get(id).then(function(result) {
          self.$refs.ruleForm.resetFields();
          self.model = result;
          self.formAction = 1;
        });
      }else{
        this.model = {};
        self.formAction = 0;

        self.$nextTick(() => {
          self.$refs.ruleForm.resetFields(); 
          self.formAction = 0;
        })
      }
    },
    dataFormSubmit() {
      let self = this;
      self.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          let model = self.model;
          tapp.services.wf_Model.save(model).then(function(result) {
            self.model = result;
            self.formAction = 1;
            self.$notify.success({
              title: '操作成功',
              message: '保存成功！',
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
