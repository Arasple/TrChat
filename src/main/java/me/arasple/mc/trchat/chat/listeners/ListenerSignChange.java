package me.arasple.mc.trchat.chat.listeners;

import io.izzel.taboolib.module.inject.TListener;
import me.arasple.mc.trchat.TrChatFiles;
import me.arasple.mc.trchat.api.TrChatAPI;
import me.arasple.mc.trchat.utils.MessageColors;
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
            if (TrChatFiles.getSettings().getBoolean("CHAT-COLOR.SIGN")) {
                line = MessageColors.replaceWithPermission(p, line);
            }
            e.setLine(i, line != null ? TrChatAPI.filterString(p, line, true).getFiltered() : null);
        }
    }

}
