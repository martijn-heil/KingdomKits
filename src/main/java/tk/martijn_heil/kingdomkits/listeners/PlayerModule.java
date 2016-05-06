package tk.martijn_heil.kingdomkits.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.jetbrains.annotations.NotNull;
import tk.martijn_heil.kingdomkits.KingdomKits;
import tk.martijn_heil.kingdomkits.model.COnlinePlayer;
import tk.martijn_heil.kingdomkits.util.Commands;
import tk.martijn_heil.kingdomkits.util.Players;
import tk.martijn_heil.nincore.api.Core;
import tk.martijn_heil.nincore.api.CoreModule;
import tk.martijn_heil.nincore.api.util.ServerUtils;


public class PlayerModule extends CoreModule implements Listener
{
    public PlayerModule(@NotNull Core core)
    {
        super(core);
    }


    @EventHandler // Give the player his class kit if the joins for the first time.
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        COnlinePlayer cp = new COnlinePlayer(e.getPlayer().getUniqueId());

        Players.populateData(e.getPlayer());

        // Player is new.
        if(!this.getCore().getDataManager().getData().isSet(e.getPlayer().getUniqueId().toString()))
        {
            for (String cmd : cp.getPlayerClass().getCmdsExecutedOnPlayerRespawn())
            {
                cmd = Commands.parsePlayerVars(cmd, e.getPlayer());
                ServerUtils.dispatchCommand(cmd);
            }

            // Give player default class kit
            if(KingdomKits.getInstance().getConfig().getBoolean("classes.enabled") &&
                    KingdomKits.getInstance().getConfig().getBoolean("classes.giveKitOnRespawn"))
            {
                cp.givePlayerClassKit();
            }
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