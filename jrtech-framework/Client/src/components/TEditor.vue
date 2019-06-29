//https://blog.csdn.net/haochuan9421/article/details/81975966
<template>
<vue-ueditor-wrap ref="ueditor" v-model="currentValue" :destroy="false" :config="config" :readonly="disabled"></vue-ueditor-wrap>
</template>
<script>
// 1、引入VueUeditorWrap组件
import VueUeditorWrap from 'vue-ueditor-wrap'

export default {
  name: 'TEditor',
  // 2、注册组件
  components: {
    VueUeditorWrap
  },
  props: {
    placeholder: '请选择',
    value: null,
    disabled: {
      type: Boolean,
      default: false,
      required: false
    },
  },
  data() {
    return {
      // 3、v-model绑定数据
      selectValue: null,
      // 4、根据项目需求自行配置,具体配置参见ueditor.config.js源码或 http://fex.baidu.com/ueditor/#start-start
      config: {
        maximumWords:4000,//允许的最大字符数
        // 编辑器不自动被内容撑高
        autoHeightEnabled: true,
        // 初始容器高度
        initialFrameHeight: 600,
        // 初始容器宽度
        initialFrameWidth: '100%',
        zIndex: 3000,
        toolbars: [
          ['fullscreen', 'source', '|', 'undo', 'redo', '|',
            'bold', 'italic', 'underline', 'fontborder', 'strikethrough', 'superscript', 'subscript', 'removeformat', 'formatmatch', 'autotypeset', 'blockquote', 'pasteplain', '|', 'forecolor', 'backcolor', 'insertorderedlist',
            'insertunorderedlist', 'selectall', 'cleardoc', '|',
            'rowspacingtop', 'rowspacingbottom', 'lineheight', '|',
            'customstyle', 'paragraph', 'fontfamily', 'fontsize', '|',
            'directionalityltr', 'directionalityrtl', 'indent', '|',
            'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify', '|', 'touppercase', 'tolowercase', '|',
            'link', 'unlink', 'anchor', '|', 'imagenone', 'imageleft', 'imageright', 'imagecenter', '|',
            'insertimage', 'emotion', 'scrawl', 'insertvideo', 'attachment', 'map', 'pagebreak', 'template', 'background', '|',
            'horizontal', 'date', 'time', 'spechars', 'wordimage', '|',
            'inserttable', 'deletetable', 'insertparagraphbeforetable', 'insertrow', 'deleterow', 'insertcol', 'deletecol', 'mergecells', 'mergeright', 'mergedown', 'splittocells', 'splittorows', 'splittocols', '|',
            'print', 'preview', 'searchreplace', 'drafts'
          ]
        ], //'simpleupload',单个图片上传，不显示
        // 上传文件接口（这个地址是我为了方便各位体验文件上传功能搭建的临时接口，请勿在生产环境使用！！！）
        serverUrl: window.SITE_CONFIG['ueditorServerUrl'],
        // UEditor 资源文件的存放路径，如果你使用的是 vue-cli 生成的项目，通常不需要设置该选项，vue-ueditor-wrap 会自动处理常见的情况，如果需要特殊配置，参考下方的常见问题2
        UEDITOR_HOME_URL: window.SITE_CONFIG['ueditorHomeUrl'],
        // 配合最新编译的资源文件，你可以实现添加自定义Request Headers,详情https://github.com/HaoChuan9421/ueditor/commits/dev-1.4.3.3
        headers: {
          access_token: this.$store.state.app.user.loginToken
        }
      }
    }
  },
  watch: {
    value(value) {
      this.currentValue = value;
    }
  },
  created() {
    this.currentValue = this.value;
  },
  computed: {
    currentValue: {
      // 动态计算currentValue的值
      get: function() {
        return this.selectValue;
      },
      set: function(val) {
        this.selectValue = val;
        this.$emit('ElFormItem', 'el.form.change', val);
        this.$emit('input', val);
      }
    }
  },
  mounted() {},
  methods: {
    isEmpty() {
      return this.currentValue == null || this.currentValue.length == 0;
    },
  }
}
</script>

<style>
.edui-default{
  line-height: 20px !important
}
</style>
