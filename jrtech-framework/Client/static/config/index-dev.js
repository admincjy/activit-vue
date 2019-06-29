/**
 * 开发环境
 */
;(function () {
  window.SITE_CONFIG = window.SITE_CONFIG || {};
  //后端服务路径：注意是http://
  window.SITE_CONFIG['serverUrl'] = 'http://localhost:8082';
  window.SITE_CONFIG['stompClientUrl'] = window.SITE_CONFIG['serverUrl'];
  window.SITE_CONFIG['wfDesingUrl'] = window.SITE_CONFIG['serverUrl'] + '/modeler.html?modelId=';
  window.SITE_CONFIG['druidMonitorUrl'] = window.SITE_CONFIG['serverUrl'] + '/druid/index.html';
  window.SITE_CONFIG['baseScriptUrl'] = window.SITE_CONFIG['serverUrl'] + '/jsscript/getAll';
  window.SITE_CONFIG['apiDoc']  = window.SITE_CONFIG['serverUrl'] +  '/swagger-ui.html';
  window.SITE_CONFIG['ueditorHomeUrl']  = window.SITE_CONFIG.cdnUrl +  'static/plugins/UEditor/';
  window.SITE_CONFIG['ueditorServerUrl']  = window.SITE_CONFIG['serverUrl'] +  '/ueditor/exec';
  window.SITE_CONFIG.resList = {
    icon: window.SITE_CONFIG.cdnUrl + 'static/img/favicon.ico',
    css: [
    ],
    js: [
      window.SITE_CONFIG.cdnUrl + 'static/plugins/jquery.min.js',
      window.SITE_CONFIG.cdnUrl + 'static/plugins/webuploader/webuploader.js',
      window.SITE_CONFIG.baseScriptUrl,
      window.SITE_CONFIG.cdnUrl + 'app.js',
    ]
  };
})();
