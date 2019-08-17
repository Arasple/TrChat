package me.arasple.mc.litechat;

import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.module.locale.logger.TLogger;
import me.arasple.mc.litechat.bstats.Metrics;
import me.arasple.mc.litechat.formats.ChatFormats;
import me.arasple.mc.litechat.utils.BungeeUtils;

import java.io.IOException;

/**
 * @author Arasple
 */
@Plugin.Version(5.02)
public final class LiteChat extends Plugin {

    @TInject
    private static LiteChat instance;
    @TInject("§3L§bChat")
    private static TLogger logger;

    @Override
    public void onLoading() {
        TLocale.sendToConsole("PLUGIN.LOADED");
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
        try {
            LCFiles.getSettings().save(LCFiles.getSettings().getFile());
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

    public static boolean isDebug() {
        return LCFiles.getSettings().getBoolean("GENERAL.DEBUG");
    }

    public static boolean switchDebug() {
        boolean state = LCFiles.getSettings().getBoolean("GENERAL.DEBUG");
        state = !state;
        LCFiles.getSettings().set("GENERAL.DEBUG", state);
        return state;
    }

}
