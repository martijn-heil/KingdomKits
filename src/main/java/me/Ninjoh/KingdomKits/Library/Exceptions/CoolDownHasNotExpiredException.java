package me.Ninjoh.KingdomKits.Library.Exceptions;

import me.ninjoh.nincore.api.NinCore;
import me.ninjoh.nincore.api.exceptions.ValidationException;
import me.ninjoh.nincore.api.util.TranslationUtils;
import org.bukkit.command.CommandSender;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormat;

import java.util.Locale;
import java.util.ResourceBundle;


public class CoolDownHasNotExpiredException extends ValidationException
{
    private static final PeriodType PERIOD_TO_MINUTES = PeriodType.standard().withSecondsRemoved().withMillisRemoved();


    public CoolDownHasNotExpiredException(CommandSender target, DateTime nextPossibleClassSwitchTime)
    {
        super(target, getMsg(target, nextPossibleClassSwitchTime), null);
    }


    public CoolDownHasNotExpiredException(CommandSender target, DateTime nextPossibleClassSwitchTime, String notSelfName)
    {
        super(target, getMsg(target, nextPossibleClassSwitchTime, notSelfName), null);
    }


    private static String getMsg(CommandSender commandSender, DateTime nextPossibleClassSwitchTime)
    {
        DateTime start = new DateTime();
        Period period = new Period(start, nextPossibleClassSwitchTime, PERIOD_TO_MINUTES);

        Locale locale = NinCore.get().getNinCommandSender(commandSender).getLocale();

        return TranslationUtils.transWithArgs(ResourceBundle.getBundle("lang.errorMsgs", locale),
                new Object[]{PeriodFormat.wordBased(locale).print(period)}, "commandError.coolDownHasNotExpired");
    }


    private static String getMsg(CommandSender commandSender, DateTime nextPossibleClassSwitchTime, String notSelfName)
    {
        DateTime start = new DateTime();
        Period period = new Period(start, nextPossibleClassSwitchTime, PERIOD_TO_MINUTES);

        Locale locale = NinCore.get().getNinCommandSender(commandSender).getLocale();

        return TranslationUtils.transWithArgs(ResourceBundle.getBundle("lang.errorMsgs", locale),
                new Object[]{notSelfName, PeriodFormat.getDefault().print(period)}, "commandError.coolDownHasNotExpired.notSelf");
    }
}
