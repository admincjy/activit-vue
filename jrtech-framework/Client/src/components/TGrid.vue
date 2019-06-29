<template>
<div>
  <t-table :options="gridOptions" :empty-text="emptyText" :data="data" :total="total" :current-page-index="currentPageIndex" @size-change="handleSizeChange" @current-change="handleIndexChange" @selection-change="handleSelectionChange" @sort-change="handleSortChange"
    @filter-change="handleFilterChange" :disabled="disabled">
    <template slot="columnDataHeader" v-if="gridOptions.customColumnDataHeader">
    <slot name="columnDataHeader">
    </slot>
  </template>
  </t-table>
</div>
</template>
<script>
import TTable from './TTable.vue'
import excelExport from '@/util/excelExport.js'

const exportCSVMaxRowsNum = 2147483647;

const defaultGridOptions = {
  pageable: true,
  reduceMethod: null,
  mutiSelect: true, // 是否支持列表项选中功能
  showOverflowTooltip: true,
  customColumnDataHeader: false,
  operates: {
    type: Object,
    default: {} // width:按钮列宽，fixed：是否固定（left,right）,按钮集合 === label: 文本，type :类型（primary / success / warning / danger / info / text），show：是否显示，icon：按钮图标，plain：是否朴素按钮，disabled：是否禁用，method：回调方法
  },
  pagination: {
    pageSize: parseInt(tapp.data.base_setting.find(p => p.id === 'Paged_List_Per_Page_Size').value),
    pageSizes: [20, 50, 100],
  },
  treeable: false,
  tree: {
    treeKey: 'id',
    parentKey: 'parentId',
    levelKey: 'level',
    childKey: 'items',
  },
  treeColumn: {
    label: '名称',
    prop: 'name',
    width: '150px',
    minWidth: '150px',
    treeKey: 'id',
    parentKey: 'parentId',
    levelKey: 'level',
    childKey: 'items',
  }
};
const defaultDataSourceOptions = {
  loadDataOnFirst: true,
  serviceInstance: null,
  serviceInstanceInputParameters: null
};

