
<template>
<el-dialog ref='dailog' title="流程代码" :close-on-click-modal="false" :fullscreen="true" top="0vh" :visible.sync="visible" width="80%" height="80%">
  <editor ref="editor" v-model="code" @init="editorInit" lang="xml" theme="chrome" width="100%" :height="height"></editor>
</el-dialog>
</template>

<script>
export default {
  data() {
    return {
      visible: false,
      code: '',
      height: '800px',
    };
  },
  components: {
    editor: require('vue2-ace-editor'),
  },
  created() {

  },
  activated() {},
  mounted() {
    this.$nextTick(() => {
      const deviceHeight = document.documentElement.clientHeight;
      this.height = (deviceHeight - 125) + 'px';
    })
  },
  methods: {
    editorInit: function() {
      require('brace/ext/language_tools') //language extension prerequsite...
      require('brace/mode/xml')
      require('brace/theme/chrome');
    },
    init(businessKey) {
      this.visible = true;
      let self = this;
      tapp.services.wf_Model.codeRead(businessKey).then(function(result) {
        self.code = result;
      });
    },
  }
}
</script>
