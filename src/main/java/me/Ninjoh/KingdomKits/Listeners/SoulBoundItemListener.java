package me.Ninjoh.KingdomKits.Listeners;

import me.Ninjoh.KingdomKits.KingdomKits;
import me.Ninjoh.KingdomKits.Library.Entity.COnlinePlayer;
import me.Ninjoh.KingdomKits.Library.Util.ItemStackUtils;
import me.ninjoh.nincore.api.NinCore;
import me.ninjoh.nincore.api.entity.NinPlayer;
import me.ninjoh.nincore.api.events.ArmorEquipEvent;
import me.ninjoh.nincore.api.util.TranslationUtils;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;


public class SoulBoundItemListener implements Listener
{
    public static FileConfiguration config = KingdomKits.getInstance().getConfig();
    //public static FileConfiguration data = KingdomKits.data;

    public ItemStackUtils helperMethods = new ItemStackUtils();


    @EventHandler(priority = EventPriority.HIGHEST) // If player tries to drop a soulbound item..
    public void onPlayerDropItem(PlayerDropItemEvent e)
    {
        // If dropped item is soulbound, cancel the event.
        if (ItemStackUtils.isSoulBound(e.getItemDrop().getItemStack()))
        {
            e.setCancelled(true);

            NinPlayer np = NinCore.getImplementation().getNinPlayer(e.getPlayer());

            np.sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs",
                    np.getMinecraftLocale().toLocale()), "eventError.cancelledItemDrop"));
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST) // If player tries to put a soulbound piece of armor on an armor stand..
    public void onPlayerArmorStandManipulate(PlayerArmorStandManipulateEvent e)
    {
        if (ItemStackUtils.isSoulBound(e.getPlayerItem()))
        {
            e.setCancelled(true);

            NinPlayer np = NinCore.getImplementation().getNinPlayer(e.getPlayer());

            np.sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs",
                    np.getMinecraftLocale().toLocale()), "eventError.cancelledPutItemOnArmorStand"));
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST) // If player tries to put a soulbound item in an item frame..
    public void onPlayerInteractEntity(PlayerInteractEntityEvent e)
    {
        if (e.getRightClicked().getType().equals(EntityType.ITEM_FRAME) && ItemStackUtils.isSoulBound(e.getPlayer().getItemInHand()))
        {
            e.setCancelled(true);

            NinPlayer np = NinCore.getImplementation().getNinPlayer(e.getPlayer());

            np.sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs",
                    np.getMinecraftLocale().toLocale()), "eventError.cancelledPutItemInItemFrame"));
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST) // If player tries to put a soulbound item in another inventory..
    public void onInventoryClick(InventoryClickEvent e)
    {
        NinPlayer np = NinCore.getImplementation().getNinPlayer((Player) e.getWhoClicked());

        // Shift click an item from your inventory into the chest
        if (e.getClick().isShiftClick() &&
                !e.getInventory().getType().equals(InventoryType.PLAYER) &&
                !e.getInventory().getType().equals(InventoryType.CREATIVE) &&
                !e.getInventory().getType().equals(InventoryType.CRAFTING))
        {
            Inventory clicked = e.getClickedInventory();
            if (clicked == e.getWhoClicked().getInventory())
            {
                // The item is being shift clicked from the bottom to the top
                ItemStack clickedOn = e.getCurrentItem();

                if (clickedOn != null && (ItemStackUtils.isSoulBound(clickedOn)))
                {
                    e.setCancelled(true);

                    np.sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs",
                            np.getMinecraftLocale().toLocale()), "eventError.cancelledPutItemInInventory"));
                }
            }
        }


        // Click the item, and then click it into the slot in the chest
        Inventory clicked = e.getClickedInventory();
        if (clicked != e.getWhoClicked().getInventory())
        { // Note: !=
            // The cursor item is going into the top inventory
            ItemStack onCursor = e.getCursor();

            if (onCursor != null && (ItemStackUtils.isSoulBound(onCursor)))
            {
                e.setCancelled(true);

                final Locale locale = np.getMinecraftLocale().toLocale();
                final ResourceBundle errorMsgs = ResourceBundle.getBundle("lang.errorMsgs", locale);

                // If player tries to drop item by clicking outside of his inventory while dragging the item..
                // the PlayerDropItemEvent would cancel this aswell, but this keeps the item being dragged,
                // The PlayerDropItemEvent just puts the item back into the inventory, so this is a bit nicer..
                if(clicked == null)
                {
                    np.sendError(errorMsgs.getString("eventError.cancelledItemDrop"));
                }
                else
                {
                    np.sendError(errorMsgs.getString("eventError.cancelledPutItemInInventory"));
                }
            }
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST) // Click the item, and drag it inside the chest
    public void onInventoryDrag(InventoryDragEvent e)
    {
        NinPlayer np = NinCore.getImplementation().getNinPlayer((Player) e.getWhoClicked());

        ItemStack dragged = e.getOldCursor(); // This is the item that is being dragged

        if (ItemStackUtils.isSoulBound(dragged))
        {
            int inventorySize = e.getInventory().getSize(); // The size of the inventory, for reference

            // Now we go through all of the slots and check if the slot is inside our inventory (using the inventory size as reference)
            for (int i : e.getRawSlots())
            {
                if (i < inventorySize)
                {
                    e.setCancelled(true);

                    np.sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs"
                            , np.getMinecraftLocale().toLocale()), "eventError.cancelledPutItemInInventory"));
                    break;
                }
            }
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST) // Prevent soulbound items from being damaged upon use.
    public void onPlayerItemDamage(PlayerItemDamageEvent e)
    {
        // Soulbound items cannot be damaged
        if (ItemStackUtils.isSoulBound(e.getItem()) && config.getBoolean("soulbound.soulboundItemsCannotBeDamaged"))
        {
            e.setCancelled(true);
            e.getPlayer().updateInventory();
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST) // Give the player his class kit on respawn..
    public void onPlayerRespawn(PlayerRespawnEvent e)
    {
        // Configure essentials to give the kit for the first time a player joins the server. This handles the respawns (Very dodgy temporary solution).

        COnlinePlayer ninOnlinePlayer = new COnlinePlayer(e.getPlayer().getUniqueId());


        NinCore.getImplementation().getNinServer().dispatchCommand("essentials:kit " +
                ninOnlinePlayer.getPlayerClass().getKitName() + " " + e.getPlayer().getName());
    }


    @EventHandler(priority = EventPriority.HIGHEST) // If the player dies
    public void onPlayerDeath(PlayerDeathEvent e)
    {
        // Prevent soulbound items from being dropped on death..
        List<ItemStack> list = e.getDrops();
        Iterator<ItemStack> i = list.iterator();

        while (i.hasNext())
        {
            ItemStack item = i.next();
            if (ItemStackUtils.isSoulBound(item))
                i.remove();
        }
    }


    // If the player tries to equip non-soulbound armor..
    //(Player can get non-soulbound armor & weapons it via dungeons & mob drops)
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onArmorEquip(ArmorEquipEvent e)
    {
        if (e.getNewArmorPiece() != null &&
                !ItemStackUtils.isSoulBound(e.getNewArmorPiece()) &&
                !e.getNewArmorPiece().getType().equals(Material.AIR) &&
                (!e.getMethod().equals(ArmorEquipEvent.EquipMethod.DEATH) ||
                !e.getMethod().equals(ArmorEquipEvent.EquipMethod.BROKE)))
        {
            NinPlayer np = NinCore.getImplementation().getNinPlayer(e.getPlayer());

            np.sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs",
                    np.getMinecraftLocale().toLocale()), "eventError.cancelledEquip"));

            e.setCancelled(true);
            e.getPlayer().updateInventory();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e)
    {
        if (config.getBoolean("soulbound.preventNonSoulboundWeaponUsage") && e.getDamager() instanceof Player)
        {
            Player player = (Player) e.getDamager();

            // List of weapons
            List<Material> weapons = new ArrayList<>();

            weapons.add(Material.WOOD_SWORD);
            weapons.add(Material.STONE_SWORD);
            weapons.add(Material.GOLD_SWORD);
            weapons.add(Material.IRON_SWORD);
            weapons.add(Material.DIAMOND_SWORD);


            if (player.getItemInHand() != null && weapons.contains(player.getItemInHand().getType()) &&
                    !ItemStackUtils.isSoulBound(player.getItemInHand()))
            {
                e.setCancelled(true);

                NinPlayer np = NinCore.getImplementation().getNinPlayer(player);

                np.sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs",
                        np.getMinecraftLocale().toLocale()), "eventError.cancelledAttackWithWeapon"));
            }
        }



        if(config.getBoolean("soulbound.preventNonSoulboundAxeUsage") && e.getDamager() instanceof Player)
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
                    !ItemStackUtils.isSoulBound(player.getItemInHand()))
            {
                e.setCancelled(true);

                NinPlayer np = NinCore.getImplementation().getNinPlayer(player);

                np.sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs",
                        np.getMinecraftLocale().toLocale()), "eventError.cancelledAttackWithWeapon"));
            }
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST) // If player tries to shoot with a non-soulbound bow.
    public void onEntityShootBow(EntityShootBowEvent e)
    {
        if (config.getBoolean("soulbound.preventNonSoulboundWeaponUsage") &&
                e.getEntity() instanceof Player &&
                !ItemStackUtils.isSoulBound(e.getBow()))
        {
            Player player = (Player) e.getEntity();

            e.setCancelled(true);

            // Update player inventory so that the client doesn't think the arrow was used.
            player.updateInventory();

            NinPlayer np = NinCore.getImplementation().getNinPlayer(player);

            np.sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs",
                    np.getMinecraftLocale().toLocale()), "eventError.cancelledShotWithBow"));
        }
    }
}