package me.ninjoh.kingdomkits.exceptions;

import me.ninjoh.nincore.api.NinCore;
import me.ninjoh.nincore.api.exceptions.ValidationException;
import me.ninjoh.nincore.api.util.TranslationUtils;
import org.bukkit.command.CommandSender;

import java.util.ResourceBundle;


public class PlayerElytraFlightDeniedException extends ValidationException
{
    public PlayerElytraFlightDeniedException(CommandSender target)
    {
        super(target, TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs",
                NinCore.getImplementation().getNinCommandSender(target).getMinecraftLocale().
                        toLocale()), "eventError.cancelledElytra"), null);
    }
}
