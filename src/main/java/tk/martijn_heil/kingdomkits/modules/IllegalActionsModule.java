package tk.martijn_heil.kingdomkits.modules;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import tk.martijn_heil.kingdomkits.KingdomKits;
import tk.martijn_heil.kingdomkits.model.ItemCategory;
import tk.martijn_heil.kingdomkits.util.ItemCategories;
import tk.martijn_heil.kingdomkits.util.ItemStacks;
import tk.martijn_heil.nincore.api.CoreModule;
import tk.martijn_heil.nincore.api.entity.NinOnlinePlayer;
import tk.martijn_heil.nincore.api.events.ArmorEquipEvent;
import tk.martijn_heil.nincore.api.util.TranslationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Handles all illegal actions.
 * This may be stuff like disallowing elytra flight, disallowing using enderpearls, or prevent fighting with certain weapons.
 */
public class IllegalActionsModule extends CoreModule<KingdomKits> implements Listener
{

    public IllegalActionsModule(KingdomKits core)
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
        return "IllegalActionsModule";
    }


    @EventHandler(priority = EventPriority.HIGHEST) // If player tries to shoot with a non-soulbound bow.
    public void onEntityShootBow(EntityShootBowEvent e)
    {
        if (this.getCore().getConfig().getBoolean("soulbound.preventNonSoulboundWeaponUsage") &&
                e.getEntity() instanceof Player &&
                !ItemStacks.isSoulBound(e.getBow()))
        {
            Player player = (Player) e.getEntity();

            e.setCancelled(true);

            // Update player inventory so that the client doesn't think the arrow was used.
            player.updateInventory();

            NinOnlinePlayer np = NinOnlinePlayer.fromPlayer(player);

            np.sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs",
                    np.getMinecraftLocale().toLocale()), "eventError.cancelledShotWithBow"));
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e)
    {
        if (this.getCore().getConfig().getBoolean("soulbound.preventNonSoulboundWeaponUsage") && e.getDamager() instanceof Player)
        {
            Player player = (Player) e.getDamager();

            // List of weapons
            List<Material> weapons = new ArrayList<>();

            weapons.add(Material.WOOD_SWORD);
            weapons.add(Material.STONE_SWORD);
            weapons.add(Material.GOLD_SWORD);
            weapons.add(Material.IRON_SWORD);
            weapons.add(Material.DIAMOND_SWORD);


            if (player.getInventory().getItemInMainHand() != null && weapons.contains(player.getInventory().getItemInMainHand().getType()) &&
                    !ItemStacks.isSoulBound(player.getInventory().getItemInMainHand()))
            {
                e.setCancelled(true);

                NinOnlinePlayer np = NinOnlinePlayer.fromPlayer(player);

                np.sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs",
                        np.getMinecraftLocale().toLocale()), "eventError.cancelledAttackWithWeapon"));
            }
        }


        if(this.getCore().getConfig().getBoolean("soulbound.preventNonSoulboundAxeUsage") && e.getDamager() instanceof Player)
        {
            Player player = (Player) e.getDamager();

            // List of weapons
            List<Material> weapons = new ArrayList<>();

            weapons.add(Material.WOOD_AXE);
            weapons.add(Material.STONE_AXE);
            weapons.add(Material.GOLD_AXE);
            weapons.add(Material.IRON_AXE);
            weapons.add(Material.DIAMOND_AXE);


            if (player.getItemInHand() != null && weapons.contains(player.getItemInHand().getType()) &&
                    !ItemStacks.isSoulBound(player.getItemInHand()))
            {
                e.setCancelled(true);

                NinOnlinePlayer np = NinOnlinePlayer.fromPlayer(player);

                np.sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs",
                        np.getLocale()), "eventError.cancelledAttackWithWeapon"));
            }
        }

    }


    @EventHandler
    public void onEntityToggleGlide(EntityToggleGlideEvent e)
    {
        if(e.isGliding() && e.getEntity() instanceof Player && this.getCore().getConfig().getBoolean("preventElytra") &&
                !e.getEntity().hasPermission("kingdomkits.bypass.elytra"))
        {
            e.setCancelled(true);

            NinOnlinePlayer p = NinOnlinePlayer.fromPlayer((Player) e.getEntity());
            p.sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs", p.getLocale()),
                    "eventError.cancelledElytra"));
        }
    }


    // TODO
    @EventHandler(priority= EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent e)
    {
        List<String> items = this.getCore().getConfig().getStringList("crafting.blacklistedItems");

        if (e.getCurrentItem() != null && e.getInventory() != null)
        {

            // If clicked inventory is a WORKBENCH or CRAFTING inventory
            if (e.getInventory().getType().equals(InventoryType.WORKBENCH) ||
                    e.getInventory().getType().equals(InventoryType.CRAFTING))
            {
                if(e.getSlotType().equals(InventoryType.SlotType.RESULT) &&
                        items.contains(e.getCurrentItem().getType().toString()))
                {
                    e.setCancelled(true);
                    NinOnlinePlayer np = NinOnlinePlayer.fromPlayer((Player) e.getWhoClicked());

                    np.sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs",
                            np.getMinecraftLocale().toLocale()), "eventError.cancelledCraft"));
                }
            }
        }

        List<String> items2 = this.getCore().getConfig().getStringList("enchanting.blacklistedItems");

        if (e.getCurrentItem() != null && e.getInventory() != null)
        {

            // If clicked inventory is an anvil
            if (e.getInventory().getType().equals(InventoryType.ANVIL))
            {
                // Foreach blacklisted item list
                for (String item : items2)
                {
                    // Cast material string to material object.
                    Material itemMaterial = Material.getMaterial(item);

                    if (itemMaterial != null && e.getInventory().contains(itemMaterial) &&
                            e.getSlotType().equals(InventoryType.SlotType.RESULT))
                    {
                        // If clicked inventory is an anvil && anvil contains enchanted book + a blacklisted item, cancel the event..
                        e.setCancelled(true);

                        NinOnlinePlayer np = NinOnlinePlayer.fromPlayer((Player) e.getWhoClicked());

                        np.sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs",
                                np.getMinecraftLocale().toLocale()), "eventError.cancelledAnvilUse"));
                    }
                }
            }
        }
    }


    // TODO
    @EventHandler(priority= EventPriority.HIGHEST)
    public void onPlayerUseItem(PlayerInteractEvent e)
    {
        if(e.getItem() != null)
        {
            if(this.getCore().getConfig().getList("usage.blacklistedItems").contains(e.getItem().getType().toString()) &&
                    (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)))
            {
                e.setCancelled(true);
                e.getPlayer().updateInventory();

                NinOnlinePlayer np = NinOnlinePlayer.fromPlayer(e.getPlayer());

                np.sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs",
                        np.getMinecraftLocale().toLocale()), "eventError.cancelledItemUse"));
            }
        }
    }





    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerItemConsume(PlayerItemConsumeEvent e)
    {
        if(this.getCore().getConfig().getList("consume.blacklistedItems").contains(e.getItem().getType().toString())
                && !ItemStacks.isConsumeAllowed(e.getItem()))
        {
            e.setCancelled(true);

            NinOnlinePlayer np = NinOnlinePlayer.fromPlayer(e.getPlayer());
            Locale locale = np.getMinecraftLocale().toLocale();
            ResourceBundle errorMsgs = ResourceBundle.getBundle("lang.errorMsgs", locale);

            np.sendError(errorMsgs.getString("eventError.cancelledItemConsume"));
        }

        if(this.getCore().getConfig().getBoolean("potions.disablePotions"))
        {
            if(e.getItem().getType().equals(Material.POTION))
            {
                PotionMeta potion = (PotionMeta) e.getItem().getItemMeta();

                if(potion.getBasePotionData().getType() != PotionType.WATER)
                {
                    e.setCancelled(true);

                    NinOnlinePlayer np = NinOnlinePlayer.fromPlayer(e.getPlayer());

                    np.sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs",
                            np.getMinecraftLocale().toLocale()), "eventError.cancelledPotionDrink"));
                }
            }
        }
    }


    @EventHandler(priority= EventPriority.HIGHEST) // Prevent player enchanting soulbound items.
    public void onEnchantItem(EnchantItemEvent e)
    {
        List<String> items = this.getCore().getConfig().getStringList("enchanting.blacklistedItems");

        ItemStack enchantedItem = e.getItem();

        Material enchantedItemMaterial = enchantedItem.getType();

        if(items.contains(enchantedItemMaterial.toString()))
        {
            // Cancel enchant event if the item is blacklisted
            e.setCancelled(true);


            NinOnlinePlayer np = NinOnlinePlayer.fromPlayer(e.getEnchanter());

            np.sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs",
                    np.getMinecraftLocale().toLocale()), "eventError.cancelledEnchant"));
        }
    }


    @EventHandler(priority= EventPriority.HIGHEST)
    public void onBlockDispenseEvent(BlockDispenseEvent e)
    {
        if(this.getCore().getConfig().getBoolean("potions.disablePotions"))
        {
            if(e.getItem().getType().equals(Material.POTION))
            {
                e.setCancelled(true);
            }
        }
    }


    @EventHandler(priority= EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent e)
    {
        if(this.getCore().getConfig().getBoolean("potions.disablePotions"))
        {
            if(e.getItem() != null && e.getItem().getType().equals(Material.POTION) &&
                    (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)))
            {
                Potion potion = Potion.fromItemStack(e.getItem());

                if(potion.getLevel() != 0 && potion.isSplash())
                {
                    e.setCancelled(true);

                    NinOnlinePlayer np = NinOnlinePlayer.fromPlayer(e.getPlayer());

                    np.sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs",
                            np.getMinecraftLocale().toLocale()), "eventError.cancelledPotionThrow"));
                }

            }
        }
    }



    @EventHandler
    public void onUse(PlayerInteractEvent e)
    {
        if(!e.hasItem()) return;

        ItemCategory cat = ItemCategories.getCategory(e.getMaterial());

        if(cat != null && cat.isUseAllowedRequired() && !ItemStacks.isUseAllowed(e.getItem()))
        {
            e.setCancelled(true);

            // send the error.
            NinOnlinePlayer.fromPlayer(e.getPlayer()).
                    sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs"),
                            "eventError.cancelledItemUse"));
        }
    }


    @EventHandler
    public void onCombat(EntityDamageByEntityEvent e)
    {
        if(!(e.getDamager() instanceof Player)) return;
        if(!e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) return;

        ItemCategory cat = ItemCategories.getCategory(((Player) e.getDamager()).getItemInHand().getType());

        if(cat != null && cat.isCombatAllowedRequired() &&
                !ItemStacks.isCombatAllowed(((Player) e.getDamager()).getInventory().getItemInMainHand()))
        {
            e.setCancelled(true);

            // send the error.
            NinOnlinePlayer.fromPlayer((Player) e.getDamager()).
                    sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs"),
                            "eventError.cancelledAttackWithWeapon"));
        }
    }


    @EventHandler
    public void onEquip(ArmorEquipEvent e)
    {
        if(e.getNewArmorPiece() == null) return;
        ItemCategory cat = ItemCategories.getCategory(e.getNewArmorPiece().getType());

        if(cat != null && cat.isEquipAllowedRequired() && !ItemStacks.isEquipAllowed(e.getNewArmorPiece()) &&
                !e.getNewArmorPiece().getType().equals(Material.AIR) &&
                (!e.getMethod().equals(ArmorEquipEvent.EquipMethod.DEATH) ||
                        !e.getMethod().equals(ArmorEquipEvent.EquipMethod.BROKE)))
        {
            e.setCancelled(true);

            // send the error.
            NinOnlinePlayer.fromPlayer(e.getPlayer()).
                    sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs"),
                            "eventError.cancelledEquip"));
        }
    }


    @EventHandler
    public void onMine(BlockBreakEvent e)
    {
        ItemCategory cat = ItemCategories.getCategory(e.getPlayer().getInventory().getItemInMainHand().getType());

        if(cat != null && cat.isEquipAllowedRequired() && !ItemStacks.isEquipAllowed(e.getPlayer().getInventory().getItemInMainHand()))
        {
            e.setCancelled(true);

            // send the error.
            NinOnlinePlayer.fromPlayer(e.getPlayer()).
                    sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs"),
                            "eventError.cancelledMine"));
        }
    }


    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e)
    {
        ItemCategory cat = ItemCategories.getCategory(e.getItem().getType());

        if(cat != null && cat.isRequireConsumeAllowed() && !ItemStacks.isConsumeAllowed(e.getItem()))
        {
            e.setCancelled(true);

            // send the error.
            NinOnlinePlayer.fromPlayer(e.getPlayer()).
                    sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs"),
                            "eventError.cancelledItemConsume"));
        }
    }
}
