package me.arasple.mc.litechat.data;

import io.izzel.taboolib.module.db.local.LocalPlayer;
import io.izzel.taboolib.module.inject.TFunction;
import io.izzel.taboolib.module.inject.TSchedule;
import io.izzel.taboolib.module.tellraw.TellrawJson;
import me.arasple.mc.litechat.LiteChat;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author Arasple
 * @date 2019/8/15 12:37
 */
@TFunction(disable = "save")
public class DataHandler {

    private static HashMap<ItemStack, TellrawJson> ITEMSHOW_CACHE = new HashMap<>();
    private static HashMap<UUID, Cooldowns> COOLDOWNS = new HashMap<>();

    @TSchedule(delay = 20, period = 20 * 10)
    public static void save() {
        COOLDOWNS.forEach((key, value) -> {
            OfflinePlayer player = Bukkit.getOfflinePlayer(key);
            LocalPlayer.get(player).set("LITECHAT.COOLDOWNS", value.writeBase64());
        });
    }

    private static void cleanCooldowns() {
        COOLDOWNS.forEach((key, value) -> value.getCooldowns().removeIf(c -> System.currentTimeMillis() > c.getTime()));
        COOLDOWNS.entrySet().removeIf(x -> x.getValue().getCooldowns().stream().noneMatch(c -> System.currentTimeMillis() < c.getTime()));
    }

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
        cleanCooldowns();
        return COOLDOWNS;
    }

    public static HashMap<ItemStack, TellrawJson> getItemshowCache() {
        if (LiteChat.isDebug()) {
            LiteChat.getTLogger().info("Iitemshow caches: " + ITEMSHOW_CACHE.size());
        }
        return ITEMSHOW_CACHE;
    }

    public static void initFor(Player p) {
        if (!LocalPlayer.get(p).isSet(String.valueOf(p.getUniqueId()))) {
            LocalPlayer.get(p).set("LITECHAT.LAST-ONLINE", System.currentTimeMillis());
        }
        if (LocalPlayer.get(p).isSet("LITECHAT.COOLDOWNS") && LocalPlayer.get(p).get("LITECHAT.COOLDOWNS") != null) {
            try {
                COOLDOWNS.put(p.getUniqueId(), (Cooldowns) new Cooldowns().readBase64(LocalPlayer.get(p).getString("LITECHAT.COOLDOWNS")));
            } catch (Exception e) {
                LiteChat.getTLogger().error("发生一个异常. 请通知作者! (暂时不影响正常使用)");
                e.printStackTrace();
            }
        }
    }

}
