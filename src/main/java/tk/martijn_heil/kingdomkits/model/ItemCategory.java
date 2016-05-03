package tk.martijn_heil.kingdomkits.model;


import lombok.Getter;
import org.bukkit.Material;

import java.util.List;

public class ItemCategory
{
    @Getter private final boolean useAllowedRequired;
    @Getter private final boolean combatAllowedRequired;
    @Getter private final boolean equipAllowedRequired;
    @Getter private final boolean mineAllowedRequired;
    @Getter private final boolean requireConsumeAllowed;
    @Getter private final boolean requireFireAllowed;

    @Getter private final List<Material> items;


    public ItemCategory(List<Material> items,
                        boolean useAllowedRequired,
                        boolean combatAllowedRequired,
                        boolean equipAllowedRequired,
                        boolean mineAllowedRequired,
                        boolean requireConsumeAllowed,
                        boolean requireFireAllowed
                        )
    {
        this.useAllowedRequired = useAllowedRequired;
        this.combatAllowedRequired = combatAllowedRequired;
        this.equipAllowedRequired = equipAllowedRequired;
        this.mineAllowedRequired = mineAllowedRequired;
        this.requireConsumeAllowed = requireConsumeAllowed;
        this.requireFireAllowed = requireFireAllowed;

        this.items = items;
    }
}
