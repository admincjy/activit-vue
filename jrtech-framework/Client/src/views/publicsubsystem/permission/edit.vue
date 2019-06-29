<template>
<el-dialog :title="title" :close-on-click-modal="false" :visible.sync="visible">
  <div>
    <el-form :model="model" ref="ruleForm" @keyup.enter.native="doSave()" label-width="140px">
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="名称" prop="name" verify class="is-required" :maxLength="50">
            <el-input v-model="model.name"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="备注" prop="remark" verify can-be-empty :maxLength="200">
            <el-input v-model="model.remark"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="排序号" prop="sortIndex" verify class="is-required">
            <el-input v-model="model.sortIndex"></el-input>
          </el-form-item>
        </el-col>
      </el-row> 
    </el-form>
  </div>
  <span slot="footer" class="dialog-footer">
    <el-button type="primary" @click="doSave()">确定</el-button>
	<el-button :disabled="formAction == 0" type="danger" @click="doDelete()">删除</el-button>
    <el-button @click="doClose()">取消</el-button>
  </span>
</el-dialog>
</template>
<script>
import {
  mapMutations,
  mapState,
} from 'vuex'
export default {
  data() {
    return {
      visible: false,
      docId: null,
      parentId: null,
      parentTitle: null,
      model: {},
      formAction: 0, // 0：add,1:edit
    }
  },
  components: {},
  created() {},
  activated() {},
  computed: {
    ...mapState({
      //currentUser: state => state.app.user,
    }),
    title: function() {
      if (this.docId) {
        return '修改功能权限';
      } else {
        return this.parentId == null ? '新增根结点-功能权限' : '新增子结点-功能权限-' + this.parentTitle;
      }
    }
  },
  methods: {
    load() {
      let self = this;
      if (self.docId) {
        tapp.services.base_Permission.get(self.docId).then(function(result) {
          self.model = result;
          self.formAction = 1;
        });
      } else {
        self.model.id = null;
        self.model.parentId = self.parentId;
        self.formAction = 0;
      }
    },
    init(parentId, parentTitle, id) {
      this.visible = true;
      this.parentId = parentId;
      this.parentTitle = parentTitle;
      this.docId = id;
      this.$nextTick(() => {
        this.$refs.ruleForm.resetFields();
      })
      this.load();
    },
    doSave(formName) {
      let self = this;
      let validPromises = [self.$refs['ruleForm'].validate()];
      Promise.all(validPromises)
        .then(resultList => {
          let requestModel = { ...self.model
          };
          tapp.services.base_Permission.save(requestModel).then(function(result) {
            self.model = result;
            self.formAction = 1;
            self.$notify.success({
              title: '操作成功！',
              message: '保存功能权限成功！',
            });
          });
        }).catch(function(e) {
          self.$notify.error({
            title: '错误',
            message: '系统输入验证失败！'
          });
          return false;

        });
    },
    doDelete() {
      let self = this;
      self.$confirm('此操作将永久删除功能权限, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        tapp.services.base_Permission.delete(self.docId).then(function(result) {
          self.formAction = 1;
          self.doClose();
          self.$notify.success({
            title: '系统删除成功',
            message: '功能权限已删除成功！'
          });

        })
      });
    },
    doClose() {
      this.visible = false;
      this.$emit('change');
    },
  }
}
</script>
