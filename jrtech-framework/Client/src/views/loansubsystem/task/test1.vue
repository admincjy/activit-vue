
<template>
<div>
  {{entity}}
  <el-button type="primary" v-if="$util.hasPermission('pl_test_test1')" @click="test1()">
    <i class="fa fa-send" style="margin-right: 15px"></i>测试1
  </el-button>
</div>
</template>
<script>
export default {
  data() {
    return {
      entity: null
    }
  },
  components: {

  },
  created() {
    tapp.services.pL_Test.test2().then(function() {
      alert('success!');

    });

  },
  activated() {},
  computed: {},
  methods: {
    test1: function() {
      let input = {
        "id": "0001",
        "name": "world",
      };
      let self = this;
      tapp.services.pL_Test.test1(input).then(function(result) {
        self.entity = result;
      });
    }
  }
}
</script>
