package me.arasple.mc.trchat.chat.format.objects;

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.module.tellraw.TellrawJson;
import io.izzel.taboolib.util.Strings;
import io.izzel.taboolib.util.Variables;
import io.izzel.taboolib.util.item.Items;
import me.arasple.mc.trchat.TrChatFiles;
import me.arasple.mc.trchat.data.Cooldowns;
import me.arasple.mc.trchat.data.Users;
import me.arasple.mc.trchat.func.ChatFunctions;
import me.arasple.mc.trchat.func.imp.Function;
import me.arasple.mc.trchat.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Arasple
 * @date 2019/11/30 12:42
 */
public class MsgComponent extends JsonComponent {

    private boolean isPrivateChat;
    private ChatColor defualtColor;

    public MsgComponent(String text, List<String> hover, String suggest, String command, String url) {
        super(text, hover, suggest, command, url);
    }

    public MsgComponent(LinkedHashMap partSection) {
        super(partSection);
        setDefualtColor(ChatColor.getByChar(String.valueOf(partSection.get("default-color"))));
    }

    public TellrawJson toMsgTellraw(Player player, String message) {
        message = MessageColors.replaceWithPermission(player, message);

        TellrawJson tellraw = TellrawJson.create();
        for (Function function : ChatFunctions.getFunctions().stream().filter(f -> Js.checkCondition(player, f.getRequirement())).collect(Collectors.toList())) {
            message = Pts.replacePattern(message, function.getPattern(), function.getFilterTextPattern(), "<" + function.getName() + ":{0}>");
        }
        // At & Item Show
        boolean atEnabled = Users.getCooldownLeft(player.getUniqueId(), Cooldowns.CooldownType.MENTION) <= 0;
        String atFormat = TrChatFiles.getFunction().getStringColored("GENERAL.MENTION.FORMAT");
        if (atEnabled) {
            for (String p : Players.getPlayers()) {
                if (p.equalsIgnoreCase(player.getName())) {
                    continue;
                }
                message = message.replaceAll("(?i)(@)?" + p, "<AT:" + p + ">");
            }
        }
        boolean itemDisplayEnabled = TrChatFiles.getFunction().getBoolean("GENERAL.ITEM-SHOW.ENABLE", true);
        List<String> itemKeys = TrChatFiles.getFunction().getStringList("GENERAL.ITEM-SHOW.KEYS");
        String itemFormat = TrChatFiles.getFunction().getStringColored("GENERAL.ITEM-SHOW.FORMAT", "§8[§3{0} §bx{1}§8]");
        if (itemDisplayEnabled) {
            for (String key : itemKeys) {
                for (int i = 0; i < 9; i++) {
                    message = message.replace(key + "-" + i, "<ITEM:" + i + ">");
                }
                message = message.replace(key, "<ITEM:" + player.getInventory().getHeldItemSlot() + ">");
            }
        }

        // Custom Functions
        for (Variables.Variable v : new Variables(message).find().getVariableList()) {
            if (v.isVariable()) {
                String[] args = v.getText().split(":", 2);
                if (itemDisplayEnabled && "ITEM".equalsIgnoreCase(args[0])) {
                    int slot = NumberUtils.toInt(args[1], player.getInventory().getHeldItemSlot());
                    ItemStack item = player.getInventory().getItem(slot) != null ? player.getInventory().getItem(slot) : new ItemStack(Material.AIR);
                    tellraw.append(Users.getItemCache().computeIfAbsent(item, i -> TellrawJson.create().append(Strings.replaceWithOrder(itemFormat, Items.isNull(item) ? "空气" : Items.getName(item), item.getType() != Material.AIR ? item.getAmount() : 1) + defualtColor).hoverItem(item)));
                    continue;
                }
                if (atEnabled && "AT".equalsIgnoreCase(args[0]) && !isPrivateChat) {
                    String atPlayer = args[1];
                    tellraw.append(Strings.replaceWithOrder(atFormat, atPlayer) + defualtColor);
                    if (TrChatFiles.getFunction().getBoolean("GENERAL.MENTION.NOTIFY") && Bukkit.getPlayerExact(atPlayer) != null && Bukkit.getPlayerExact(atPlayer).isOnline()) {
                        TLocale.sendTo(Bukkit.getPlayer(atPlayer), "MENTIONS.NOTIFY", player.getName());
                    }
                    Users.updateCooldown(player.getUniqueId(), Cooldowns.CooldownType.MENTION, TrChatFiles.getFunction().getLong("GENERAL.MENTION.COOLDOWNS"));
                    continue;
                }
                Function function = ChatFunctions.mathFunction(args[0]);
                if (function != null) {
                    tellraw.append(function.getDisplayJson().toTellrawJson(player, true, args[1]));
                    continue;
                }
            }
            tellraw.append(toTellrawPart(player, defualtColor + v.getText()));
        }
        return tellraw;
    }

    public TellrawJson toTellrawPart(Player player, String text) {
        TellrawJson tellraw = TellrawJson.create();
        tellraw.append(text != null ? text : "§8[§fNull§8]");
        if (getHover() != null) {
            tellraw.hoverText(Vars.replace(player, getHover()));
        }
        if (getSuggest() != null) {
            tellraw.clickSuggest(Vars.replace(player, getSuggest()));
        }
        if (getCommand() != null) {
            tellraw.clickCommand(Vars.replace(player, getCommand()));
        }
        if (getUrl() != null) {
            tellraw.clickOpenURL(Vars.replace(player, getUrl()));
        }
        return tellraw;
    }

    public ChatColor getDefualtColor() {
        return defualtColor;
    }

    public void setDefualtColor(ChatColor defualtColor) {
        this.defualtColor = defualtColor;
    }

    public boolean isPrivateChat() {
        return isPrivateChat;
    }

    public void setPrivateChat(boolean privateChat) {
        isPrivateChat = privateChat;
    }

}
