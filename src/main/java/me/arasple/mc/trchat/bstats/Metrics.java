package me.arasple.mc.trchat.bstats;

import io.izzel.taboolib.module.inject.TSchedule;
import me.arasple.mc.trchat.TrChat;
import me.arasple.mc.trchat.TrChatFiles;

import java.text.DecimalFormat;

/**
 * @author Arasple
 */
public class Metrics {

    private static MetricsBukkit metrics;
    private static DecimalFormat doubleFormat = new DecimalFormat("#.#");
    private static int[] counts = new int[]{0, 0};

    public static void increase(int index) {
        increase(index, 1);
    }

    public static void increase(int index, int value) {
        if (counts[index] < Integer.MAX_VALUE) {
            counts[index] += value;
        }
    }

    @TSchedule
    public static void init() {
        metrics = new MetricsBukkit(TrChat.getPlugin());

        // 聊天次数统计
        metrics.addCustomChart(new MetricsBukkit.SingleLineChart("chat_counts", () -> {
            int i = counts[0];
            counts[0] = 0;
            return i;
        }));
        // 敏感词过滤器启用统计
        metrics.addCustomChart(new MetricsBukkit.SingleLineChart("filter_counts", () -> {
            int i = counts[1];
            counts[1] = 0;
            return i;
        }));
        // 自动检测更新
        metrics.addCustomChart(new MetricsBukkit.SimplePie("update_checker", () -> TrChatFiles.getSettings().getBoolean("GENERAL.CHECK-UPDATE", true) ? "Enabled" : "Disabled"));
        // 调试模式
        metrics.addCustomChart(new MetricsBukkit.SimplePie("debug_mode", () -> TrChatFiles.getSettings().getBoolean("GENERAL.DEBUG", false) ? "Enabled" : "Disabled"));
    }

    public static MetricsBukkit getMetrics() {
        return metrics;
    }


}