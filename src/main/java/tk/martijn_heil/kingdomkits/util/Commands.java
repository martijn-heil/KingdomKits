package tk.martijn_heil.kingdomkits.util;


import org.bukkit.entity.Player;

public class Commands
{
    public static String parsePlayerVars(String s, Player p)
    {
        s = s.replace("{player_name}", p.getName());
        s = s.replace("{player_displayname}", p.getDisplayName());
        s = s.replace("{player_uuid}", p.getUniqueId().toString());

        return s;
    }
}
