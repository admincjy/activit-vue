<template>
<el-select :placeholder="placeholder" v-model="currentValue" clearable @change="handleChange" :disabled="disabled">
  <el-option-group
      v-for="group in data"
      :key="group.id"
      :label="group.name">
      <el-option
        v-for="item in group.items"
        :key="item.id"
        :label="item.name"
        :value="item.id">
      </el-option>
    </el-option-group>
</el-select>
</template>
<script>
export default {
  components: {},
  props: {
    placeholder: '请选择',
    disabled:false,
    value: null,
  },
  data() {
    return {
      selectValue: null,
      data: [],
    }
  },
  watch: {
   value (value) {
     this.currentValue = value;
   }
 },
  created() {
    this.currentValue = this.value;
    this.data = tapp.pl_loanproducttypes;
  },
  mounted() {
  },
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
      let dataItems = this.data.map((item)=>item.items)
      .reduce((a, b) => a.concat(b), []);
      let selectedItem = dataItems.find(p=>p.id===val);
      this.$emit('change', selectedItem);
    }
  },
}
</script>
