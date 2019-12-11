package me.arasple.mc.trchat.nms;

import me.arasple.mc.trchat.filter.ChatFilter;
import net.minecraft.server.v1_14_R1.IChatBaseComponent;
import net.minecraft.server.v1_14_R1.NonNullList;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Arasple
 * @date 2019/11/30 11:16
 */
public class InternalPacketUtils extends AbstractPacketUtils {

    @Override
    public Object filterIChatComponent(Object component) {
        try {
            String raw = IChatBaseComponent.ChatSerializer.a((IChatBaseComponent) component);
            String filtered = ChatFilter.filter(raw).getFiltered();

//            System.out.println(ChatColor.GRAY + "RAW: " + raw);
//            System.out.println(ChatColor.AQUA + "FILTERED: " + ChatColor.WHITE + filtered);

            return IChatBaseComponent.ChatSerializer.a(filtered);
        } catch (Throwable throwable) {
            return component;
        }
    }

    @Override
    public void filterItem(Object item) {
        ItemStack itemStack = CraftItemStack.asCraftMirror((net.minecraft.server.v1_14_R1.ItemStack) item);
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return;

        }
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            if (meta.hasLore() && meta.getLore() != null && meta.getLore().size() > 0) {
                List<String> lore = new ArrayList<>();
                meta.getLore().forEach(l -> lore.add(ChatFilter.filter(l).getFiltered()));
                meta.setLore(lore);
            }
            if (meta.hasDisplayName()) {
                String tran = ChatFilter.filter(meta.getDisplayName()).getFiltered();
                meta.setDisplayName(meta.getDisplayName().charAt(0) != ChatColor.COLOR_CHAR ? ChatColor.RESET + tran : tran);
            }
            itemStack.setItemMeta(meta);
        }
    }

    @Override
    public void filterItemList(Object items) {
        try {
            ((List<ItemStack>) items).forEach(this::filterItem);
        } catch (Throwable e) {
            try {
                ((NonNullList) items).forEach(this::filterItem);
            } catch (Throwable e2) {
                Arrays.asList((ItemStack[]) items).forEach(this::filterItem);
            }
        }
    }

}
