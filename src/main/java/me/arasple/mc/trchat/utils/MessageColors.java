package me.arasple.mc.trchat.utils;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Arasple
 * @date 2019/8/15 20:52
 */
public class MessageColors {

    public static final List<Character> COLOR_CODES = Arrays.stream(ChatColor.values()).map(ChatColor::getChar).collect(Collectors.toList());

    private static final String COLOR_CHAR = String.valueOf(ChatColor.COLOR_CHAR);
    private static final String COLOR_PERMISSION_NODE = "trchat.color.";
    private static final String FORCE_CHAT_COLOR_PERMISSION_NODE = "trchat.color.force-defaultcolor.";

    public static List<String> replaceWithPermission(Player player, List<String> strings) {
        return player == null ? strings : strings.stream().map(string -> replaceWithPermission(player, string)).collect(Collectors.toList());
    }

    public static String replaceWithPermission(Player player, String string) {
        if (player == null) {
            return string;
        }

        for (Character code : COLOR_CODES) {
            if (player.hasPermission(COLOR_PERMISSION_NODE + code)) {
                string = StringUtils.replace(string, "&" + code, COLOR_CHAR + code);
            }
        }

        return string;
    }

    public static ChatColor catchDefaultMessageColor(Player player, ChatColor defaultColor) {
        if (player.hasPermission(FORCE_CHAT_COLOR_PERMISSION_NODE + "*")) {
            return defaultColor;
        }

        for (Character code : COLOR_CODES) {
            if (player.hasPermission(FORCE_CHAT_COLOR_PERMISSION_NODE + code)) {
                return ChatColor.getByChar(code);
            }
        }

        return defaultColor;
    }

}