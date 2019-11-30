package me.arasple.mc.trchat.menus;

import io.izzel.taboolib.util.item.inventory.MenuBuilder;
import io.izzel.taboolib.util.lite.Materials;
import io.izzel.taboolib.util.lite.SoundPack;
import me.arasple.mc.trchat.data.Users;
import me.arasple.mc.trchat.utils.ItemEdit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Arasple
 * @date 2019/11/30 11:40
 * Internal Menu For Chat Filter Control
 */
public class MenuFilterControl {

    public static void displayFor(Player player) {
        new SoundPack("BLOCK_NOTE_BLOCK_PLING-1-2").play(player);
        player.openInventory(MenuBuilder.builder()
                .title("TrChat Filter")
                .rows(5)
                .items(
                        "#########",
                        "",
                        "    A    ",
                        "",
                        "#########"
                )
                .put('#', new ItemEdit(Materials.CYAN_STAINED_GLASS_PANE.parseItem()).name("§3TrChat §bFilter").build())
                .put('A', getToggleButton(player))
                .event(e -> {
                    if (e.getSlot() == 'A') {
                        Users.setFilter(player, !Users.isFilterEnabled(player));
                    }
                    displayFor(player);
                })
                .build());
    }

    private static ItemStack getToggleButton(Player player) {
        ItemEdit item;
        if (Users.isFilterEnabled(player)) {
            item = new ItemEdit(Materials.LIME_STAINED_GLASS_PANE.parseItem()).name("§3聊天过滤器 §a√").lore("", "§7你已经开启聊天过滤器,", "§7系统将会为您过滤掉聊天", "§7内容中的敏感内容, 祝您游戏愉快", "", "§6▶ §e点击关闭此功能");
        } else {
            item = new ItemEdit(Materials.RED_STAINED_GLASS_PANE.parseItem()).name("§8聊天过滤器 §c×").lore("", "§7你已经开启聊天过滤器,", "§7系统将会为您过滤掉聊天", "§7内容中的敏感内容, 祝您游戏愉快", "", "§2▶ §a点击开启此功能");
        }
        return item.build();
    }

}
