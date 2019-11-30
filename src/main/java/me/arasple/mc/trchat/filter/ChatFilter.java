package me.arasple.mc.trchat.filter;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.izzel.taboolib.module.inject.TSchedule;
import me.arasple.mc.trchat.TrChat;
import me.arasple.mc.trchat.TrChatFiles;
import me.arasple.mc.trchat.TrChatPlugin;
import me.arasple.mc.trchat.filter.processer.Filter;
import me.arasple.mc.trchat.filter.processer.FilteredObject;
import me.arasple.mc.trchat.utils.Notifys;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * @author Arasple
 * @date 2019/10/12 20:52
 */
public class ChatFilter {

    private static String CHATFILTER_CLOUD_LAST_UPDATE;
    private static final String[] CHATFILTER_CLOUD_URL = new String[]{
            "https://arasple.oss-cn-beijing.aliyuncs.com/plugins/TrChat/database.json",
            "https://raw.githubusercontent.com/Arasple/TrChat-Cloud/master/database.json",
    };

    @TSchedule(delay = 20 * 120, period = 30 * 60 * 20)
    static void asyncRefreshCloud() {
        loadCloudFilter(0);
    }

    /**
     * 加载聊天过滤器
     *
     * @param updateCloud 是否更新云端词库
     * @param notify      接受通知反馈
     */
    public static void loadFilter(boolean updateCloud, CommandSender... notify) {
        // 初始化本地配置
        Filter.setSensitiveWord(TrChatFiles.getFilter().getStringList("LOCAL"));
        Filter.setPunctuations(TrChatFiles.getFilter().getStringList("IGNORED-PUNCTUATIONS"));
        Filter.setReplacement(TrChatFiles.getFilter().getString("REPLACEMENT").charAt(0));

        // 更新云端词库
        if (updateCloud && TrChatFiles.getFilter().getBoolean("CLOUD-THESAURUS", true)) {
            loadCloudFilter(0, notify);
        } else {
            Notifys.notify(notify, "PLUGIN.LOADED-FILTER-LOCAL", TrChatFiles.getFilter().getStringList("LOCAL").size());
        }
    }

    /**
     * 加载云端聊天敏感词库
     *
     * @param url    尝试 URL 序号
     * @param notify 接受通知反馈
     */
    private static void loadCloudFilter(int url, CommandSender... notify) {
        Bukkit.getScheduler().runTaskAsynchronously(TrChat.getPlugin(), () -> {
            List<String> whitelist = TrChatFiles.getFilter().getStringList("WHITELIST");
            List<String> collected = Lists.newArrayList();
            String lastUpdateDate;

            try {
                JsonObject database = (JsonObject) new JsonParser().parse(TrChatPlugin.readFromURL(CHATFILTER_CLOUD_URL[url]));
                if (!database.has("lastUpdateDate") || !database.has("words")) {
                    throw new NullPointerException("Wrong database json object");
                }

                lastUpdateDate = database.get("lastUpdateDate").getAsString();
                if (CHATFILTER_CLOUD_LAST_UPDATE != null && !CHATFILTER_CLOUD_LAST_UPDATE.equals(lastUpdateDate)) {
                    CHATFILTER_CLOUD_LAST_UPDATE = lastUpdateDate;
                } else {
                    return;
                }

                database.get("words").getAsJsonArray().iterator().forEachRemaining(i -> {
                    String word = i.getAsString();
                    if (whitelist.stream().noneMatch(w -> w.equalsIgnoreCase(word))) {
                        collected.add(word);
                    }
                });
            } catch (Throwable e) {
                if (url == 0) {
                    loadCloudFilter(url + 1, notify);
                } else {
                    Notifys.notify(notify, "PLUGIN.LOADED-FILTER-LOCAL", TrChatFiles.getFilter().getStringList("LOCAL").size());
                    Notifys.notify(notify, "PLUGIN.FAILED-LOAD-FILTER-CLOUD");
                }
                return;
            }
            Notifys.notify(notify, "PLUGIN.LOADED-FILTER-LOCAL", TrChatFiles.getFilter().getStringList("LOCAL").size());
            Notifys.notify(notify, "PLUGIN.LOADED-FILTER-CLOUD", collected.size(), url, lastUpdateDate);
            Filter.addSensitiveWord(collected);
        });
    }

    /**
     * 过滤一个字符串
     *
     * @param string  待过滤字符串
     * @param execute 是否真的过滤
     * @return 过滤后的字符串
     */
    public static FilteredObject filter(String string, boolean... execute) {
        if (execute.length > 0 && !execute[0]) {
            return new FilteredObject(string, 0);
        } else {
            return Filter.doFilter(string);
        }
    }

}
