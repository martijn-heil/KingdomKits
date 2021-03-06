package tk.martijn_heil.kingdomkits.exceptions;

import org.bukkit.command.CommandSender;
import tk.martijn_heil.nincore.api.entity.NinCommandSender;
import tk.martijn_heil.nincore.api.exceptions.ValidationException;
import tk.martijn_heil.nincore.api.util.TranslationUtils;

import java.util.ResourceBundle;

public class PlayerClassNotFoundException extends ValidationException
{
    public PlayerClassNotFoundException(CommandSender target)
    {
        super(target, TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs",
                NinCommandSender.fromCommandSender(target).getMinecraftLocale().
                        toLocale()), "commandError.invalidPlayerClass"), null);
    }
}
