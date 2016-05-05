package tk.martijn_heil.kingdomkits.model;

import com.massivecraft.factions.entity.MPlayer;
import org.bukkit.inventory.ItemStack;
import tk.martijn_heil.kingdomkits.KingdomKits;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.joda.time.DateTime;
import tk.martijn_heil.kingdomkits.util.ItemStacks;
import tk.martijn_heil.nincore.api.entity.NinOfflinePlayer;

import java.util.List;
import java.util.UUID;


public class COfflinePlayer
{
    private JavaPlugin plugin = KingdomKits.getInstance();
    private FileConfiguration data = KingdomKits.getInstance().getDataManager().getData();
    private FileConfiguration config = KingdomKits.getInstance().getConfig();

    UUID uuid;



    public COfflinePlayer(UUID uuid)
    {
        this.uuid = uuid;
    }


    public NinOfflinePlayer toNinOfflinePlayer()
    {
        return NinOfflinePlayer.fromOfflinePlayer(Bukkit.getOfflinePlayer(uuid));
    }


    public OfflinePlayer toOfflinePlayer()
    {
        return plugin.getServer().getOfflinePlayer(uuid);
    }


    /**
     * Get a player's class
     *
     * @return The player's class object
     */
    public PlayerClass getPlayerClass()
    {
        String playerClassname = data.getString(uuid + ".class");

        if(playerClassname.equals("default"))
        {
            return new PlayerClass(config.getString("classes.defaultClass"));
        }
        else
        {
            return new PlayerClass(playerClassname);
        }
    }


    /**
     * Set the player's class.
     *
     * @param className The class name.
     */
    public void setPlayerClass(String className)
    {
        this.setPlayerClass(new PlayerClass(className), true);
    }


    /**
     * Set the player's class.
     *
     * @param className The class name.
     * @param withCoolDown Add cooldown value to player section in data file?
     */
    public void setPlayerClass(String className, boolean withCoolDown)
    {
        this.setPlayerClass(new PlayerClass(className), withCoolDown);
    }


    @Deprecated
    public void setPlayerClass(PlayerClass playerClass)
    {
        this.setPlayerClass(playerClass, true);
    }


    public void setPlayerClass(PlayerClass playerClass, boolean withCoolDown)
    {
        if(this.toOfflinePlayer().isOnline())
        {
            for(ItemStack i : this.toOfflinePlayer().getPlayer().getInventory().getContents())
            {
                if(ItemStacks.isPartOfKit(i, this.getPlayerClass().getName()))
                {
                    this.toOfflinePlayer().getPlayer().getInventory().remove(i);
                }
            }
        }

        data.set(uuid + ".class", playerClass.getName());

        if(withCoolDown)
        {
            DateTime currentDateTime = new DateTime();
            DateTime nextPossibleClassSwitchTime = currentDateTime.plusMinutes(config.getInt("classes.coolDownInMinutes"));
            data.set(uuid + ".nextPossibleClassSwitchTime", nextPossibleClassSwitchTime.toString());
        }

        if(this.toOfflinePlayer().isOnline()) new COnlinePlayer(this.uuid).givePlayerClassKit();
    }


    /**
     * Move the player to the default player class.
     *
     */
    public void moveToDefaultPlayerClass()
    {
        this.setPlayerClass(PlayerClass.getDefaultPlayerClass(), false);
    }


    /**
     * Move the player to the default player class.
     *
     */
    public void moveToDefaultPlayerClass(boolean withCoolDown)
    {
        this.setPlayerClass(PlayerClass.getDefaultPlayerClass(), withCoolDown);
    }


    /**
     * Check if the cooldown has expired.
     *
     * @return True if the coolDown has expired
     */
    public boolean hasPlayerClassSwitchCoolDownExpired()
    {
        DateTime nextPossibleClassSwitchTime = new DateTime(data.getString(uuid + ".nextPossibleClassSwitchTime"));
        return nextPossibleClassSwitchTime.isBeforeNow();
    }


    /**
     * Get the next possible time to switch player class for this player.
     *
     * @return Next possible time to switch player class.
     */
    public DateTime getNextPossibleClassSwitchTime()
    {
        return DateTime.parse(data.getString(uuid + ".nextPossibleClassSwitchTime"));
    }


    /**
     * Check if a player can become a player class.
     *
     * @param className The class name to check for.
     * @return True if the player can become this class.
     */
    public boolean canBecomeClass(String className)
    {

        // If factions integration isn't enabled, a player can always become this class.
        if(!KingdomKits.useFactions)
        {
            return true;
        }


        PlayerClass playerClass = new PlayerClass(className);

        // If this player class does not use MaxPercentagePerFaction.
        if(!playerClass.usesMaxPercentagePerFaction())
        {
            return true;
        }


        // Get MPlayer
        MPlayer mPlayer = MPlayer.get(uuid);


        // If the player is in no faction, he can always can become the class.
        if(!mPlayer.hasFaction())
        {
            return true;
        }


        List<MPlayer> list = mPlayer.getFaction().getMPlayers();

        float total = 0;

        // Players in a class which doesn't use MaxPercentagePerFaction should not be counted to the total.
        for (MPlayer mP: list)
        {
            COnlinePlayer cPlayer = new COnlinePlayer(mP.getUuid());

            if(cPlayer.getPlayerClass().usesMaxPercentagePerFaction())
            {
                total++;
            }
        }


        float effect = (100/total);

        float count = 0;


        // Count all players in this specific player class.
        for (MPlayer mP : list)
        {
            COnlinePlayer cPlayer = new COnlinePlayer(mP.getUuid());

            if(cPlayer.getPlayerClass().equals(playerClass))
            {
                count++;
            }
        }


        // Calculate percentage
        float currentPercentage = count*100/total;


        float newPercentage = currentPercentage + effect;



        //int roundedCurrentPercentage = Math.round(currentPercentage);

        // Percentage of players with the class already reached the max percentage.
        return !(newPercentage > config.getDouble("soulbound.classes." + className + ".maxPercentagePerFaction"));
    }
}
