package me.ninjoh.kingdomkits.listeners;

import me.ninjoh.kingdomkits.KingdomKits;
import me.ninjoh.nincore.api.NinCore;
import me.ninjoh.nincore.api.entity.NinPlayer;
import me.ninjoh.nincore.api.util.TranslationUtils;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.ResourceBundle;

public class EnchantListener implements Listener
{
    public static FileConfiguration config = KingdomKits.getInstance().getConfig();


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


            NinPlayer np = NinCore.getImplementation().getNinPlayer(e.getEnchanter());

            np.sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs",
                    np.getMinecraftLocale().toLocale()), "eventError.cancelledEnchant"));
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

                        NinPlayer np = NinPlayer.fromPlayer((Player) e.getWhoClicked());

                        np.sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs",
                                np.getMinecraftLocale().toLocale()), "eventError.cancelledAnvilUse"));
                    }
                }
            }
        }
    }
}