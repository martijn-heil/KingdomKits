package me.Ninjoh.KingdomKits.Listeners;

import me.Ninjoh.KingdomKits.Library.Entity.COnlinePlayer;
import me.Ninjoh.KingdomKits.Main;
import me.Ninjoh.NinCore.Library.Entity.NinOnlinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class CraftingListener implements Listener
{
    public static FileConfiguration config = Main.config;


    @EventHandler(priority= EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent e)
    {
        List<String> items = config.getStringList("crafting.blacklistedItems");

        if (e.getCurrentItem() != null && e.getClickedInventory() != null)
        {

            // If clicked inventory is a WORKBENCH or CRAFTING inventory
            if (e.getClickedInventory().getType().equals(InventoryType.WORKBENCH) ||
                    e.getClickedInventory().getType().equals(InventoryType.CRAFTING))
            {
                if(e.getSlotType().equals(InventoryType.SlotType.RESULT) &&
                        items.contains(e.getCurrentItem().getType().toString()))
                {
                    e.setCancelled(true);

                    final Locale locale = NinOnlinePlayer.fromUUID(e.getWhoClicked().getUniqueId()).getMinecraftLocale().toLocale();
                    final ResourceBundle errorMsgs = ResourceBundle.getBundle("lang.errorMsgs", locale);

                    // Send user error message..
                    COnlinePlayer cOnlinePlayer = new COnlinePlayer(e.getWhoClicked().getUniqueId());
                    cOnlinePlayer.getNinOnlinePlayer().sendError(errorMsgs.getString("eventError.cancelledCraft"));
                }
            }
        }
    }
}