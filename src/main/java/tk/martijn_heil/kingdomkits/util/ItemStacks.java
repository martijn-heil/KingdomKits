package tk.martijn_heil.kingdomkits.util;

import org.bukkit.inventory.ItemStack;


public class ItemStacks
{
    /**
     * Check if an item is soulbound.
     *
     * @param item ItemStack to check.
     * @return Returns true if the item is soulbound.
     */
    public static boolean isSoulBound(ItemStack item)
    {
        return (item.getItemMeta().getLore() != null) && item.getItemMeta().getLore().contains("§6§oSoulbound");
    }


    public static boolean isUnbreakable(ItemStack item)
    {
        return (item.getItemMeta().getLore() != null) && item.getItemMeta().getLore().contains("§6§oUnbreakable");
    }


    public static boolean isUseAllowed(ItemStack item)
    {
        return (item.getItemMeta().getLore() != null) && item.getItemMeta().getLore().contains("§6§oUse-Allowed");
    }


    public static boolean isCombatAllowed(ItemStack item)
    {
        return (item.getItemMeta().getLore() != null) && item.getItemMeta().getLore().contains("§6§oCombat-Allowed");
    }


    public static boolean isEquipAllowed(ItemStack item)
    {
        return (item.getItemMeta().getLore() != null) && item.getItemMeta().getLore().contains("§6§oEquip-Allowed");
    }


    public static boolean isConsumeAllowed(ItemStack item)
    {
        return (item.getItemMeta().getLore() != null) && item.getItemMeta().getLore().contains("§6§oConsume-Allowed");
    }
}
