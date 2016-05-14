package tk.martijn_heil.kingdomkits;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.joda.time.DateTimeZone;
import tk.martijn_heil.kingdomkits.hooks.PlaceHolderApiHook;
import tk.martijn_heil.kingdomkits.listeners.FactionEventsListener;
import tk.martijn_heil.kingdomkits.modules.PlayerModule;
import tk.martijn_heil.kingdomkits.modules.*;
import tk.martijn_heil.nincore.api.Core;
import tk.martijn_heil.nincore.api.logging.LogColor;

public class KingdomKits extends Core
{
    private static final int CONFIG_VERSION = 5;

    @Getter private static KingdomKits instance;
    @Getter private KingdomKitsConfig kkConfig;

    // Factions integration
    public static boolean useFactions = false;
    private static boolean usePlaceholderApi = false;

    private static PlaceHolderApiHook placeHolderApiHook;


    @Override
    public void onEnableInner() // Fired when the server enables the plugin
    {
        instance = this;
        DateTimeZone.setDefault(DateTimeZone.UTC);


        // Check if Factions integration should be enabled.
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("Factions"))
        {
            if (!this.getConfig().getBoolean("enableFactionsIntegration"))
            {
                this.getNinLogger().config("Factions plugin was found, but integration is disabled in config.");
            }
            else
            {
                useFactions = true;
                this.getNinLogger().config(LogColor.HIGHLIGHT + "Activated" + LogColor.RESET + " integration with Factions");
            }
        }

        // Check if Factions integration should be enabled.
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("PlaceHolderAPI"))
        {
            this.getNinLogger().info("PlaceHolderAPI found, hooking in..");
            placeHolderApiHook = new PlaceHolderApiHook(this);
            usePlaceholderApi = true;
        }


        // If data file doesn't exist, create it
        if (!this.getDataManager().dataFileExists()) this.getDataManager().createDataFile();


        // Load data file
        this.getDataManager().loadDataFile();

        // Schedule automatic data file saving.
        this.getDataManager().scheduleAutomaticDataFileSave(this.getConfig().getLong("dataFileSaveInterval"));


        // Register events
        this.getNinLogger().info("Registering event listeners..");
        getServer().getPluginManager().registerEvents(new PlayerModule(this), this);

        if (useFactions) // If faction integration is activated.
        {
            this.getNinLogger().info("Faction integration is activated, registering FactionEventsListener..");
            getServer().getPluginManager().registerEvents(new FactionEventsListener(), this);
        }


        // Generate config if it doesn't already exist..
        this.saveDefaultConfig();


        // If the configuration file version in the config doesn't match with the hardcoded version above,
        // the plugin terminates with an error message.
        if (this.getConfig().getInt("configVersion") != CONFIG_VERSION)
        {
            this.getNinLogger().severe("The current configuration file is not compatible with this version of KingdomKits!");
            this.endEnable();
            return;
        }


        // If the config file contains a player class named 'default', terminate the plugin with an error message
        if (this.getConfig().getBoolean("classes.enabled") &&
                this.getConfig().getConfigurationSection("classes.classes").getKeys(false).contains("default"))
        {
            // TODO: Don't think this is required anymore?
            this.getNinLogger().severe("Invalid player class name: 'default' in configuration file!");
            this.endEnable();
            return;
        }


        this.getNinLogger().info("Registering modules..");
        this.kkConfig = new KingdomKitsConfig(this); // It's best to register this one first.

        this.getModuleManager().addModule(this.kkConfig); // It's best to register this one first.
        this.getModuleManager().addModule(new IllegalActionsModule(this));
        this.getModuleManager().addModule(new SoulboundItemsModule(this));
        this.getModuleManager().addModule(new UnbreakableItemsModule(this));
        this.getModuleManager().addModule(new CommandModule(this));
        this.getModuleManager().addModule(new SignModule(this));
    }


    @Override
    public void onDisableInner() //Fired when the server stops and disables all plugins
    {
        // Save the data file
        this.getDataManager().saveDataFile();
    }


    public static String parseString(Player p, String s)
    {
        if(placeHolderApiHook != null)
        {
            return placeHolderApiHook.parseString(p, s);
        }
        else
        {
            return s;
        }
    }
}