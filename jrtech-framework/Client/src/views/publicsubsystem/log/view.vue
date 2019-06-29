
<template>
<el-dialog :title="'日志详细'" :close-on-click-modal="false" :visible.sync="visible">
  <el-form :model="model" ref="ruleForm" label-width="80px">
    <el-form-item label="请求方法" prop="method">
      <span>{{ model.method }}</span>
    </el-form-item>
    <el-form-item label="请求参数" prop="params">
      <span>{{ model.params }}</span>
    </el-form-item>
    <el-form-item label="执行结果" prop="result">
      <span><el-tag  :type="model.result === 'error' ? 'danger' : 'success'">{{ model.result === 'error' ? '失败' : '成功' }}</el-tag>
      </span>
    </el-form-item>

    <el-form-item label="错误信息" prop="errorMessage">
      <span>{{ model.errorMessage }}</span>
    </el-form-item>
    <el-form-item label="错误堆栈" prop="errorStackTrace">
      <span>{{ model.errorStackTrace }}</span>
    </el-form-item>
  </el-form>
  <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
    </span>
</el-dialog>
</template>

<script>
export default {
  data() {
    return {
      visible: false,
      model: {

      },
    };
  },
  created() {


  },
  activated() {},
  methods: {
    init(id) {
      this.visible = true;
      if (id) {
        let self = this;
        tapp.services.base_Log.get(id).then(function(result) {
          self.model = result;
        });
      }else{
        this.model = {};
      }
    },


  }
  }
  </script>
<style>
span {
  white-space: normal;
  word-break: break-all;
  word-wrap: break-word
}
</style>
