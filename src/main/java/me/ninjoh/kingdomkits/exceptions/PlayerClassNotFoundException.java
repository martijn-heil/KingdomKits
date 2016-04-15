package me.ninjoh.kingdomkits.exceptions;

import me.ninjoh.nincore.api.NinCore;
import me.ninjoh.nincore.api.exceptions.ValidationException;
import me.ninjoh.nincore.api.util.TranslationUtils;
import org.bukkit.command.CommandSender;

import java.util.ResourceBundle;

public class PlayerClassNotFoundException extends ValidationException
{
    public PlayerClassNotFoundException(CommandSender target)
    {
        super(target, TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs",
                NinCore.getImplementation().getNinCommandSender(target).getMinecraftLocale().
                        toLocale()), "commandError.invalidPlayerClass"), null);
    }
}
