package me.Ninjoh.KingdomKits.Library.Entity;


import me.ninjoh.nincore.api.NinCore;
import me.ninjoh.nincore.api.entity.NinPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class COnlinePlayer extends COfflinePlayer
{

    /**
     * Constructor
     *
     * @param uuid The player's UUID
     */
    public COnlinePlayer(UUID uuid)
    {
        super(uuid);
    }


    public NinPlayer getNinOnlinePlayer()
    {
        return NinCore.getImplementation().getNinPlayer(getPlayer());
    }


    public Player getPlayer()
    {
        return plugin.getServer().getPlayer(uuid);
    }
}