package me.arasple.mc.trchat.utils;

import io.izzel.taboolib.module.inject.TInject;
import me.arasple.mc.trchat.nms.AbstractPacketUtils;

/**
 * @author Arasple
 * @date 2019/11/30 11:21
 */
public class PacketUtils {

    @TInject(asm = "me.arasple.mc.trchat.nms.InternalPacketUtils")
    private static AbstractPacketUtils packetUtils;

    public static AbstractPacketUtils get() {
        return packetUtils;
    }

}
