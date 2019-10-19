package me.arasple.mc.trchat.commands;

import io.izzel.taboolib.module.command.base.BaseCommand;
import io.izzel.taboolib.module.command.base.BaseMainCommand;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.ArrayUtil;
import me.arasple.mc.trchat.api.TrChatAPI;
import me.arasple.mc.trchat.channels.GlobalChat;
import me.arasple.mc.trchat.filter.ChatFilter;
import me.arasple.mc.trchat.filter.process.FilteredObject;
import me.arasple.mc.trchat.utils.Bungees;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Arasple
 * @date 2019/8/4 21:19
 */
@BaseCommand(name = "shout", aliases = {"all", "global"}, permission = "trchat.global")
public class CommandGlobalShout extends BaseMainCommand {

    @Override
    public String getCommandTitle() {
        return TLocale.asString("PLUGIN.COMMAND-TITLE");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!Bungees.isEnable()) {
            TLocale.sendTo(sender, "GLOBAL-MESSAGE.NOT-ENABLE");
            return true;
        }
        if (!(sender instanceof Player)) {
            TLocale.sendTo(sender, "GLOBAL-MESSAGE.NOT-PLAYER");
            return true;
        }
        if (args.length == 0) {
            TLocale.sendTo(sender, "GLOBAL-MESSAGE.NO-MESSAGE");
            return true;
        }
        String message = ArrayUtil.arrayJoin(args, 0);
        FilteredObject filteredObject = TrChatAPI.filterString((Player) sender, message, ChatFilter.getEnable()[0]);

        if (filteredObject.getSensitiveWords() >= ChatFilter.getBlockSending()) {
            TLocale.sendTo(sender, "GENERAL.NO-SWEAR");
            return true;
        }

        GlobalChat.execute((Player) sender, filteredObject.getFiltered());
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        return null;
    }


}
