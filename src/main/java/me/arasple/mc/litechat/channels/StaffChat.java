package me.arasple.mc.litechat.channels;

import com.google.common.collect.Lists;
import io.izzel.taboolib.module.tellraw.TellrawJson;
import io.izzel.taboolib.util.chat.ComponentSerializer;
import me.arasple.mc.litechat.bstats.Metrics;
import me.arasple.mc.litechat.formats.ChatFormats;
import me.arasple.mc.litechat.utils.BungeeUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

/**
 * @author Arasple
 * @date 2019/8/16 17:02
 */
public class StaffChat {

    public static void execute(Player player, String message) {
        if (player.hasPermission("litechat.staff")) {
            TellrawJson format = ChatFormats.getStaff(player, message);
            if (BungeeUtils.isEnable()) {
                String raw = ComponentSerializer.toString(format.getComponentsAll());
                BungeeUtils.sendBungeeData(player, "LiteChat", "SendRawPerm", raw, "litechat.staff");
            } else {
                Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission("litechat.staff")).forEach(format::send);
            }
            Metrics.increaseChatTimes();
        }
    }

    private static List<UUID> staffs = Lists.newArrayList();

    public static boolean switchStaff(Player player) {
        if (!staffs.contains(player.getUniqueId())) {
            staffs.add(player.getUniqueId());
        } else {
            staffs.remove(player.getUniqueId());
        }
        return staffs.contains(player.getUniqueId());
    }

    public static boolean isInStaffChannel(Player player) {
        return staffs.contains(player.getUniqueId());
    }

}
