package tk.martijn_heil.kingdomkits.util;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;


public class ItemStacks
{
    /**
     * Check if an {@link ItemStack} is soulbound.
     *
     * @param item The {@link ItemStack} to check.
     * @return Returns true if the {@link ItemStack} is soulbound.
     * @throws NullPointerException if item is null.
     */
    public static boolean isSoulBound(@NotNull ItemStack item)
    {
        checkNotNull(item, "item can not be null.");
        return (item.getItemMeta() != null) && (item.getItemMeta().getLore() != null) &&
                item.getItemMeta().getLore().contains("§6§oSoulbound");
    }


    /**
     * Check if an {@link ItemStack} is unbreakable.
     *
     * @param item The {@link ItemStack} to check.
     * @return true if this {@link ItemStack} is unbreakable.
     * @throws NullPointerException if item is null.
     */
    public static boolean isUnbreakable(@NotNull ItemStack item)
    {
        checkNotNull(item, "item can not be null.");
        return (item.getItemMeta().getLore() != null) && (item.getItemMeta().getLore() != null) &&
                item.getItemMeta().getLore().contains("§6§oUnbreakable");
    }


    /**
     * Check if an {@link ItemStack} is use allowed.
     *
     * @param item The {@link ItemStack} to check.
     * @return true if the {@link ItemStack} is use allowed.
     * @throws NullPointerException if item is null.
     */
    public static boolean isUseAllowed(@NotNull ItemStack item)
    {
        checkNotNull(item, "item can not be null.");
        return (item.getItemMeta().getLore() != null) && (item.getItemMeta().getLore() != null) &&
                item.getItemMeta().getLore().contains("§6§oUse-Allowed");
    }


    /**
     * Check if an {@link ItemStack} is combat allowed.
     *
     * @param item The {@link ItemStack} to check.
     * @return true if the {@link ItemStack} is combat allowed.
     * @throws NullPointerException if item is null.
     */
    public static boolean isCombatAllowed(@NotNull ItemStack item)
    {
        checkNotNull(item, "item can not be null.");
        return (item.getItemMeta().getLore() != null) && (item.getItemMeta().getLore() != null) &&
                item.getItemMeta().getLore().contains("§6§oCombat-Allowed");
    }


    /**
     * Check if an {@link ItemStack} is equip allowed.
     *
     * @param item The {@link ItemStack} to check.
     * @return true if the {@link ItemStack} is equip allowed.
     * @throws NullPointerException if item is null.
     */
    public static boolean isEquipAllowed(@NotNull ItemStack item)
    {
        checkNotNull(item, "item can not be null.");
        return (item.getItemMeta().getLore() != null) && (item.getItemMeta().getLore() != null) &&
                item.getItemMeta().getLore().contains("§6§oEquip-Allowed");
    }


    /**
     * Check if an {@link ItemStack} is consume allowed.
     *
     * @param item The {@link ItemStack} to check.
     * @return true if the {@link ItemStack} is consume allowed.
     * @throws NullPointerException if the item is null.
     */
    public static boolean isConsumeAllowed(@NotNull ItemStack item)
    {
        checkNotNull(item, "item can not be null.");
        return (item.getItemMeta().getLore() != null) && (item.getItemMeta().getLore() != null) &&
                item.getItemMeta().getLore().contains("§6§oConsume-Allowed");
    }


    /**
     * Check if an item is part of a given kit. The item is known to be part of a certain kit if the lore contains
     * {@literal '§b§oKitNameHere'}
     *
     * @param item The {@link ItemStack} to check.
     * @param kitName The name of the kit to check if this item is part of it.
     * @return true if the item is part of this kit.
     * @throws NullPointerException if kitName is null.
     */
    public static boolean isPartOfKit(@Nullable ItemStack item, @NotNull String kitName)
    {
        checkNotNull(kitName, "kitName can not be null.");
        return (item != null) && (item.getItemMeta().getLore() != null) && (item.getItemMeta().getLore() != null) &&
                item.getItemMeta().getLore().contains("§b§o" + kitName);
                                            // NOTE: §b instead of §6
    }
}
