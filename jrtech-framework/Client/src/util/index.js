import moment from 'moment';
import accounting from 'accounting-js'
import idcard from 'idcard' //https://www.npmjs.com/package/idcard

let util = {
  ui: {

  }
};
//前端权限校验
util.hasPermission = function(permission) {
  let vm = window.vue;
  let permissions = vm.$store.state.app.user.userPermissions || [];
  let found = permissions.includes(permission);

  return found;
};
//获取前端地址参数对象
util.getRequestSearchObject = function() {
  var url = window.location.search; //获取url中"?"符后的字串
  var theRequest = new Object();
  if (url.indexOf("?") != -1) {
    var str = url.substr(1);
    strs = str.split("&");
    for (var i = 0; i < strs.length; i++) {
      //就是这句的问题
      theRequest[strs[i].split("=")[0]] = decodeURI(strs[i].split("=")[1]);
      //之前用了unescape()
      //才会出现乱码
    }
  }
  return theRequest;
};
//字符串转化为日期类型
util.toLocalDate = function(input) {
  if (!input) {
    return null;
  }
  let a = moment(input);

  let b = a.toDate();
  return b;
};
//根据数据字典Id，获取显示名称
util.dataDicFormat = function(dicType, dicId) {
  if (!dicId || !dicType) {
    return '';
  }
  let dic = tapp.data.base_datadictionary[dicType.toLowerCase()];
  if (!dic) {
    return '';
  }

  let dicItem = dic.find(i => i.id === dicId);
  if (!dicItem) {
    return '';
  }
  return dicItem.name || '';
};
//根据数据字典Id列表，获取用','分隔的显示名称
util.dataDicsFormat = function(dicType, dicIds) {
  if (!dicIds || !dicType) {
    return '';
  }
  let dic = tapp.data.base_datadictionary[dicType.toLowerCase()];
  if (!dic) {
    return '';
  }
  let checkedDicItemIds = dicIds.split(',');
  let dicItems = dic.filter(i => checkedDicItemIds.includes(i.id));
  return dicItems.map(p => p.name).join(",");
};
//根据字典类别生成grid探件的过滤条件
util.getListDataDicFilters = function(dicType) {
    if (!dicType) {
      return [];
    }
    let data = tapp.data.base_datadictionary[dicType.toLowerCase()];
    if (!data) {
      return [];
    }
    return data.map(p => {
      return {
        text: p.name,
        value: p.id
      };
    });
  },
  //日期时间格式化,:YYYY-MM-DD HH:mm:ss格式
  util.datetimeFormat = function(input) {
    if (!input) {
      return '';
    }
    let a = moment(input).utc(8);
    return a.format('YYYY-MM-DD HH:mm:ss');
  };
