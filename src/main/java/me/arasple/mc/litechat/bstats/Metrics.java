package me.arasple.mc.litechat.bstats;

import io.izzel.taboolib.module.inject.TSchedule;
import me.arasple.mc.litechat.LiteChat;

import java.text.DecimalFormat;

/**
 * @author Arasple
 */
public class Metrics {

    private static MetricsBukkit metrics;
    private static DecimalFormat doubleFormat = new DecimalFormat("#.#");

    @TSchedule
    public static void init() {
        chat_times = 0;

        metrics = new MetricsBukkit(LiteChat.getPlugin());

        // 聊天次数统计
        metrics.addCustomChart(new MetricsBukkit.SingleLineChart("chat_times", () -> {
            int s = chat_times;
            chat_times = 0;
            return s;
        }));
        // 敏感词过滤器启用统计
        metrics.addCustomChart(new MetricsBukkit.SingleLineChart("filtered_words", () -> {
            int s = filtered_words;
            filtered_words = 0;
            return s;
        }));
        // 自动检测更新
        metrics.addCustomChart(new MetricsBukkit.SimplePie("update_checker", () -> LiteChat.getSettings().getBoolean("GENERAL.CHECK-UPDATE", true) ? "Enabled" : "Disabled"));
        // 独立世界频道聊天
        metrics.addCustomChart(new MetricsBukkit.SimplePie("per_world_chat", () -> LiteChat.getSettings().getBoolean("GENERAL.PER-WORLD-CHAT", false) ? "Enabled" : "Disabled"));
        // 调试模式
        metrics.addCustomChart(new MetricsBukkit.SimplePie("debug_mode", () -> LiteChat.getSettings().getBoolean("GENERAL.DEBUG", false) ? "Enabled" : "Disabled"));
        // 聊天冷却
        metrics.addCustomChart(new MetricsBukkit.SimplePie("chat_cooldown", () -> {
            double cd = LiteChat.getSettings().getDouble("CHAT-CONTROL.COOLDOWN", 2.0);
            return cd < 20 ? doubleFormat.format(cd) : "2.0";
        }));
        // 聊天字符长度限制
        metrics.addCustomChart(new MetricsBukkit.SimplePie("chat_length_limit", () -> String.valueOf(LiteChat.getSettings().getInt("CHAT-CONTROL.LENGTH-LIMIT", 100))));
    }

    public static MetricsBukkit getMetrics() {
        return metrics;
    }

    private static int chat_times, filtered_words;

    public static void increaseChatTimes() {
        chat_times++;
    }

    public static void increaseFilteredWords(int i) {
        filtered_words += i;
    }

}