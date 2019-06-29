<template>
<el-tabs v-model="tabActiveName" :closable="true" @tab-click="selectedTabHandle" @tab-remove="removeTabHandle">
  <el-tab-pane v-for="item in contentTabs" :key="item.name" :label="item.title" :name="item.name" >
  </el-tab-pane>
  <el-card :body-style="contentViewStyles()">
    <div>
      <router-view v-if="!$route.meta.keepAlive"></router-view>
      <keep-alive>
        <router-view v-if="$route.meta.keepAlive"></router-view>
      </keep-alive>
    </div>
  </el-card>
  <!-- tabs tools -->
  <el-dropdown class="site-tabs__tools" @command="toolsCommandHandle" :show-timeout="0">
    <i class="el-icon-arrow-down el-icon--right"></i>
    <el-dropdown-menu slot="dropdown">
      <el-dropdown-item command="closeCurrent">关闭当前标签页</el-dropdown-item>
      <el-dropdown-item command="closeOther">关闭其它标签页</el-dropdown-item>
      <el-dropdown-item command="closeAll">关闭全部标签页</el-dropdown-item>
      <el-dropdown-item command="refreshCurrent">刷新当前标签页</el-dropdown-item>
    </el-dropdown-menu>
  </el-dropdown>
</el-tabs>
</template>

<script>
import isEmpty from 'lodash/isEmpty'
import {
  mapMutations,
  mapState,
} from 'vuex'
export default {
  data() {
    return {}
  },
  mounted() {},
  computed: {
    ...mapState({
      path: state => state.route.path,
      contentTabs: state => state.ui.contentTabs,
    }),
    tabActiveName: {
      get() {
        return this.$store.state.ui.contentTabsActiveName
      },
      set(name) {
        this.UPDATE_CONTENT_TABS_ACTIVE_NAME({
          name
        })
      }
    }
  },
  watch: {
    '$store.state.ui.contentTabs' (tabs) {
      if (tabs.length <= 0) {
        this.UPDATE_MENU_NAV_ACTIVE_NAME({
          name: ''
        })
        this.$router.push({
          name: 'home'
        })
      }
    }
  },
  methods: {
    // tab内容容器显示高度
    contentViewStyles() {
      var height = this.$store.state.ui.documentClientHeight
      height -= 50 // site-topbar
      height -= 40 // el-tabs__header
      height -= 15 // el-tabs__header margin-bottom
      height -= 15 // el-tabs__content padding-bottom
      height -= 2 // el-card border-top border-bottom
      height += 'px'
      return [
        {
           minHeight: height
         }
      ]
    },
    // 选中tab
    selectedTabHandle(tab) {
      tab = this.$store.state.ui.contentTabs.filter(item => item.name === tab.name) 
      if (!isEmpty(tab)) {
        this.$router.push({
          path: tab[0].url
        })
      }
    },
    // 删除tab
    removeTabHandle(tabName) {
      var newTabs = this.$store.state.ui.contentTabs.filter(item => item.name !== tabName)
      // 当前选中tab被删除
      if (newTabs.length >= 1 && tabName === this.tabActiveName) {
        this.$router.push({
          path: newTabs[newTabs.length - 1].url
        }, () => {
          this.tabActiveName = this.$route.fullPath
        })
      }
      this.UPDATE_CONTENT_TABS(newTabs)
    },
    // 工具操作
    toolsCommandHandle(command) {
      if (command === 'closeCurrent') {
        this.removeTabHandle(this.tabActiveName)
      } else if (command === 'closeOther') {
        this.UPDATE_CONTENT_TABS(this.$store.state.ui.contentTabs.filter(item => item.name === this.tabActiveName))
      } else if (command === 'closeAll') {
        this.DELETE_CONTENT_TABS()
      } else if (command === 'refreshCurrent') {
        var tempTabName = this.tabActiveName
        this.removeTabHandle(tempTabName)
        let tab = this.$store.state.ui.contentTabs.filter(item => item.name === tempTabName)
        if (!isEmpty(tab)) {
          this.$nextTick(() => {
            this.$router.push({
              path: tab[0].url
            })
          })
        }


      }
    },
    ...mapMutations(['UPDATE_MENU_NAV_ACTIVE_NAME', 'UPDATE_CONTENT_TABS', 'UPDATE_CONTENT_TABS_ACTIVE_NAME', 'DELETE_CONTENT_TABS'])
  }
}
</script>
