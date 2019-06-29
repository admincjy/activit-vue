
<template>
<div>

      <wf-processtrack ref="processtrack" :procInstId="procInstId" >
      </wf-processtrack>

</div>
</template>

<script>

export default {
  data() {
    return {

      procInstId: null,　
      docId: null,
      docEntity: {},　
    }
  },
  components: {
  },
  created() {
    Object.assign(this.$data, this.$options.data());
    this.docId = this.$route.query.id;　
    this.procInstId = this.$route.query.procInstId;
    if (this.docId) {
      let self = this;
      tapp.services.pL_LoanEnter.get(self.docId).then(function(result) {
        self.docEntity = result;　
      });
    }
  },
  activated() {

  },
  computed: {

  },
  methods: {

  }
}
</script>
