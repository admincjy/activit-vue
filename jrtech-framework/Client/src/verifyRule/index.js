
/*

element-ui-verify自定义验证规则

https://github.com/aweiu/element-ui-verify
maxLength: 最大文本长度校验
idcard: 身份证号校验
num: 数字格式校验
ltdatenow: 不能大于当前日期校验
*/
import util from '@/util';

// 最大文本长度
export default [{
    rule: {
      name: 'maxLength',
      type: Number
    },
    ruleMethod: maxLength => ({
      max: maxLength,
      message: '长度在 ' + maxLength + ' 个字符内'
    })
  },
  {
    rule: {
      name: 'idcard',
    },
    ruleMethod: () => ({
      validator(rule, val, callback) {
        if (!val || util.verifyIdCard(val)) {
          callback()
        } else callback('无效的身份证号')
      }
    })
  },
  {
    rule: {
      name: 'num',
    },
    ruleMethod: () => ({
      validator(rule, val, callback) {
        if (!val || util.verifyNumber(val)) {
          callback()
        } else callback('非数字格式')
      }
    })
  },
  {
    rule: {
      name: 'ltdatenow',
    },
    ruleMethod: () => ({
      validator(rule, val, callback) {
        if (!val || util.verifyLTDateNow(val)) {
          callback()
        } else callback('不能大于当前日期')
      }
    })
  },
    {
    rule: {
      name: 'gtdatenow',
    },
    ruleMethod: () => ({
      validator(rule, val, callback) {
        if (!val || util.verifyGTDateNow(val)) {
          callback()
        } else callback('不能小于当前日期')
      }
    })
  },
];
