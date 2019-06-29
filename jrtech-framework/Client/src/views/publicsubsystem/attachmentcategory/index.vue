
<template>
<div>
  <el-row :gutter="20">
    <el-col :span="9">
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>附件分类</span>
        </div>
        <div class="text item">
          <t-tree ref="categoryTree" :options="categoryTreeOptons" @node-click="handleNodeClick">
          </t-tree>
        </div>
      </el-card>
    </el-col>
    <el-col :span="15">
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>{{selectedCategoryItemName}}-所属附件类别</span>
          <div style="float: right; padding: 3px 0">
            <el-button icon="el-icon-plus" type="primary" @click="doNew()" :disabled="selectedCategoryItem ==null">新增</el-button>
            <el-button icon="el-icon-search" @click="doExistsSelect()" :disabled="selectedCategoryItem ==null">选择</el-button>
            <el-button icon="el-icon-delete" type="danger" @click="doBatchDelete()" :disabled="selectedItems.length <= 0">批量删除</el-button>
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
  <exist-category-select-form v-if="existCategorySelectVisible" ref="existCategorySelectForm" @change="doSearch"></exist-category-select-form>
</div>
</template>
<script>
import EditForm from './edit'
import ExistCategorySelectForm from './existCategorySelect'


export default {
  data() {
    return {
      editFormVisible: false,
      existCategorySelectVisible: false,
      selectedItems: [],
      selectedCategoryItem: null,
      categoryTreeOptons: {
        dataSource: {
          serviceInstance: tapp.services.base_Attachment.getTreeCategoryClassifications,
          serviceInstanceInputParameters: {}
        },
        tree: {
          showCheckbox: false,
          defaultCheckedKeys: []
        }
      },
      gridOptions: {
        dataSource: {
          loadDataOnFirst: false,
          serviceInstance: tapp.services.base_Attachment.getCategoryiesByClassificationId,
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
              prop: 'name',
              label: '名称',
              sortable: true,
              width: 250
            },
            {
              prop: 'required',
              columnKey: 'required',
              label: '必输',
              filters: [{
                text: '是',
                value: 1
              }, {
                text: '否',
                value: 0
              }],
              formatter: (row, column, cellValue) => {
                return row.required === 0 ? '否' : '是';
              },
              render: (h, params) => {
                return h('el-tag', {
                  props: {
                    type: params.row.required === true ? 'success' : 'info'
                  } // 组件的props
                }, params.row.required === true ? '是' : '否')
              }
            },
            {
              prop: 'sortIndex',
              label: '排序号',
              sortable: true,
              minWidth: 100
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
    ExistCategorySelectForm,
  },
  created() {

  },
  computed: {
    selectedCategoryItemName() {
      if (this.selectedCategoryItem == null ||
        this.selectedCategoryItem.name == null) {
        return '未选择附件分类'
      }
      return this.selectedCategoryItem.name;
    },
  },
  methods: {
    doNew() {
      if (this.selectedCategoryItem == null) {
        this.$notify.info({
          title: '系统提示',
          message: '未选择附件分类！'
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
      this.selectedCategoryItem = dataItem;
      this.gridOptions.dataSource.serviceInstanceInputParameters.categoryId = dataItem.id;
      this.$refs.searchReulstList.refresh();
    },
    handleSelectionChange(val) {
      this.selectedItems = val;
    },
    doSearch() {
      this.$refs.searchReulstList.refresh();
    },
    doExistsSelect() {
      if (this.selectedCategoryItem == null) {
        this.$notify.info({
          title: '系统提示',
          message: '未选择附件分类！'
        });
        return;
      }

      this.existCategorySelectVisible = true;
      this.$nextTick(() => {
        this.$refs.existCategorySelectForm.init(this.selectedCategoryItem.id)
      })
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
        tapp.services.base_Attachment.batchDeleteCategories(ids).then(function(result) {
          self.$notify.success({
            title: '系统删除成功',
            message: '系统删除成功！'
          });
          self.$refs.searchReulstList.refresh();
        })
      });
    },
    doExportExcel() {
      this.$refs.searchReulstList.exportCSV('附件类别列表');
    },
  }
}
</script>
<style >
.el-card__header {
  height: 82px;
}
</style>
