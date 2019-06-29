<template>
<el-dialog :title="title" :close-on-click-modal="false" :visible.sync="visible" width="70%">
  <div>
    <el-form :model="model" ref="ruleForm" @keyup.enter.native="doSave()" label-width="140px">
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="被授权人" prop="attorney" verify class="is-required" :maxLength="50">
            <base-user-select v-model="model.attorney" :text="model.attorneyName" placeholder="请选择">
            </base-user-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="开始日期" prop="startTime"  verify gtdatenow class="is-required">
            <t-datetime-picker v-model="model.startTime" type="date"></t-datetime-picker>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="结束日期" prop="endTime" verify class="is-required" :verify="validateEndTime">
            <t-datetime-picker v-model="model.endTime" type="date"></t-datetime-picker>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="流程定义" prop="processDefinitionList" verify class="is-required">
            <el-transfer v-model="model.processDefinitionList" :data="processDefinationAllList" :props="{ key: 'key', label: 'name' }"></el-transfer>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </div>
  <span slot="footer" class="dialog-footer">
    <el-button type="primary" @click="doSave()">确定</el-button>
	<el-button :disabled="formAction == 0" type="danger" @click="doDelete()">删除</el-button>
    <el-button @click="doClose()">取消</el-button>
  </span>
</el-dialog>
</template>
<script>
import {
  mapMutations,
  mapState,
} from 'vuex'
export default {
  data() {
    return {
      processDefinationAllList: [],
      visible: false,
      docId: null,
      model: {
        processDefinitionList: [],
      },
      formAction: 0, // 0：add,1:edit
    }
  },
  components: {},
  created() {
    this.loadProcessDefList();
  },
  activated() {},
  computed: {
    ...mapState({
      //currentUser: state => state.app.user,
    }),
    title: function() {
      return this.formAction == 0 ? '新增任务授权' : '修改任务授权';
    }
  },
  methods: {
    validateEndTime(rule, value, callback) {
      if (this.model.endTime && this.model.startTime && this.model.endTime < this.model.startTime) {
        callback(new Error('结束日期应大于等于开始日期'))
      } else {
        callback()
      }
    },
    loadProcessDefList() {
      let self = this;
      tapp.services.wf_Model.getMadelList().then(function(result) {
        self.processDefinationAllList = result;
      })
    },
    load() {
      let self = this;
      if (self.docId) {
        tapp.services.wF_TaskAgent.get(self.docId).then(function(result) {
          self.model = result;
          self.formAction = 1;
        });
      } else {
        self.model.id = null;
        self.formAction = 0;
      }
    },
    init(id) {
      this.visible = true;
      this.docId = id;
      this.$nextTick(() => {
        this.$refs.ruleForm.resetFields();
      })
      this.load();
    },
    doSave(formName) {
      let self = this;
      let validPromises = [self.$refs['ruleForm'].validate()];
      Promise.all(validPromises)
        .then(resultList => {
          let requestModel = self.model.processDefinitionList.map(p => {
            let rm = { ...self.model
            };
            delete rm.processDefinitionList;
            rm.processDefinitionKey = p;
            return rm;
          });
          tapp.services.wF_TaskAgent.batchSetAssignee(requestModel).then(function(result) {
            self.formAction = 1;
            self.doClose();
            self.$notify.success({
              title: '操作成功！',
              message: '保存任务授权成功！',
            });
          });
        }).catch(function(e) {
          self.$notify.error({
            title: '错误',
            message: '系统输入验证失败！'
          });
          return false;

        });
    },
    doClose() {
      this.visible = false;
      this.$emit('change');
    },
  }
}
</script>
