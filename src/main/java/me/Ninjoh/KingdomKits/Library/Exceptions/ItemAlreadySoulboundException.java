package me.Ninjoh.KingdomKits.Library.Exceptions;

import me.ninjoh.nincore.api.NinCore;
import me.ninjoh.nincore.api.exceptions.ValidationException;
import me.ninjoh.nincore.api.util.TranslationUtils;
import org.bukkit.command.CommandSender;

import java.util.ResourceBundle;

public class ItemAlreadySoulboundException extends ValidationException
{
    public ItemAlreadySoulboundException(CommandSender target)
    {
        super(target, TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs",
                NinCore.getImplementation().getNinCommandSender(target).getMinecraftLocale().
                        toLocale()), "commandError.itemAlreadySoulbound"), null);
    }
}
