package me.arasple.mc.trchat.chat.listeners;

import io.izzel.taboolib.module.inject.TListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;

/**
 * @author Arasple
 * @date 2020/1/17 14:41
 */
@TListener
public class ListenerTabComplete implements Listener {

    @EventHandler
    public void onTab(TabCompleteEvent e) {
        e.setCancelled(!e.getSender().hasPermission("trchat.bypass.tabcomplete"));
    }

}
