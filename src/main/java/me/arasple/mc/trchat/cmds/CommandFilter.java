package me.arasple.mc.trchat.cmds;

import io.izzel.taboolib.module.command.base.BaseCommand;
import io.izzel.taboolib.module.command.base.BaseMainCommand;
import io.izzel.taboolib.module.locale.TLocale;
import me.arasple.mc.trchat.menus.MenuFilterControl;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Arasple
 * @date 2019/8/4 21:19
 */
@BaseCommand(name = "filter", aliases = {"filters"}, permission = "trchat.filter")
public class CommandFilter extends BaseMainCommand {

    @Override
    public String getCommandTitle() {
        return TLocale.asString("PLUGIN.COMMAND-TITLE");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            TLocale.sendTo(sender, "STAFF-CHANNEL.NOT-PLAYER");
            return true;
        }

        Player p = (Player) sender;
        MenuFilterControl.displayFor((Player) sender);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        return null;
    }

}
