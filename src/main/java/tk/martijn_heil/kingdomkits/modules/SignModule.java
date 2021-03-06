package tk.martijn_heil.kingdomkits.modules;

import org.bukkit.Bukkit;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import tk.martijn_heil.kingdomkits.KingdomKits;
import tk.martijn_heil.kingdomkits.KingdomKitsSign;
import tk.martijn_heil.kingdomkits.model.COnlinePlayer;
import tk.martijn_heil.kingdomkits.model.PlayerClass;
import tk.martijn_heil.kingdomkits.util.SignActionType;
import tk.martijn_heil.kingdomkits.util.Signs;
import tk.martijn_heil.nincore.api.CoreModule;
import tk.martijn_heil.nincore.api.entity.NinOnlinePlayer;
import tk.martijn_heil.nincore.api.util.TranslationUtils;

import java.util.ResourceBundle;

/**
 * Handles all sign related operations and commands.
 */
public class SignModule extends CoreModule<KingdomKits> implements Listener
{

    public SignModule(KingdomKits core)
    {
        super(core);
    }


    @Override
    public void onEnable()
    {
        this.getLogger().info("Registering event handlers..");
        Bukkit.getPluginManager().registerEvents(this, this.getCore());
    }


    @Override
    public String getName()
    {
        return "SignModule";
    }


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e)
    {
        if(e.getClickedBlock() == null) return;
        if(!(e.getClickedBlock().getState() instanceof Sign)) return;

        Sign sign = (Sign) e.getClickedBlock().getState();

        if(!Signs.isKingdomKitsSign(sign)) return;

        KingdomKitsSign ks = new KingdomKitsSign(sign);
        NinOnlinePlayer np = NinOnlinePlayer.fromPlayer(e.getPlayer());

        if(ks.getSignActionType().equals(SignActionType.SET_CLASS))
        {
            if(!PlayerClass.PlayerClassExists(ks.getValue()))
            {
                np.sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs",
                        np.getLocale()), "commandError.invalidPlayerClass"));
                return;
            }

            PlayerClass pc = new PlayerClass(ks.getValue());
            COnlinePlayer cp = new COnlinePlayer(e.getPlayer());

            if(cp.hasPlayerClassSwitchCoolDownExpired())
            {
                cp.setPlayerClass(pc, true);
            }
            else
            {
                np.sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs",
                        np.getLocale()), "commandError.coolDownHasNotExpired"));
            }
        }
    }


    @EventHandler
    public void onSignChange(SignChangeEvent e)
    {
        if(e.getPlayer().hasPermission("kingdomkits.signs.create.switchclass") &&
                e.getLine(0).equalsIgnoreCase("[KingdomKits]"))
        {
            if(!SignActionType.isValidSignActionType(e.getLine(1)))
            {
                NinOnlinePlayer.fromPlayer(e.getPlayer()).sendError("Invalid sign action type.");
                return;
            }


            // Validation passed, make it a kingdomkits sign.
            e.setLine(0, Signs.getKingdomKitsPrefix());
        }
    }
}
