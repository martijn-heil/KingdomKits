package tk.martijn_heil.kingdomkits.modules;


import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import tk.martijn_heil.kingdomkits.util.ItemStacks;
import tk.martijn_heil.nincore.api.Core;
import tk.martijn_heil.nincore.api.CoreModule;
import tk.martijn_heil.nincore.api.entity.NinOnlinePlayer;
import tk.martijn_heil.nincore.api.util.TranslationUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Handles soulbound items, these items cannot be lost and are bound to the player.
 * soulbound items do not have any properties except for being bound to a player.
 */
public class SoulboundItemsModule extends CoreModule implements Listener
{

    public SoulboundItemsModule(Core core)
    {
        super(core);
    }


    @Override
    public void onEnable()
    {
        this.getLogger().info("Registering event handlers..");
        Bukkit.getPluginManager().registerEvents(this, this.getCore());
    }


    @EventHandler(priority = EventPriority.HIGHEST) // If player tries to put a soulbound item in an item frame..
    public void onPlayerInteractEntity(PlayerInteractEntityEvent e)
    {
        if (e.getRightClicked().getType().equals(EntityType.ITEM_FRAME) && ItemStacks.isSoulBound(
                e.getPlayer().getInventory().getItemInMainHand()))
        {
            e.setCancelled(true);

            NinOnlinePlayer np = NinOnlinePlayer.fromPlayer(e.getPlayer());

            np.sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs",
                    np.getMinecraftLocale().toLocale()), "eventError.cancelledPutItemInItemFrame"));
        }
    }


    // TODO Test & Fix
    @EventHandler(priority = EventPriority.HIGHEST) // If player tries to drop a soulbound item..
    public void onPlayerDropItem(PlayerDropItemEvent e)
    {
        // If dropped item is soulbound, cancel the event.
        if (ItemStacks.isSoulBound(e.getItemDrop().getItemStack()))
        {
            e.setCancelled(true);
            e.getPlayer().updateInventory();

            NinOnlinePlayer np = NinOnlinePlayer.fromPlayer(e.getPlayer());


            np.sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs",
                    np.getMinecraftLocale().toLocale()), "eventError.cancelledItemDrop"));
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST) // If player tries to put a soulbound item in another inventory..
    public void onInventoryClick(InventoryClickEvent e)
    {
        NinOnlinePlayer np = NinOnlinePlayer.fromPlayer((Player) e.getWhoClicked());

//        // Shift click an item from your inventory into the chest
//        if (e.getClick().isShiftClick() && e.getView().getTopInventory() != null)
//        {
//            Inventory clicked = e.getInventory();
//            if (clicked == e.getWhoClicked().getInventory())
//            {
//                // The item is being shift clicked from the bottom to the top
//                ItemStack clickedOn = e.getCurrentItem();
//
//                if (clickedOn != null && (ItemStacks.isSoulBound(clickedOn)))
//                {
//                    e.setCancelled(true);
//
//                    np.sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs",
//                            np.getMinecraftLocale().toLocale()), "eventError.cancelledPutItemInInventory"));
//                }
//            }
//        }

        if(e.getCurrentItem() != null && e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY &&
                ItemStacks.isSoulBound(e.getCurrentItem()) &&
                e.getInventory().getType() != InventoryType.PLAYER &&
                e.getInventory().getType() != InventoryType.CRAFTING &&
                e.getInventory().getType() != InventoryType.CREATIVE)
        {
            e.setCancelled(true);

            np.sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs",
                    np.getMinecraftLocale().toLocale()), "eventError.cancelledPutItemInInventory"));
        }


        // Click the item, and then click it into the slot in the chest
        Inventory clicked = e.getClickedInventory();
        if ((clicked == null) ||
                (clicked.getType() != InventoryType.PLAYER &&
                clicked.getType() != InventoryType.CREATIVE &&
                clicked.getType() != InventoryType.CRAFTING))
        {
            // The cursor item is going into the top inventory
            ItemStack onCursor = e.getCursor();

            if (onCursor != null && (ItemStacks.isSoulBound(onCursor)))
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




        if (e.getCurrentItem() != null && e.getInventory() != null)
        {
            boolean cancel = false;
            for (ItemStack i : e.getInventory().getContents())
            {
                if(i != null && ItemStacks.isSoulBound(i))
                {
                    cancel = true;
                    break;
                }
            }


            // If clicked inventory is a WORKBENCH or CRAFTING inventory
            if (e.getInventory().getType().equals(InventoryType.WORKBENCH) ||
                    e.getInventory().getType().equals(InventoryType.CRAFTING))
            {
                if(e.getSlotType().equals(InventoryType.SlotType.RESULT) && cancel)
                {
                    e.setCancelled(true);
                    NinOnlinePlayer np2 = NinOnlinePlayer.fromPlayer((Player) e.getWhoClicked());

                    np2.sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs",
                            np2.getMinecraftLocale().toLocale()), "eventError.cancelledSoulboundCraft"));
                }
            }
        }






        if(e.getCurrentItem() != null && ItemStacks.isSoulBound(e.getCurrentItem()) &&
                ((e.getClick() == ClickType.DROP || e.getClick() == ClickType.CONTROL_DROP)))
        {
            e.setCancelled(true);
            np.toPlayer().updateInventory();

            np.sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs",
                    np.getMinecraftLocale().toLocale()), "eventError.cancelledItemDrop"));
        }
