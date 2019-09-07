package me.arasple.mc.litechat;

import io.izzel.taboolib.module.config.TConfig;
import io.izzel.taboolib.module.db.local.LocalPlayer;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.module.locale.logger.TLogger;
import io.izzel.taboolib.util.Files;
import me.arasple.mc.litechat.filter.WordFilter;
import me.arasple.mc.litechat.formats.ChatFormats;

import java.io.File;

/**
 * @author Arasple
 */
@LiteChatPlugin.Version(5.05)
public final class LiteChat extends LiteChatPlugin {

    @TInject("§3L§bChat")
    private static TLogger logger;
    @TInject(value = "settings.yml")
    private static TConfig settings;

    @Override
    public void onStarting() {
        configUpdate();
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

    public static TConfig getSettings() {
        return settings;
    }

    public static boolean isDebug() {
        return getSettings().getBoolean("GENERAL.DEBUG");
    }

    static void configUpdate() {
        int cfgVer = settings.getInt("GENERAL.CONFIG-VERSION", -1);
        if (cfgVer < 1) {
            Files.copy(settings.getFile(), new File(getPlugin().getDataFolder(), "settings-backup.yml"));
            Files.deepDelete(settings.getFile());
            settings = TConfig.create(getPlugin(), "settings.yml");
            TLocale.sendToConsole("PLUGIN.CONFIG.UPDATED");
        } else if (cfgVer != 3) {
            switch (cfgVer) {
                case 1:
                    settings.set("CHAT-CONTROL.COLOR-CODE.BOOK", true);
                    break;
                case 2:
                    settings.set("CHAT-CONTROL.LENGTH-LIMIT", 100);
                    Files.deepDelete(LocalPlayer.getFolder());
                default:
                    break;
            }
            settings.set("GENERAL.CONFIG-VERSION", 3);
            settings.saveToFile();
        } else {
            TLocale.sendToConsole("PLUGIN.CONFIG.LATEST");
        }
        settings.listener(LiteChat::configReload);
        configReload();
    }

    static void configReload() {
        WordFilter.loadSettings();
        ChatFormats.load(true);
        TLocale.sendToConsole("PLUGIN.CONFIG.RELOADED");
    }

    public static boolean toggleDebug() {
        getSettings().set("GENERAL.DEBUG", !getSettings().getBoolean("GENERAL.DEBUG"));
        return getSettings().getBoolean("GENERAL.DEBUG");
    }

}