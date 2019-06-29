<template>
<nav class="site-navbar" :class="navbarClasses">
  <div class="site-navbar__header">
    <h1 class="site-navbar__brand" @click="$router.push({ name: 'home' })">
        <a class="site-navbar__brand-lg" href="javascript:;">{{appName}}</a>
        <a class="site-navbar__brand-mini" href="javascript:;">{{appShortName}}</a>
      </h1>
  </div>
  <div class="site-navbar__body clearfix">
    <el-menu class="site-navbar__menu" mode="horizontal">
      <el-menu-item class="site-navbar__switch" index="0" @click="switchSidebarCollapseHandle()">
        <icon-svg name="zhedie"></icon-svg>
      </el-menu-item>
    </el-menu>
    <div class="div-loading" v-show="isLoading">
      <div class="div-loading-inner"><i class="el-icon-loading"></i><span class="icon-name">系统加载中...</span>
      </div>
    </div>
    <el-menu class="site-navbar__menu site-navbar__menu--right" mode="horizontal">
      <el-menu-item index="2" id="base_notification">
        <div class="el-badge">
          <a href="#" target="_blank" v-on:click.stop.prevent="notificationHandle()">
            <icon-svg name="message" class="site-sidebar__menu-icon"></icon-svg>
            <span>消息</span>
          </a>
          <sup class="el-badge__content is-fixed">{{notificationNum}}</sup>
        </div>
      </el-menu-item>
      <el-menu-item index="3">
        <el-dropdown :show-timeout="0" placement="bottom">
          <span class="el-dropdown-link">
            <icon-svg name="profile" class="site-sidebar__menu-icon" ></icon-svg>
            <span>{{user.userDisplayName}}</span>
          <i class="el-icon-arrow-down el-icon--right"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="updatePasswordHandle()">修改密码</el-dropdown-item>
            <el-dropdown-item @click.native="profileHandle()">个人信息</el-dropdown-item>
            <el-dropdown-item @click.native="setTaskAgentHandle()">任务授权</el-dropdown-item>
            <el-dropdown-item @click.native="logoutHandle()">注销退出</el-dropdown-item>
            <el-dropdown-item @click.native="settingHandle()">风格设置</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </el-menu-item>
    </el-menu>
  </div>
  <!-- 弹窗, 修改密码 -->
  <update-password v-if="updatePassowrdVisible" ref="updatePassowrd"></update-password>
  <profile v-if="profileVisible" ref="profile"></profile>
  <stomp-client ref="stompClient"></stomp-client>
</nav>
</template>

<script>
import UpdatePassword from '../publicsubsystem/user/changePassword'
import Profile from '../publicsubsystem/user/profile'
import StompClient from './stompClient'

import {
  mapMutations,
  mapState,
} from 'vuex'
export default {
  data() {
    return {
      appName: window.SITE_CONFIG['appName'],
      appShortName: window.SITE_CONFIG['appShortName'],
      updatePassowrdVisible: false,
      profileVisible: false,
    }
  },
  components: {
    UpdatePassword,
    Profile,
    StompClient,
  },
  created() {

  },
  computed: {
    navbarClasses() {
      let type = this.$store.state.ui.navbarLayoutType
      return [!/\S/.test(type) || type === 'default' ? '' : `site-navbar--${type}`]
    },
    ...mapState({
      path: state => state.route.path,
      user: state => state.app.user,
      notificationNum: state => state.ui.notificationNum,
      isLoading: state => state.ui.isLoading,
    }),
  },
  mounted() {
  },
  methods: {
    // 切换侧边栏, 水平折叠收起状态
    switchSidebarCollapseHandle() {
      this.SWITCH_SIDEBAR_COLLAPSE({
        collapse: !this.$store.state.ui.sidebarCollapse
      })
    },

    // 修改密码
    updatePasswordHandle() {
      this.updatePassowrdVisible = true
      this.$nextTick(() => {
        this.$refs.updatePassowrd.init()
      })
    },
    profileHandle() {
      this.profileVisible = true
      this.$nextTick(() => {
        this.$refs.profile.init()
      })
    },
    // 任务授权
    setTaskAgentHandle() {
      this.$router.push({
        name: 'wf_taskagent'
      })
    },
    settingHandle() {
      this.$router.push({
        name: 'gl_setting'
      })
    },
    notificationHandle() {
      this.$router.push({
        name: 'gl_notification'
      })
    },
    // 退出
    logoutHandle() {
      let self = this;
      tapp.services.base_Account.loginout().then(function(response) {
          self.$store.commit('loginOut');
          window.location.href = window.SITE_CONFIG['domain'] + 'login';
        })
        .catch(function(error) {
          //self.$message.error(error);
		  window.console && console.log(error);
        });
    },
    ...mapMutations(['SWITCH_SIDEBAR_COLLAPSE', 'DELETE_CONTENT_TABS'])
  },
}
</script>
<style>
.div-loading {
  width: 200px;
  height: 50px;

  position: absolute;
  left: 50%;
  top: 50%;
  margin-left: -100px;
  margin-top: -25px;
}

.div-loading-inner {
  margin-left: 50px;
  margin-top: 18px;
}
</style>
