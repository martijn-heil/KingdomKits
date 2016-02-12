package me.Ninjoh.KingdomKits.Library.Entity;


import me.Ninjoh.NinCore.Library.Entity.NinOnlinePlayer;
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


    public NinOnlinePlayer getNinOnlinePlayer()
    {
        return new NinOnlinePlayer(uuid);
    }


    public Player getPlayer()
    {
        return plugin.getServer().getPlayer(uuid);
    }
}