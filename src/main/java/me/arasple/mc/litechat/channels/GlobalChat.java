package me.arasple.mc.litechat.channels;

import io.izzel.taboolib.module.tellraw.TellrawJson;
import io.izzel.taboolib.util.chat.ComponentSerializer;
import me.arasple.mc.litechat.api.events.GlobalShoutEvent;
import me.arasple.mc.litechat.bstats.Metrics;
import me.arasple.mc.litechat.formats.ChatFormats;
import me.arasple.mc.litechat.utils.Bungees;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2019/8/17 23:06
 */
public class GlobalChat {

    public static void execute(Player from, String message) {
        TellrawJson format = ChatFormats.getGlobal(from, message);

        GlobalShoutEvent event = new GlobalShoutEvent(from, message, format);

        if (event.call()) {
            String raw = ComponentSerializer.toString(event.getFormat().getComponentsAll());
            Bungees.sendBungeeData(from, "LiteChat", "BroadcastRaw", raw);
            Metrics.increase(0);
        }
    }

}
