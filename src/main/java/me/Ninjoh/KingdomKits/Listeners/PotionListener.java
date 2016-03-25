package me.Ninjoh.KingdomKits.Listeners;

import me.Ninjoh.KingdomKits.KingdomKits;
import me.ninjoh.nincore.api.NinCore;
import me.ninjoh.nincore.api.entity.NinPlayer;
import me.ninjoh.nincore.api.util.TranslationUtils;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.Potion;

import java.util.ResourceBundle;


public class PotionListener implements Listener
{
    public static FileConfiguration config = KingdomKits.getInstance().getConfig();


    @EventHandler(priority= EventPriority.HIGHEST)
    public void onPlayerItemConsume(PlayerItemConsumeEvent e)
    {
        if(config.getBoolean("potions.disablePotions"))
        {
            if(e.getItem().getType().equals(Material.POTION))
            {
                Potion potion = Potion.fromItemStack(e.getItem());

                if(potion.getLevel() != 0)
                {
                    e.setCancelled(true);

                    NinPlayer np = NinCore.getImplementation().getNinPlayer(e.getPlayer());

                    np.sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs",
                            np.getMinecraftLocale().toLocale()), "eventError.cancelledPotionDrink"));
                }
            }
        }
    }


    @EventHandler(priority= EventPriority.HIGHEST)
    public void onBlockDispenseEvent(BlockDispenseEvent e)
    {
        if(config.getBoolean("potions.disablePotions"))
        {
            if(e.getItem().getType().equals(Material.POTION))
            {
                e.setCancelled(true);
            }
        }
    }


    @EventHandler(priority= EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent e)
    {
        if(config.getBoolean("potions.disablePotions"))
        {
            if(e.getItem() != null && e.getItem().getType().equals(Material.POTION) &&
                    (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)))
            {
                Potion potion = Potion.fromItemStack(e.getItem());

                if(potion.getLevel() != 0 && potion.isSplash())
                {
                    e.setCancelled(true);

                    NinPlayer np = NinCore.getImplementation().getNinPlayer(e.getPlayer());

                    np.sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs",
                            np.getMinecraftLocale().toLocale()), "eventError.cancelledPotionThrow"));
                }

            }
        }
    }
}