package me.brownie.inventoryrandomizer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class Events implements Listener {

    private InventoryRandomizer main;
    public void Events(InventoryRandomizer Main) {
        this.main = Main;
    }

    //   Adds a player to the GUI onJoin
    @EventHandler @Deprecated
    public void onJoin(PlayerJoinEvent e) {
        main.menu.addPlayers(true,main.CurrentCompassUser);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        //   Defines item
        ItemStack item = e.getItem();
        //   Checks if the item is the plugin's
        if (item.getType() != Material.CLOCK || !(item.getItemMeta().getDisplayName().equalsIgnoreCase("Inventory Randomizer"))
                || !(item.getItemMeta().hasLore())) return;
        //   Opens randomizer GUI
        e.getPlayer().openInventory(main.inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        //   Checks if its my inventory and its an item
        if (!e.getInventory().equals(main.inv) || e.getCurrentItem() == null || e.getCurrentItem().getItemMeta() == null
        || e.getCurrentItem().getItemMeta().getDisplayName() == null) return;
        //   Cancel the click event so the item isn't removed from the inventory
        e.setCancelled(true);
        //   Randomize the order of the items in the selected player's inventory
        main.Randomize(Bukkit.getPlayer(main.inv.getItem(e.getSlot()).getItemMeta().getDisplayName()));
    }
}
