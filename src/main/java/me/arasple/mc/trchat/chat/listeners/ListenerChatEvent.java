package me.arasple.mc.trchat.chat.listeners;

import io.izzel.taboolib.module.inject.TListener;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.module.tellraw.TellrawJson;
import io.izzel.taboolib.util.Strings;
import me.arasple.mc.trchat.TrChatFiles;
import me.arasple.mc.trchat.bstats.Metrics;
import me.arasple.mc.trchat.channels.ChannelGlobal;
import me.arasple.mc.trchat.channels.ChannelStaff;
import me.arasple.mc.trchat.chat.ChatFormats;
import me.arasple.mc.trchat.chat.format.Format;
import me.arasple.mc.trchat.chat.obj.ChatType;
import me.arasple.mc.trchat.data.Cooldowns;
import me.arasple.mc.trchat.data.Users;
import me.arasple.mc.trchat.logs.ChatLogs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * @author Arasple
 * @date 2019/11/30 12:10
 */
@TListener
public class ListenerChatEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();

        if (TrChatFiles.getSettings().getStringList("GENERAL.DISABLED-WORLDS").contains(player.getWorld().getName())) {
            e.setCancelled(false);
            return;
        }
        if (ChannelStaff.isInStaffChannel(player)) {
            e.setCancelled(true);
            ChannelStaff.execute(player, e.getMessage());
            return;
        }
        if (TrChatFiles.getChannels().getBoolean("FORCE-GLOBAL")) {
            e.setCancelled(true);
            ChannelGlobal.execute(player, e.getMessage());
            return;
        }
        if (!checkLimits(player, e.getMessage())) {
            e.setCancelled(true);
            return;
        }

        Format format = ChatFormats.getFormat(ChatType.NORMAL, player);

        if (format != null) {
            e.setCancelled(true);


            TellrawJson tellraw = format.apply(player, e.getMessage());
            Bukkit.getOnlinePlayers().stream().filter(p -> !Users.getIgnoredList(p).contains(player.getName())).forEach(tellraw::send);
            tellraw.send(Bukkit.getConsoleSender());
            ChatLogs.log(player, e.getMessage());
            Metrics.increase(0);
        }
    }

    private boolean checkLimits(Player p, String message) {
        if (!p.hasPermission("trchat.bypass.*")) {
            long limit = TrChatFiles.getSettings().getLong("CHAT-CONTROL.LENGTH-LIMIT", 100);
            if (message.length() > limit) {
                TLocale.sendTo(p, "GENERAL.TOO-LONG", message.length(), limit);
                return false;
            }
        }
        if (!p.hasPermission("trchat.bypass.itemcd")) {
            long itemShowCooldown = Users.getCooldownLeft(p.getUniqueId(), Cooldowns.CooldownType.ITEM_SHOW);
            if (TrChatFiles.getFunction().getStringList("GENERAL.ITEM-SHOW.KEYS").stream().anyMatch(message::contains)) {
                if (itemShowCooldown > 0) {
                    TLocale.sendTo(p, "GENERAL.COOLDOWNS.ITEM-SHOW", String.valueOf(itemShowCooldown / 1000D));
                    return false;
                } else {
                    Users.updateCooldown(p.getUniqueId(), Cooldowns.CooldownType.ITEM_SHOW, (long) (TrChatFiles.getFunction().getDouble("GENERAL.ITEM-SHOW.COOLDOWNS") * 1000));
                }
            }
        }
        if (!p.hasPermission("trchat.bypass.chatcd")) {
            long chatCooldown = Users.getCooldownLeft(p.getUniqueId(), Cooldowns.CooldownType.CHAT);
            if (chatCooldown > 0) {
                TLocale.sendTo(p, "GENERAL.COOLDOWNS.CHAT", String.valueOf(chatCooldown / 1000D));
                return false;
            } else {
                Users.updateCooldown(p.getUniqueId(), Cooldowns.CooldownType.CHAT, (long) (TrChatFiles.getSettings().getDouble("CHAT-CONTROL.COOLDOWN") * 1000));
            }
        }
        if (!p.hasPermission("trchat.bypass.repeat")) {
            String lastSay = Users.getLastMessage(p.getUniqueId());
            if (Strings.similarDegree(lastSay, message) > TrChatFiles.getSettings().getDouble("CHAT-CONTROL.ANTI-REPEAT", 0.85)) {
                TLocale.sendTo(p, "GENERAL.TOO-SIMILAR");
                return false;
            } else {
                Users.setLastMessage(p.getUniqueId(), message);
            }
        }
        return true;
    }

}
