package me.arasple.mc.trchat.logs;

import com.google.common.collect.Lists;
import io.izzel.taboolib.module.inject.TFunction;
import io.izzel.taboolib.module.inject.TSchedule;
import io.izzel.taboolib.util.Files;
import io.izzel.taboolib.util.Strings;
import jdk.nashorn.internal.objects.annotations.Function;
import me.arasple.mc.trchat.TrChatFiles;
import org.bukkit.entity.Player;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Arasple
 * @date 2019/11/30 16:08
 */
public class ChatLogs {

    private static List<String> waveList = Lists.newArrayList();
    private static SimpleDateFormat dateFormat0 = new SimpleDateFormat("yyyy-M-dd");
    private static SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-M-dd");

    @TSchedule(delay = 20 * 15, period = 20 * 60 * 5, async = true)
    @TFunction.Cancel
    public static void writeToFile() {
        File logFile = Files.file("plugins/TrChat/logs/" + dateFormat0.format(System.currentTimeMillis()) + ".txt");
        Files.write(logFile, writer -> {
            for (String line : waveList) {
                writer.write(line);
                writer.newLine();
            }
        });
        waveList.clear();
    }

    public static void log(Player player, String originalMessage) {
        waveList.add(Strings.replaceWithOrder(TrChatFiles.getSettings().getStringColored("GENERAL.LOG", "[{0}] {1}: {2}"),
                dateFormat1.format(System.currentTimeMillis()),
                player.getName(),
                originalMessage
        ));
    }

    public static void logPrivate(String from, String to, String originalMessage) {
        waveList.add(Strings.replaceWithOrder(TrChatFiles.getSettings().getStringColored("GENERAL.LOG", "[{0}] {1} -> {2}: {3}"),
                dateFormat1.format(System.currentTimeMillis()),
                from,
                to,
                originalMessage
        ));
    }
}
