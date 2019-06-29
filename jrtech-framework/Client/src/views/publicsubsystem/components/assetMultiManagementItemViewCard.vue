<template>
<div>
  <div v-if="fileList.length>0">
    <div style="margin-bottom: 20px;">
      <el-checkbox :indeterminate="itemIsIndeterminate" v-model="checkItemAll" @change="handleCheckAllChange">全选</el-checkbox>
    </div>
    <ul class="el-upload-list el-upload-list--picture-card">
      <li tabindex="0" v-for="file,index in fileList" :key="index" class="el-upload-list__item" style="width:180px;height:290px">
        <img :src="getFileUrl(file)" :alt="file.name" class="image" @click="selectFile(file)" style="width:200px;height:180px" v-if=" getFileType(file) == 'image'">
        <div class="image" @click="selectFile(file)" style="width:180px;line-height:180px;height:180px;vertical-align: middle;text-align:center" v-else>
          <span style="font-size: 4.5em; vertical-align: middle;"> <i class="el-icon-question"></i> </span>
        </div>
        <div style="padding: 14px;">
          <el-checkbox @change="checkFile()" :label="file.name" :key="file.id" v-model="file.checked">{{file.name}}</el-checkbox>
          <div class="bottom clearfix">
            <time class="time">{{ $util.datetimeFormat(file.gmtCreatedOn) }}</time>
            <a :href="getFileDownloadUrl(file)">
              <el-button type="text" class="button">下载</el-button>
            </a>
          </div>
        </div>
      </li>
    </ul>
  </div>
  <div style="text-align:center" v-else>
    <span>暂无数据</span>
  </div>
</div>
</template>
<script>
export default {
  components: {},
  props: {
    disabled: {
      type: Boolean,
      default: false,
      required: false
    },
    fileList: {
      type: Array,
      default: [],
      required: true
    },
  },
  data() {
    return {
      checkItemAll: false,
      itemIsIndeterminate: false,
    }
  },
  watch: {
    fileList(newValue, oldValue) {
      if (newValue != oldValue) {
        this.checkItemAll = false;
        this.itemIsIndeterminate = false;
      }
    }
  },
  created() {},
  mounted() {},
  computed: {},
  mounted() {},
  methods: {
    handleCheckAllChange(val) {
      this.checkItemAll = val;

      this.fileList.forEach(p => {
        p.checked = val;
      })

      this.itemIsIndeterminate = false;
      this.$emit('select', this.fileList);
    },
    getFileUrl(file) {
      return window.SITE_CONFIG['serverUrl'] + "/authapi/base_AssetManagement/view?tb=1&id=" + file.id;
    },
    getFileDownloadUrl(file) {
      return window.SITE_CONFIG['serverUrl'] + "/authapi/base_AssetManagement/download?id=" + file.id;
    },
    getFileType(file) {
      return this.$util.getFileType(file.name);
    },
    checkFile() {
      let checkedFiles = this.fileList.filter((item, index) => {
        return item.checked;
      });
      if (checkedFiles.length == this.fileList.length) {
        this.handleCheckAllChange(true);
      } else if (checkedFiles.length == 0) {
        this.handleCheckAllChange(false);
      } else {
        this.itemIsIndeterminate = true;
        this.$emit('select', checkedFiles);
      }
    },
    selectFile(file) {
      this.$emit('click', file.id, file);
    },
  },
}
</script>
