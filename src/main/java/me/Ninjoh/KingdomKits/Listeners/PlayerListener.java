package me.ninjoh.kingdomkits.listeners;

import me.ninjoh.kingdomkits.KingdomKits;
import me.ninjoh.kingdomkits.entity.COnlinePlayer;
import me.ninjoh.kingdomkits.entity.PlayerClass;
import me.ninjoh.nincore.api.NinCore;
import me.ninjoh.nincore.api.entity.NinPlayer;
import me.ninjoh.nincore.api.util.TranslationUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.util.ResourceBundle;


public class PlayerListener implements Listener
{
    public static FileConfiguration data = KingdomKits.getInstance().getDataManager().getData();
    public static FileConfiguration config = KingdomKits.getInstance().getConfig();
    Plugin plugin = KingdomKits.getInstance();


    @EventHandler // Give the player his class kit if the joins for the first time.
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        String playerUUID = e.getPlayer().getUniqueId().toString();

        String defaultClassName = config.getString("soulbound.defaultClass");


        // Add player to data file if he isn't yet added.
        if (!data.isSet(playerUUID))
        {
            data.set(playerUUID + ".class", "default");
            data.set(playerUUID + ".lastSeenName", e.getPlayer().getName());
            data.set(playerUUID + ".nextPossibleClassSwitchTime", null);

            plugin.getLogger().info("Creating new data entry for player: " + e.getPlayer().getName() + " (" + playerUUID + ")");

            // Give player default class kit
            NinCore.get().getNinServer().dispatchCommand("essentials:kit " + config.getString("soulbound.classes." +
                    defaultClassName + ".kitName") + " " + e.getPlayer().getName());
        }
        else if (data.isSet(playerUUID))
        {
            // Update last seen name.
            data.set(playerUUID + ".lastSeenName", e.getPlayer().getName());

            // Check if player's class still exists
            if (!PlayerClass.PlayerClassExists(data.getString(playerUUID + ".class")))
            {
                // If player's class doesn't exist, put him back in the default class.
                COnlinePlayer ninOnlinePlayer = new COnlinePlayer(e.getPlayer().getUniqueId());

                ninOnlinePlayer.moveToDefaultPlayerClass();
            }
        }
    }

    @EventHandler
    public void onEntityToggleGlide(EntityToggleGlideEvent e)
    {
        if(e.isGliding() && e.getEntity() instanceof Player && config.getBoolean("preventElytra") &&
                !e.getEntity().hasPermission("kingdomkits.bypass.elytra"))
        {
            e.setCancelled(true);

            NinPlayer p = NinCore.get().getNinPlayer((Player) e.getEntity());
            p.sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs"), "eventError.cancelledElytra"));
        }
    }
}