//日期格式化,:YYYY-MM-DD格式
util.dateFormat = function(input) {
  if (!input) {
    return '';
  }
  let a = moment(input).utc(8);
  return a.format('YYYY-MM-DD');
};
//根据输入datePattern做日期格式化
util.datePatternFormat = function(input, fmt) {
  if (!input) {
    return '';
  }
  let a = moment(input).utc(8);
  return a.format(fmt);
};
//货币格式化，如：数字输入100000，格式化后：¥100,000
util.moneyFormat = function(input) {
  if (input == null) {
    return '';
  }
  let s = accounting.formatMoney(input, {
    symbol: '¥',

  })
  return s;
};
//货币阿拉伯数字转中文大写格式
util.moneyArabiaToChinese = function(Num) {
  let isNegative = false; // 是否负数
  if (Num < 0) {
    Num = -Num;
    isNegative = true;
  }
  if (typeof Num == 'number') {
    Num = Num.toString();
  }
  for (var i = Num.length - 1; i >= 0; i--) {
    Num = Num.replace(",", ""); // 替换money中的“,”
    Num = Num.replace(" ", ""); // 替换money中的空格
  }
  Num = Num.replace("￥", ""); // 替换掉可能出现的￥字符
  if (isNaN(Num)) { // 验证输入的字符是否为数字
    return;
  }
  // ---字符处理完毕，开始转换，转换采用前后两部分分别转换---//
  let part = String(Num).split(".");
  let newchar = "";
  // 小数点前进行转化
  for (let i = part[0].length - 1; i >= 0; i--) {
    if (part[0].length > 10) {
      alert("位数过大，无法计算");
      return "";
    } // 若数量超过拾亿单位，提示
    let tmpnewchar = "";
    let perchar = part[0].charAt(i);
    switch (perchar) {
      case "0":
        tmpnewchar = "零" + tmpnewchar;
        break;
      case "1":
        tmpnewchar = "壹" + tmpnewchar;
        break;
      case "2":
        tmpnewchar = "贰" + tmpnewchar;
        break;
      case "3":
        tmpnewchar = "叁" + tmpnewchar;
        break;
      case "4":
        tmpnewchar = "肆" + tmpnewchar;
        break;
      case "5":
        tmpnewchar = "伍" + tmpnewchar;
        break;
      case "6":
        tmpnewchar = "陆" + tmpnewchar;
        break;
      case "7":
        tmpnewchar = "柒" + tmpnewchar;
        break;
      case "8":
        tmpnewchar = "捌" + tmpnewchar;
        break;
      case "9":
        tmpnewchar = "玖" + tmpnewchar;
        break;
    }
    switch (part[0].length - i - 1) {
      case 0:
        tmpnewchar = tmpnewchar + "元";
        break;
      case 1:
        if (perchar != 0)
          tmpnewchar = tmpnewchar + "拾";
        break;
      case 2:
        if (perchar != 0)
          tmpnewchar = tmpnewchar + "佰";
        break;
      case 3:
        if (perchar != 0)
          tmpnewchar = tmpnewchar + "仟";
        break;
      case 4:
        tmpnewchar = tmpnewchar + "万";
        break;
      case 5:
        if (perchar != 0)
          tmpnewchar = tmpnewchar + "拾";
        break;
      case 6:
        if (perchar != 0)
          tmpnewchar = tmpnewchar + "佰";
        break;
      case 7:
        if (perchar != 0)
          tmpnewchar = tmpnewchar + "仟";
        break;
      case 8:
        tmpnewchar = tmpnewchar + "亿";
        break;
      case 9:
        tmpnewchar = tmpnewchar + "拾";
        break;
    }
    newchar = tmpnewchar + newchar;
  }
  // 小数点之后进行转化
  if (Num.indexOf(".") != -1) {
    if (part[1].length > 2) {
      alert("小数点之后只能保留两位,系统将自动截段");
      part[1] = part[1].substr(0, 2);
    }
    for (var i = 0; i < part[1].length; i++) {
      let tmpnewchar = "";
      let perchar = part[1].charAt(i);
      switch (perchar) {
        case "0":
          tmpnewchar = "零" + tmpnewchar;
          break;
        case "1":
          tmpnewchar = "壹" + tmpnewchar;
          break;
        case "2":
          tmpnewchar = "贰" + tmpnewchar;
          break;
        case "3":
          tmpnewchar = "叁" + tmpnewchar;
          break;
        case "4":
          tmpnewchar = "肆" + tmpnewchar;
          break;
        case "5":
          tmpnewchar = "伍" + tmpnewchar;
          break;
        case "6":
          tmpnewchar = "陆" + tmpnewchar;
          break;
        case "7":
          tmpnewchar = "柒" + tmpnewchar;
          break;
        case "8":
          tmpnewchar = "捌" + tmpnewchar;
          break;
        case "9":
          tmpnewchar = "玖" + tmpnewchar;
          break;
      }
      if (i == 0)
        tmpnewchar = tmpnewchar + "角";
      if (i == 1)
        tmpnewchar = tmpnewchar + "分";
      newchar = newchar + tmpnewchar;
    }
  }
  // 替换所有无用汉字
  while (newchar.search("零零") != -1)
    newchar = newchar.replace("零零", "零");
  newchar = newchar.replace("零亿", "亿");
  newchar = newchar.replace("亿万", "亿");
  newchar = newchar.replace("零万", "万");
  newchar = newchar.replace("零元", "元");
  newchar = newchar.replace("零角", "");
  newchar = newchar.replace("零分", "");

  if (newchar.charAt(newchar.length - 1) == "元" || newchar.charAt(newchar.length - 1) == "角") {
    newchar = newchar + "整";
  }

  if (isNegative) {
    newchar = '负' + newchar;
  }

  return newchar;
};
//百分比格式化，如0.15,格式化后 15%
util.percentFormat = function(input) {
  if (input == null) {
    return '';
  }

  let s = accounting.formatMoney(input, {
    symbol: '%',
    format: "%v %s"
  })
  return s;
};
//验证输入字符串是否是有效的手机号码
util.verifyMobile = function(str) {
  let re = /^1\d{10}$/;
  return re.test(str);
};
//验证输入字符串字符是否都是数字
util.verifyNumber = function(str) {
  let re = /^\d+$/;
  return re.test(str);
};
//验证输入日期是否小于等于当前日期
util.verifyLTDateNow = function(input) {
  let a = moment(input);
  let b = a.toDate();
  let r = b <= Date.now();
  return r;
};
//验证输入日期是否大于等于当前日期
util.verifyGTDateNow = function(input) {
  let now = new Date();
  let a = moment(input);
  let b = a.toDate();
  let r = b >= new Date(now.getFullYear(), now.getMonth(), now.getDate());
  return r;
};
//验证输入字符串字符是否有效的身份证号码
util.verifyIdCard = function(input) {
  if (input == null) {
    return true;
  }
  return idcard.verify(input);
};
//解析身份证号码字符串的信息
util.parseIdCard = function(input) {
  if (input == null) {
    return '';
  }

  let cardNoInfo = idcard.info(input);
  if (cardNoInfo && cardNoInfo.valid) {
    let birthdayStr = cardNoInfo.birthday.toString();
    cardNoInfo.birthdayString = birthdayStr.substr(0, 4) + "-" +
      birthdayStr.substr(4, 2) +
      "-" +
      birthdayStr.substr(6, 2) +
      " 00:00:00";
  }
  return cardNoInfo;
};
//修改当前网页标题
util.ui.title = function(title) {
  title = title || window.SITE_CONFIG.appName;
  window.document.title = title;
};
//关闭导航tab,formName:关闭的导航name，toName:激活的导航name，如果已打开则激活，如果未打开，则打开页面
util.closeTabNav = function(fromName, toName) {

  if (!fromName) {
    return;
  }
  let vm = window.vue;
  let contentTabsActiveName = vm.$store.state.ui.contentTabsActiveName;
  var newTabs = vm.$store.state.ui.contentTabs.filter(item => item.name !== fromName);

  vm.$store.commit('UPDATE_CONTENT_TABS', newTabs);
  if (newTabs == null || newTabs.length == 0) {
    vm.$router.push({
      path: '/'
    }, () => {
      vm.$store.commit('UPDATE_CONTENT_TABS', []);
    })
    return;
  }
  if (toName) {
    let routerObj = newTabs.find(p => {
      return p.name == toName
    })

    vm.$router.push({
      // 'name': routerObj.name
      path: routerObj.url
    }, () => {
      vm.$store.commit('UPDATE_CONTENT_TABS', newTabs);
      vm.$store.commit('UPDATE_CONTENT_TABS_ACTIVE_NAME', routerObj.name);
    })
  }
};
//关闭当前导航tab并激活toName导航,toName:激活的导航name，如果已打开则激活，如果未打开，则打开页面
util.closeCurrentTabNav = function(toName) {
  let vm = window.vue;
  if (!vm.$router.currentRoute) {
    return;
  }
  let fromName = vm.$router.currentRoute.name;
  util.closeTabNav(fromName, toName);
};
//遍历树形实体（服务器端定义有树形结构）
util.retriveTree = function(items, func) {
  function innerRetriveTrees(nodes) {
    for (let i = 0; i < nodes.length; i++) {
      let node = nodes[i];
      func(node);

      if (node.items && node.items.length > 0) {
        innerRetriveTrees(nodes[i].items);
      }
    }
  }
  innerRetriveTrees(items);
}
//读取树形实体的所有叶子结点
util.getTreeLeafNodes = function(nodes) {
  let leaftNodes = [];
  util.retriveTree(nodes, (node) => {
    if (!node.items || node.items.length == 0) {
      leaftNodes.push(node);
    }
  })
  return leaftNodes;
};
//树形实体转换为数组结构
util.treeToArray = function(nodes) {
  let allnodes = [];

  util.retriveTree(nodes, (node) => {
    let {
      self
    } = node;
    Object.assign(node, self);
    if (node._expanded === undefined) {
      node._expanded = true;  
    }
    allnodes.push(node);
  })
  return allnodes;
};
//设置cookie
util.setCookie = function(cname, cvalue, exdays) {
  var d = new Date();
  d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
  var expires = "expires=" + d.toUTCString();
  document.cookie = cname + "=" + escape(cvalue) + "; " + expires;
};
//获取cookie
util.getCookie = function(cname) {
  var name = cname + "=";
  var ca = document.cookie.split(';');
  for (var i = 0; i < ca.length; i++) {
    var c = ca[i];
    while (c.charAt(0) == ' ') c = c.substring(1);
    if (c.indexOf(name) != -1) return unescape(c.substring(name.length, c.length));
  }
  return "";
};
//清除cookie
util.clearCookie = function(cname) {
  util.setCookie(cname, "", -1);
};
// 根据文件名后缀区分 文件类型
util.getFileType = function(fileName) {
  /*
   * @param: fileName - 文件名称
   * @param: 数据返回 1) 无后缀匹配 - false
   * @param: 数据返回 2) 匹配图片 - image
   * @param: 数据返回 3) 匹配 txt - txt
   * @param: 数据返回 4) 匹配 excel - excel
   * @param: 数据返回 5) 匹配 word - word
   * @param: 数据返回 6) 匹配 pdf - pdf
   * @param: 数据返回 7) 匹配 ppt - ppt
   * @param: 数据返回 8) 匹配 视频 - video
   * @param: 数据返回 9) 匹配 音频 - audio
   * @param: 数据返回 10) 其他匹配项 - other
   */



  // 后缀获取
  var suffix = '';
  // 获取类型结果
  var result = '';
  try {

    var flieArr = fileName.split('.');
    suffix = flieArr[flieArr.length - 1];
  } catch (err) {
    suffix = '';
  }
  // fileName无后缀返回 false

  if (!suffix) {
    result = false;
    return result;
  }
  // 图片格式
  var imglist = ['png', 'jpg', 'jpeg', 'bmp', 'gif'];
  // 进行图片匹配
  result = imglist.some(function(item) {
    return item == suffix;
  });
  if (result) {
    result = 'image';
    return result;
  };

  // 匹配txt
  var txtlist = ['txt'];
  result = txtlist.some(function(item) {
    return item == suffix;
  });
  if (result) {
    result = 'txt';
    return result;
  };
  // 匹配 excel
  var excelist = ['xls', 'xlsx'];
  result = excelist.some(function(item) {
    return item == suffix;
  });
  if (result) {
    result = 'excel';
    return result;
  };
  // 匹配 word
  var wordlist = ['doc', 'docx'];
  result = wordlist.some(function(item) {
    return item == suffix;
  });
  if (result) {
    result = 'word';
    return result;
  };
  // 匹配 pdf
  var pdflist = ['pdf'];
  result = pdflist.some(function(item) {
    return item == suffix;
  });

  if (result) {
    result = 'pdf';
    return result;
  };

  // 匹配 ppt
  var pptlist = ['ppt'];
  result = pptlist.some(function(item) {
    return item == suffix;
  });

  if (result) {
    result = 'ppt';
    return result;
  };


  // 匹配 视频
  var videolist = ['mp4', 'm2v', 'mkv'];
  result = videolist.some(function(item) {
    return item == suffix;
  });

  if (result) {
    result = 'video';
    return result;
  };

  // 匹配 音频
  var radiolist = ['mp3', 'wav', 'wmv'];
  result = radiolist.some(function(item) {
    return item == suffix;
  });
  if (result) {
    result = 'audio';
    return result;

  }
  // 其他 文件类型
  result = 'other';
  return result;
};
util.download = function(fileName, url, params) {
  window.vue.$http.request({
    url: url,
    method: 'post',
    data: params,
    responseType: 'blob'
  }).then(res => {
    const content = res
    const blob = new Blob([content])
    if ('download' in document.createElement('a')) { // 非IE下载
      const elink = document.createElement('a')
      elink.download = fileName
      elink.style.display = 'none'
      elink.href = URL.createObjectURL(blob)
      document.body.appendChild(elink)
      elink.click()
      URL.revokeObjectURL(elink.href) // 释放URL 对象
      document.body.removeChild(elink)
    } else { // IE10+下载
      navigator.msSaveBlob(blob, fileName)
    }
  }).catch(err => {
    alert(err);
  })
};
export default util;
