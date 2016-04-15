package me.ninjoh.kingdomkits;

import me.ninjoh.kingdomkits.listeners.*;
import me.ninjoh.kingdomkits.subcommands.KingdomKitsBindCmd;
import me.ninjoh.kingdomkits.subcommands.KingdomKitsGetClassCmd;
import me.ninjoh.kingdomkits.subcommands.KingdomKitsListCmd;
import me.ninjoh.kingdomkits.subcommands.KingdomKitsSetClassCmd;
import me.ninjoh.nincore.api.NinCorePlugin;
import me.ninjoh.nincore.api.command.NinCommand;
import me.ninjoh.nincore.api.command.builders.CommandBuilder;
import me.ninjoh.nincore.api.command.builders.SubCommandBuilder;
import me.ninjoh.nincore.api.localization.LocalizedString;
import me.ninjoh.nincore.api.logging.LogColor;
import net.mcapi.uuid.ServerRegion;
import net.mcapi.uuid.UUIDAPI;
import org.bukkit.Bukkit;
import org.joda.time.DateTimeZone;

public class KingdomKits extends NinCorePlugin
{
    private static String DEFAULT_SERVER_REGION = "EU";

    private static KingdomKits instance;

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
        getServer().getPluginManager().registerEvents(new ConsumeListener(), this);
        getServer().getPluginManager().registerEvents(new CraftingListener(), this);
        getServer().getPluginManager().registerEvents(new EnchantListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new PotionListener(), this);
        getServer().getPluginManager().registerEvents(new SoulBoundItemListener(), this);
        getServer().getPluginManager().registerEvents(new UsageListener(), this);

        if (useFactions) // If faction integration is enabled.
        {
            this.getNinLogger().info("Faction integration is enabled, registering FactionEventsListener..");
            getServer().getPluginManager().registerEvents(new FactionEventsListener(), this);
        }


        // Generate config..
        this.saveDefaultConfig();


        // Current configuration file version
        int currentConfigVersion = 4;

        // If the configuration file version in the config doesn't match with the hardcoded version above,
        // the plugin terminates with an error message.
        if (this.getConfig().getInt("configVersion") != currentConfigVersion)
        {
            this.getNinLogger().severe("The current configuration file is not compatible with this version of KingdomKits!");
            this.endEnable();
            return;
        }


        // If the config file contains a player class named 'default', terminate the plugin with an error message
        if (this.getConfig().getConfigurationSection("soulbound.classes").getKeys(false).contains("default"))
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
            this.getNinLogger().warning("Using default server region ('" + DEFAULT_SERVER_REGION + "') due to previous errors");
        }
        else
        {
            // Set UUID API region
            UUIDAPI.setRegion(ServerRegion.valueOf(this.getConfig().getString("serverRegion")));
            this.getNinLogger().config("UUID API Server region set to: " + this.getConfig().getString("serverRegion"));
        }

        this.getNinLogger().info("Creating kingdomkits command..");
        CommandBuilder kkBuider = new CommandBuilder(this);
        kkBuider.setName("kingdomkits");
        kkBuider.setUseStaticDescription(true);
        NinCommand kk = kkBuider.construct();
        kk.addDefaultInfoSubCmd();
        kk.addDefaultHelpSubCmd();


        this.getNinLogger().info("Creating sub commands..");

        this.getNinLogger().fine("Creating" + LogColor.HIGHLIGHT + "/kingdomkits bind" + LogColor.RESET + " sub command.");
        SubCommandBuilder bindBuilder = new SubCommandBuilder();
        bindBuilder.setName("bind");
        bindBuilder.setRequiredPermission("kingdomkits.bind");
        bindBuilder.setUseStaticDescription(false);
        bindBuilder.setLocalizedDescription(new LocalizedString(this.getClassLoader(), "lang.subCmdMessages", "bind.desc"));
        bindBuilder.setParentCommand(kk);
        bindBuilder.setExecutor(new KingdomKitsBindCmd());
        bindBuilder.construct();

        this.getNinLogger().fine("Creating" + LogColor.HIGHLIGHT + "/kingdomkits getclass" + LogColor.RESET + " sub command.");
        SubCommandBuilder getclassBuilder = new SubCommandBuilder();
        getclassBuilder.setName("getclass");
        getclassBuilder.setUsage("<player=you>");
        getclassBuilder.setRequiredPermission("kingdomkits.getclass");
        getclassBuilder.setUseStaticDescription(false);
        getclassBuilder.setLocalizedDescription(new LocalizedString(this.getClassLoader(), "lang.subCmdMessages", "getclass.desc"));
        getclassBuilder.setParentCommand(kk);
        getclassBuilder.setExecutor(new KingdomKitsGetClassCmd());
        getclassBuilder.construct();

        this.getNinLogger().fine("Creating" + LogColor.HIGHLIGHT + "/kingdomkits list" + LogColor.RESET + " sub command.");
        SubCommandBuilder listBuilder = new SubCommandBuilder();
        listBuilder.setName("list");
        listBuilder.setRequiredPermission("kingdomkits.list");
        listBuilder.setUseStaticDescription(false);
        listBuilder.setLocalizedDescription(new LocalizedString(this.getClassLoader(), "lang.subCmdMessages", "list.desc"));
        listBuilder.setParentCommand(kk);
        listBuilder.setExecutor(new KingdomKitsListCmd());
        listBuilder.construct();

        this.getNinLogger().fine("Creating" + LogColor.HIGHLIGHT + "/kingdomkits setclass" + LogColor.RESET + " sub command.");
        SubCommandBuilder setClassBuilder = new SubCommandBuilder();
        setClassBuilder.setName("setclass");
        setClassBuilder.setUsage("<class> <player=you>");
        setClassBuilder.setRequiredPermission("kingdomkits.setclass");
        setClassBuilder.setUseStaticDescription(false);
        setClassBuilder.setLocalizedDescription(new LocalizedString(this.getClassLoader(), "lang.subCmdMessages", "setclass.desc"));
        setClassBuilder.setParentCommand(kk);
        setClassBuilder.setExecutor(new KingdomKitsSetClassCmd());
        setClassBuilder.construct();
    }


    @Override
    public void onDisableInner() //Fired when the server stops and disables all plugins
    {
        // Save the data file
        this.getDataManager().saveDataFile();
    }


    public static KingdomKits getInstance()
    {
        return instance;
    }
}