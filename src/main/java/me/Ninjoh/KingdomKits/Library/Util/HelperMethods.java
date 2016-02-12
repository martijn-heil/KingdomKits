package me.Ninjoh.KingdomKits.Library.Util;

import org.bukkit.inventory.ItemStack;


public class HelperMethods
{
    /**
     * Check if an item is soulbound.
     *
     * @param item ItemStack to check.
     * @return Returns true if the item is soulbound.
     */
    public static boolean isSoulBound(ItemStack item)
    {
        try
        {
            return item.getItemMeta().getLore().contains("§6§oSoulbound");
        }
        catch(NullPointerException e)
        {
            //
        }

        return false;
    }
}
