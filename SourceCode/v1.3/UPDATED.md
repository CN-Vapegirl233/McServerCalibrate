# **Steve's Server Calibrate**

## **v1.1.4-->v1.3**

**更新日志：**

跳过1.2版本（测试），发布1.3



--增加功能：语言

&nbsp;	服务端或管理员使用/serverscl <语言> （zh-cn,zh-tr,en）可以设置服务器默认语言

&nbsp;	玩家使用/playerscl <语言> （zh-cn,zh-tr,en）可以设置自己的语言（高于服务器默认设置）



--修复漏洞：储存

&nbsp;	修复了1.1.4版本中玩家密码是储存在内存中，服务器重启或重新加载插件时导致后台需重新设置密码的问题



--修改内容：配置

&nbsp;	修改配置文件，由原来的sc.yml改为config.yml，便于未来增加功能以及修改，同时服务器默认语言也将储存在config.yml里

&nbsp;	玩家语言将储存在player\_language.yml里



&nbsp;													WorldNo.1Steve 20250825

ps:修了半天终于修好了，建议大家备份好文件内容，在安装新版的插件时删除旧的“ServerCalibrate”文件夹！

