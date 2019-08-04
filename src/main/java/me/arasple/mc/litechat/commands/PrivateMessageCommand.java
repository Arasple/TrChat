package me.arasple.mc.litechat.commands;

import io.izzel.taboolib.module.command.lite.CommandBuilder;
import io.izzel.taboolib.module.inject.TFunction;
import io.izzel.taboolib.module.locale.TLocale;
import me.arasple.mc.litechat.LiteChat;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2019/8/4 21:19
 */
@TFunction(enable = "register")
public class PrivateMessageCommand {

    public static void register() {
        CommandBuilder
                .create("msg", LiteChat.getInst())
                .aliases("tell")
                .execute(((sender, args) -> {
                    if (!(sender instanceof Player)) {
                        TLocale.sendTo(sender, "ERROR.NOT-PLAYER");
                    }
                }))
                .build()
        ;
    }

}
