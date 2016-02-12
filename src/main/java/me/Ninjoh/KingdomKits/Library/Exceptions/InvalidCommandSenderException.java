package me.Ninjoh.KingdomKits.Library.Exceptions;

import me.Ninjoh.NinCore.Library.Util.MessageUtil;
import org.bukkit.command.CommandSender;

public class InvalidCommandSenderException extends Exception
{
    private final String msg = "this command can only be sent by a player";


    public InvalidCommandSenderException(CommandSender commandSender)
    {
        MessageUtil.sendError(commandSender, msg);
    }

    public InvalidCommandSenderException()
    {

    }
}
