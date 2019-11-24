package me.arasple.mc.trchat.channels;

import io.izzel.taboolib.module.tellraw.TellrawJson;
import io.izzel.taboolib.util.chat.ComponentSerializer;
import me.arasple.mc.trchat.api.events.GlobalShoutEvent;
import me.arasple.mc.trchat.bstats.Metrics;
import me.arasple.mc.trchat.formats.ChatFormats;
import me.arasple.mc.trchat.utils.Bungees;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2019/8/17 23:06
 */
public class GlobalChat {

    public static void execute(Player from, String message) {
        TellrawJson format = ChatFormats.getGlobal(from, message);

        GlobalShoutEvent event = new GlobalShoutEvent(from, message, format);
        if (event.call().nonCancelled()) {
            String raw = ComponentSerializer.toString(event.getFormat().getComponentsAll());
            Bungees.sendBungeeData(from, "TrChat", "BroadcastRaw", raw);
            Metrics.increase(0);
        }
    }

}
