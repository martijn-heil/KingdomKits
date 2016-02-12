package me.Ninjoh.KingdomKits.Commands.SubCommands;


import me.Ninjoh.KingdomKits.Library.Entity.CConsoleCommandSender;
import me.Ninjoh.KingdomKits.Library.Entity.COnlinePlayer;
import me.Ninjoh.KingdomKits.Main;
import me.Ninjoh.NinCore.Library.Entity.Command;
import me.Ninjoh.NinCore.Library.Entity.SubCommand;
import me.Ninjoh.NinCore.Library.Interfaces.SubCommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class KingdomKitsInfoCmd implements SubCommandExecutor
{
    JavaPlugin plugin = Main.plugin;

    @Override
    public void Handle(CommandSender sender, String[] args, Command command, SubCommand subCommand)
    {
        try
        {
            if(sender instanceof Player)
            {
                COnlinePlayer cOnlinePlayer = new COnlinePlayer(((Player) sender).getUniqueId());

                cOnlinePlayer.getNinOnlinePlayer().sendPluginInfo(plugin);
            }
            else if (sender instanceof ConsoleCommandSender)
            {
                CConsoleCommandSender cConsoleCommandSender = new CConsoleCommandSender();

                cConsoleCommandSender.sendPluginInfo(plugin);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
