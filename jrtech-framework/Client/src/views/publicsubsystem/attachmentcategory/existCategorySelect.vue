<template>
<el-dialog :title="'选择附件类别'" :close-on-click-modal="false" :visible.sync="visible">

  <el-form :inline="true" @keyup.enter.native="loadData()">
    <el-form-item>
      <el-input v-model="searchKey" clearable></el-input>
    </el-form-item>
    <el-form-item>
      <el-button icon="el-icon-search" @click="loadData()">查询</el-button>
    </el-form-item>
  </el-form>

  <el-table ref="grid" :data="gridData" tooltip-effect="dark" style="width: 100%" @selection-change="handleSelectionChange">
    <el-table-column type="selection" width="55">
    </el-table-column>
    <el-table-column prop="name" label="附件类别" width="200">
    </el-table-column>
    <el-table-column prop="required" label="必输">
      <template slot-scope="scope">
        <el-switch v-model="scope.row.required"></el-switch>
      </template>
    </el-table-column>
  </el-table>
  <span slot="footer" class="dialog-footer">
    <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
      <el-button @click="visible = false">取消</el-button>
  </span>
</el-dialog>
</template>
<script>
export default {
  data() {
    return {
      visible: false,
      id: null,
      searchKey: '',
      sortField: 'name',
      sortDirection: 'ascending',
      gridData: [],
      selectedItems: []
    };
  },
  created() {

  },
  activated() {},
  methods: {

    init(id) {
      this.visible = true;
      this.id = id;
      this.$nextTick(() => {
        this.loadData();
      });


    },
    loadData() {
      let self = this;
      let iRequestData = {};
      iRequestData.sorting = self.sortField + ' ' + self.sortDirection;
      iRequestData.searchKey = self.searchKey;

      tapp.services.base_Attachment.getFilterCategories(iRequestData).then(function(result) {
        self.gridData = result;
      });
    },
    handleSelectionChange(val) {
      this.selectedItems = val;
    },

    dataFormSubmit() {
      let self = this;

      let categoryOfClassificationList = self.selectedItems.map(function(item) {
        return {
          categoryId: item.id,
          categoryClassificationId: self.id,
          required: !!item.required,
        };
      });

      var requestObject = {
        categoryClassificationId: self.id,
        categoryOfClassificationList: categoryOfClassificationList
      };
      let model = self.model;
      tapp.services.base_Attachment.createCategoryOfClassificationList(requestObject).then(function(result) {
        self.$notify.success({
          title: '操作成功',
          message: '操作成功！',
          duration: 1500,
          onClose: () => {
            self.visible = false;
            self.$emit('change');
          }
        });
      });


    },
  }
}
</script>
