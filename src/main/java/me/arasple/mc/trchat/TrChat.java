package me.arasple.mc.trchat;

import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.module.locale.logger.TLogger;

/**
 * @author Arasple
 */
@TrChatPlugin.Version(5.07)
public final class TrChat extends TrChatPlugin {

    @TInject("§3Tr§bChat")
    private static TLogger logger;

    public static TLogger getTLogger() {
        return logger;
    }

    public static boolean isDebug() {
        return TrChatFiles.getSettings().getBoolean("GENERAL.DEBUG", false);
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