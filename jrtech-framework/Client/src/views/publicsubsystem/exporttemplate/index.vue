<template>
<div>
  <el-row :gutter="20">
    <el-col :span="9">
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>类别</span>
        </div>
        <div class="text item">
          <t-tree ref="categoryTree" :options="categoryTreeOptons" @node-click="handleCategoryNodeClick">
          </t-tree>
        </div>
      </el-card>
    </el-col>
    <el-col :span="15" >
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>{{selectedCategoryItemName}}</span>
          <div style="float: right; padding: 3px 0">
            <div class="btns " style="float:left; width:88px;height:56px;">
              <div id="picker">选择文件</div>
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
        <div class="text item">
          <t-grid ref="itemGrid" :options="itemGridOptions" @selection-change="handleItemSelectionChange">
          </t-grid>
        </div>
      </el-card>
      <br/>
      <el-card class="box-card" v-show="fileList.length>0">
        <div slot="header" class="clearfix">
          <span>待上传文件列表</span>
        </div>
        <div class="text item">
          <div class="file-list">
            <ul class="file-item" :class="`file-${file.id}`" v-for="file in fileList">
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
</div>
</template>

<script>
import Uploader from '@/components/TUpload.vue'
import axios from 'axios'

import {
  mapMutations,
  mapState,
} from 'vuex'

//ES6引入
export default {
  data() {
    return {
      selectedCategoryItem: null,
      fileUploadUrl: '',
      fileIdList: [],
      fileList: [],
      selectedItemRows: [],
      categoryTreeOptons: {
        tree: {
          checkStrictly: true,
          showCheckbox: false,
        },
          dataSource: [],
      },
      itemGridOptions: {
        dataSource: {
          loadDataOnFirst: false,
          serviceInstance: tapp.services.base_ExportTemplate.getList,
          serviceInstanceInputParameters: null,
        },
        grid: {
          pageable: false,
          operates: {}, // 列操作按钮
          columns: [{
              prop: 'id',
              label: '下载',
              sortable: false,
              width: 60,
              formatter: (row, column, cellValue) => {
                return "<a target='_blank' href='" + window.SITE_CONFIG['serverUrl'] + "/authapi/base_exportTemplateManagement/download?id=" + row.id + "'>下载</a>";
              }
            },
            {
              prop: 'name',
              label: '名称',
              sortable: true,
              formatter: (row, column, cellValue) => {
                return "<a target='_blank' href='" + window.SITE_CONFIG['serverUrl'] + "/authapi/base_exportTemplateManagement/download?id=" + row.id + "'>" + row.name + "</a>";
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
          ], // 需要展示的列
          defaultSort: {
            prop: 'id',
            order: 'descending'
          },
        }
      }
    }
  },
  components: {
    Uploader
  },
  created() {
    this.fileUploadUrl = window.SITE_CONFIG['serverUrl'] + '/authapi/base_exportTemplateManagement/upload';

    let self = this;
    let requestModel = {
      "categoryId":"base_exporttemplate",
      "sorting":" id desc",
    };
    tapp.services.base_DataDictionary.getDataDictionaries(requestModel).then(function(result) {
      self.data = result;
      let categoryTreeData = result.map(p => {
        return {
          id: p.id,
          name: p.name,
          parentId: null,
          level: 1,
          items: [],
        }
      });
      self.categoryTreeOptons.dataSource = categoryTreeData; 
    });
  },
  activated() {},
  computed: {
    selectedCategoryItemName() {
      if (this.selectedCategoryItem == null ||
        this.selectedCategoryItem.name == null) {
        return '未选择类别'
      }
      return this.selectedCategoryItem.name;
    },
    uploader() {
      return this.$refs.uploader;
    },
    ...mapState({
      user: state => state.app.user,
    }),
  },
  methods: {

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
      this.fileList = [];
      this.selectedCategoryItem = dataItem;
      let self = this;
      tapp.services.base_Common.getSUIds(200).then(function(result) {
        self.fileIdList = result;
        console.log('getSUIDS')
      });
      this.$nextTick(() => {
        this.itemGridOptions.dataSource.serviceInstanceInputParameters = dataItem.id;
        this.$refs.itemGrid.refresh();
      })

    },
    handleItemSelectionChange(val) {
      this.selectedItemRows = val;
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
        tapp.services.base_ExportTemplate.batchDelete(ids).then(function(result) {
          self.doItemRefresh();
          self.$notify.success({
            title: '操作成功',
            message: '系统删除成功！'
          });
        })
      });
    },
    doItemRefresh() {
      this.$refs.itemGrid.refresh();
    },
    fileChange(file) {
      if (this.selectedCategoryItem == null) {
        this.$notify.info({
          title: '系统提示',
          message: '未选择附件类别！'
        });
        return;
      }
      if (!file.size) return;
      file.fid = this.getFileId();
      file.businessDocId = this.businessDocId;
      file.attachmentCategoryId = this.selectedCategoryItem.id;
      this.fileList.push(file);
    },
    onUploadBeforeSend(object, data, header) {
      if (this.selectedCategoryItem == null) {
        this.$notify.info({
          title: '系统提示',
          message: '未选择类别！'
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
      let index = this.fileList.findIndex(ele => ele.id === file.id);
      this.fileList.splice(index, 1);
      if (this.fileList.length == 0) {
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
      let index = this.fileList.findIndex(ele => ele.id === file.id);
      this.fileList.splice(index, 1);
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
<style lang="scss">
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
</style>
