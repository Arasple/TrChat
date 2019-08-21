package me.arasple.mc.litechat;

import io.izzel.taboolib.module.config.TConfig;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.module.locale.logger.TLogger;
import io.izzel.taboolib.util.Files;
import me.arasple.mc.litechat.bstats.Metrics;
import me.arasple.mc.litechat.filter.WordFilter;
import me.arasple.mc.litechat.formats.ChatFormats;
import me.arasple.mc.litechat.utils.BungeeUtils;

import java.io.File;

/**
 * @author Arasple
 */
@LiteChatPlugin.Version(5.03)
public final class LiteChat extends LiteChatPlugin {

    @TInject
    private static LiteChat instance;
    @TInject("§3L§bChat")
    private static TLogger logger;
    @TInject(value = "settings.yml")
    private static TConfig settings;

    static void configUpdate() {
        int cfgVer = settings.getInt("GENERAL.CONFIG-VERSION", -1);
        if (cfgVer < 1) {
            Files.copy(settings.getFile(), new File(getInst().getDataFolder(), "settings-backup.yml"));
            Files.deepDelete(settings.getFile());
            settings = TConfig.create(getInst(), "settings.yml");
            TLocale.sendToConsole("PLUGIN.CONFIG.UPDATED");
        } else if (cfgVer != 2) {
            switch (cfgVer) {
                case 1:
                    settings.set("CHAT-CONTROL.COLOR-CODE.BOOK", true);
                    settings.set("GENERAL.CONFIG-VERSION", 2);
                    settings.saveToFile();
                    break;
                default:
                    break;
            }
        } else {
            TLocale.sendToConsole("PLUGIN.CONFIG.LATEST");
        }
        settings.listener(LiteChat::configReload);
        configReload();
    }

    static void configReload() {
        WordFilter.loadSettings();
        ChatFormats.load(false);
        TLocale.sendToConsole("PLUGIN.CONFIG.RELOADED");
    }

    @Override
    public void onStarting() {
        configUpdate();

        BungeeUtils.init(this);
        new Metrics(this);

        TLocale.sendToConsole("PLUGIN.ENABLED", getDescription().getVersion());
    }

    @Override
    public void onLoading() {
        TLocale.sendToConsole("PLUGIN.LOADED");
    }

    @Override
    public void onStopping() {
        TLocale.sendToConsole("PLUGIN.DISABLED");
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

    public static boolean isDebug() {
        return getSettings().getBoolean("GENERAL.DEBUG");
    }

    public static boolean toggleDebug() {
        getSettings().set("GENERAL.DEBUG", !getSettings().getBoolean("GENERAL.DEBUG"));
        return getSettings().getBoolean("GENERAL.DEBUG");
    }

}