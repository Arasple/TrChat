package me.arasple.mc.trchat.utils;

import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * @author Arasple
 * @date 2019/8/15 20:52
 */
public class MessageColors {

    private static final List<Character> COLOR_CODES = Arrays.asList(
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
            'e', 'f', 'k', 'l', 'm', 'n', 'o', 'r'
    );

    private static final String COLOR_PERMISSION_NODE = "trchat.color";
    private static final String FORCE_CHAT_COLOR_PERMISSION_NODE = "trchat.color.force-defaultcolor.";

    public static List<String> replaceWithPermission(Player player, List<String> strings) {
        List<String> result = Lists.newArrayList();
        strings.forEach(s -> result.add(replaceWithPermission(player, s)));
        return result;
    }

    public static String replaceWithPermission(Player player, String string) {
        for (Character code : COLOR_CODES) {
            if (player.hasPermission(COLOR_PERMISSION_NODE + "." + code)) {
                string = string.replace("&" + code, "§" + code);
            }
        }
        return string;
    }

    public static ChatColor catchDefaultMessageColor(Player player, ChatColor defaultColor) {
        for (Character code : COLOR_CODES) {
            if (player.hasPermission(FORCE_CHAT_COLOR_PERMISSION_NODE + "." + code)) {
                return ChatColor.getByChar(code);
            }
        }
        return defaultColor;
    }

}