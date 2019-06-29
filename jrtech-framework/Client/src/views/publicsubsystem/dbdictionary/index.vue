<template>
<div>
  <el-row :gutter="20">
    <el-col :span="8">
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>表名</span>
        </div>
        <div class="text item">
          <t-tree ref="categoryTree" :options="categoryTreeOptons" @node-click="handleNodeClick">
          </t-tree>
        </div>
      </el-card>
    </el-col>
    <el-col :span="16">
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>{{selectedCategoryItemName}}-列</span>
          <div style="float: right; padding: 3px 0">
            <el-button icon="el-icon-download" @click="doExportExcel()">导出</el-button>
          </div>
        </div>
        <div class="text item">
          <t-grid ref="searchReulstList" :options="gridOptions">
          </t-grid>
        </div>
      </el-card>
    </el-col>
  </el-row>
</div>
</template>
<script>
export default {
  data() {
    return {
      selectedCategoryItem: null,
      data: [],
      categoryTreeOptons: {
        tree: {
          showCheckbox: false,
          defaultCheckedKeys: []
        },
        dataSource: [],
      },
      gridOptions: {
        dataSource: [],
        grid: {
          data: [],
          pageable: false,
          columns: [{
              prop: 'columnName',
              label: '列名',
              sortable: true,
              width: 200
            },
            {
              prop: 'columnType',
              label: '数据类型',
              sortable: true,
              width: 120
            },
            {
              prop: 'isNullable',
              label: '允许非空',
              sortable: false,
              width: 60
            },
            {
              prop: 'columnKey',
              label: '主键约束',
              sortable: false,
              width: 60
            },
            {
              prop: 'columnComment',
              label: '备注',
              sortable: false,
            },
          ], // 需要展示的列
          defaultSort: {
            prop: 'ordinalPosition',
            order: 'ascending'
          },
        }
      }
    }
  },
  components: {},
  created() {
    let self = this;
    tapp.services.base_DBDictionary.queryDBDictionary().then(function(result) {
      self.data = result;
      let categoryTreeData = result.map(p => {
        return {
          id: p.tableName,
          name: p.tableComment + '(' + p.tableName + ')',
          parentId: null,
          level: 1,
          items: [],
        }
      });
      self.categoryTreeOptons.dataSource = categoryTreeData;
    });
  },
  computed: {
    selectedCategoryItemName() {
      if (this.selectedCategoryItem == null ||
        this.selectedCategoryItem.name == null) {
        return '未选择表名'
      }
      return this.selectedCategoryItem.name;
    },
  },
  methods: {
    handleNodeClick(dataItem, node, el) {
      this.selectedCategoryItem = dataItem;
      let selectedCategoryData = this.data.find(p => p.tableName === dataItem.id);
      let gridData = selectedCategoryData.columns;
      this.gridOptions.dataSource = gridData;



    },
    doSearch() {
      this.$refs.searchReulstList.refresh();
    },
    doExportExcel() {
      this.$refs.searchReulstList.exportCSV(this.selectedCategoryItemName + '-列');
    },
  }
}
</script>
<style >
.el-card__header {

  height: 82px;
}
</style>
