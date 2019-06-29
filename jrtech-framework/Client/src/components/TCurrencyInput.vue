<template>
<el-tooltip :tabindex="1" class="item" effect="dark" :content="chineseValue" placement="top-start" v-model="chineseValueShow" @focus="onToolTipFocusHandler">
  <number-input ref="input" :tabindex="0" :placeholder="placeholder" v-model="currentValue" @change="handleChange" @blur="onBlurHandler" @focus="onFocusHandler" :disabled="disabled" :readOnly="readOnly">
    <template slot="prepend" v-if="$slots.prepend">
    <slot name="prepend">
    </slot>
  </template>
    <template slot="append" v-if="$slots.append">
    <slot name="append">
    </slot>
  </template>
  </number-input>
</el-tooltip>
</template>
<script>
import NumberInput from './TNumberInput.vue'
import util from '@/util'
export default {
  components: {
    NumberInput
  },
  props: {
    placeholder: {
      type: String,
      default: '',
      required: false
    },
    disabled: false,
    unitValue: null,
    value: null,
    readOnly: {
      type: Boolean,
      default: false,
      required: false
    },
  },
  data() {
    return {
      selectValue: null,
      chineseValue: '金额大写',
      chineseValueShow: false,
    }
  },
  watch: {
    value(value) {
      this.currentValue = value;
    }
  },
  created() {
    this.currentValue = this.value;
  },

  computed: {
    currentValue: {
      // 动态计算currentValue的值
      get: function() {
        return this.selectValue;
      },
      set: function(val) {
        this.selectValue = val;
        this.setChineseValue(val);
        this.$emit('input', val);
      }
    },

  },
  mounted() {
    this.$el.tabIndex = 1;
  },
  methods: {
    isEmpty() {
      return this.currentValue == null || this.currentValue.length == 0;
    },
    onToolTipFocusHandler() {
      this.$refs.input.focus();
    },
    onFocusHandler() {
      this.setChineseValue(this.currentValue);
      this.chineseValueShow = true;
      this.$emit('focus', this.currentValue);
    },
    onBlurHandler() {
      this.chineseValueShow = false;
      this.$emit('blur', this.currentValue);
    },
    handleChange(val) {
      this.setChineseValue(val);
      this.chineseValueShow = true;
      this.$emit('change', val);
    },
    setChineseValue(val) {
      if (val == null || val == undefined) {
        return;
      }

      if (val == 0) {
        this.chineseValue = '零圆整';
        return;
      }
      let yValue = (this.unitValue || 1) * val;
      this.chineseValue = util.moneyArabiaToChinese(yValue);
    },
  },
}
</script>
