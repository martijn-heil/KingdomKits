package me.Ninjoh.KingdomKits.Library.Entity;

import me.Ninjoh.KingdomKits.Main;
import org.bukkit.configuration.file.FileConfiguration;

public class PlayerClass
{
    private static FileConfiguration config = Main.config;

    private String name;

    /**
     * Constructor
     *
     * @param className The player class name.
     */
    PlayerClass(String className)
    {
        name = className;
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
        return config.getString("soulbound.classes." + name + ".kitName");
    }


    /**
     * Check if this player class uses maxPercentagePerFaction.
     *
     * @return true if this player class uses maxPercentagePerFaction.
     */
    public boolean usesMaxPercentagePerFaction()
    {
        return config.getBoolean("soulbound.classes." + name + ".useMaxPercentagePerFaction");
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
