package me.Ninjoh.KingdomKits.Commands.SubCommands;


import me.Ninjoh.KingdomKits.Main;
import me.Ninjoh.NinCore.Library.Entity.Command;
import me.Ninjoh.NinCore.Library.Entity.SubCommand;
import me.Ninjoh.NinCore.Library.Interfaces.SubCommandExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class KingdomKitsListCmd implements SubCommandExecutor
{
    //JavaPlugin plugin = Main.plugin;
    FileConfiguration config = Main.config;
    //FileConfiguration data = Main.data;

    @Override
    public void Handle(CommandSender sender, String[] args, Command command, SubCommand subCommand)
    {
        try
        {
            ConfigurationSection playerClasses = config.getConfigurationSection("soulbound.classes");

            sender.sendMessage("");
            sender.sendMessage("§8-=[ §b§l+ §8]=- §8[ §6List Of Player Classes §8] -= [ §b§l+ §8]=-");
            sender.sendMessage("");

            int count = 1;

            // List all player classes
            for (String key : playerClasses.getKeys(false))
            {
                sender.sendMessage(ChatColor.GRAY + "" + count + ". " + ChatColor.YELLOW + key);
                count++;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
