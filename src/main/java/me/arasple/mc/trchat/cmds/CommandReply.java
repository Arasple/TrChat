package me.arasple.mc.trchat.cmds;

import io.izzel.taboolib.module.command.base.BaseCommand;
import io.izzel.taboolib.module.command.base.BaseMainCommand;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.ArrayUtil;
import me.arasple.mc.trchat.channels.ChannelPrivate;
import me.arasple.mc.trchat.utils.Players;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Arasple
 * @date 2020/1/26 16:31
 */

@BaseCommand(name = "reply", aliases = {"r"}, permission = "trchat.private")
public class CommandReply extends BaseMainCommand {

    private static HashMap<UUID, String> lastMessageFrom = new HashMap<>();

    public static HashMap<UUID, String> getLastMessageFrom() {
        return lastMessageFrom;
    }

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
        if (getLastMessageFrom().containsKey(((Player) sender).getUniqueId())) {
            String privateMessage = ArrayUtil.arrayJoin(args, 0);
            ChannelPrivate.execute((Player) sender, Players.getPlayerFullName(getLastMessageFrom().get(((Player) sender).getUniqueId())), privateMessage);
        }
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

}
