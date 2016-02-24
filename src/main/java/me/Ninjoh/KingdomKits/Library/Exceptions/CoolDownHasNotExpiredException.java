package me.Ninjoh.KingdomKits.Library.Exceptions;

import me.Ninjoh.NinCore.Library.Entity.NinOnlinePlayer;
import me.Ninjoh.NinCore.Library.Util.MessageUtil;
import me.Ninjoh.NinCore.NinCore;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormat;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;


public class CoolDownHasNotExpiredException extends Exception
{
    private static final PeriodType PERIOD_TO_MINUTES =
            PeriodType.standard().withSecondsRemoved().withMillisRemoved();

    public CoolDownHasNotExpiredException(CommandSender commandSender, DateTime nextPossibleClassSwitchTime)
    {
        DateTime start = new DateTime();
        Period period = new Period(start, nextPossibleClassSwitchTime, PERIOD_TO_MINUTES);

        final MessageFormat formatter = new MessageFormat("");



        if(commandSender instanceof Player)
        {
            final Locale locale = NinOnlinePlayer.fromUUID(((Player) commandSender).getUniqueId()).getMinecraftLocale().toLocale();
            final ResourceBundle errorMsgs = ResourceBundle.getBundle("lang.errorMsgs", locale);
            formatter.setLocale(locale);

            final Object[] messageArguments = {PeriodFormat.wordBased(locale).print(period)};
            formatter.applyPattern(errorMsgs.getString("commandError.coolDownHasNotExpired"));
            final String msg = formatter.format(messageArguments);


            MessageUtil.sendError(commandSender, msg);
        }
        else
        {
            final Locale locale = NinCore.getDefaultMinecraftLocale().toLocale();
            final ResourceBundle errorMsgs = ResourceBundle.getBundle("lang.errorMsgs", locale);
            formatter.setLocale(locale);

            final Object[] messageArguments = {PeriodFormat.wordBased(locale).print(period)};
            formatter.applyPattern(errorMsgs.getString("commandError.coolDownHasNotExpired"));
            final String msg = formatter.format(messageArguments);


            MessageUtil.sendError(commandSender, msg);
        }
    }


    public CoolDownHasNotExpiredException(CommandSender commandSender, DateTime nextPossibleClassSwitchTime, String notSelfName)
    {
        DateTime start = new DateTime();
        Period period = new Period(start, nextPossibleClassSwitchTime, PERIOD_TO_MINUTES);
        final MessageFormat formatter = new MessageFormat("");

        final Locale locale = NinCore.getDefaultMinecraftLocale().toLocale();
        final ResourceBundle errorMsgs = ResourceBundle.getBundle("lang.errorMsgs", locale);
        formatter.setLocale(locale);

        final Object[] messageArguments = {notSelfName, PeriodFormat.getDefault().print(period)};
        formatter.applyPattern(errorMsgs.getString("commandError.coolDownHasNotExpired.notSelf"));
        final String msg = formatter.format(messageArguments);


        MessageUtil.sendError(commandSender, msg);
    }


    public CoolDownHasNotExpiredException()
    {

    }
}
