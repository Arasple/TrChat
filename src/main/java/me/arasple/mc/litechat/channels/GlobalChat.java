package me.arasple.mc.litechat.channels;

import io.izzel.taboolib.module.tellraw.TellrawJson;
import io.izzel.taboolib.util.chat.ComponentSerializer;
import me.arasple.mc.litechat.api.events.LChatGlobalShoutEvent;
import me.arasple.mc.litechat.formats.ChatFormats;
import me.arasple.mc.litechat.utils.BungeeUtils;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2019/8/17 23:06
 */
public class GlobalChat {

    public static void execute(Player from, String message) {
        TellrawJson format = ChatFormats.getGlobal(from, message);

        LChatGlobalShoutEvent event = new LChatGlobalShoutEvent(from, message, format);
//        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            String raw = ComponentSerializer.toString(event.getFormat().getComponentsAll());
            BungeeUtils.sendBungeeData(from, "LiteChat", "BroadcastRaw", raw);
        }
    }

}
