package me.Ninjoh.KingdomKits;

import me.Ninjoh.KingdomKits.Commands.SubCommands.KingdomKitsBindCmd;
import me.Ninjoh.KingdomKits.Commands.SubCommands.KingdomKitsGetClassCmd;
import me.Ninjoh.KingdomKits.Commands.SubCommands.KingdomKitsListCmd;
import me.Ninjoh.KingdomKits.Commands.SubCommands.KingdomKitsSetClassCmd;
import me.Ninjoh.KingdomKits.Listeners.*;
import me.ninjoh.nincore.api.NinCore;
import me.ninjoh.nincore.api.NinCorePlugin;
import me.ninjoh.nincore.api.command.NinCommand;
import me.ninjoh.nincore.api.command.builders.CommandBuilder;
import me.ninjoh.nincore.api.command.builders.SubCommandBuilder;
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
        int currentConfigVersion = 3;

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


//        String[] subCmdDesc_bind = {"bind.desc", "lang.subCmdMessages"};
//        String[] subCmdDesc_getclass = {"getclass.desc", "lang.subCmdMessages"};
//        String[] subCmdDesc_info = {"info.desc", "lang.subCmdMessages"};
//        String[] subCmdDesc_list = {"list.desc", "lang.subCmdMessages"};
//        String[] subCmdDesc_setclass = {"setclass.desc", "lang.subCmdMessages"};
//        String[] subCmdDesc_help = {"help.desc", "lang.subCmdMessages"};


        // Register kingdomkits command & sub commands.
//        List<SubCommand> subCommands = new ArrayList<>();
//        subCommands.add(new SubCommand("bind", null, null, subCmdDesc_bind, "kingdomkits.bind", new KingdomKitsBindCmd()));
//        subCommands.add(new SubCommand("getclass", null, "<player=you>", subCmdDesc_getclass, "kingdomkits.getclass", new KingdomKitsGetClassCmd()));
//        subCommands.add(new SubCommand("info", null, null, subCmdDesc_info, null, new KingdomKitsInfoCmd()));
//        subCommands.add(new SubCommand("list", null, null, subCmdDesc_list, "kingdomkits.list", new KingdomKitsListCmd()));
//        subCommands.add(new SubCommand("setclass", null, "<class> <player=you>", subCmdDesc_setclass, "kingdomkits.setclass", new KingdomKitsSetClassCmd()));
//        subCommands.add(new SubCommand("help", null, "<sub command?>", subCmdDesc_help, null, new KingdomKitsHelpCmd()));
//
//
//        Command kingdomkits = new Command("kingdomkits", subCommands, plugin);
//        kingdomkits.setExecutor(new KingdomKitsCmd(kingdomkits));
//
//        Commands.add(kingdomkits);

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
        bindBuilder.setDescriptionBundleBaseName("lang.subCmdMessages");
        bindBuilder.setDescriptionKey("bind.desc");
        bindBuilder.setParentCommand(kk);
        bindBuilder.setExecutor(new KingdomKitsBindCmd());
        NinCore.get().registerNinSubCommand(bindBuilder.construct(), this);

        this.getNinLogger().fine("Creating" + LogColor.HIGHLIGHT + "/kingdomkits getclass" + LogColor.RESET + " sub command.");
        SubCommandBuilder getclassBuilder = new SubCommandBuilder();
        getclassBuilder.setName("getclass");
        getclassBuilder.setUsage("<player=you>");
        getclassBuilder.setRequiredPermission("kingdomkits.getclass");
        getclassBuilder.setUseStaticDescription(false);
        getclassBuilder.setDescriptionBundleBaseName("lang.subCmdMessages");
        getclassBuilder.setDescriptionKey("getclass.desc");
        getclassBuilder.setParentCommand(kk);
        getclassBuilder.setExecutor(new KingdomKitsGetClassCmd());
        NinCore.get().registerNinSubCommand(getclassBuilder.construct(), this);

        this.getNinLogger().fine("Creating" + LogColor.HIGHLIGHT + "/kingdomkits list" + LogColor.RESET + " sub command.");
        SubCommandBuilder listBuilder = new SubCommandBuilder();
        listBuilder.setName("list");
        listBuilder.setRequiredPermission("kingdomkits.list");
        listBuilder.setUseStaticDescription(false);
        listBuilder.setDescriptionBundleBaseName("lang.subCmdMessages");
        listBuilder.setDescriptionKey("list.desc");
        listBuilder.setParentCommand(kk);
        listBuilder.setExecutor(new KingdomKitsListCmd());
        NinCore.get().registerNinSubCommand(listBuilder.construct(), this);

        this.getNinLogger().fine("Creating" + LogColor.HIGHLIGHT + "/kingdomkits setclass" + LogColor.RESET + " sub command.");
        SubCommandBuilder setlassBuilder = new SubCommandBuilder();
        setlassBuilder.setName("setclass");
        setlassBuilder.setUsage("<class> <player=you>");
        setlassBuilder.setRequiredPermission("kingdomkits.setclass");
        setlassBuilder.setUseStaticDescription(false);
        setlassBuilder.setDescriptionBundleBaseName("lang.subCmdMessages");
        setlassBuilder.setDescriptionKey("setclass.desc");
        setlassBuilder.setParentCommand(kk);
        setlassBuilder.setExecutor(new KingdomKitsSetClassCmd());
        NinCore.get().registerNinSubCommand(setlassBuilder.construct(), this);
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