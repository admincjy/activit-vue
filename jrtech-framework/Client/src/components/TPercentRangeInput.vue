<template>
<div style="width:100%">
  <table width="100%">
    <tr>
      <td>
        <t-percent-input v-model="currentFromValue"  :disabled="disabled">
        </t-percent-input>
      </td>
      <td width="10">~</td>
      <td>
        <t-percent-input v-model="currentToValue"  :disabled="disabled">
        </t-percent-input>
      </td>
    </tr>
  </table>
</div>
</template>

<script>

export default {
  components: {},
  props: {
    fromValue: null,
    toValue: null,
    disabled: false,
    value: null,
  },
  data() {
    return {
      selectFromValue: null,
      selectToValue: null,
    }
  },
  watch: {
    fromValue(value) {
      if (value) {
        this.currentFromValue = value;
      } else {
        this.currentFromValue = null;
      }
    },
    toValue(value) {
      if (value) {
        this.currentToValue = value;
      } else {
        this.currentToValue = null;
      }
    }
  },
  created() {
    if (this.fromValue) {
      this.currentFromValue = this.fromValue;
    } else {
      this.currentFromValue = null;
    }
    if (this.toValue) {
      this.currentToValue = this.toValue;
    } else {
      this.currentToValue = null;
    }
  },
  mounted() {},
  computed: {
    currentFromValue: {
      // 动态计算currentValue的值
      get: function() {
        return this.selectFromValue;
      },
      set: function(val) {
        this.selectFromValue = val;
        this.$emit('fromValue-change', val);
        this.$emit('input', [this.selectFromValue, this.selectToValue]);
        this.$emit('change', [this.selectFromValue, this.selectToValue]);
      }
    },
    currentToValue: {
      // 动态计算currentValue的值
      get: function() {
        return this.selectToValue;
      },
      set: function(val) {
        this.selectToValue = val;
        this.$emit('toValue-change', val);
        this.$emit('input', [this.selectFromValue, this.selectToValue]);
        this.$emit('change', [this.selectFromValue, this.selectToValue]);
      }
    }
  },
  mounted() {},
  methods: {
  },
}
</script>
