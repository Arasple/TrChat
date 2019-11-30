package me.arasple.mc.trchat.utils;

import io.izzel.taboolib.module.locale.TLocale;
import org.bukkit.command.CommandSender;

/**
 * @author Arasple
 * @date 2019/11/30 10:07
 */
public class Notifys {

    public static void notify(CommandSender[] senders, String path, Object... args) {
        for (CommandSender sender : senders) {
            TLocale.sendTo(sender, path, args);
        }
    }

}
