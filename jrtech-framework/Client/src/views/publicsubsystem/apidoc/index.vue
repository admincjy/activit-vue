
<template>
<div id='main'>
  <iframe :src="url" id="show-iframe" frameborder=0 name="showHere" scrolling=auto :height="iframeHeight" width="100%" />

</div>
</template>

<script>
export default {
  data() {
    return {
      iframeHeight:'800px',
      url: window.SITE_CONFIG.apiDoc,
    };
  },
  created() {

  },
  activated() {},
  mounted(){
  this.$nextTick(() => {
      //const deviceWidth = document.documentElement.clientWidth;
      const deviceHeight = document.documentElement.clientHeight;
      this.iframeHeight = (deviceHeight-10) + 'px';
    })
},
  methods: {

  }
  }
  </script>
