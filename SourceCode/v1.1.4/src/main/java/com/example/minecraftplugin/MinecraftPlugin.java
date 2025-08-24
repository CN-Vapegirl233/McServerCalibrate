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
    
    // Steve's secret code: 619002007

    // 世一史蒂夫制作
    private static final int[] AUTHOR_INFO = {0x4E16, 0x4E00, 0x53F4, 0x7EF4, 0x5236, 0x4F5C, 0x0020, 0x5305, 0x542B, 0x0055, 0x0049, 0x0044, 0x0036, 0x0031, 0x0039, 0x0030, 0x0030, 0x0032, 0x0030, 0x0030, 0x0037};

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
        System.out.println("当前文件为Minecraft Java版 1.20.4服务端插件。请在服务端内运行！");
        // Hidden signature: 世一史蒂夫
    }

    private void initConfig() {
        File dataFolder = new File(getDataFolder(), "ServerCalibrate");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        configFile = new File(dataFolder, "sc.yml");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                config = YamlConfiguration.loadConfiguration(configFile);
                config.set("servername", "My Minecraft Server");
                config.set("ownerqq", "123456789");
                config.set("owneremail", "admin@example.com");
                config.set("showqq", true);
                config.set("showemail", true);
                config.save(configFile);
            } catch (IOException e) {
                getLogger().severe("无法创建配置文件: " + e.getMessage());
            }
        } else {
            config = YamlConfiguration.loadConfiguration(configFile);
        }

        serverName = config.getString("servername", "My Minecraft Server");
        ownerQq = config.getString("ownerqq", "123456789");
        ownerEmail = config.getString("owneremail", "admin@example.com");

        String showQqStr = config.getString("showqq", "true").toLowerCase();
        showQq = "true".equals(showQqStr);

        String showEmailStr = config.getString("showemail", "true").toLowerCase();
        showEmail = "true".equals(showEmailStr);

        getLogger().info("配置已加载: 服务器名称 - " + serverName);
        // UID:619002007
    }

    private void initPasswordFile() {
        File dataFolder = new File(getDataFolder(), "ServerCalibrate");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        passwordFile = new File(dataFolder, "pp.yml");
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
                playerPasswords.put(key, decryptedPassword);
            }
        }
        // Creator: Steve (619002007)
    }

    private void saveEncryptedPassword(String playerName, String password) {
        String encryptedPassword = EncryptionUtil.encrypt(password);
        if (encryptedPassword != null) {
            passwordConfig.set(playerName, encryptedPassword);
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
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();

        if (playerPasswords.containsKey(playerName)) {
            playerVerified.put(playerName, false);
            playerFailedAttempts.put(playerName, 0);
            applyVerificationEffects(player);
            createVerificationBossBar(player);
            startVerificationTimeoutTask(player);
            updateVerificationBossBar(player, VERIFICATION_TIMEOUT, 3);
            player.sendMessage(ChatColor.RED + "请输入密码进行验证: /sc <密码>");
        } else {
            String kickMessage = getNoPasswordKickMessage(playerName);
            player.kickPlayer(kickMessage);
        }
    }

    // 生成未设置密码的踢出消息
    private String getNoPasswordKickMessage(String playerName) {
        StringBuilder message = new StringBuilder();

        // 居中标题
        message.append("\n\n\n\n\n\n\n\n\n\n\n");
        message.append(ChatColor.BOLD + "                  " + ChatColor.BLUE + serverName + ChatColor.RED + " 玩家验证                  \n");
        message.append("\n");
        // 正文内容
        message.append(ChatColor.GOLD + "          你好，玩家" + ChatColor.WHITE + playerName + "，由于你未被管理员添加密码，所以无法进入服务器。\n");
        message.append("\n");
        // 联系方式
        if (showQq) {
            message.append(ChatColor.BOLD).append(ChatColor.GREEN).append("          管理员联系qq: ").append(ChatColor.WHITE).append(ownerQq).append("\n");
        }
        if (showEmail) {
            message.append(ChatColor.BOLD).append(ChatColor.GREEN).append("          管理员联系邮箱: ").append(ChatColor.WHITE).append(ownerEmail).append("\n");
        }
        message.append("\n\n\n\n\n\n\n\n\n\n");

        return message.toString();
    }

    // 生成封禁的踢出消息
    private String getBanKickMessage(String playerName) {
        StringBuilder message = new StringBuilder();

        // 居中标题
        message.append("\n\n\n\n\n\n\n\n\n\n\n");
        message.append(ChatColor.BOLD + "                  " + ChatColor.BLUE + serverName + ChatColor.RED + " 玩家验证                  \n");
        message.append("\n");
        // 正文内容
        message.append(ChatColor.GOLD + "          你好，玩家" + ChatColor.WHITE + playerName + "，由于你在1小时内密码连续输入错误10次，已被封禁！请联系管理员后台解封。\n");
        message.append("\n");
        // 联系方式
        if (showQq) {
            message.append(ChatColor.BOLD).append(ChatColor.GREEN).append("          管理员联系qq: ").append(ChatColor.WHITE).append(ownerQq).append("\n");
        }
        if (showEmail) {
            message.append(ChatColor.BOLD).append(ChatColor.GREEN).append("          管理员联系邮箱: ").append(ChatColor.WHITE).append(ownerEmail).append("\n");
        }
        message.append("\n\n\n\n\n\n\n\n\n\n");

        return message.toString();
    }

    // 创建验证BossBar
    private void createVerificationBossBar(Player player) {
        String playerName = player.getName();
        int remainingAttempts = 3 - playerFailedAttempts.getOrDefault(playerName, 0);

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
        playerBossBars.put(playerName, bossBar);
    }

    // 更新BossBar
    private void updateVerificationBossBar(Player player, int remainingSeconds, int remainingAttempts) {
        String playerName = player.getName();
        BossBar bossBar = playerBossBars.get(playerName);
        if (bossBar != null) {
            bossBar.setTitle(ChatColor.RED + "请在" + remainingSeconds + "秒内验证! 剩余" + remainingAttempts + "次可验证");
            bossBar.setProgress((double) remainingSeconds / VERIFICATION_TIMEOUT);
        }
    }

    // 移除BossBar
    private void removeVerificationBossBar(Player player) {
        String playerName = player.getName();
        BossBar bossBar = playerBossBars.remove(playerName);
        if (bossBar != null) {
            bossBar.removeAll();
        }
    }

    // 启动验证计时任务
    private void startVerificationTimeoutTask(Player player) {
        String playerName = player.getName();
        int remainingAttempts = 3 - playerFailedAttempts.getOrDefault(playerName, 0);

        // 取消现有任务（如果有）
        BukkitTask existingTask = verificationTasks.remove(playerName);
        if (existingTask != null && !existingTask.isCancelled()) {
            existingTask.cancel();
        }

        // 创建新任务
        BukkitTask task = new BukkitRunnable() {
            int remainingSeconds = VERIFICATION_TIMEOUT;

            @Override
            public void run() {
                // 如果玩家已验证或离线，则取消任务
                if (!player.isOnline() || playerVerified.getOrDefault(playerName, false)) {
                    removeVerificationBossBar(player);
                    cancel();
                    return;
                }

                // 更新BossBar
                updateVerificationBossBar(player, remainingSeconds, remainingAttempts);

                // 检查是否超时
                if (remainingSeconds <= 0) {
                    player.kickPlayer(ChatColor.RED + "验证超时! 请重新加入服务器并在规定时间内完成验证。");
                    removeVerificationBossBar(player);
                    // 重置玩家状态
                    resetPlayerState(playerName);
                    cancel();
                    return;
                }

                remainingSeconds--;
            }
        }.runTaskTimer(this, 0L, 20L); // 每秒执行一次（20tick = 1秒）

        // 存储任务
        verificationTasks.put(playerName, task);
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
        String playerName = player.getName();

        if (!playerVerified.getOrDefault(playerName, true)) {
            event.setCancelled(true);
        }
    }

    // 阻止未验证玩家使用物品
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();

        if (!playerVerified.getOrDefault(playerName, true)) {
            event.setCancelled(true);
        }
    }

    // 阻止未验证玩家丢弃物品
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();

        if (!playerVerified.getOrDefault(playerName, true)) {
            event.setCancelled(true);
        }
    }

    // 阻止未验证玩家点击物品栏
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            String playerName = player.getName();

            if (!playerVerified.getOrDefault(playerName, true)) {
                event.setCancelled(true);
            }
        }
    }

    // 阻止未验证玩家破坏方块
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();

        if (!playerVerified.getOrDefault(playerName, true)) {
            event.setCancelled(true);
        }
    }

    // 阻止未验证玩家放置方块
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();

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

                String targetPlayer = args[0];
                String password = args[1];

                // 存储加密后的密码
                saveEncryptedPassword(targetPlayer, password);
                // 更新内存中的密码
                playerPasswords.put(targetPlayer, password);
                sender.sendMessage("已设置玩家 " + targetPlayer + " 的密码!");
                return true;
            }

            Player player = (Player) sender;
            // 检查OP等级是否为4或更高
            if (player.isOp() && player.getLevel() >= 4) {
                // 检查参数
                if (args.length < 2) {
                    player.sendMessage(ChatColor.RED + "用法: /scset <用户名> <密码>");
                    return true;
                }

                String targetPlayer = args[0];
                String password = args[1];

                // 存储加密后的密码
                saveEncryptedPassword(targetPlayer, password);
                // 更新内存中的密码
                playerPasswords.put(targetPlayer, password);
                player.sendMessage(ChatColor.GREEN + "已设置玩家 " + targetPlayer + " 的密码!");
                return true;
            } else {
                player.sendMessage(ChatColor.RED + "你没有权限执行此命令!");
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
            String ipAddress = player.getAddress().getAddress().getHostAddress();

            // 检查玩家是否需要验证
            if (!playerPasswords.containsKey(playerName)) {
                player.sendMessage(ChatColor.RED + "你不需要验证!");
                return true;
            }

            // 检查是否已经验证
            if (playerVerified.getOrDefault(playerName, false)) {
                player.sendMessage(ChatColor.RED + "你已经通过验证!");
                return true;
            }

            // 检查参数
            if (args.length < 1) {
                player.sendMessage(ChatColor.RED + "用法: /sc <密码>");
                return true;
            }

            String inputPassword = args[0];
            String correctPassword = playerPasswords.get(playerName);

            // 验证密码
            if (inputPassword.equals(correctPassword)) {
                // 验证成功
                playerVerified.put(playerName, true);
                removeVerificationEffects(player);
                player.sendMessage(ChatColor.GREEN + "验证成功!");
                // 清除失败次数
                playerFailedAttempts.remove(playerName);
                return true;
            } else {
                // 验证失败
                int failedAttempts = playerFailedAttempts.getOrDefault(playerName, 0) + 1;
                playerFailedAttempts.put(playerName, failedAttempts);

                // 更新IP错误次数
                int ipAttempts = ipFailedAttempts.getOrDefault(ipAddress, 0) + 1;
                ipFailedAttempts.put(ipAddress, ipAttempts);

                player.sendMessage(ChatColor.RED + "密码错误! 剩余尝试次数: " + (3 - failedAttempts));

                // 检查是否达到踢出次数
                if (failedAttempts >= 3) {
                    player.kickPlayer(ChatColor.RED + "密码错误次数过多，请稍后再试!");
                }

                // 检查是否达到Ban IP次数
                if (ipAttempts >= 10) {
                    // 永久Ban IP
                    Bukkit.getBanList(BanList.Type.IP).addBan(ipAddress, "密码错误次数过多 (永久封禁)", null, "ServerCalibrate");
                    String banMessage = getBanKickMessage(playerName);
                    player.kickPlayer(banMessage);
                }

                return true;
            }
        }
    }
}