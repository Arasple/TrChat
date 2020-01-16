package me.arasple.mc.trchat.chat.listeners;

import io.izzel.taboolib.module.inject.TListener;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.Strings;
import me.arasple.mc.trchat.TrChatFiles;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

/**
 * @author Arasple
 * @date 2020/1/16 21:41
 */
@TListener
public class ListenerCommandController implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();
        String command = e.getMessage().substring(1);

        if (TrChatFiles.getFunction().getBoolean("GENERAL.COMMAND-CONTROLLER.ENABLE", true) && Strings.nonEmpty(command) && !player.hasPermission(TrChatFiles.getFunction().getString("GENERAL.COMMAND-CONTROLLER.BYPASS", "trchat.admin"))) {
            boolean whitelist = TrChatFiles.getFunction().getString("GENERAL.COMMAND-CONTROLLER.TYPE", "BLACKLIST").equalsIgnoreCase("WHITELIST");
            List<String> matches = TrChatFiles.getFunction().getStringList("GENERAL.COMMAND-CONTROLLER.LIST");
            boolean matched = matches.stream().anyMatch(m -> {
                if (m.toLowerCase().contains("<exact>")) {
                    return command.matches("(?i)" + m.replaceAll("(?i)<exact>", ""));
                } else {
                    return command.split(" ")[0].matches("(?i)" + m);
                }
            });
            // 黑名单下，匹配到 或 白名单下，未匹配到
            if ((matched && !whitelist) || (!matched && whitelist)) {
                e.setCancelled(true);
                TLocale.sendTo(player, "COMMAND-CONTROLLER.DENY");
            }
        }
    }

}
