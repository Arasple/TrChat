package me.arasple.mc.litechat;

import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.module.locale.logger.TLogger;
import me.arasple.mc.litechat.bstats.Metrics;
import me.arasple.mc.litechat.formats.ChatFormats;
import me.arasple.mc.litechat.utils.BungeeUtils;

import java.io.IOException;

import static org.bukkit.Bukkit.getMessenger;

/**
 * @author Arasple
 */
public final class LiteChat extends Plugin {

    private static LiteChat instance;
    @TInject("§3L§bChat")
    private static TLogger logger;

    public static boolean isDebug() {
        return LCFiles.getSettings().getBoolean("General.debug");
    }

    public static boolean switchDebug() {
        boolean state = LCFiles.getSettings().getBoolean("General.debug");
        state = !state;
        LCFiles.getSettings().set("General.debug", state);
        return state;
    }

    @Override
    public void onStarting() {
        instance = this;

        if (!getMessenger().isOutgoingChannelRegistered(this, "BungeeCord")) {
            getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
            getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeUtils());
            BungeeUtils.setEnable(true);
            TLocale.sendToConsole("PLUGIN.REGISTERED-BUNGEE");
        }

        new Metrics(this);

        TLocale.sendToConsole("PLUGIN.ENABLED", getDescription().getVersion());
    }

    @Override
    public void onActivated() {
        ChatFormats.load(true);
    }


    @Override
    public void onStopping() throws IOException {
        LCFiles.getSettings().save(LCFiles.getSettings().getFile());
    }

    public static TLogger getTLogger() {
        return logger;
    }

    public static LiteChat getInst() {
        return instance;
    }

}
