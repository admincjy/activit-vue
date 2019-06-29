<template>
<div>
  <el-input ref="ref1" :placeholder="placeholder" v-model="currentText" v-on:click.native="onSelect()" readonly="true" :disabled="disabled">
    <i class="el-icon-close el-input__icon" slot="suffix" @click.stop="onClear">
      </i>
  </el-input>
  <organization-open-dialog v-if="organizationOpenDialogVisible" ref="organizationOpenDialog" @select="onorganizationOpenDialogSelect"></organization-open-dialog>
</div>
</template>
<script>
import OrganizationOpenDialog from './organizationOpenDialog'
export default {
  components: {
    OrganizationOpenDialog
  },
  props: {
    placeholder: '',
    value: null,
    text: null,
    disabled: false,
  },
  data() {
    return {
      organizationOpenDialogVisible: false,
      currentValue: null,
      currentText: null,
    }
  },
  watch: {
    value(value) {
      this.currentValue = value;
    },
    text(value) {
      this.currentText = value;
    }
  },
  created() {
    this.currentValue = this.value;
    this.currentText = this.text;
  },
  mounted() {},
  computed: {},
  mounted() {},
  methods: {
    onSelect() {
      this.organizationOpenDialogVisible = true
      this.$nextTick(() => {
        this.$refs.organizationOpenDialog.init();
      })
    },
    onClear(event) {
      this.currentText = null;
      this.currentValue = null;
      this.$emit('input', null);
      this.$emit('change', null);
    },
    onorganizationOpenDialogSelect(val) {

      this.organizationOpenDialogVisible = false;

      this.currentText = val.name ;
      this.currentValue = val.id;
      this.$emit('input', val.id);
      this.$emit('change', val);
      if (this.$parent.$options.componentName === 'ElFormItem') {
          this.$parent.$emit('el.form.change', this.currentValue);
      }
    },
    isEmpty() {
      return this.currentValue == null || this.currentValue.length == 0;
    },
  },
}
</script>
