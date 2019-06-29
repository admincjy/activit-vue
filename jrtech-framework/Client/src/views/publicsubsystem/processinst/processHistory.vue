
<template>
<div>
  <wf-processtrack ref="processtrack" :procInstId="procInstId">
  </wf-processtrack> 
</div>
</template>

<script>
export default {
  data() {
    return {
      procInstId: null,
    }
  },
  components: {},
  created() {
    this.procInstId = this.$route.query.procInstId;
  },
  activated() {

  },
  computed: {

  },
  methods: {

  }
}
</script>
