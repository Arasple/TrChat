package me.arasple.mc.litechat.menu;

import com.google.common.collect.Lists;
import io.izzel.taboolib.module.command.lite.CommandBuilder;
import io.izzel.taboolib.module.inject.TSchedule;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.item.ItemBuilder;
import io.izzel.taboolib.util.item.inventory.MenuBuilder;
import me.arasple.mc.litechat.LCFiles;
import me.arasple.mc.litechat.LiteChat;
import me.arasple.mc.litechat.updater.UpdateChecker;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * @author Arasple
 * @date 2019/8/4 21:06
 */
public class ControlMenu {

    private static MenuBuilder menu;

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
                        "$ U I D $",
                        "$       $",
                        "#########"
                )
                .put('#', new ItemBuilder(MaterialVersion.getGlassPane("CYAN")).damage(9).name("§8LiteChat - Plane").build())
                .put('$', new ItemBuilder(MaterialVersion.getGlassPane("GREEN")).damage(13).name("§8LiteChat - Plane").build())
                .put('I', new ItemBuilder(MaterialVersion.getSign()).name("§3§lLite§b§lChat §e§lInformation").lore("",
                        "§8▪ §7插件名称: §6LiteChat",
                        "§8▪ §7插件作者: §aArasple",
                        "§8▪ §7联系方式: §21197897763",
                        "",
                        "§8▪ §7支持版本: §a1.8 §7- §a1.14",
                        "§8▪ §7支持服务端: §3Spigot/Paper, BungeeCord/Waterfall",
                        "§8▪ §7开源地址: §bhttps://github.com/Arasple/LiteChat",
                        ""
                ).build())

                .event(e -> {
                    e.castClick().setCancelled(true);
                    Player p = e.getClicker();
                    char slot = e.getSlot();

                    if (slot == 'U' && UpdateChecker.isHasNewerVersion()) {
                        UpdateChecker.notifyUpdates(p);
                        p.closeInventory();
                    }
                    if (slot == 'D') {
                        boolean result = LiteChat.switchDebug();
                        TLocale.sendTo(p, "PLUGIN.DEBUG." + (result ? "ON" : "OFF"));
                        p.closeInventory();
                    }
                })
        ;
    }

    public static void openFor(Player p) {
        ItemBuilder updates = new ItemBuilder(MaterialVersion.getClock()).name("§e更新检测");
        List<String> lores = Lists.newArrayList();
        lores.add("");
        lores.add("§7当前版本: §2" + UpdateChecker.getVersion());

        if (!LCFiles.getSettings().getBoolean("General.check-update")) {
            lores.add("§c自动更新检测已被禁用.");
        } else {
            if (UpdateChecker.isHasNewerVersion()) {
                lores.add("§8▪ §7最新版本: §a" + UpdateChecker.getLatestVersion());
                lores.add("§8▪ §7更新内容: §a");
                lores.addAll(Arrays.asList(UpdateChecker.getUpdatesMessages()));
                lores.add("");
                lores.add("§a点击获取最新版下载地址.");
            } else {
                lores.add("§8▪ §b已经是最新版本了.");
            }
        }
        updates.lore(lores);

        menu.put('U', updates.build());
        menu.put('D', new ItemBuilder(Material.STICK).shiny().name("§9调试模式").lore("", "§6调试模式开启后会向后台", "§6输出更多详细内容.", "", "§8▪ §7当前状态: " + (LiteChat.isDebug() ? "§aON √" : "§cOFF ×"), "", "§3点击切换调试开关状态").build());
        p.openInventory(menu.build());
    }

    public static class MaterialVersion {

        private static boolean isNew = false;

        static {
            try {
                Material.valueOf("STAINED_GLASS_PANE");
            } catch (Exception e) {
                isNew = true;
            }
        }

        public static Material getGlassPane(String... color) {
            return isNew ? Material.valueOf(color[0] + "_STAINED_GLASS_PANE") : Material.valueOf("STAINED_GLASS_PANE");
        }

        public static Material getSign() {
            return isNew ? Material.valueOf("OAK_SIGN") : Material.valueOf("SIGN");
        }

        public static Material getClock() {
            return isNew ? Material.valueOf("CLOCK") : Material.valueOf("WATCH");
        }

    }

}
