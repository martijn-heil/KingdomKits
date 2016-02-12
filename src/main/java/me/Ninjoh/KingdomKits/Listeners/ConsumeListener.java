package me.Ninjoh.KingdomKits.Listeners;

import me.Ninjoh.KingdomKits.Library.Entity.COnlinePlayer;
import me.Ninjoh.NinCore.Library.Entity.NinOnlinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import me.Ninjoh.KingdomKits.Main;

import java.util.Locale;
import java.util.ResourceBundle;


public class ConsumeListener implements Listener
{
    public static FileConfiguration config = Main.config;


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerItemConsume(PlayerItemConsumeEvent e)
    {
        if(config.getList("consume.blacklistedItems").contains(e.getItem().getType().toString()))
        {
            e.setCancelled(true);

            final Locale locale = NinOnlinePlayer.fromUUID(e.getPlayer().getUniqueId()).getMinecraftLocale().toLocale();
            final ResourceBundle errorMsgs = ResourceBundle.getBundle("lang.errorMsgs", locale);


            final COnlinePlayer cOnlinePlayer = new COnlinePlayer(e.getPlayer().getUniqueId());
            cOnlinePlayer.getNinOnlinePlayer().sendError(errorMsgs.getString("eventError.cancelledItemConsume"));
        }
    }
}