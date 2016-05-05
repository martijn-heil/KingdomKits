package tk.martijn_heil.kingdomkits.subcommands;


import tk.martijn_heil.kingdomkits.KingdomKits;
import tk.martijn_heil.kingdomkits.model.COfflinePlayer;
import tk.martijn_heil.kingdomkits.model.COnlinePlayer;
import net.mcapi.uuid.UUIDAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import tk.martijn_heil.nincore.api.command.executors.NinSubCommandExecutor;
import tk.martijn_heil.nincore.api.exceptions.TechnicalException;
import tk.martijn_heil.nincore.api.exceptions.ValidationException;
import tk.martijn_heil.nincore.api.exceptions.validationexceptions.AccessDeniedException;
import tk.martijn_heil.nincore.api.exceptions.validationexceptions.NotEnoughArgumentsException;
import tk.martijn_heil.nincore.api.exceptions.validationexceptions.PlayerNotFoundException;
import tk.martijn_heil.nincore.api.exceptions.validationexceptions.TooManyArgumentsException;

import java.util.UUID;

public class KingdomKitsGetClassCmd extends NinSubCommandExecutor
{
    @Override
    public void execute(CommandSender sender, String[] args) throws ValidationException, TechnicalException
    {
        if (!sender.hasPermission("kingdomkits.getclass"))
        {
            throw new AccessDeniedException(sender);
        }

        // If no target player has been given.
        if (args.length == 0)
        {
            if (sender instanceof Player)
            {
                COnlinePlayer ninOnlinePlayer = new COnlinePlayer(((Player) sender).getUniqueId());

                sender.sendMessage(ChatColor.DARK_GRAY + "You " + ChatColor.YELLOW + "have the " + ChatColor.DARK_GRAY +
                        ninOnlinePlayer.getPlayerClass().getName() + ChatColor.YELLOW + " class");
            }
            else if (sender instanceof ConsoleCommandSender)
            {
                throw new NotEnoughArgumentsException(sender);
            }


        } // Target player has been given
        else if (args.length == 1)
        {
            if (!sender.hasPermission("kingdomkits.getclass.others"))
            {
                throw new AccessDeniedException(sender);
            }


            String targetPlayer = args[0];

            UUID targetPlayerUUID = UUIDAPI.getUUID(targetPlayer);


            if (targetPlayerUUID == null || !KingdomKits.getInstance().getDataManager().getData().getKeys(false).contains(targetPlayerUUID.toString()))
            {
                throw new PlayerNotFoundException(sender);
            }


            if (!KingdomKits.getInstance().getDataManager().getData().getKeys(false).contains(targetPlayerUUID.toString()))
            {
                throw new PlayerNotFoundException(sender);
            }

            COfflinePlayer cOfflinePlayer = new COfflinePlayer(targetPlayerUUID);


            if (cOfflinePlayer.getPlayerClass().getName().equals("default"))
            {
                // Get default class name and send the player the message.
                sender.sendMessage(ChatColor.DARK_GRAY + cOfflinePlayer.toOfflinePlayer().getName() + ChatColor.YELLOW + " has the " +
                        ChatColor.DARK_GRAY + KingdomKits.getInstance().getConfig().getString("classes.defaultClass") +
                        ChatColor.YELLOW + " class");
            }
            else
            {
                // Send the player the message..
                sender.sendMessage(ChatColor.DARK_GRAY + targetPlayer + ChatColor.YELLOW + " has the " +
                        ChatColor.DARK_GRAY + cOfflinePlayer.getPlayerClass().getName() + ChatColor.YELLOW + " class");
            }
        }
        else
        {
            throw new TooManyArgumentsException(sender);
        }
    }
}
