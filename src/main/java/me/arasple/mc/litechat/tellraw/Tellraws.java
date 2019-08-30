package me.arasple.mc.litechat.tellraw;

import io.izzel.taboolib.module.inject.TInject;
import me.arasple.mc.litechat.tellraw.internal.BaseTellraws;

/**
 * @author Arasple
 * @date 2019/8/30 12:33
 */
public class Tellraws {

    @TInject(asm = "me.arasple.mc.litechat.tellraw.internal.InternalTellraws")
    private static BaseTellraws baseTellraws;

    public static BaseTellraws getBaseTellraws() {
        return baseTellraws;
    }

}
