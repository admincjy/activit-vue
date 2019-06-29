<template>
<div>
  <el-alert title="系统对'登陆账号'，'岗位权限'，'配置项'，'菜单导航'，'数据字典'做了数据缓存，如果在数据库直接修改数据，在点击'重新加载缓存'按钮,并重新登陆后修改生效" type="info" show-icon center>
  </el-alert>
  <div style="margin-top:20px">
  <el-button type="primary" @click="doReladCache()">重新加载缓存</el-button>
  </div>
</div>
</template>
<script>
export default {
  data() {
    return {

    }
  },
  methods: {
    doReladCache(tab, event) {
      let self = this;
      tapp.services.appCache.reloadCache().then(function(result) {
        self.$notify.success({
          title: '操作成功！',
          message: '缓存加载成功，注销后重新登陆系统后配置生效！',
        });
      });
    },
  }
}
</script>
