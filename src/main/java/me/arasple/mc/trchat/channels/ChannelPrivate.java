package me.arasple.mc.trchat.channels;

import com.google.common.collect.Lists;
import io.izzel.taboolib.module.command.lite.CommandBuilder;
import io.izzel.taboolib.module.inject.TFunction;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.module.tellraw.TellrawJson;
import io.izzel.taboolib.util.chat.ComponentSerializer;
import me.arasple.mc.trchat.TrChat;
import me.arasple.mc.trchat.bstats.Metrics;
import me.arasple.mc.trchat.chat.ChatFormats;
import me.arasple.mc.trchat.chat.obj.ChatType;
import me.arasple.mc.trchat.logs.ChatLogs;
import me.arasple.mc.trchat.utils.Bungees;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

/**
 * @author Arasple
 * @date 2019/8/17 22:57
 */
@TFunction(enable = "init")
public class ChannelPrivate {

    private static List<UUID> spying = Lists.newArrayList();

    public static void init() {
        CommandBuilder
                .create("spy", TrChat.getPlugin())
                .permission("trchat.admin")
                .permissionMessage(TLocale.asString("GENERAL.NO-PERMISSION"))
                .tab((sender, args) -> null)
                .execute((sender, args) -> {
                    if (!(sender instanceof Player)) {
                        TLocale.sendTo(sender, "STAFF-CHANNEL.NOT-PLAYER");
                        return;
                    }

                    Player p = (Player) sender;
                    boolean state = switchSpy(p);
                    TLocale.sendTo(p, state ? "PRIVATE-MESSAGE.SPY.ON" : "PRIVATE-MESSAGE.SPY.OFF");
                })
                .build()
        ;
    }

    public static void execute(Player from, String to, String message) {
        OfflinePlayer toPlayer = Bukkit.getPlayerExact(to);

        TellrawJson sender = ChatFormats.getFormat(ChatType.PRIVATE_SEND, from).apply(toPlayer, message, to);
        TellrawJson receiver = ChatFormats.getFormat(ChatType.PRIVATE_RECEIVE, from).apply(from, message, to);

        if (toPlayer == null || !toPlayer.isOnline()) {
            String raw = ComponentSerializer.toString(receiver.getComponentsAll());
            Bungees.sendBungeeData(from, "TrChat", "SendRaw", to, raw);
        } else {
            receiver.send(Bukkit.getPlayer(to));
            TLocale.sendTo(Bukkit.getPlayer(to), "PRIVATE-MESSAGE.RECEIVE", from.getName());
        }

        sender.send(from);

        String spyFormat = "§8[§3监听§8] §6" + from.getName() + " &3-> §2" + to + "§f: §7" + message;

        spying.forEach(spy -> {
            Player spyPlayer = Bukkit.getPlayer(spy);
            if (spyPlayer != null && spyPlayer.isOnline()) {
                spyPlayer.sendMessage(spyFormat);
            }
        });
        Bukkit.getConsoleSender().sendMessage(spyFormat);
        ChatLogs.logPrivate(from, toPlayer.getName(), message);
        Metrics.increase(0);
    }

    public static boolean switchSpy(Player player) {
        if (!spying.contains(player.getUniqueId())) {
            spying.add(player.getUniqueId());
        } else {
            spying.remove(player.getUniqueId());
        }
        return spying.contains(player.getUniqueId());
    }

    public static boolean isSpying(Player player) {
        return spying.contains(player.getUniqueId());
    }

}
