package me.Ninjoh.KingdomKits.Listeners;

import me.Ninjoh.KingdomKits.Library.Entity.COnlinePlayer;
import me.Ninjoh.KingdomKits.Main;
import me.Ninjoh.NinCore.Library.Entity.NinOnlinePlayer;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class EnchantListener implements Listener
{
    public static FileConfiguration config = Main.config;


    @EventHandler(priority= EventPriority.HIGHEST) // Prevent player enchanting soulbound items.
    public void onEnchantItem(EnchantItemEvent e)
    {
        List<String> items = config.getStringList("enchanting.blacklistedItems");

        ItemStack enchantedItem = e.getItem();

        Material enchantedItemMaterial = enchantedItem.getType();

        if(items.contains(enchantedItemMaterial.toString()))
        {
            // Cancel enchant event if the item is blacklisted
            e.setCancelled(true);

            final Locale locale = NinOnlinePlayer.fromUUID(e.getEnchanter().getUniqueId()).getMinecraftLocale().toLocale();
            final ResourceBundle errorMsgs = ResourceBundle.getBundle("lang.errorMsgs", locale);


            COnlinePlayer cOnlinePlayer = new COnlinePlayer(e.getEnchanter().getUniqueId());
            cOnlinePlayer.getNinOnlinePlayer().sendError(errorMsgs.getString("eventError.cancelledEnchant"));
        }
    }

    @EventHandler(priority= EventPriority.HIGHEST) // Prevent player from using soulbound items in an anvil.
    public void onInventoryClick(InventoryClickEvent e)
    {
        List<String> items = config.getStringList("enchanting.blacklistedItems");

        if (e.getCurrentItem() != null && e.getClickedInventory() != null)
        {

            // If clicked inventory is an anvil
            if (e.getClickedInventory().getType().equals(InventoryType.ANVIL))
            {
                // Foreach blacklisted item list
                for (String item : items)
                {
                    // Cast material string to material object.
                    Material itemMaterial = Material.getMaterial(item);

                    if (itemMaterial != null && e.getClickedInventory().contains(itemMaterial) &&
                            e.getSlotType().equals(InventoryType.SlotType.RESULT))
                    {
                        // If clicked inventory is an anvil && anvil contains enchanted book + a blacklisted item, cancel the event..
                        e.setCancelled(true);

                        final Locale locale = NinOnlinePlayer.fromUUID(e.getWhoClicked().getUniqueId()).getMinecraftLocale().toLocale();
                        final ResourceBundle errorMsgs = ResourceBundle.getBundle("lang.errorMsgs", locale);

                        COnlinePlayer cOnlinePlayer = new COnlinePlayer(e.getWhoClicked().getUniqueId());
                        cOnlinePlayer.getNinOnlinePlayer().sendError(errorMsgs.getString("eventError.cancelledAnvilUse"));
                    }
                }
            }
        }
    }
}