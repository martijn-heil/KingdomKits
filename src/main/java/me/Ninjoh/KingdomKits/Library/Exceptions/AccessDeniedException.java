package me.Ninjoh.KingdomKits.Library.Exceptions;

import me.Ninjoh.NinCore.Library.Util.MessageUtil;
import org.bukkit.command.CommandSender;

public class AccessDeniedException extends Exception
{
    private final String msg = "you do not have access to use this command";


    public AccessDeniedException(CommandSender commandSender)
    {
        MessageUtil.sendError(commandSender, msg);
    }


    public AccessDeniedException()
    {

    }
}
