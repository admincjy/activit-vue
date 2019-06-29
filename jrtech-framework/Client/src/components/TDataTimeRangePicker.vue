<template>
<div style="width:100%">
  <table width="100%">
    <tr>
      <td style="width:80px;">
        <el-select v-model="currentRangeTypeValue" @change="handleRangeOptionChange">
          <el-option v-for="(item, index) in rangeOptions" :key='item.id' :label="item.name" :value="item.id"></el-option>
        </el-select>
      </td>
      <td>
        <t-datetime-picker v-model="currentFromValue" type="date" :disabled="disabled">
        </t-datetime-picker>
      </td>
      <td width="10">~</td>
      <td>
        <t-datetime-picker v-model="currentToValue" type="date" :disabled="disabled">
        </t-datetime-picker>
      </td>
    </tr>
  </table>
</div>
</template>

<script>
import moment from 'moment';

export default {
  components: {},
  props: {
    rangeType: {
      type: String,
      default: "5",
      required: false
    },
    fromValue: null,
    toValue: null,
    disabled: false,
    value: null,
  },
  data() {
    return {
      rangeOptions: [{
        "id": "0",
        "name": "今天"
      }, {
        "id": "1",
        "name": "昨天"
      }, {
        "id": "2",
        "name": "本周"
      }, {
        "id": "3",
        "name": "本月"
      }, {
        "id": "4",
        "name": "本年"
      }, {
        "id": "5",
        "name": "不限"
      }],
      selectRangeType: null,
      selectFromValue: null,
      selectToValue: null,
    }
  },
  watch: {
    rangeType(value) {
      if (value) {
        this.currentRangeTypeValue = value;
      } else {
        this.currentRangeTypeValue = null;
      }
    },
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

    if (this.rangeType) {
      this.currentRangeTypeValue = this.rangeType;
    } else {
      this.currentRangeTypeValue = null;
    }
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
    this.handleRangeOptionChange();
  },
  mounted() {},
  computed: {

    currentRangeTypeValue: {
      // 动态计算currentValue的值
      get: function() {
        return this.selectRangeType;
      },
      set: function(val) {
        this.selectRangeType = val;
        this.$emit('rangetype-change', val);
      }
    },
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
    handleRangeOptionChange() {

      if (!this.currentRangeTypeValue) {
        return;
      }
      let val = this.currentRangeTypeValue;

      const date = new Date();
      let _fromValue = null;
      let _toValue = null;

      if (val == '0') {
        _fromValue = this.$util.datetimeFormat(moment().startOf('day'));
        _toValue = this.$util.datetimeFormat(moment().endOf('day').toDate());
      } else if (val == '1') {
        _fromValue = this.$util.datetimeFormat(moment().startOf('day').subtract(1, 'days'));
        _toValue = this.$util.datetimeFormat(moment().startOf('day').subtract(1, 'days'));
      } else if (val == '2') {
        _fromValue = this.$util.datetimeFormat(moment().startOf('isoWeek'));
        _toValue = this.$util.datetimeFormat(moment().endOf('isoWeek'));
      } else if (val == '3') {
        _fromValue = this.$util.datetimeFormat(moment().startOf('month'));
        _toValue = this.$util.datetimeFormat(moment().endOf('month'));
      } else if (val == '4') {
        _fromValue = this.$util.datetimeFormat(moment().startOf('year'));
        _toValue = this.$util.datetimeFormat(moment().endOf('year'));
      } else if (val == '5') {
        _fromValue = null;
        _toValue = null;
      }
      this.currentFromValue = _fromValue;
      this.currentToValue = _toValue;
    }
  },
}
</script>
