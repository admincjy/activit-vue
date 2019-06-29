
<template>
<div>
  <el-row :gutter="20">
    <el-col :span="9">
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>字典类别</span>

        </div>
        <div class="text item">
          <t-tree ref="categoryTree" :options="categoryTreeOptons" @node-click="handleNodeClick" @node-drop="handleDrop">
          </t-tree>
        </div>
      </el-card>
    </el-col>
    <el-col :span="15" v-if="showGrid">
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>{{selectedCategoryItemName}}-项</span>
          <div style="float: right; padding: 3px 0">
            <el-button type="primary" icon="el-icon-delete" @click="doNew()" :disabled="selectedCategoryItem ==null">新增</el-button>
            <el-button type="danger" icon="el-icon-delete" @click="doBatchDelete()" :disabled="selectedItems.length <= 0">批量删除</el-button>
            <el-button icon="el-icon-download" @click="doExportExcel()">导出</el-button>
          </div>
        </div>
        <div class="text item">
          <t-grid ref="searchReulstList" :options="gridOptions" @selection-change="handleSelectionChange">
          </t-grid>
        </div>
      </el-card>
    </el-col>
  </el-row>
  <!-- 弹窗, 新增 / 修改 -->
  <edit-form v-if="editFormVisible" ref="editForm" @change="doSearch"></edit-form>
</div>
</template>
<script>
import EditForm from './edit'

export default {
  data() {
    return {
      editFormVisible: false,
      selectedItems: [],
      selectedCategoryItem: null,
      showGrid:false,
      categoryTreeOptons: {
        dataSource: {
          serviceInstance: tapp.services.base_DataDictionary.getTreeDataDictionaryCategories,
          serviceInstanceInputParameters: {}
        },
        tree: {
          draggable: true,
          showCheckbox: false,
          defaultCheckedKeys: []
        }
      },
      gridOptions: {
        dataSource: {
          loadDataOnFirst: false,
          serviceInstance: tapp.services.base_DataDictionary.getDataDictionaries,
          serviceInstanceInputParameters: {
            categoryId: null,
          }
        },
        grid: {
          pageable: false,
          operates: {
            width: 120,
            fixed: 'left',
            list: [{
              type: 'text',
              show: true,
              label: '查看',
              method: this.doEdit,
            }, ]
          }, // 列操作按钮
          columns: [{
              prop: 'id',
              label: '编码',
              sortable: true,
              width: 150
            }, {
              prop: 'name',
              label: '名称',
              sortable: true,
              width: 150
            },
            {
              prop: 'sortIndex',
              label: '排序号',
              sortable: true,
              width: 100
            },
            {
              prop: 'remark',
              label: '备注',
              sortable: false,
            },

          ], // 需要展示的列
          defaultSort: {
            prop: 'sortIndex',
            order: 'ascending'
          },
        }
      }
    }
  },
  components: {
    EditForm,
  },
  created() {

  },
  computed: {
    selectedCategoryItemName() {
      if (this.selectedCategoryItem == null ||
        this.selectedCategoryItem.name == null) {
        return '未选择字典类别'
      }
      return this.selectedCategoryItem.name;
    },
  },
  methods: {
    doNew() {
      if (this.selectedCategoryItem == null) {
        this.$notify.info({
          title: '系统提示',
          message: '未选择字典类别！'
        });
        return;
      }
      this.editFormVisible = true
      this.$nextTick(() => {
        this.$refs.editForm.init(this.selectedCategoryItem.id, null)
      })

    },
    doEdit(key, row) {
      this.editFormVisible = true
      this.$nextTick(() => {
        this.$refs.editForm.init(this.selectedCategoryItem.id, row.id)
      })

    },
    handleNodeClick(dataItem, node, el) {
      if (dataItem.hasChildren) {
        this.showGrid = false;
        this.$refs.categoryTree.expandOrNot(dataItem);
      } else {
        this.showGrid = true;
        this.selectedCategoryItem = dataItem;
        this.gridOptions.dataSource.serviceInstanceInputParameters.categoryId = dataItem.id;
        this.$refs.searchReulstList.refresh();
      }
    },
    handleDrop(draggingNode, dropNode, dropType, ev) {
      let self = this;
      tapp.services.base_DataDictionary.updateCategoryParentIdAndSortIndex({
        'id': draggingNode.data.id,
        'newParentId': dropType == 'inner' ? dropNode.data.id : dropNode.data.parentId,
        'newSortIndex': dropType == 'inner' ? 0 : (dropNode.data.self.sortIndex || 0) + (dropType == 'after' ? 1 : -1),
      }).then(function(result) {
        self.$notify.success({
          title: '字典类别变更父节点成功，注销后重新登陆系统后修改生效',
          message: '系统提示'
        });

      })
    },
    handleSelectionChange(val) {
      this.selectedItems = val;
    },
    doSearch() {
      this.$refs.searchReulstList.refresh();
    },
    doBatchDelete() {
      let self = this;
      if (!self.selectedItems || self.selectedItems.length == 0) {
        self.$notify.info({
          title: '系统提示',
          message: '您没选择任何行，无法操作！'
        });
        return;
      }
      let ids = self.selectedItems.map(function(row) {
        return row.id;
      });
      self.$confirm('确认要删除共' + ids.length + '项吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        tapp.services.base_DataDictionary.batchDelete(ids).then(function(result) {
          self.$notify.success({
            title: '系统删除成功',
            message: '系统删除成功，注销后重新登陆系统后修改生效！'
          });
          self.$refs.searchReulstList.refresh();
        })
      });
    },
    doExportExcel() {
      this.$refs.searchReulstList.exportCSV('字典项列表');
    },
  }
}
</script>
<style >
.el-card__header {

  height: 82px;
}
</style>
