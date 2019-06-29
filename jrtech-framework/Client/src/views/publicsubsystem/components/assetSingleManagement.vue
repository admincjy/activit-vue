<template>
<div>
  <el-row>
    <el-col :span="24">
      <el-card class="box-card">
        <div slot="header" class="clearfix" v-if="disabled==false">
          <div style="float: left; padding: 3px 0">
            <div class="btns " style="float:left; width:88px;height:36px;line-height:16px;">
              <div id="filePicker">选择文件</div>
            </div>
            <div style="float:left;width:88px;height:36px;line-height:16px;margin-left:15px" v-if="file !=null">
              <el-button type="primary" @click="handlePreview()" style="
                  padding: 10px;
                  border-radius:2px;
                  width:86px;
              ">预览</el-button>
            </div>
            <div style="float:left;width:88px;height:36px;line-height:16px;margin-left:15px" v-if="file !=null">
              <a :href="getFileDownloadUrl()">
                <el-button type="primary" style="
                  padding: 10px;
                  border-radius:2px;
                  width:86px;
              ">下载</el-button>
              </a>
            </div>
            <div style="float:left;width:88px;height:36px;line-height:16px;margin-left:15px" v-if="file !=null">
              <el-button type="danger" @click="doDelete()" v-show="file!=null" style="
                  padding: 10px;
                  border-radius:2px;
                  width:86px;
              ">删除</el-button>
            </div>
          </div>
        </div>
        <div class="text item" v-if="viewMode=='item'">
          <t-grid ref="itemGrid" :options="itemGridOptions">
          </t-grid>
        </div>
        <div v-if="viewMode=='card'">
          <item-card-view :file="file" @click="handlePreview"></item-card-view>
        </div>
        <div class="text item" v-show="uploadFileList.length>0" style="margin:10px;">
          <div class="file-list">
            <ul class="file-item" :class="`file-${file.id}`" v-for="file in uploadFileList">
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
        <uploader ref="uploader" :url="fileUploadUrl" uploadButton=".btns" :multiple="false" @fileChange="fileChange" @uploadStart="onUploadStart" @uploadBeforeSend="onUploadBeforeSend" @progress="onProgress" @success="onSuccess" @error="onError" @uploadError="onUploadError"></uploader>

      </el-card>
    </el-col>
  </el-row>
  <file-viewer ref="fileViewer" v-if="fileViewerVisible"></file-viewer>
</div>
</template>

<script>
import Uploader from '@/components/TUpload.vue'
import ItemCardView from './assetSingleManagementViewCard.vue'
import FileViewer from '@/components/TFileViewer.vue'
import axios from 'axios'

import {
  mapMutations,
  mapState,
} from 'vuex'

//ES6引入
export default {
  props: {
    viewMode: { //item,card
      type: String,
      default: 'card',
      required: false
    },
    businessDocId: {
      type: String,
      required: false
    },
    assetCategory: {
      type: String,
      required: false
    },
    disabled: {
      type: Boolean,
      default: false,
      required: false
    },
  },
  data() {
    return {
      fileUploadUrl: '',
      fileViewerVisible: false,
      fileIdList: [],
      file: null,
      uploadFileList: [],
      itemGridOptions: {
        dataSource: [],
        grid: {
          pageable: false,
          mutiSelect: false,
          columns: [{
              prop: 'name',
              label: '名称',
              sortable: false,
              formatter: (row, column, cellValue) => {
                return "<a target='_blank' href='" + window.SITE_CONFIG['serverUrl'] + "/authapi/base_AssetManagement/view?id=" + row.id + "'>" + row.name + "</a>";
              }
            },
            {
              prop: 'gmtCreatedOn',
              label: '上传时间',
              sortable: false,
              width: 180,
              formatter: (row, column, cellValue) => {
                return this.$util.datetimeFormat(row.gmtCreatedOn);
              }
            },
          ], // 需要展示的列
          defaultSort: {
            prop: 'id',
            order: 'descending'
          },
        }
      },
    }
  },
  components: {
    Uploader,
    FileViewer,
    ItemCardView
  },
  created() {
    this.fileUploadUrl = window.SITE_CONFIG['serverUrl'] + '/authapi/base_AssetManagement/upload';
    this.load();
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
  watch: {
    businessDocId(value) {
      this.load();
    }
  },
  methods: {
    isEmpty() {
      return this.file == null;
    },
    load() {
      let self = this;
      tapp.services.base_Common.getSUIds(200).then(function(result) {
        self.fileIdList = result;
        self.loadViewFileList();
      });
    },
    getFileId() {
      if (this.fileIdList.length == 0) {
        this.$notify.info({
          title: '系统提示',
          message: '您超出最大文件上传个数！'
        });
        return null;
      }
      return this.fileIdList.shift();
    },
    getFileDownloadUrl() {
      if (!this.file) {
        return;
      }
      return window.SITE_CONFIG['serverUrl'] + "/authapi/base_AssetManagement/download?id=" + this.file.id;
    },
    handlePreview() {
      if (!this.file) {
        return;
      }
      this.fileViewerVisible = true;
      let self = this;
      self.$nextTick(() => {
        self.$refs.fileViewer.show([self.file], self.file);
      })

    },
    doDelete() {
      let self = this;
      if (!this.file) {
        return;
      }
      self.$confirm('确认要删除吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        tapp.services.base_Attachment.delete(this.file.id).then(function(result) {
          self.doRefresh();
          self.$notify.success({
            title: '操作成功',
            message: '系统删除成功！'
          });
        })
      });
    },
    loadViewFileList() {
      if (!this.assetCategory || !this.businessDocId) {
        return;
      }
      let requestModel = {
        'businessDocId': this.businessDocId,
        "categoryId": this.assetCategory
      };
      let self = this;
      tapp.services.base_Attachment.getList(requestModel).then(function(result) {
        self.itemGridOptions.dataSource = result;
        if (result && result.length > 0) {
          self.file = result[0];
        } else {
          self.file = null;
        }
      });
    },
    doRefresh() {
      this.loadViewFileList();
    },
    fileChange(file) {
      if (!file.size) return;
      file.fid = this.getFileId();
      file.businessDocId = this.businessDocId;
      file.attachmentCategoryId = this.assetCategory;
      file.multiple = false;
      this.uploadFileList.length = 0;
      this.uploadFileList.push(file);
    },
    onUploadBeforeSend(object, data, header) {
      data.fid = object.file.fid;
      data.businessDocId = object.file.businessDocId;
      data.attachmentCategoryId = object.file.attachmentCategoryId;
      data.multiple = object.file.multiple;
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
      this.uploadFileList.length = 0;
      this.doRefresh();
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
      let index = this.uploadFileList.findIndex(ele => ele.id === file.id);
      this.uploadFileList.splice(index, 1);
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

<style lang="scss" >
$h-row: 36px;

.file-list {
    position: relative;
    minheight: 36px;
    overflow-y: auto;
    margin: 0;
}
.file-item {
    position: relative;
    height: $h-row;
    line-height: $h-row;
    padding: 0 10px;
    margin: 0;
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
.btns > div:nth-child(2) {
    width: 100%!important;
    height: 100%!important;
}
</style>
