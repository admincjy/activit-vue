<template>
<div class="tips-import">
  <el-dialog title="模型文件上传" :visible.sync="visible" width="800px">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <div style="float: left; padding: 3px 0">
          <div class="btns " style="float:left; width:88px;height:56px;">
            <div id="picker">选择文件</div>
          </div>
        </div>
      </div>
      <div class="text item">
        <div class="file-list">
          <ul class="file-item" :class="`file-${file.id}`" v-if="file!=null">
            <li class="file-type" :icon="fileCategory(file.ext)"></li>
            <li class="file-name">{{file.name}}</li>
            <li class="file-size">{{fileSize(file.size)}}</li>
            <li class="file-status">上传中...</li>
            <li class="file-operate">
              <a title="开始" @click="resume(file)"><i class="iconfont icon-control-play"></i></a>
              <a title="暂停" @click="stop(file)"><i class="iconfont icon-video-pause"></i></a>
              <a title="移除" @click="remove(file)"><i class="iconfont icon-close-big"></i></a>
            </li>
            <li class="progress"></li>
          </ul>
        </div>
      </div>
      <uploader ref="uploader" :url="fileUploadUrl" uploadButton=".btns" :multiple='false' @fileChange="fileChange" @uploadStart="onUploadStart" @uploadBeforeSend="onUploadBeforeSend" @progress="onProgress" @success="onSuccess" @error="onError" @uploadError="onUploadError"></uploader>
    </el-card>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
    </span>
  </el-dialog>
</div>
</template>
<script>
import Uploader from '@/components/TUpload.vue'
import {
  mapMutations,
  mapState,
} from 'vuex'

//ES6引入
export default {
  data() {
    return {
      visible: false,
      id: null,
      fileUploadUrl: '',
      file: null,
    }
  },
  components: {
    Uploader,
  },
  created() {
    this.fileUploadUrl = window.SITE_CONFIG['serverUrl'] + '/authapi/base_AssetManagement/upload';
  },
  activated() {},
  computed: {
    uploader() {
      return this.$refs.uploader;
    },
    ...mapState({
      user: state => state.app.user,
    }),
  },
  methods: {
    init(id) {
      this.visible = true;
      this.id=id;
      this.fileUploadUrl = window.SITE_CONFIG['serverUrl'] + '/authapi/wf_Model/deployFromFile?id=' + id;
    },
    fileChange(file) {
      if (!file.size) return;
      file.fid = this.id;
      this.file = file;
    },
    onUploadBeforeSend(object, data, header) {
      data.fid = object.file.fid;
      if (this.user.loginToken) {
        header.Authorization = `${this.user.loginToken}`;
      }
    },
    onUploadStart(file) {},
    onProgress(file, percent) {
      $(`.file-${file.id} .progress`).css('width', percent * 100 + '%');
      $(`.file-${file.id} .file-status`).html((percent * 100).toFixed(2) + '%');
    },
    onSuccess(file, response) {
      //console.log('上传成功', response);
      this.file = null;

      this.visible = false;
    },
    onError(message) {
      this.$notify.error({
        title: '文件上传失败',
        message: message
      });
    },
    onUploadError(file, reason) {
      $(`.file-${file.id} .progress`).css('background', '#fab6b6');
      $(`.file-${file.id} .file-status`).text('上传出错');
    },
    resume(file) {
      this.uploader.upload(file);
    },
    stop(file) {
      this.uploader.stop(file);
    },
    remove(file) {
      // 取消并中断文件上传
      this.uploader.cancelFile(file);
      // 在队列中移除文件
      this.uploader.removeFile(file, true);
      // 在ui上移除
      this.file = null;
    },
    fileSize(size) {
      return WebUploader.Base.formatSize(size);
    },
    fileCategory(ext) {
      let type = '';
      const typeMap = {
        image: ['gif', 'jpg', 'jpeg', 'png', 'bmp', 'webp'],
        video: ['mp4', 'm3u8', 'rmvb', 'avi', 'swf', '3gp', 'mkv', 'flv'],
        text: ['doc', 'txt', 'docx', 'pages', 'epub', 'pdf', 'numbers', 'csv', 'xls', 'xlsx', 'keynote', 'ppt', 'pptx']
      };
      Object.keys(typeMap).forEach((_type) => {
        const extensions = typeMap[_type];
        if (extensions.indexOf(ext) > -1) {
          type = _type
        }
      });
      return type
    },
  }

}
</script>

<style lang="scss" scoped>
$h-row: 50px;

.file-list {
    position: relative;
    minheight: 30px;
    overflow-y: auto;
}
.file-item {
    position: relative;
    height: $h-row;
    line-height: $h-row;
    padding: 0 10px;
    border-bottom: 1px solid #ccc;

    z-index: 1;
    > li {
        display: inline-block;
    }
}
.file-type {
    width: 24px;
    height: 24px;
    vertical-align: -5px;
}
.file-name {
    width: 40%;
    margin-left: 10px;
}
.file-size {
    width: 20%;
}
.file-status {
    width: 20%;
}
.file-operate {
    width: 10%;
    > a {
        padding: 10px 5px;
        cursor: pointer;
        color: #666;
        &:hover {
            color: #ff4081;
        }
    }
}
.file-type[icon=text] {
    //background: url(../../assets/images/icon/text-icon.png);
}
.file-type[icon=video] {
    //background: url(../../assets/images/icon/video-icon.png);
}
.file-type[icon=image] {
    //background: url(../../assets/images/icon/image-icon.png);
}
.progress {
    position: absolute;
    top: 0;
    left: 0;
    height: $h-row - 1;
    width: 0;
    background: #67c23a;
    z-index: -1;
}
.time {
    font-size: 13px;
    color: #999;
}

.bottom {
    margin-top: 13px;
    line-height: 12px;
}

.button {
    padding: 0;
    float: right;
}

.image {
    width: 100%;
    display: block;
}
.clearfix:after,
.clearfix:before {
    display: table;
    content: "";
}
.clearfix:after {
    clear: both;
}
</style>
