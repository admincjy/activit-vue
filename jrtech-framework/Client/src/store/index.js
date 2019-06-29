import Vue from 'vue'
import Vuex from 'vuex'
import appModule from './app'
import uiModule from './ui'

Vue.use(Vuex)

const store = new Vuex.Store({
    modules:{
        app:appModule,
        ui:uiModule
    }
})
export default store
