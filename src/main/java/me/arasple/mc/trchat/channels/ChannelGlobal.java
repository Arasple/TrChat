package me.arasple.mc.trchat.channels;

import io.izzel.taboolib.module.tellraw.TellrawJson;
import io.izzel.taboolib.util.chat.ComponentSerializer;
import me.arasple.mc.trchat.bstats.Metrics;
import me.arasple.mc.trchat.chat.ChatFormats;
import me.arasple.mc.trchat.chat.obj.ChatType;
import me.arasple.mc.trchat.utils.Bungees;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2019/8/17 23:06
 */
public class ChannelGlobal {

    public static void execute(Player from, String message) {
        TellrawJson format = ChatFormats.getFormat(ChatType.GLOBAL, from).apply(from, message);
        String raw = ComponentSerializer.toString(format.getComponentsAll());
        Bungees.sendBungeeData(from, "TrChat", "BroadcastRaw", raw);
        Metrics.increase(0);
    }

}
