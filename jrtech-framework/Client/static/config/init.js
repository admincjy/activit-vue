

(function() {
  var resList = window.SITE_CONFIG.resList;
  // 图标
  (function() {
    if (resList.icon) {
      var _icon = document.createElement('link');
      _icon.setAttribute('rel', 'shortcut icon');
      _icon.setAttribute('type', 'image/x-icon');
      _icon.setAttribute('href', resList.icon);
      document.getElementsByTagName('head')[0].appendChild(_icon);
    }
  })();

  // 样式
  (function() {
    var _script = null;
    for (var i = 0; i < resList.css.length; i++) {
      _style = document.createElement('link');
      _style.href = resList.css[i];
      _style.setAttribute('rel', 'stylesheet');
      document.getElementsByTagName('head')[0].appendChild(_style);
    }
  })();

  // 脚本
  document.onreadystatechange = function() {
    if (document.readyState === 'complete') {
        //js会并行的加载，但会按照参数给定的先后顺序执行这些文件
        //http://headjs.com/site/download.html
      head.load(resList.js, function() {
        console.log('resList js done')
      });

    }
  };
})();
