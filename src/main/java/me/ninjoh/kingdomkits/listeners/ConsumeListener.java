package me.ninjoh.kingdomkits.listeners;

import me.ninjoh.kingdomkits.KingdomKits;
import me.ninjoh.kingdomkits.entity.COnlinePlayer;
import me.ninjoh.nincore.api.entity.NinPlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.Locale;
import java.util.ResourceBundle;


public class ConsumeListener implements Listener
{
    public static FileConfiguration config = KingdomKits.getInstance().getConfig();


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerItemConsume(PlayerItemConsumeEvent e)
    {
        if(config.getList("consume.blacklistedItems").contains(e.getItem().getType().toString()))
        {
            e.setCancelled(true);

            NinPlayer np = NinPlayer.fromPlayer(e.getPlayer());
            final Locale locale = np.getMinecraftLocale().toLocale();
            final ResourceBundle errorMsgs = ResourceBundle.getBundle("lang.errorMsgs", locale);


            final COnlinePlayer cOnlinePlayer = new COnlinePlayer(e.getPlayer().getUniqueId());
            np.sendError(errorMsgs.getString("eventError.cancelledItemConsume"));
        }
    }
}