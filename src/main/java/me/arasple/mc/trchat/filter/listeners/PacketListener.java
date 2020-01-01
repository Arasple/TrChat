package me.arasple.mc.trchat.filter.listeners;

import io.izzel.taboolib.module.packet.Packet;
import io.izzel.taboolib.module.packet.TPacket;
import me.arasple.mc.trchat.data.Users;
import me.arasple.mc.trchat.utils.PacketUtils;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2019/11/30 10:16
 */
public class PacketListener {

    @TPacket(type = TPacket.Type.SEND)
    static boolean filterChat(Player player, Packet packet) {
        try {
            if (packet == null || player == null || !player.isOnline()) {
                return true;
            }
            if (Users.isFilterEnabled(player)) {
                if (packet.is("PacketPlayOutChat")) {
                    packet.write("a", PacketUtils.get().filterIChatComponent(packet.read("a")));
                } else if (packet.is("PacketPlayOutWindowItems")) {
                    PacketUtils.get().filterItemList(packet.read("b"));
                } else if (packet.is("PacketPlayOutSetSlot")) {
                    PacketUtils.get().filterItem(packet.read("c"));
                }
            }
        } catch (Throwable ignored) {
        }
        return true;
    }

}
