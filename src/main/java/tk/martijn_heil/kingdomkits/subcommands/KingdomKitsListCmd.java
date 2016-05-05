package tk.martijn_heil.kingdomkits.subcommands;


import tk.martijn_heil.kingdomkits.KingdomKits;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import tk.martijn_heil.nincore.api.command.executors.NinSubCommandExecutor;
import tk.martijn_heil.nincore.api.entity.NinCommandSender;
import tk.martijn_heil.nincore.api.exceptions.TechnicalException;
import tk.martijn_heil.nincore.api.exceptions.ValidationException;

import java.util.Locale;
import java.util.ResourceBundle;

public class KingdomKitsListCmd extends NinSubCommandExecutor
{
    @Override
    public void execute(CommandSender sender, String[] strings) throws ValidationException, TechnicalException
    {
        Locale locale = NinCommandSender.fromCommandSender(sender).getLocale();


        ConfigurationSection playerClasses = KingdomKits.getInstance().getConfig().getConfigurationSection("classes.classes");

        final ResourceBundle mainMsgs = ResourceBundle.getBundle("lang.mainMsgs", locale);

        sender.sendMessage("");
        sender.sendMessage("§8-=[ §b§l+ §8]=- §8[ §6" + mainMsgs.getString("listOfPlayerClasses") + " §8] -= [ §b§l+ §8]=-");
        sender.sendMessage("");

        int count = 1;

        // List all player classes
        for (String key : playerClasses.getKeys(false))
        {
            sender.sendMessage(ChatColor.GRAY + "" + count + ". " + ChatColor.YELLOW + key);
            count++;
        }
    }
}
