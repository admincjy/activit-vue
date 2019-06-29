
<template>
<div>
  <iframe :src="src" id="show-iframe" frameborder=0 name="showHere" scrolling=auto :height="iframeHeight" width="100%" />
</div>
</template>
<script>
export default {
  data() {
    return {
      src: null,
      iframeHeight:'800px',
    };
  },
  created() {
    this.src = window.SITE_CONFIG['druidMonitorUrl'];

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
