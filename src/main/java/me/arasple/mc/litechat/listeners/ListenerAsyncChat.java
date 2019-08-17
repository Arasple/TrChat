package me.arasple.mc.litechat.listeners;

import io.izzel.taboolib.module.inject.TListener;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.module.tellraw.TellrawJson;
import me.arasple.mc.litechat.LiteChat;
import me.arasple.mc.litechat.channels.StaffChat;
import me.arasple.mc.litechat.data.Cooldowns;
import me.arasple.mc.litechat.data.DataHandler;
import me.arasple.mc.litechat.filter.WordFilter;
import me.arasple.mc.litechat.formats.ChatFormats;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Arasple
 * @date 2019/8/16 11:04
 */
@TListener
public class ListenerAsyncChat implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent e) {
        long start = System.currentTimeMillis();

        Player p = e.getPlayer();
        String message = e.getMessage();
        e.setCancelled(true);

        if (LiteChat.getSettings().getStringList("GENERAL.DISABLED-WORLDS").contains(p.getWorld().getName())) {
            e.setCancelled(false);
            return;
        }

        if (!processCooldown(p, message) || !processFilter(p, message)) {
            e.setCancelled(true);
            return;
        }
        if (StaffChat.isInStaffChannel(p)) {
            StaffChat.execute(p, message);
            return;
        }

        if (e.getMessage().toLowerCase().startsWith("kb")) {
            Vector vector = p.getLocation().getDirection();
            vector = vector.multiply(-Double.parseDouble(e.getMessage().split(" ")[1]));
            p.setVelocity(vector);
        }

        TellrawJson format = ChatFormats.getNormal(p, WordFilter.doFilter(message, LiteChat.getSettings().getBoolean("CHAT-CONTROL.FILTER.ENABLE.CHAT", true) && !p.hasPermission("litechat.bypass.filter")));
        List<Player> players = Bukkit.getOnlinePlayers().stream().filter(x -> !(LiteChat.getSettings().getBoolean("GENERAL.PER-WORLD-CHAT") && x.getWorld() == p.getWorld())).collect(Collectors.toList());

        players.forEach(format::send);
        format.send(Bukkit.getConsoleSender());
        if (LiteChat.isDebug()) {
            LiteChat.getTLogger().fine("[Chat-Event]: Process Took " + (System.currentTimeMillis() - start) + " Ms");
        }
    }

    private boolean processFilter(Player p, String message) {
        if (p.hasPermission("litechat.bypass.filter")) {
            return true;
        }
        if (LiteChat.getSettings().getBoolean("CHAT-CONTROL.FILTER.BLOCK-SENDING.ENABLE", true)) {
            if (WordFilter.getContainsAmount(message) >= LiteChat.getSettings().getInt("CHAT-CONTROL.FILTER.BLOCK-SENDING.MIN", 5)) {
                TLocale.sendTo(p, "GENERAL.NO-SWEAR");
                return false;
            }
        }
        return true;
    }

    private boolean processCooldown(Player p, String message) {
        if (!p.hasPermission("litechat.bypass.itemcd")) {
            long itemShowCooldown = DataHandler.getCooldownLeft(p.getUniqueId(), Cooldowns.CooldownType.ITEM_SHOW);
            if (LiteChat.getSettings().getStringList("CHAT-CONTROL.ITEM-SHOW.KEYS").stream().anyMatch(message::contains)) {
                if (itemShowCooldown > 0) {
                    TLocale.sendTo(p, "COOLDOWNS.ITEM-SHOW", String.valueOf(itemShowCooldown / 1000D));
                    return false;
                } else {
                    DataHandler.updateCooldown(p.getUniqueId(), Cooldowns.CooldownType.ITEM_SHOW, (long) (LiteChat.getSettings().getDouble("CHAT-CONTROL.ITEM-SHOW.COOLDOWN") * 1000));
                }
            }
        }
        if (!p.hasPermission("litechat.bypass.chatcd")) {
            long chatCooldown = DataHandler.getCooldownLeft(p.getUniqueId(), Cooldowns.CooldownType.CHAT);
            if (chatCooldown > 0) {
                TLocale.sendTo(p, "COOLDOWNS.CHAT", String.valueOf(chatCooldown / 1000D));
                return false;
            } else {
                DataHandler.updateCooldown(p.getUniqueId(), Cooldowns.CooldownType.CHAT, (long) (LiteChat.getSettings().getDouble("CHAT-CONTROL.COOLDOWN") * 1000));
            }
        }
        return true;
    }

}
