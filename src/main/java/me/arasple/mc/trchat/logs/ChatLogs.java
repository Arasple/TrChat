package me.arasple.mc.trchat.logs;

import io.izzel.taboolib.module.inject.TFunction;
import io.izzel.taboolib.module.inject.TSchedule;
import io.izzel.taboolib.util.Files;
import io.izzel.taboolib.util.Strings;
import me.arasple.mc.trchat.TrChatFiles;
import org.bukkit.entity.Player;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Arasple
 * @date 2019/11/30 16:08
 */
@TFunction(disable = "write")
public class ChatLogs {

    private static List<String> wave;
    private static File logFile;

    public static void init() {
        logFile = new File("plugins/TrChat/logs/" + new SimpleDateFormat("yyyy-M-dd").format(new Date(System.currentTimeMillis())) + ".txt");
        wave = new ArrayList<>();
    }

    @TSchedule(delay = 20 * 10, period = 20 * 60 * 5, async = true)
    public static void write() {
        try {
            Files.file(logFile);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logFile), StandardCharsets.UTF_8));
            for (String l : wave) {
                bw.write(l + "\r\n");
            }
            bw.close();
            wave.clear();
        } catch (Throwable e) {
            System.out.println("Error when writing the logs.");
            e.printStackTrace();
        }
    }

    public static void log(Player player, String originalMessage) {
        String record = Strings.replaceWithOrder(TrChatFiles.getSettings().getStringColored("GENERAL.LOG", "[{0}] {1}: {2}"),
                new SimpleDateFormat("yyyy-M-dd HH:mm:ss").format(new Date(System.currentTimeMillis())),
                player.getName(),
                originalMessage
        );
        wave.add(record);
    }

    public static void logPrivate(Player player, String to, String originalMessage) {
        String record = Strings.replaceWithOrder(TrChatFiles.getSettings().getStringColored("GENERAL.LOG", "[{0}] {1} -> {2}: {3}"),
                new SimpleDateFormat("yyyy-M-dd HH:mm:ss").format(new Date(System.currentTimeMillis())),
                player.getName(),
                to,
                originalMessage
        );
        wave.add(record);
    }

}
