<template>
<div>
  <el-row :gutter="20">
    <el-col :span="showCategoryTree?9:0" v-if="showCategoryTree">
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>附件类别</span>
          <el-button plain size="small" icon="el-icon-download" @click="doBatchDownload()" >一键下载</el-button>
        </div>
        <div class="text item">
          <t-tree ref="categoryTree" :options="categoryTreeOptons" @node-click="handleCategoryNodeClick">
          </t-tree>
        </div>
      </el-card>
    </el-col>
    <el-col :span="showCategoryTree?15:24" v-if="showAttachmentGrid">
      <el-card class="box-card">
        <div slot="header" class="clearfix" v-if="disabled==false">
          <span>{{selectedCategoryItemName}}</span>
          <div style="float: right; padding: 3px 0">
            <div class="btns " style="float:left; width:88px;height:56px;">
              <div id="picker">选择文件</div>
            </div>
            <div style="float:left;width:88px;height:56px;">
              <el-button type="primary" @click="handleGlobalPreview()" style="
                  padding: 10px;
                  border-radius:2px;
                  width:86px;
              ">文件预览</el-button>
            </div>
            <div style="float:left;width:88px;height:56px;">
              <el-button type="primary" @click="switchItemViewModel()" style="
                  padding: 10px;
                  border-radius:2px;
                  width:86px;
              ">{{switchItemViewModelButtonText}}</el-button>
            </div>
            <div style="float:left;width:88px;height:56px;">
              <el-button type="danger" @click="doItemBatchDelete()" :disabled="selectedItemRows.length <= 0" style="
                  padding: 10px;
                  border-radius:2px;
                  width:86px;
              ">批量删除</el-button>
            </div>

          </div>
        </div>
        <div class="text item" v-if="viewMode=='item'">
          <t-grid ref="itemGrid" :options="itemGridOptions" @selection-change="handleItemSelectionChange">
          </t-grid>
        </div>
        <div v-if="viewMode=='card'">
          <item-card-view :fileList="itemGridOptions.dataSource" @click="handlePreview" @select="handleItemSelectionChange"></item-card-view>
        </div>
      </el-card>
      <br/>
      <el-card class="box-card" v-show="uploadFileList.length>0">
        <div slot="header" class="clearfix">
          <span>待上传文件列表</span>
        </div>
        <div class="text item">
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
        <uploader ref="uploader" :url="fileUploadUrl" uploadButton=".btns" multiple @fileChange="fileChange" @uploadStart="onUploadStart" @uploadBeforeSend="onUploadBeforeSend" @progress="onProgress" @success="onSuccess" @error="onError" @uploadError="onUploadError"></uploader>
      </el-card>
    </el-col>
  </el-row>
  <file-viewer ref="fileViewer" v-if="fileViewerVisible"></file-viewer>
</div>
</template>

<script>
import Uploader from '@/components/TUpload.vue'
import ItemCardView from './assetMultiManagementItemViewCard.vue'
import FileViewer from '@/components/TFileViewer.vue'
import axios from 'axios'

import {
  mapMutations,
  mapState,
} from 'vuex'

