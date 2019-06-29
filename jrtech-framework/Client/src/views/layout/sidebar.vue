<template>
<aside class="site-sidebar" :class="sidebarClasses">
  <div class="site-sidebar__inner">
    <el-menu :default-active="menuNavActiveName" :collapse="$store.state.ui.sidebarCollapse" :collapseTransition="false" class="site-sidebar__menu" default-active="base_user">
      <sub-menu-nav v-for="menuNav in menuNavList" :key="menuNav.self.id" :menu-nav="menuNav">
      </sub-menu-nav>
    </el-menu>
  </div>
</aside>
</template>

<script>
import SubMenuNav from '@/components/sub-menu-nav'


import 'driver.js/dist/driver.min.css'
import Driver from 'driver.js/dist/driver.min.js'

import {
  mapMutations,
  mapState,
} from 'vuex'
import {
  getRouteNameByUrl
} from '@/util'
import isEmpty from 'lodash/isEmpty'
import appRouterHelper from "@/router/appRouterHelper"
import router from '@/router'

export default {
  data() {
    return {
      menuNavActiveName: null,
      menuNavList: [],
    }
  },
  components: {
    SubMenuNav
  },
  computed: {
    ...mapState({
      path: state => state.route.path,
    }),
    sidebarClasses() {
      let skin = this.$store.state.ui.sidebarLayoutSkin
      return [!/\S/.test(skin) || skin === 'light' ? '' : `site-sidebar--${skin}`]
    }
  },
  watch: {
    $route: 'routeHandle'
  },
  mounted() {

  },
  created() {
    this.getUserSystemInfo();
  },
  methods: {
    // 获取菜单导航列表 / 权限
    getUserSystemInfo() {
      let self = this;
      self.$http.post('/authapi/base_Account/getUserSystemInfo', {}).then(function(response) {
          let {
            authoritiyNavigationTree,
            notificationCount,
            permissions,
          } = response;
          self.menuNavList = authoritiyNavigationTree || [];
          self.routeHandle(self.$route);

          self.$store.commit('setNotificationNum', notificationCount);
          self.$store.commit('setPermissions', permissions);

          self.$nextTick(() => {
            self.showUserGuide();
          });
        })
        .catch(function(error) {
          window.console && console.log(error);
          //self.$message.error(error);
        });

    },
    // 路由操作
    routeHandle(route) {
      if (route.meta && route.meta.isTab) {
        var foundTab = this.$store.state.ui.contentTabs.filter(item => item.name == route.name);
        if (!foundTab || foundTab.length == 0) {
          let tab = {
            id: route.name,
            name: route.name,
            title: route.meta.title,
            type: (window.SITE_CONFIG.nestIframeRouteNameList || []).indexOf(route.name) !== -1 ? 'iframe' : 'module',
            url: route.fullPath,
            show: route.meta.show,
          }
          this.ADD_CONTENT_TAB(tab);
        }
        this.menuNavActiveName = route.name;
        this.UPDATE_CONTENT_TABS_ACTIVE_NAME({
          name: route.name
        })
      }
    },
    showUserGuide() {
      if (!document.getElementById('base_appcache')) {
        return;
      }
      const driver = new Driver();
      // Define the steps for introduction
      driver.defineSteps([{
          element: '#base_appcache',
          popover: {
            title: '系统对"登陆账号","岗位权限","配置项"，"菜单导航"，"数据字典"做了数据缓存，如果在数据库直接修改数据，在点击"重新加载缓存"按钮后修改生效',
            position: 'right'
          }
        },
        {
          element: '#base_organization',
          popover: {
            title: '维护组织机构、岗位、功能权限、数据权限',
            position: 'right'
          }
        },
        {
          element: '#base_user',
          popover: {
            title: '维护登陆账号、所属岗位、修改密码',
            position: 'right'
          }
        },
        {
          element: '#base_datadictionary',
          popover: {
            title: '维护前台下拉(dropdown)、单选(radio)、多选(checkedbox)数据字典',
            position: 'right'
          }
        },
        {
          element: '#base_log',
          popover: {
            title: '记录接口请求参数、执行时间、执行结果、错误内容',
            position: 'right'
          }
        },
        {
          element: '#base_attachmentcategory',
          popover: {
            title: '定义附件分类下的附件类别，在多文件上传处显示为左侧附件类别树',
            position: 'right'
          }
        },
        {
          element: '#base_exporttemplate',
          popover: {
            title: '维护word导出模板，基于aspose，word合并域',
            position: 'right'
          }
        },
                  {
          element: '#base_dbdictionary',
          popover: {
            title: '查看数据库表结构及字段信息',
            position: 'right'
          }
        },
          {
          element: '#base_apidoc',
          popover: {
            title: '基于swagger2生成api文档',
            position: 'right'
          }
        },
        {
          element: '#base_schedulejob',
          popover: {
            title: 'java Quartz 任务调度，已实现Mysql数据库定时备份功能',
            position: 'right'
          }
        },  {
          element: '#base_setting',
          popover: {
            title: '维护系统配置项',
            position: 'right'
          }
        },
        {
          element: '#wf_workflowdef',
          popover: {
            title: 'activiti自定义流程设计',
            position: 'right'
          }
        },
          {
          element: '#wf_processinstlist',
          popover: {
            title: '流程实例管理、提供跟踪、办理、转办、跳转、挂起、删除功能',
            position: 'right'
          }
        },
          {
          element: '#base_druidmonitor',
          popover: {
            title: 'druid monitor sql 监控功能',
            position: 'right'
          }
        },
        {
          element: '#base_informationlist',
          popover: {
            title: '基于UEditor的通告及文件下载',
            position: 'right'
          }
        },
          {
          element: '#base_notification',
          popover: {
            title: 'websocket 即时通讯技术，消息通知',
            position: 'right'
          }
        },
      ]);
      driver.start();
    },
    ...mapMutations(['ADD_CONTENT_TAB', 'UPDATE_CONTENT_TABS_ACTIVE_NAME'])
  }
}
</script>
<style>
#driver-highlighted-element-stage {
  opacity: 0.1;
}
</style>
