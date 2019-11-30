package me.arasple.mc.trchat.chat.listeners;

import io.izzel.taboolib.module.inject.TListener;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.Strings;
import io.izzel.taboolib.util.lite.SoundPack;
import me.arasple.mc.trchat.TrChat;
import me.arasple.mc.trchat.chat.ChatFormats;
import me.arasple.mc.trchat.filter.ChatFilter;
import me.arasple.mc.trchat.func.ChatFunctions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * @author Arasple
 * @date 2019/11/30 21:56
 */
@TListener
public class ListenerTrChatInfo implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent e) {
        e.setCancelled(react(e.getPlayer(), e.getMessage().startsWith("#") ? e.getMessage().substring(1) : null));

        if ("#TRCHAT-RELOAD".equals(e.getMessage()) && e.getPlayer().hasPermission("trchat.admin")) {
            ChatFormats.loadFormats(e.getPlayer());
            ChatFilter.loadFilter(true, e.getPlayer());
            ChatFunctions.loadFunctions(e.getPlayer());
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent e) {
        e.setCancelled(react(e.getPlayer(), e.getMessage().substring(1)));
    }

    private boolean react(Player p, String message) {
        if (!Strings.isBlank(message) && ("trchatr".equalsIgnoreCase(message) || "trixeychat".equalsIgnoreCase(message))) {
            TLocale.Display.sendTitle(p, "§3§lTr§b§lChat", "§7Designed by §6Arasple", 10, 35, 10);
            TLocale.Display.sendActionBar(p, Strings.replaceWithOrder(
                    "§2Running version §av{0}§7",
                    TrChat.getTrVersion()
            ));
            new SoundPack("BLOCK_NOTE_BLOCK_PLING-1-2").play(p);
            return true;
        }
        return false;
    }

}
