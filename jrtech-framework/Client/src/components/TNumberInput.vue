/*
数字（decimal)输入控件
*/
<template>
<el-input :placeholder="placeholder" @blur="onBlurHandler" @input="onInputHandler" @focus="onFocusHandler" ref="numeric" type="tel" v-model="amount" :readOnly="readOnly" :disabled="disabled">
  <template slot="prepend" v-if="$slots.prepend">
    <slot name="prepend">
    </slot>
  </template>
  <template slot="append" v-if="$slots.append">
    <slot name="append">
    </slot>
  </template>
</el-input>
</template>

<script>
import accounting from 'accounting-js'
export default {
  name: 'NumberInput',
  props: {
    /**
     * 前缀符号.
     */
    currency: {
      type: String,
      default: '',
      required: false
    },
    /**
     * 允许的最大值.
     */
    max: {
      type: Number,
      default: Number.MAX_SAFE_INTEGER || 9007199254740991,
      required: false,
    },
    /**
     * 允许的最小值.
     */
    min: {
      type: Number,
      default: Number.MIN_SAFE_INTEGER || -9007199254740991,
      required: false
    },
    /**
     * 是否启用最小值验证.
     */
    minus: {
      type: Boolean,
      default: false,
      required: false
    },
    /**
     * placeholder.
     */
    placeholder: {
      type: String,
      default: '',
      required: false
    },
    /**
     * Value when the input is empty
     */
    emptyValue: {
      type: [Number, String],
      default: '',
      required: false
    },
    /**
     * Number of decimals.
     * Decimals symbol are the opposite of separator symbol.
     */
    precision: {
      type: Number,
      default: 2,
      required: false
    },
    /**
     * Thousand separator type.
     * Separator props accept either . or , (default).
     */
    separator: {
      type: String,
      default: ',',
      required: false
    },
    /**
     * Forced thousand separator.
     * Accepts any string.
     */
    thousandSeparator: {
      default: undefined,
      required: false,
      type: String
    },
    /**
     * Forced decimal separator.
     * Accepts any string.
     */
    decimalSeparator: {
      default: undefined,
      required: false,
      type: String
    },
    /**
     * The output type used for v-model.
     * It can either be String or Number (default).
     */
    outputType: {
      required: false,
      type: String,
      default: 'Number'
    },
    /**
     * v-model value.
     */
    value: {
      default: null,
      required: false
    },
    /**
     * Hide input and show value in text only.
     */
    disabled: {
      type: Boolean,
      default: false,
      required: false
    },
    readOnly: {
      type: Boolean,
      default: false,
      required: false
    },
    /**
     * Position of currency symbol
     * Symbol position props accept either 'suffix' or 'prefix' (default).
     */
    currencySymbolPosition: {
      type: String,
      default: 'prefix',
      required: false
    }
  },
  data: () => ({
    amount: null
  }),
  computed: {
    /**
     * Number type of formatted value.
     * @return {Number}
     */
    amountNumber() {
      if (this.isAmountNull(this.amount)) {
        return null;
      }
      return this.unformat(this.amount)
    },
    /**
     * Number type of value props.
     * @return {Number}
     */
    valueNumber() {
      if (this.isAmountNull(this.value)) {
        return null;
      }
      return this.unformat(this.value)
    },
    /**
     * Define decimal separator based on separator props.
     * @return {String} '.' or ','
     */
    decimalSeparatorSymbol() {
      if (typeof this.decimalSeparator !== 'undefined') return this.decimalSeparator
      if (this.separator === ',') return '.'
      return ','
    },
    /**
     * Define thousand separator based on separator props.
     * @return {String} '.' or ','
     */
    thousandSeparatorSymbol() {
      if (typeof this.thousandSeparator !== 'undefined') return this.thousandSeparator
      if (this.separator === '.') return '.'
      if (this.separator === 'space') return ' '
      return ','
    },
    /**
     * Define format position for currency symbol and value.
     * @return {String} format
     */
    symbolPosition() {
      if (!this.currency) return '%v'
      return this.currencySymbolPosition === 'suffix' ? '%v %s' : '%s %v'
    }
  },
  watch: {
    /**
     * Watch for value change from other input with same v-model.
     * @param {Number} newValue
     */
    valueNumber(newValue) {
      if (this.$refs.numeric !== document.activeElement) {
        this.amount = this.format(newValue)
      }
    },

    /**
     * Immediately reflect separator changes
     */
    separator() {
      this.process(this.valueNumber)
      this.amount = this.format(this.valueNumber)
    },
    /**
     * Immediately reflect currency changes
     */
    currency() {
      this.process(this.valueNumber)
      this.amount = this.format(this.valueNumber)
    },
    /**
     * Immediately reflect precision changes
     */
    precision() {
      this.process(this.valueNumber)
      this.amount = this.format(this.valueNumber)
    }
  },
  mounted() {


    // Set default value props when placeholder undefined.

    this.process(this.valueNumber)
    this.amount = this.format(this.valueNumber)
    // In case of delayed props value.
    setTimeout(() => {
      this.process(this.valueNumber)
      this.amount = this.format(this.valueNumber)
    }, 500)

  },
  methods: {
    isAmountNull(amount) {
      return amount == undefined || amount == null;
    },
    /**
     * Handle blur event.
     * @param {Object} e
     */
    onBlurHandler(e) {

      this.$emit('blur', e)
      this.process(this.amountNumber)
      this.amount = this.format(this.amountNumber)

    },
    /**
     * Handle focus event.
     * @param {Object} e
     */
    onFocusHandler(e) {
      if (this.isAmountNull(this.value)) {
        return;
      }
      this.$emit('focus', e)
      if (this.amountNumber === null) {
        this.amount = null
      } else {
        this.amount = accounting.formatMoney(this.amountNumber, {
          symbol: '',
          format: '%v',
          thousand: '',
          decimal: this.decimalSeparatorSymbol,
          precision: Number(this.precision)
        })
      }
    },
    /**
     * Handle input event.
     */
    onInputHandler(e) {
      this.$emit('change', e);
    },
    /**
     * Validate value before update the component.
     * @param {Number} value
     */
    process(value) {
      if (this.isAmountNull(value)) {
        return;
      }
      if (value >= this.max) this.update(this.max)
      if (value <= this.min) this.update(this.min)
      if (value > this.min && value < this.max) this.update(value)
      if (!this.minus && value < 0) this.min >= 0 ? this.update(this.min) : this.update(0)
    },
    /**
     * Update parent component model value.
     * @param {Number} value
     */
    update(value) {
      if (this.isAmountNull(value)) {
        return;
      }
      const fixedValue = accounting.toFixed(value, this.precision)
      const output = this.outputType.toLowerCase() === 'string' ? fixedValue : Number(fixedValue)
      this.$emit('input', output)
    },
    /**
     * Format value using symbol and separator.
     * @param {Number} value
     * @return {String}
     */
    format(value) {
      if (this.isAmountNull(value)) {
        return null;
      }
      return accounting.formatMoney(value, {
        symbol: this.currency,
        format: this.symbolPosition,
        precision: Number(this.precision),
        decimal: this.decimalSeparatorSymbol,
        thousand: this.thousandSeparatorSymbol
      })
    },
    /**
     * Remove symbol and separator.
     * @param {Number} value
     * @return {Number}
     */
    unformat(value) {
      if (this.isAmountNull(value)) {
        return;
      }
      const toUnformat = typeof value === 'string' && value === '' ? this.emptyValue : value
      return accounting.unformat(toUnformat, this.decimalSeparatorSymbol)
    }
  }
}
</script>
