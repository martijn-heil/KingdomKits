package tk.martijn_heil.kingdomkits.listeners;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;
import tk.martijn_heil.kingdomkits.KingdomKits;
import tk.martijn_heil.kingdomkits.model.COnlinePlayer;
import tk.martijn_heil.kingdomkits.model.PlayerClass;
import tk.martijn_heil.kingdomkits.util.Commands;
import tk.martijn_heil.nincore.api.util.ServerUtils;


public class PlayerListener implements Listener
{
    public static FileConfiguration data = KingdomKits.getInstance().getDataManager().getData();
    public static FileConfiguration config = KingdomKits.getInstance().getConfig();
    private Plugin plugin = KingdomKits.getInstance();


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
            if(config.getBoolean("classes.enabled") && config.getBoolean("classes.giveKitOnRespawn"))
            {
                ServerUtils.dispatchCommand("essentials:kit " + config.getString("soulbound.classes." +
                        defaultClassName + ".kitName") + " " + e.getPlayer().getName());
            }


            for (String cmd : data.getStringList("commandsExecutedOnPlayerFirstJoin"))
            {
                cmd = Commands.parsePlayerVars(cmd, e.getPlayer());
                ServerUtils.dispatchCommand(cmd);
            }
        }
        else if (data.isSet(playerUUID))
        {
            // Update last seen name.
            data.set(playerUUID + ".lastSeenName", e.getPlayer().getName());

            // Check if player's class still exists
            if (!PlayerClass.PlayerClassExists(data.getString(playerUUID + ".class")))
            {
                // If player's class doesn't exist, put him back in the default class.
                COnlinePlayer cOnlinePlayer = new COnlinePlayer(e.getPlayer().getUniqueId());

                cOnlinePlayer.moveToDefaultPlayerClass();
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST) // Give the player his class kit on respawn..
    public void onPlayerRespawn(PlayerRespawnEvent e)
    {
        COnlinePlayer ninOnlinePlayer = new COnlinePlayer(e.getPlayer().getUniqueId());


        if(KingdomKits.getInstance().getDataManager().getData().getBoolean("classes.enable") &&
                KingdomKits.getInstance().getDataManager().getData().getBoolean("classes.giveKitOnRespawn"))
        {
            ServerUtils.dispatchCommand("essentials:kit " +
                    ninOnlinePlayer.getPlayerClass().getKitName() + " " + e.getPlayer().getName());
        }

        for (String cmd : ninOnlinePlayer.getPlayerClass().getCmdsExecutedOnPlayerRespawn())
        {
            cmd = Commands.parsePlayerVars(cmd, e.getPlayer());
            ServerUtils.dispatchCommand(cmd);
        }
    }
}