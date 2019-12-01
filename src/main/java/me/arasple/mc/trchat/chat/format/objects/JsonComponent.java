package me.arasple.mc.trchat.chat.format.objects;

import io.izzel.taboolib.module.tellraw.TellrawJson;
import io.izzel.taboolib.util.Strings;
import me.arasple.mc.trchat.utils.Js;
import me.arasple.mc.trchat.utils.Vars;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Arasple
 * @date 2019/11/30 12:42
 */
public class JsonComponent {

    private String requirement;
    private String text;
    private String hover;
    private String suggest;
    private String command;
    private String url;

    public JsonComponent(String text, List<String> hover, String suggest, String command, String url) {
        this.text = text;
        this.hover = convertHoverText(hover);
        this.suggest = suggest;
        this.command = command;
        this.url = url;
    }

    public JsonComponent(LinkedHashMap partSection) {
        if (partSection.containsKey("text")) {
            setText(String.valueOf(partSection.get("text")));
        }
        if (partSection.containsKey("hover")) {
            setHover(convertHoverText(partSection.get("hover")));
        }
        if (partSection.containsKey("suggest")) {
            setSuggest(String.valueOf(partSection.get("suggest")));
        }
        if (partSection.containsKey("command")) {
            setCommand(String.valueOf(partSection.get("command")));
        }
        if (partSection.containsKey("url")) {
            setUrl(String.valueOf(partSection.get("url")));
        }
        if (partSection.containsKey("requirement")) {
            setRequirement(String.valueOf(partSection.get("requirement")));
        }
    }

    public static List<JsonComponent> loadList(Object parts) {
        List<JsonComponent> jsonComponents = new ArrayList<>();
        ((LinkedHashMap) parts).values().forEach(part -> jsonComponents.add(new JsonComponent((LinkedHashMap) part)));
        return jsonComponents;
    }

    public TellrawJson toTellrawJson(Player player, String... vars) {
        return toTellrawJson(player, false, vars);
    }

    public TellrawJson toTellrawJson(Player player, boolean function, String... vars) {
        TellrawJson tellraw = TellrawJson.create();
        if (!Js.checkCondition(player, getRequirement())) {
            return tellraw;
        }

        String text = getText();

        if (vars.length == 1 && !function) {
            text = String.valueOf(vars[0]);
        }
        if (vars.length > 0) {
            if (Boolean.parseBoolean(vars[0])) {
                text = text.replaceAll("%toplayer_name%", vars[2]);
            }
        }

        tellraw.append(text != null ? Vars.replace(player, text) : "§8[§fNull§8]");
        if (hover != null) {
            tellraw.hoverText(Vars.replace(player, Strings.replaceWithOrder(hover, vars)));
        }
        if (suggest != null) {
            tellraw.clickSuggest(Vars.replace(player, Strings.replaceWithOrder(suggest, vars)));
        }
        if (command != null) {
            tellraw.clickCommand(Vars.replace(player, Strings.replaceWithOrder(command, vars)));
        }
        if (url != null) {
            tellraw.clickOpenURL(Vars.replace(player, Strings.replaceWithOrder(url, vars)));
        }
        return tellraw;
    }

    public String convertHoverText(Object object) {
        List<String> hovers;
        if (object instanceof List) {
            hovers = (List<String>) object;
        } else {
            return String.valueOf(object);
        }
        StringBuilder hover = new StringBuilder();
        hovers.forEach(l -> hover.append(l).append("\n"));
        String result = hover.toString();
        result = result.substring(0, result.length() - 1);
        return result;
    }

    /*
    GETTERS && SETTERS
     */

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHover() {
        return hover;
    }

    public void setHover(String hover) {
        this.hover = hover;
    }

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
