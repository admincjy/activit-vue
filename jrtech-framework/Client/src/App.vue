<template>
<div>
  <div v-if="!showNav">
    <router-view v-if="!showNav"></router-view>
  </div>
  <div v-else :class="siteWarpperClasses" class="site-wrapper">
    <template>
      <navbar></navbar>
      <sidebar></sidebar>
      <div class="site-content__wrapper" :style="siteContentWarpperStyles">
        <main class="site-content" :class="{ 'site-content--tabs': routeIsTab }">
            <content-card v-if="showNav&&!routeIsTab"></content-card>
            <!-- tab标签页, 内容展示方式 -->
            <content-tabs v-else-if="showNav"></content-tabs>
        </main>
    </div>
    </template>
  </div>
</div>
</template>

<script>
import Navbar from './views/layout/navbar'
import Sidebar from './views/layout/sidebar'
import ContentTabs from './views/layout/content-tabs'
import ContentCard from './views/layout/content-card'


import {
  mapMutations,
  mapState,
} from 'vuex'
export default {
  data() {
    return {}
  },
  components: {
    Navbar,
    Sidebar,
    ContentTabs,
    ContentCard
  },
  computed: {
    ...mapState({
      path: state => state.route.path,
    }),
    siteWarpperClasses() {
      return [{
        'site-sidebar--collapse': this.$store.state.ui.sidebarCollapse
      }]
    },
    siteContentWarpperStyles() {
      return [{
        'minHeight': this.$store.state.ui.documentClientHeight + 'px'
      }]
    },
    routeIsTab() {
      let a = this.$route.meta && this.$route.meta.isTab;
      return a;
    },
    showNav() {
      let innerStore = this.$store.state;
      if (!innerStore.app.user || !innerStore.app.user.loginToken || /login/.test(this.path)) {
        return false;
      }
      if(/firstLoginChangePassword/.test(this.path)) {
        return false;
      }
      return true;
    },
    contentViewStyles() {
      var height = this.$store.state.ui.documentClientHeight
      height -= 50 // site-topbar
      height -= 15 // site-content padding-top
      height -= 15 // site-content padding-bottom
      height -= 2 // el-card border-top border-bottom
      height += 'px'
      return [{
        minHeight: height
      }]
    }
  },
  created() {},
  mounted() {
    this.resetDocumentClientHeight()
    window.onresize = () => {
      this.resetDocumentClientHeight()
    }
  },
  updated() {

  },
  methods: {
    // 重置窗口可视高度
    resetDocumentClientHeight() {
      this.UPDATE_DOCUMENT_CLIENT_HEIGHT({
        height: document.documentElement['clientHeight']
      })
    },
    ...mapMutations(['UPDATE_DOCUMENT_CLIENT_HEIGHT', 'UPDATE_USER_ID', 'UPDATE_USER_NAME', 'DELETE_CONTENT_TABS'])
  }
}
</script>
<!-- Add "scoped" attribute to limit CSS to this component only -->
