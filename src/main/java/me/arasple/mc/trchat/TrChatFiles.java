package me.arasple.mc.trchat;

import io.izzel.taboolib.module.config.TConfig;
import io.izzel.taboolib.module.inject.TFunction;
import io.izzel.taboolib.module.inject.TInject;
import me.arasple.mc.trchat.chat.ChatFormats;
import me.arasple.mc.trchat.filter.ChatFilter;
import me.arasple.mc.trchat.func.ChatFunctions;
import org.bukkit.Bukkit;

/**
 * @author Arasple
 * @date 2019/11/30 9:59
 */
public class TrChatFiles {

    @TInject("settings.yml")
    private static TConfig settings;
    @TInject("formats.yml")
    private static TConfig formats;
    @TInject("filter.yml")
    private static TConfig filter;
    @TInject("function.yml")
    private static TConfig function;
    @TInject("channels.yml")
    private static TConfig channels;

    @TFunction.Init
    public static void init() {
        filter.listener(() -> ChatFilter.loadFilter(false, Bukkit.getConsoleSender())).runListener();
        formats.listener(() -> ChatFormats.loadFormats(Bukkit.getConsoleSender())).runListener();
        function.listener(() -> ChatFunctions.loadFunctions(Bukkit.getConsoleSender())).runListener();
    }

    public static TConfig getSettings() {
        return settings;
    }

    public static TConfig getFormats() {
        return formats;
    }

    public static TConfig getFilter() {
        return filter;
    }

    public static TConfig getFunction() {
        return function;
    }

    public static TConfig getChannels() {
        return channels;
    }

}
