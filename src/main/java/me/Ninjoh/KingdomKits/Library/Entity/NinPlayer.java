package me.Ninjoh.KingdomKits.Library.Entity;


import me.Ninjoh.KingdomKits.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class NinPlayer
{
    JavaPlugin plugin = Main.plugin;
    FileConfiguration data = Main.data;
    FileConfiguration config = Main.config;


    public UUID uuid;


    /**
     * Get the player's UUID.
     *
     * @return the player's UUID.
     */
    public UUID getUuid()
    {
        return uuid;
    }
}
