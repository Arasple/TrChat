package me.arasple.mc.trchat.hook;

import io.izzel.taboolib.module.inject.THook;
import me.arasple.mc.trchat.TrChat;
import me.arasple.mc.trchat.data.Users;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2019/11/30 11:35
 */
@THook
public class TrChatPlaceholders extends PlaceholderExpansion {

    @Override
    public String getIdentifier() {
        return "TRCHAT";
    }

    @Override
    public String getAuthor() {
        return "ARASPLE";
    }

    @Override
    public String getVersion() {
        return String.valueOf(TrChat.getTrVersion());
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        if (player == null || !player.isOnline()) {
            return null;
        }

        if ("FILTER".equalsIgnoreCase(params)) {
            return String.valueOf(Users.isFilterEnabled(player));
        }
        return null;
    }

}
