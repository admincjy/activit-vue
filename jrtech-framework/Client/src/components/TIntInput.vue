<template>
<number-input ref="input" :placeholder="placeholder" v-model="currentValue" @change="handleChange" currency="" :precision="0" separator='' :disabled="disabled" :readOnly="readOnly">
  <template slot="prepend" v-if="$slots.prepend">
    <slot name="prepend">
    </slot>
  </template>
  <template slot="append" v-if="$slots.append">
    <slot name="append">
    </slot>
  </template>
</number-input>
</template>

<script>
import NumberInput from './TNumberInput.vue'
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
      this.$emit('change', val);
    }
  },
}
</script>
