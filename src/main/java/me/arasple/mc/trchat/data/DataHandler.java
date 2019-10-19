package me.arasple.mc.trchat.data;

import io.izzel.taboolib.module.tellraw.TellrawJson;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author Arasple
 * @date 2019/8/15 12:37
 */
public class DataHandler {

    private static HashMap<ItemStack, TellrawJson> ITEMSHOW_CACHE = new HashMap<>();
    private static HashMap<UUID, Cooldowns> COOLDOWNS = new HashMap<>();

    public static long getCooldownLeft(UUID uuid, Cooldowns.CooldownType type) {
        COOLDOWNS.putIfAbsent(uuid, new Cooldowns());
        for (Cooldowns.Cooldown COOLDOWN : COOLDOWNS.get(uuid).getCooldowns()) {
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
        COOLDOWNS.putIfAbsent(uuid, new Cooldowns());
        COOLDOWNS.get(uuid).getCooldowns().removeIf(c -> c.getId().equalsIgnoreCase(type.getName()));
        COOLDOWNS.get(uuid).getCooldowns().add(new Cooldowns.Cooldown(type.getName(), System.currentTimeMillis() + lasts));
    }

    public static HashMap<UUID, Cooldowns> getCooldowns() {
        return COOLDOWNS;
    }

    public static HashMap<ItemStack, TellrawJson> getItemshowCache() {
        return ITEMSHOW_CACHE;
    }

}
