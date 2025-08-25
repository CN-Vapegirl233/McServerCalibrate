package com.example.minecraftplugin;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
public class MinecraftPlugin extends JavaPlugin implements Listener {
    private final Map<String, String> playerPasswords = new ConcurrentHashMap<>();
    private FileConfiguration passwordConfig;
    private File passwordFile;
    private final Map<String, Boolean> playerVerified = new ConcurrentHashMap<>();
    private final Map<String, Integer> playerFailedAttempts = new ConcurrentHashMap<>();
    private final Map<String, Integer> ipFailedAttempts = new ConcurrentHashMap<>();
    private final Map<String, BukkitTask> verificationTasks = new ConcurrentHashMap<>();
    private final Map<String, BossBar> playerBossBars = new ConcurrentHashMap<>();
    private static final int VERIFICATION_TIMEOUT = 30;
    
    private FileConfiguration config;
    private File configFile;
    private String serverName;
    private String ownerQq;
    private String ownerEmail;
    private boolean showQq;
    private boolean showEmail;
    
    // 语言配置
    private final Map<String, FileConfiguration> languageConfigs = new ConcurrentHashMap<>();
    private final Map<String, String> playerLanguage = new ConcurrentHashMap<>();
    private static String serverDefaultLanguage = "zh-cn"; // 服务器默认语言
    private static final String DEFAULT_LANGUAGE = "zh-cn";
    private FileConfiguration playerLangConfig;
    private File playerLangFile;
    
    // Steve's secret code: 619002007

    // 世一史蒂夫制作
    private static final int[] AUTHOR_INFO = {0x4E16, 0x4E00, 0x53F4, 0x7EF4, 0x5236, 0x4F5C, 0x0020, 0x5305, 0x542B, 0x0055, 0x0049, 0x0044, 0x0036, 0x0031, 0x0039, 0x0030, 0x0030, 0x0032, 0x0030, 0x0030, 0x0037};

