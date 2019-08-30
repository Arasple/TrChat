package me.arasple.mc.litechat.tellraw.internal;

import io.izzel.taboolib.module.packet.TPacketHandler;
import io.izzel.taboolib.module.tellraw.TellrawJson;
import net.minecraft.server.v1_14_R1.ChatMessageType;
import net.minecraft.server.v1_14_R1.IChatBaseComponent;
import net.minecraft.server.v1_14_R1.PacketPlayOutChat;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2019/8/30 12:27
 */
public class InternalTellraws implements BaseTellraws {

    @Override
    public void sendTellraw(TellrawJson tellrawJson, CommandSender... senders) {
        for (CommandSender sender : senders) {
            sendRaw(tellrawJson.toRawMessage(), (byte) 0, senders);
        }
    }

    @Override
    public void sendRaw(String raw, byte type, CommandSender... senders) {
        for (CommandSender sender : senders) {
            TPacketHandler.sendPacket((Player) sender, new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a(raw), ChatMessageType.a(type)));
        }
    }

}
