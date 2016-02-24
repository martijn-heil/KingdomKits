package me.Ninjoh.KingdomKits.Library.Exceptions;

import me.Ninjoh.NinCore.Library.Entity.NinOnlinePlayer;
import me.Ninjoh.NinCore.Library.Util.MessageUtil;
import me.Ninjoh.NinCore.NinCore;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ResourceBundle;

public class PlayerClassNotFoundException extends Exception
{

    public PlayerClassNotFoundException(CommandSender commandSender)
    {
        if(commandSender instanceof Player)
        {
            final ResourceBundle errorMsgs = ResourceBundle.getBundle("lang.errorMsgs",
                    NinOnlinePlayer.fromUUID(((Player) commandSender).getUniqueId()).getMinecraftLocale().toLocale());

            MessageUtil.sendError(commandSender, errorMsgs.getString("commandError.invalidPlayerClass"));
        }
        else
        {
            final ResourceBundle errorMsgs = ResourceBundle.getBundle("lang.errorMsgs",
                    NinCore.getDefaultMinecraftLocale().toLocale());

            MessageUtil.sendError(commandSender, errorMsgs.getString("commandError.invalidPlayerClass"));
        }
    }


    public PlayerClassNotFoundException()
    {

    }
}
