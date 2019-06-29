<template>
<el-submenu v-if="subMenus && subMenus.length >= 1" :data-idx="menuNav.self.id + ''" :index="menuNav.self.id + ''" :id="menuNav.self.id + ''">
  <template slot="title">
      <icon-svg :name="menuNav.self.icon" class="site-sidebar__menu-icon" ></icon-svg>
      <span>{{ menuNav.self.title }}</span>
    </template>
  <sub-menu-nav v-for="item in subMenus" :key="item.self.id" :menu-nav="item">
  </sub-menu-nav>
</el-submenu>
<el-menu-item v-else :index="menuNav.self.id + ''" :data-idx="menuNav.self.id + ''" :id="menuNav.self.id + ''" @click="gotoRouteHandle(menuNav.self.path,menuNav.self.name)">
  <icon-svg :name="menuNav.self.icon" class="site-sidebar__menu-icon" ></icon-svg>
  <span>{{ menuNav.self.title }}</span>
</el-menu-item>
</template>

<script>
import appRouterHelper from "@/router/appRouterHelper"

import SubMenuNav from '../sub-menu-nav'

export default {
  name: 'sub-menu-nav',
  props: {
    menuNav: Object,
    required: true
  },
  components: {
    SubMenuNav
  },
  computed: {
    subMenus() {
      let subMenus = this.menuNav.items;
      return subMenus;
    },
  },
  methods: {
    // 跳转到菜单导航对应路由
    gotoRouteHandle(url, routeName) {
      this.$router.push({
        path: url
      })
    }
  }
}
</script>
