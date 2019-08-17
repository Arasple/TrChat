package me.arasple.mc.litechat;

import io.izzel.taboolib.module.config.TConfig;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.inject.TSchedule;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.module.locale.logger.TLogger;
import me.arasple.mc.litechat.bstats.Metrics;
import me.arasple.mc.litechat.data.DataHandler;
import me.arasple.mc.litechat.filter.WordFilter;
import me.arasple.mc.litechat.formats.ChatFormats;
import me.arasple.mc.litechat.utils.BungeeUtils;

import java.io.IOException;

/**
 * @author Arasple
 */
@LiteChatPlugin.Version(5.03)
public final class LiteChat extends LiteChatPlugin {

    @TInject
    private static LiteChat instance;
    @TInject("§3L§bChat")
    private static TLogger logger;
    @TInject("settings.yml")
    private static TConfig settings;
    @TInject("data.yml")
    private static TConfig data;

    @Override
    public void onLoading() {
        TLocale.sendToConsole("PLUGIN.LOADED");

        settings.listener(() -> {
            WordFilter.loadSettings();
            ChatFormats.load(false);
            LiteChat.getTLogger().fine("&7重新载入配置...");
        });
    }

    @Override
    public void onStarting() {
        BungeeUtils.init(this);
        new Metrics(this);

        TLocale.sendToConsole("PLUGIN.ENABLED", getDescription().getVersion());
    }

    @Override
    public void onActivated() {
        ChatFormats.load(true);
    }


    @Override
    public void onStopping() {
        purgeData();
        save();

        TLocale.sendToConsole("PLUGIN.DISABLED");
    }

    @TSchedule(delay = 5)
    private static void purgeData() {
        long purgeDays = settings.getLong("GENERAL.PURGE", 30);
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
        DataHandler.getCooldowns().forEach((key, value) -> data.set("UserData." + key + ".COOLDOWNs", value.write()));

        try {
            data.save(data.getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static TLogger getTLogger() {
        return logger;
    }

    public static LiteChat getInst() {
        return instance;
    }

    public static TConfig getSettings() {
        return settings;
    }

    public static TConfig getData() {
        return data;
    }

    public static boolean isDebug() {
        return getSettings().getBoolean("GENERAL.DEBUG");
    }

    public static boolean switchDebug() {
        boolean state = getSettings().getBoolean("GENERAL.DEBUG");
        state = !state;
        getSettings().set("GENERAL.DEBUG", state);
        return state;
    }

}
