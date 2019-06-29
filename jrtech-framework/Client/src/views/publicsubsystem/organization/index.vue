<template>
<div>
  <el-row :gutter="20">
    <el-col :span="9">
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>组织机构</span>
          <div style="float: right; padding: 3px 0">
            <el-button icon="el-icon-delete" type="danger" @click="doOrgBatchDelete()">批量删除</el-button>
          </div>
        </div>
        <div class="text item">
          <t-tree ref="orgTree" :options="orgTreeOptons" @node-click="handleOrgNodeClick">
          </t-tree>
        </div>
      </el-card>
    </el-col>
    <el-col :span="15">
    <span>{{selectedOrgItemName}}</span>
      <el-tabs v-model="tabActive" @tab-click="handleTabClick" >
        <el-tab-pane label="岗位" name="role" >
          <role-list ref="rolelist" :organizationId="selectedOrgId" ></role-list>
        </el-tab-pane>
      </el-tabs>
    </el-col>
  </el-row>
  <org-edit-form v-if="orgEditFormVisible" ref="orgEditForm" @change="doOrgRefresh"></org-edit-form>

</div>
</template>

<script>
import RoleList from './roleList'
import OrgEditForm from './orgEdit'
export default {
  data() {
    return {
      tabActive:'role',
      orgEditFormVisible: false,
      selectedOrgItem: null,
      orgTreeOptons: {
        dataSource: {
          serviceInstance: tapp.services.base_Organization.getTreeOrganizations,
          serviceInstanceInputParameters: {}
        },
        tree: {
          checkStrictly: true,
          showCheckbox: true,
          defaultCheckedKeys: [],
          renderContent: this.renderOrgContent,
        }
      },
    }
  },
  components: {
    RoleList,
    OrgEditForm
  },
  created() {

  },
  computed: {
    selectedOrgId(){
      if (this.selectedOrgItem == null ) {
        return null;
      }
      return this.selectedOrgItem.id;
    },
    selectedOrgItemName() {
      if (this.selectedOrgItem == null ||
        this.selectedOrgItem.name == null) {
        return '未选择组织机构'
      }
      return this.selectedOrgItem.name;
    },
  },
  methods: {
        renderOrgContent(h, { node, data, store }) {
            return (
              <span class="custom-tree-node">
                <span>{node.label}</span>
                <span>
                  <el-button size="mini" type="text" on-click={ () => this.doOrgEdit(node, data) }>编辑</el-button>
                  <el-button size="mini" type="text" on-click={ () => this.doOrgAppend(data) }>增加子项</el-button>
                  <el-button size="mini" type="text" on-click={ () => this.doOrgCopy(data) }>复制</el-button>
                </span>
              </span>);
        },
        handleTabClick(tab, event) {
          if (!tab) {
            return;
          }
          this.tabActive = tab.name;
        },
        doOrgEdit(node, data) {
          this.orgEditFormVisible = true
          this.$nextTick(() => {
            this.$refs.orgEditForm.edit(data.id, data.name)
          })
        },
        doOrgCopy(data) {
        let self = this;
         tapp.services.base_Organization.copy(data.id).then(function(result) {
              self.doOrgRefresh();
              self.$notify.success({
                title: '操作成功',
                message: '复制成功！'
              });
            })
        },
        doOrgAppend(data) {
          this.orgEditFormVisible = true
          this.$nextTick(() => {
            this.$refs.orgEditForm.new(data.id, data.name)
          })
        },
        doOrgBatchDelete() {
          let self = this;
          let ids = self.$refs.orgTree.getCheckedKeys();
          if (!ids || ids.length == 0) {
            self.$notify.info({
              title: '系统提示',
              message: '您没选择任何行，无法操作！'
            });
            return;
          }
          self.$confirm('确认要删除共' + ids.length + '项组织机构吗?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            tapp.services.base_Organization.batchDelete(ids).then(function(result) {
              self.doOrgRefresh();
              self.$notify.success({
                title: '操作成功',
                message: '系统删除成功！'
              });
            })
          });
        },
        doOrgRefresh() {
          this.$refs.orgTree.refresh();
        },
        handleOrgNodeClick(dataItem, node, el) {
          this.selectedOrgItem = dataItem;
        },
    }
  }
</script>
<style >

</style>
