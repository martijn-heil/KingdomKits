package me.Ninjoh.KingdomKits.Listeners;

import com.massivecraft.factions.event.EventFactionsMembershipChange;
import me.Ninjoh.KingdomKits.KingdomKits;
import me.Ninjoh.KingdomKits.Library.Entity.COfflinePlayer;
import me.Ninjoh.KingdomKits.Library.Entity.PlayerClass;
import me.ninjoh.nincore.api.NinCore;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;


public class FactionEventsListener implements Listener
{
    public static FileConfiguration data = KingdomKits.getInstance().getDataManager().getData();
    public static FileConfiguration config = KingdomKits.getInstance().getConfig();


    @EventHandler
    public void onFactionsMembershipChange(EventFactionsMembershipChange e)
    {
        // If a player's class should not be reset to default on faction change.
        if(!config.getBoolean("soulbound.setPlayerClassToDefaultOnFactionChange"))
        {
            return;
        }

        Locale locale = NinCore.get().getDefaultMinecraftLocale().toLocale();

        if(e.getMPlayer().isOnline())
        {
            locale = NinCore.get().getNinPlayer(e.getMPlayer().getPlayer()).getLocale();
        }

        ResourceBundle messages = ResourceBundle.getBundle("lang.errorMsgs", locale);

        final MessageFormat formatter = new MessageFormat("");
        formatter.setLocale(locale);


        Object[] messageArguments = {PlayerClass.getDefaultPlayerClass().getName()};
        formatter.applyPattern(messages.getString("eventError.playerClassChangeDueToFactionChange"));
        String finalMessage = formatter.format(messageArguments);


        List<EventFactionsMembershipChange.MembershipChangeReason> invalidReasons = new ArrayList<>();

        invalidReasons.add(EventFactionsMembershipChange.MembershipChangeReason.LEADER);
        invalidReasons.add(EventFactionsMembershipChange.MembershipChangeReason.RANK);

        // If player doesn't have bypass permission & it's a valid reason.
        if(!invalidReasons.contains(e.getReason()) &&
                !e.getMPlayer().getPlayer().hasPermission("kingdomkits.bypass.factionmembershipchangeevent"))
        {
            // Change player's player class to default player class.
            COfflinePlayer ninOfflinePlayer = new COfflinePlayer(e.getMPlayer().getUuid());
            ninOfflinePlayer.moveToDefaultPlayerClass();

            if(ninOfflinePlayer.getOfflinePlayer().isOnline())
            {
                ninOfflinePlayer.getOfflinePlayer().getPlayer().sendMessage(finalMessage);
            }
        }
    }
}
