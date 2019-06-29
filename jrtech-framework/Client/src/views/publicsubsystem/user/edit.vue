<template>
<el-dialog :title="formAction == 0 ? '新增用户信息' : '修改用户信息'" :close-on-click-modal="false" :visible.sync="visible">
  <el-tabs v-model="tabActive" @tab-click="handleTabClick">
    <el-tab-pane label="基本信息" name="userInfo">
      <el-form :model="model" ref="ruleForm" label-width="160px" v-show="tabActive =='userInfo'">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="登陆名" prop="loginId" verify :maxLength="50" class="is-required">
              <el-input v-model="model.loginId"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="密码" prop="loginPassword" v-if="formAction == 0 " :maxLength="128" :verify="validateLoginPassword" class="is-required">
              <el-input v-model="model.loginPassword" type="password"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="姓名" prop="name" verify :maxLength="50" class="is-required">
              <el-input v-model="model.name"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email" verify can-be-empty :maxLength="45">
              <el-input v-model="model.email"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item prop="mobile" label="手机" verify can-be-empty :maxLength="45">
              <el-input v-model="model.mobile"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否启用" prop="activited" verify>
              <el-switch v-model="model.activited"></el-switch>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="首次登录必须修改密码" prop="shouldChangePassword" verify>
              <el-switch v-model="model.shouldChangePassword"></el-switch>
            </el-form-item>
          </el-col>
          <el-col :span="12">
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col>
            <el-form-item label="备注" prop="remark" verify can-be-empty :maxLength="200">
              <el-input :autosize="{ minRows: 2, maxRows: 4}" v-model="model.remark"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-tab-pane>
    <el-tab-pane label="所属岗位" name="roleInfo">
      <t-tree ref="userRoleTree" :options="userRoleTreeOptons" v-show="tabActive =='roleInfo'">
      </t-tree>
    </el-tab-pane>
  </el-tabs>
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
      tabActive: 'userInfo',
      model: {
        activited: true,
        shouldChangePassword: true,
      },
      userRoleTreeOptons: {
        dataSource: {
          serviceInstance: tapp.services.base_Role.getOrgRoleTree,
          serviceInstanceInputParameters: {}
        },
        tree: {
          nodeKey: 'id',
          defaultCheckedKeys: []
        }
      }
    };
  },
  created() {

  },
  activated() {},
  methods: {
    init(id) {
      this.visible = true;
      this.tabActive = 'userInfo';
      let self = this;
      if (id) {
        tapp.services.base_User.getUser(id).then(function(result) {
          self.model = result;
          self.$refs.ruleForm.resetFields();
          self.$refs.userRoleTree.setCheckedKeys(result.roleIds);
          self.formAction = 1;
        });
      } else {
        self.model = {
          activited: true,
          shouldChangePassword: true,
        };

        self.$nextTick(() => {
          self.$refs.ruleForm.resetFields();
          self.$refs.userRoleTree.setCheckedKeys([]);
          self.formAction = 0;
        })
      }
    },
    validateLoginPassword(rule, value, callback) {
      if (!(/^(?![0-9]+$)(?![a-zA-Z]+$)(?!(.)\1{5}).{8,16}$/.test(value))) {
        callback(new Error('密码强度弱，长度必须在8位和16位数之间，包含字母数字'));
      }
      callback();
    },
    handleTabClick(tab, event) {
      if (!tab) {
        return;
      }
      this.tabActive = tab.name;
    },

    dataFormSubmit() {
      let self = this;
      self.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          let model = self.model;
          model.roleIds = self.$refs.userRoleTree.getCheckedKeys();
          tapp.services.base_User.saveUser(model).then(function(result) {
            self.model.id = result.id;
            self.formAction = 1;
            self.$notify.success({
              title: '操作成功',
              message: '用户信息已保存成功！',
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
      self.$confirm('此操作将永久删除用户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        tapp.services.base_User.delete(self.model.id).then(function(result) {
          self.formAction = 0;
          self.$notify.success({
            title: '系统删除成功',
            duration: 1500,
            message: '用户信息已删除成功！',
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
