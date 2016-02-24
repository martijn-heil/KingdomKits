package me.Ninjoh.KingdomKits.Commands.SubCommands;


import me.Ninjoh.KingdomKits.Library.Entity.*;
import me.Ninjoh.KingdomKits.Library.Exceptions.CoolDownHasNotExpiredException;
import me.Ninjoh.KingdomKits.Library.Exceptions.PlayerCannotBecomeClassException;
import me.Ninjoh.NinCore.Library.Exceptions.*;
import me.Ninjoh.KingdomKits.Library.Exceptions.PlayerClassNotFoundException;
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

public class KingdomKitsSetClassCmd implements SubCommandExecutor
{
    FileConfiguration data = Main.data;


    @Override
    public void Handle(CommandSender sender, String[] args, Command command, SubCommand subCommand)
    {
        try
        {
            // If sender doesn't have the kingdomkits.setclass permission
            if(!sender.hasPermission("kingdomkits.setclass"))
            {
                if(sender instanceof Player)
                {
                    throw new AccessDeniedException(sender);
                }
                else if(sender instanceof ConsoleCommandSender)
                {
                    ConsoleCommandSender consoleCommandSender = (ConsoleCommandSender) sender;

                    throw new AccessDeniedException((CommandSender) consoleCommandSender);
                }
                return;
            }


            // Not enough arguments
            if(args.length <= 0)
            {
                if(sender instanceof Player)
                {
                    COnlinePlayer cOnlinePlayer = new COnlinePlayer(((Player) sender).getUniqueId());

                    cOnlinePlayer.getNinOnlinePlayer().sendSubCommandHelp(command, subCommand);
                }
                else
                {
                    CConsoleCommandSender cConsoleCommandSender = new CConsoleCommandSender();

                    cConsoleCommandSender.sendSubCommandHelp(command, subCommand);
                }
            }
            else if(args.length == 1) // Class has been given, target player has not been given.
            {
                String className = args[0];

                // If sender doesn't have the kingdomkits.setclass.[class] permission
                if(!sender.hasPermission("kingdomkits.setclass." + className))
                {
                    throw new AccessDeniedException(sender);
                }


                // Class validation
                if(!PlayerClass.PlayerClassExists(className))
                {
                    throw new PlayerClassNotFoundException(sender);
                }


                // Integration with factions validation.
                if(sender instanceof Player)
                {
                    COnlinePlayer ninOnlinePlayer = new COnlinePlayer(((Player) sender).getUniqueId());

                    if(!ninOnlinePlayer.canBecomeClass(className))
                    {
                        throw new PlayerCannotBecomeClassException((CommandSender) ninOnlinePlayer.getPlayer());
                    }
                }


                // Player class switch cool down validation.
                if(sender instanceof Player)
                {
                    COnlinePlayer ninOnlinePlayer = new COnlinePlayer(((Player) sender).getUniqueId());

                    if(!ninOnlinePlayer.hasPlayerClassSwitchCoolDownExpired() &&
                            !sender.hasPermission("kingdomkits.bypass.changeclasscooldown"))
                    {
                        throw new CoolDownHasNotExpiredException(sender, ninOnlinePlayer.getNextPossibleClassSwitchTime());
                    }
                }



                // If class validation has passed..

                if(sender instanceof Player)
                {
                    COnlinePlayer ninOnlinePlayer = new COnlinePlayer(((Player) sender).getUniqueId());

                    ninOnlinePlayer.setPlayerClass(className, true);

                    sender.sendMessage(ChatColor.DARK_GRAY + "Your" + ChatColor.YELLOW +
                            " class has been set to " + ChatColor.DARK_GRAY + className);

                } // Target player has to be specified when this command is executed from console.
                else if(sender instanceof ConsoleCommandSender)
                {
                    CConsoleCommandSender cConsoleCommandSender = new CConsoleCommandSender();

                    cConsoleCommandSender.sendSubCommandHelp(command, subCommand);
                }
            }
            else if(args.length == 2) // Class has been given, target player has been given.
            {
                String className = args[0];

                // If sender doesn't have the kingdomkits.setclass.[class] permission
                if(!sender.hasPermission("kingdomkits.setclass.class." + className) && !sender.hasPermission("kingdomkits.setclass.class.*"))
                {
                    if(sender instanceof Player)
                    {
                        throw new AccessDeniedException(sender);
                    }
                    else if(sender instanceof ConsoleCommandSender)
                    {
                        ConsoleCommandSender consoleCommandSender = (ConsoleCommandSender) sender;

                        throw new AccessDeniedException((CommandSender) consoleCommandSender);
                    }
                    return;
                }


                if(!sender.hasPermission("kingdomkits.setclass.others"))
                {
                    throw new AccessDeniedException(sender);
                }

                // Class validation
                if(!PlayerClass.PlayerClassExists(className))
                {
                    if(sender instanceof Player)
                    {
                        throw new PlayerClassNotFoundException(sender);
                    }
                    else if (sender instanceof ConsoleCommandSender)
                    {
                        ConsoleCommandSender consoleCommandSender = (ConsoleCommandSender) sender;

                        throw new PlayerClassNotFoundException((CommandSender) consoleCommandSender);
                    }

                    return;
                }


                // Class validation has passed..

                String targetPlayerName = args[1];

                UUID targetPlayerUUID = UUIDAPI.getUUID(targetPlayerName);

                // Player validation..
                if(targetPlayerUUID == null || !data.getKeys(false).contains(targetPlayerUUID.toString()))
                {
                    throw new PlayerNotFoundException(sender);
                }

                // Player validation & class validation has passed..

                COfflinePlayer ninOfflinePlayer = new COfflinePlayer(targetPlayerUUID);


                // Player can become class validation..
                if(!ninOfflinePlayer.canBecomeClass(className))
                {
                    throw new PlayerCannotBecomeClassException(sender);
                }


                // Cooldown validation..
                if(!ninOfflinePlayer.hasPlayerClassSwitchCoolDownExpired() &&
                        !sender.hasPermission("kingdomkits.bypass.changeclasscooldown"))
                {
                    throw new CoolDownHasNotExpiredException(sender, ninOfflinePlayer.getNextPossibleClassSwitchTime(),
                            ninOfflinePlayer.getOfflinePlayer().getName());
                }


                // All validation has passed, set player's class..
                ninOfflinePlayer.setPlayerClass(className, true);


                if(sender instanceof Player)
                {
                    sender.sendMessage(ChatColor.DARK_GRAY + targetPlayerName +
                            "'s" + ChatColor.YELLOW + " class has been set to " + ChatColor.DARK_GRAY + className);
                }
                else if(sender instanceof ConsoleCommandSender)
                {
                    ConsoleCommandSender consoleCommandSender = (ConsoleCommandSender) sender;

                    consoleCommandSender.sendMessage(ChatColor.DARK_GRAY + targetPlayerName +
                            "'s" + ChatColor.YELLOW + " class has been set to " + ChatColor.DARK_GRAY + className);
                }

                // If the target player is online, give him a little notification.
                if(ninOfflinePlayer.getOfflinePlayer().isOnline())
                {
                    ninOfflinePlayer.getOfflinePlayer().getPlayer().sendMessage(ChatColor.DARK_GRAY +
                            sender.getName() + ChatColor.YELLOW + " has set your class to " + ChatColor.DARK_GRAY + className);
                }
            }
        }
        catch(PlayerCannotBecomeClassException | PlayerNotFoundException | PlayerClassNotFoundException |
                AccessDeniedException | CoolDownHasNotExpiredException e)
        {
            //
        }
    }
}
