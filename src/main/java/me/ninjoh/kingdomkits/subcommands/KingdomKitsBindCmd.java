package me.ninjoh.kingdomkits.subcommands;


import me.ninjoh.kingdomkits.exceptions.ItemAlreadySoulboundException;
import me.ninjoh.kingdomkits.util.ItemStackUtils;
import me.ninjoh.nincore.api.command.executors.NinSubCommandExecutor;
import me.ninjoh.nincore.api.exceptions.TechnicalException;
import me.ninjoh.nincore.api.exceptions.ValidationException;
import me.ninjoh.nincore.api.exceptions.validationexceptions.InvalidCommandSenderException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

public class KingdomKitsBindCmd extends NinSubCommandExecutor
{
    @Override
    public void execute(CommandSender sender, String[] strings) throws ValidationException, TechnicalException
    {
        if (!(sender instanceof Player)) throw new InvalidCommandSenderException(sender);

        // Make item in player's hand soulbound.
        Player p = (Player) sender;

        // Check if item isn't null
        if (p.getItemInHand() == null) return;

        // Check if item isn't already soulbound
        if (ItemStackUtils.isSoulBound(p.getItemInHand())) throw new ItemAlreadySoulboundException(sender);


        List<String> lore = Collections.singletonList("§6§oSoulbound");

        ItemStack is = p.getItemInHand();
        ItemMeta im = is.getItemMeta();

        im.setLore(lore);
        is.setItemMeta(im);


        sender.sendMessage(ChatColor.DARK_PURPLE + "§oMade item soulbound");
    }
}
