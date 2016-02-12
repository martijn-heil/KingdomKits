package me.Ninjoh.KingdomKits.Library.Exceptions;

import me.Ninjoh.NinCore.Library.Util.MessageUtil;
import org.bukkit.command.CommandSender;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormat;


public class CoolDownHasNotExpiredException extends Exception
{
    private static String msg;
    private CommandSender target;
    private static final PeriodType PERIOD_TO_MINUTES =
            PeriodType.standard().withSecondsRemoved().withMillisRemoved();

    public CoolDownHasNotExpiredException(CommandSender commandSender, DateTime nextPossibleClassSwitchTime)
    {
        super(msg);
        DateTime start = new DateTime();
        Period period = new Period(start, nextPossibleClassSwitchTime, PERIOD_TO_MINUTES);

        target = commandSender;
        msg = "The next time you can switch player class is in " + PeriodFormat.getDefault().print(period);
        sendMsg();
    }


    public CoolDownHasNotExpiredException(CommandSender commandSender, DateTime nextPossibleClassSwitchTime, String notSelfName)
    {
        DateTime start = new DateTime();
        Period period = new Period(start, nextPossibleClassSwitchTime, PERIOD_TO_MINUTES);

        target = commandSender;
        msg = "The next time " + notSelfName + " can switch player class is in " + PeriodFormat.getDefault().print(period);
    }


    public CoolDownHasNotExpiredException()
    {

    }


    private void sendMsg()
    {
        MessageUtil.sendError(target, msg);
    }
}
