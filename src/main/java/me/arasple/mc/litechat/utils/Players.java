package me.arasple.mc.litechat.utils;

import com.google.common.collect.Lists;
import io.izzel.taboolib.module.inject.TSchedule;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.Objects;

/**
 * @author Arasple
 * @date 2019/8/4 21:28
 */
public class Players {

    private static List<String> players = Lists.newArrayList();

    @TSchedule(delay = 20, period = 20 * 5)
    public static void updateOnline() {
        if (Bukkit.getOnlinePlayers().size() > 0) {
            if (Bungees.isEnable()) {
                Bungees.sendBungeeData(Bukkit.getOnlinePlayers().iterator().next(), "PlayerList", "ALL");
            }
            Bukkit.getOnlinePlayers().forEach(p -> {
                if (!players.contains(p.getName())) {
                    players.add(p.getName());
                }
            });
            players.removeIf(p -> Bukkit.getPlayer(p) == null || !Objects.requireNonNull(Bukkit.getPlayer(p)).isOnline());
        }
    }

    /*
    GETTERS & SETTERS
     */
    public static List<String> getPlayers() {
        return players;
    }

    public static void setPlayers(List<String> players) {
        Players.players = players;
    }

    public static boolean isPlayerOnline(String target) {
        return players.stream().anyMatch(p -> p.equalsIgnoreCase(target));
    }

    public static String getPlayerFullName(String target) {
        return players.stream().filter(p -> p.equalsIgnoreCase(target)).findFirst().orElse(null);
    }

}
