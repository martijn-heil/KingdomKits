package tk.martijn_heil.kingdomkits.util;

import com.google.common.base.Preconditions;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class ItemStacks
{
    /**
     * Check if an item is soulbound.
     *
     * @param item ItemStack to check.
     * @return Returns true if the item is soulbound.
     */
    public static boolean isSoulBound(@NotNull ItemStack item)
    {
        Preconditions.checkNotNull(item, "item can not be null.");
        return (item.getItemMeta() != null) && (item.getItemMeta().getLore() != null) &&
                item.getItemMeta().getLore().contains("§6§oSoulbound");
    }


    public static boolean isUnbreakable(@NotNull ItemStack item)
    {
        Preconditions.checkNotNull(item, "item can not be null.");
        return (item.getItemMeta().getLore() != null) && (item.getItemMeta().getLore() != null) &&
                item.getItemMeta().getLore().contains("§6§oUnbreakable");
    }


    public static boolean isUseAllowed(@NotNull ItemStack item)
    {
        Preconditions.checkNotNull(item, "item can not be null.");
        return (item.getItemMeta().getLore() != null) && (item.getItemMeta().getLore() != null) &&
                item.getItemMeta().getLore().contains("§6§oUse-Allowed");
    }


    public static boolean isCombatAllowed(@NotNull ItemStack item)
    {
        Preconditions.checkNotNull(item, "item can not be null.");
        return (item.getItemMeta().getLore() != null) && (item.getItemMeta().getLore() != null) &&
                item.getItemMeta().getLore().contains("§6§oCombat-Allowed");
    }


    public static boolean isEquipAllowed(@NotNull ItemStack item)
    {
        Preconditions.checkNotNull(item, "item can not be null.");
        return (item.getItemMeta().getLore() != null) && (item.getItemMeta().getLore() != null) &&
                item.getItemMeta().getLore().contains("§6§oEquip-Allowed");
    }


    public static boolean isConsumeAllowed(@NotNull ItemStack item)
    {
        Preconditions.checkNotNull(item, "item can not be null.");
        return (item.getItemMeta().getLore() != null) && (item.getItemMeta().getLore() != null) &&
                item.getItemMeta().getLore().contains("§6§oConsume-Allowed");
    }


    public static boolean isPartOfKit(@Nullable ItemStack item, @NotNull String kitName)
    {
        Preconditions.checkNotNull(kitName, "kitName can not be null.");
        return (item != null) && (item.getItemMeta().getLore() != null) && (item.getItemMeta().getLore() != null) &&
                item.getItemMeta().getLore().contains("§b§o" + kitName);
                                            // NOTE: §b instead of §6
    }
}
