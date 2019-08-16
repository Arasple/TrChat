package me.arasple.mc.litechat.listeners;

import me.arasple.mc.litechat.LCFiles;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author Arasple
 * @date 2019/8/16 11:06
 */
public class ListenerPlayerQuit implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        LCFiles.getData().set("UserData." + p.getUniqueId() + ".last-online", System.currentTimeMillis());
        LCFiles.save();
    }

}
