package me.Ninjoh.KingdomKits.Listeners;

import me.Ninjoh.KingdomKits.Library.Entity.COnlinePlayer;
import me.Ninjoh.KingdomKits.Library.Entity.PlayerClass;
import me.Ninjoh.KingdomKits.Main;
import me.Ninjoh.NinCore.Library.Util.ServerUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;


public class PlayerListener implements Listener
{
    public static FileConfiguration data = Main.data;
    public static FileConfiguration config = Main.config;
    Plugin plugin = me.Ninjoh.KingdomKits.Main.plugin;


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
            ServerUtils.dispatchCommand("essentials:kit " + config.getString("soulbound.classes." +
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
}