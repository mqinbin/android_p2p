# android_p2p




## 客户端
在/out/中有一个apk，可以安装查看效果
登录用户名qinbin 密码123456

### 应用技术：

整体界面框架：Activity+ViewPager+Fragment+Fragment嵌套。自定义状态布局，实现初始化、数据加载中、加载成功、加载失败等统一界面显示。部分界面使用DataBinding实现。
自定义控件：渐变色ViewPager、联动ViewPager、随机布局(孩子位置随机，但不会重叠)、流式布局、飞入飞出布局(用于用户切换到自己感兴趣的内容)。
官方及第三方扩展：AndroidMPChart显示图表信息、支付宝SDK、AndroidAnotation。
服务器通讯：OkHttp、RSA数据加密。


## 服务器
非常简单的静态服务器， 运行在aliyun中
