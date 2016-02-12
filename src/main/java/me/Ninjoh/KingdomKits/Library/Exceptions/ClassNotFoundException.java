package me.Ninjoh.KingdomKits.Library.Exceptions;

import me.Ninjoh.NinCore.Library.Util.MessageUtil;
import org.bukkit.command.CommandSender;

public class ClassNotFoundException extends Exception
{
    private final String msg = "class not found";


    public ClassNotFoundException(CommandSender commandSender)
    {
        MessageUtil.sendError(commandSender, msg);
    }


    public ClassNotFoundException()
    {

    }
}