export default {
  components: {
    TTable
  },
  props: {
    disabled: {
      type: Boolean,
      default: false,
      required: false
    },
    options: {
      dataSource: {},
      grid: {}
    } // table 表格的控制参数
  },
  data() {
    return {
      isFirstEnter: false, //是否首次加载
      keepAlive: this.$router.currentRoute.meta.keepAlive,
      gridOptions: Object.assign({}, defaultGridOptions, this.options.grid || {}),
      dataSourceOptions: Array.isArray(this.options.dataSource) ? this.options.dataSource : Object.assign({}, defaultDataSourceOptions, this.options.dataSource || {}),
      emptyText: '...数据待加载',
      data: [],
      total: 0,
      currentPageIndex: 1,
      pageSize: parseInt(tapp.data.base_setting.find(p => p.id === 'Paged_List_Per_Page_Size').value),
      reduces: null,
      sortField: '',
      sortDirection: '',
      filters: {},
    }
  },
  computed: {},
  created() {
    this.isFirstEnter = true;
    this.sortField = this.gridOptions.defaultSort.prop;
    this.sortDirection = this.gridOptions.defaultSort.order;
    if (this.resolveSummaryMethod != null) {
      this.resolveSummaryMethod(this.gridOptions);
    }
    this.gridOptions.sortable = this.dataSourceOptions.serviceInstance == null ? true : 'custom';
    if (Array.isArray(this.dataSourceOptions) ||
      this.dataSourceOptions.loadDataOnFirst === true) {
      this.bindGrid();
    } else {
      this.emptyText = "暂无数据";
    }

  },
  activated() {
    if (this.isFirstEnter == true) {
      this.isFirstEnter = false;
    } else {
      this.bindGrid();
    }
  },
  mounted() {
    if (this.isFirstEnter && !this.keepAlive) {
      this.isFirstEnter = false;
    }
  },
  watch: {
    'options.grid': {　　　　
      handler(newValue, oldValue) {　
        this.gridOptions = newValue;
        this.resolveSummaryMethod(newValue);
      },
      deep: true　　
    },
    'options.dataSource': {　　　　
      handler(newValue, oldValue) {　
        this.dataSourceOptions = newValue;　　
        if (Array.isArray(this.dataSourceOptions)) {
          this.bindGrid();　　
        }
      },
      deep: true　　
    }
  },

  methods: {
    resolveSummaryMethod(options) {
      let reduceMethod = options.reduceMethod;
      if (reduceMethod != null) {
        this.gridOptions.showSummary = true;
        this.gridOptions.summaryMethod = this.getSummaryMethod;
      }
    },
    getSummaryMethod(params) {
      params.reduces = this.reduces;
      return this.gridOptions.reduceMethod(params);
    },
    async bindGrid() {
      this.emptyText = "...数据待加载";
      let isPageable = this.gridOptions.pageable;
      let self = this;

      let skip = null;
      let take = null;
      if (this.gridOptions && this.gridOptions.pagination) {
        skip = this.currentPageIndex;
        take = this.pageSize;
      }
      let result = await this.getDataPromise(isPageable, skip, take);

      self.data = result.data;
      self.reduces = result.reduces;
      self.total = result.totalCount;

      if (result.data == null || result.data.length == 0) {
        this.emptyText = "暂无数据";
      }
      this.$emit('data-change', {
        data: self.data,
        totalCount: result.totalCount || result.data.length,
        reduces: result.reduces,
      });
    },
    getDataPromise(isPageable, skip, take) {
      let self = this;
      if (Array.isArray(self.dataSourceOptions)) {
        return new Promise(function(resolve, reject) {
          resolve({
            data: self.dataSourceOptions,
            totalCount: self.dataSourceOptions == null ? 0 : self.dataSourceOptions.length,
            reduces: null,
          });
        });
      }
      if (self.dataSourceOptions == null || self.dataSourceOptions.serviceInstance == null) {
        return new Promise(function(resolve, reject) {
          resolve({
            data: [],
            totalCount: 0,
            reduces: null,
          });
        });
      }
      let mDatasourceService = self.dataSourceOptions.serviceInstance;
      let mDatasourceRequestParameters = self.dataSourceOptions.serviceInstanceInputParameters;

      var iRequestData = {};
      if (isPageable) {
        iRequestData.skipCount = skip;
        iRequestData.maxResultCount = take;
      }
      if (self.sortField && self.sortDirection) {
        iRequestData.sorting = self.sortField + ' ' + self.sortDirection;
      }
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

          if (self.filters) {
            iRequestData.filters = self.filters;
          }
        }
      }

      return new Promise(function(resolve, reject) {
        return mDatasourceService(iRequestData).then(function(result) {
          if (isPageable) {
            resolve({
              data: self.gridOptions.treeable ? self.$util.treeToArray(result.items) : result.items,
              totalCount: result.totalCount,
              reduces: result.reduces,
            });
          } else {
            resolve({
              data: self.gridOptions.treeable ? self.$util.treeToArray(result) : result,
              totalCount: result.length,
              reduces: null,
            });
          }
        });
      });
    },
    async exportCSV(filename) {
      let columns = [];
      if (this.gridOptions.treeable) {
        columns.push({ ...this.gridOptions.treeColumn
        });
      }
      columns = [...columns, ...this.gridOptions.columns];
      let isPageable = this.gridOptions.pageable;
      let self = this;
      let result = await this.getDataPromise(this.gridOptions.pageable, 0, exportCSVMaxRowsNum);
      excelExport(result.data, columns, filename);
    },
    refresh() {
      this.$nextTick(() => {
        this.currentPageIndex = 1;
        this.bindGrid();
      })
    },
    clear() {
      this.$nextTick(() => {
        if (this.gridOptions.pagination) {
          this.currentPageIndex = 1;
          this.total = 0;
        }
        this.data = [];
      })
    },
    handleSelectionChange(val) {
      this.$emit('selection-change', val);
    },
    // 切换每页显示的数量
    handleSizeChange(size) {
      this.currentPageIndex = 1;
      this.pageSize = size;
      this.bindGrid();
    },
    // 切换页码
    handleIndexChange(skip) {
      this.currentPageIndex = skip;
      this.bindGrid();
    },
    handleSortChange(sort) {
      let {
        column,
        prop,
        order
      } = sort;

      this.sortField = prop;
      this.sortDirection = order === 'descending' ? 'desc' : 'asc';
      this.bindGrid();
    },
    handleFilterChange(filters) {
      this.filters = filters;
      this.bindGrid();
    },
  },
}
</script>
