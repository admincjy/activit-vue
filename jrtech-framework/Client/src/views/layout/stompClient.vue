
<template>
</template>
<script>
import Stomp from 'stompjs'
import SockJS from 'sockjs-client'
export default {
  name: 'stompClient',
  data() {
    return {
      stompClient: null,
      connected: false,
    }
  },
  created() {
    if (this.$store.state.app.user.loginToken) {
      this.connect()
    }
  },
  beforeDestroy: function() {
    this.disconnect();
  },
  methods: {
    onConnected: function(frame) {
      this.connected = true;
      console.log('Connected: ' + frame)
      this.stompClient.subscribe('/topic/getResponse', this.responseCallback, this.onFailed);
      this.stompClient.subscribe('/user/' + this.$store.state.app.user.userId + '/notification', this.responseCallback, this.onFailed);
    },
    onFailed: function(frame) {
      //this.$notify.error({
      //  title: '系统错误',
      //  message: '消息服务连接失败!',
      //});

      console.log('STOMP: ' + frame);
      //setTimeout(this.connect, 10000);
      //console.log('STOMP: Reconecting in 10 seconds');

    },
    responseCallback: function(frame) {
      let body = JSON.parse(frame.body);
      let noreadCount = body.noReadCount;
      this.$store.commit('setNotificationNum', noreadCount);

      this.$notify({
        title: body.title,
        message: body.content,
        dangerouslyUseHTMLString: true,
        type: 'warning'
      });
    },
    connect: function() {
      let socket = new SockJS(window.SITE_CONFIG['stompClientUrl'] + "/pmtapi/endpointNotification");
      this.stompClient = Stomp.over(socket);
      this.stompClient.debug = function(str) {
        //  console.log(str);
      };
      let headers = {}
      if (this.$store.state.app.user.loginToken) {
        headers.Authorization = `${this.$store.state.app.user.loginToken}`;
      }
      this.stompClient.connect(headers, this.onConnected, this.onFailed)
    },
    disconnect: function() {
      try {
        if (this.connected) {
          this.stompClient.disconnect(function() {
            //alert("See you next time!");
          });
        }
      } catch (err) {
        //在这里处理错误
      }
    }
  },
}
</script>
