package me.arasple.mc.litechat.commands;

import io.izzel.taboolib.module.command.base.BaseCommand;
import io.izzel.taboolib.module.command.base.BaseMainCommand;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.ArrayUtil;
import me.arasple.mc.litechat.LCFiles;
import me.arasple.mc.litechat.channels.GlobalChat;
import me.arasple.mc.litechat.filter.WordFilter;
import me.arasple.mc.litechat.utils.BungeeUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Arasple
 * @date 2019/8/4 21:19
 */
@BaseCommand(name = "shout", aliases = {"all", "global"}, permission = "litechat.global")
public class CommandGlobalShout extends BaseMainCommand {

    @Override
    public String getCommandTitle() {
        return TLocale.asString("PLUGIN.COMMAND-TITLE");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!BungeeUtils.isEnable()) {
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
        if (LCFiles.getSettings().getBoolean("CHAT-CONTROL.FILTER.BLOCK-SENDING.ENABLE", true)) {
            if (WordFilter.getContainsAmount(message) >= LCFiles.getSettings().getInt("CHAT-CONTROL.FILTER.BLOCK-SENDING.MIN", 5)) {
                TLocale.sendTo(sender, "GENERAL.NO-SWEAR");
                return true;
            }
        }
        GlobalChat.execute((Player) sender, WordFilter.doFilter(message, LCFiles.getSettings().getBoolean("CHAT-CONTROL.FILTER.ENABLE.CHAT", true) && !sender.hasPermission("litechat.bypass.filter")));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        return null;
    }


}
