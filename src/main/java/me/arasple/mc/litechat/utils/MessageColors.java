package me.arasple.mc.litechat.utils;

import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2019/8/15 20:52
 */
public class MessageColors {

    private static final String COLOR_PERMISSION_NODE = "litechat.color";

    public static String processWithPermission(Player player, String message) {
        for (ChatColor value : ChatColor.values()) {
            char code = value.getCode();
            String name = value.getName();

            if (player.hasPermission(COLOR_PERMISSION_NODE + "." + code) || player.hasPermission(COLOR_PERMISSION_NODE + "." + name)) {
                message = message.replace("&" + code, "§" + code);
            }
        }
        return message;
    }

    private enum ChatColor {
        /**
         * A custom copy of net.md_5.bungee.api.ChatColor
         */

        BLACK('0', "black"),
        DARK_BLUE('1', "dark_blue"),
        DARK_GREEN('2', "dark_green"),
        DARK_AQUA('3', "dark_aqua"),
        DARK_RED('4', "dark_red"),
        DARK_PURPLE('5', "dark_purple"),
        GOLD('6', "gold"),
        GRAY('7', "gray"),
        DARK_GRAY('8', "dark_gray"),
        BLUE('9', "blue"),
        GREEN('a', "green"),
        AQUA('b', "aqua"),
        RED('c', "red"),
        LIGHT_PURPLE('d', "light_purple"),
        YELLOW('e', "yellow"),
        WHITE('f', "white"),
        MAGIC('k', "obfuscated"),
        BOLD('l', "bold"),
        STRIKETHROUGH('m', "strikethrough"),
        UNDERLINE('n', "underline"),
        ITALIC('o', "italic"),
        RESET('r', "reset");

        private char code;
        private String name;

        ChatColor(char code, String name) {
            this.code = code;
            this.name = name;
        }

        public char getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

    }

}
