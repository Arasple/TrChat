package me.arasple.mc.litechat.menu;

import io.izzel.taboolib.module.inject.TFunction;
import io.izzel.taboolib.util.item.inventory.MenuBuilder;
import me.arasple.mc.litechat.LiteChat;

/**
 * @author Arasple
 * @date 2019/8/4 21:06
 */
@TFunction(enable = "init")
public class ControlMenu {

    private static MenuBuilder menu;

    public static void init() {
        menu = new MenuBuilder(LiteChat.getInst())
                .title("LiteChat Control-Menu")
                .rows(5)
                .lockHand()
                .items(
                        "########C",
                        "$       $",
                        "$       $",
                        "$       $",
                        "#########"
                )
        ;
    }

}
