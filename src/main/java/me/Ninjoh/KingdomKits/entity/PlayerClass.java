package me.ninjoh.kingdomkits.entity;

import me.ninjoh.kingdomkits.KingdomKits;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class PlayerClass
{
    private static FileConfiguration config = KingdomKits.getInstance().getConfig();

    private String name;
    private ConfigurationSection classSection;

    /**
     * Constructor
     *
     * @param className The player class name.
     */
    public PlayerClass(String className)
    {
        name = className;
        this.classSection = KingdomKits.getInstance().getConfig().getConfigurationSection("soulbound.classes." + name);
    }

    /**
     * Get the player class' name.
     *
     * @return the player class' name.
     */
    public String getName()
    {
        return name;
    }


    /**
     * Get the kit name of the essentials kit for this class.
     *
     * @return The name of the essentials kit for this class.
     */
    public String getKitName()
    {
        return classSection.getString("kitname");
    }


    /**
     * Check if this player class uses maxPercentagePerFaction.
     *
     * @return true if this player class uses maxPercentagePerFaction.
     */
    public boolean usesMaxPercentagePerFaction()
    {
        return classSection.getBoolean("useMaxPercentagePerFaction");
    }


    /**
     * Check if this player class is this default player class.
     *
     * @return true if this player class is the default player class.
     */
    public boolean isDefaultPlayerClass()
    {
        return name.equals(config.getString("soulbound.defaultClass"));
    }


    public List<String> getCmdsExecutedOnPlayerRespawn()
    {
        return classSection.getStringList("commandsExecutedOnPlayerRespawn");
    }


    /**
     *  Check if a player class exists.
     *
     * @param className The player class name to check for.
     * @return true if this player class exists.
     */
    public static boolean PlayerClassExists(String className)
    {
        return config.getConfigurationSection("soulbound.classes").getKeys(false).contains(className);
    }


    /**
     * Get the default player class.
     *
     * @return The default player class.
     */
    public static PlayerClass getDefaultPlayerClass()
    {
        return new PlayerClass(config.getString("soulbound.defaultClass"));
    }
}
