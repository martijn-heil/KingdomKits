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


    public NinPlayer toNinPlayer()
    {
        return NinPlayer.fromPlayer(this.toPlayer());
    }


    public Player toPlayer()
    {
        return Bukkit.getServer().getPlayer(uuid);
    }
}