package me.arasple.mc.trchat.chat.listeners;

import io.izzel.taboolib.module.inject.TListener;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.Strings;
import me.arasple.mc.trchat.TrChatFiles;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
        Map.Entry<String, String[]> mCmd = Bukkit.getCommandAliases().entrySet().stream().filter(entry -> Arrays.stream(entry.getValue()).anyMatch(m -> m.equalsIgnoreCase(e.getMessage().substring(1).split(" ")[0]))).findFirst().orElse(null);

        if (TrChatFiles.getFunction().getBoolean("GENERAL.COMMAND-CONTROLLER.ENABLE", true) && Strings.nonEmpty(command) && !player.hasPermission(TrChatFiles.getFunction().getString("GENERAL.COMMAND-CONTROLLER.BYPASS", "trchat.admin"))) {
            boolean whitelist = TrChatFiles.getFunction().getString("GENERAL.COMMAND-CONTROLLER.TYPE", "BLACKLIST").equalsIgnoreCase("WHITELIST");
            List<String> matches = TrChatFiles.getFunction().getStringList("GENERAL.COMMAND-CONTROLLER.LIST");
            boolean matched = matches.stream().anyMatch(m -> {
                boolean exact = m.toLowerCase().contains("<exact>");
                m = m.replaceAll("(?i)<exact>", "");

                if (exact) {
                    return command.matches("(?i)" + m);
                } else {
                    return command.split(" ")[0].matches("(?i)" + m) || (mCmd != null && mCmd.getKey().matches("(?i)" + m));
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
