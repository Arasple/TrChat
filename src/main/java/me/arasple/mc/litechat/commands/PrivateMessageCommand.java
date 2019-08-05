package me.arasple.mc.litechat.commands;

import io.izzel.taboolib.module.command.lite.CommandBuilder;
import io.izzel.taboolib.module.inject.TSchedule;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.module.tellraw.TellrawJson;
import io.izzel.taboolib.util.ArrayUtil;
import io.izzel.taboolib.util.chat.ComponentSerializer;
import me.arasple.mc.litechat.LCFiles;
import me.arasple.mc.litechat.LiteChat;
import me.arasple.mc.litechat.filter.WordFilter;
import me.arasple.mc.litechat.formats.ChatFormats;
import me.arasple.mc.litechat.utils.BungeeUtils;
import me.arasple.mc.litechat.utils.Players;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Arasple
 * @date 2019/8/4 21:19
 */
public class PrivateMessageCommand {

    @TSchedule(delay = 10)
    public static void register() {
        CommandBuilder.create("Msg", LiteChat.getInst())
                .aliases("tell", "talk")
                .forceRegister()
                .execute(((sender, args) -> {
                    if (!(sender instanceof Player)) {
                        TLocale.sendTo(sender, "PRIVATE-MESSAGE.NOT-PLAYER");
                        return;
                    }
                    if (args.length == 0) {
                        TLocale.sendTo(sender, "PRIVATE-MESSAGE.NO-PLAYER");
                        return;
                    }
                    if (!Players.isPlayerOnline(args[0])) {
                        TLocale.sendTo(sender, "PRIVATE-MESSAGE.NOT-EXIST");
                        return;
                    } else if (args.length == 1) {
                        TLocale.sendTo(sender, "PRIVATE-MESSAGE.NO-MESSAGE");
                        return;
                    }
                    String privateMessage = ArrayUtil.arrayJoin(args, 1);
                    if (LCFiles.getSettings().getBoolean("ChatControl.filter.block-sending.enable", true)) {
                        if (WordFilter.getContainsAmount(privateMessage) >= LCFiles.getSettings().getInt("ChatControl.filter.block-sending.min", 5)) {
                            TLocale.sendTo(sender, "GENERAL.NO-SWEAR");
                            return;
                        }
                    }
                    privateMessage = WordFilter.doFilter(privateMessage);
                    sendPrivateMessage((Player) sender, Players.getPlayerFullName(args[0]), privateMessage);
                }))
                .tab((sender, args) -> {
                    if (args.length == 0) {
                        return Players.getPlayers();
                    } else if (args.length == 1) {
                        return Players.getPlayers().stream().filter(s -> s.toLowerCase().startsWith(args[0])).collect(Collectors.toList());
                    }
                    return null;
                })
                .build();
    }

    private static void sendPrivateMessage(Player from, String to, String message) {
        ChatFormats.getPrivateSender(from, to, message).send(from);
        TellrawJson receiver = ChatFormats.getPrivateReceiver(from, to, message);

        if (Objects.requireNonNull(Bukkit.getPlayer(to)).isOnline()) {
            receiver.send(Bukkit.getPlayer(to));
            TLocale.sendTo(Bukkit.getPlayer(to), "PRIVATE-MESSAGE.RECEIVE", from.getName());
        } else {
            String raw = ComponentSerializer.toString(receiver.getComponentsAll());
            BungeeUtils.sendBungeeData(from, "LiteChat", "SendRaw", to, raw);
        }
    }

}
