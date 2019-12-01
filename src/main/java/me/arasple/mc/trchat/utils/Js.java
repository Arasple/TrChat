package me.arasple.mc.trchat.utils;

import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.Strings;
import io.izzel.taboolib.util.lite.Scripts;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.script.SimpleBindings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Arasple
 * @date 2019/11/30 13:21
 */
public class Js {

    public static boolean checkCondition(Player player, String requirement) {
        if (Strings.isEmpty(requirement) || "null".equalsIgnoreCase(requirement)) {
            return true;
        }

        Map<String, Object> bind = new HashMap<>();
        bind.put("player", player);
        bind.put("bukkitServer", Bukkit.getServer());
        requirement = Vars.replace(player, requirement);
        try {
            return (boolean) Scripts.compile(requirement).eval(new SimpleBindings(bind));
        } catch (Throwable e) {
            TLocale.sendTo(player, "ERROR.JS", requirement, e.getMessage(), Arrays.toString(e.getStackTrace()));
            TLocale.sendToConsole("ERROR.JS", requirement, e.getMessage(), Arrays.toString(e.getStackTrace()));
        }
        return false;
    }

}
