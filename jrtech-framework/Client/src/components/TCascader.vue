<template>
<el-cascader ref="cascader" :placeholder="placeholder" :options="data" v-model="currentValue" v-bind="cascaderOptions" filterable clearable change-on-select @change="handleChange" :disabled="disabled"></el-cascader>
</template>
<script>
const defaultCascaderOptions = {
  props: {
    value: 'id',
    label: 'name',
    children: 'items',
  },
  data: null,
};

const defaultDataSourceOptions = {
  loadDataOnFirst: true,
  serviceInstance: null,
  serviceInstanceInputParameters: null
};

export default {
  props: {
    placeholder: '请选择',
    value: null,
    disabled: {
      type: Boolean,
      default: false,
      required: false
    },
    options: {
      type: Object,
      default: function() {
        return {
          dataSource: {},
          cascader: {}
        };
      }
    }
  },
  data() {
    return {
      data: [],
      selectValue: null,
      cascaderOptions: Object.assign({}, defaultCascaderOptions, this.options.cascader || {}),
      dataSourceOptions: Object.assign({}, defaultDataSourceOptions, this.options.dataSource || {}),
    }
  },
  watch: {
    'options.cascader': {　　　　
      handler(newValue, oldValue) {　　　
        this.cascaderOptions = Object.assign({}, this.cascaderOptions || {}, newValue);　　　　
      },
      deep: true,
      immediate: true,
    },
    'options.dataSource': {　　　　
      handler(newValue, oldValue) {
        if (!newValue) {
          return;
        }　
        if (Array.isArray(newValue)) {
          this.dataSourceOptions = newValue;　　　
        } else {　　　　　
          this.dataSourceOptions = Object.assign({}, newValue, this.options.dataSource || {});
        }　　
        this.bindCascader();　　
      },
      deep: true,
      immediate: true,
    },
    value(value) {
      this.currentValue = value;
    }
  },
  computed: {
    currentValue: {
      // 动态计算currentValue的值
      get: function() {
        let ival = this.arrayData.find(p => {
          return p.id == this.selectValue;
        })
        let idPath = ival && ival.idPath;
        return idPath;
      },
      set: function(val) {
        let ival = null;
        if (!val) {
          ival = null;
        } else {
          ival = Array.isArray(val) ? val[val.length - 1] : val;
        }
        this.selectValue = ival;
        this.$emit('ElFormItem', 'el.form.change', [ival]);
        this.$emit('input', ival);
      }
    },
    arrayData: {
      get: function() {
        return this.$util.treeToArray(this.data);
      },
    },
  },
  created() {
    this.currentValue = this.value;
    if (this.dataSourceOptions.loadDataOnFirst === true) {
      this.bindCascader();
    }
  },
  activated() {
    this.bindCascader();
  },
  mounted() {},
  methods: {
    bindCascader() {
      let self = this;
      this.getDataPromise().then(function(result) {
        let data = result.data;
        //Cascader的BUG。如果items长度为0，会显示一个空的面板。
        self.$util.retriveTree(data, p => {
          if (p.items.length == 0) delete p.items;
        })
        self.data = data;
      });
    },
    getDataPromise() {
      let self = this;

      if (Array.isArray(self.dataSourceOptions)) {
        return new Promise(function(resolve, reject) {
          resolve({
            data: self.dataSourceOptions || [],
          });
        });
      }
      let mDatasourceService = self.dataSourceOptions.serviceInstance;
      if (mDatasourceService == null) {
        return new Promise(function(resolve, reject) {
          resolve({
            data: [],
          });
        });
      }
      let mDatasourceRequestParameters = this.dataSourceOptions.serviceInstanceInputParameters;

      var iRequestData = {};
      if (mDatasourceRequestParameters != null) {
        if (typeof(mDatasourceRequestParameters) != "object" ||
          Array.isArray(mDatasourceRequestParameters)) {
          iRequestData = mDatasourceRequestParameters;
        } else {
          var parameters = {};
          if (mDatasourceRequestParameters) {
            for (var i in mDatasourceRequestParameters) {
              if (mDatasourceRequestParameters.hasOwnProperty(i)) { //filter,只输出man的私有属性
                var source = mDatasourceRequestParameters[i];
                if (typeof source == 'function') {
                  parameters[i] = mDatasourceRequestParameters[i]();
                } else {
                  parameters[i] = mDatasourceRequestParameters[i];
                }
              };
            }
          }

          iRequestData = { ...iRequestData,
            ...parameters
          };
        }
      }
      return new Promise(function(resolve, reject) {
        return mDatasourceService(iRequestData).then(function(result) {
          resolve({
            data: result
          });
        });
      });
    },
    handleChange(val) {
      if (!val) {
        this.$emit('change', null);
      } else {
        let lastVal = val[val.length - 1];
        let lastItem = this.arrayData.find(p=>p.id === lastVal); 
        this.$emit('change', lastItem);
      }
    }
  },
}
</script>
