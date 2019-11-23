package me.arasple.mc.trchat.utils;

import com.google.common.collect.Lists;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Arasple
 * @date 2019/10/6 21:59
 */
public class Vars {

    public static List<String> setPlaceholders(CommandSender player, List<String> strings) {
        List<String> results = Lists.newArrayList();
        strings.forEach(str -> results.add(setPlaceholders(player, str)));
        return results;
    }

    public static String setPlaceholders(CommandSender player, String string) {
        if (string == null || player == null) {
            return null;
        }
        if (player instanceof Player) {
            return PlaceholderAPI.setPlaceholders((Player) player, string);
        }
        return string;
    }

}
