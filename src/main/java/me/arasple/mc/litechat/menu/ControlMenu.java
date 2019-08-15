package me.arasple.mc.litechat.menu;

import io.izzel.taboolib.module.command.lite.CommandBuilder;
import io.izzel.taboolib.module.inject.TSchedule;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.item.ItemBuilder;
import io.izzel.taboolib.util.item.inventory.MenuBuilder;
import me.arasple.mc.litechat.LiteChat;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2019/8/4 21:06
 */
public class ControlMenu {

    private static MenuBuilder menu;

    @SuppressWarnings("ConstantConditions")
    @TSchedule(delay = 10)
    public static void init() {
        CommandBuilder.create("litechat", LiteChat.getInst())
                .permission("litechat.admin")
                .aliases("lchat")
                .permissionMessage(TLocale.asString("GENERAL.NO-PERMISSION"))
                .execute(((sender, args) -> {
                    if (!(sender instanceof Player)) {
                        TLocale.sendTo(sender, "GENERAL.NOT-PLAYER");
                    } else {
                        openFor((Player) sender);
                    }
                }))
                .build();

        menu = new MenuBuilder(LiteChat.getInst())
                .title("LiteChat Control-Menu")
                .rows(5)
                .lockHand()
                .items(
                        "#########",
                        "$       $",
                        "$   A   $",
                        "$       $",
                        "#########"
                )
                .put('#', new ItemBuilder(Material.getMaterial("CYAN_STAINED_GLASS_PANE", false)).name("§8LiteChat - Plane").build())
                .put('$', new ItemBuilder(Material.getMaterial("GREEN_STAINED_GLASS_PANE", false)).name("§8LiteChat - Plane").build())
                .event(e -> {
                    e.castClick().setCancelled(true);
                    Player p = e.getClicker();
                    char slot = e.getSlot();

                    if (slot == 'A') {
                        boolean result = LiteChat.switchDebug();
                        TLocale.sendTo(p, "PLUGIN.DEBUG." + (result ? "ON" : "OFF"));
                        p.closeInventory();
                    }
                })
        ;
    }

    public static void openFor(Player p) {
        menu.put('A', new ItemBuilder(Material.STICK).shiny().name("§9调试模式").lore("", "§6调试模式开启后会向后台", "§6输出更多详细内容.", "", "§8▪ §7当前状态: " + (LiteChat.isDebug() ? "§aON √" : "§cOFF ×"), "", "§3点击切换调试开关状态").build());
        p.openInventory(menu.build());
    }

}
