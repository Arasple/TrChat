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
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Arasple
 * @date 2019/8/4 21:19
 */
@BaseCommand(name = "shout", aliases = {"all", "global"}, permission = "litechat.global")
public class GlobalShoutCommand extends BaseMainCommand {

    @Override
    public String getCommandTitle() {
        return TLocale.asString("PLUGIN.COMMAND-TITLE");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            TLocale.sendTo(sender, "GLOBAL-MESSAGE.NOT-PLAYER");
            return true;
        }
        if (args.length == 0) {
            TLocale.sendTo(sender, "GLOBAL-MESSAGE.NO-MESSAGE");
            return true;
        }
        String shoutMessage = ArrayUtil.arrayJoin(args, 0);
        if (LCFiles.getSettings().getBoolean("ChatControl.filter.block-sending.enable", true)) {
            if (WordFilter.getContainsAmount(shoutMessage) >= LCFiles.getSettings().getInt("ChatControl.filter.block-sending.min", 5)) {
                TLocale.sendTo(sender, "GENERAL.NO-SWEAR");
                return true;
            }
        }
        shoutMessage((Player) sender, WordFilter.doFilter(shoutMessage));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        return null;
    }

    private static void shoutMessage(Player from, String message) {
        TellrawJson format = ChatFormats.getGlobal(from, message);
        String raw = ComponentSerializer.toString(format.getComponentsAll());
        BungeeUtils.sendBungeeData(from, "LiteChat", "BroadcastRaw", raw);
    }

}
