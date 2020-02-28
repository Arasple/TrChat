package me.arasple.mc.trchat;

import io.izzel.taboolib.loader.Plugin;
import io.izzel.taboolib.module.inject.TInject;

/**
 * @author Arasple
 */
@Plugin.Version(5.17)
public final class TrChat extends Plugin {

    @TInject(state = TInject.State.STARTING, init = "init", active = "load", cancel = "unload")
    private static TrChatLoader loader;

    public static double getTrVersion() {
        return 1.71;
    }

}