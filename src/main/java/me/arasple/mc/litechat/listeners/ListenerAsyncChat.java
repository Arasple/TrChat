package me.arasple.mc.litechat.listeners;

import io.izzel.taboolib.module.inject.TListener;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.module.tellraw.TellrawJson;
import io.izzel.taboolib.util.chat.ComponentSerializer;
import me.arasple.mc.litechat.LCFiles;
import me.arasple.mc.litechat.LiteChat;
import me.arasple.mc.litechat.commands.StaffChatCommand;
import me.arasple.mc.litechat.data.Cooldowns;
import me.arasple.mc.litechat.data.DataHandler;
import me.arasple.mc.litechat.filter.WordFilter;
import me.arasple.mc.litechat.formats.ChatFormats;
import me.arasple.mc.litechat.utils.BungeeUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

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

        if (LCFiles.getSettings().getStringList("General.disabled-worlds").contains(p.getWorld().getName())) {
            e.setCancelled(false);
            return;
        }
        if (!processCooldown(p, message) || !processFilter(p, message)) {
            return;
        }
        if (StaffChatCommand.isInStaffChannel(p)) {
            String raw = ComponentSerializer.toString(ChatFormats.getStaff(p, message).getComponentsAll());
            BungeeUtils.sendBungeeData(p, "LiteChat", "SendRawPerm", raw, "litechat.staff");
            return;
        }

        message = p.hasPermission("litechat.bypass.filter") || !LCFiles.getSettings().getBoolean("ChatControl.filter.enable.chat") ? message : WordFilter.doFilter(message);
        TellrawJson format = ChatFormats.getNormal(p, message);
        List<Player> players = Bukkit.getOnlinePlayers().stream().filter(x -> !(LCFiles.getSettings().getBoolean("General.per-world-chat") && x.getWorld() == p.getWorld())).collect(Collectors.toList());

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
        if (LCFiles.getSettings().getBoolean("ChatControl.filter.block-sending.enable", true)) {
            if (WordFilter.getContainsAmount(message) >= LCFiles.getSettings().getInt("ChatControl.filter.block-sending.min", 5)) {
                TLocale.sendTo(p, "GENERAL.NO-SWEAR");
                return false;
            }
        }
        return true;
    }

    private boolean processCooldown(Player p, String message) {
        if (!p.hasPermission("litechat.bypass.itemcd")) {
            long itemShowCooldown = DataHandler.getCooldownLeft(p.getUniqueId(), Cooldowns.CooldownType.ITEM_SHOW);
            if (LCFiles.getSettings().getStringList("ChatControl.item-show.keys").stream().anyMatch(message::contains)) {
                if (itemShowCooldown > 0) {
                    TLocale.sendTo(p, "COOLDOWNS.ITEM-SHOW", String.valueOf(itemShowCooldown / 1000D));
                    return false;
                } else {
                    DataHandler.updateCooldown(p.getUniqueId(), Cooldowns.CooldownType.ITEM_SHOW, (long) (LCFiles.getSettings().getDouble("ChatControl.item-show.cooldown") * 1000));
                }
            }
        }
        if (!p.hasPermission("litechat.bypass.chatcd")) {
            long chatCooldown = DataHandler.getCooldownLeft(p.getUniqueId(), Cooldowns.CooldownType.CHAT);
            if (chatCooldown > 0) {
                TLocale.sendTo(p, "COOLDOWNS.CHAT", String.valueOf(chatCooldown / 1000D));
                return false;
            } else {
                DataHandler.updateCooldown(p.getUniqueId(), Cooldowns.CooldownType.CHAT, (long) (LCFiles.getSettings().getDouble("ChatControl.cooldown") * 1000));
            }
        }
        return true;
    }

}
