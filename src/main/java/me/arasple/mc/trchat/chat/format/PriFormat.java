package me.arasple.mc.trchat.chat.format;

import io.izzel.taboolib.module.tellraw.TellrawJson;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * @author Arasple
 * @date 2019/12/1 11:42
 */
public class PriFormat extends Format {

    public PriFormat(Map<?, ?> formatMap) {
        super(formatMap);
    }

    @Override
    public TellrawJson apply(Player player, String... message) {
        TellrawJson format = TellrawJson.create();
        getJsons().forEach(x -> format.append(x.toTellrawJson(player, false, "true", message[1], message[2])));
        format.append(getMsg().toTellrawJson(player, message[0]));
        return format;
    }

}
