package me.Ninjoh.KingdomKits.Commands.SubCommands;


import me.Ninjoh.KingdomKits.Library.Entity.*;
import me.Ninjoh.NinCore.Library.Exceptions.AccessDeniedException;
import me.Ninjoh.NinCore.Library.Exceptions.PlayerNotFoundException;
import me.Ninjoh.NinCore.Library.Exceptions.TooManyArgumentsException;
import me.Ninjoh.KingdomKits.Main;
import me.Ninjoh.NinCore.Library.Entity.Command;
import me.Ninjoh.NinCore.Library.Entity.SubCommand;
import me.Ninjoh.NinCore.Library.Interfaces.SubCommandExecutor;
import net.mcapi.uuid.UUIDAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.UUID;

public class KingdomKitsGetClassCmd implements SubCommandExecutor
{
    //JavaPlugin plugin = Main.plugin;
    FileConfiguration config = Main.config;
    FileConfiguration data = Main.data;

    @Override
    public void Handle(CommandSender sender, String[] args, Command command, SubCommand subCommand)
    {
        try
        {
            if(!sender.hasPermission("kingdomkits.getclass"))
            {
                throw new AccessDeniedException(sender);
            }

            // If no target player has been given.
            if(args.length == 0)
            {
                if(sender instanceof Player)
                {
                    COnlinePlayer ninOnlinePlayer = new COnlinePlayer(((Player) sender).getUniqueId());

                    sender.sendMessage(ChatColor.DARK_GRAY + "You " + ChatColor.YELLOW + "have the " + ChatColor.DARK_GRAY +
                            ninOnlinePlayer.getPlayerClass().getName() + ChatColor.YELLOW + " class");
                }
                else if(sender instanceof ConsoleCommandSender)
                {
                    CConsoleCommandSender cConsoleCommandSender = new CConsoleCommandSender();

                    cConsoleCommandSender.sendSubCommandHelp(command, subCommand);
                }


            } // Target player has been given
            else if(args.length == 1)
            {
                if(!sender.hasPermission("kingdomkits.getclass.others"))
                {
                    throw new AccessDeniedException(sender);
                }


                String targetPlayer = args[0];

                UUID targetPlayerUUID = UUIDAPI.getUUID(targetPlayer);


                if (targetPlayerUUID == null || !data.getKeys(false).contains(targetPlayerUUID.toString()))
                {
                    throw new PlayerNotFoundException(sender);
                }


                if (!data.getKeys(false).contains(targetPlayerUUID.toString()))
                {
                    throw new PlayerNotFoundException(sender);
                }

                COfflinePlayer cOfflinePlayer = new COfflinePlayer(targetPlayerUUID);


                if(cOfflinePlayer.getPlayerClass().getName().equals("default"))
                {
                    // Get default class name and send the player the message.
                    sender.sendMessage(ChatColor.DARK_GRAY + cOfflinePlayer.getOfflinePlayer().getName() + ChatColor.YELLOW + " has the " +
                            ChatColor.DARK_GRAY + config.getString("soulbound.defaultClass") +
                            ChatColor.YELLOW + " class");
                }
                else
                {
                    // Send the player the message..
                    sender.sendMessage(ChatColor.DARK_GRAY + targetPlayer + ChatColor.YELLOW + " has the " +
                            ChatColor.DARK_GRAY + cOfflinePlayer.getPlayerClass().getName() + ChatColor.YELLOW + " class");
                }
            }
            else
            {
                throw new TooManyArgumentsException(sender);
            }
        }
        catch(TooManyArgumentsException | PlayerNotFoundException | AccessDeniedException e)
        {
            //
        }
    }
}
