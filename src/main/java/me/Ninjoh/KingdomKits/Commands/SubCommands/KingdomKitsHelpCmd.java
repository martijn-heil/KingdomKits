package me.Ninjoh.KingdomKits.Commands.SubCommands;


import me.Ninjoh.NinCore.Library.Util.MessageUtil;
import me.Ninjoh.NinCore.Library.Entity.Command;
import me.Ninjoh.NinCore.Library.Entity.SubCommand;
import me.Ninjoh.NinCore.Library.Interfaces.SubCommandExecutor;
import org.bukkit.command.CommandSender;

public class KingdomKitsHelpCmd implements SubCommandExecutor
{

    @Override
    public void Handle(CommandSender sender, String[] args, Command command, SubCommand subCommand)
    {
        if(args.length == 1) // Sub command supplied.
        {
            SubCommand subCmd = command.getSubCommand(args[0]);

            if(subCmd == null)
            {
                MessageUtil.sendCommandHelp(sender, command);
            }
            else
            {
                MessageUtil.sendCommandHelp(sender, command, subCmd);
            }
        }
        else
        {
            MessageUtil.sendCommandHelp(sender, command);
        }
    }
}
