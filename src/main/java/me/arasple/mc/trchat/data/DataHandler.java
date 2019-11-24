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

    private static HashMap<ItemStack, TellrawJson> itemCache = new HashMap<>();
    private static HashMap<UUID, Cooldowns> cooldown = new HashMap<>();

    public static long getCooldownLeft(UUID uuid, Cooldowns.CooldownType type) {
        return cooldown.computeIfAbsent(uuid, n -> new Cooldowns()).getCooldowns().stream().filter(COOLDOWN -> COOLDOWN.getId().equalsIgnoreCase(type.getName())).findFirst().map(COOLDOWN -> COOLDOWN.getTime() - System.currentTimeMillis()).orElse(-1L);
    }

    public static boolean isInCooldown(UUID uuid, Cooldowns.CooldownType type) {
        return getCooldownLeft(uuid, type) > 0;
    }

    public static void updateCooldown(UUID uuid, Cooldowns.CooldownType type, long lasts) {
        cooldown.putIfAbsent(uuid, new Cooldowns());
        cooldown.get(uuid).getCooldowns().removeIf(c -> c.getId().equalsIgnoreCase(type.getName()));
        cooldown.get(uuid).getCooldowns().add(new Cooldowns.Cooldown(type.getName(), System.currentTimeMillis() + lasts));
    }

    public static HashMap<UUID, Cooldowns> getCooldowns() {
        return cooldown;
    }

    public static HashMap<ItemStack, TellrawJson> getItemCache() {
        return itemCache;
    }

}
