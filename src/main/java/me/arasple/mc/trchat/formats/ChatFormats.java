package me.arasple.mc.trchat.formats;

import com.google.common.collect.Lists;
import io.izzel.taboolib.module.inject.TFunction;
import io.izzel.taboolib.module.inject.TSchedule;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.module.tellraw.TellrawJson;
import me.arasple.mc.trchat.TrChat;
import me.arasple.mc.trchat.TrChatFiles;
import me.arasple.mc.trchat.formats.format.Format;
import me.arasple.mc.trchat.formats.format.PrivateFormat;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.cloud.CloudExpansion;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Arasple
 * @date 2019/8/4 14:51
 */
@TFunction(disable = "unload")
public class ChatFormats {

    private static List<Format> normal = Lists.newArrayList();
    private static List<Format> global = Lists.newArrayList();
    private static List<Format> staff = Lists.newArrayList();
    private static List<PrivateFormat> private_sender = Lists.newArrayList();
    private static List<PrivateFormat> private_receiver = Lists.newArrayList();

    @TSchedule
    public static void loadFormats() {
        loadFormats(Bukkit.getConsoleSender());
    }

    public static void loadFormats(CommandSender... notify) {
        Bukkit.getScheduler().runTaskAsynchronously(TrChat.getPlugin(), () -> {
            long start = System.currentTimeMillis();
            normal.clear();
            global.clear();
            staff.clear();
            private_sender.clear();
            private_receiver.clear();

            try {
                TrChatFiles.getFormats().getMapList("NORMAL").forEach(x -> normal.add(new Format((Map<String, Object>) x)));
                TrChatFiles.getFormats().getMapList("GLOBAL").forEach(x -> global.add(new Format((Map<String, Object>) x)));
                TrChatFiles.getFormats().getMapList("STAFF").forEach(x -> staff.add(new Format((Map<String, Object>) x)));
                TrChatFiles.getFormats().getMapList("PRIVATE-SENDER").forEach(x -> private_sender.add(new PrivateFormat((Map<String, Object>) x)));
                TrChatFiles.getFormats().getMapList("PRIVATE-RECEIVER").forEach(x -> private_receiver.add(new PrivateFormat((Map<String, Object>) x)));
            } catch (Throwable e) {
                for (CommandSender sender : notify) {
                    sender.sendMessage("§8[§3Tr§bChat§8]§8[§6WARN§8] §6--------------------------------------------------");
                    sender.sendMessage("§8[§3Tr§bChat§8]§8[§7INFO§8] §6加载聊天格式过程中发生错误, 请检查配置节点. 抛出异常如下");
                    sender.sendMessage("§4" + e.getMessage());
                    sender.sendMessage("§8" + Arrays.toString(e.getStackTrace()));
                    sender.sendMessage("§8[§3Tr§bChat§8]§8[§6WARN§8] §6--------------------------------------------------");
                }
            }

            for (CommandSender sender : notify) {
                TLocale.sendTo(sender, "PLUGIN.LOADED-CHAT-FORMATS", System.currentTimeMillis() - start);
            }
        });

        checkDepends();
    }

    /**
     * 检测需要的 PAPI 依赖并自动下载
     */
    private static void checkDepends() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null || !Bukkit.getPluginManager().getPlugin("PlaceholderAPI").isEnabled()) {
            return;
        }

        List<String> dependExpansions = TrChatFiles.getSettings().getStringList("GENERAL.DEPEND-EXPANSIONS");
        if (dependExpansions != null && dependExpansions.size() > 0) {
            if (PlaceholderAPIPlugin.getInstance().getExpansionCloud().getCloudExpansions().isEmpty()) {
                PlaceholderAPIPlugin.getInstance().getExpansionCloud().fetch(false);
            }
            List<String> unInstalled = dependExpansions.stream().filter(d -> PlaceholderAPI.getExpansions().stream().noneMatch(e -> e.getName().equalsIgnoreCase(d)) && PlaceholderAPIPlugin.getInstance().getExpansionCloud().getCloudExpansion(d) != null && !PlaceholderAPIPlugin.getInstance().getExpansionCloud().isDownloading(d)).collect(Collectors.toList());
            if (unInstalled.size() > 0) {
                unInstalled.forEach(ex -> {
                    CloudExpansion cloudExpansion = PlaceholderAPIPlugin.getInstance().getExpansionCloud().getCloudExpansion(ex);
                    PlaceholderAPIPlugin.getInstance().getExpansionCloud().downloadExpansion(null, cloudExpansion);
                });
                PlaceholderAPIPlugin.getInstance().getExpansionManager().registerAllExpansions();
            }
        }
    }

    /*
    GETTERS
     */

    public static TellrawJson getNormal(Player player, String message) {
        Format format = normal.stream().filter(x -> x.hasPermission(player)).findFirst().orElse(null);
        return format == null ? null : format.getDisplay(player, message);
    }

    public static TellrawJson getGlobal(Player player, String message) {
        Format format = global.stream().filter(x -> x.hasPermission(player)).findFirst().orElse(null);
        return format == null ? null : format.getDisplay(player, message);
    }

    public static TellrawJson getStaff(Player player, String message) {
        Format format = staff.stream().filter(x -> x.hasPermission(player)).findFirst().orElse(null);
        return format == null ? null : format.getDisplay(player, message);
    }

    public static TellrawJson getPrivateSender(Player player, String receiver, String message) {
        PrivateFormat format = private_sender.stream().filter(x -> x.hasPermission(player)).findFirst().orElse(null);
        return format == null ? null : format.getDisplay(player, receiver, message);
    }

    public static TellrawJson getPrivateReceiver(Player player, String receiver, String message) {
        PrivateFormat format = private_receiver.stream().filter(x -> x.hasPermission(player)).findFirst().orElse(null);
        return format == null ? null : format.getDisplay(player, receiver, message);
    }

}
