<template>
<el-dialog :title="'选择组织机构'" :close-on-click-modal="false" :visible.sync="visible">
  <div class="mod-role">
    <t-tree ref="orgTree" :options="orgTreeOptons" @node-click="handleOrgNodeClick">
    </t-tree>
  </div>
</el-dialog>
</template>

<script>
export default {
  data() {
    return {
      visible: false,
      orgTreeOptons: {
        dataSource: {
          serviceInstance: tapp.services.base_Organization.getTreeOrganizations,
          serviceInstanceInputParameters: {}
        },
        tree: {
          showCheckbox: false,
        }
      },
    }
  },
  components: {},
  created() {

  },
  methods: {
    // 初始化
    init() {
      this.visible = true;

      this.$nextTick(() => {

      })
    },
    handleOrgNodeClick(dataItem, node, el) {
      this.visible = false; 
      this.$emit('select', dataItem);
    },
  }
}
</script>
