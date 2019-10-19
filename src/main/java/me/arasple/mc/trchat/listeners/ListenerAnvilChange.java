package me.arasple.mc.trchat.listeners;

import io.izzel.taboolib.module.inject.TListener;
import me.arasple.mc.trchat.TrChatFiles;
import me.arasple.mc.trchat.api.TrChatAPI;
import me.arasple.mc.trchat.filter.ChatFilter;
import me.arasple.mc.trchat.utils.MessageColors;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author Arasple
 * @date 2019/8/15 21:18
 */
@TListener
public class ListenerAnvilChange implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onAnvilCraft(PrepareAnvilEvent e) {
        Player p = (Player) e.getView().getPlayer();
        ItemStack result = e.getResult();

        if (e.getInventory().getType() != InventoryType.ANVIL || result == null || result.getType() == Material.AIR) {
            return;
        }
        ItemMeta meta = result.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) {
            return;
        }
        String name = meta.getDisplayName();
        if (TrChatFiles.getSettings().getBoolean("CHAT-COLOR.ANVIL")) {
            name = MessageColors.replaceWithPermission(p, name);
        }
        meta.setDisplayName(TrChatAPI.filterString(p, name, ChatFilter.getEnable()[2]).getFiltered());
        result.setItemMeta(meta);
        e.setResult(result);
    }

}
