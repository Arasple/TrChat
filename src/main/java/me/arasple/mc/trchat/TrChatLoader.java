package me.arasple.mc.trchat;

import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.Files;
import me.arasple.mc.trchat.chat.ChatFormats;
import me.arasple.mc.trchat.filter.ChatFilter;
import me.arasple.mc.trchat.func.ChatFunctions;
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
        motd.forEach(l -> Bukkit.getConsoleSender().sendMessage(l));
        TLocale.sendToConsole("PLUGIN.LOADED");

        if (hookPlaceholderAPI()) {
            return;
        }
        // Updater
        Updater.init(TrChat.getPlugin());
        // Chat Filter
        ChatFilter.loadFilter(true, Bukkit.getConsoleSender());
        // Chat Formats
        ChatFormats.loadFormats(Bukkit.getConsoleSender());
        // Chat Functions
        ChatFunctions.loadFunctions(Bukkit.getConsoleSender());
        // Bungees
        Bungees.init();
    }


    void load() {
        TLocale.sendToConsole("PLUGIN.ENABLED", TrChat.getPlugin().getDescription().getVersion());
    }

    void unload() {
        TLocale.sendToConsole("PLUGIN.DISABLED");
    }

    /**
     * 检测前置 PlaceholderAPI
     * 并自动下载、重启服务器
     */
    private boolean hookPlaceholderAPI() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
        File jarFile = new File("plugins/PlaceholderAPI.jar");
        String url = "https://api.spiget.org/v2/resources/6245/download";

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
