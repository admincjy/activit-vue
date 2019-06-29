import Vue from 'vue'
import Router from 'vue-router'
import lazyLoading from './lazyLoading'
Vue.use(Router)

const appRouters = [{
  path: '/',
    name: 'home',
    title: '首页',
    component: () =>
      import ('@/views/Home'),
    meta: {
      name: 'home',
      title: '首页',
      show: 1,
      requiresAuth: true,
      isTab: true,
    }
  },
  {
  path: '/firstLoginChangePassword',
    name: 'firstLoginChangePassword',
    title: '首次登陆，请修改密码',
    component: () =>
      import ('@/views/publicsubsystem/user/firstLoginChangePassword'),
    meta: {
      name: 'firstLoginChangePassword',
      title: '首次登陆，请修改密码',
      show: 1,
      requiresAuth: true,
      isTab: false,
    }
  },
  {
    path: '/login',
    name: 'login',
    title: '登录',
    component: () =>
      import ('@/views/Login'),
    meta: {
      name: 'login',
      title: '登录',
      show: 0,
      requiresAuth: false,
    }
  },
  {
    path: '/404',
    name: '404',
    title: '404未找到',
    component: () =>
      import ('@/views/error/404'),
    meta: {
      name: '404',
      title: '404未找到',
      show: 0,
      requiresAuth: false,
      isTab: false,
    }
  },
  {
    path: '*',
    redirect: {
      name: '404'
    }
  }
]

let menus = tapp.data.base_navigation;
let sRouters =  menus.map((node) => {
  node.menupath = node.path;
  node.path = node.menupath == null || node.menupath.length == 0 ? null : node.menupath.split('?')[0];
  node.meta = {
    name: node.name,
    title: node.title,
    requiresAuth: true,
    keepAlive: node.keepAlive,
    show: node.show,
    isTab: node.layoutType == 0,
  };

  if (node.component) {
    node.componentName = node.component;
    node.component = lazyLoading(node.componentName);
  }
  return node;
});

appRouters.push(...sRouters);

export default new Router({
  //base: '/',
  base: window.SITE_CONFIG.domain,
  mode: 'history',
  linkActiveClass: 'is-active',
  routes: appRouters, //默认只有登陆
})
