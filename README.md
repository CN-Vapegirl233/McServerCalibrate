# 我的世界服务器校验插件-我的世界伺服器校驗外掛程式-McServerCalibrate
简体中文（中国大陆）：
一个我的世界Java版服务器插件，用于校验玩家的身份和密码，以保护你的我的世界服务器。
目前插件可以支持Java版1.20.x~1.21.x。（只测试了1.20.1、1.20.4、1.21.8，其他版本请自行测试）
运行插件需要你的服务器是支持安装插件的插件端（如Spigot、Bukkit、Paper）或混合端（如Mohist、Arclight）。

--用法介绍
将Steve's_Server_Calibrate_vx.x.jar拖入服务器的plugins文件夹内。放入后首次加载会创建ServerCalibrate文件夹。进入文件夹，打开plugins\ServerCalibrate\ServerCalibrate，会有两个文件sc.yml和pp.yml。其中，sc.yml里，可以编辑：服务器名字、服主联系qq、服主联系邮箱、是否展示qq/邮箱。如果选择不展示某一项，可以将true改为false。pp.yml用于储存玩家密码。密码将会通过AES加密，无法被破解（概率极小，除非密钥被破解）。
编辑后在服务端输入/reload以重新加载。当看见用0/6拼成的：
WORLD
NO.1
STEVE
BUILD
SERVER
CALIBRATE
ON 20250824
时，说明加载成功。
之后便可在后台（或OP等级=4的玩家）运行/scset <玩家名称> <密码>来为特定的玩家设定密码。
玩家在进入服务器时，可以输入/sc <密码>来进行验证，验证期间无法移动、破坏以及获取/丢弃物品，每次登录可以验证3次，每次30秒时间，超时则浪费一次机会，1小时内错误超过10次则会banip。
没有被认证的玩家将无法进入服务器（这便是与一般登录插件/白名单的区别，登录插件只能防盗号，而白名单容易被仿冒（如果服务器处于离线模式），ServerCalibrate则中和了登录插件与白名单，并且更加小巧，兼容性好，拥有锁号机制，若被banip，只有服主或其他有权限的玩家可以使用/pardon-ip（可以在控制台内查看）解除封禁。

繁體中文（使用機翻）：
一個我的世界Java版伺服器外掛程式，用於校驗玩家的身份和密碼，以保護你的我的世界伺服器。
目前外掛程式可以支援Java版1.20.x~1.21.x。 （只測試了1.20.1、1.20.4、1.21.8，其他版本請自行測試）
運行外掛程式需要你的伺服器是支援安裝外掛程式的外掛程式端（如Spigot、Bukkit、Paper）或混合端（如Mohist、Arclight）。

--用法介紹
將Steve's_Server_Calibrate_vx.x.jar拖入伺服器的plugins資料夾內。 放入後首次載入會創建ServerCalibrate資料夾。 進入資料夾，打開plugins\ServerCalibrate\ServerCalibrate，會有兩個文件sc.yml和pp.yml。 其中，sc.yml里，可以編輯：伺服器名字、服主聯繫qq、服主聯繫郵箱、是否展示qq/郵箱。 如果選擇不展示某一項，可以將true改為false。 pp.yml用於儲存玩家密碼。 密碼將會通過AES加密，無法被破解（概率極小，除非密鑰被破解）。
編輯后在服務端輸入/reload以重新載入。 當看見用0/6拼成的：
WORLD
NO.1
STEVE
BUILD
SERVER
CALIBRATE
ON 20250824
時，說明載入成功。
之後便可在後台（或OP等級=4的玩家）運行/scset <玩家名称> <密码>來為特定的玩家設定密碼。
玩家在進入伺服器時，可以輸入/sc <密码>來進行驗證，驗證期間無法移動、破壞以及獲取/丟棄物品，每次登錄可以驗證3次，每次30秒時間，超時則浪費一次機會，1小時內錯誤超過10次則會banip。
沒有被認證的玩家將無法進入伺服器（這便是與一般登錄外掛程式/白名單的區別，登錄外掛程式只能防盜號，而白名單容易被仿冒（如果伺服器處於離線模式），ServerCalibrate則中和了登錄外掛程式與白名單，並且更加小巧，相容性好，擁有鎖號機制，若被banip，只有服主或其他有許可權的玩家可以使用/pardon-ip（可以在控制台內查看）解除封禁。

English(Using machine flip):
A Minecraft Java Edition server plugin that verifies the player's identity and password to protect your Minecraft server.
Currently, the plugin can support Java version 1.20.x~1.21.x. (Only tested 1.20.1, 1.20.4, 1.21.8, please test other versions by yourself)
To run the plugin, your server needs to be a plugin side that supports installing plugins (e.g. Spigot, Bukkit, Paper) or a hybrid side (e.g. Mohist, Arclight).

--Introduction to usage
Drag Steve's_Server_Calibrate_vx.x.jar into the plugins folder of your server. The first load after putting in creates a ServerCalibrate folder. Go to the folder, open plugins\ServerCalibrate\ServerCalibrate, and there will be two files sc.yml and pp.yml. In the sc.yml, you can edit: server name, server owner contact QQ, server owner contact email, and whether to display QQ/email. If you choose not to show an item, you can change true to false. pp.yml used to store player passwords. The password will be encrypted via AES and cannot be cracked (very unlikely unless the key is cracked).
After editing, enter /reload on the server to reload. When you see a spelling made of 0/6:
WORLD
NO.1
STEVE
BUILD
SERVER
CALIBRATE
ON 20250824
indicates that the loading was successful.
After that, you can run /scset in the background (or players with OP level = 4) <playername> <password>to set a password for a specific player.
When players enter the server, they can enter /sc <password>for verification, during which they cannot move, destroy, or obtain/discard items, and can be verified 3 times per login, each time it takes 30 seconds, and if the timeout is over, a chance will be wasted, and if there are more than 10 errors in 1 hour, it will be baniped.
Unauthenticated players will not be able to enter the server (this is the difference from the general login plugin/whitelist, the login plugin can only prevent theft of the number, and the whitelist is easy to be counterfeited (if the server is in offline mode), ServerCalibrate neutralizes the login plugin and the whitelist, and is more compact, compatible, has a lock mechanism, if baniped, only the server owner or other authorized players can use /pardon-ip (can be viewed in the console) to unblock.
