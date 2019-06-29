
import axios from 'axios'

import vue from "../main.js";

axios.defaults.baseURL = window.SITE_CONFIG.serverUrl
axios.defaults.withCredentials = true
// http request 拦截器
axios.interceptors.request.use(
  config => {
    if (vue.$store.state.app.user.loginToken) {
      config.headers.Authorization = `${vue.$store.state.app.user.loginToken}`;
    }
    vue.$store.commit('startLoading');
    return config;
  },
  err => {
    vue.$notify.error({
      title: '错误',
      message: '系统出现错误，请重试----！'
    });

    return Promise.reject(err);
  });

// http response 拦截器
axios.interceptors.response.use(
  response => {
    vue.$store.commit('endLoading');
    return response.data;
  },
  error => {
    vue.$store.commit('endLoading');
    if (error.response) {
      switch (error.response.status) {
        case 401:
          // 401 清除token信息并跳转到登录页面
          vue.$store.commit('loginOut');
          vue.$router.replace({
            path: 'login',
            query: {
              redirect: vue.$router.currentRoute.fullPath
            }
          });
          return;
        case 8888: //UserFriendlyException
          vue.$message.error(error.response.data.message);
          return Promise.reject(error.response.data)
        default:
          let msg = "系统出现错误，请重试";
          vue.$message.error(msg);
          return Promise.reject(msg)
      }
    }
  });

export default axios;
