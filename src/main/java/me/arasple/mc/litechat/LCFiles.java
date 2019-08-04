package me.arasple.mc.litechat;

import io.izzel.taboolib.module.config.TConfig;
import io.izzel.taboolib.module.inject.TFunction;
import io.izzel.taboolib.module.inject.TInject;
import me.arasple.mc.litechat.filter.WordFilter;

/**
 * @author Arasple
 * @date 2019/7/31 14:33
 */
@TFunction(enable = "load", disable = "unload")
public class LCFiles {

    @TInject("settings.yml")
    private static TConfig settings;

    static void load() {
        settings.listener(() -> {
            WordFilter.loadSettings();

            LiteChat.getTLogger().fine("&7重新载入配置...");
        });
    }

    static void unload() {

    }

    public static TConfig getSettings() {
        return settings;
    }

}
