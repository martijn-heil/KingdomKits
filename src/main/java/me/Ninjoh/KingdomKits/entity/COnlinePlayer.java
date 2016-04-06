package me.ninjoh.kingdomkits.entity;


import me.ninjoh.nincore.api.entity.NinPlayer;
import org.bukkit.Bukkit;
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


    public NinPlayer toNinOnlinePlayer()
    {
        return NinPlayer.fromPlayer(this.getPlayer());
    }


    public Player getPlayer()
    {
        return Bukkit.getServer().getPlayer(uuid);
    }
}