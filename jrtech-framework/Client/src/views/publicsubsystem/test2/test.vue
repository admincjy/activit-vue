<template>
<el-form :model="model" ref="ruleForm">
  <el-form-item prop="age" verify>
    <el-input v-model="model.age"></el-input>
  </el-form-item>
  <el-form-item>
    <el-button @click="doSave()">保存</el-button>
  </el-form-item>
</el-form>
</template>
<script>
export default {
  data() {
    return {
      model: {
        age: null
      }
    }
  },
  methods: {
    doSave(tab, event) {
      tapp.services.test.testSum().then(function(result) {
        alert(result);
      });
    },
  }
}
</script>
