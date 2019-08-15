package me.arasple.mc.litechat.commands;

import io.izzel.taboolib.module.command.base.BaseCommand;
import io.izzel.taboolib.module.command.base.BaseMainCommand;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.module.tellraw.TellrawJson;
import io.izzel.taboolib.util.ArrayUtil;
import io.izzel.taboolib.util.chat.ComponentSerializer;
import me.arasple.mc.litechat.LCFiles;
import me.arasple.mc.litechat.filter.WordFilter;
import me.arasple.mc.litechat.formats.ChatFormats;
import me.arasple.mc.litechat.utils.BungeeUtils;
import me.arasple.mc.litechat.utils.Players;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Arasple
 * @date 2019/8/4 21:19
 */
@BaseCommand(name = "msg", aliases = {"message", "tell", "talk"}, permission = "litechat.private")
public class PrivateMessageCommand extends BaseMainCommand {

    @Override
    public String getCommandTitle() {
        return TLocale.asString("PLUGIN.COMMAND-TITLE");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            TLocale.sendTo(sender, "PRIVATE-MESSAGE.NOT-PLAYER");
            return true;
        }
        if (args.length == 0) {
            TLocale.sendTo(sender, "PRIVATE-MESSAGE.NO-PLAYER");
            return true;
        }
        if (!Players.isPlayerOnline(args[0])) {
            TLocale.sendTo(sender, "PRIVATE-MESSAGE.NOT-EXIST");
            return true;
        } else if (args.length == 1) {
            TLocale.sendTo(sender, "PRIVATE-MESSAGE.NO-MESSAGE");
            return true;
        }
        String privateMessage = ArrayUtil.arrayJoin(args, 1);
        if (LCFiles.getSettings().getBoolean("ChatControl.filter.block-sending.enable", true)) {
            if (WordFilter.getContainsAmount(privateMessage) >= LCFiles.getSettings().getInt("ChatControl.filter.block-sending.min", 5)) {
                TLocale.sendTo(sender, "GENERAL.NO-SWEAR");
                return true;
            }
        }
        privateMessage = sender.hasPermission("litechat.bypass.filter") || !LCFiles.getSettings().getBoolean("ChatControl.filter.enable.chat") ? privateMessage : WordFilter.doFilter(privateMessage);
        sendPrivateMessage((Player) sender, Players.getPlayerFullName(args[0]), privateMessage);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        if (args.length == 0) {
            return Players.getPlayers();
        } else if (args.length == 1) {
            return Players.getPlayers().stream().filter(s -> s.toLowerCase().startsWith(args[0])).collect(Collectors.toList());
        }
        return null;
    }

    private static void sendPrivateMessage(Player from, String to, String message) {
        TellrawJson sender = ChatFormats.getPrivateSender(from, to, message);
        TellrawJson receiver = ChatFormats.getPrivateReceiver(from, to, message);

        if (Objects.requireNonNull(Bukkit.getPlayer(to)).isOnline()) {
            receiver.send(Bukkit.getPlayer(to));
            TLocale.sendTo(Bukkit.getPlayer(to), "PRIVATE-MESSAGE.RECEIVE", from.getName());
        } else {
            String raw = ComponentSerializer.toString(receiver.getComponentsAll());
            BungeeUtils.sendBungeeData(from, "LiteChat", "SendRaw", to, raw);
        }

        sender.send(from);
        sender.send(Bukkit.getConsoleSender());
    }

}
