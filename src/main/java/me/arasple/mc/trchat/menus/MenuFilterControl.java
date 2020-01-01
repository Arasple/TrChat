package me.arasple.mc.trchat.menus;

import io.izzel.taboolib.util.item.ItemBuilder;
import io.izzel.taboolib.util.item.inventory.MenuBuilder;
import io.izzel.taboolib.util.lite.Materials;
import io.izzel.taboolib.util.lite.SoundPack;
import me.arasple.mc.trchat.data.Users;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Arasple
 * @date 2019/11/30 11:40
 * Internal Menu For Chat Filter Control
 */
public class MenuFilterControl {

    public static final SoundPack SOUND = new SoundPack("BLOCK_NOTE_BLOCK_PLING-1-2");

    public static void displayFor(Player player) {
        MenuBuilder.builder()
                .rows(5)
                .title("TrChat Filter")
                .items(
                        "#########",
                        "",
                        "    A    ",
                        "",
                        "#########"
                )
                .put('#', new ItemBuilder(Materials.CYAN_STAINED_GLASS_PANE.parseItem()).name("§3TrChat §bFilter").build())
                .put('A', getToggleButton(player))
                .event(e -> {
                    if (e.getSlot() == 'A') {
                        Users.setFilter(player, !Users.isFilterEnabled(player));
                    }
                    displayFor(player);
                }).open(player);
        SOUND.play(player);
    }

    private static ItemStack getToggleButton(Player player) {
        ItemBuilder item;
        if (Users.isFilterEnabled(player)) {
            item = new ItemBuilder(Materials.LIME_STAINED_GLASS_PANE.parseItem()).name("§3聊天过滤器 §a√").lore("", "§7你已经开启聊天过滤器,", "§7系统将会为您过滤掉聊天", "§7内容中的敏感内容, 祝您游戏愉快", "", "§6▶ §e点击关闭此功能");
        } else {
            item = new ItemBuilder(Materials.RED_STAINED_GLASS_PANE.parseItem()).name("§8聊天过滤器 §c×").lore("", "§7你当前已关闭聊天过滤器,", "§7系统将不会为您过滤掉聊天", "§7内容中的敏感内容...", "", "§2▶ §a点击开启此功能");
        }
        return item.build();
    }

}
