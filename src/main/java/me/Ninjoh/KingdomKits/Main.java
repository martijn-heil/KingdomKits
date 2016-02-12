package me.Ninjoh.KingdomKits;

import me.Ninjoh.KingdomKits.Commands.SubCommands.*;
import me.Ninjoh.NinCore.Library.Entity.Tick;
import me.Ninjoh.NinCore.Library.Util.DataManager;
import me.Ninjoh.KingdomKits.Listeners.*;
import me.Ninjoh.KingdomKits.Commands.KingdomKitsCmd;
import me.Ninjoh.NinCore.Library.Entity.Command;
import me.Ninjoh.NinCore.Library.Entity.SubCommand;
import net.mcapi.uuid.ServerRegion;
import net.mcapi.uuid.UUIDAPI;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.joda.time.DateTimeZone;

import java.util.*;

public class Main extends JavaPlugin
{
    public static Main plugin;
    public static FileConfiguration config;
    public static FileConfiguration data;
    //public static FileConfiguration lang;


    private DataManager dataManager;

    // Factions integration
    public static boolean useFactions = false;


    public static List<Command> Commands = new ArrayList<>();



    @Override
    public void onEnable() // Fired when the server enables the plugin
    {
        // Used to use JavaPlugin methods in listener classes.
        plugin = this;
        config = this.getConfig();


        DateTimeZone.setDefault(DateTimeZone.UTC);


        // Check if Factions integration should be enabled.
        if(plugin.getServer().getPluginManager().isPluginEnabled("Factions"))
        {
            if(!config.getBoolean("enableFactionsIntegration"))
            {
                plugin.getLogger().info("Factions plugin was found, but integration is disabled in config.");
            }
            else
            {
                useFactions = true;
                plugin.getLogger().info("Activated integration with Factions");
            }
        }

        dataManager = new DataManager(plugin);

        // If data file doesn't exist, create it
        if(!dataManager.dataFileExists())
        {
            dataManager.createDataFile();
        }

        // Load data file
        data = dataManager.loadDataFile();

        // Schedule automatic data file saving.
        dataManager.scheduleAutomaticDataFileSave(Tick.valueOf(config.getLong("dataFileSaveInterval")));

        // If language file doesn't exist, create it
//        if(!dataManager.langFileExists())
//        {
//            dataManager.createLangFile();
//        }
//
//        // Load language file
//        lang = dataManager.loadLangFile();


        // Register events
        getServer().getPluginManager().registerEvents(new ConsumeListener(), this);
        getServer().getPluginManager().registerEvents(new CraftingListener(), this);
        getServer().getPluginManager().registerEvents(new EnchantListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new PotionListener(), this);
        getServer().getPluginManager().registerEvents(new SoulBoundItemListener(), this);
        getServer().getPluginManager().registerEvents(new UsageListener(), this);

        if(useFactions) // If faction integration is enabled.
        {
            getServer().getPluginManager().registerEvents(new FactionEventsListener(), this);
        }


        // Generate config..
        //this.getConfig().options().copyDefaults(true);
        this.saveDefaultConfig();


        // Current configuration file version
        int currentConfigVersion = 3;

        // If the configuration file version in the config doesn't match with the hardcoded version above,
        // the plugin terminates with an error message.
        if(this.getConfig().getInt("configVersion") != currentConfigVersion)
        {
            plugin.getLogger().severe("The current configuration file is not compatible with this version of KingdomKits!");
            plugin.setEnabled(false);
            return;
        }


        // If the config file contains a player class named 'default', terminate the plugin with an error message
        if(config.getConfigurationSection("soulbound.classes").getKeys(false).contains("default"))
        {
            // TODO: Don't think this is required anymore?
            plugin.getLogger().severe("Invalid player class name: 'default' in configuration file!");
            plugin.setEnabled(false);
            return;
        }

        // UUID API Server region, either EU or US.
        String apiRegion = config.getString("serverRegion").toUpperCase();

        // Check if the UUID API Server region has a valid value.
        if(!apiRegion.equals("EU") && !apiRegion.equals("US"))
        {
            plugin.getLogger().severe("Invalid server region value in configuration file (" + apiRegion + ") change it to either US or EU!");
            plugin.setEnabled(false);
            return;
        }

        // Set UUID API region
        UUIDAPI.setRegion(ServerRegion.valueOf(config.getString("serverRegion")));
        plugin.getLogger().info("UUID API Server region set to: " + config.getString("serverRegion"));


        String[] subCmdDesc_bind = {"bind.desc", "lang.subCmdMessages"};
        String[] subCmdDesc_getclass = {"getclass.desc", "lang.subCmdMessages"};
        String[] subCmdDesc_info = {"info.desc", "lang.subCmdMessages"};
        String[] subCmdDesc_list = {"list.desc", "lang.subCmdMessages"};
        String[] subCmdDesc_setclass = {"setclass.desc", "lang.subCmdMessages"};
        String[] subCmdDesc_help = {"help.desc", "lang.subCmdMessages"};


        // Register kingdomkits command & sub commands.
        List<SubCommand> subCommands = new ArrayList<>();
        subCommands.add(new SubCommand("bind", null, null, subCmdDesc_bind, "kingdomkits.bind", new KingdomKitsBindCmd()));
        subCommands.add(new SubCommand("getclass", null, "<player=you>",  subCmdDesc_getclass, "kingdomkits.getclass", new KingdomKitsGetClassCmd()));
        subCommands.add(new SubCommand("info", null, null, subCmdDesc_info, null, new KingdomKitsInfoCmd()));
        subCommands.add(new SubCommand("list", null, null, subCmdDesc_list, "kingdomkits.list", new KingdomKitsListCmd()));
        subCommands.add(new SubCommand("setclass", null, "<class> <player=you>",  subCmdDesc_setclass, "kingdomkits.setclass", new KingdomKitsSetClassCmd()));
        subCommands.add(new SubCommand("help", null, "<sub command?>",  subCmdDesc_help, null, new KingdomKitsHelpCmd()));



        Command kingdomkits = new Command("kingdomkits", subCommands, plugin);
        kingdomkits.setExecutor(new KingdomKitsCmd(kingdomkits));

        Commands.add(kingdomkits);
    }

    @Override
    public void onDisable() //Fired when the server stops and disables all plugins
    {
        // Save the data & lang file
        dataManager.saveDataFile();

        //dataManager.saveLangFile(lang);
    }
}