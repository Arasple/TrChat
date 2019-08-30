package me.arasple.mc.litechat.tellraw.internal;

import io.izzel.taboolib.module.tellraw.TellrawJson;
import org.bukkit.command.CommandSender;

/**
 * @author Arasple
 * @date 2019/8/30 12:24
 */
public interface BaseTellraws {

    void sendTellraw(TellrawJson tellrawJson, CommandSender... senders);

    void sendRaw(String raw, byte type, CommandSender... senders);

}
