<template>
<el-dialog :close-on-click-modal="false" :visible.sync="visible" :fullscreen="true">
  <div slot="title">
    <el-row>
      <el-col :span="12">
        <span class="el-dialog__title">{{title}}
            </span>
      </el-col>
      <el-col :span="12">
        <div class="div-loading" v-show="isLoading">
          <div class="div-loading-inner"><i class="el-icon-loading"></i><span class="icon-name">系统加载中...</span>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
  <div id="preview" class="preview" :style="{ 'height': bodyHeight + 'px','overflow':'auto','text-align':'center','vertical-align':'middle' }">
    <viewer v-if="type == 'image'" :options="imageViewerOptions" :images="[url]">
      <img v-lazy="url" :key="url" :alt="name">
    </viewer>
    <audio v-else-if="type == 'audio'" v-lazy="url" autoplay controls style="width:100%;height:100%"></audio>
    <video v-else-if="type == 'video'" v-lazy="url" autoplay controls style="width:100%;height:100%"></video>
    <object v-else-if="type == 'pdf'" v-lazy  :data="url" style="width:100%;height:100%"></object>
    <a v-else :href="download">
      <h2 class="message">无法预览，点击下载</h2>
    </a>
  </div>
  <div slot="footer" class="dialog-footer">
    <el-row>
      <el-col :span="12">
        <div style="text-align: left; margin-left:20px;">
          <span>文件名称：{{name}}</span>
          <div class="bottom clearfix" style="margin-top:5px;">
            <time class="time">上传时间：{{ uploadDate }}</time>
          </div>
        </div>
      </el-col>
      <el-col :span="12">
        <div style="text-align: right; margin-right:20px;">
          <el-button-group>
            <el-button type="primary" icon="el-icon-arrow-left" v-if="hasPreview()" @click="preview">上一个</el-button>
            <el-button type="primary" v-if="hasNext()" @click="next">下一个<i class="el-icon-arrow-right el-icon--right"></i></el-button>
          </el-button-group>
        </div>
      </el-col>
    </el-row>
  </div>
</el-dialog>
</template>
<script>
export default {
  name: 'preview',
  components: {},
  data: function() {
    return {
      visible: false,
      isLoading: false,
      url: null,
      viewerlist: [],
      viewer: null,
      signImages: [],
      bodyHeight: document.documentElement.clientHeight - 200,
      imageViewerOptions: {
        "inline": false,
        "button": true,
        "navbar": false,
        "title": false,
        "toolbar": {
          zoomIn: true,
          zoomOut: true,
          oneToOne: true,
          reset: true,
          prev: false,
          play: false,
          next: false,
          rotateLeft: true,
          rotateRight: true,
          flipHorizontal: true,
          flipVertical: true,
        },
        "tooltip": true,
        "movable": true,
        "zoomable": true,
        "rotatable": true,
        "scalable": true,
        "transition": true,
        "fullscreen": false,
        "keyboard": true,
        "url": "data-source",
        "container": "#preview"
      }
    }
  },
  computed: {
    title() {
      if (this.viewer == null ||
        this.viewer.name == null) {
        return '预览';
      }
      return this.viewer.name + ' 预览';
    },
    name() {
      if (this.viewer == null ||
        this.viewer.name == null) {
        return '';
      }
      return this.viewer.name;
    },
    uploadDate() {
      if (this.viewer == null ||
        this.viewer.gmtCreatedOn == null) {
        return '';
      }
      return this.$util.datetimeFormat(this.viewer.gmtCreatedOn);
    },
    type() {
      if (this.viewer == null ||
        this.viewer.name == null) {
        return 'none';
      }
      return this.$util.getFileType(this.viewer.name);
    },
    viewerIndex() {
      if (this.viewer == null ||
        this.viewer.id == null) {
        return -1;
      }
      let currIndex = this.viewerlist.findIndex((item, index, arr) => {
        return item.id === this.viewer.id;
      })
      return currIndex;
    },
    download() {
      if (this.viewer == null ||
        this.viewer.id == null) {
        return '';
      }
      return window.SITE_CONFIG['serverUrl'] + "/authapi/base_AssetManagement/download?id=" + this.viewer.id;
    },

  },
  created() {

  },
  mounted() {
    const that = this
    window.onresize = () => {
      return (() => {
        that.bodyHeight = document.documentElement.clientHeight - 200
      })()
    }
    document.onkeydown = (e) => {
      return (() => {
        if (e.keyCode === 37) {
          this.preview();
          // 左
        } else if (e.keyCode === 39) {
          // 右
          this.next();
        }
      })()
    }
  },
  beforeDestroy() {},
  methods: {
    show(filelist, file) {
      this.visible = true;
      this.viewerlist = filelist;
      this.viewer = file;
      this.loadFile();
    },
    hasNext() {
      return this.viewerIndex < this.viewerlist.length - 1 && this.viewerIndex >= 0;
    },
    hasPreview() {
      return this.viewerIndex > 0;
    },
    next() {
      if (!this.hasNext()) {
        return;
      }
      if (this.viewerIndex < this.viewerlist.length) {
        this.viewer = this.viewerlist[this.viewerIndex + 1];
        this.loadFile();
      }
    },
    preview() {
      if (!this.hasPreview()) {
        return;
      }
      if (this.viewerIndex > 0) {
        this.viewer = this.viewerlist[this.viewerIndex - 1];
        this.loadFile();
      }
    },
    loadFile() {
      if (this.viewer == null ||
        this.viewer.id == null) {
        return '';
      }
      this.url = window.SITE_CONFIG['serverUrl'] + "/authapi/base_AssetManagement/view?id=" + this.viewer.id;
    },

  }
}
</script>
<style scope>
.time {
  font-size: 13px;
  color: #999;
}


.div-loading {
  width: 200px;
  height: 50px;
  left: 50%;
  top: 50%;
  margin-left: -100px;
  margin-top: -25px;
}

.div-loading-inner {
  margin-left: 50px;
  margin-top: 18px;
}
</style>
