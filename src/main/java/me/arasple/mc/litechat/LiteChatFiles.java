package me.arasple.mc.litechat;

import io.izzel.taboolib.module.config.TConfig;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.inject.TSchedule;
import io.izzel.taboolib.module.locale.TLocale;
import me.arasple.mc.litechat.filter.ChatFilter;
import me.arasple.mc.litechat.formats.ChatFormats;
import org.bukkit.Bukkit;

/**
 * @author Arasple
 * @date 2019/10/12 18:58
 */
public class LiteChatFiles {

    @TInject("settings.yml")
    private static TConfig settings;
    @TInject("formats.yml")
    private static TConfig formats;
    @TInject("filter.yml")
    private static TConfig filter;
    @TInject("funcitons.yml")
    private static TConfig funcitons;

    @TSchedule
    public static void init() {
        int cfgVer = settings.getInt("GENERAL.CONFIG-VERSION", -1);
        if (cfgVer != -1) {
            LiteChat.getTLogger().warn("&6--------------------------------------------------");
            LiteChat.getTLogger().warn("");
            LiteChat.getTLogger().warn("&4由于该版本内容较大,");
            LiteChat.getTLogger().warn("&4请备份您的 &cLiteChat &4文件夹, 并删除. 再重启服务器即可完成更新!");
            LiteChat.getTLogger().warn("");
            LiteChat.getTLogger().warn("&7LiteChat 卸载中....");
            LiteChat.getTLogger().warn("&6--------------------------------------------------");
            Bukkit.getPluginManager().disablePlugin(LiteChat.getPlugin());
        }

        settings.listener(LiteChatFiles::reloadFiles);
        formats.listener(LiteChatFiles::reloadFiles);
        filter.listener(LiteChatFiles::reloadFiles);
        funcitons.listener(LiteChatFiles::reloadFiles);
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

    public static TConfig getFuncitons() {
        return funcitons;
    }

}
