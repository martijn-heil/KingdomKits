package tk.martijn_heil.kingdomkits.listeners;

import com.massivecraft.factions.event.EventFactionsMembershipChange;
import tk.martijn_heil.kingdomkits.KingdomKits;
import tk.martijn_heil.kingdomkits.model.COfflinePlayer;
import tk.martijn_heil.kingdomkits.model.PlayerClass;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import tk.martijn_heil.nincore.api.NinCore;
import tk.martijn_heil.nincore.api.entity.NinOnlinePlayer;

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
        if(!config.getBoolean("classes.setPlayerClassToDefaultOnFactionChange"))
        {
            return;
        }

        Locale locale = NinCore.get().getLocalizationManager().getDefaultMinecraftLocale().toLocale();

        if(e.getMPlayer().isOnline())
        {
            locale = NinOnlinePlayer.fromPlayer(e.getMPlayer().getPlayer()).getLocale();
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

            if(ninOfflinePlayer.toOfflinePlayer().isOnline())
            {
                ninOfflinePlayer.toOfflinePlayer().getPlayer().sendMessage(finalMessage);
            }
        }
    }
}
