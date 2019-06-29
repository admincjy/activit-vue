<template>
<el-card  :body-style="contentViewStyles">
  <keep-alive>
    <router-view v-if="$route.meta.keepAlive"></router-view>
  </keep-alive>
  <router-view v-if="!$route.meta.keepAlive"></router-view>
</el-card>
</template>

<script>

import {
  mapMutations,
  mapState,
} from 'vuex'
export default {
  data() {
    return {}
  },
  components: {

  },
  computed: { 
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
