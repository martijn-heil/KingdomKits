package me.ninjoh.kingdomkits.util;

import org.bukkit.inventory.ItemStack;


public class ItemStackUtils
{
    /**
     * Check if an item is soulbound.
     *
     * @param item ItemStack to check.
     * @return Returns true if the item is soulbound.
     */
    public static boolean isSoulBound(ItemStack item)
    {
        return item.getItemMeta().getLore().contains("§6§oSoulbound");
    }
}
