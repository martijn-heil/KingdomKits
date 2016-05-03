package tk.martijn_heil.kingdomkits.util;


import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;
import tk.martijn_heil.kingdomkits.KingdomKits;
import tk.martijn_heil.kingdomkits.model.ItemCategory;

public class ItemCategories
{

    @Nullable
    public static ItemCategory getCategory(Material material)
    {
        for (ItemCategory category : KingdomKits.getInstance().getKkConfig().getItemCategories())
        {
            if(category.getItems().contains(material)) return category;
        }

        return null;
    }
}
