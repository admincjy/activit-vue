
<template>
<div class="main">
  <h3 class="login-title">首次登陆系统，请修改密码</h3>
  <el-form :model="model" ref="ruleForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="姓名">
      <span>{{ model.name }}</span>
    </el-form-item>
    <el-form-item label="账号">
      <span>{{ model.loginId }}</span>
    </el-form-item>
    <el-form-item label="原密码" prop="oldPassword" :maxLength="128" class="is-required" :verify="validateLogiPassword">
      <el-input type="password" v-model="model.oldPassword"></el-input>
    </el-form-item>
    <el-form-item label="新密码" prop="newPassword" :maxLength="128" class="is-required" :verify="validateLogiPassword">
      <el-input type="password" v-model="model.newPassword"></el-input>
    </el-form-item>
    <el-form-item label="确认密码" prop="confirmNewPassword" :maxLength="128" class="is-required" :verify="validateConfirmPassword">
      <el-input type="password" v-model="model.confirmNewPassword"></el-input>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="dataFormSubmit()">确认修改密码</el-button>
    </el-form-item>
  </el-form>
</div>
</template>

<script>
import {
  mapState,
  mapGetters,
  mapActions
} from 'vuex'

export default {
  data() {
    return {
      model: {},
    }
  },
  created() {
    this.model.name = this.user.userDisplayName;
    this.model.loginId = this.user.userLoginId;
  },
  activated() {

  },
  computed: {
    ...mapState({
      user: state => state.app.user,
    }),
  },
  methods: {
    // 初始化
    init() {
      this.model = {};
      this.$nextTick(() => {
        this.$refs['ruleForm'].resetFields()
      })
    },
    validateLogiPassword(rule, value, callback) {
      if (!(/^(?![0-9]+$)(?![a-zA-Z]+$)(?!(.)\1{5}).{8,16}$/.test(value))) {
        callback(new Error('新密码强度弱，长度必须在8位和16位数之间，包含字母数字'));
      }
      callback();
    },
    validateConfirmPassword(rule, value, callback) {
      if (this.model.newPassword !== value) {
        callback(new Error('确认新密码与新密码不一致'))
      } else {
        callback()
      }
    },
    dataFormSubmit() {
      let self = this;
      self.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          tapp.services.base_User.changePassword(self.model).then(function(result) {
            self.$notify.success({
              title: '操作成功！',
              message: '修改密码成功，跳转登陆页面，重新登陆...！',
              duration:1000,
              onClose: function() {
                self.$store.commit('loginOut');
                window.location.href = window.SITE_CONFIG['domain'] + 'login';
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
<style lang="scss">
.main {
    position: absolute;
    left: 50%;
    width: 400px;
    margin-left: -200px;
    border: solid 1px #ebebeb;
    border-radius: 3px;
    transition: 0.2s;
    padding: 20px;
    margin-top: 100px;
}
</style>
