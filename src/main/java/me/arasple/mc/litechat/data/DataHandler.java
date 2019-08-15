package me.arasple.mc.litechat.data;

import io.izzel.taboolib.module.inject.TSchedule;
import me.arasple.mc.litechat.LCFiles;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author Arasple
 * @date 2019/8/15 12:37
 */

public class DataHandler {

    private static HashMap<UUID, Cooldowns> cooldowns = new HashMap<>();

    @TSchedule(delay = 5)
    public static void init() {
        if (!LCFiles.getData().isSet("UserData")) {
            LCFiles.getData().set("UserData", null);
        } else {
            LCFiles.getData().getConfigurationSection("UserData").getKeys(false).forEach(x -> {
                UUID uuid = UUID.fromString(x);

                if (LCFiles.getData().isSet("UserData." + x + ".cooldowns")) {
                    cooldowns.put(uuid, (Cooldowns) new Cooldowns().read(LCFiles.getData().getString("UserData." + x + ".cooldowns")));
                }
            });
        }
    }

    private static void cleanCooldowns() {
        cooldowns.forEach((key, value) -> value.getCooldowns().removeIf(c -> System.currentTimeMillis() > c.getTime()));
        cooldowns.entrySet().removeIf(x -> x.getValue().getCooldowns().stream().noneMatch(c -> System.currentTimeMillis() < c.getTime()));
    }

    public static long getCooldownLeft(UUID uuid, Cooldowns.CooldownType type) {
        cooldowns.putIfAbsent(uuid, new Cooldowns());
        for (Cooldowns.Cooldown cooldown : cooldowns.get(uuid).getCooldowns()) {
            if (cooldown.getId().equalsIgnoreCase(type.getName())) {
                return cooldown.getTime() - System.currentTimeMillis();
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
        cleanCooldowns();
        return cooldowns;
    }

    public static void initFor(Player p) {
        if (!LCFiles.getData().isSet("UserData." + p.getUniqueId())) {
            LCFiles.getData().set("UserData." + p.getUniqueId() + ".last-online", System.currentTimeMillis());
        }
    }

}
