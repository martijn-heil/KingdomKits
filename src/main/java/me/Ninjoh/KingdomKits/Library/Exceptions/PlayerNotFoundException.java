package me.Ninjoh.KingdomKits.Library.Exceptions;

import me.Ninjoh.NinCore.Library.Util.MessageUtil;
import org.bukkit.command.CommandSender;

public class PlayerNotFoundException extends Exception
{
    private final String msg = "player not found";


    public PlayerNotFoundException(CommandSender commandSender)
    {
        MessageUtil.sendError(commandSender, msg);
    }


    public PlayerNotFoundException()
    {

    }
}
