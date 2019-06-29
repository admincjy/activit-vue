
<template>
<el-dialog ref='dailog' title="流程设计" :close-on-click-modal="false" :fullscreen="true" top="0vh" :visible.sync="visible" width="80%" height="80%">
  <div>
    <iframe :src="designSrc" id="show-iframe" frameborder=0 name="showHere" scrolling=auto :height="iframeHeight" width="100%" />
  </div>
</el-dialog>
</template>

<script>
export default {
  data() {
    return {
      designSrc: null,
      visible: false,
      iframeHeight:'800px', 
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
    init(id) {
      this.visible = true;
      this.designSrc = window.SITE_CONFIG.wfDesingUrl + id;
    },
  }
  }
  </script>
