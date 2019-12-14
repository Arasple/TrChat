package me.arasple.mc.trchat;

import io.izzel.taboolib.module.inject.TInject;

/**
 * @author Arasple
 */
@TrChatPlugin.Version(5.13)
public final class TrChat extends TrChatPlugin {

    @TInject(state = TInject.State.LOADING, init = "init", active = "load", cancel = "unload")
    private static TrChatLoader loader;

    public static double getTrVersion() {
        return 1.62;
    }

}