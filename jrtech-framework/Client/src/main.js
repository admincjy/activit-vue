// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from '@/App'
import router from '@/router' // api: https://github.com/vuejs/vue-router
import store from '@/store' // api: https://github.com/vuejs/vuex
import VueCookie from 'vue-cookie' // api: https://github.com/alfhen/vue-cookie
import ElementUI from 'element-ui'
import ElementUIVerify from 'element-ui-verify'
import '@/icons' // api: http://www.iconfont.cn/
import '@/assets/scss/index.scss'

import '@/assets/global.scss'

import Vuex from 'vuex';

import Viewer from 'v-viewer'
import 'viewerjs/dist/viewer.css'
import VueLazyload from 'vue-lazyload'
import VCharts from 'v-charts'

Vue.use(ElementUI)

Vue.use(VueLazyload, {

　　// 设置默认显示的图片
　　loading: require('./assets/img/loading.gif'),
    error:require('./assets/img/error.jpg'),
})

Vue.use(VCharts)

//https://github.com/aweiu/element-ui-verify
/*
length: 校验文本长度
minLength: 校验文本最短长度
gt: 校验数字要大于某数
gte: 校验数字要大于等于某数
lt: 校验数字要小于某数
lte: 校验数字要小于等于某数
maxDecimalLength: 校验数字最大小数位
number: 校验是否为数字
int: 校验是否为整数
phone: 校验是否为手机号（随着号段的增加，未来可能需要更新）
email: 校验是否为电子邮件地址
verifyCode: 校验是否为6位数字验证码

*/
Vue.use(ElementUIVerify)

Vue.use(VueCookie)

Vue.use(Viewer);
Viewer.setDefaults({
  Options: { "inline": true, "button": true, "navbar": false, "title": true, "toolbar": true, "tooltip": true, "movable": true, "zoomable": true, "rotatable": true, "scalable": true, "transition": true, "fullscreen": true, "keyboard": true, "url": "data-source" }
});

Vue.config.productionTip = false

import VueRouter from 'vue-router'
import {
  sync
} from 'vuex-router-sync'

import http from './http'
import util from './util'


Vue.prototype.$http = http
Vue.prototype.$util = util

import TGrid from './components/TGrid.vue'
import TEditGrid from './components/TEditGrid.vue'
import TDicDropdownSelect from './components/TDicDropdownSelect.vue'
import TDicRadioSelect from './components/TDicRadioSelect.vue'
import TDicCheckBoxList from './components/TDicCheckBoxList.vue'
import TCascader from './components/TCascader.vue'
import TExcelImport from './components/TExcelImport.vue'
import TTree from './components/TTree.vue'
import TNumberInput from './components/TNumberInput.vue'
import TNumberRangeInput from './components/TNumberRangeInput.vue'
import TCurrencyInput from './components/TCurrencyInput.vue'
import TCurrencyRangeInput from './components/TCurrencyRangeInput.vue'
import TPercentInput from './components/TPercentInput.vue'
import TPercentRangeInput from './components/TPercentRangeInput.vue'
import TIntInput from './components/TIntInput.vue'
import TIntRangeInput from './components/TIntRangeInput.vue'
import TDicAutoComplete from './components/TDicAutoComplete.vue'
import TDataTimePicker from './components/TDataTimePicker.vue'
import TDataTimeRangePicker from './components/TDataTimeRangePicker.vue'
import THtmlPanel from './components/THtmlPanel.vue'
import TPopoverHyperlink from './components/TPopoverHyperlink.vue'
import TEditor from './components/TEditor.vue'

import userSelect from './views/publicsubsystem/components/userSelect.vue'
import organizationSelect from './views/publicsubsystem/components/organizationSelect.vue'
import wfProcessTrack from './views/publicsubsystem/components/wfProcessTrack.vue'
import assetMultiManagement from './views/publicsubsystem/components/assetMultiManagement.vue'
import assetSingleManagement from './views/publicsubsystem/components/assetSingleManagement.vue'
import exportTemplate from './views/publicsubsystem/components/exportTemplate.vue'
import loanProductTypeSelect from './views/loansubsystem/components/loanProductTypeSelect.vue'

Vue.component('t-grid', TGrid)
Vue.component('t-edit-grid', TEditGrid)
Vue.component('t-excel-import', TExcelImport)
Vue.component('t-dic-dropdown-select', TDicDropdownSelect)
Vue.component('t-dic-radio-select', TDicRadioSelect)
Vue.component('t-dic-checkbox-list', TDicCheckBoxList)
Vue.component('t-cascader', TCascader)
Vue.component('t-tree', TTree)
Vue.component('t-number-input', TNumberInput)
Vue.component('t-number-range-input', TNumberRangeInput)
Vue.component('t-currentcy-input', TCurrencyInput)
Vue.component('t-currentcy-range-input', TCurrencyRangeInput)
Vue.component('t-percent-input', TPercentInput)
Vue.component('t-percent-range-input', TPercentRangeInput)
Vue.component('t-int-input', TIntInput)
Vue.component('t-int-range-input', TIntRangeInput)
Vue.component('t-datetime-picker', TDataTimePicker)
Vue.component('t-datetime-range-picker', TDataTimeRangePicker)
Vue.component('t-html-panel', THtmlPanel)
Vue.component('t-popover-hyperlink', TPopoverHyperlink)
Vue.component('t-editor', TEditor)
Vue.component('pl-loanProducttype-select', loanProductTypeSelect)
Vue.component('base-user-select', userSelect)
Vue.component('base-organization-select', organizationSelect)
Vue.component('base-asset-multi-management', assetMultiManagement)
Vue.component('base-asset-single-management', assetSingleManagement)
Vue.component('base-export-template', exportTemplate)
Vue.component('wf-processtrack', wfProcessTrack)
sync(store, router)

import  verifyrules from '@/verifyRule'

verifyrules.forEach(item => {
  ElementUIVerify.addRule(item.rule, item.ruleMethod);
})
import * as filters from './filters/index.js'

Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})

import appRouterHelper from "@/router/appRouterHelper"

router.beforeEach(function(to, from, next) {
  store.commit('startLoading')

  util.ui.title(to.meta && to.meta.title ? to.meta.title : '')

  if (to.name == 'login') {
    next();
    return;
  }
  if (to.matched.some(record => !record.meta || !record.meta.requiresAuth)) {
    next()
    return;
  }
  let innerStore = store.state
  if (!innerStore.app.user || !innerStore.app.user.loginToken) {
    store.commit('endLoading');
    next({
      path: '/login?to=' + to.fullPath
    });
    return;
  }
  if (!to.path || !to.name && !to.name == '/') {
    next({
      path: '/'
    });
  } else {
    next()
  }

})

router.afterEach(function(to, from) {

  store.commit('endLoading');
  window.scrollTo(0, 0);
})

function initVue() {
  return new Vue({
    store,
    router,
    render: h => h(App)
  }).$mount('#app')
}

let vueInst = initVue();

if (window && !window.vue) {
  window.vue = vueInst;
}
export default vue;
