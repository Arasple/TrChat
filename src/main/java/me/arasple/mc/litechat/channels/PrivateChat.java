package me.arasple.mc.litechat.channels;

import com.google.common.collect.Lists;
import io.izzel.taboolib.internal.apache.lang3.ArrayUtils;
import io.izzel.taboolib.module.command.lite.CommandBuilder;
import io.izzel.taboolib.module.inject.TFunction;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.module.tellraw.TellrawJson;
import io.izzel.taboolib.util.chat.ComponentSerializer;
import io.izzel.taboolib.util.chat.TextComponent;
import me.arasple.mc.litechat.LiteChat;
import me.arasple.mc.litechat.api.events.PrivateMessageEvent;
import me.arasple.mc.litechat.formats.ChatFormats;
import me.arasple.mc.litechat.utils.BungeeUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

/**
 * @author Arasple
 * @date 2019/8/17 22:57
 */
@TFunction(enable = "init")
public class PrivateChat {

    public static void init() {
        CommandBuilder
                .create("spy", LiteChat.getPlugin())
                .permission("litechat.admin")
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
        TellrawJson sender = ChatFormats.getPrivateSender(from, to, message);
        TellrawJson receiver = ChatFormats.getPrivateReceiver(from, to, message);
        Player toPlayer = Bukkit.getPlayerExact(to);

        PrivateMessageEvent event = new PrivateMessageEvent(from, to, message, toPlayer == null || !toPlayer.isOnline());

        if (!event.call()) {
            return;
        }
        if (event.isCrossServer()) {
            String raw = ComponentSerializer.toString(receiver.getComponentsAll());
            BungeeUtils.sendBungeeData(from, "LiteChat", "SendRaw", to, raw);
        } else {
            receiver.send(Bukkit.getPlayer(to));
            TLocale.sendTo(Bukkit.getPlayer(to), "PRIVATE-MESSAGE.RECEIVE", from.getName());
        }

        sender.send(from);
        sender.send(Bukkit.getConsoleSender());
        sender.setComponents(ArrayUtils.insert(0, sender.getComponentsAll(), TextComponent.fromLegacyText("§8[§3监听§8] ")));
        spying.forEach(spy -> {
            Player spyPlayer = Bukkit.getPlayer(spy);
            if (spyPlayer != null && spyPlayer.isOnline()) {
                sender.send(spyPlayer);
            }
        });
    }

    private static List<UUID> spying = Lists.newArrayList();

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
