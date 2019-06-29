<template>
<div style="display:inline">
  <el-button @click="doPrint()" :disabled="disabled">{{title}}</el-button>
  <export-template-open-dialog v-if="openDialogVisible" ref="openDialog"></export-template-open-dialog>
</div>
</template>
<script>
import ExportTemplateOpenDialog from './exportTemplateOpenDialog'
export default {
  components: {
    ExportTemplateOpenDialog
  },
  props: {
    templateUrl: null,
    templateCategoryId: null,
    title: null,
    disabled: false,
    docId: null,
  },
  data() {
    return {
      openDialogVisible: false,
    }
  },
  watch: {

  },
  created() {},
  mounted() {},
  computed: {},
  mounted() {},
  methods: {
    doPrint() {
      this.openDialogVisible = true
      this.$nextTick(() => {
        this.$refs.openDialog.init(this.templateUrl, this.templateCategoryId, this.title,this.docId);
      })
    },
  },
}
</script>
