<template>
<div>
  <div v-if="file !=null ">
    <div class="el-upload-list el-upload-list--picture-card">
      <li tabindex="0" class="el-upload-list__item" style="width:200px;height:290px">
        <img :src="getFileUrl(file)" :alt="file.name" class="image" @click="selectFile(file)" style="width:200px;height:200px" v-if=" getFileType(file) == 'image'">
        <div class="image" @click="selectFile(file)" style="width:200px;line-height:200px;height:200px;vertical-align: middle;text-align:center" v-else>
          <span style="font-size: 4.5em; vertical-align: middle;"> <i class="el-icon-question"></i> </span>
        </div>
        <div style="padding: 14px;">
          <div>{{file.name}}</div>
          <div class="bottom clearfix">
            <time class="time">{{ $util.datetimeFormat(file.gmtCreatedOn) }}</time>
          </div>
        </div>
      </li>
      </ul>
    </div>
  </div>
  <div style="text-align:center" v-else>
    <span>无文件</span>
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
    file: {
      type: Object,
      default: null,
      required: false
    },
  },
  data() {
    return {}
  },
  watch: {},
  created() {},
  mounted() {},
  computed: {},
  mounted() {},
  methods: {
    getFileUrl(file) {
      return window.SITE_CONFIG['serverUrl'] + "/authapi/base_AssetManagement/view?tb=1&id=" + file.id;
    },
    getFileDownloadUrl(file) {
      return window.SITE_CONFIG['serverUrl'] + "/authapi/base_AssetManagement/download?id=" + file.id;
    },
    getFileType(file) {
      return this.$util.getFileType(file.name);
    },
    selectFile(file) {
      this.$emit('click', file.id, file);
    },
  },
}
</script>
<style lang="scss" scoped>
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
