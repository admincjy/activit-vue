
<template>
<div id='main'>
  <t-html-panel :url.asyc="dbmonitorUrl"></t-html-panel>
</div>
</template>

<script>
export default {
  data() {
    return {
      dbmonitorUrl: window.SITE_CONFIG.dbmonitorUrl,
    };
  },
  created() {

  },
  activated() {},
  mounted(){
    this.$nextTick(() => {
      //const deviceWidth = document.documentElement.clientWidth;
      const deviceHeight = document.documentElement.clientHeight;
      this.iframeHeight = (deviceHeight-125) + 'px';
    })
  },
  methods: {

  }
  }
  </script>