    // 加载语言配置
    private void loadLanguageConfigs() {
        // 首先尝试从数据目录的lang文件夹加载
        File langFolder = new File(getDataFolder(), "lang");
        for (String lang : Arrays.asList("zh-cn", "zh-tr", "en")) {
            File langFile = new File(langFolder, lang + ".yml");
            if (langFile.exists()) {
                try {
                    FileConfiguration langConfig = YamlConfiguration.loadConfiguration(langFile);
                    languageConfigs.put(lang, langConfig);
                    getLogger().info("已从lang文件夹加载语言文件: " + lang + ".yml");
                } catch (Exception e) {
                    getLogger().severe("无法加载语言文件: " + lang + ".yml - " + e.getMessage());
                }
            } else {
                // 如果lang文件夹中不存在，则从内置资源加载
                try {
                    InputStream inputStream = getResource("lang/" + lang + ".yml");
                    if (inputStream != null) {
                        FileConfiguration langConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(inputStream));
                        languageConfigs.put(lang, langConfig);
                        getLogger().info("已从内置资源加载语言文件: " + lang + ".yml");
                    }
                } catch (Exception e) {
                    getLogger().severe("无法加载语言文件: " + lang + ".yml - " + e.getMessage());
                }
            }
        }
    }

    // 获取消息 - 优先使用玩家个人语言，其次是服务器默认语言，最后是系统默认语言
    private String getMessage(String playerName, String key, Map<String, String> placeholders) {
        String lang = playerLanguage.getOrDefault(playerName, serverDefaultLanguage);
        FileConfiguration langConfig = languageConfigs.getOrDefault(lang, languageConfigs.get(DEFAULT_LANGUAGE));
        
        String message = langConfig.getString("messages." + key, "[Missing message: " + key + "]");
        
        // 替换占位符
        if (placeholders != null) {
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                message = message.replace("{" + entry.getKey() + "}", entry.getValue());
            }
        }
        
        return message;
    }

    @Override
    public void onEnable() {
        // 输出ASCII艺术字
getLogger().info(":0         0   00000   00000   0      000000");
getLogger().info(":0         0  0     0  0    0  0        0   0");
getLogger().info(": 0   0   0   0     0  00000   0        0   0");
getLogger().info(":  0 0 0 0    0     0  0   0   0        0   0");
getLogger().info(":   0   0      00000   0    0  00000  000000");
getLogger().info(":");
getLogger().info(":00    0   00000          00");
getLogger().info(":0 0   0  0     0        000");
getLogger().info(":0  0  0  0     0          0");
getLogger().info(":0   0 0  0     0  00      0");
getLogger().info(":0    00   00000   00   0000000");
getLogger().info(":");
getLogger().info(":  00000  00000  00000  0     0  00000");
getLogger().info(": 0         0    0      0     0  0");
getLogger().info(":  0000     0    00000   0   0   00000");
getLogger().info(":      0    0    0        0 0    0");
getLogger().info(": 00000     0    00000     0     00000");
getLogger().info(": ");
getLogger().info(": ");
getLogger().info(": 00000000   0    0  00000  0       0000000");
getLogger().info(":   0     0  0    0    0    0         0    0");
getLogger().info(":   0000000  0    0    0    0         0    0");
getLogger().info(":   0     0  0    0    0    0         0    0");
getLogger().info(": 00000000    0000   00000  00000   0000000");
getLogger().info(": ");
getLogger().info(": ");
getLogger().info(": ");
getLogger().info(":  6666666  666666  66666   6     6  666666   66666");
getLogger().info(": 6         6       6    6  6     6  6        6    6");
getLogger().info(":  666666   666666  66666    6   6   666666   66666");
getLogger().info(":        6  6       6   6     6 6    6        6   6");
getLogger().info(": 6666666   666666  6    6     6     666666   6    6");
getLogger().info(": ");
getLogger().info(":  66666     666    6       66666   66666666   66666     666   66666  66666");
getLogger().info(": 6         6   6   6         6       6     6  6    6   6   6    6    6");
getLogger().info(": 6         66666   6         6       6666666  66666    66666    6    66666");
getLogger().info(": 6         6   6   6         6       6     6  6   6    6   6    6    6");
getLogger().info(":  66666    6   6   666666  66666   66666666   6    6   6   6    6    66666");
getLogger().info(": ");
getLogger().info(":ON 20250824");

        // 加载语言配置
        loadLanguageConfigs();
        
        // Steve's calibration code starts here
        Obfuscation.complexCalculation();
        int obfuscatedValue = Obfuscation.obfuscate(42);
        @SuppressWarnings("unused")
        int deobfuscatedValue = Obfuscation.deobfuscate(obfuscatedValue);
        @SuppressWarnings("unused")
        String randomKey = Obfuscation.generateKey(16);
        
        initConfig();
        initPasswordFile();
        getServer().getPluginManager().registerEvents(this, this);
        registerCommands();
    }

    public static void main(String[] args) {
        System.out.println("当前文件为Minecraft Java版服务端插件。请在服务端内运行！");
        // Hidden signature: 世一史蒂夫
    }

    private void initConfig() {
        // 确保数据目录存在
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        // 检查并创建config.yml（主配置文件）
        configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                config = YamlConfiguration.loadConfiguration(configFile);
                config.set("servername", "My Minecraft Server");
                config.set("ownerqq", "123456789");
                config.set("owneremail", "admin@example.com");
                config.set("showqq", true);
                config.set("showemail", true);
                config.set("defaultLanguage", "zh-cn"); // 添加默认语言配置
                config.save(configFile);
            } catch (IOException e) {
                getLogger().severe("无法创建配置文件: " + e.getMessage());
            }
        } else {
            config = YamlConfiguration.loadConfiguration(configFile);
            // 从配置文件加载服务器默认语言
            serverDefaultLanguage = config.getString("defaultLanguage", "zh-cn");
        }

        // 检查并创建sc.yml（旧版配置文件，用于兼容）
        File oldConfigFile = new File(getDataFolder(), "sc.yml");
        if (oldConfigFile.exists()) {
            FileConfiguration oldConfig = YamlConfiguration.loadConfiguration(oldConfigFile);
            serverName = oldConfig.getString("servername", "My Minecraft Server");
            ownerQq = oldConfig.getString("ownerqq", "123456789");
            ownerEmail = oldConfig.getString("owneremail", "admin@example.com");
            String showQqStr = oldConfig.getString("showqq", "true").toLowerCase();
            showQq = "true".equals(showQqStr);
            String showEmailStr = oldConfig.getString("showemail", "true").toLowerCase();
            showEmail = "true".equals(showEmailStr);
            // 将旧配置迁移到新配置文件
            config.set("servername", serverName);
            config.set("ownerqq", ownerQq);
            config.set("owneremail", ownerEmail);
            config.set("showqq", showQq);
            config.set("showemail", showEmail);
            try {
                config.save(configFile);
                getLogger().info("配置已从sc.yml迁移到config.yml");
            } catch (IOException e) {
                getLogger().severe("无法保存配置文件: " + e.getMessage());
            }
        } else {
            serverName = config.getString("servername", "My Minecraft Server");
            ownerQq = config.getString("ownerqq", "123456789");
            ownerEmail = config.getString("owneremail", "admin@example.com");
            String showQqStr = config.getString("showqq", "true").toLowerCase();
            showQq = "true".equals(showQqStr);
            String showEmailStr = config.getString("showemail", "true").toLowerCase();
            showEmail = "true".equals(showEmailStr);
        }

        // 检查lang文件夹是否存在
        File langFolder = new File(getDataFolder(), "lang");
        if (!langFolder.exists()) {
            langFolder.mkdirs();
            // 复制内置语言文件到lang文件夹
            for (String lang : Arrays.asList("zh-cn", "zh-tr", "en")) {
                try {
                    InputStream inputStream = getResource("lang/" + lang + ".yml");
                    if (inputStream != null) {
                        File langFile = new File(langFolder, lang + ".yml");
                        java.nio.file.Files.copy(inputStream, langFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    getLogger().severe("无法复制语言文件: " + lang + ".yml - " + e.getMessage());
                }
            }
        }

        getLogger().info("配置已加载: 服务器名称 - " + serverName + ", 默认语言 - " + serverDefaultLanguage);

        // 初始化玩家语言配置文件
        playerLangFile = new File(getDataFolder(), "player_languages.yml");
        if (!playerLangFile.exists()) {
            try {
                playerLangFile.createNewFile();
                playerLangConfig = YamlConfiguration.loadConfiguration(playerLangFile);
                playerLangConfig.save(playerLangFile);
            } catch (IOException e) {
                getLogger().severe("无法创建玩家语言配置文件: " + e.getMessage());
            }
        } else {
            playerLangConfig = YamlConfiguration.loadConfiguration(playerLangFile);
            // 加载保存的玩家语言设置
            for (String playerName : playerLangConfig.getKeys(false)) {
                String lang = playerLangConfig.getString(playerName);
                playerLanguage.put(playerName, lang);
            }
            getLogger().info("已加载 " + playerLanguage.size() + " 个玩家的语言设置");
        }
    }

    private void initPasswordFile() {
        // 确保数据目录存在
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        // 检查并创建pp.yml（密码文件）
        passwordFile = new File(getDataFolder(), "pp.yml");
        if (!passwordFile.exists()) {
            try {
                passwordFile.createNewFile();
                passwordConfig = YamlConfiguration.loadConfiguration(passwordFile);
                passwordConfig.save(passwordFile);
            } catch (IOException e) {
                getLogger().severe("无法创建密码文件: " + e.getMessage());
            }
        } else {
            passwordConfig = YamlConfiguration.loadConfiguration(passwordFile);
            loadEncryptedPasswords();
        }
    }

    private void loadEncryptedPasswords() {
        for (String key : passwordConfig.getKeys(false)) {
            String encryptedPassword = passwordConfig.getString(key);
            String decryptedPassword = EncryptionUtil.decrypt(encryptedPassword);
            if (decryptedPassword != null) {
                // 使用小写玩家名作为键，实现大小写不敏感匹配
                playerPasswords.put(key.toLowerCase(), decryptedPassword);
            }
        }
        // Creator: Steve (619002007)
    }

    private void saveEncryptedPassword(String playerName, String password) {
        String encryptedPassword = EncryptionUtil.encrypt(password);
        if (encryptedPassword != null) {
            // 使用小写玩家名作为键，实现大小写不敏感匹配
            passwordConfig.set(playerName.toLowerCase(), encryptedPassword);
            try {
                passwordConfig.save(passwordFile);
            } catch (IOException e) {
                getLogger().severe("无法保存密码文件: " + e.getMessage());
            }
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("ServerCalibrate plugin has been disabled!");
        for (BukkitTask task : verificationTasks.values()) {
            if (task != null && !task.isCancelled()) {
                task.cancel();
            }
        }
        // B站世一史蒂夫制作
    }

    private void registerCommands() {
        PluginCommand scsetCommand = getCommand("scset");
        if (scsetCommand != null) {
            scsetCommand.setExecutor(new ScSetCommandExecutor());
            scsetCommand.setTabCompleter(new ScSetTabCompleter());
        }

        PluginCommand scCommand = getCommand("sc");
        if (scCommand != null) {
            scCommand.setExecutor(new ScCommandExecutor());
        }

        PluginCommand serversclCommand = getCommand("serverscl");
        if (serversclCommand != null) {
            serversclCommand.setExecutor(new ServerSclCommandExecutor());
        }

        PluginCommand playersclCommand = getCommand("playerscl");
        if (playersclCommand != null) {
            playersclCommand.setExecutor(new PlayerSclCommandExecutor());
        }
    }

    // /serverscl指令执行器 - 服务器默认语言设置
    private class ServerSclCommandExecutor implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            // 检查是否为控制台或拥有服务器管理员权限
            if (!(sender instanceof Player)) {
                // 控制台可以执行
            } else if (((Player)sender).isOp()) {
                // OP玩家可以执行
            } else {
                sender.sendMessage(ChatColor.RED + "只有OP玩家或服务器后台可以执行此命令!");
                return true;
            }

            // 检查参数
            if (args.length < 1) {
                sender.sendMessage(ChatColor.RED + "/serverscl <语言> - 可用语言: zh-cn, zh-tr, en");
                return true;
            }

            String lang = args[0].toLowerCase();
            // 检查语言是否支持
            if (Arrays.asList("zh-cn", "zh-tr", "en").contains(lang)) {
                serverDefaultLanguage = lang;
                sender.sendMessage(ChatColor.GREEN + "服务器默认语言已设置为: " + lang);
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "不支持的语言! 可用语言: zh-cn, zh-tr, en");
                return true;
            }
        }
    }

    // /playerscl指令执行器 - 玩家个人语言设置
    private class PlayerSclCommandExecutor implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            // 检查是否为玩家
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "只有玩家可以执行此命令!");
                return true;
            }

            Player player = (Player) sender;
            String playerName = player.getName();
            String lowerCaseName = playerName.toLowerCase();

            // 检查参数
            if (args.length < 1) {
                player.sendMessage(ChatColor.RED + "/playerscl <语言> - 可用语言: zh-cn, zh-tr, en");
                return true;
            }

            String lang = args[0].toLowerCase();
            // 检查语言是否支持
            if (Arrays.asList("zh-cn", "zh-tr", "en").contains(lang)) {
                playerLanguage.put(lowerCaseName, lang);
                // 保存到配置文件
                playerLangConfig.set(lowerCaseName, lang);
                try {
                    playerLangConfig.save(playerLangFile);
                } catch (IOException e) {
                    getLogger().severe("无法保存玩家语言设置: " + e.getMessage());
                }
                player.sendMessage(ChatColor.GREEN + "个人语言已切换为: " + lang);
                return true;
            } else {
                player.sendMessage(ChatColor.RED + "不支持的语言! 可用语言: zh-cn, zh-tr, en");
                return true;
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        String lowerCaseName = playerName.toLowerCase();

        // 使用小写玩家名进行查找，实现大小写不敏感匹配
        if (playerPasswords.containsKey(lowerCaseName)) {
            playerVerified.put(lowerCaseName, false);
            playerFailedAttempts.put(lowerCaseName, 0);
            applyVerificationEffects(player);
            createVerificationBossBar(player);
            startVerificationTimeoutTask(player);
            updateVerificationBossBar(player, VERIFICATION_TIMEOUT, 3);
            String message = getMessage(lowerCaseName, "verification_required", null);
            player.sendMessage(ChatColor.RED + message);
        } else {
            String kickMessage = getNoPasswordKickMessage(playerName);
            player.kickPlayer(kickMessage);
        }
    }

    // 生成未设置密码的踢出消息
    private String getNoPasswordKickMessage(String playerName) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("server", serverName);
        placeholders.put("player", playerName);
        placeholders.put("qq", showQq ? ownerQq : "");
        placeholders.put("email", showEmail ? ownerEmail : "");
        
        return getMessage(playerName.toLowerCase(), "no_password_kick", placeholders);
    }

    // 生成封禁的踢出消息
    private String getBanKickMessage(String playerName) {
        String message = getMessage(playerName, "banned", null);
        return ChatColor.RED + message;
    }

    // 创建验证BossBar
    private void createVerificationBossBar(Player player) {
        String playerName = player.getName();
        String lowerCaseName = playerName.toLowerCase();
        int remainingAttempts = 3 - playerFailedAttempts.getOrDefault(lowerCaseName, 0);

        // 创建BossBar
        BossBar bossBar = Bukkit.createBossBar(
                ChatColor.RED + "请在" + VERIFICATION_TIMEOUT + "秒内验证! 剩余" + remainingAttempts + "次可验证",
                BarColor.RED,
                BarStyle.SOLID
        );
        bossBar.addPlayer(player);
        bossBar.setVisible(true);
        bossBar.setProgress(1.0); // 初始进度为100%

        // 存储BossBar
        playerBossBars.put(lowerCaseName, bossBar);
    }

    // 更新BossBar
    private void updateVerificationBossBar(Player player, int remainingSeconds, int remainingAttempts) {
        String playerName = player.getName();
        String lowerCaseName = playerName.toLowerCase();
        BossBar bossBar = playerBossBars.get(lowerCaseName);
        if (bossBar != null) {
            bossBar.setTitle(ChatColor.RED + "请在" + remainingSeconds + "秒内验证! 剩余" + remainingAttempts + "次可验证");
            bossBar.setProgress((double) remainingSeconds / VERIFICATION_TIMEOUT);
        }
    }

    // 移除BossBar
    private void removeVerificationBossBar(Player player) {
        String playerName = player.getName();
        String lowerCaseName = playerName.toLowerCase();
        BossBar bossBar = playerBossBars.remove(lowerCaseName);
        if (bossBar != null) {
            bossBar.removeAll();
        }
    }

    // 启动验证计时任务
    private void startVerificationTimeoutTask(Player player) {
        String playerName = player.getName();
        String lowerCaseName = playerName.toLowerCase();
        int remainingAttempts = 3 - playerFailedAttempts.getOrDefault(lowerCaseName, 0);

        // 取消现有任务（如果有）
        BukkitTask existingTask = verificationTasks.remove(lowerCaseName);
        if (existingTask != null && !existingTask.isCancelled()) {
            existingTask.cancel();
        }

        // 创建新任务
        BukkitTask task = new BukkitRunnable() {
            int remainingSeconds = VERIFICATION_TIMEOUT;

            @Override
            public void run() {
                // 如果玩家已验证或离线，则取消任务
                if (!player.isOnline() || playerVerified.getOrDefault(lowerCaseName, false)) {
                    removeVerificationBossBar(player);
                    cancel();
                    return;
                }

                // 更新BossBar
                updateVerificationBossBar(player, remainingSeconds, remainingAttempts);

                // 检查是否超时
                if (remainingSeconds <= 0) {
                    String message = getMessage(lowerCaseName, "verification_timeout", null);
                    player.kickPlayer(ChatColor.RED + message);
                    removeVerificationBossBar(player);
                    // 重置玩家状态
                    resetPlayerState(lowerCaseName);
                    cancel();
                    return;
                }

                remainingSeconds--;
            }
        }.runTaskTimer(this, 0L, 20L); // 每秒执行一次（20tick = 1秒）

        // 存储任务
        verificationTasks.put(lowerCaseName, task);
    }

    // 重置玩家验证状态
    private void resetPlayerState(String playerName) {
        playerVerified.remove(playerName);
        playerFailedAttempts.remove(playerName);
        verificationTasks.remove(playerName);
        Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            removeVerificationBossBar(player);
        }
    }

    // 施加验证效果
    private void applyVerificationEffects(Player player) {
        // 失明效果
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 100, false, false));
        // 缓慢效果
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 100, false, false));
    }

    // 阻止未验证玩家移动
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName().toLowerCase();

        if (!playerVerified.getOrDefault(playerName, true)) {
            event.setCancelled(true);
        }
    }

    // 阻止未验证玩家使用物品
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName().toLowerCase();

        if (!playerVerified.getOrDefault(playerName, true)) {
            event.setCancelled(true);
        }
    }

    // 阻止未验证玩家丢弃物品
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName().toLowerCase();

        if (!playerVerified.getOrDefault(playerName, true)) {
            event.setCancelled(true);
        }
    }

    // 阻止未验证玩家点击物品栏
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            String playerName = player.getName().toLowerCase();

            if (!playerVerified.getOrDefault(playerName, true)) {
                event.setCancelled(true);
            }
        }
    }

    // 阻止未验证玩家破坏方块
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName().toLowerCase();

        if (!playerVerified.getOrDefault(playerName, true)) {
            event.setCancelled(true);
        }
    }

    // 阻止未验证玩家放置方块
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName().toLowerCase();

        if (!playerVerified.getOrDefault(playerName, true)) {
            event.setCancelled(true);
        }
    }

    // 移除验证效果
    private void removeVerificationEffects(Player player) {
        player.removePotionEffect(PotionEffectType.BLINDNESS);
        player.removePotionEffect(PotionEffectType.SLOW);
    }

    // /scset指令执行器
    private class ScSetCommandExecutor implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            // 允许服务器后台执行
            if (!(sender instanceof Player)) {
                // 服务器后台逻辑
                if (args.length < 2) {
                    sender.sendMessage("用法: /scset <用户名> <密码>");
                    return true;
                }

                String targetPlayer = args[0].toLowerCase();
                String password = args[1];

                // 存储加密后的密码
                saveEncryptedPassword(targetPlayer, password);
                // 更新内存中的密码
                playerPasswords.put(targetPlayer, password);
                sender.sendMessage("已设置玩家 " + args[0] + " 的密码!");
                return true;
            }

            Player player = (Player) sender;
            // 检查OP等级是否为4或更高
            if (player.isOp() && player.getLevel() >= 4) {
                // 检查参数
                if (args.length < 2) {
                    String message = getMessage(player.getName(), "command_usage_scset", null);
                    player.sendMessage(ChatColor.RED + message);
                    return true;
                }

                String targetPlayer = args[0].toLowerCase();
                String password = args[1];

                // 存储加密后的密码
                saveEncryptedPassword(targetPlayer, password);
                // 更新内存中的密码
                playerPasswords.put(targetPlayer, password);
                Map<String, String> placeholders = new HashMap<>();
                placeholders.put("player", args[0]);
                String message = getMessage(player.getName(), "password_set", placeholders);
                player.sendMessage(ChatColor.GREEN + message);
                return true;
            } else {
                String message = getMessage(player.getName(), "no_permission", null);
                player.sendMessage(ChatColor.RED + message);
                return true;
            }
        }
    }

    // /scset指令Tab补全
    private class ScSetTabCompleter implements TabCompleter {
        @Override
        public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
            if (args.length == 1) {
                // 补全在线玩家名称
                List<String> playerNames = new ArrayList<>();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    playerNames.add(player.getName());
                }
                return playerNames;
            } else if (args.length == 2) {
                // 密码不提供补全
                return Collections.singletonList("<密码>");
            }
            return Collections.emptyList();
        }
    }

    // /sc指令执行器
    private class ScCommandExecutor implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            // 检查是否为玩家
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "只有玩家可以执行此命令!");
                return true;
            }

            Player player = (Player) sender;
            String playerName = player.getName();
            String lowerCaseName = playerName.toLowerCase();
            String ipAddress = player.getAddress().getAddress().getHostAddress();

            // 检查玩家是否需要验证（使用小写玩家名进行查找）
            if (!playerPasswords.containsKey(lowerCaseName)) {
                player.sendMessage(ChatColor.RED + "你不需要验证!");
                return true;
            }

            // 检查是否已经验证
            if (playerVerified.getOrDefault(lowerCaseName, false)) {
                player.sendMessage(ChatColor.RED + "你已经通过验证!");
                return true;
            }

            // 检查参数
            if (args.length < 1) {
                String message = getMessage(lowerCaseName, "command_usage_sc", null);
                player.sendMessage(ChatColor.RED + message);
                return true;
            }

            String inputPassword = args[0];
            // 使用小写玩家名进行查找，实现大小写不敏感匹配
            String correctPassword = playerPasswords.get(lowerCaseName);

            // 验证密码
            if (inputPassword.equalsIgnoreCase(correctPassword)) {
                // 验证成功
            playerVerified.put(lowerCaseName, true);
            removeVerificationEffects(player);
            String message = getMessage(lowerCaseName, "verification_success", null);
            player.sendMessage(ChatColor.GREEN + message);
            
            // 显示金色语言设置提示
            player.sendMessage(ChatColor.GOLD + "输入/playerscl可以设置自己的语言（简体中文、繁体中文、英语）");
            player.sendMessage(ChatColor.GOLD + "Type /playerscl to set your language (Simplified Chinese, Traditional Chinese, English)");
            player.sendMessage(ChatColor.GOLD + "輸入/playerscl可以設定自己的語言（簡體中文、繁體中文、英語）");
            
            // 清除失败次数
            playerFailedAttempts.remove(lowerCaseName);
            return true;
            } else {
                // 验证失败
                int failedAttempts = playerFailedAttempts.getOrDefault(lowerCaseName, 0) + 1;
                playerFailedAttempts.put(lowerCaseName, failedAttempts);

                // 更新IP错误次数
                int ipAttempts = ipFailedAttempts.getOrDefault(ipAddress, 0) + 1;
                ipFailedAttempts.put(ipAddress, ipAttempts);

                Map<String, String> placeholders = new HashMap<>();
                placeholders.put("remaining", String.valueOf(3 - failedAttempts));
                String message = getMessage(lowerCaseName, "incorrect_password", placeholders);
                player.sendMessage(ChatColor.RED + message);

                // 检查是否达到踢出次数
                if (failedAttempts >= 3) {
                    message = getMessage(lowerCaseName, "too_many_attempts", null);
                    player.kickPlayer(ChatColor.RED + message);
                }

                // 检查是否达到Ban IP次数
                if (ipAttempts >= 10) {
                    // 永久Ban IP
                    Bukkit.getBanList(BanList.Type.IP).addBan(ipAddress, "密码错误次数过多 (永久封禁)", null, "ServerCalibrate");
                    String banMessage = getBanKickMessage(lowerCaseName);
                    player.kickPlayer(banMessage);
                }

                return true;
            }
        }
    }
}