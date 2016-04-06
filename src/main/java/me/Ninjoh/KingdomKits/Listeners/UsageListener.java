package me.ninjoh.kingdomkits.listeners;

import me.ninjoh.kingdomkits.KingdomKits;
import me.ninjoh.nincore.api.entity.NinPlayer;
import me.ninjoh.nincore.api.util.TranslationUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ResourceBundle;


public class UsageListener implements Listener
{
    public static FileConfiguration config = KingdomKits.getInstance().getConfig();


    @EventHandler(priority= EventPriority.HIGHEST)
    public void onPlayerUseItem(PlayerInteractEvent e)
    {
        if(e.getItem() != null)
        {
            if(config.getList("usage.blacklistedItems").contains(e.getItem().getType().toString()) && (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)))
            {
                e.setCancelled(true);
                e.getPlayer().updateInventory();

                NinPlayer np = NinPlayer.fromPlayer(e.getPlayer());

                np.sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs",
                        np.getMinecraftLocale().toLocale()), "eventError.cancelledItemUse"));
            }
        }
    }
}