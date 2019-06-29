<template>
<div>
  <el-input ref="ref1" :placeholder="placeholder" v-model="currentText" v-on:click.native="onSelect()" readonly="true" :disabled="disabled">
    <i class="el-icon-close el-input__icon" slot="suffix" @click.stop="onClear">
    </i>
  </el-input>
  <user-open-dialog v-if="userOpenDialogVisible" ref="userOpenDialog" @select="onUserOpenDialogSelect"></user-open-dialog>
</div>
</template>
<script>
import UserOpenDialog from './userOpenDialog'
export default {
  components: {
    UserOpenDialog
  },
  props: {
    roleCategory: {
      type: String,
      required: false
    },
    placeholder: '',
    value: null,
    text: null,
    disabled: false,
  },
  data() {
    return {
      userOpenDialogVisible: false,
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
      if (this.disabled) {
        return;
      }
      this.userOpenDialogVisible = true
      this.$nextTick(() => {
        this.$refs.userOpenDialog.init(this.roleCategory);
      })
    },
    onClear(event) {
      if (this.disabled) {
        return;
      }
      this.currentText = null;
      this.currentValue = null;
      this.$emit('input', null);
      this.$emit('change', null);
    },
    onUserOpenDialogSelect(val) {
      this.userOpenDialogVisible = false;
      this.currentText = val.name;
      this.currentValue = val.id;
      let self = this;
      tapp.services.base_User.getUserWithDepartments(val.id).then(function(result) {
        self.$emit('input', result.id);
        self.$emit('change', result);
        self.$emit('blur');
        if (self.$parent.$options.componentName === 'ElFormItem') {
            self.$parent.$emit('el.form.change', self.currentValue);
        }
      });
    },
    isEmpty() {
      return this.currentValue == null || this.currentValue.length == 0;
    },
  },
}
</script>
