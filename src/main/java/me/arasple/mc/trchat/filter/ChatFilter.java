package me.arasple.mc.trchat.filter;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.izzel.taboolib.module.inject.TSchedule;
import io.izzel.taboolib.module.locale.TLocale;
import me.arasple.mc.trchat.TrChat;
import me.arasple.mc.trchat.TrChatFiles;
import me.arasple.mc.trchat.TrChatPlugin;
import me.arasple.mc.trchat.filter.process.Filter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * @author Arasple
 * @date 2019/10/12 20:52
 */
public class ChatFilter {

    private static final String CLOUD_URL = "https://raw.githubusercontent.com/Arasple/TrChat-Cloud/master/database.json";

    private static int blockSending;
    private static boolean[] enable;

    @TSchedule
    public static void init() {
        loadFilter(true, Bukkit.getConsoleSender());
    }

    public static void loadFilter(boolean updateCloud, CommandSender... notify) {
        Bukkit.getScheduler().runTaskAsynchronously(TrChat.getPlugin(), () -> {
            long start = System.currentTimeMillis();

            Filter.setSensitiveWord(TrChatFiles.getFilter().getStringList("LOCAL"));
            Filter.setPunctuations(TrChatFiles.getFilter().getStringList("IGNORED-PUNCTUATIONS"));
            Filter.setReplacement(TrChatFiles.getFilter().getString("REPLACEMENT").charAt(0));

            blockSending = TrChatFiles.getFilter().getInt("BLOCK-SENDING", 3);
            enable = new boolean[]{
                    TrChatFiles.getFilter().getBoolean("ENABLE.CHAT", true),
                    TrChatFiles.getFilter().getBoolean("ENABLE.SIGN", true),
                    TrChatFiles.getFilter().getBoolean("ENABLE.ANVIL", true)
            };

            for (CommandSender sender : notify) {
                TLocale.sendTo(sender, "PLUGIN.LOADED-FILTER-LOCAL", TrChatFiles.getFilter().getStringList("LOCAL").size(), System.currentTimeMillis() - start);
            }

            // 更新云端敏感词库
            if (updateCloud && TrChatFiles.getFilter().getBoolean("CLOUD-THESAURUS", true)) {
                List<String> whitelist = TrChatFiles.getFilter().getStringList("WHITELIST");
                List<String> collected = Lists.newArrayList();
                String lastUpdateDate = "Unknow";

                try {
                    JsonObject database = (JsonObject) new JsonParser().parse(TrChatPlugin.readFromURL(CLOUD_URL));
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
                    return;
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
