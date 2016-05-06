package tk.martijn_heil.kingdomkits.util;


import com.google.common.base.Preconditions;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import tk.martijn_heil.kingdomkits.KingdomKits;
import tk.martijn_heil.kingdomkits.model.COnlinePlayer;
import tk.martijn_heil.kingdomkits.model.PlayerClass;

public class Players
{
    public static void populateData(@NotNull Player p)
    {
        Preconditions.checkNotNull(p);

        FileConfiguration data = KingdomKits.getInstance().getDataManager().getData();
        FileConfiguration config = KingdomKits.getInstance().getConfig();

        String playerUUID = p.getUniqueId().toString();
        String defaultClassName = config.getString("classes.defaultClass");


        if(!data.isSet(playerUUID)) // he's new.
        {
            KingdomKits.getInstance().getNinLogger().info("Creating new data entry for player: " + p.getName() + " (" + playerUUID + ")");

            data.set(playerUUID + ".class", defaultClassName);
            data.set(playerUUID + ".lastSeenName", p.getName());
        }


        if(!data.isSet(playerUUID + ".class"))
        {
            data.set(playerUUID + ".class", defaultClassName);
        }


        if(!data.isSet(playerUUID + ".lastSeenName"))
        {
            data.set(playerUUID + ".lastSeenName", p.getName());
        }


        // Update last seen name.
        data.set(playerUUID + ".lastSeenName", p.getName());

        // Check if player's class still exists
        if (!PlayerClass.PlayerClassExists(data.getString(playerUUID + ".class")))
        {
            // If player's class doesn't exist, put him back in the default class.
            COnlinePlayer cOnlinePlayer = new COnlinePlayer(p.getUniqueId());

            cOnlinePlayer.moveToDefaultPlayerClass();
        }
    }
}
