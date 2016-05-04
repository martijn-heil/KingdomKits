package tk.martijn_heil.kingdomkits;


import com.google.common.base.Preconditions;
import lombok.Getter;
import org.bukkit.block.Sign;
import org.jetbrains.annotations.NotNull;
import tk.martijn_heil.kingdomkits.util.SignActionType;

public class KingdomKitsSign
{
    @Getter private Sign sign;
    @Getter private SignActionType signActionType;
    @Getter private String value;


    public KingdomKitsSign(@NotNull Sign sign)
    {
        Preconditions.checkNotNull(sign);

        this.sign = sign;
        this.signActionType = SignActionType.fromString(sign.getLine(1));
        this.value = sign.getLine(2);
    }
}
