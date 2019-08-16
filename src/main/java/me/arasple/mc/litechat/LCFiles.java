package me.arasple.mc.litechat;

import io.izzel.taboolib.module.config.TConfig;
import io.izzel.taboolib.module.inject.TFunction;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.inject.TSchedule;
import io.izzel.taboolib.module.locale.TLocale;
import me.arasple.mc.litechat.data.DataHandler;
import me.arasple.mc.litechat.filter.WordFilter;

import java.io.IOException;

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
        long purgeDays = settings.getLong("General.purge", 30);
        if (purgeDays != -1 && data.isSet("UserData")) {
            int count = 0;
            for (String u : data.getConfigurationSection("UserData").getKeys(false)) {
                long lastOnline = data.getLong("UserData." + u + ".last-online", System.currentTimeMillis());
                if (System.currentTimeMillis() - lastOnline > purgeDays * 24 * 60 * 60 * 1000) {
                    data.set("UserData." + u, null);
                    count++;
                }
            }
            if (count > 0) {
                TLocale.sendToConsole("GENERAL.PURGE", String.valueOf(count));
            }
        }
    }

    @TSchedule(delay = 20 * 10, period = 30 * 20)
    public static void save() {
        DataHandler.getCooldowns().forEach((key, value) -> data.set("UserData." + key + ".cooldowns", value.write()));

        try {
            data.save(data.getFile());
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
