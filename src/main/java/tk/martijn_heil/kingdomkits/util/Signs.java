package tk.martijn_heil.kingdomkits.util;


import com.google.common.base.Preconditions;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Signs
{
    public static boolean isKingdomKitsSign(@NotNull Sign s)
    {
        Preconditions.checkNotNull(s, "Sign can not be null.");
        return s.getLine(0).equals(ChatColor.GOLD + "KingdomKits");
    }


    @Contract(pure = true)
    public static boolean isKingdomKitsSign(@NotNull String firstLine)
    {
        Preconditions.checkNotNull(firstLine, "firstLine can not be null.");

        return firstLine.equals(ChatColor.GOLD + "KingdomKits");
    }


    @Contract(pure = true)
    public static String getKingdomKitsPrefix()
    {
        return ChatColor.GOLD + "KingdomKits";
    }
}
