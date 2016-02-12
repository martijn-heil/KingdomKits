package me.Ninjoh.KingdomKits.Library.Exceptions;

import me.Ninjoh.NinCore.Library.Util.MessageUtil;
import org.bukkit.command.CommandSender;

public class PlayerCannotBecomeClassException extends Exception
{
    private final String msg = "this class is already full in this faction";


    public PlayerCannotBecomeClassException(CommandSender commandSender)
    {
        MessageUtil.sendError(commandSender, msg);
    }


    public PlayerCannotBecomeClassException()
    {

    }
}
