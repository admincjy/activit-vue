/*
html显示组件 控件
*/
<template>
<div>
  <div v-html="html"></div>
</div>
</template>
<style>

</style>
<script>
export default {

/**
 要显示的网站地址
 使用时请使用 :url.sync=""传值
 */
  props: {
    url: {
      required: true
    }
  },
  data() {
    return {
      html: ''
    }
  },
  watch: {
    url(value) {
      this.load(value)
    }
  },
  mounted() {
    this.load(this.url)
  },
  methods: {
    load(url) {
      if (url && url.length > 0) {

        let param = {
          accept: 'text/html, text/plain'
        }
        this.$http.get(url, param).then((response) => {
          // 处理HTML显示
          this.html = response;
        }).catch((error) => {
          this.$message.error(error);
        })
      }
    }
  }
}
</script>
