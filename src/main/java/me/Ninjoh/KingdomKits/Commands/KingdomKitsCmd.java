package me.Ninjoh.KingdomKits.Commands;

import me.Ninjoh.NinCore.Library.Exceptions.AccessDeniedException;
import me.Ninjoh.NinCore.Library.Entity.SubCommand;
import me.Ninjoh.NinCore.Library.Util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;


public class KingdomKitsCmd implements CommandExecutor
{
    private me.Ninjoh.NinCore.Library.Entity.Command Command;


    public KingdomKitsCmd(me.Ninjoh.NinCore.Library.Entity.Command cmd)
    {
        Command = cmd;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(cmd.getName().equalsIgnoreCase(Command.getName()))
        {
            // Check if the sender has the required permission for this command.
            if(Command.requiresPermission() && !sender.hasPermission(Command.getRequiredPermission()))
            {
                try
                {
                    throw new AccessDeniedException(sender);
                }
                catch(AccessDeniedException e)
                {
                    //
                }
                return true;
            }

            if(Command.hasSubCommands())
            {
                if(args.length < 1)
                {
                    // Sub command not specified, send command help.
                    sendCommandHelp(sender);
                    return true;
                }


                SubCommand subCmd = Command.getSubCommandByAlias(args[0].toLowerCase());

                if(subCmd != null)
                {
                    if(subCmd.requiresPermission() && !sender.hasPermission(subCmd.getRequiredPermission()))
                    {
                        try
                        {
                            throw new AccessDeniedException(sender);
                        }
                        catch(AccessDeniedException e)
                        {
                            //
                        }
                        return true;
                    }

                    // The first argument is the sub command, so remove that one
                    List<String> list = new ArrayList<>();

                    int count = 0;

                    for (String arg : args)
                    {
                        if(count > 0)
                        {
                            list.add(arg);
                        }

                        count++;
                    }

                    // Generate new args without the first argument, wich would be the sub command.
                    String[] newArgs = list.toArray(new String[list.size()]);



                    // Handle sub command.
                    subCmd.getExecutor().Handle(sender, newArgs, Command, subCmd);
                }
                else
                {
                    sendCommandHelp(sender);
                }
            }
            else
            {
                // Execute normal command behaviour, only if this command has no sub commands.
            }
        }

        return true;
    }


    /**
     * Send command to help to CommandSender.
     *
     * @param sender The CommandSender
     */
    private void sendCommandHelp(CommandSender sender)
    {
        MessageUtil.sendCommandHelp(sender, Command);
    }
}
