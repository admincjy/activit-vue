import appRouterHelper from "@/router/appRouterHelper"

import * as types from './ui-mutation-types'

export default {
  state: {
    isLoading: false,
    // 导航条, 布局风格, defalut(默认) / inverse(反向)
    navbarLayoutType: 'default',
    // 侧边栏, 布局皮肤, light(浅色) / dark(黑色)
    sidebarLayoutSkin: 'dark',
    // 侧边栏, 水平折叠收起状态
    sidebarCollapse: false,
    // 页面文档可视高度(随窗口改变大小)
    documentClientHeight: 0,
    // 菜单导航
    menuNavList: [],
    menuNavActiveName: '1-1',
    // 内容区域tabs标签页
    contentTabs: [],
    contentTabsActiveName: '',
    notificationNum: null,
  },
  getters: {
    isLoading: state => state.isLoading,
  },
  mutations: {
    startLoading(state) {
      state.isLoading = true
    },
    endLoading(state) {
      state.isLoading = false
    },
    setNotificationNum(state,value) {
      state.notificationNum = value
    },
    [types.SET_NAVBAR_LAYOUT_TYPE](state, {
      type
    }) {
      state.navbarLayoutType = type
    },
    [types.SET_SIDEBAR_LAYOUT_SKIN](state, {
      skin
    }) {
      state.sidebarLayoutSkin = skin
    },
    [types.SWITCH_SIDEBAR_COLLAPSE](state, {
      collapse
    }) {
      state.sidebarCollapse = collapse
    },

    [types.UPDATE_DOCUMENT_CLIENT_HEIGHT](state, {
      height
    }) {
      state.documentClientHeight = height
    },

    [types.UPDATE_MENU_NAV_LIST](state, list) {
      state.menuNavList = list
    },

    [types.UPDATE_MENU_NAV_ACTIVE_NAME](state, {
      name
    }) {
      state.menuNavActiveName = name
    },

    [types.ADD_CONTENT_TAB](state, tab) {
      if (tab.show === 1) {
        let foundTabNavIndex = state.contentTabs.findIndex(p => {
          return p.url == tab.url;
        })
        if (foundTabNavIndex < 0) {
          state.contentTabs.push(tab)
        } else {
          // do nothing;
        }
      } else {
        let foundTabNavIndex = state.contentTabs.findIndex(p => {
          return p.id == tab.id;
        })
        if (foundTabNavIndex >= 0) {
          state.contentTabs.splice(foundTabNavIndex, 1, tab);
        } else {
          state.contentTabs.push(tab)
        }
      }
    },

    [types.UPDATE_CONTENT_TABS](state, tabs) {
      state.contentTabs = tabs
    },

    [types.DELETE_CONTENT_TABS](state) {
      state.contentTabs = []
    },

    [types.UPDATE_CONTENT_TABS_ACTIVE_NAME](state, {
      name
    }) {
      state.contentTabsActiveName = name
    }
  }
}
