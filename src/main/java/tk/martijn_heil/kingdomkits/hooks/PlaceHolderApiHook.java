package tk.martijn_heil.kingdomkits.hooks;


import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.external.EZPlaceholderHook;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import tk.martijn_heil.kingdomkits.model.COnlinePlayer;

public class PlaceHolderApiHook extends EZPlaceholderHook
{
    public PlaceHolderApiHook(Plugin plugin)
    {
        super(plugin, "kingdomkits");
    }


    @Override
    public String onPlaceholderRequest(Player player, String s)
    {
        switch (s)
        {
            case("class"):
                return new COnlinePlayer(player).getPlayerClass().getName();
        }

        return null;
    }

    public String parseString(Player p, String s)
    {
        return PlaceholderAPI.setPlaceholders(p, s);
    }
}
