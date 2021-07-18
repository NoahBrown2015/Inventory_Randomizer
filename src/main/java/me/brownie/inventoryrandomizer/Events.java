package me.brownie.inventoryrandomizer;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class Events implements Listener {

    private InventoryRandomizer main;
    public void Events(InventoryRandomizer Main) {
        this.main = Main;
    }

    //   Adds a player to the GUI onJoin
    @EventHandler
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


}
