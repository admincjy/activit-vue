<template>
<el-date-picker :type="type" v-model="selectValue" align="right" :picker-options="pickerOptions" :value-format="'yyyy-MM-dd HH:mm:ss'" :disabled="disabled" @change="handleChange">
</el-date-picker>
</template>
<script>
import moment from 'moment';
import util from '@/util'
export default {
  components: {},
  props: {
    value: null,
    disabled: false,
    type: {
      type: String,
      default: 'datetime'
    }
  },
  data() {
    return {
      selectValue: null,
      pickerOptions: {
        shortcuts: [{
          text: '今天',
          onClick(picker) {
            const date = util.datetimeFormat(moment());
            picker.$emit('pick', date);
          }
        }, {
          text: '昨天',
          onClick(picker) {
            const date = util.datetimeFormat(moment().startOf('day').subtract(1, 'days'));
            picker.$emit('pick', date);
          }
        }, {
          text: '周一',
          onClick(picker) {
            const date = util.datetimeFormat(moment().startOf('isoWeek'));
            picker.$emit('pick', date);
          }
        }, {
          text: '月初',
          onClick(picker) {
            const date = util.datetimeFormat(moment().startOf('month'));
            picker.$emit('pick', date);
          }
        }, {
          text: '年初',
          onClick(picker) {
            const date = util.datetimeFormat(moment().startOf('year'));
            picker.$emit('pick', date);
          }
        }]
      },
    }
  },
  watch: {
    value(value) {
      if (value) {
        this.currentValue = value;
      } else {
        this.currentValue = null;
      }

    }
  },
  created() {
    if (this.value) {
      this.currentValue = this.value;
    } else {
      this.currentValue = null;
    }
  },
  mounted() {},
  computed: {
    valueFormat: {
      get: function() {
        let datefromPattern = '"YYYY-MM-DD HH:mm:ss"';
        return datefromPattern;
      },
    },
    currentValue: {
      // 动态计算currentValue的值
      get: function() {
        return this.selectValue;
      },
      set: function(val) {
        this.selectValue = val;
      }
    }
  },
  mounted() {},
  methods: {
    isEmpty() {
      return this.currentValue == null || this.currentValue.length == 0;
    },
    handleChange(val) {
      this.$emit('input', val);
      this.$emit('blur');
      this.$emit('change', val);

    }
  },
}
</script>
