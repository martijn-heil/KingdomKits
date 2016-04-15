package me.ninjoh.kingdomkits.subcommands;


import me.ninjoh.kingdomkits.KingdomKits;
import me.ninjoh.nincore.api.NinCore;
import me.ninjoh.nincore.api.command.executors.NinSubCommandExecutor;
import me.ninjoh.nincore.api.exceptions.TechnicalException;
import me.ninjoh.nincore.api.exceptions.ValidationException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Locale;
import java.util.ResourceBundle;

public class KingdomKitsListCmd extends NinSubCommandExecutor
{
    @Override
    public void execute(CommandSender sender, String[] strings) throws ValidationException, TechnicalException
    {
        Locale locale = NinCore.get().getNinCommandSender(sender).getLocale();


        ConfigurationSection playerClasses = KingdomKits.getInstance().getConfig().getConfigurationSection("soulbound.classes");

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
