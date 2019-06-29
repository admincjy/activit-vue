'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  WebDomain:'"/"',
  WebCDNUrl:'"/"',
  AppName:'"未来软件"',
  AppShortName:'"jrtech"',
  OPEN_PROXY: false // 是否开启代理, 重置后需重启vue-cli
})
