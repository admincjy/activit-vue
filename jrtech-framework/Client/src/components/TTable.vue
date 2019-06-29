<template>
<div class="table">
  <el-table ref="mutipleTable" v-bind="tableOptions" :empty-text="emptyText" :data="data" size="medium" @selection-change="handleSelectionChange" @sort-change="handleSortChange" @filter-change="handleFilterChange" header-cell-class-name="tableHeader">
    <!--region 选择框-->
    <el-table-column v-if="mutiSelect" type="selection" style="width: 55px;">
    </el-table-column>
    <el-table-column type="index" label="行号" width="50">
    </el-table-column>
    <table-tree-column v-if="treeable" style="width: 40px;" v-bind="treeColumnOptions">
    </table-tree-column>
    <!--endregion-->
    <!--region 按钮操作组-->
    <el-table-column v-if="operates.list!=null&&operates.list.length>0" ref="fixedColumn" label="操作" align="center" :width="operates.width" :fixed="operates.fixed">
      <template slot-scope="scope">
        <template v-for="(btn, key) in operates.list">
         <el-button :type="btn.type" size="small" :icon="btn.icon" :disabled="btn.disabled" :style="btn.style"
             @click.native.prevent="btn.method(key,scope.row)">
             {{ btn.render==null?btn.label:btn.render(key,scope.row) }}
         </el-button>
        </template>
      </template>
    </el-table-column>
    <!--endregion-->

    <!--region 数据列-->
    <slot name="columnDataHeader">
      <template v-for="(column, index) in availColumns">
          <el-table-column v-bind="column">
            <template slot-scope="scope">
              <template v-if="!column.render">
                <template v-if="column.formatter">
                  <span v-html="column.formatter(scope.row, column)"></span>
                </template>
      <template v-else>
                  <span>{{scope.row[column.prop]}}</span>
                </template>
      </template>
      <template v-else>
                <expand-dom :column="column" :row="scope.row" :render="column.render" :index="index"></expand-dom>
              </template>
      </template>
      </el-table-column>
      </template>
    </slot>
    <!--endregion-->

  </el-table>

  <!--region 分页-->
  <el-pagination layout="total,sizes, prev, pager, next,jumper" v-if="pageable" v-bind="paginationPropsOptions" :total="total" :current-page="currentPageIndex" @size-change="handleSizeChange" @current-change="handleIndexChange"></el-pagination>
  <!--endregion-->
</div>
</template>
<!--endregion-->
<script>
import TableTreeColumn from '@/components/table-tree-column'

const defaultTableOptins = {
  stripe: false,
  border: false,
  highlightCurrentRow: true,
}
const defaultOperatorsPropsOptions = {
  width: 200,
  fixed: 'right',
  list: null,
}


export default {
  props: {
    disabled: {
      type: Boolean,
      default: false,
      required: false
    },
    options: {
      type: Object,
      default: function() {
        return {
          table: {},
          operates: {
            type: Object,
            default: {

            } // width:按钮列宽，fixed：是否固定（left,right）,按钮集合 === label: 文本，type :类型（primary / success / warning / danger / info / text），show：是否显示，icon：按钮图标，disabled：是否禁用，method：回调方法
          },
          pagination: {
            type: Object,
            default: {

            } // 分页参数 === pageSize:每页展示的条数，pageIndex:当前页，pageArray: 每页展示条数的控制集合，默认 _page_array
          },
        };
      } // table 表格的控制参数
    },
    emptyText: {
      type: String,
      default: function() {
        return '...数据待加载'
      }
    },
    data: {
      type: Array,
      default: function() {
        return []
      }
    },
    total: {
      type: Number,
      default: function() {
        return 0
      }
    },
    currentPageIndex: {
      type: Number,
      default: function() {
        return 1
      }
    }
  },
  components: {
    TableTreeColumn,
    expandDom: {
      functional: true,
      props: {
        row: Object,
        render: Function,
        index: Number,
        column: {
          type: Object,
          default: null
        }
      },
      render: (h, ctx) => {
        const params = {
          row: ctx.props.row,
          index: ctx.props.index
        }
        if (ctx.props.column) params.column = ctx.props.column
        return ctx.props.render(h, params)
      }
    }
  },
  data() {
    return {
      tableOptions: {},
      operates: {},
      mutiSelect: false,
      pageable: false,
      paginationPropsOptions: {},
      treeable: false,
      treeColumnOptions: {},
    }
  },
  created() {
    this.init(this.options);
  },
  computed: {
    availColumns: {
      // 动态计算currentValue的值
      get: function() {
        if (this.tableOptions.columns == null) {
          return null;
        }
        let aColumns = this.tableOptions.columns.filter((item) => {
          return item.visible == null || item.visible === true;
        });
        return aColumns;
      },
    }
  },
  watch: {
    options: {
      handler(newValue, oldValue) {
        this.init(newValue);
      },
      deep: true　　
    },
  },
  mounted() {

  },
  methods: {
    init(options) {
      if (!options) {
        return;
      }
      let {
        pagination,
        pageable,
        treeable,
        treeColumn,
        mutiSelect,
        operates,
        ...tableOptions
      } = options;

      this.operates = { ...defaultOperatorsPropsOptions,
        ...operates
      };
      this.paginationPropsOptions = {
        ...pagination
      };

      this.pageable = pageable;
      this.mutiSelect = mutiSelect && !!!this.disabled;
      this.treeable = treeable,
        this.treeColumnOptions = {
          ...treeColumn
        };　　
      this.tableOptions = {
        ...defaultTableOptins,
        ...tableOptions
      };　　　　
    },
    handleFilterChange(filters) {
      this.$emit('filter-change', filters);
    },
    handleSelectionChange(val) {
      this.$emit('selection-change', val);
    },
    handleSortChange(sort) {
      this.$emit('sort-change', sort);
    },
    // 切换每页显示的数量
    handleSizeChange(size) {
      if (this.pageable) {
        this.$emit('size-change', size);
      }
    },

    // 切换页码
    handleIndexChange(currnet) {
      if (this.pageable) {
        this.$emit('current-change', currnet);
      }
    },
  }

}
</script>
