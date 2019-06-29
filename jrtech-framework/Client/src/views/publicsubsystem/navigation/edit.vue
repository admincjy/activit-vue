<template>
<el-dialog :title="title" :close-on-click-modal="false" :visible.sync="visible">
  <div>
    <el-form :model="model" ref="ruleForm" @keyup.enter.native="doSave()" label-width="160px">
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="名称" prop="name" verify class="is-required" :maxLength="50">
            <el-input v-model="model.name"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="标题" prop="title" verify class="is-required" :maxLength="50">
            <el-input v-model="model.title"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="路径" prop="path" verify can-be-empty  :maxLength="200">
            <el-input v-model="model.path"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="图标" prop="icon" verify  can-be-empty :maxLength="200">
            <el-input v-model="model.icon"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="组件地址" prop="component" verify  can-be-empty :maxLength="200">
            <el-input v-model="model.component"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="关联权限" prop="requiredPermissionId" verify class="is-required" :maxLength="50">
            <t-cascader v-model="model.requiredPermissionId" :options="requiredPermissionIdOptions"></t-cascader>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="在导航菜单是否显示" prop="show" verify class="is-required">
            <el-switch v-model="model.show"></el-switch>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="保存页面状态" prop="keepAlive" verify class="is-required">
            <el-switch v-model="model.keepAlive"></el-switch>
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
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="备注" prop="remark" verify can-be-empty :maxLength="200">
            <el-input v-model="model.remark"></el-input>
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
      requiredPermissionIdOptions:{
        dataSource: {
          serviceInstance: tapp.services.base_Permission.getPermissions,
          serviceInstanceInputParameters: {}
        },
      },
      model: {
        requiredPermissionId:'common', //普通权限
        show: true,
        keepAlive: false,
        layoutType:0,
      },
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
        return '修改菜单导航';
      } else {
        return (this.parentId == null ? '新增根结点-菜单导航' : '新增子结点-菜单导航') + this.parentTitle;
      }
    }
  },
  methods: {
    load() {
      let self = this;
      if (self.docId) {
        tapp.services.base_Navigation.get(self.docId).then(function(result) {
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
          tapp.services.base_Navigation.save(requestModel).then(function(result) {
            self.model = result;
            self.formAction = 1;
            self.$notify.success({
              title: '操作成功！',
              message: '保存菜单导航成功，注销后重新登陆系统后修改生效！',
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
      self.$confirm('此操作将永久删除菜单导航, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        tapp.services.base_Navigation.delete(self.docId).then(function(result) {
          self.formAction = 1;
          self.doClose();
          self.$notify.success({
            title: '系统删除成功',
            message: '菜单导航已删除成功，注销后重新登陆系统后修改生效！'
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
<!-- <el-input type="textarea" :rows="3" v-model="model.applyRemark"></el-input> -->
