package me.arasple.mc.litechat;

import io.izzel.taboolib.module.config.TConfig;
import io.izzel.taboolib.module.inject.TFunction;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.inject.TSchedule;
import me.arasple.mc.litechat.data.DataHandler;
import me.arasple.mc.litechat.filter.WordFilter;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Arasple
 * @date 2019/7/31 14:33
 */
@TFunction(enable = "load", disable = "unload")
public class LCFiles {

    @TInject("settings.yml")
    private static TConfig settings;
    @TInject("data.yml")
    private static TConfig data;
//    @TInject("logs.yml")
//    private static TConfig logs;

    static void load() {
        settings.listener(() -> {
            WordFilter.loadSettings();
            LiteChat.getTLogger().fine("&7重新载入配置...");
        });
    }

    static void unload() {
        purgeData();
        save();
    }

    @TSchedule(delay = 5)
    private static void purgeData() {
        int purgeDays = settings.getInt("General", 30);
        if (purgeDays != -1) {
            AtomicInteger number = new AtomicInteger();
            data.getConfigurationSection("UserData").getKeys(false).forEach(u -> {
                long lastOnline = data.getLong("UserData." + u + ".last-online", System.currentTimeMillis());
                if (System.currentTimeMillis() - lastOnline > purgeDays * 24 * 60 * 60 * 1000) {
                    data.set("UserData." + u, null);
                    number.getAndIncrement();
                }
            });
            if (number.get() > 0) {
                LiteChat.getTLogger().fine("&3清理了 &a" + number.get() + " &3个不活跃用户数据...");
            }
        } else {
            LiteChat.getTLogger().info("不活跃清理系统已被禁用...");
        }
    }

    @TSchedule(delay = 20 * 10, period = 30 * 20)
    public static void save() {
        DataHandler.getCooldowns().forEach((key, value) -> data.set("UserData." + key + ".cooldowns", value.write()));

        try {
            data.save(LCFiles.getData().getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static TConfig getSettings() {
        return settings;
    }

    public static TConfig getData() {
        return data;
    }

}
