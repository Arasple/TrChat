package me.arasple.mc.trchat.utils;

import com.google.common.collect.Lists;
import io.izzel.taboolib.module.inject.TSchedule;
import me.arasple.mc.trchat.TrChat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Arasple
 * @date 2019/8/4 21:28
 */
public class Players {

    private static List<String> players = Lists.newArrayList();

    @TSchedule(delay = 20)
    public static void startTask() {
        if (Bungees.isEnable()) {
            Bukkit.getScheduler().runTaskTimer(TrChat.getPlugin(), () -> {
                if (Bukkit.getOnlinePlayers().size() > 0) {
                    Bungees.sendBungeeData(Bukkit.getOnlinePlayers().iterator().next(), "PlayerList", "ALL");
                }
            }, 0, 60);
        }
    }

    /*
    GETTERS & SETTERS
     */

    public static void setPlayers(List<String> players) {
        Players.players = players;
    }

    public static boolean isPlayerOnline(String target) {
        Player player = Bukkit.getPlayerExact(target);
        return (player != null && player.isOnline()) || players.stream().anyMatch(p -> p.equalsIgnoreCase(target));
    }

    public static String getPlayerFullName(String target) {
        Player player = Bukkit.getPlayerExact(target);
        return (player != null && player.isOnline()) ? player.getName() : players.stream().filter(p -> p.equalsIgnoreCase(target)).findFirst().orElse(null);
    }

    public static List<String> getPlayers() {
        List<String> players = Lists.newArrayList();
        Bukkit.getOnlinePlayers().forEach(x -> players.add(x.getName()));
        players.addAll(Players.players);
        return players;
    }

}
