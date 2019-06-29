
<template>
<div>
  <el-checkbox-group v-model="currentValue" @change="handleChange" :disabled="disabled">
    <el-checkbox v-for="item in data" :key="item.id" :label="item.id">{{item.name}}</el-checkbox>
  </el-checkbox-group>
</div>
</template>
<script>
export default {
  components: {},
  props: {
    dicType: {
      type: String,
      required: true
    },
    value: {
      type: Array,
      default: null,
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
      selectValue: [],
      data: [],
    }
  },
  watch: {
    value(value) {
      this.currentValue = value || [];
    }
  },
  created() {
    this.currentValue = this.value || [];

    let data = [];
    if (this.dicType) {
      data = tapp.data.base_datadictionary[this.dicType];
    }
    this.data = data;
  },
  mounted() {},
  computed: {
    currentValue: {
      // 动态计算currentValue的值
      get: function() {
        return this.selectValue || [];
      },
      set: function(val) {
        this.selectValue = val || [];
        this.$emit('input', this.selectValue);
      }
    }
  },
  mounted() {},
  methods: {
    isEmpty() {
      return this.currentValue == null || this.currentValue.length == 0;
    },
    handleChange(val) {
      this.$emit('change', val);
    }
  },
}
</script>
