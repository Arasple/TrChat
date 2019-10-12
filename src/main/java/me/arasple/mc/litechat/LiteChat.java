package me.arasple.mc.litechat;

import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.module.locale.logger.TLogger;

/**
 * @author Arasple
 */
@LiteChatPlugin.Version(5.07)
public final class LiteChat extends LiteChatPlugin {

    @TInject("§3L§bChat")
    private static TLogger logger;

    public static TLogger getTLogger() {
        return logger;
    }

    public static boolean isDebug() {
        return LiteChatFiles.getSettings().getBoolean("GENERAL.DEBUG", false);
    }

    @Override
    public void onStarting() {
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

}