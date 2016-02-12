package me.Ninjoh.KingdomKits.Library.Exceptions;

import me.Ninjoh.NinCore.Library.Util.MessageUtil;
import org.bukkit.command.CommandSender;

public class ItemAlreadySoulboundException extends Exception
{
    private final String msg = "this item is already soulbound";


    public ItemAlreadySoulboundException(CommandSender commandSender)
    {
        MessageUtil.sendError(commandSender, msg);
    }


    public ItemAlreadySoulboundException()
    {

    }
}
