package tk.martijn_heil.kingdomkits.util;


import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Commands
{
    public static String parsePlayerVars(@NotNull String s, @NotNull Player p)
    {
        Preconditions.checkNotNull(s, "s can not be null.");
        Preconditions.checkNotNull(p, "p can not be null.");

        s = s.replace("{player_name}", p.getName());
        s = s.replace("{player_displayname}", p.getDisplayName());
        s = s.replace("{player_uuid}", p.getUniqueId().toString());

        return s;
    }
}
