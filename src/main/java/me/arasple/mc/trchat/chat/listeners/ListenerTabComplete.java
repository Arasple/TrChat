package me.arasple.mc.trchat.chat.listeners;

import io.izzel.taboolib.module.inject.TListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;

/**
 * @author Arasple
 * @date 2020/1/17 14:41
 */
@TListener
public class ListenerTabComplete implements Listener {

    @EventHandler
    public void onTabCommandSend(PlayerCommandSendEvent e) {
        Player p = e.getPlayer();
        if (!p.hasPermission("trchat.bypass.tabcomplete")) {
            e.getCommands().clear();
        }
    }

}
