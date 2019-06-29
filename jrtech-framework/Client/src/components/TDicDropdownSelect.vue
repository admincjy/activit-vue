<template>
<el-select :placeholder="placeholder" v-model="currentValue" clearable @change="handleChange" :disabled="disabled">
  <el-option v-for="(item, index) in data" :key='item.id' :label="item.name" :value="item.id"></el-option>
</el-select>
</template>
<script>
export default {
  components: {},
  props: {
    dicType: {
      type: String,
      required: true
    },
    placeholder: '请选择',
    value: null,
    disabled: {
      type: Boolean,
      default: false,
      required: false
    },
  },
  data() {
    return {
      selectValue: null,
      data: [],
    }
  },
  watch: {
    value(value) {
      this.currentValue = value;
    }
  },
  created() {
    this.currentValue = this.value;

    let data = [];
    if (this.dicType) {
      data = tapp.data.base_datadictionary[this.dicType.toLowerCase()];
    }
    this.data = data;
  },
  mounted() {},
  computed: {
    currentValue: {
      // 动态计算currentValue的值
      get: function() {
        return this.selectValue;
      },
      set: function(val) {
        this.selectValue = val;
        this.$emit('input', val);
      }
    }
  },
  mounted() {},
  methods: {
    isEmpty() {
      return this.currentValue == null || this.currentValue.length == 0;
    },
    handleChange(val) {
     let item = this.data.find(p=>p.id===val);
      this.$emit('change', item);
    }
  },
}
</script>
