package me.Ninjoh.KingdomKits.Library.Exceptions;

import me.Ninjoh.NinCore.Library.Util.MessageUtil;
import org.bukkit.command.CommandSender;

public class TooManyArgumentsException extends Exception
{
    private final String msg = "too many arguments supplied";

    public TooManyArgumentsException(CommandSender commandSender)
    {
        MessageUtil.sendError(commandSender, msg);
    }
}
