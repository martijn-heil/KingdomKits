package tk.martijn_heil.kingdomkits.model;


import org.bukkit.entity.Player;
import tk.martijn_heil.nincore.api.entity.NinOnlinePlayer;
import tk.martijn_heil.nincore.api.util.ServerUtils;

import java.util.UUID;

public class COnlinePlayer extends COfflinePlayer
{
    private Player player;

    /**
     * Constructor
     *
     * @param uuid The player's UUID
     */
    public COnlinePlayer(UUID uuid)
    {
        super(uuid);
    }


    public COnlinePlayer(Player p)
    {
        super(p);
        this.player = p;
    }


    public NinOnlinePlayer toNinOnlinePlayer()
    {
        return NinOnlinePlayer.fromPlayer(this.toPlayer());
    }


    public Player toPlayer()
    {
        return this.player;
    }


    public void givePlayerClassKit()
    {
        ServerUtils.dispatchCommand("essentials:kit " + this.getPlayerClass().getKitName() + " " + this.player.getName());
    }
}