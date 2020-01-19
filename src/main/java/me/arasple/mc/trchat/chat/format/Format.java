package me.arasple.mc.trchat.chat.format;

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils;
import io.izzel.taboolib.module.tellraw.TellrawJson;
import me.arasple.mc.trchat.chat.format.objects.JsonComponent;
import me.arasple.mc.trchat.chat.format.objects.MsgComponent;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Arasple
 * @date 2019/11/30 12:43
 */
public class Format {

    private int priority;
    private String requirement;
    private List<JsonComponent> jsons;
    private MsgComponent msg;

    public Format(Map<?, ?> formatMap) {
        this(formatMap.containsKey("priority") ? NumberUtils.toInt(String.valueOf(formatMap.get("priority")), 0) : Integer.MIN_VALUE, formatMap.containsKey("requirement") ? String.valueOf(formatMap.get("requirement")) : null, JsonComponent.loadList(formatMap.get("parts")), new MsgComponent((LinkedHashMap) formatMap.get("msg")));
    }

    public Format(int priority, String requirement, List<JsonComponent> jsons, MsgComponent msg) {
        this.priority = priority;
        this.requirement = requirement;
        this.jsons = jsons;
        this.msg = msg;
    }

    public TellrawJson apply(Player player, String... message) {
        TellrawJson format = TellrawJson.create();
        jsons.forEach(x -> format.append(x.toTellrawJson(player)));
        format.append(msg.toMsgTellraw(player, message[0]));
        return format;
    }

    /*
    GETTERS & SETTERS
     */

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public List<JsonComponent> getJsons() {
        return jsons;
    }

    public void setJsons(List<JsonComponent> jsons) {
        this.jsons = jsons;
    }

    public MsgComponent getMsg() {
        return msg;
    }

    public void setMsg(MsgComponent msg) {
        this.msg = msg;
    }

}
