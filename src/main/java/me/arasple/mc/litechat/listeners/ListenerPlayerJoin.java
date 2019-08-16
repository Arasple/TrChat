package me.arasple.mc.litechat.listeners;

import io.izzel.taboolib.module.inject.TListener;
import me.arasple.mc.litechat.data.DataHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author Arasple
 * @date 2019/8/16 11:05
 */
@TListener
public class ListenerPlayerJoin implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        DataHandler.initFor(p);
    }

}
