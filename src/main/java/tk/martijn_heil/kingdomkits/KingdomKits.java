package tk.martijn_heil.kingdomkits;

import lombok.Getter;
import net.mcapi.uuid.ServerRegion;
import net.mcapi.uuid.UUIDAPI;
import org.bukkit.Bukkit;
import org.joda.time.DateTimeZone;
import tk.martijn_heil.kingdomkits.listeners.*;
import tk.martijn_heil.kingdomkits.modules.*;
import tk.martijn_heil.nincore.api.Core;
import tk.martijn_heil.nincore.api.logging.LogColor;

public class KingdomKits extends Core
{
    private static final String DEFAULT_SERVER_REGION = "EU";
    private static final int CONFIG_VERSION = 5;

    @Getter private static KingdomKits instance;
    @Getter private KingdomKitsConfig kkConfig;

    // Factions integration
    public static boolean useFactions = false;


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


        // If data file doesn't exist, create it
        if (!this.getDataManager().dataFileExists())
        {
            this.getDataManager().createDataFile();
        }

        // Load data file
        this.getDataManager().loadDataFile();

        // Schedule automatic data file saving.
        this.getDataManager().scheduleAutomaticDataFileSave(this.getConfig().getLong("dataFileSaveInterval"));


        // Register events
        this.getNinLogger().info("Registering event listeners..");
        //getServer().getPluginManager().registerEvents(new ConsumeListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        if (useFactions) // If faction integration is enabled.
        {
            this.getNinLogger().info("Faction integration is enabled, registering FactionEventsListener..");
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

        // This handler does not spam the console with request status messages. Furthermore it's exactly the same.
        UUIDAPI.setHandler(new MainUUIDAPIHandler());

        // UUID API Server region, either EU or US.
        String apiRegion = this.getConfig().getString("serverRegion").toUpperCase();

        // Check if the UUID API Server region has a valid value.
        if (!apiRegion.equals("EU") && !apiRegion.equals("US"))
        {
            this.getNinLogger().warning("Invalid server region value in configuration file (" + apiRegion + ") change it to either US or EU!");
            this.getNinLogger().warning("Using default server region ('" + DEFAULT_SERVER_REGION + "') due to previous errors.");
        }
        else
        {
            // Set UUID API region
            UUIDAPI.setRegion(ServerRegion.valueOf(this.getConfig().getString("serverRegion")));
            this.getNinLogger().config("UUID API Server region set to: " + this.getConfig().getString("serverRegion"));
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
}