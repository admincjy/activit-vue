
<template>
<el-dialog :title="formAction == 0 ? '新增' : '修改'" :close-on-click-modal="false" :visible.sync="visible">
  <el-form :model="model" ref="ruleForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="名称" prop="name" verify>
      <el-input v-model="model.name"></el-input>
    </el-form-item>
    <el-form-item label="必输" prop="required">
      <el-switch v-model="model.required"></el-switch>
    </el-form-item>
    <el-form-item label="排序号" prop="sortIndex" verify class="is-required">
      <el-input type="number" min="0" max="1000" step="1" v-model="model.sortIndex"></el-input>
    </el-form-item>
    <el-form-item label="备注" prop="remark" verify can-be-empty :maxLength="200">
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
    init(categoryId,id) {
      this.visible = true;
      if (id) {
        let self = this;
        tapp.services.base_Attachment.getCategory(id).then(function(result) {
          self.model = result;
          self.formAction = 1;
        });
      }else{
        this.formAction = 0;
        this.model = {};
        this.model.categoryClassificationId = categoryId;
      }
    },
    dataFormSubmit() {
      let self = this;
      self.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          let model = self.model;
          model.required = !!model.required;
          tapp.services.base_Attachment.saveCategory(model).then(function(result) {
            self.model.id = result.id;
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
    doDelete() {
      let self = this;
      self.$confirm('此操作将永久删除, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        tapp.services.base_Attachment.deleteCategory(self.model.id).then(function(result) {
          self.formAction = 0;

          self.$notify.success({
            title: '操作成功',
            message: '删除成功！',
            duration: 1500,
            onClose: () => {
              self.visible = false;
              self.$emit('change');
            }
          });
        })
      });
    },
  }
  }
  </script>
