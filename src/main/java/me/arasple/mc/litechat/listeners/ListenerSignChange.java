package me.arasple.mc.litechat.listeners;

import io.izzel.taboolib.module.inject.TListener;
import me.arasple.mc.litechat.LiteChat;
import me.arasple.mc.litechat.api.LiteChatAPI;
import me.arasple.mc.litechat.utils.MessageColors;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

/**
 * @author Arasple
 * @date 2019/8/15 21:18
 */
@TListener
public class ListenerSignChange implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSignChange(SignChangeEvent e) {
        Player p = e.getPlayer();

        for (int i = 0; i < e.getLines().length; i++) {
            String line = e.getLine(i);
            if (LiteChat.getSettings().getBoolean("CHAT-CONTROL.COLOR-CODE.SIGN")) {
                line = MessageColors.processWithPermission(p, line);
            }
            e.setLine(i, line != null ? LiteChatAPI.filterString(p, line, LiteChat.getSettings().getBoolean("CHAT-CONTROL.FILTER.ENABLE.SIGN", true)).getFiltered() : null);
        }
    }

}
