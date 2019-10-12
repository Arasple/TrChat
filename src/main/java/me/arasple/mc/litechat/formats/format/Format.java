package me.arasple.mc.litechat.formats.format;

import com.google.common.collect.Lists;
import io.izzel.taboolib.module.tellraw.TellrawJson;
import me.arasple.mc.litechat.formats.part.JsonPart;
import me.arasple.mc.litechat.formats.part.MessagePart;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

/**
 * @author Arasple
 * @date 2019/8/4 15:25
 */
public class Format {

    private String permission;
    private List<JsonPart> parts = Lists.newArrayList();
    private MessagePart msgPart;

    /**
     * 通过节点加载一个聊天格式模板
     *
     * @param map 节点
     */
    public Format(Map<String, Object> map) {
        if (map == null) {
            return;
        }
        permission = map.containsKey("permission") ? String.valueOf(map.get("permission")) : null;
        Map<String, Object> part = (Map<String, Object>) map.getOrDefault("parts", null);
        if (part != null) {
            part.forEach((node, value) -> {
                Map<String, String> json = null;
                try {
                    json = (Map<String, String>) value;
                } catch (Throwable ignored) {

                }
                if (node != null && json != null) {
                    if (!"message".equalsIgnoreCase(node)) {
                        parts.add(new JsonPart(json));
                    } else {
                        msgPart = new MessagePart(json);
                    }
                }
            });
        }
    }

    /**
     * 替换该节点的相关变量转为 Tellraw
     *
     * @param player  玩家
     * @param message 消息
     * @return tellraw
     */
    public TellrawJson getDisplay(Player player, String... message) {
        TellrawJson display = TellrawJson.create();
        parts.forEach(p -> display.append(p.toTellrawJson(player, player.getName())));
        display.append(msgPart.toTellrawJson(player, message[0], true));
        return display;
    }

    /**
     * 判定该玩家是否有使用该格式的权限
     *
     * @param player 玩家
     * @return 是否
     */
    public boolean hasPermission(Player player) {
        return permission == null || "null".equalsIgnoreCase(permission) || player.hasPermission(permission);
    }

    /*
    GETTERS & SETTERS
     */

    public String getPermission() {
        return permission;
    }

    public List<JsonPart> getParts() {
        return parts;
    }

    public MessagePart getMsgPart() {
        return msgPart;
    }

}
