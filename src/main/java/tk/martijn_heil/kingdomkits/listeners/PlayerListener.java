package tk.martijn_heil.kingdomkits.listeners;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import tk.martijn_heil.kingdomkits.KingdomKits;
import tk.martijn_heil.kingdomkits.model.COnlinePlayer;
import tk.martijn_heil.kingdomkits.model.PlayerClass;
import tk.martijn_heil.kingdomkits.util.Commands;
import tk.martijn_heil.nincore.api.util.ServerUtils;


public class PlayerListener implements Listener
{
    public static FileConfiguration data = KingdomKits.getInstance().getDataManager().getData();
    public static FileConfiguration config = KingdomKits.getInstance().getConfig();


    @EventHandler // Give the player his class kit if the joins for the first time.
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        String playerUUID = e.getPlayer().getUniqueId().toString();
        COnlinePlayer cp = new COnlinePlayer(e.getPlayer().getUniqueId());
        String defaultClassName = config.getString("classes.defaultClass");


        if(!data.isSet(playerUUID)) // he's new.
        {
            KingdomKits.getInstance().getNinLogger().info("Creating new data entry for player: " + e.getPlayer().getName() + " (" + playerUUID + ")");

            data.set(playerUUID + ".class", defaultClassName);
            data.set(playerUUID + ".lastSeenName", e.getPlayer().getName());
            data.set(playerUUID + ".nextPossibleClassSwitchTime", null);

            // Give player default class kit
            if(config.getBoolean("classes.enabled") && config.getBoolean("classes.giveKitOnRespawn"))
            {
                cp.givePlayerClassKit();
            }

            for (String cmd : data.getStringList("commandsExecutedOnPlayerFirstJoin"))
            {
                cmd = Commands.parsePlayerVars(cmd, e.getPlayer());
                ServerUtils.dispatchCommand(cmd);
            }
        }


        if(!data.isSet(playerUUID + ".class"))
        {
            data.set(playerUUID + ".class", defaultClassName);
        }


        if(!data.isSet(playerUUID + ".lastSeenName"))
        {
            data.set(playerUUID + ".lastSeenName", e.getPlayer().getName());
        }


        if(!data.isSet(playerUUID + ".nextPossibleClassSwitchTime"))
        {
            data.set(playerUUID + ".nextPossibleClassSwitchTime", null);
        }

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


    @EventHandler(priority = EventPriority.HIGHEST) // Give the player his class kit on respawn..
    public void onPlayerRespawn(PlayerRespawnEvent e)
    {
        COnlinePlayer cOnlinePlayer = new COnlinePlayer(e.getPlayer().getUniqueId());


        if(KingdomKits.getInstance().getDataManager().getData().getBoolean("classes.enable") &&
                KingdomKits.getInstance().getDataManager().getData().getBoolean("classes.giveKitOnRespawn"))
        {
            cOnlinePlayer.givePlayerClassKit();
        }

        for (String cmd : cOnlinePlayer.getPlayerClass().getCmdsExecutedOnPlayerRespawn())
        {
            cmd = Commands.parsePlayerVars(cmd, e.getPlayer());
            ServerUtils.dispatchCommand(cmd);
        }
    }
}