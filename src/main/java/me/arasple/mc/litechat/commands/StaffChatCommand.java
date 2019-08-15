package me.arasple.mc.litechat.commands;

import com.google.common.collect.Lists;
import io.izzel.taboolib.module.command.base.BaseCommand;
import io.izzel.taboolib.module.command.base.BaseMainCommand;
import io.izzel.taboolib.module.locale.TLocale;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

/**
 * @author Arasple
 * @date 2019/8/4 21:19
 */
@BaseCommand(name = "staff", aliases = {"staffchannel"}, permission = "litechat.staff")
public class StaffChatCommand extends BaseMainCommand {

    private static List<UUID> staffs = Lists.newArrayList();

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

        boolean result = switchStaff((Player) sender);
        TLocale.sendTo(sender, result ? "STAFF-CHANNEL.JOIN" : "STAFF-CHANNEL.QUIT");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        return null;
    }

    private boolean switchStaff(Player player) {
        if (!staffs.contains(player.getUniqueId())) {
            staffs.add(player.getUniqueId());
        } else {
            staffs.remove(player.getUniqueId());
        }
        return staffs.contains(player.getUniqueId());
    }

    public static boolean isInStaffChannel(Player player) {
        return staffs.contains(player.getUniqueId());
    }

}
