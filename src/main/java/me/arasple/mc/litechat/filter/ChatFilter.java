package me.arasple.mc.litechat.filter;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.izzel.taboolib.module.inject.TSchedule;
import io.izzel.taboolib.module.locale.TLocale;
import me.arasple.mc.litechat.LiteChat;
import me.arasple.mc.litechat.LiteChatFiles;
import me.arasple.mc.litechat.LiteChatPlugin;
import me.arasple.mc.litechat.filter.process.Filter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * @author Arasple
 * @date 2019/10/12 20:52
 */
public class ChatFilter {

    private static final String CLOUD_URL = "https://raw.githubusercontent.com/Arasple/LiteChat-Cloud/master/database.json";

    private static int blockSending;
    private static boolean[] enable;

    @TSchedule
    public static void init() {
        loadFilter(true, Bukkit.getConsoleSender());
    }

    public static void loadFilter(boolean updateCloud, CommandSender... notify) {
        Bukkit.getScheduler().runTaskAsynchronously(LiteChat.getPlugin(), () -> {
            long start = System.currentTimeMillis();

            Filter.setSensitiveWord(LiteChatFiles.getFilter().getStringList("LOCAL"));
            Filter.setPunctuations(LiteChatFiles.getFilter().getStringList("IGNORED-PUNCTUATIONS"));
            Filter.setReplacement(LiteChatFiles.getFilter().getString("REPLACEMENT").charAt(0));

            blockSending = LiteChatFiles.getFilter().getInt("BLOCK-SENDING", 3);
            enable = new boolean[]{
                    LiteChatFiles.getFilter().getBoolean("ENABLE.CHAT", true),
                    LiteChatFiles.getFilter().getBoolean("ENABLE.SIGN", true),
                    LiteChatFiles.getFilter().getBoolean("ENABLE.ANVIL", true)
            };

            for (CommandSender sender : notify) {
                TLocale.sendTo(sender, "PLUGIN.LOADED-FILTER-LOCAL", LiteChatFiles.getFilter().getStringList("LOCAL").size(), System.currentTimeMillis() - start);
            }

            // 更新云端敏感词库
            if (updateCloud && LiteChatFiles.getFilter().getBoolean("CLOUD-THESAURUS", true)) {
                List<String> whitelist = LiteChatFiles.getFilter().getStringList("WHITELIST");
                List<String> collected = Lists.newArrayList();
                String lastUpdateDate = "Unknow";

                try {
                    JsonObject database = (JsonObject) new JsonParser().parse(LiteChatPlugin.readFromURL(CLOUD_URL));
                    if (!database.has("lastUpdateDate") || !database.has("words")) {
                        throw new NullPointerException("Wrong database json object");
                    }

                    lastUpdateDate = database.get("lastUpdateDate").getAsString();
                    database.get("words").getAsJsonArray().iterator().forEachRemaining(i -> {
                        String word = i.getAsString();
                        if (whitelist.stream().noneMatch(w -> w.equalsIgnoreCase(word))) {
                            collected.add(word);
                        }
                    });

                } catch (Throwable e) {
                    for (CommandSender sender : notify) {
                        TLocale.sendTo(sender, "PLUGIN.FAILED-LOAD-FILTER-CLOUD");
                    }
                }

                for (CommandSender sender : notify) {
                    TLocale.sendTo(sender, "PLUGIN.LOADED-FILTER-CLOUD", collected.size(), lastUpdateDate);
                }

                Filter.addSensitiveWord(collected);
            }
        });
    }

    public static int getBlockSending() {
        return blockSending;
    }


    /**
     * @return [0] = Chat, [1] = Sign; [2] = Anvil
     */
    public static boolean[] getEnable() {
        return enable;
    }

}
