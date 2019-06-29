<template>
<div>
  <el-input placeholder="输入关键字进行过滤" v-model="filterText" style="width:250px;">
  </el-input>
  <el-tree ref="tree" :data="data" v-bind="treeOptions" :filterNodeMethod="filterNode" @node-click="handleNodeClick" style="margin-top: 20px;" @check-change="handleCheckChangeClick" @check="handleCheckeClick" @node-drop="handleDrop">
  </el-tree>
</div>
</template>
<script>
const defaultTreeOptions = {
  props: {
    children: 'items',
    label: 'name',
    isLeaf: 'hasChildren'
  },
  showCheckbox: true,
  highlightCurrent: true,
  defaultExpandAll: true,
  expandOnClickNode: false,
  nodeKey: 'id',
  data: null,
};

const defaultDataSourceOptions = {
  loadDataOnFirst: true,
  serviceInstance: null,
  serviceInstanceInputParameters: null
};

export default {
  props: {
    options: {
      type: Object,
      default: function() {
        return {
          dataSource: {},
          tree: {}
        };
      }
    }
  },
  data() {
    return {
      isFirstEnter: false, //是否首次加载
      keepAlive: this.$router.currentRoute.meta.keepAlive,
      data: [],
      filterText: '',
      treeOptions: Object.assign({}, defaultTreeOptions, this.options.tree || {}),
      dataSourceOptions: Object.assign({}, defaultDataSourceOptions, this.options.dataSource || {}),
    }
  },
  watch: {
    'options.tree': {　　　　
      handler(newValue, oldValue) {　　　
        this.treeOptions = Object.assign({}, this.treeOptions || {}, newValue);　　　　
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
        this.bindTree();　　
      },
      deep: true,
    },
    filterText(val) {
      this.$refs.tree.filter(val);
    }
  },
  created() {
    this.isFirstEnter = true;
    if (this.dataSourceOptions.loadDataOnFirst === true) {
      this.bindTree();
    }
  },
  activated() {
    if (this.isFirstEnter == true) {
      this.isFirstEnter = false;
    } else {
      this.bindTree();
    }
  },
  mounted() {
    if (this.isFirstEnter && !this.keepAlive) {
      this.isFirstEnter = false;
    }
  },
  methods: {
    bindTree() {
      let self = this;
      this.getDataPromise().then(function(result) {
        self.data = result.data;
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
    getCheckedKeys() {
      return this.$refs.tree.getCheckedKeys();
    },
    setCheckedNodes(nodes) {
      this.$refs.tree.setCheckedNodes(nodes);
    },
    setCheckedKeys(keys) {
      this.$refs.tree.setCheckedKeys(keys);
    },
    resetChecked() {
      this.$refs.tree.setCheckedKeys([]);
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.name.toLowerCase().indexOf(value.toLowerCase()) !== -1;
    },
    refresh() {
      this.bindTree();
    },
    refreshNode(key, data) {
      let node = this.$refs.tree.getNode(key);
      node.setData(data);
    },
    checkParentDeep(data) {
      let checkedKeys = this.getCheckedKeys();
      let thisChecked = checkedKeys.includes(data.id);
      if (thisChecked) {
        let idPath = data.idPath;
        let newCheckedKeys = new Set([...checkedKeys, ...idPath]);
        this.setCheckedKeys(newCheckedKeys);
      }
    },
    checkChildrenDeep(data, containSelf) {
      let checkedKeys = this.getCheckedKeys();
      let thisChecked = !checkedKeys.includes(data.id);
      let deepChildrenKeys = [];
      this.$util.retriveTree(data.items, p => {
        deepChildrenKeys.push(p.id);
      });
      if (containSelf) {
        deepChildrenKeys.push(data.id);
      }
      if (thisChecked) {
        let newCheckedKeys = new Set([...checkedKeys, ...deepChildrenKeys]);
        this.setCheckedKeys(newCheckedKeys);
      } else {
        let newCheckedKeys = checkedKeys.filter(t => !deepChildrenKeys.includes(t));
        this.setCheckedKeys(newCheckedKeys);
      }
    },
    expandOrNot(data){
      this.$refs.tree.store.nodesMap[data.id].expanded = !!!this.$refs.tree.store.nodesMap[data.id].expanded;
    },
    handleNodeClick(item, node, el) {
      this.$emit('node-click', item, node, el);
    },
    handleCheckChangeClick(item, checked, node) {
      this.$emit('check-change', item, checked, node);
    },
    handleCheckeClick(item, node) {
      this.$emit('check', item, node);
    },
    handleDrop(draggingNode, dropNode, dropType, ev) {
      this.$emit('node-drop', draggingNode, dropNode, dropType, ev);
    },
  },
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
