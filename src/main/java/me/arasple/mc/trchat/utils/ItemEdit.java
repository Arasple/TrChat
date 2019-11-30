package me.arasple.mc.trchat.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * @author Arasple
 * @date 2019/11/30 11:54
 */
public class ItemEdit {

    private ItemStack itemStack;
    private ItemMeta itemMeta;

    public ItemEdit(ItemStack item) {
        this.itemStack = item;
        this.itemMeta = item.getItemMeta();
    }

    public ItemEdit name(String string) {
        itemMeta.setDisplayName(string);
        return this;
    }

    public ItemStack build() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemEdit lore(String... lores) {
        itemMeta.setLore(Arrays.asList(lores));
        return this;
    }

}
