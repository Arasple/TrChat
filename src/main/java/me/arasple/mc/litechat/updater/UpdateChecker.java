package me.arasple.mc.litechat.updater;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils;
import io.izzel.taboolib.module.inject.TListener;
import io.izzel.taboolib.module.inject.TSchedule;
import io.izzel.taboolib.module.locale.TLocale;
import me.arasple.mc.litechat.LCFiles;
import me.arasple.mc.litechat.LiteChat;
import me.arasple.mc.litechat.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * @author Arasple
 * @date 2019/8/15 22:35
 */
@TListener
public class UpdateChecker implements Listener {

    private static final String URL = "https://api.github.com/repos/Arasple/LiteChat/releases/latest";
    private static double version = -1;
    private static double latestVersion = -1;
    private static boolean hasNewerVersion = false;
    private static String[] updatesMessages;
    private static boolean[] noticed = new boolean[]{false, false};

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (!noticed[1] && hasNewerVersion && p.hasPermission("litechat.admin")) {
            notifyUpdates(p);
            noticed[1] = true;
        }
    }

    @TSchedule(delay = 20, period = 30 * 60 * 20, async = true)
    public static void onCheck() {
        if (!LCFiles.getSettings().getBoolean("GENERAL.CHECK-UPDATE")) {
            return;
        }

        if (version == -1) {
            version = NumberUtils.toDouble(LiteChat.getInst().getDescription().getVersion().split("-")[0], -1);
            if (version == -1) {
                LiteChat.getTLogger().error("检测版本号时发生异常... 关闭服务器!");
                Bukkit.shutdown();
            }
        }

        String read;
        try (InputStream inputStream = new URL(URL).openStream(); BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
            read = Plugin.readFully(bufferedInputStream, StandardCharsets.UTF_8);

            JsonObject json = (JsonObject) new JsonParser().parse(read);
            double latestVersion = json.get("tag_name").getAsDouble();
            if (latestVersion > version) {
                if (hasNewerVersion) {
                    if (UpdateChecker.latestVersion != -1 && latestVersion > UpdateChecker.latestVersion) {
                        UpdateChecker.latestVersion = latestVersion;
                        updatesMessages = json.get("body").getAsString().replace("\r", "").split("\n");
                        noticed[1] = false;
                        notifyUpdates(Bukkit.getConsoleSender());
                    }
                } else {
                    hasNewerVersion = true;
                    noticed[0] = true;
                    UpdateChecker.latestVersion = latestVersion;
                    updatesMessages = json.get("body").getAsString().replace("\r", "").split("\n");
                    notifyUpdates(Bukkit.getConsoleSender());
                }
            } else if (!noticed[0]) {
                noticed[0] = true;
                TLocale.sendToConsole("PLUGIN.UPDATE-NOTIFY.LATEST");
            }
        } catch (Exception t) {
            if (LiteChat.isDebug()) {
                LiteChat.getTLogger().warn("[Updater-Checker]: ");
                t.printStackTrace();
            }
        }
    }

    public static void notifyUpdates(CommandSender sender) {
        List<String> messages = Lists.newArrayList();
        messages.addAll(TLocale.asStringList("PLUGIN.UPDATE-NOTIFY.HEADER", String.valueOf(version), String.valueOf(latestVersion)));
        messages.addAll(Arrays.asList(getUpdatesMessages()));
        messages.addAll(TLocale.asStringList("PLUGIN.UPDATE-NOTIFY.FOOTER"));
        messages.forEach(sender::sendMessage);
    }

    public static boolean hasNewerVersion() {
        return hasNewerVersion;
    }

    public static double getVersion() {
        return version;
    }

    public static double getLatestVersion() {
        return latestVersion;
    }

    public static String[] getUpdatesMessages() {
        return updatesMessages != null ? updatesMessages : new String[]{};
    }

}
