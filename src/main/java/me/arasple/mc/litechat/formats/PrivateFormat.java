package me.arasple.mc.litechat.formats;

import io.izzel.taboolib.module.tellraw.TellrawJson;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2019/8/5 11:19
 */
public class PrivateFormat extends Format {

    public PrivateFormat(ConfigurationSection section) {
        super(section);
    }

    public TellrawJson replaceFor(Player sender, String receiver, String message) {
        TellrawJson result = TellrawJson.create();
        getParts().forEach(p -> result.append(p.toTellrawJson(sender, receiver)));
        result.append(getMsgPart().toTellrawJson(sender, message));
        return result;
    }

}
