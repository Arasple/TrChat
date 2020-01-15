package me.arasple.mc.trchat;

import io.izzel.taboolib.module.config.TConfig;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.inject.TSchedule;
import me.arasple.mc.trchat.chat.ChatFormats;
import me.arasple.mc.trchat.filter.ChatFilter;
import me.arasple.mc.trchat.func.ChatFunctions;

/**
 * @author Arasple
 * @date 2019/11/30 9:59
 */
public class TrChatFiles {

    @TInject(value = "settings.yml", locale = "LOCALE-PRIORITY")
    private static TConfig settings;
    @TInject("formats.yml")
    private static TConfig formats;
    @TInject("filter.yml")
    private static TConfig filter;
    @TInject("function.yml")
    private static TConfig function;
    @TInject("channels.yml")
    private static TConfig channels;

    @TSchedule
    public static void init() {
        filter.listener(() -> ChatFilter.loadFilter(false)).runListener();
        formats.listener(ChatFormats::loadFormats).runListener();
        function.listener(ChatFunctions::loadFunctions).runListener();
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
