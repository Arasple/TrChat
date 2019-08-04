package me.arasple.mc.litechat.formats;

import com.google.common.collect.Lists;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.module.tellraw.TellrawJson;
import io.izzel.taboolib.util.Strings;
import io.izzel.taboolib.util.item.Items;
import me.arasple.mc.litechat.LCFiles;
import me.arasple.mc.litechat.LiteChat;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author Arasple
 * @date 2019/8/4 15:25
 */
public class Format {

    private List<ChatPart> parts;
    private MessagePart msgPart;

    public Format(ConfigurationSection section) {
        parts = Lists.newArrayList();

        section.getKeys(false).forEach(x -> {
            ConfigurationSection s = section.getConfigurationSection(x);
            if (s != null) {
                if (!"message".equals(x)) {
                    parts.add(new ChatPart(s));
                } else {
                    msgPart = new MessagePart(section.getString("default-color", "7"), s);
                }
            } else {
                LiteChat.getTLogger().warn("&7加载聊天格式中发生错误. 请检查此节点: " + x);
            }
        });
    }

    public TellrawJson replaceFor(Player player, String message) {
        TellrawJson result = TellrawJson.create();
        parts.forEach(p -> result.append(p.toTellrawJson(player, player.getName())));
        result.append(msgPart.toTellrawJson(player, message));
        return result;
    }

    /**
     * 聊天组件
     */
    public static class ChatPart {

        private String text;
        private String hover;
        private String command;
        private String suggest;
        private String url;

        public ChatPart(ConfigurationSection section) {
            this(
                    section.getString(".text", null),
                    section.getString(".hover", null),
                    section.getString(".command", null),
                    section.getString(".suggest", null),
                    section.getString(".url", null)
            );
        }

        public ChatPart(String text, String hover, String command, String suggest, String url) {
            this.text = text;
            this.hover = hover;
            this.command = command;
            this.suggest = suggest;
            this.url = url;
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
            return TLocale.Translate.setPlaceholders(sender, str != null ? str
                    .replace('&', ChatColor.COLOR_CHAR)
                    .replace("{PLAYER}", sender.getName())
                    : null);
        }

        public TellrawJson applyJson(Player player, TellrawJson tellraw) {
            if (hover != null) {
                tellraw.hoverText(process(player, hover));
            }
            if (command != null) {
                tellraw.clickCommand(process(player, command));
            }
            if (suggest != null) {
                tellraw.clickSuggest(process(player, suggest));
            }
            if (url != null) {
                tellraw.clickOpenURL(process(player, url));
            }

            return tellraw;
        }

        public TellrawJson toTellrawJson(Player player, String value) {
            return applyJson(player, TellrawJson.create().append(process(player, getText() == null ? "§8[§cERROR-NULL§8]" : getText())));
        }

    }

    /**
     * 组件 - 消息部分
     */
    public static class MessagePart extends ChatPart {

        private ChatColor defaultColor;

        public MessagePart(String defaultColor, ConfigurationSection section) {
            super(section);

            this.defaultColor = ChatColor.getByChar(defaultColor);
        }

        @Override
        public TellrawJson toTellrawJson(Player p, String value) {
            List<String> keys = LCFiles.getSettings().getStringList("ChatControl.item-show.keys");
            String format = LCFiles.getSettings().getStringColored("ChatControl.item-show.format", "§8[§3{0} §bx{1}§8]");
            String key = null;
            String[] args;
            for (String k : keys) {
                if (value.toLowerCase().contains(k)) {
                    key = k;
                } else {
                    value = value.replace(k, "");
                }
            }
            if (key != null) {
                while (value.lastIndexOf(key) != value.indexOf(key)) {
                    value = value.replaceFirst(key, "");
                }
                args = value.split(key).length > 0 ? value.split(key) : new String[]{""};
            } else {
                args = new String[]{value};
            }
            TellrawJson tellraw = applyJson(p, TellrawJson.create().append(defaultColor + args[0]));
            if (key != null) {
                ItemStack i = p.getInventory().getItemInMainHand();
                tellraw.append(Strings.replaceWithOrder(format, Items.getName(i), i.getType() != Material.AIR ? i.getAmount() : 1)).hoverItem(i);

                if (args.length > 1) {
                    tellraw.append(applyJson(p, TellrawJson.create().append(defaultColor + args[1])));
                }
            }
            return tellraw;
        }
    }

}