//ES6引入
export default {
  props: {
    businessDocId: {
      type: String,
      required: true
    },
    assetCategoryClassifications: {
      type: Array,
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
    customername: {
      type: String,
      required: false
    },
  },
  data() {
    return {
      viewMode: 'card',
      selectedCategoryItem: null,
      checkItemAll: false,
      itemIsIndeterminate:true,
      showCategoryTree: true,
      showAttachmentGrid: false,
      fileUploadUrl: '',
      fileViewerVisible: false,
      fileIdList: [],
      uploadFileList: [],
      selectedItemRows: [],
      categoryTreeOptons: {
        dataSource: {
          loadDataOnFirst: false,
          serviceInstance: tapp.services.base_Attachment.getCategoriesTreeWithClassificationId,
          serviceInstanceInputParameters: {
            'cagegoryClassificationIds': this.assetCategoryClassifications,
            'businessDocId': this.businessDocId
          }
        },
        tree: {
          checkStrictly: true,
          showCheckbox: false,
          renderContent: this.renderCategoryTreeContent,
        }
      },
      itemGridOptions: {
        dataSource: [],
        grid: {
          pageable: false,
          mutiSelect: this.disabled == false,
          operates: {
            width: 50,
            fixed: 'left',
            list: [{
                type: 'text',
                show: true,
                label: '预览',
                method: this.handlePreview,
              },

            ]
          }, // 列操作按钮
          columns: [{
              prop: 'name',
              label: '名称',
              sortable: true,
              formatter: (row, column, cellValue) => {
                return "<a target='_blank' href='" + window.SITE_CONFIG['serverUrl'] + "/authapi/base_AssetManagement/view?id=" + row.id + "'>" + row.name + "</a>";
              }
            },
            {
              prop: 'gmtCreatedOn',
              label: '上传时间',
              sortable: true,
              width: 180,
              formatter: (row, column, cellValue) => {
                return this.$util.datetimeFormat(row.gmtCreatedOn);
              }
            },
            {
              prop: 'id',
              label: '下载',
              sortable: false,
              width: 60,
              formatter: (row, column, cellValue) => {
                return "<a target='_blank' href='" + window.SITE_CONFIG['serverUrl'] + "/authapi/base_AssetManagement/download?id=" + row.id + "'>下载</a>";
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

    if (this.assetCategoryClassifications != null && this.assetCategoryClassifications.length > 0) {
      this.$nextTick(() => {
        this.showCategoryTree = true;
        this.showAttachmentGrid = false;
        this.$refs.categoryTree.refresh();
      })
    } else if (this.assetCategory) {

      let self = this;
      self.showAttachmentGrid = true;
      self.showCategoryTree = false;

      tapp.services.base_Common.getSUIds(200).then(function(result) {
        self.fileIdList = result;
        self.loadViewFileList();
      });


    } else {
      alert('assetCategoryClassifications,assetCategory not all null')
    }
  },
  activated() {},
  computed: {
    selectedCategoryItemName() {
      if (this.selectedCategoryItem == null ||
        this.selectedCategoryItem.name == null) {
        return this.showCategoryTree ? '未选择附件类别' : '';
      }

      return this.selectedCategoryItem.name;
    },
    switchItemViewModelButtonText() {
      return this.viewMode == 'item' ? '文档模式' : '列表模式';
    },
    selectedAssetCategoryId() {
      if (this.assetCategory) {
        return this.assetCategory;
      }
      return this.selectedCategoryItem == null ? null : this.selectedCategoryItem.id;
    },
    uploader() {
      return this.$refs.uploader;
    },
    ...mapState({
      user: state => state.app.user,
    }),
  },
  methods: {
    renderCategoryTreeContent(h, {
      node,
      data,
      store
    }) {
      return (
        <span class = {[data.self.required ? 'is-required' : '', 'tree-node-label-container']} >
          {node.isLeaf?
            <span class = "tree-node-lable"> { node.label + '(' + (data.self.attachmentCount || 0) + ')'} </span>
            :<span class = "tree-node-lable"> { node.label}</span>
          }
        </span> );

    },
    switchItemViewModel() {
      if (this.viewMode == 'item') {
        this.viewMode = 'card';
      } else {
        this.viewMode = 'item';
        this.doItemRefresh();
      }
    },

    handleGlobalPreview() {
      this.handlePreview(null, null);
    },
    handlePreview(key, row) {
      let self = this;
      if (self.assetCategoryClassifications != null) {
        let requestModel = {
          'businessDocId': self.businessDocId,
          "cagegoryClassificationIds": self.assetCategoryClassifications
        };
        tapp.services.base_Attachment.getListByClassificationIds(requestModel).then(function(result) {
          if (result.length == 0) {
            self.$notify.info({
              title: '系统提示',
              message: '没有文件可以预览！'
            });
            return;
          } else {
            self.fileViewerVisible = true;
            let currItem = row == null ? result[0] : row;
            self.$nextTick(() => {
              self.$refs.fileViewer.show(result, currItem);
            })
          }
        });
      } else if (self.assetCategory != null) {
        let requestModel = {
          'businessDocId': self.businessDocId,
          "categoryId": self.selectedAssetCategoryId
        };
        tapp.services.base_Attachment.getList(requestModel).then(function(result) {
          if (result.length == 0) {
            self.$notify.info({
              title: '系统提示',
              message: '没有文件可以预览！'
            });
            return;
          } else {
            self.fileViewerVisible = true;
            let currItem = row == null ? result[0] : row;
            self.$nextTick(() => {
              self.$refs.fileViewer.show(result, currItem);
            })
          }
        });
      }
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
    handleCategoryNodeClick(dataItem, node, el) {
      if (dataItem.hasChildren) {
        this.showAttachmentGrid = false;
        this.$refs.categoryTree.expandOrNot(dataItem);
      } else {
        this.uploadFileList = [];
        this.showAttachmentGrid = true;
        this.selectedCategoryItem = dataItem;

        let self = this;
        tapp.services.base_Common.getSUIds(200).then(function(result) {
          self.fileIdList = result;
          self.loadViewFileList();
          console.log('getSUIDS')
        });
      }
    },
    handleItemSelectionChange(val) {
      this.selectedItemRows = val;
    },
    loadViewFileList() {
      if (!this.selectedAssetCategoryId || !this.businessDocId) {
        return;
      }
      let requestModel = {
        'businessDocId': this.businessDocId,
        "categoryId": this.selectedAssetCategoryId
      };
      let self = this;
      tapp.services.base_Attachment.getList(requestModel).then(function(result) {
        result.forEach(p => {
          p.checked = false;
        })
        self.itemGridOptions.dataSource = result;

        let newCategoryItem = {...self.selectedCategoryItem};
        newCategoryItem.self.attachmentCount = result.length;
        self.$refs.categoryTree.refreshNode(self.selectedAssetCategoryId,newCategoryItem);

      });
    },
    doItemBatchDelete() {
      let self = this;
      if (!self.selectedItemRows || self.selectedItemRows.length == 0) {
        self.$notify.info({
          title: '系统提示',
          message: '您没选择任何行，无法操作！'
        });
        return;
      }
      let ids = self.selectedItemRows.map(function(row) {
        return row.id;
      });
      self.$confirm('确认要删除共' + ids.length + '项吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        tapp.services.base_Attachment.batchDelete(ids).then(function(result) {
          self.doItemRefresh();
          self.$notify.success({
            title: '操作成功',
            message: '系统删除成功！'
          });
        })
      });
    },
    doItemRefresh() {
      this.loadViewFileList();
    },
    fileChange(file) {
      if (this.selectedAssetCategoryId == null) {
        this.$notify.info({
          title: '系统提示',
          message: '未选择附件类别！'
        });
        return;
      }
      if (!file.size) return;
      file.fid = this.getFileId();
      file.businessDocId = this.businessDocId;
      file.attachmentCategoryId = this.selectedAssetCategoryId;
      this.uploadFileList.push(file);
    },
    onUploadBeforeSend(object, data, header) {
      if (this.selectedAssetCategoryId == null) {
        this.$notify.info({
          title: '系统提示',
          message: '未选择附件类别！'
        });
        return;
      }
      data.fid = object.file.fid;
      data.businessDocId = object.file.businessDocId;
      data.attachmentCategoryId = object.file.attachmentCategoryId;
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
      // 在ui上移除
      let index = this.uploadFileList.findIndex(ele => ele.id === file.id);
      this.uploadFileList.splice(index, 1);
      if (this.uploadFileList.length == 0) {
        this.doItemRefresh();
      }
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
    doBatchDownload() {
      let customername = this.customername || '';
      window.open(window.SITE_CONFIG['serverUrl'] + '/authapi/base_AssetManagement/batchDownload?assetCategoryClassifications=' + this.assetCategoryClassifications + '&businessDocId=' + this.businessDocId + '&customername=' + customername);
    }
  }

}
</script>
<style >
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}

.el-card__header {
  height: 82px;
}

.is-required .tree-node-lable:before {
  content: '*';
  color: #f56c6c;
  margin-right: 4px;
}
</style>
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
