package me.arasple.mc.litechat.formats.format;

import io.izzel.taboolib.module.tellraw.TellrawJson;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * @author Arasple
 * @date 2019/8/5 11:19
 */
public class PrivateFormat extends Format {

    public PrivateFormat(Map<String, Object> map) {
        super(map);
    }

    @Override
    public TellrawJson getDisplay(Player sender, String... args) {
        TellrawJson display = TellrawJson.create();
        getParts().forEach(p -> display.append(p.toTellrawJson(sender, args[0])));
        display.append(getMsgPart().toTellrawJson(sender, args[1], false));
        return display;
    }

}
