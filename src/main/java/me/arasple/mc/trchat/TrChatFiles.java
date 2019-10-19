package me.arasple.mc.trchat;

import io.izzel.taboolib.module.config.TConfig;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.inject.TSchedule;
import io.izzel.taboolib.module.locale.TLocale;
import me.arasple.mc.trchat.filter.ChatFilter;
import me.arasple.mc.trchat.formats.ChatFormats;
import org.bukkit.Bukkit;

/**
 * @author Arasple
 * @date 2019/10/12 18:58
 */
public class TrChatFiles {

    @TInject("settings.yml")
    private static TConfig settings;
    @TInject("formats.yml")
    private static TConfig formats;
    @TInject("filter.yml")
    private static TConfig filter;
    @TInject("functions.yml")
    private static TConfig functions;

    @TSchedule
    public static void init() {
        int cfgVer = settings.getInt("GENERAL.CONFIG-VERSION", -1);
        if (cfgVer != -1) {
            TrChat.getTLogger().warn("&6--------------------------------------------------");
            TrChat.getTLogger().warn("");
            TrChat.getTLogger().warn("&4由于该版本内容较大,");
            TrChat.getTLogger().warn("&4请备份您的 &cTrChat &4文件夹, 并删除. 再重启服务器即可完成更新!");
            TrChat.getTLogger().warn("");
            TrChat.getTLogger().warn("&7TrChat 卸载中....");
            TrChat.getTLogger().warn("&6--------------------------------------------------");
            Bukkit.getPluginManager().disablePlugin(TrChat.getPlugin());
        }

        settings.listener(TrChatFiles::reloadFiles);
        formats.listener(TrChatFiles::reloadFiles);
        filter.listener(TrChatFiles::reloadFiles);
        functions.listener(TrChatFiles::reloadFiles);
    }

    /**
     * 重载相关配置文件
     */
    public static void reloadFiles() {
        ChatFilter.loadFilter(false);
        ChatFormats.loadFormats();
        TLocale.sendToConsole("PLUGIN.RELOADED");
    }

    /*
    GETTERS
     */

    public static TConfig getSettings() {
        return settings;
    }

    public static TConfig getFormats() {
        return formats;
    }

    public static TConfig getFilter() {
        return filter;
    }

    public static TConfig getFunctions() {
        return functions;
    }

}
