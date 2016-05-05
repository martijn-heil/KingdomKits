package tk.martijn_heil.kingdomkits.modules;

import org.bukkit.Bukkit;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import tk.martijn_heil.kingdomkits.KingdomKitsSign;
import tk.martijn_heil.kingdomkits.model.COnlinePlayer;
import tk.martijn_heil.kingdomkits.model.PlayerClass;
import tk.martijn_heil.kingdomkits.util.SignActionType;
import tk.martijn_heil.kingdomkits.util.Signs;
import tk.martijn_heil.nincore.api.Core;
import tk.martijn_heil.nincore.api.CoreModule;
import tk.martijn_heil.nincore.api.entity.NinOnlinePlayer;
import tk.martijn_heil.nincore.api.util.TranslationUtils;

import java.util.ResourceBundle;

/**
 * Handles all sign related operations and commands.
 */
public class SignModule extends CoreModule implements Listener
{

    public SignModule(Core core)
    {
        super(core);
    }


    @Override
    public void onEnable()
    {
        this.getLogger().info("Registering event handlers..");
        Bukkit.getPluginManager().registerEvents(this, this.getCore());
    }


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e)
    {
        if(e.getClickedBlock() == null) return;
        if(!(e.getClickedBlock() instanceof Sign)) return;
        Sign sign = (Sign) e.getClickedBlock();

        if(!Signs.isKingdomKitsSign(sign)) return;

        KingdomKitsSign ks = new KingdomKitsSign(sign);

        if(ks.getSignActionType().equals(SignActionType.SET_CLASS))
        {
            if(!PlayerClass.PlayerClassExists(ks.getValue()))
            {
                NinOnlinePlayer np = NinOnlinePlayer.fromPlayer(e.getPlayer());

                np.sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang",
                        np.getLocale()), "commandError.invalidPlayerClass"));
                return;
            }

            PlayerClass pc = new PlayerClass(ks.getValue());
            new COnlinePlayer(e.getPlayer().getUniqueId()).setPlayerClass(pc);
        }
    }

    @EventHandler
    public void onSignChange(SignChangeEvent e)
    {
        if(e.getPlayer().hasPermission("kingdomkits.signs.create.switchclass") &&
                e.getLine(0).equalsIgnoreCase("[KingdomKits]"))
        {
            if(!SignActionType.isValidSignActionType(e.getLine(0)))
            {
                NinOnlinePlayer.fromPlayer(e.getPlayer()).sendError("Invalid sign action type.");
                return;
            }


            // Validation passed, make it a kingdomkits sign.
            e.setLine(0, Signs.getKingdomKitsPrefix());
        }
    }
}
