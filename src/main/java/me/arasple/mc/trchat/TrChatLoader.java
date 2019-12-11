package me.arasple.mc.trchat;

import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.Files;
import me.arasple.mc.trchat.chat.ChatFormats;
import me.arasple.mc.trchat.filter.ChatFilter;
import me.arasple.mc.trchat.func.ChatFunctions;
import me.arasple.mc.trchat.logs.ChatLogs;
import me.arasple.mc.trchat.updater.Updater;
import me.arasple.mc.trchat.utils.Bungees;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @author Arasple
 * @date 2019/11/29 15:05
 */
public class TrChatLoader {

    private List<String> motd = Arrays.asList(
            "",
            "§3  ___________      _________ .__            __",
            "§3  \\__    __________\\_   ___ \\|  |__ _____ _/  |_",
            "§3  |    |  \\_  __ /    \\  \\/|  |  \\\\__  \\\\   __\\",
            "§3  |    |   |  | \\\\     \\___|   Y  \\/ __ \\|  |",
            "§3  |____|   |__|   \\______  |___|  (____  |__|",
            "§3  \\/     \\/     \\/      ");

    void init() {
        if (isAbandoned()) {
            return;
        }

        if (!new File(TrChat.getPlugin().getDataFolder(), "do_not_notify").exists()) {
            motd.forEach(l -> Bukkit.getConsoleSender().sendMessage(l));
            TLocale.sendToConsole("PLUGIN.LOADED");
        }

        if (hookPlaceholderAPI()) {
            return;
        }

        // Chat Filter
        ChatFilter.loadFilter(true, Bukkit.getConsoleSender());
        // Chat Formats
        ChatFormats.loadFormats(Bukkit.getConsoleSender());
        // Chat Functions
        ChatFunctions.loadFunctions(Bukkit.getConsoleSender());
    }


    void load() {
        if (!new File(TrChat.getPlugin().getDataFolder(), "do_not_notify").exists()) {
            TLocale.sendToConsole("PLUGIN.ENABLED", TrChat.getPlugin().getDescription().getVersion());
        }
    }

    void unload() {
        if (!new File(TrChat.getPlugin().getDataFolder(), "do_not_notify").exists()) {
            TLocale.sendToConsole("PLUGIN.DISABLED");
        }
    }

    /**
     * 判断是否为低于 2.0 的旧版本
     *
     * @return result
     */
    private boolean isAbandoned() {
        if (new File("plugins/TrChat/functions.yml").exists()) {
            Arrays.asList(
                    "§8--------------------------------------------------",
                    "§r",
                    "§8# §4为了您能够顺利升级到 §cTrChat 1.6 §4,",
                    "§8# §4需要备份并删除旧的 TrChat 文件夹. 然后重新启动",
                    "§8# §r",
                    "§8# §4本次将关闭服务器...",
                    "§r",
                    "§8--------------------------------------------------"
            ).forEach(l -> Bukkit.getConsoleSender().sendMessage(l));
            try {
                Thread.sleep(5 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Bukkit.shutdown();
            return true;
        }
        return false;
    }

    /**
     * 检测前置 PlaceholderAPI
     * 并自动下载、重启服务器
     */
    private boolean hookPlaceholderAPI() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
        File jarFile = new File("plugins/PlaceholderAPI.jar");
        String url = "https://ci.extendedclip.com/job/PlaceholderAPI/lastSuccessfulBuild/artifact/target/PlaceholderAPI-2.10.4.jar";

        if (plugin == null) {
            jarFile.delete();
            TLocale.sendToConsole("PLUGIN.DEPEND.DOWNLOAD", "PlaceholderAPI");
            if (Files.downloadFile(url, jarFile)) {
                TLocale.sendToConsole("PLUGIN.DEPEND.INSTALL", "PlaceholderAPI");
                Bukkit.shutdown();
            } else {
                TLocale.sendToConsole("PLUGIN.DEPEND.INSTALL-FAILED", "PlaceholderAPI");
                Bukkit.shutdown();
            }
            return true;
        }
        return false;
    }

}
