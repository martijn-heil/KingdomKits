package me.Ninjoh.KingdomKits.Commands.SubCommands;


import me.Ninjoh.KingdomKits.Library.Exceptions.AccessDeniedException;
import me.Ninjoh.KingdomKits.Library.Exceptions.InvalidCommandSenderException;
import me.Ninjoh.KingdomKits.Library.Exceptions.ItemAlreadySoulboundException;
import me.Ninjoh.KingdomKits.Library.Util.HelperMethods;
import me.Ninjoh.NinCore.Library.Entity.Command;
import me.Ninjoh.NinCore.Library.Entity.SubCommand;
import me.Ninjoh.NinCore.Library.Interfaces.SubCommandExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

public class KingdomKitsBindCmd implements SubCommandExecutor
{

    @Override
    public void Handle(CommandSender sender, String[] args, Command command, SubCommand subCommand)
    {
        try
        {
            if (sender instanceof Player)
            {
                if (sender.hasPermission("kingdomkits.bind") || sender.isOp())
                {
                    // Make item in player's hand soulbound.
                    Player p = (Player) sender;

                    // Check if item isn't null
                    if (p.getItemInHand() == null)
                    {
                        return;
                    }

                    // Check if item isn't already soulbound
                    if (HelperMethods.isSoulBound(p.getItemInHand()))
                    {
                        throw new ItemAlreadySoulboundException(sender);
                    }

                    List<String> lore = Collections.singletonList("§6§oSoulbound");

                    ItemStack is = p.getItemInHand();
                    ItemMeta im = is.getItemMeta();

                    im.setLore(lore);
                    is.setItemMeta(im);


                    sender.sendMessage(ChatColor.DARK_PURPLE + "§oMade item soulbound");
                }
                else
                {
                    throw new AccessDeniedException(sender);
                }
            }
            else if(sender instanceof ConsoleCommandSender)
            {
                ConsoleCommandSender consoleCommandSender = (ConsoleCommandSender) sender;

                throw new InvalidCommandSenderException((CommandSender) consoleCommandSender);
            }
        }
        catch(InvalidCommandSenderException | AccessDeniedException | ItemAlreadySoulboundException e)
        {
            //
        }
    }
}
