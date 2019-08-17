package me.arasple.mc.litechat.formats;

import io.izzel.taboolib.module.inject.TFunction;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.module.tellraw.TellrawJson;
import me.arasple.mc.litechat.LCFiles;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2019/8/4 14:51
 */
@TFunction(disable = "unload")
public class ChatFormats {

    public static TellrawJson getNormal(Player player, String message) {
        return normal.replaceFor(player, message);
    }

    public static TellrawJson getGlobal(Player player, String message) {
        return global.replaceFor(player, message);
    }

    public static TellrawJson getStaff(Player player, String message) {
        return staff.replaceFor(player, message);
    }

    public static TellrawJson getPrivateSender(Player sender, String receiver, String message) {
        return private_sender.replaceFor(sender, receiver, message);
    }

    public static TellrawJson getPrivateReceiver(Player sender, String receiver, String message) {
        return private_receiver.replaceFor(sender, receiver, message);
    }

    private static Format normal, global, staff;
    private static PrivateFormat private_sender, private_receiver;

    public static void load(boolean notify) {
        long start = System.currentTimeMillis();
        normal = new Format(LCFiles.getSettings().getConfigurationSection("FORMAT-NORMAL"));
        private_sender = new PrivateFormat(LCFiles.getSettings().getConfigurationSection("FORMAT-PRIVATE-SENDER"));
        private_receiver = new PrivateFormat(LCFiles.getSettings().getConfigurationSection("FORMAT-PRIVATE-RECEIVER"));
        global = new Format(LCFiles.getSettings().getConfigurationSection("FORMAT-GLOBAL"));
        staff = new Format(LCFiles.getSettings().getConfigurationSection("FORMAT-STAFF"));

        if (notify) {
            TLocale.sendToConsole("PLUGIN.LOADED-CHAT-FORMATS", String.valueOf(System.currentTimeMillis() - start));
        }
    }

}
