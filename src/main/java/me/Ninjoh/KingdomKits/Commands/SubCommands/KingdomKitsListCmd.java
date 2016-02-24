package me.Ninjoh.KingdomKits.Commands.SubCommands;


import me.Ninjoh.KingdomKits.Main;
import me.Ninjoh.NinCore.Library.Entity.Command;
import me.Ninjoh.NinCore.Library.Entity.NinOnlinePlayer;
import me.Ninjoh.NinCore.Library.Entity.SubCommand;
import me.Ninjoh.NinCore.Library.Interfaces.SubCommandExecutor;
import me.Ninjoh.NinCore.Library.Util.LocaleUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.ResourceBundle;

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
            Locale locale;
            if(sender instanceof Player)
            {
                locale = NinOnlinePlayer.fromUUID(((Player) sender).getUniqueId()).getMinecraftLocale().toLocale();
            }
            else
            {
                locale = LocaleUtils.getDefaultMinecraftLocale().toLocale();
            }


            ConfigurationSection playerClasses = config.getConfigurationSection("soulbound.classes");

            final ResourceBundle mainMsgs = ResourceBundle.getBundle("lang.mainMsgs", locale);

            sender.sendMessage("");
            sender.sendMessage("§8-=[ §b§l+ §8]=- §8[ §6" + mainMsgs.getString("listOfPlayerClasses") + " §8] -= [ §b§l+ §8]=-");
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
