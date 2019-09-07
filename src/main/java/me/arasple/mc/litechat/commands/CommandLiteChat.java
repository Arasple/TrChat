package me.arasple.mc.litechat.commands;

import io.izzel.taboolib.module.command.lite.CommandBuilder;
import io.izzel.taboolib.module.inject.TSchedule;
import io.izzel.taboolib.module.locale.TLocale;
import me.arasple.mc.litechat.LiteChat;

/**
 * @author Arasple
 * @date 2019/8/4 21:06
 */
public class CommandLiteChat {

    @TSchedule
    public static void init() {
        CommandBuilder.create("litechat", LiteChat.getPlugin())
                .permission("litechat.admin")
                .aliases("lchat")
                .permissionMessage(TLocale.asString("GENERAL.NO-PERMISSION"))
                .execute(((sender, args) -> {
                    sender.sendMessage("§7暂时没有此命令...");
                }))
                .build();
    }

}
