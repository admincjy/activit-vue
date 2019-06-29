<template>
<el-popover placement="top-start" trigger="hover" width="500" @show="onShow" @hide="onHide">

  <div v-html="content"></div> 
  <div style="text-align: right; margin: 0">
    <el-button type="primary" size="mini" @click="goUrl" v-if="url!=null">转到</el-button>
  </div>
  <el-button type="text" slot="reference">{{text}}</el-button>
</el-popover>
</template>
<script>
export default {
  props: {
    id: '',
    text: '',
    titile: '',
    content: '',
    url: null,
  },
  data() {
    return {}
  },
  watch: {

  },
  mounted() {

  },
  methods: {
    goUrl() {
      let tpath = this.url;
      if (!tpath) {
        return;
      }
      if (tpath.startsWith('http')) {
        window.location.href = tpath;
      } else {
        this.$router.push({
          path: tpath
        });
      }

      this.$emit('go', this.id);
    },
    onShow() {
      this.$emit('show', this.id);
    },
    onHide() {
      this.$emit('hide', this.id);
    }
  }
}
</script>