//        else if(ItemStacks.isSoulBound(e.getCursor()) && e.getSlotType() == InventoryType.SlotType.OUTSIDE &&
//                e.getClickedInventory() == null)
//        {
//            e.setCancelled(true);
//            ((Player) e.getWhoClicked()).updateInventory();
//            e.getWhoClicked().setItemOnCursor(e.getCursor());
//
//            np.sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs",
//                    np.getMinecraftLocale().toLocale()), "eventError.cancelledItemDrop"));
//        }


        // If player tries to drop item by clicking outside of his inventory while dragging the item..
        // the PlayerDropItemEvent would cancel this aswell, but this keeps the item being dragged,
        // The PlayerDropItemEvent just puts the item back into the inventory, so this is a bit nicer..
//        if(ItemStacks.isSoulBound(e.getCursor()) && (e.getSlotType() == InventoryType.SlotType.OUTSIDE))
//        {
//            e.setCancelled(true);
//            ((Player) e.getWhoClicked()).updateInventory();
//            e.getWhoClicked().setItemOnCursor(e.getCursor());
//
//            np.sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs",
//                    np.getMinecraftLocale().toLocale()), "eventError.cancelledItemDrop"));
//        }

//        if(ItemStacks.isSoulBound(e.getCursor()) && e.getSlotType() == InventoryType.SlotType.OUTSIDE &&
//                e.getClickedInventory() == null)
//        {
//
//        }
    }


    @EventHandler(priority = EventPriority.HIGHEST) // Click the item, and drag it inside the chest
    public void onInventoryDrag(InventoryDragEvent e)
    {
        NinOnlinePlayer np = NinOnlinePlayer.fromPlayer((Player) e.getWhoClicked());

        ItemStack dragged = e.getOldCursor(); // This is the item that is being dragged

//        if (ItemStacks.isSoulBound(dragged))
//        {
//            int inventorySize = e.getInventory().getSize(); // The size of the inventory, for reference
//
//            // Now we go through all of the slots and check if the slot is inside our inventory (using the inventory size as reference)
//            for (int i : e.getRawSlots())
//            {
//                if (i < inventorySize)
//                {
//                    e.setCancelled(true);
//
//                    np.sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs"
//                            , np.getMinecraftLocale().toLocale()), "eventError.cancelledPutItemInInventory"));
//                    break;
//                }
//            }
//        }
        InventoryType type = e.getInventory().getType();

        if(ItemStacks.isSoulBound(dragged) &&
                type != InventoryType.CREATIVE &&
                type != InventoryType.PLAYER &&
                type != InventoryType.CRAFTING)
        {
            e.setCancelled(true);

                    np.sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs"
                            , np.getMinecraftLocale().toLocale()), "eventError.cancelledPutItemInInventory"));
        }
    }


//    @EventHandler
//    public void onInventoryClick(InventoryClickEvent e)
//    {
//
//    }


    @EventHandler // Soulbound items can not be put in any inventory.
    public void onInventoryMoveItem(InventoryMoveItemEvent e)
    {
        InventoryHolder holder = e.getInitiator().getHolder();

        if(holder instanceof Player)
        {
            Player pHolder = (Player) holder;
            NinOnlinePlayer npHolder = NinOnlinePlayer.fromPlayer(pHolder);

            if(e.getDestination() != pHolder.getInventory())
            {
                e.setCancelled(true);
                npHolder.toPlayer().updateInventory();

                npHolder.sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs"
                        , npHolder.getMinecraftLocale().toLocale()), "eventError.cancelledPutItemInInventory"));
            }
        }
    }


    @EventHandler // soulbound items can not be used in crafting recipes.
    public void onPrepareItemCraft(CraftItemEvent e)
    {
        boolean containsSoulboundItem = false;
        for (ItemStack i : e.getInventory().getContents())
        {
            if(i != null && ItemStacks.isSoulBound(i))
            {
                containsSoulboundItem = true;
                break; // don't waste time continuing to iterate till the end.
            }
        }

        if(containsSoulboundItem)
        {
            e.setCancelled(true);

            NinOnlinePlayer np = NinOnlinePlayer.fromPlayer((Player) e.getWhoClicked());
            np.toPlayer().updateInventory();

            np.sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs",
                            np.getMinecraftLocale().toLocale()), "eventError.cancelledSoulboundCraft"));
        }
    }


    // If the player dies, all soulbound drops should be removed from his death drops.
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent e)
    {
        // Prevent soulbound items from being dropped on death..
        List<ItemStack> list = e.getDrops();
        Iterator<ItemStack> i = list.iterator();

        while (i.hasNext())
        {
            ItemStack item = i.next();
            if (ItemStacks.isSoulBound(item))
                i.remove();
        }
    }


    // Soulbound items should not be put on any armor stand.
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerArmorStandManipulate(PlayerArmorStandManipulateEvent e)
    {
        if (e.getPlayerItem() != null && ItemStacks.isSoulBound(e.getPlayerItem()))
        {
            e.setCancelled(true);

            NinOnlinePlayer np = NinOnlinePlayer.fromPlayer(e.getPlayer());

            np.sendError(TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs",
                    np.getMinecraftLocale().toLocale()), "eventError.cancelledPutItemOnArmorStand"));
        }
    }
}
