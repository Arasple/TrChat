package me.arasple.mc.trchat.commands;

import io.izzel.taboolib.module.command.base.BaseCommand;
import io.izzel.taboolib.module.command.base.BaseMainCommand;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.ArrayUtil;
import me.arasple.mc.trchat.api.TrChatAPI;
import me.arasple.mc.trchat.channels.PrivateChat;
import me.arasple.mc.trchat.filter.ChatFilter;
import me.arasple.mc.trchat.filter.process.FilteredObject;
import me.arasple.mc.trchat.utils.Players;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Arasple
 * @date 2019/8/4 21:19
 */
@BaseCommand(name = "msg", aliases = {"message", "tell", "talk"}, permission = "trchat.private")
public class CommandPrivateMessage extends BaseMainCommand {

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
        FilteredObject filteredObject = TrChatAPI.filterString((Player) sender, privateMessage, ChatFilter.getEnable()[0]);

        if (filteredObject.getSensitiveWords() >= ChatFilter.getBlockSending()) {
            TLocale.sendTo(sender, "GENERAL.NO-SWEAR");
            return true;
        }

        PrivateChat.execute((Player) sender, Players.getPlayerFullName(args[0]), filteredObject.getFiltered());
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
