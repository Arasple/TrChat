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

    private static HashMap<UUID, Cooldowns> COOLDOWNs = new HashMap<>();

    @TSchedule(delay = 5)
    public static void init() {
        if (!LCFiles.getData().isSet("UserData")) {
            LCFiles.getData().set("UserData", null);
        } else {
            LCFiles.getData().getConfigurationSection("UserData").getKeys(false).forEach(x -> {
                UUID uuid = UUID.fromString(x);

                if (LCFiles.getData().isSet("UserData." + x + ".COOLDOWNs")) {
                    COOLDOWNs.put(uuid, (Cooldowns) new Cooldowns().read(LCFiles.getData().getString("UserData." + x + ".COOLDOWNs")));
                }
            });
        }
    }

    private static void cleanCooldowns() {
        COOLDOWNs.forEach((key, value) -> value.getCooldowns().removeIf(c -> System.currentTimeMillis() > c.getTime()));
        COOLDOWNs.entrySet().removeIf(x -> x.getValue().getCooldowns().stream().noneMatch(c -> System.currentTimeMillis() < c.getTime()));
    }

    public static long getCooldownLeft(UUID uuid, Cooldowns.CooldownType type) {
        COOLDOWNs.putIfAbsent(uuid, new Cooldowns());
        for (Cooldowns.Cooldown COOLDOWN : COOLDOWNs.get(uuid).getCooldowns()) {
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
        COOLDOWNs.putIfAbsent(uuid, new Cooldowns());
        COOLDOWNs.get(uuid).getCooldowns().removeIf(c -> c.getId().equalsIgnoreCase(type.getName()));
        COOLDOWNs.get(uuid).getCooldowns().add(new Cooldowns.Cooldown(type.getName(), System.currentTimeMillis() + lasts));
    }

    public static HashMap<UUID, Cooldowns> getCooldowns() {
        cleanCooldowns();
        return COOLDOWNs;
    }

    public static void initFor(Player p) {
        if (!LCFiles.getData().isSet("UserData." + p.getUniqueId())) {
            LCFiles.getData().set("UserData." + p.getUniqueId() + ".last-online", System.currentTimeMillis());
        }
    }

}
