package me.arasple.mc.trchat.data;

import io.izzel.taboolib.module.db.local.LocalPlayer;
import io.izzel.taboolib.module.tellraw.TellrawJson;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author Arasple
 * @date 2019/11/30 11:30
 */
public class Users {

    private static HashMap<ItemStack, TellrawJson> itemCache = new HashMap<>();
    private static HashMap<UUID, Cooldowns> cooldowns = new HashMap<>();
    private static HashMap<UUID, String> message = new HashMap<>();

    public static long getCooldownLeft(UUID uuid, Cooldowns.CooldownType type) {
        cooldowns.putIfAbsent(uuid, new Cooldowns());
        for (Cooldowns.Cooldown COOLDOWN : cooldowns.get(uuid).getCooldowns()) {
            if (COOLDOWN.getId().equalsIgnoreCase(type.getName())) {
                return COOLDOWN.getTime() - System.currentTimeMillis();
            }
        }
        return -1;
    }

    public static boolean isInCooldown(UUID uuid, Cooldowns.CooldownType type) {
        return getCooldownLeft(uuid, type) > 0;
    }

    public static void updateCooldown(UUID uuid, Cooldowns.CooldownType type, long lasts) {
        cooldowns.putIfAbsent(uuid, new Cooldowns());
        cooldowns.get(uuid).getCooldowns().removeIf(c -> c.getId().equalsIgnoreCase(type.getName()));
        cooldowns.get(uuid).getCooldowns().add(new Cooldowns.Cooldown(type.getName(), System.currentTimeMillis() + lasts));
    }

    public static HashMap<UUID, Cooldowns> getCooldowns() {
        return cooldowns;
    }

    public static HashMap<ItemStack, TellrawJson> getItemCache() {
        return itemCache;
    }

    public static boolean isFilterEnabled(Player user) {
        return LocalPlayer.get(user).getBoolean("TRCHAT.FILTER", true);
    }

    public static void setFilter(Player user, boolean value) {
        LocalPlayer.get(user).set("TRCHAT.FILTER", value);
    }

    public static List<String> getIgnoredList(Player user) {
//        if (!LocalPlayer.get(user).isSet("TRCHAT.IGNORED")) {
//            LocalPlayer.get(user).set("TRCHAT.IGNORED", new ArrayList<>());
//        }
//        return LocalPlayer.get(user).getStringList("TRCHAT.IGNORED");
        return new ArrayList<>();
    }

    public static String getLastMessage(UUID uuid) {
        return message.getOrDefault(uuid, "");
    }

    public static void setLastMessage(UUID uuid, String msg) {
        message.put(uuid, msg);
    }

}
