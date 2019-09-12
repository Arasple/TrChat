package me.arasple.mc.litechat.api;

import me.arasple.mc.litechat.filter.FilteredObject;
import me.arasple.mc.litechat.filter.WordFilter;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2019/8/18 0:18
 * <p>
 * will come, fucking soon
 */
public class LiteChatAPI {

    /**
     * 根据玩家的权限，过滤的字符串
     *
     * @param player 玩家
     * @param string 字符串
     * @return 过滤后的
     */
    public static FilteredObject filterString(Player player, String string) {
        return WordFilter.doFilter(string, !player.hasPermission("litechat.bypass.filter"));
    }

    public static FilteredObject filterString(Player player, String string, boolean execute) {
        return execute ? filterString(player, string) : new FilteredObject(string, 0);
    }

}
