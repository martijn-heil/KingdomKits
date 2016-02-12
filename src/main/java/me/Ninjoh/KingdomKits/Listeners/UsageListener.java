package me.Ninjoh.KingdomKits.Listeners;

import me.Ninjoh.KingdomKits.Library.Entity.COnlinePlayer;
import me.Ninjoh.NinCore.Library.Entity.NinOnlinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.Ninjoh.KingdomKits.Main;

import java.util.Locale;
import java.util.ResourceBundle;


public class UsageListener implements Listener
{
    public static FileConfiguration config = Main.config;


    @EventHandler(priority= EventPriority.HIGHEST)
    public void onPlayerUseItem(PlayerInteractEvent e)
    {
        if(e.getItem() != null)
        {
            if(config.getList("usage.blacklistedItems").contains(e.getItem().getType().toString()) && (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)))
            {
                e.setCancelled(true);
                e.getPlayer().updateInventory();

                final Locale locale = NinOnlinePlayer.fromUUID(e.getPlayer().getUniqueId()).getMinecraftLocale().toLocale();
                final ResourceBundle errorMsgs = ResourceBundle.getBundle("lang.errorMsgs", locale);

                COnlinePlayer cOnlinePlayer = new COnlinePlayer(e.getPlayer().getUniqueId());
                cOnlinePlayer.getNinOnlinePlayer().sendError(errorMsgs.getString("eventError.cancelledItemUse"));
            }
        }
    }
}