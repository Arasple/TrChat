package me.arasple.mc.litechat.formats;

import io.izzel.taboolib.module.inject.TFunction;
import io.izzel.taboolib.module.inject.TSchedule;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.module.tellraw.TellrawJson;
import me.arasple.mc.litechat.LCFiles;
import org.bukkit.entity.Player;

import java.util.Objects;

/**
 * @author Arasple
 * @date 2019/8/4 14:51
 */
@TFunction(disable = "unload")
public class ChatFormats {

    public static TellrawJson getNormal(Player player, String message) {
        return normal.replaceFor(player, message);
    }

    public static TellrawJson getPrivateSender(Player sender, String receiver, String message) {
        return private_sender.replaceFor(sender, message);
    }

    public static TellrawJson getPrivateReceiver(String sender, Player receiver, String message) {
        return private_receiver.replaceFor(receiver, message);
    }

    private static Format normal, private_sender, private_receiver, global, staff;

    @TSchedule(delay = 3)
    public static void load() {
        long start = System.currentTimeMillis();
        normal = new Format(Objects.requireNonNull(LCFiles.getSettings().getConfigurationSection("ChatFormats.NORMAL")));
        private_sender = new Format(Objects.requireNonNull(LCFiles.getSettings().getConfigurationSection("ChatFormats.PRIVATE-SENDER")));
        private_receiver = new Format(Objects.requireNonNull(LCFiles.getSettings().getConfigurationSection("ChatFormats.PRIVATE-RECEIVER")));
        TLocale.sendToConsole("PLUGIN.LOADED-CHAT-FORMATS", String.valueOf(System.currentTimeMillis() - start));
    }

}
