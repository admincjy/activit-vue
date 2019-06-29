# 安装依赖(优先使用)
npm install
# 安装依赖(下载较慢时使用)
npm install --registry=https://registry.npm.taobao.org

#上述2种【安装依赖】无法正常时，请尝试删除node_modules文件夹后，使用cnpm安装
# 第一步
npm install -g cnpm --registry=https://registry.npm.taobao.org
# 第二步
cnpm install

# 启动服务
npm run dev
```

- 开发时，如何连接后台项目api接口？
