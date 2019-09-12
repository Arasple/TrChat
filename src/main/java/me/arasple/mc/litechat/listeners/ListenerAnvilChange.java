package me.arasple.mc.litechat.listeners;

import io.izzel.taboolib.module.inject.TListener;
import me.arasple.mc.litechat.LiteChat;
import me.arasple.mc.litechat.api.LiteChatAPI;
import me.arasple.mc.litechat.utils.MessageColors;
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
        if (LiteChat.getSettings().getBoolean("CHAT-CONTROL.COLOR-CODE.ANVIL")) {
            name = MessageColors.processWithPermission(p, name);
        }
        meta.setDisplayName(LiteChatAPI.filterString(p, name, LiteChat.getSettings().getBoolean("CHAT-CONTROL.FILTER.ENABLE.ANVIL", true)).getFiltered());
        result.setItemMeta(meta);
        e.setResult(result);
    }

}
