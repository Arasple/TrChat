package me.arasple.mc.litechat.formats.part;

import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.module.tellraw.TellrawJson;
import io.izzel.taboolib.util.Strings;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * @author Arasple
 * @date 2019/10/12 20:20
 */
public class JsonPart {

    private String text;
    private String hover;
    private String command;
    private String suggest;
    private String url;
    private String permRequire;

    public JsonPart(Map<String, String> map) {
        this(
                map.getOrDefault("text", null),
                map.getOrDefault("hover", null),
                map.getOrDefault("command", null),
                map.getOrDefault("suggest", null),
                map.getOrDefault("url", null),
                map.getOrDefault("permission", null)
        );
    }

    public JsonPart(String text, String hover, String command, String suggest, String url, String permRequire) {
        this.text = text;
        this.hover = hover;
        this.command = command;
        this.suggest = suggest;
        this.url = url;
        this.permRequire = permRequire;
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

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String process(CommandSender sender, String str) {
        return process(sender, str, null);
    }

    public String process(CommandSender sender, String str, String value) {
        return TLocale.Translate.setPlaceholders(sender, str != null ? str
                .replace('&', ChatColor.COLOR_CHAR)
                .replace("{PLAYER}", sender.getName())
                .replace("{SENDER}", sender.getName())
                .replace("{RECEIVER}", value == null ? "" : value)
                : null);
    }

    public TellrawJson applyJson(Player player, TellrawJson tellraw) {
        return applyJson(player, tellraw, null);
    }

    public TellrawJson applyJson(Player player, TellrawJson tellraw, String value) {
        if (hover != null) {
            tellraw.hoverText(process(player, hover, value));
        }
        if (command != null) {
            tellraw.clickCommand(process(player, command, value));
        }
        if (suggest != null) {
            tellraw.clickSuggest(process(player, suggest, value));
        }
        if (url != null) {
            tellraw.clickOpenURL(process(player, url, value));
        }

        return tellraw;
    }

    public TellrawJson toTellrawJson(Player player, String value) {
        if (!Strings.isEmpty(permRequire) && !player.hasPermission(permRequire)) {
            return TellrawJson.create();
        }
        return applyJson(player, TellrawJson.create().append(process(player, getText() == null ? "§8[§cERROR-NULL§8]" : getText(), value)), value);
    }

}
