package me.Ninjoh.KingdomKits.Listeners;

import me.Ninjoh.KingdomKits.KingdomKits;
import me.ninjoh.nincore.api.NinCore;
import me.ninjoh.nincore.api.entity.NinPlayer;
import me.ninjoh.nincore.api.util.TranslationUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.List;
import java.util.ResourceBundle;

public class CraftingListener implements Listener
{
    public static FileConfiguration config = KingdomKits.getInstance().getConfig();


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
                    NinPlayer np = NinCore.getImplementation().getNinPlayer((Player) e.getWhoClicked());

                    np.sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs",
                            np.getMinecraftLocale().toLocale()), "eventError.cancelledCraft"));
                }
            }
        }
    }
}